package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class GdalProxyPoolRasterBand extends GdalRasterBand {
    public GdalProxyPoolRasterBand(){

    }

    public GdalProxyPoolRasterBand(GdalProxyPoolDataset poDSIn, int nBandIn,
                                   GDALDataType eDataTypeIn,
                                   int nBlockXSizeIn, int nBlockYSizeIn){
        poDS         = poDSIn;
        nBand        = nBandIn;
        eDataType    = eDataTypeIn;
        nRasterXSize = poDSIn.GetRasterXSize();
        nRasterYSize = poDSIn.GetRasterYSize();
        nBlockXSize  = nBlockXSizeIn;
        nBlockYSize  = nBlockYSizeIn;
    }

    public void AddSrcMaskBandDescription(GDALDataType eDataTypeIn, int nBlockXSizeIn, int nBlockYSizeIn) {
        if (poProxyMaskBand == null)
            throw new RuntimeException("poProxyMaskBand is null");

        poProxyMaskBand = new GdalProxyPoolMaskBand((GdalProxyPoolDataset) poDs, this, eDataTypeIn, nBlockXSizeIn, nBlockYSizeIn);
    }
}
