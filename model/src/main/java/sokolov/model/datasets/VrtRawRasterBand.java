package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class VrtRawRasterBand extends VrtRasterBand {
    RawRasterBand  m_poRawRaster;
    String m_pszSourceFilename;
    boolean            m_bRelativeToVRT;

    public VrtRawRasterBand(GdalDataset poDSIn,
                            int nBandIn,
                            GDALDataType eType){
        m_poRawRaster = null;
        m_pszSourceFilename = null;
        m_bRelativeToVRT = false;
        Initialize(poDSIn.GetRasterXSize(), poDSIn.GetRasterYSize());

        poDS = poDSIn;
        nBand = nBandIn;

        eDataType = eType;
    }



    public void SetRawLink(String pszFilename, String l_pszVRTPath, boolean bRelativeToVRT, long nImageOffset, int nPixelOffset, int nLineOffset, String pszByteOrder) {

    }
}
