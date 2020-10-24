package ru.sokolov.alghorithms.maskimplementation;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class MaskExecutor {
    public void executeMask(Raster raster,
                            byte[] maskBand,
                            int bandRasterXSize,
                            int bandRasterYSize,
                            int nBand){
        DataBuffer dataBuffer = raster.getDataBuffer();
        SampleModel sampleModel = raster.getSampleModel();

        for(int y = 0; y < bandRasterYSize; y++)
            for(int x = 0; x < bandRasterXSize; x++){
                int[] array = new int[nBand];
                int[] pixel = sampleModel.getPixel(x, y, array, dataBuffer);

                for(int i = 0; i < nBand; i++){
                    pixel[i] = pixel[i] & maskBand[x + y * bandRasterYSize];
                }

                sampleModel.setPixel(x, y, array, dataBuffer);
            }
    }
}
