package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.GDAL_GCP;
import sokolov.model.supclasses.VRTGroup;
import sokolov.model.supclasses.VRTImageReadFunc;

import java.util.List;
import java.util.Map;

public class VrtDataset extends GdalDataset {
    public double VRT_NODATA_UNSET = -1234.56;
    boolean m_bGeoTransformSet;
    double[] m_adfGeoTransform = new double[6];
    int m_nGCPCount;
    GDAL_GCP m_pasGCPList;
    OGRSpatialReference m_poGCP_SRS = null;

    boolean m_bNeedsFlush;
    boolean m_bWritable;

    String m_pszVRTPath;

    VrtRasterBand m_poMaskBand;

    int m_bCompatibleForDatasetIO;

    List<GdalDataset> m_apoOverviews;
    List<GdalDataset> m_apoOverviewsBak;
    String[] m_papszXMLVRTMetadata;

    Map<String, GdalDataset> m_oMapSharedSources;
    VRTGroup m_poRootGroup;

    public VrtDataset() {

    }

    public VrtDataset(int nXSize, int nYSize) {
        m_bGeoTransformSet = false;
        m_nGCPCount = 0;
        m_pasGCPList = null;
        m_bNeedsFlush = false;
        m_bWritable = true;
        m_pszVRTPath = null;
        m_poMaskBand = null;
        m_bCompatibleForDatasetIO = -1;
        m_papszXMLVRTMetadata = null;
        nRasterXSize = nXSize;
        nRasterYSize = nYSize;

        m_adfGeoTransform[0] = 0.0;
        m_adfGeoTransform[1] = 1.0;
        m_adfGeoTransform[2] = 0.0;
        m_adfGeoTransform[3] = 0.0;
        m_adfGeoTransform[4] = 0.0;
        m_adfGeoTransform[5] = 1.0;

        //GDALRegister_VRT();

        //poDriver = static_cast < GDALDriver * > (GDALGetDriverByName("VRT"));
    }

    public void AddBand(GDALDataType eType, String[] papszOptions) {
        m_bNeedsFlush = true;

        /* ==================================================================== */
        /*      Handle a new raw band.                                          */
        /* ==================================================================== */
        String pszSubClass = CSLFetchNameValue(papszOptions, "subclass");

        if (pszSubClass != null && pszSubClass.equals("VRTRawRasterBand")) {
            int nWordDataSize = GDALGetDataTypeSizeBytes(eType);

            /* -------------------------------------------------------------------- */
            /*      Collect required information.                                   */
            /* -------------------------------------------------------------------- */
            String pszImageOffset =
                    CSLFetchNameValueDef(papszOptions, "ImageOffset", "0");
            long nImageOffset = pszImageOffset.length();

            int nPixelOffset = nWordDataSize;
            String pszPixelOffset =
                    CSLFetchNameValue(papszOptions, "PixelOffset");
            if (pszPixelOffset != null)
                nPixelOffset = Integer.parseInt(pszPixelOffset);

            int nLineOffset;
            String pszLineOffset =
                    CSLFetchNameValue(papszOptions, "LineOffset");
            if (pszLineOffset != null)
                nLineOffset = Integer.parseInt(pszLineOffset);
            else {
                if (nPixelOffset > Integer.MAX_VALUE / GetRasterXSize() ||
                        nPixelOffset < Integer.MIN_VALUE / GetRasterXSize()) {
                    throw new RuntimeException("Int overflow");
                }
                nLineOffset = nPixelOffset * GetRasterXSize();
            }

            String pszByteOrder =
                    CSLFetchNameValue(papszOptions, "ByteOrder");

            String pszFilename =
                    CSLFetchNameValue(papszOptions, "SourceFilename");
            if (pszFilename == null) {
                throw new RuntimeException("AddBand() requires a SourceFilename option for VRTRawRasterBands.");
            }

            boolean bRelativeToVRT =
                    CPLFetchBool(papszOptions, "relativeToVRT", false);

            /* -------------------------------------------------------------------- */
            /*      Create and initialize the band.                                 */
            /* -------------------------------------------------------------------- */

            VrtRawRasterBand poBand =
                    new VrtRawRasterBand(this, GetRasterCount() + 1, eType);

            String l_pszVRTPath = CPLGetPath(getDescription());
            if (l_pszVRTPath.equals("")) {
                l_pszVRTPath = null;
            }
            poBand.SetRawLink(pszFilename, l_pszVRTPath, bRelativeToVRT,
                    nImageOffset, nPixelOffset, nLineOffset,
                    pszByteOrder);

            SetBand(GetRasterCount() + 1, poBand);

            return;
        }

        /* ==================================================================== */
        /*      Handle a new "sourced" band.                                    */
        /* ==================================================================== */
        else {
            VrtSourcedRasterBand poBand = null;

            /* ---- Check for our sourced band 'derived' subclass ---- */
            if (pszSubClass != null && pszSubClass.equals("VRTDerivedRasterBand")) {

                /* We'll need a pointer to the subclass in case we need */
                /* to set the new band's pixel function below. */
                VrtDerivedRasterBand poDerivedBand = new VrtDerivedRasterBand(
                        this, GetRasterCount() + 1, eType,
                        GetRasterXSize(), GetRasterYSize());

                /* Set the pixel function options it provided. */
                String pszFuncName =
                        CSLFetchNameValue(papszOptions, "PixelFunctionType");
                if (pszFuncName != null)
                    poDerivedBand.SetPixelFunctionName(pszFuncName);

                String pszLanguage =
                        CSLFetchNameValue(papszOptions, "PixelFunctionLanguage");
                if (pszLanguage != null)
                    poDerivedBand.SetPixelFunctionLanguage(pszLanguage);

                String pszTransferTypeName =
                        CSLFetchNameValue(papszOptions, "SourceTransferType");
                if (pszTransferTypeName != null) {
                    GDALDataType eTransferType =
                            GDALGetDataTypeByName(pszTransferTypeName);
                    if (eTransferType == GDALDataType.GDT_Unknown) {
                        throw new RuntimeException(String.format("invalid SourceTransferType: \"%s\".", pszTransferTypeName));
                    }
                    poDerivedBand.SetSourceTransferType(eTransferType);
                }

                /* We're done with the derived band specific stuff, so */
                /* we can assigned the base class pointer now. */
                poBand = poDerivedBand;
            } else {
                /* ---- Standard sourced band ---- */
                poBand = new VrtSourcedRasterBand(
                        this, GetRasterCount() + 1, eType,
                        GetRasterXSize(), GetRasterYSize());
            }

            SetBand(GetRasterCount() + 1, poBand);

            for (int i = 0; papszOptions != null && papszOptions[i] != null; i++) {
                if (STARTS_WITH_CI(papszOptions[i], "AddFuncSource=")) {
                    String[] papszTokens
                            = CSLTokenizeStringComplex(papszOptions[i] + 14,
                            ",", true, false);
                    if (CSLCount(papszTokens) < 1) {
                        throw new RuntimeException("AddFuncSource(): required argument missing.");
                        // TODO: How should this error be handled?  Return
                        // CE_Failure?
                    }

                    VRTImageReadFunc pfnReadFunc = null;
                    /*sscanf(papszTokens[0], "%p", & pfnReadFunc );

                    void *pCBData = nullptr;
                    if (CSLCount(papszTokens) > 1)
                        sscanf(papszTokens[1], "%p", & pCBData );*/

                    double dfNoDataValue =
                            (CSLCount(papszTokens) > 2) ?
                                    CPLAtof(papszTokens[2]) : VRT_NODATA_UNSET;

                    //TODO poBand.AddFuncSource(pfnReadFunc, pCBData, dfNoDataValue);
                    poBand.AddFuncSource(pfnReadFunc, dfNoDataValue);
                }
            }

            return;
        }
    }

    private boolean STARTS_WITH_CI(String papszOption, String s) {
        return false;
    }

    private String CPLGetPath(String description) {
        return null;
    }

    private boolean CPLFetchBool(String[] papszOptions, String relativeToVRT, boolean b) {
        return false;
    }

    private GDALDataType GDALGetDataTypeByName(String pszTransferTypeName) {
        return null;
    }

    private String[] CSLTokenizeStringComplex(String s, String s1, boolean b, boolean b1) {
        return new String[0];
    }

    private String CSLFetchNameValueDef(String[] papszOptions, String imageOffset, String s) {
        return null;
    }

    private double CPLAtof(String papszToken) {
        return 0;
    }

    private int GDALGetDataTypeSizeBytes(GDALDataType eType) {
        return 0;
    }

    private int CSLCount(String[] papszTokens) {
        return 0;
    }

    private String CSLFetchNameValue(String[] papszOptions, String subclass) {
        return null;
    }
}
