package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;

import java.util.List;
import java.util.Map;

public class VrtDataset extends GdalDataset {
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

        GDALRegister_VRT();

        poDriver = static_cast < GDALDriver * > (GDALGetDriverByName("VRT"));
    }

    public void AddBand(GDALDataType eType, String[] papszOptions) {
        m_bNeedsFlush = true;

        /* ==================================================================== */
        /*      Handle a new raw band.                                          */
        /* ==================================================================== */
        String pszSubClass = CSLFetchNameValue(papszOptions, "subclass");

        if (pszSubClass != null && pszSubClass.equals("VRTRawRasterBand")) {
        const int nWordDataSize = GDALGetDataTypeSizeBytes(eType);

            /* -------------------------------------------------------------------- */
            /*      Collect required information.                                   */
            /* -------------------------------------------------------------------- */
            String pszImageOffset =
                    CSLFetchNameValueDef(papszOptions, "ImageOffset", "0");
            vsi_l_offset nImageOffset = CPLScanUIntBig(
                    pszImageOffset, static_cast < int>(strlen(pszImageOffset)) );

            int nPixelOffset = nWordDataSize;
            String pszPixelOffset =
                    CSLFetchNameValue(papszOptions, "PixelOffset");
            if (pszPixelOffset != null)
                nPixelOffset = atoi(pszPixelOffset);

            int nLineOffset;
            String pszLineOffset =
                    CSLFetchNameValue(papszOptions, "LineOffset");
            if (pszLineOffset != null)
                nLineOffset = atoi(pszLineOffset);
            else {
                if (nPixelOffset > Integer.MAX_VALUE / GetRasterXSize() ||
                        nPixelOffset < Integer.MIN_VALUE / GetRasterXSize()) {
                    throw new RuntimeException("Int overflow");
                    return;
                }
                nLineOffset = nPixelOffset * GetRasterXSize();
            }

            String pszByteOrder =
                    CSLFetchNameValue(papszOptions, "ByteOrder");

            String pszFilename =
                    CSLFetchNameValue(papszOptions, "SourceFilename");
            if (pszFilename == null) {
                throw new RuntimeException("AddBand() requires a SourceFilename option for VRTRawRasterBands.");
                return;
            }

            boolean bRelativeToVRT =
                    CPLFetchBool(papszOptions, "relativeToVRT", false);

            /* -------------------------------------------------------------------- */
            /*      Create and initialize the band.                                 */
            /* -------------------------------------------------------------------- */

            VrtRawRasterBand poBand =
                    new VrtRawRasterBand(this, GetRasterCount() + 1, eType);

            String l_pszVRTPath = CPLStrdup(CPLGetPath(GetDescription()));
            if (l_pszVRTPath.equals("")) {
                l_pszVRTPath = null;
            }

        const CPLErr eErr =
                    poBand -> SetRawLink(pszFilename, l_pszVRTPath, bRelativeToVRT,
                            nImageOffset, nPixelOffset, nLineOffset,
                            pszByteOrder);
            CPLFree(l_pszVRTPath);
            if (eErr != CE_None) {
                delete poBand;
                return eErr;
            }

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
                const GDALDataType eTransferType =
                            GDALGetDataTypeByName(pszTransferTypeName);
                    if (eTransferType == GDALDataType.GDT_Unknown) {
                        throw new RuntimeException(String.format("invalid SourceTransferType: \"%s\".", pszTransferTypeName));
                        return;
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

                    poBand.AddFuncSource(pfnReadFunc, pCBData, dfNoDataValue);
                }
            }

            return;
        }
    }
}
