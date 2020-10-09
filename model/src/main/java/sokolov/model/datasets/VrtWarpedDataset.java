package sokolov.model.datasets;

import sokolov.model.supclasses.GDALWarpOperation;

import java.util.concurrent.atomic.AtomicInteger;

public class VrtWarpedDataset extends VrtDataset {
    int               m_nBlockXSize;
    int               m_nBlockYSize;
    GDALWarpOperation m_poWarper;

    int               m_nOverviewCount;
    VrtWarpedDataset m_papoOverviews;
    int               m_nSrcOvrLevel;

    public void GetBlockSize(AtomicInteger nBlockXSize, AtomicInteger nBlockYSize) {
        nBlockXSize.set(m_nBlockXSize);
        nBlockYSize.set(m_nBlockYSize);
    }
}
