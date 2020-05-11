package sokolov.model.datasets;

import sokolov.model.supclasses.GDALWarpOperation;

public class VrtWarpedDataset extends VrtDataset {
    int               m_nBlockXSize;
    int               m_nBlockYSize;
    GDALWarpOperation m_poWarper;

    int               m_nOverviewCount;
    VrtWarpedDataset m_papoOverviews;
    int               m_nSrcOvrLevel;
}
