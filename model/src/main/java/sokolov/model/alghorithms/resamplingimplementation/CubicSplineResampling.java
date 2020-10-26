package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;

import java.awt.image.BufferedImage;

public class CubicSplineResampling implements ResamplingAlgorithm {

    @Override
    public byte[] resampling(int bandNumber,
                             int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal,
                             int xOffResult, int yOffResult, int xSizeResult, int ySizeResult,
                             int noDataValue,BufferedImage originalImage) {
        return new byte[0];
    }

    @Override
    public int[] resamplingInt(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, BufferedImage originalImage) {
        return new int[0];
    }

    @Override
    public short[] resamplingUShort(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, BufferedImage originalImage) {
        return new short[0];
    }
}
