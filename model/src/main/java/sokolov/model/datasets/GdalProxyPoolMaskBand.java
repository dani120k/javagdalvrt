package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class GdalProxyPoolMaskBand extends GdalProxyPoolRasterBand {
    GdalProxyPoolRasterBand poMainBand;
    GdalRasterBand poUnderlyingMainRasterBand = null;
    int nRefCountUnderlyingMainRasterBand = 0;

    public GdalProxyPoolMaskBand(GdalProxyPoolDataset poDs,
                                 GdalProxyPoolRasterBand gdalProxyPoolRasterBand,
                                 GDALDataType eDataTypeIn, int nBlockXSizeIn, int nBlockYSizeIn) {
        //TODO
    }
}
