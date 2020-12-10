package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;
import sokolov.model.common.PixelValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class CubicSplineResampling implements ResamplingAlgorithm {
    private PixelValue getValue(int x, int y, int band, String type, Raster data) {
        try {
            return PixelValue.getPixelValue(x, y, type, band, data);
        } catch (Exception ex) {
            return PixelValue.getEmptyForType(type);
        }
    }

    @Override
    public PixelValue[] resampling(int bandNumber, int xOffOriginal, int yOffOriginal, int xSizeOriginal, int ySizeOriginal, int xOffResult, int yOffResult, int xSizeResult, int ySizeResult, int noDataValue, String type, BufferedImage originalImage) {
        PixelValue[] resultedArray = new PixelValue[xSizeResult * ySizeResult];

        Rectangle rectangle = new Rectangle(xOffOriginal, yOffOriginal, xSizeOriginal, ySizeOriginal);

        Raster data = originalImage.getData(rectangle);

        int index = 0;

        for (int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++) {
                double xOriginalIndex = (((xSizeOriginal) / (1.0 * xSizeResult)) * (xRes - xOffResult));
                double yOriginalIndex = (((ySizeOriginal) / (1.0 * ySizeResult)) * (yRes - yOffResult));

                int intXOriginalIndex = (int) xOriginalIndex;
                int intYOriginalIndex = (int) yOriginalIndex;

                double nSum = 0;
                double nDenom = 0;

                for(int m = -1; m <= 2; m++)
                    for(int n = -1; n <= 2; n++){
                        double f = BSpline(m - (xOriginalIndex - (int)xOriginalIndex));
                        double f1 = BSpline(- (n - (yOriginalIndex - (int)yOriginalIndex)));

                        PixelValue pixel = getValue(intXOriginalIndex + xOffOriginal + m,
                                intYOriginalIndex + yOffOriginal + n,
                                bandNumber,
                                type,
                                data);

                        nSum += pixel.getAnyValue() * f * f1;
                        nDenom += f * f1;
                    }

                double value = nSum/nDenom;

                if (value - (int)value != 0.0)
                    value += 1;

                if (value > PixelValue.getMaxForType(type))
                    value = PixelValue.getMaxForType(type);

                if (value < PixelValue.getMinForType(type))
                    value = PixelValue.getMinForType(type);

                PixelValue pixelValue = new PixelValue();
                pixelValue.type = "byte";
                pixelValue.byteValue =  (int)value;

                resultedArray[index++] = pixelValue;
            }

        return resultedArray;
    }

    private double BSpline(double x) {
        if (x < 0.0)
            x = -x;

        if (x >= 0.0 && x <= 1.0)
            return (2.0 / 3.0) + 0.5 * (x * x * x) - (x * x);
        else if (x > 1.0 && x <= 2.0)
            return 1.0 / 6.0 * Math.pow((2.0 - x), 3.0);

        return 1.0;
    }
}
