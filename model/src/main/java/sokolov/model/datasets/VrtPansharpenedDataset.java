package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.GByte;
import sokolov.model.supclasses.GDALPansharpenOperation;
import sokolov.model.supclasses.GTAdjustment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VrtPansharpenedDataset extends VrtDataset {
    int               m_nBlockXSize;
    int               m_nBlockYSize;
    GDALPansharpenOperation m_poPansharpener;
    VrtPansharpenedDataset m_poMainDataset;
    List<VrtPansharpenedDataset> m_apoOverviewDatasets;
    // Map from absolute to relative.
    Map<String,String> m_oMapToRelativeFilenames;

    int               m_bLoadingOtherBands;

    GByte m_pabyLastBufferBandRasterIO;
    int               m_nLastBandRasterIOXOff;
    int               m_nLastBandRasterIOYOff;
    int               m_nLastBandRasterIOXSize;
    int               m_nLastBandRasterIOYSize;
    GDALDataType m_eLastBandRasterIODataType;

    GTAdjustment m_eGTAdjustment;
    int               m_bNoDataDisabled;

    List<GdalDataset> m_apoDatasetsToClose;

    public VrtPansharpenedDataset(int xSize, int ySize){
        //TODO for test
        this.nRasterXSize = xSize;
        this.nRasterYSize = ySize;
    }

    public void GetBlockSize(AtomicInteger nBlockXSize, AtomicInteger nBlockYSize) {
        nBlockXSize.set(m_nBlockXSize);
        nBlockYSize.set(m_nBlockYSize);
    }
}
