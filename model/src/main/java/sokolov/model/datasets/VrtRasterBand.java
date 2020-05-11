package sokolov.model.datasets;

import sokolov.model.enums.GDALColorInterp;
import sokolov.model.enums.GDALColorTableH;
import sokolov.model.supclasses.CPLXMLNode;
import sokolov.model.supclasses.GDALRasterAttributeTable;
import sokolov.model.supclasses.VRTOverviewInfo;

import java.util.List;

public class VrtRasterBand extends GdalRasterBand {
    int            m_bIsMaskBand;
    int            m_bNoDataValueSet;
    // If set to true, will not report the existence of nodata.
    int            m_bHideNoDataValue;
    double         m_dfNoDataValue;

    GDALColorTableH m_poColorTable;

    GDALColorInterp m_eColorInterp;

    String m_pszUnitType;
    String[] m_papszCategoryNames;

    double         m_dfOffset;
    double         m_dfScale;

    CPLXMLNode m_psSavedHistograms;

    List<VRTOverviewInfo> m_apoOverviews;

    VrtRasterBand m_poMaskBand;

    GDALRasterAttributeTable m_poRAT;


    public void SetOffset(double dfNewOffset){
        poDS.SetNeedsFlush();

        m_dfOffset = dfNewOffset;
    }

    public void SetScale(double dfNewScale){
        poDS.SetNeedsFlush();

        m_dfScale = dfNewScale;
    }
}
