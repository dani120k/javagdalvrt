package ru.sokolov.alghorithms.resamplingimplementation;

import ru.sokolov.alghorithms.ResamplingAlgorithm;

import java.awt.image.BufferedImage;

public class AverageResampling implements ResamplingAlgorithm {

    @Override
    public byte[] resampling(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeReuslt, int ySizeResult, BufferedImage originalImage) {
        return new byte[0];
    }
}