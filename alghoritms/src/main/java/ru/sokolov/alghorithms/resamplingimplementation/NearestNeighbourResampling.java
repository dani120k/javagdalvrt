package ru.sokolov.alghorithms.resamplingimplementation;

import ru.sokolov.alghorithms.ResamplingAlgorithm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class NearestNeighbourResampling implements ResamplingAlgorithm {
    @Override
    public byte[] resampling(int bandNumber,
                               int xOffOriginal,
                               int yOffOriginal,
                               int xSizeOriginal,
                               int ySizeOriginal,
                               int xOffResult,
                               int yOffResult,
                               int xSizeResult,
                               int ySizeResult,
                               BufferedImage originalImage){
        byte[] resultedArray = new byte[xSizeResult * ySizeResult];

        Rectangle rectangle = new Rectangle(xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal);

        Raster data = originalImage.getData(rectangle);



        int index = 0;

        for(int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++){
                int xOriginalIndex = (int)(((xSizeOriginal)/(1.0 * xSizeResult)) * (xRes - xOffResult));
                int yOriginalIndex = (int)(((ySizeOriginal)/(1.0 * ySizeResult)) * (yRes - yOffResult));

                int[] array = new int[3];
                data.getSampleModel().getPixel(
                        xOriginalIndex + xOffOriginal,
                        yOriginalIndex + yOffOriginal,
                        array,
                        data.getDataBuffer()
                        );

                int resultedValue = array[bandNumber - 1];
                resultedArray[index++] = (byte)resultedValue;
            }

        return resultedArray;
    }
}
