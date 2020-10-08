package sokolov.javagdalvrt;

/**
 * Options for use with GDALBuildVRT(). GDALBuildVRTOptions* must be allocated and
 * freed with GDALBuildVRTOptionsNew() and GDALBuildVRTOptionsFree() respectively.
 */
public class GdalBuildVrtOptions {
    String pszResolution;
    boolean bSeparate;
    boolean bAllowProjectionDifference;
    double we_res;
    double ns_res;
    boolean bTargetAlignedPixels;
    double xmin;
    double ymin;
    double xmax;
    double ymax;
    boolean bAddAlpha;
    boolean bHideNoData;
    int nSubdataset;
    String pszSrcNoData;
    String pszVRTNoData;
    String pszOutputSRS;
    int[] panBandList;
    int nBandCount;
    int nMaxBandNo;
    String pszResampling;
    String[] papszOpenOptions;

    /*! allow or suppress progress monitor and other non-error output */
    int bQuiet;

    public GdalBuildVrtOptions(Integer hz, Integer tohz) {
        //TODO check this constructor
        this.bSeparate = true;
        this.bTargetAlignedPixels = false;
        this.papszOpenOptions = new String[]{"optionFromBui8ldOption"};
    }
}
