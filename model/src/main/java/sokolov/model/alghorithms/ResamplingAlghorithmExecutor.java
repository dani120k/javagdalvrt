package sokolov.model.alghorithms;

import sokolov.model.alghorithms.resamplingimplementation.AverageResampling;
import sokolov.model.alghorithms.resamplingimplementation.BilinearResampling;
import sokolov.model.alghorithms.resamplingimplementation.NearestNeighbourResampling;
import sokolov.model.common.PixelValue;
import sokolov.model.xmlmodel.RectType;

import java.awt.image.BufferedImage;

public class ResamplingAlghorithmExecutor {

    public PixelValue[] imageRescaling(int bandNumber,
                               RectType srcRect,
                               RectType dstRect,
                               int noDataValue,
                               String type,
                               BufferedImage originalImage,
                               String resampling){
        return this.imageRescaling(bandNumber,
                srcRect.getxOff().intValue(), srcRect.getyOff().intValue(), srcRect.getxSize().intValue(), srcRect.getySize().intValue(),
                dstRect.getxOff().intValue(),  dstRect.getyOff().intValue(), dstRect.getxSize().intValue(),dstRect.getySize().intValue(),
                noDataValue, type, originalImage, resampling);
    }

    public PixelValue[] imageRescaling(int bandNumber,
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
                //TODO resamplingAlgorithm = new AverageResampling();
                break;
        }

        if (resamplingAlgorithm == null)
            return null;

        PixelValue[] resamplingResultedArray = resamplingAlgorithm.resampling(bandNumber,
                xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal,
                xOffResult, yOffResult, xSizeResult, ySizeResult,
                noDataValue,
                type,
                originalImage);

        return resamplingResultedArray;
    }
}
