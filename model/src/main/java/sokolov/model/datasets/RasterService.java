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

    public static int getByteValue(int bandNumber,
                               int x, int y,
                               Raster raster){
        int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

        return pixel[bandNumber-1];
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
            pixelValue.uIntValue = pixel[bandNumber-1];
        }

        if (type.equals("ushort")) {
            int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

            pixelValue.type = "ushort";
            pixelValue.uInt16Value = (int)pixel[bandNumber-1];
        }

        if (type.equals("byte")){
            int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

            pixelValue.type = "byte";
            pixelValue.byteValue = pixel[bandNumber-1];
        }

        if (type.equals("double")){
            double[] pixel = raster.getSampleModel().getPixel(x, y, new double[3], raster.getDataBuffer());

            pixelValue.type = "byte";
            pixelValue.doubleValue = (double)pixel[bandNumber-1];
        }

        if (type.equals("float")){
            float[] pixel = raster.getSampleModel().getPixel(x, y, new float[3], raster.getDataBuffer());

            pixelValue.type = "float";
            pixelValue.float32Value = (float)pixel[bandNumber-1];
        }

        return pixelValue;
    }

    public static double getSingleBandValue(PixelValue[] pixelValues, int x, int y, int nRasterXSize, int nRasterYSize) {
        PixelValue pixelValue = pixelValues[x + y * nRasterXSize];

        if (pixelValue.type.equals("int") ){
            return pixelValue.uIntValue;
        }

        if (pixelValue.type.equals("ushort")) {
            return pixelValue.uInt16Value;
        }

        if (pixelValue.type.equals("byte")){
            return pixelValue.byteValue;
        }

        if (pixelValue.type.equals("double")){
            return pixelValue.doubleValue;
        }

        if (pixelValue.type.equals("float")){
            return pixelValue.float32Value;
        }

        return 0;
    }
}
