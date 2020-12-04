package sokolov.model.alghorithms.resamplingimplementation;

import sokolov.model.alghorithms.ResamplingAlgorithm;
import sokolov.model.common.PixelValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class CubicResampling implements ResamplingAlgorithm {
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

        for (int yRes = yOffResult; yRes < yOffResult + ySizeResult; yRes++)
            for (int xRes = xOffResult; xRes < xOffResult + xSizeResult; xRes++) {
                double xOriginalIndex = (((xSizeOriginal)/(1.0 * xSizeResult)) * (xRes - xOffResult));
                double yOriginalIndex = (((ySizeOriginal)/(1.0 * ySizeResult)) * (yRes - yOffResult));

                int intXOriginalIndex = (int) xOriginalIndex;
                int intYOriginalIndex = (int) yOriginalIndex;

                PixelValue p00 = getValue(intXOriginalIndex + xOffOriginal - 1,
                        intYOriginalIndex + yOffOriginal - 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p10 = getValue(intXOriginalIndex + xOffOriginal - 0,
                        intYOriginalIndex + yOffOriginal - 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p20 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal - 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p30 = getValue(intXOriginalIndex + xOffOriginal + 2,
                        intYOriginalIndex + yOffOriginal - 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p01 = getValue(intXOriginalIndex + xOffOriginal - 1,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);

                PixelValue p11 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);

                PixelValue p21 = getValue(intXOriginalIndex + xOffOriginal + 1,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);

                PixelValue p31 = getValue(intXOriginalIndex + xOffOriginal + 2,
                        intYOriginalIndex + yOffOriginal,
                        bandNumber,
                        type,
                        data);

                PixelValue p02 = getValue(intXOriginalIndex + xOffOriginal -1,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p12 = getValue(intXOriginalIndex + xOffOriginal,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p22 = getValue(intXOriginalIndex + xOffOriginal+1,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p32 = getValue(intXOriginalIndex + xOffOriginal+2,
                        intYOriginalIndex + yOffOriginal + 1,
                        bandNumber,
                        type,
                        data);

                PixelValue p03 = getValue(intXOriginalIndex + xOffOriginal - 1,
                        intYOriginalIndex + yOffOriginal + 2,
                        bandNumber,
                        type,
                        data);

                PixelValue p13 = getValue(intXOriginalIndex + xOffOriginal+0,
                        intYOriginalIndex + yOffOriginal + 2,
                        bandNumber,
                        type,
                        data);

                PixelValue p23 = getValue(intXOriginalIndex + xOffOriginal+1,
                        intYOriginalIndex + yOffOriginal + 2,
                        bandNumber,
                        type,
                        data);

                PixelValue p33 = getValue(intXOriginalIndex + xOffOriginal+2,
                        intYOriginalIndex + yOffOriginal + 2,
                        bandNumber,
                        type,
                        data);

                double col0 = CubicHermite(p00.getAnyValue(), p10.getAnyValue(), p20.getAnyValue(), p30.getAnyValue(), xOriginalIndex - intXOriginalIndex);
                double col1 = CubicHermite(p01.getAnyValue(), p11.getAnyValue(), p21.getAnyValue(), p31.getAnyValue(), xOriginalIndex - intXOriginalIndex);
                double col2 = CubicHermite(p02.getAnyValue(), p12.getAnyValue(), p22.getAnyValue(), p32.getAnyValue(), xOriginalIndex - intXOriginalIndex);
                double col3 = CubicHermite(p03.getAnyValue(), p13.getAnyValue(), p23.getAnyValue(), p33.getAnyValue(), xOriginalIndex - intXOriginalIndex);
                double value = CubicHermite(col0, col1, col2, col3, yOriginalIndex - intYOriginalIndex);

                //TODO > then pixelvalue.maxvalue
                if (value > 255)
                    value = 255;

                PixelValue pixelValue = new PixelValue();
                pixelValue.type = "byte";
                pixelValue.byteValue = (byte)value;

                resultedArray[index++] = pixelValue;
        }

        return resultedArray;
    }

    // t is a value that goes from 0 to 1 to interpolate in a C1 continuous way across uniformly sampled data points.
// when t is 0, this will return B.  When t is 1, this will return C.  Inbetween values will return an interpolation
// between B and C.  A and B are used to calculate slopes at the edges.
    double CubicHermite (double A, double B, double C, double D, double t)
    {
        double a = -A / 2.0f + (3.0f*B) / 2.0f - (3.0f*C) / 2.0f + D / 2.0f;
        double b = A - (5.0f*B) / 2.0f + 2.0f*C - D / 2.0f;
        double c = -A / 2.0f + C / 2.0f;
        double d = B;

        return a*t*t*t + b*t*t + c*t + d;
    }
}
