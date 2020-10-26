package sokolov.model.common;

import it.geosolutions.jaiext.range.Range;
import sokolov.model.datasets.RasterService;
import sokolov.model.xmlmodel.SourceFilenameType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.awt.image.DataBuffer.TYPE_BYTE;
import static java.awt.image.DataBuffer.TYPE_USHORT;

public class XmlDeserializer {
    public static String getPathForSourceFileName(SourceFilenameType sourceFilenameType, String pathToVrt) {
        Path path;

        if (sourceFilenameType == null || sourceFilenameType.getSourceFileName() == null)
            throw new RuntimeException("Неверное задан тэг SourceFilename");

        if (sourceFilenameType.getRelativetoVRT() == 1) {
            path = Paths.get(pathToVrt, sourceFilenameType.getSourceFileName());
        } else {
            path = Paths.get(sourceFilenameType.getSourceFileName());
        }

        if (!path.toFile().exists())
            throw new RuntimeException("Отсутствует указанный в VRT файл");

        return path.toString();
    }

    /*public static void writeSpectralBandList(List<byte[]> spectralBandList, int nRasterXSize, int nRasterYSize) throws IOException {
        WritableRaster interleavedRaster = Raster.createBandedRaster(DataBuffer.TYPE_BYTE, nRasterXSize, nRasterYSize, 3, null);


        BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_INT_RGB);


        //BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_CUSTOM);

        //WritableRaster interleavedRaster = colorModel.createCompatibleWritableRaster(nRasterXSize, nRasterYSize);
        SampleModel model = interleavedRaster.getSampleModel();

        for (int y = 0; y < nRasterYSize; y++) {
            for (int x = 0; x < nRasterXSize; x++) {


                int realNumber = 0;
                for (byte[] resampledArray : spectralBandList) {
                    model.setSample(x, y, realNumber++,
                            RasterService.getSingleBandValue(resampledArray,
                                    x, y, nRasterXSize, nRasterYSize),
                            interleavedRaster.getDataBuffer());
                }
            }
        }

        bufferedImage.getRaster().setRect(interleavedRaster);

        ImageIO.write(bufferedImage, "tiff", new File(String.format("testafterresample.tiff")));
    }*/

    public static void writeSpectralBandList(List<int[]> spectralBandList, int nRasterXSize, int nRasterYSize) throws IOException {

        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

        WritableRaster interleavedRaster = Raster.createBandedRaster(TYPE_USHORT, nRasterXSize, nRasterYSize, 3, null);
        BufferedImage bufferedImage = new BufferedImage(colorModel, interleavedRaster, false, null);
        SampleModel model = interleavedRaster.getSampleModel();

        for (int band = 0; band < 3; band++) {
            int[] nearests = spectralBandList.get(band);

            for (int i = 0; i < nearests.length; i++) {
                int x = i % nRasterXSize;
                int y = i / nRasterXSize;

                model.setSample(x, y, band, nearests[i], interleavedRaster.getDataBuffer());
            }
        }

        bufferedImage.getRaster().setRect(interleavedRaster);

        ImageIO.write(bufferedImage, "tiff", new File(String.format("zxcv.tiff")));
    }
}
