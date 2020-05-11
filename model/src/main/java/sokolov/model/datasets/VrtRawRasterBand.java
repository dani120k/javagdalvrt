package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

public class VrtRawRasterBand extends VrtRasterBand {
    RawRasterBand  m_poRawRaster;

    public VrtRawRasterBand(GdalDataset gdalDataset,
                            int rasterCount,
                            GDALDataType eType){
        //TODO
    }

    String m_pszSourceFilename;
    int            m_bRelativeToVRT;

    public void SetRawLink(String pszFilename, String l_pszVRTPath, boolean bRelativeToVRT, long nImageOffset, int nPixelOffset, int nLineOffset, String pszByteOrder) {

    }
}
