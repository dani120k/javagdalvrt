package sokolov.model.datasets;

public class VrtRawRasterBand extends VrtRasterBand {
    RawRasterBand  m_poRawRaster;

    String m_pszSourceFilename;
    int            m_bRelativeToVRT;
}
