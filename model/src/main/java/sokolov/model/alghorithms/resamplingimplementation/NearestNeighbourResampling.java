package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;
import sokolov.model.common.PixelValue;
import sokolov.model.common.XmlDeserializer;
import sokolov.model.datasets.RasterService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class NearestNeighbourResampling implements ResamplingAlgorithm {
    @Override
    public PixelValue[] resampling(int bandNumber,
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
                                   BufferedImage originalImage) {
        PixelValue[] resultedArray = new PixelValue[xSizeResult * ySizeResult];

        Rectangle rectangle = new Rectangle(xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal);

        Raster data = originalImage.getData(rectangle);

        int index = 0;

        for (int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++) {
                int xOriginalIndex = (int) (((xSizeOriginal) / (1.0 * xSizeResult)) * (xRes - xOffResult));
                int yOriginalIndex = (int) (((ySizeOriginal) / (1.0 * ySizeResult)) * (yRes - yOffResult));

                PixelValue resultedValue = PixelValue.getPixelValue(
                        xOriginalIndex + xOffOriginal,
                        yOriginalIndex + yOffOriginal,
                        type,
                        bandNumber,
                        data);

                resultedArray[index++] = resultedValue;
            }

        return resultedArray;
    }
}
