package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;
import sokolov.model.common.PixelValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class BilinearResampling implements ResamplingAlgorithm {
    private PixelValue lerp(PixelValue s, PixelValue e, double t) {
        double diff = PixelValue.calcDiff(e, s);
        double value =  s.getAnyValue() + diff * t;

        PixelValue resulted = new PixelValue();
        resulted.type = s.type;
        resulted.setDoubleValue(value);

        return resulted;
    }

    private PixelValue getValue(int x, int y, int band, String type, Raster data){
        try {
            return PixelValue.getPixelValue(x, y, type, band, data);
        }catch (Exception ex){
            return PixelValue.getEmptyForType(type);
        }
    }

    @Override
    public PixelValue[] resampling(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, String type, BufferedImage originalImage) {
        PixelValue[] resultedArray = new PixelValue[xSizeResult * ySizeResult];

        Rectangle rectangle = new Rectangle(xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal);

        Raster data = originalImage.getData(rectangle);

        int index = 0;

        for(int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++){
                double xOriginalIndex = (((xSizeOriginal)/(1.0 * xSizeResult)) * (xRes - xOffResult));
                double yOriginalIndex = (((ySizeOriginal)/(1.0 * ySizeResult)) * (yRes - yOffResult));

                int intXOriginalIndex = (int) xOriginalIndex;
                int intYOriginalIndex = (int) yOriginalIndex;
                int rgb = 0;

                PixelValue c00 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);

                PixelValue c10 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);
                PixelValue c01 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);
                PixelValue c11 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);

                PixelValue lerp = lerp(
                        lerp(c00, c10, xOriginalIndex - intXOriginalIndex),
                        lerp(c01, c11, xOriginalIndex - intXOriginalIndex), yOriginalIndex - intYOriginalIndex);

                resultedArray[index++] = lerp;
            }

        return resultedArray;

    }
}
