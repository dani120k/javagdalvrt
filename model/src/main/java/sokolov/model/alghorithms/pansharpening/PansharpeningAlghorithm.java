package sokolov.model.alghorithms.pansharpening;

import sokolov.model.alghorithms.FileOpenService;
import sokolov.model.alghorithms.ResamplingAlghorithmExecutor;
import sokolov.model.common.PixelValue;
import sokolov.model.common.XmlDeserializer;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.datasets.RasterService;
import sokolov.model.xmlmodel.PanchroBandType;
import sokolov.model.xmlmodel.PansharpeningOptionsType;
import sokolov.model.xmlmodel.SpectralBandType;
import sokolov.model.xmlmodel.VRTDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.DataBuffer.*;

public class PansharpeningAlghorithm {

    public void executePansharpening(GdalDataset gdalDataset, String pathToVrt, VRTDataset vrtDataset) throws IOException {
        PansharpeningOptionsType pansharpeningOptions = vrtDataset.getPansharpeningOptions();

        if (pansharpeningOptions != null) {
            //get pachroband
            PanchroBandType panchroBand = pansharpeningOptions.getPanchroBand();

            String type = "int";
            BufferedImage pachroImage = ImageIO.read(Paths.get(XmlDeserializer.getPathForSourceFileName(panchroBand.getSourceFilename(), pathToVrt)).toFile());

            //get each spectral band
            List<SpectralBandType> spectralBand = pansharpeningOptions.getSpectralBand();
            List<BufferedImage> spectralBands = new ArrayList<>();

            if (spectralBand == null || spectralBand.size() == 0)
                throw new RuntimeException("Не указаны спектральные band-ы для операции pansharpening");

            for (SpectralBandType spectralBandType : spectralBand) {
                spectralBands.add(ImageIO.read(Paths.get(XmlDeserializer.getPathForSourceFileName(spectralBandType.getSourceFilename(), pathToVrt)).toFile()));

                //spectralBands.add(FileOpenService.openFile(Paths.get(XmlDeserializer.getPathForSourceFileName(spectralBandType.getSourceFilename(), pathToVrt), pathToVrt).toFile()));
                //BufferedImage image =
            }

            System.out.println();

            //calculate pansharpening result

            BufferedImage pansharpenedImage = pansharpening(pachroImage, spectralBands, type);

            //ImageIO.write(pansharpenedImage, "tif", Paths.get("pansdf.tif").toFile());

            gdalDataset.bufferedImage = pansharpenedImage;
        } else {
            throw new RuntimeException("Указан subClass VRTPansharpenedDataset, но не указаны параметры pansharpening (тэг <PansharpeningOptions>)");
        }
    }

    private BufferedImage pansharpening(BufferedImage pachroImage,
                                        List<BufferedImage> spectralBands,
                                        String type) throws IOException {
        int nRasterYSize = pachroImage.getRaster().getHeight();
        int nRasterXSize = pachroImage.getRaster().getWidth();

        int bN = 1;
        List<PixelValue[]> spectralBandList = new ArrayList<>();

        for (BufferedImage spectralBand : spectralBands) {
            int spectralRasterXSize = spectralBand.getWidth();
            int spectralRasterYSize = spectralBand.getHeight();

            ResamplingAlghorithmExecutor resamplingAlghorithmExecutor = new ResamplingAlghorithmExecutor();
            spectralBandList.add(resamplingAlghorithmExecutor.imageRescaling(bN++,
                    0, 0, spectralRasterXSize, spectralRasterYSize,
                    0, 0, nRasterXSize, nRasterYSize,
                    -1000000,
                    type,
                    spectralBand,
                    "nearest"
            ));
        }

        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

        WritableRaster interleavedRaster = Raster.createBandedRaster(TYPE_USHORT, nRasterXSize, nRasterYSize, 3, null);
        BufferedImage bufferedImage = new BufferedImage(colorModel, interleavedRaster, false, null);
        SampleModel model = interleavedRaster.getSampleModel();

        for (int x = 0; x < nRasterXSize; x++) {
            for (int y = 0; y < nRasterYSize; y++) {
                double psevdo = 0;

                int bandNumber = 1;
                for (int w = 0; w < spectralBandList.size(); w++) {
                    double weight = 0;
                    if (w == 0)
                        weight = 0.52;
                    else if (w == 1)
                        weight = 0.25;
                    else if (w == 2)
                        weight = 0.22;

                    psevdo += weight * RasterService.getSingleBandValue(spectralBandList.get(w),
                            x, y, nRasterXSize, nRasterYSize);
                }

                double ratio = 0.0;

                if (psevdo != 0.0)
                    ratio = RasterService.getValue(1, x, y, pachroImage.getRaster()) / (1.0 * psevdo);

                int realNumber = 0;
                boolean isInvalid = false;
                for (PixelValue[] resampledArray : spectralBandList) {
                    if (ratio == 0.0) {
                        model.setSample(x, y, realNumber++,
                                0,
                                interleavedRaster.getDataBuffer());
                    } else {
                        if (RasterService.getSingleBandValue(resampledArray,
                                x, y, nRasterXSize, nRasterYSize) * ratio > 65536)
                            isInvalid = true;

                        model.setSample(x, y, realNumber++,
                                RasterService.getSingleBandValue(resampledArray,
                                        x, y, nRasterXSize, nRasterYSize) * ratio,
                                interleavedRaster.getDataBuffer());
                    }
                }

                if (isInvalid) {
                    model.setSample(x, y, 0,
                            PixelValue.getMaxForType(type),
                            interleavedRaster.getDataBuffer());
                    model.setSample(x, y, 1,
                            PixelValue.getMaxForType(type),
                            interleavedRaster.getDataBuffer());
                    model.setSample(x, y, 2,
                            PixelValue.getMaxForType(type),
                            interleavedRaster.getDataBuffer());
                }
            }
        }

        bufferedImage.getRaster().setRect(interleavedRaster);

        //ImageIO.write(bufferedImage, "tiff", new File(String.format("pansharped.tiff")));

        return bufferedImage;
    }
}