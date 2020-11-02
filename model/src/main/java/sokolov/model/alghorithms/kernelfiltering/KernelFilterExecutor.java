package sokolov.model.alghorithms.kernelfiltering;

import sokolov.model.xmlmodel.KernelFilteredSourceType;
import sokolov.model.xmlmodel.KernelType;

import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class KernelFilterExecutor {

    public static void executeKernel(KernelFilteredSourceType kernelFilteredSourceType, WritableRaster interleavedRaster,
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
                int[] array = new int[nBand * size * size];

                int xIndex = (x - halfOfSize >= 0)? x - halfOfSize : 0;
                int yIndex = (y - halfOfSize >= 0)? y - halfOfSize : 0;
                int xSize = (x - halfOfSize - size >= 0)? size : -(x - halfOfSize);
                int ySize = (y - halfOfSize - size >= 0)? size : -(y - halfOfSize);

                if (xIndex + xSize > nRasterXSize)
                    xSize = nRasterXSize - xIndex;

                if (yIndex + ySize > nRasterYSize)
                    ySize = nRasterYSize - yIndex;

                interleavedRaster.getPixels(xIndex, yIndex, xSize, ySize, array);

                for (int i = 0; i < array.length; i++) {
                    array[i] = array[i] * 5 % 255;
                }

                interleavedRaster.setPixels(xIndex, yIndex, xSize, ySize, array);


                multiplyMatrix(parsedCoeffs, array);

                //int value = array[bandNumber - 1];
            }
        }
    }

    private static void multiplyMatrix(List<Double> parsedCoeffs, int[] array) {

    }

    private static List<Double> parseCoeffs(String[] eachCoeff) {
        List<Double> parseCoeffs = new ArrayList<>();

        for (String coef : eachCoeff) {
            parseCoeffs.add(Double.parseDouble(coef));
        }

        return parseCoeffs;
    }
}
