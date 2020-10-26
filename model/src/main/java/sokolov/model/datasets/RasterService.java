package sokolov.model.datasets;

import java.awt.image.Raster;

public class RasterService {
    public static int getValue(int bandNumber,
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
}
