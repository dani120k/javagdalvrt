package sokolov.model.resamplng;

import sokolov.model.resamplng.resamplingimplementation.AverageResampling;
import sokolov.model.resamplng.resamplingimplementation.BilinearResampling;
import sokolov.model.resamplng.resamplingimplementation.NearestNeighbourResampling;
import sokolov.model.xmlmodel.RectType;

import java.awt.image.BufferedImage;

public class ResamplingAlghorithmExecutor {

    public byte[] imageRescaling(int bandNumber,
                               RectType srcRect,
                               RectType dstRect,
                               int noDataValue,
                               BufferedImage originalImage,
                               String resampling){
        return this.imageRescaling(bandNumber,
                srcRect.getxOff().intValue(), srcRect.getyOff().intValue(), srcRect.getxSize().intValue(), srcRect.getySize().intValue(),
                dstRect.getxOff().intValue(),  dstRect.getyOff().intValue(), dstRect.getxSize().intValue(),dstRect.getySize().intValue(),
                noDataValue, originalImage, resampling);
    }

    public byte[] imageRescaling(int bandNumber,
                               int xOffOriginal,
                               int yOffOriginal,
                               int xSizeOriginal,
                               int ySizeOriginal,
                               int xOffResult,
                               int yOffResult,
                               int xSizeResult,
                               int ySizeResult,
                               int noDataValue,
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
                xOffResult, yOffResult, xSizeResult, ySizeResult,
                noDataValue,
                originalImage);

        return resamplingResultedArray;
    }
}
