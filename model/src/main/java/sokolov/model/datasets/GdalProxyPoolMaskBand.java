package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

import java.util.concurrent.atomic.AtomicInteger;

public class GdalProxyPoolMaskBand extends GdalProxyPoolRasterBand {
    GdalProxyPoolRasterBand poMainBand;
    GdalRasterBand poUnderlyingMainRasterBand = null;
    int nRefCountUnderlyingMainRasterBand = 0;

    public GdalProxyPoolMaskBand(GdalProxyPoolDataset poDs,
                                 GdalProxyPoolRasterBand gdalProxyPoolRasterBand,
                                 GDALDataType eDataTypeIn, AtomicInteger nBlockXSizeIn, AtomicInteger nBlockYSizeIn) {
        //TODO
    }
}
