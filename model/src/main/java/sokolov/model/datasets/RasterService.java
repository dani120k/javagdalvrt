package sokolov.model.datasets;

import java.awt.image.Raster;

public class RasterService {
    public static int getValue(int bandNumber,
                           int x, int y,
                           Raster raster){
        int[] pixel = raster.getSampleModel().getPixel(x, y, new int[3], raster.getDataBuffer());

        return pixel[bandNumber-1];
    }
}
