package sokolov.model.common;

import it.geosolutions.jaiext.range.Range;
import sokolov.model.datasets.RasterService;
import sokolov.model.datasets.VrtRasterBand;
import sokolov.model.xmlmodel.RectType;
import sokolov.model.xmlmodel.SourceFilenameType;
import sokolov.model.xmlmodel.VRTRasterBandType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.awt.image.DataBuffer.*;

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


    public static boolean checkRectContains(int x, int y, RectType rectType){
        if (x >= rectType.getxOff() && x <= rectType.getxOff() + rectType.getxSize())
            if (y >= rectType.getyOff() && y <= rectType.getyOff() + rectType.getySize())
                return true;

        return false;
    }

    public static String getType(int dataType) {
        switch (dataType){
            case TYPE_BYTE:
                return "byte";
            case TYPE_USHORT:
            case DataBuffer.TYPE_SHORT:
                return "short";
            case DataBuffer.TYPE_DOUBLE:
                return "double";
            case DataBuffer.TYPE_FLOAT:
                return "float";
            case DataBuffer.TYPE_INT:
                return "int";
            case DataBuffer.TYPE_UNDEFINED:
                throw new RuntimeException("Невозможно узнать тип пикселя растрового изображения");
        }

        throw new RuntimeException("Невозможно узнать тип пикселя растрового изображения");
    }

    public static int getResultedImageType(List<VRTRasterBandType> vrtRasterBandList) {
        String type = null;
        int codeType = -1;


        for (VRTRasterBandType vrtRasterBand : vrtRasterBandList) {
            if (vrtRasterBand.getDataType().getValue().equals("Byte") && codeType < 0) {
                type = "byte";
                codeType = 0;
            }

            if (vrtRasterBand.getDataType().getValue().equals("UInt16") && codeType < 1){
                type = "short";
                codeType = 1;
            }

            if (vrtRasterBand.getDataType().getValue().equals("Int16") && codeType < 1){
                type = "short";
                codeType = 1;
            }

            if (vrtRasterBand.getDataType().getValue().equals("UInt32") && codeType < 2){
                type = "int";
                codeType = 2;
            }

            if (vrtRasterBand.getDataType().getValue().equals("Int32") && codeType < 2){
                type = "int";
                codeType = 2;
            }

            if (vrtRasterBand.getDataType().getValue().equals("Float32") && codeType < 3){
                type = "float";
                codeType = 3;
            }

            if (vrtRasterBand.getDataType().getValue().equals("Float64")  && codeType < 3){
                type = "float";
                codeType = 3;
            }

            if (vrtRasterBand.getDataType().getValue().equals("CInt16") && codeType < 2){
                type = "int";
                codeType = 2;
            }

            if (vrtRasterBand.getDataType().getValue().equals("CInt32") && codeType < 2){
                type = "int";
                codeType = 2;
            }

            if (vrtRasterBand.getDataType().getValue().equals("CFloat32 ") && codeType < 3){
                type = "float";
                codeType = 3;
            }

            if (vrtRasterBand.getDataType().getValue().equals("CFloat64") && codeType < 3){
                type = "float";
                codeType = 3;
            }
        }

        switch (type){
            case "byte":
                if (vrtRasterBandList.size() == 1)
                    return BufferedImage.TYPE_BYTE_GRAY;
                else
                    return BufferedImage.TYPE_INT_RGB;
            case "short":
                if (vrtRasterBandList.size() == 1)
                    return BufferedImage.TYPE_USHORT_GRAY;
                else
                    return BufferedImage.TYPE_USHORT_555_RGB;
            case "int":
                if (vrtRasterBandList.size() == 1)
                    return BufferedImage.TYPE_INT_BGR;
                else
                    return BufferedImage.TYPE_INT_RGB;
            case "float":
            case "double":
                if (vrtRasterBandList.size() == 1)
                    return BufferedImage.TYPE_4BYTE_ABGR;
                else
                    return BufferedImage.TYPE_4BYTE_ABGR;
        }

        return BufferedImage.TYPE_CUSTOM;
    }

    public static int getRasterImageType(int value) {
        switch (value){
            case BufferedImage.TYPE_USHORT_GRAY:
                return TYPE_USHORT;
            case BufferedImage.TYPE_4BYTE_ABGR:
                return TYPE_INT;
            case BufferedImage.TYPE_INT_RGB:
                return TYPE_BYTE;
            case BufferedImage.TYPE_BYTE_GRAY:
                return TYPE_BYTE;
            case BufferedImage.TYPE_USHORT_555_RGB:
                return TYPE_USHORT;
        }

        return 0;
    }
}
