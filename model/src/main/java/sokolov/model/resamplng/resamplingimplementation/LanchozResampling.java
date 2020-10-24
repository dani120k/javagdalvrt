package sokolov.model.resamplng.resamplingimplementation;

import sokolov.model.resamplng.ResamplingAlgorithm;

import java.awt.image.BufferedImage;

public class LanchozResampling implements ResamplingAlgorithm {

    @Override
    public byte[] resampling(int bandNumber,
                             int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal,
                             int xOffResult, int yOffResult, int xSizeResult, int ySizeResult,
                             int noDataValue,BufferedImage originalImage) {
        return new byte[0];
    }
}
