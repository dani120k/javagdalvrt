package sokolov.model.datasets;

public class GDALRasterIOExtraArg {
    int                    nVersion;

    /*! Resampling algorithm */
    GDALRIOResampleAlg     eResampleAlg;

    /*! Indicate if dfXOff, dfYOff, dfXSize and dfYSize are set.
        Mostly reserved from the VRT driver to communicate a more precise
        source window. Must be such that dfXOff - nXOff < 1.0 and
        dfYOff - nYOff < 1.0 and nXSize - dfXSize < 1.0 and nYSize - dfYSize < 1.0 */
    int                    bFloatingPointWindowValidity;
    /*! Pixel offset to the top left corner. Only valid if bFloatingPointWindowValidity = TRUE */
    double                 dfXOff;
    /*! Line offset to the top left corner. Only valid if bFloatingPointWindowValidity = TRUE */
    double                 dfYOff;
    /*! Width in pixels of the area of interest. Only valid if bFloatingPointWindowValidity = TRUE */
    double                 dfXSize;
    /*! Height in pixels of the area of interest. Only valid if bFloatingPointWindowValidity = TRUE */
    double                 dfYSize;
}
