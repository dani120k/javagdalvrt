package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.enums.GdalAccess;

public class VrtPansharpenedRasterBand extends VrtRasterBand {
    int               m_nIndexAsPansharpenedBand;

    public VrtPansharpenedRasterBand(GdalDataset poDsIn, int nBandIn
                                     ){
        GDALDataType eDataTypeIn = GDALDataType.GDT_Unknown;
        m_nIndexAsPansharpenedBand = nBand -1;
        Initialize(poDsIn.GetRasterXSize(), poDsIn.GetRasterYSize());

        poDS = poDsIn;
        nBand = nBandIn;
        eAccess = GdalAccess.GA_Update;
        eDataType = eDataTypeIn;

        //TODO &nBlockXSize, &nBlockYSize
        ((VrtPansharpenedDataset)poDS).GetBlockSize(nBlockXSize, nBlockYSize);
    }
}
