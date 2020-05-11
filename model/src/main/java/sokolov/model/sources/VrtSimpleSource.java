package sokolov.model.sources;

public class VrtSimpleSource extends VrtSource {

    public void setNoDataValue(double dfNewNoDataValue){
        if( dfNewNoDataValue == VRT_NODATA_UNSET )
        {
            m_bNoDataSet = false;
            m_dfNoDataValue = VRT_NODATA_UNSET;
            return;
        }

        m_bNoDataSet = true;
        m_dfNoDataValue = dfNewNoDataValue;
    }

    public void setResampling(String pszResampling){
        m_osResampling = (pszResampling != null) ? pszResampling : "";
    }
}
