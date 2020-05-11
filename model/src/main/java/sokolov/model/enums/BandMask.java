package sokolov.model.enums;

public enum  BandMask {
    GMF_ALL_VALID(0x01), // There are no invalid pixels, all mask values will be 255. When used this will normally be the only flag set.
    GMF_PER_DATASET(0x02), // The mask band is shared between all bands on the dataset.
    GMF_ALPHA(0x04), // The mask band is actually an alpha band and may have values other than 0 and 255.
    GMF_NODATA(0x08); // Indicates the mask is actually being generated from nodata values. (mutually exclusive of GMF_ALPHA)

    private int value;

    BandMask(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
