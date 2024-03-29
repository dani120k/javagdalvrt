package sokolov.model.datasets;

import sokolov.model.enums.GDALColorTableH;
import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.CPLHashSet;

import java.util.concurrent.atomic.AtomicInteger;

public class GdalProxyPoolRasterBand extends GdalRasterBand {
    CPLHashSet metadataSet = null;
    CPLHashSet metadataItemSet = null;
    String pszUnitType = null;
    String[] papszCategoryNames = null;
    GDALColorTableH poColorTable = null;

    int                               nSizeProxyOverviewRasterBand = 0;

    GDALProxyPoolOverviewRasterBand[] papoProxyOverviewRasterBand = null;
    GdalProxyPoolMaskBand poProxyMaskBand = null;

    public GdalProxyPoolRasterBand(){

    }

    public GdalProxyPoolRasterBand(GdalProxyPoolDataset poDSIn, int nBandIn,
                                   GDALDataType eDataTypeIn,
                                   AtomicInteger nBlockXSizeIn, AtomicInteger nBlockYSizeIn){
        poDS         = poDSIn;
        nBand        = nBandIn;
        eDataType    = eDataTypeIn;
        nRasterXSize = poDSIn.GetRasterXSize();
        nRasterYSize = poDSIn.GetRasterYSize();
        nBlockXSize  = nBlockXSizeIn;
        nBlockYSize  = nBlockYSizeIn;
    }

    public void AddSrcMaskBandDescription(GDALDataType eDataTypeIn, AtomicInteger nBlockXSizeIn, AtomicInteger nBlockYSizeIn) {
        if (poProxyMaskBand == null)
            throw new RuntimeException("poProxyMaskBand is null");

        poProxyMaskBand = new GdalProxyPoolMaskBand((GdalProxyPoolDataset) poDS, this, eDataTypeIn, nBlockXSizeIn, nBlockYSizeIn);
    }
}
