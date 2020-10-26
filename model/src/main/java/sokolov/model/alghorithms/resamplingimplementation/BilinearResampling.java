package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class BilinearResampling implements ResamplingAlgorithm {
    @Override
    public int[] resamplingInt(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, BufferedImage originalImage) {
        return new int[0];
    }

    @Override
    public byte[] resampling(int bandNumber,
                             int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal,
                             int xOffResult, int yOffResult, int xSizeResult, int ySizeResult,
                             int noDataValue,
                             BufferedImage originalImage) {
        byte[] resultedArray = new byte[xSizeResult * ySizeResult];

        Rectangle rectangle = new Rectangle(xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal);

        Raster data = originalImage.getData(rectangle);



        int index = 0;

        for(int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++){
                double xOriginalIndex = (((xSizeOriginal)/(1.0 * xSizeResult)) * (xRes - xOffResult));
                double yOriginalIndex = (((ySizeOriginal)/(1.0 * ySizeResult)) * (yRes - yOffResult));

                int intXOriginalIndex = (int) xOriginalIndex;
                int intYOriginalIndex = (int) yOriginalIndex;
                int rgb = 0;

                int c00 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        data);

                int c10 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        data);
                int c01 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        data);
                int c11 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        data);

                int lerp = (int)lerp(
                        lerp(c00, c10, xOriginalIndex - intXOriginalIndex),
                        lerp(c01, c11, xOriginalIndex - intXOriginalIndex), yOriginalIndex - intYOriginalIndex);

                resultedArray[index++] = (byte)lerp;
            }

        return resultedArray;
    }

    private double lerp(double s, double e, double t) {
        return s + (e - s) * t;
    }

    private int getValue(int x, int y, int band, Raster data){
        int[] array = new int[3];
        try {
            data.getSampleModel().getPixel(
                    x,
                    y,
                    array,
                    data.getDataBuffer()
            );

            return array[band - 1];
        }catch (Exception ex){
            return 0;
        }
    }

    private double calculateDistance(double x, double y,
                                     int x1, int y1){
        return (x - x1) * (x - x1) + (y - y1)* (y - y1);
    }

    @Override
    public short[] resamplingUShort(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, BufferedImage originalImage) {
        return new short[0];
    }
}
