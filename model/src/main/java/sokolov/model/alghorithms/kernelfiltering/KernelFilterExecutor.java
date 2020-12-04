package sokolov.model.alghorithms.kernelfiltering;

import sokolov.model.xmlmodel.KernelFilteredSourceType;
import sokolov.model.xmlmodel.KernelType;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class KernelFilterExecutor {

    //TODO some fixes
    public static void executeKernel(KernelFilteredSourceType kernelFilteredSourceType,
                                               WritableRaster interleavedRaster,
                                     Raster realRaster,
                                     int nRasterXSize,
                                     int nRasterYSize,
                                     int nBand,
                                     int bandNumber) {
        KernelType kernel = kernelFilteredSourceType.getKernel();

        String coefs = kernel.getCoefs();

        String[] eachCoeff = coefs.split(" ");
        int size = kernel.getSize();

        List<Double> parsedCoeffs = parseCoeffs(eachCoeff);

        if (eachCoeff.length != size * size){
            throw new RuntimeException("Неверно указана матрица свертки");
        }

        int halfOfSize = size/2;

        for (int x = 0; x < nRasterXSize; x++) {
            for(int y = 0; y < nRasterYSize; y++){
                int[] array = new int[3 * size * size];

                int xIndex = (x - halfOfSize >= 0)? x - halfOfSize : 0;
                int yIndex = (y - halfOfSize >= 0)? y - halfOfSize : 0;
                int xSize = (x - halfOfSize - size >= 0)? size : -(x - halfOfSize);
                int ySize = (y - halfOfSize - size >= 0)? size : -(y - halfOfSize);

                if (xIndex + xSize > nRasterXSize)
                    xSize = nRasterXSize - xIndex;

                if (yIndex + ySize > nRasterYSize)
                    ySize = nRasterYSize - yIndex;

                realRaster.getSampleModel().getPixels(xIndex, yIndex, xSize, ySize, array, realRaster.getDataBuffer());
                int[] resultedArray = new int[size * size];
                int j = 0;
                for(int i = 0; i < array.length; i+=3){
                    resultedArray[j++] = array[i];
                }
                System.out.println(x + " " + y);

                //TODO this is shit
//                for (int i = 0; i < array.length; i++) {
//                    array[i] = array[i] * 5 % 255;
//                }

                int value = multiplyMatrix(parsedCoeffs, resultedArray);

                //resultedArray[5] = value;

                //interleavedRaster.setPixels(xIndex, yIndex, xSize, ySize, array);
                int[] pixelArray = new int[nBand];
                interleavedRaster.getPixel(x, y, pixelArray);
                pixelArray[bandNumber] = value;
                interleavedRaster.setPixel(x, y, pixelArray);
                //int value = array[bandNumber - 1];
            }
        }
    }

    private static int multiplyMatrix(List<Double> parsedCoeffs, int[] array) {
        int resultValue = 0;

        for (int i = 0; i < array.length; i++) {
            resultValue += array[i] * parsedCoeffs.get(i);
        }

        return resultValue;
    }

    private static List<Double> parseCoeffs(String[] eachCoeff) {
        List<Double> parseCoeffs = new ArrayList<>();

        for (String coef : eachCoeff) {
            parseCoeffs.add(Double.parseDouble(coef));
        }

        return parseCoeffs;
    }
}
