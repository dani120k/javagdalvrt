package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class VrtDerivedRasterBand extends VrtSourcedRasterBand {
    VRTDerivedRasterBandPrivateData m_poPrivate;
    String pszFuncName;
    GDALDataType eSourceTransferType;

    VrtDerivedRasterBand(GdalDataset gdalDataset,
                         int rasterCount,
                         GDALDataType eType,
                         int rasterXSize,
                         int rasterYSize){
        //TODO
    }

    public void SetPixelFunctionName(String pszFuncName) {

    }

    public void SetPixelFunctionLanguage(String pszLanguage) {

    }

    public void SetSourceTransferType(GDALDataType eTransferType) {

    }
}
