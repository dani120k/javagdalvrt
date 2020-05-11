package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class GdalNoDataMaskBand extends GdalRasterBand {
    double dfNoDataValue;
    GdalRasterBand poParent;

    public GdalNoDataMaskBand(GdalRasterBand gdalRasterBand){

    }

    public static boolean IsNoDataInRange(double dfNoDataValue, GDALDataType eDataType) {
        return false;
    }
}
