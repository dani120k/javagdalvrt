package sokolov.model.alghorithms.maskimplementation;

import sokolov.model.common.XmlDeserializer;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.datasets.GdalRasterBand;
import sokolov.model.datasets.VrtDataset;
import sokolov.model.xmlmodel.VRTDataset;
import sokolov.model.xmlmodel.VRTRasterBandType;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

//TODO 255 to nodata value
public class MaskExecutor {
    public void executeMask(Raster raster,
                            VRTDataset vrtDataset,
                            VRTRasterBandType vrtRasterBandType,
                            int bandRasterXSize,
                            int bandRasterYSize,
                            Integer type,
                            int nBand) throws IOException {
        GdalRasterBand gdalRasterBand = new GdalRasterBand();

        WritableRaster interleavedRaster = Raster.createInterleavedRaster(XmlDeserializer.getRasterImageType(vrtRasterBandType.getDataType()),
                bandRasterXSize, bandRasterYSize, 1, null);


        gdalRasterBand.initXml(vrtDataset, vrtRasterBandType, vrtRasterBandType.getSimpleSource().get(0).getSourceBand(), interleavedRaster, type);

        executeMask(raster, interleavedRaster, bandRasterXSize, bandRasterYSize, raster.getNumBands(), nBand);
    }

    public void executeMask(Raster raster,
                            VRTDataset vrtDataset,
                            VRTRasterBandType vrtRasterBandType,
                            int bandRasterXSize,
                            int bandRasterYSize,
                            Integer type) throws IOException {
        GdalRasterBand gdalRasterBand = new GdalRasterBand();

        WritableRaster interleavedRaster = Raster.createInterleavedRaster(XmlDeserializer.getRasterImageType(vrtRasterBandType.getDataType()),
                bandRasterXSize, bandRasterYSize, 1, null);


        gdalRasterBand.initXml(vrtDataset, vrtRasterBandType, vrtRasterBandType.getSimpleSource().get(0).getSourceBand(), interleavedRaster, type);

        executeMask(raster, interleavedRaster, bandRasterXSize, bandRasterYSize, raster.getNumBands());
    }

    public void executeMask(Raster raster,
                            WritableRaster maskBand,
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
                    int[] values = new int[1];
                    maskBand.getPixel(x, y, values);

                    if (values[0] == 0)
                        pixel[i] = 255;
                }

                sampleModel.setPixel(x, y, array, dataBuffer);
            }
    }

    public void executeMask(Raster raster,
                            WritableRaster maskBand,
                            int bandRasterXSize,
                            int bandRasterYSize,
                            int nBand,
                            int bandNumber){
        DataBuffer dataBuffer = raster.getDataBuffer();
        SampleModel sampleModel = raster.getSampleModel();

        for(int y = 0; y < bandRasterYSize; y++)
            for(int x = 0; x < bandRasterXSize; x++){
                int[] array = new int[nBand];
                int[] pixel = sampleModel.getPixel(x, y, array, dataBuffer);

                int[] values = new int[1];
                maskBand.getPixel(x, y, values);

                if (values[0] == 0)
                    pixel[bandNumber - 1] = 255;

                sampleModel.setPixel(x, y, array, dataBuffer);
            }
    }
}
