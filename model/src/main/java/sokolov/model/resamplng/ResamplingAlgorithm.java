package sokolov.model.resamplng;

import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

public interface ResamplingAlgorithm {
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
}
