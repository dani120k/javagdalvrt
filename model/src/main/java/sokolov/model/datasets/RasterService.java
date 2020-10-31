package sokolov.model.datasets;

import sokolov.model.common.PixelValue;

import java.awt.image.Raster;

public class RasterService {
    public static int getValue(int bandNumber,
                           int x, int y,
                           Raster raster){
        int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

        return pixel[bandNumber-1];
    }

    public static byte getByteValue(int bandNumber,
                               int x, int y,
                               Raster raster){
        int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

        return (byte)pixel[bandNumber-1];
    }

    public static byte getSingleBandValue(byte[] resampledArray, int x, int y, int nRasterXSize, int nRasterYSize) {
        return resampledArray[x + y * nRasterXSize];
    }

    public static int getSingleBandValue(int[] resampledArray, int x, int y, int nRasterXSize, int nRasterYSize) {
        return resampledArray[x + y * nRasterXSize];
    }

    public static short getSingleBandValue(short[] resampledArray, int x, int y, int nRasterXSize, int nRasterYSize) {
        return resampledArray[x + y * nRasterXSize];
    }

    public static PixelValue getPixelValue(Integer bandNumber, int x, int y, String type, Raster raster) {
        PixelValue pixelValue = new PixelValue();

        if (type.equals("int") ){
            int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

            pixelValue.type = "int";
            pixelValue.intValue = pixel[bandNumber-1];
        }

        if (type.equals("short")) {
            int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

            pixelValue.type = "short";
            pixelValue.shortValue = (short)pixel[bandNumber-1];
        }

        if (type.equals("byte")){
            int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

            pixelValue.type = "byte";
            pixelValue.byteValue = (byte)pixel[bandNumber-1];
        }

        if (type.equals("double")){
            double[] pixel = raster.getSampleModel().getPixel(x, y, new double[3], raster.getDataBuffer());

            pixelValue.type = "byte";
            pixelValue.doubleValue = (double)pixel[bandNumber-1];
        }

        if (type.equals("float")){
            float[] pixel = raster.getSampleModel().getPixel(x, y, new float[3], raster.getDataBuffer());

            pixelValue.type = "float";
            pixelValue.floatValue = (float)pixel[bandNumber-1];
        }

        return pixelValue;
    }
}
