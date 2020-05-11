package sokolov.model.sources;

import sokolov.model.datasets.GdalRasterBand;

public class VrtSimpleSource extends VrtSource {
    GdalRasterBand m_poRasterBand;

    // When poRasterBand is a mask band, poMaskBandMainBand is the band
    // from which the mask band is taken.
    GdalRasterBand m_poMaskBandMainBand;

    double m_dfSrcXOff;
    double m_dfSrcYOff;
    double m_dfSrcXSize;
    double m_dfSrcYSize;

    double m_dfDstXOff;
    double m_dfDstYOff;
    double m_dfDstXSize;
    double m_dfDstYSize;

    boolean m_bNoDataSet;
    double m_dfNoDataValue;
    String m_osResampling;

    int m_nMaxValue;

    int m_bRelativeToVRTOri;
    String m_osSourceFileNameOri;

    int m_nExplicitSharedStatus; // -1 unknown, 0 = unshared, 1 = shared


    public void setNoDataValue(double dfNewNoDataValue) {
        if (dfNewNoDataValue == VRT_NODATA_UNSET) {
            m_bNoDataSet = false;
            m_dfNoDataValue = VRT_NODATA_UNSET;
            return;
        }

        m_bNoDataSet = true;
        m_dfNoDataValue = dfNewNoDataValue;
    }

    public void setResampling(String pszResampling) {
        m_osResampling = (pszResampling != null) ? pszResampling : "";
    }

    public void SetSrcMaskBand(GdalRasterBand poSrcBand) {

    }

    public void SetSrcBand(GdalRasterBand poSrcBand) {

    }

    public void SetSrcWindow(double dfSrcXOff, double dfSrcYOff, double dfSrcXSize, double dfSrcYSize) {

    }

    public void SetDstWindow(double dfDstXOff, double dfDstYOff, double dfDstXSize, double dfDstYSize) {

    }

    public void SetMaxValue(int i) {

    }
}
