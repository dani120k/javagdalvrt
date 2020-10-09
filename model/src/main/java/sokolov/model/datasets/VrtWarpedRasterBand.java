package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.enums.GdalAccess;

public class VrtWarpedRasterBand extends VrtRasterBand {
    public VrtWarpedRasterBand(GdalDataset poDSIn, int nBandIn,
                               GDALDataType eType ) {
        Initialize(poDSIn.GetRasterXSize(), poDSIn.GetRasterYSize());

        poDS = poDSIn;
        nBandIn = nBandIn;
        eAccess = GdalAccess.GA_Update;

        ((VrtWarpedDataset)poDS).GetBlockSize(nBlockXSize, nBlockYSize);

        eDataType = eType;
    }
}
