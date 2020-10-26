package sokolov.model.alghorithms.pansharpening;

import sokolov.model.alghorithms.ResamplingAlghorithmExecutor;
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

    public void executePansharpening(GdalDataset GdalDataset, String pathToVrt, VRTDataset vrtDataset) throws IOException {
        PansharpeningOptionsType pansharpeningOptions = vrtDataset.getPansharpeningOptions();

        if (pansharpeningOptions != null) {
            //get pachroband
            PanchroBandType panchroBand = pansharpeningOptions.getPanchroBand();

            BufferedImage pachroImage = ImageIO.read(Paths.get(XmlDeserializer.getPathForSourceFileName(panchroBand.getSourceFilename(), pathToVrt)).toFile());

            //get each spectral band
            List<SpectralBandType> spectralBand = pansharpeningOptions.getSpectralBand();
            List<BufferedImage> spectralBands = new ArrayList<>();

            if (spectralBand == null || spectralBand.size() == 0)
                throw new RuntimeException("Не указаны спектральные band-ы для операции pansharpening");

            for (SpectralBandType spectralBandType : spectralBand) {
                spectralBands.add(ImageIO.read(Paths.get(XmlDeserializer.getPathForSourceFileName(spectralBandType.getSourceFilename(), pathToVrt)).toFile()));
            }

            System.out.println();

            //calculate pansharpening result

            BufferedImage pansharpenedImage = pansharpening(pachroImage, spectralBands);

            ImageIO.write(pansharpenedImage, "tif", Paths.get("pansdf.tif").toFile());
        } else {
            throw new RuntimeException("Указан subClass VRTPansharpenedDataset, но не указаны параметры pansharpening (тэг <PansharpeningOptions>)");
        }
    }

    private BufferedImage pansharpening(BufferedImage pachroImage,
                                        List<BufferedImage> spectralBands) throws IOException {
        int spectralCount = 3;//TODO

        int nRasterYSize = pachroImage.getRaster().getHeight();
        int nRasterXSize = pachroImage.getRaster().getWidth();

        int bN = 1;
        List<int[]> spectralBandList = new ArrayList<>();

        for (BufferedImage spectralBand : spectralBands) {
            int spectralRasterXSize = spectralBand.getWidth();
            int spectralRasterYSize = spectralBand.getHeight();

            ResamplingAlghorithmExecutor resamplingAlghorithmExecutor = new ResamplingAlghorithmExecutor();
            spectralBandList.add(resamplingAlghorithmExecutor.imageRescalingInt(bN++,
                    0, 0, spectralRasterXSize, spectralRasterYSize,
                    0, 0, nRasterXSize, nRasterYSize,
                    -1000000,
                    spectralBand,
                    "nearest"
            ));


        }

        XmlDeserializer.writeSpectralBandList(spectralBandList, nRasterXSize, nRasterYSize);

        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

        //BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_USHORT_565_RGB);


        //BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_CUSTOM);
        //WritableRaster interleavedRaster = Raster.create(DataBuffer.TYPE_BYTE, nRasterXSize, nRasterYSize, 3, null);
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
                //psevdo += (1/4.0) * RasterService.getValue(1, x, y, pachroImage.getRaster());

                //psevdoPachro = 1/3* spectral[x,y][1] + 1/3* spectral[x,y][2] + 1/3 * spectral[x,y][3];

                double ratio = 0.0;

                //if (psevdo == 0.0)
                //  psevdo = 1.0;

                if (psevdo != 0.0)
                    ratio = RasterService.getValue(1, x, y, pachroImage.getRaster()) / (1.0 * psevdo);


                //ratio = panchro[x,y] / psevdoPachro;

                int realNumber = 0;
                boolean isInvalid = false;
                for (int[] resampledArray : spectralBandList) {
                    /*model.setSample(x, y, realNumber++,
                            Math.abs(RasterService.getSingleBandValue(resampledArray,
                                    x, y, nRasterXSize, nRasterYSize) * ratio) %256 ,
                            interleavedRaster.getDataBuffer());*/
                    System.out.println(x + " " + y);
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
                            (short)(255* 65535),
                            interleavedRaster.getDataBuffer());
                    model.setSample(x, y, 1,
                            (short)(255* 65535),
                            interleavedRaster.getDataBuffer());
                    model.setSample(x, y, 2,
                            (short)(255* 65535),
                            interleavedRaster.getDataBuffer());
                }

                //output[x,y][1] = spectral[x,y][1] * ratio;
                //output[x,y][2] = spectral[x,y][2] * ratio;
                //output[x,y][3] = spectral[x,y][3] * ratio;
            }
        }


        /*for (int x = 0; x < nRasterXSize; x++){
            for(int y = 0; y < nRasterYSize; y++){
                psevdoPachro = 1/3* spectral[x,y][1] + 1/3* spectral[x,y][2] + 1/3 * spectral[x,y][3];

                ratio = panchro[x,y] / psevdoPachro;

                output[x,y][1] = spectral[x,y][1] * ratio;
                output[x,y][2] = spectral[x,y][2] * ratio;
                output[x,y][3] = spectral[x,y][3] * ratio;
            }
        }*/

        bufferedImage.getRaster().setRect(interleavedRaster);

        ImageIO.write(bufferedImage, "tiff", new File(String.format("pansharped.tiff")));

        return bufferedImage;
    }
}