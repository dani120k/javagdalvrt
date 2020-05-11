package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

import java.util.List;
import java.util.Map;

public class VrtPansharpenedDataset extends VrtDataset {
    int               m_nBlockXSize;
    int               m_nBlockYSize;
    GDALPansharpenOperation m_poPansharpener;
    VrtPansharpenedDataset m_poMainDataset;
    List<VrtPansharpenedDataset> m_apoOverviewDatasets;
    // Map from absolute to relative.
    Map<CPLString,CPLString> m_oMapToRelativeFilenames;

    int               m_bLoadingOtherBands;

    GByte            m_pabyLastBufferBandRasterIO;
    int               m_nLastBandRasterIOXOff;
    int               m_nLastBandRasterIOYOff;
    int               m_nLastBandRasterIOXSize;
    int               m_nLastBandRasterIOYSize;
    GDALDataType m_eLastBandRasterIODataType;

    GTAdjustment      m_eGTAdjustment;
    int               m_bNoDataDisabled;

    List<GdalDataset> m_apoDatasetsToClose;
}
