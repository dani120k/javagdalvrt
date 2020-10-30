package sokolov.model.alghorithms;

import sokolov.model.common.PixelValue;

import java.awt.image.BufferedImage;

public interface ResamplingAlgorithm<T extends Number> {
    PixelValue[] resampling(int bandNumber,
                            int xOffOriginal,
                            int yOffOriginal,
                            int xSizeOriginal,
                            int ySizeOriginal,
                            int xOffResult,
                            int yOffResult,
                            int xSizeResult,
                            int ySizeResult,
                            int noDataValue,
                            String type,
                            BufferedImage originalImage);

    int[] resamplingInt(int bandNumber,
                      int xOffOriginal,
                      int yOffOriginal,
                      int xSizeOriginal,
                      int ySizeOriginal,
                      int xOffResult,
                      int yOffResult,
                      int xSizeResult,
                      int ySizeResult,
                      int noDataValue,
                      BufferedImage originalImage);

    short[] resamplingUShort(int bandNumber,
                        int xOffOriginal,
                        int yOffOriginal,
                        int xSizeOriginal,
                        int ySizeOriginal,
                        int xOffResult,
                        int yOffResult,
                        int xSizeResult,
                        int ySizeResult,
                        int noDataValue,
                        BufferedImage originalImage);
}
