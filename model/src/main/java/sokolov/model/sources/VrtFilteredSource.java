package sokolov.model.sources;

import sokolov.model.enums.GDALDataType;

public class VrtFilteredSource extends VrtComplexSource {
    int m_nSupportedTypesCount;
    GDALDataType[] m_aeSupportedTypes = new GDALDataType[20];

    int m_nExtraEdgePixels;
}
