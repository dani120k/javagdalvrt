package sokolov.model.alghorithms;

import sokolov.model.alghorithms.resamplingimplementation.AverageResampling;
import sokolov.model.alghorithms.resamplingimplementation.BilinearResampling;
import sokolov.model.alghorithms.resamplingimplementation.NearestNeighbourResampling;
import sokolov.model.xmlmodel.RectType;

import java.awt.image.BufferedImage;

public class ResamplingAlghorithmExecutor {

    public int[] imageRescaling(int bandNumber,
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

    public int[] imageRescaling(int bandNumber,
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

        int[] resamplingResultedArray = resamplingAlgorithm.resamplingInt(bandNumber,
                xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal,
                xOffResult, yOffResult, xSizeResult, ySizeResult,
                noDataValue,
                originalImage);

        return resamplingResultedArray;
    }

    public int[] imageRescalingInt(int bandNumber,
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

        int[] resamplingResultedArray = resamplingAlgorithm.resamplingInt(bandNumber,
                xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal,
                xOffResult, yOffResult, xSizeResult, ySizeResult,
                noDataValue,
                originalImage);

        return resamplingResultedArray;
    }

    public short[] imageRescalingShort(int bandNumber,
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

        short[] resamplingResultedArray = resamplingAlgorithm.resamplingUShort(bandNumber,
                xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal,
                xOffResult, yOffResult, xSizeResult, ySizeResult,
                noDataValue,
                originalImage);

        return resamplingResultedArray;
    }
}
