package sokolov.model.supclasses;

import sokolov.model.datasets.GdalRasterBand;

public class GDALDefaultOverviews {
    public boolean IsInitialized() {
        return false;
    }

    public void CreateMaskBand(int nFlagsIn, int i) {
        return;
    }

    public boolean HaveMaskFile() {
        return false;
    }

    public GdalRasterBand GetMaskBand(int nBand) {
        return null;
    }

    public boolean GetMaskFlags(int nBand) {
        return false;
    }
}
