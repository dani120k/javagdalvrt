package ru.sokolov.alghorithms;

import ru.sokolov.alghorithms.resamplingimplementation.AverageResampling;
import ru.sokolov.alghorithms.resamplingimplementation.BilinearResampling;
import ru.sokolov.alghorithms.resamplingimplementation.NearestNeighbourResampling;
import sokolov.model.xmlmodel.RectType;

import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

public class ResamplingAlghorithmExecutor {

    public byte[] imageRescaling(int bandNumber,
                               RectType srcRect,
                               RectType dstRect,
                               BufferedImage originalImage,
                               String rescaling){
        return this.imageRescaling(bandNumber,
                srcRect.getxOff().intValue(), srcRect.getxSize().intValue(), srcRect.getyOff().intValue(), srcRect.getySize().intValue(),
                dstRect.getxOff().intValue(), dstRect.getxSize().intValue(), dstRect.getyOff().intValue(), dstRect.getySize().intValue(),
                originalImage, rescaling);
    }

    public byte[] imageRescaling(int bandNumber,
                               int xOffOriginal,
                               int yOffOriginal,
                               int xSizeOriginal,
                               int ySizeOriginal,
                               int xOffResult,
                               int yOffResult,
                               int xSizeReuslt,
                               int ySizeResult,
                               BufferedImage originalImage,
                               String resampling){
        ResamplingAlgorithm resamplingAlgorithm = null;

        switch (resampling){
            case "nearest":
                resamplingAlgorithm = new NearestNeighbourResampling();
                break;
            case "bilinear":
                resamplingAlgorithm = new BilinearResampling();
                break;
            case "average":
                resamplingAlgorithm = new AverageResampling();
                break;
        }

        if (resamplingAlgorithm == null)
            return null;

        byte[] resamplingResultedArray = resamplingAlgorithm.resampling(bandNumber,
                xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal,
                xOffResult, yOffResult, xSizeReuslt, ySizeResult,
                originalImage);

        return resamplingResultedArray;
    }
}
