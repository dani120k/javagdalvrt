package sokolov.model.alghorithms;

import java.awt.image.BufferedImage;

public interface ResamplingAlgorithm<T extends Number> {
    byte[] resampling(int bandNumber,
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
