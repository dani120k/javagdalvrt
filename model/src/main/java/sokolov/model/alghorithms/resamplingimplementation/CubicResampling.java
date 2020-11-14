package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;
import sokolov.model.common.PixelValue;

import java.awt.image.BufferedImage;

public class CubicResampling implements ResamplingAlgorithm {

    @Override
    public PixelValue[] resampling(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, String type, BufferedImage originalImage) {
        return new PixelValue[0];
    }
}
