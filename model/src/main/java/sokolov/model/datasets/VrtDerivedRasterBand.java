package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class VrtDerivedRasterBand extends VrtSourcedRasterBand {
    VRTDerivedRasterBandPrivateData m_poPrivate;
    String pszFuncName;
    GDALDataType eSourceTransferType;
}
