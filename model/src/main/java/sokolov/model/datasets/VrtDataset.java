package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.GDAL_GCP;
import sokolov.model.supclasses.VRTGroup;
import sokolov.model.supclasses.VRTImageReadFunc;
import sokolov.model.xmlmodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sokolov.model.enums.OSRAxisMappingStrategy.OAMS_TRADITIONAL_GIS_ORDER;

public class VrtDataset extends GdalDataset {
    public double VRT_NODATA_UNSET = -1234.56;
    boolean m_bGeoTransformSet;
    double[] m_adfGeoTransform = new double[6];
    int m_nGCPCount;
    GDAL_GCP m_pasGCPList;
    OGRSpatialReference m_poGCP_SRS = null;
    OGRSpatialReference m_poSRS = null;

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

    public void initDataset(VRTDataset poDS, String pszVRTPathIn) {
        if (pszVRTPathIn != null)
            m_pszVRTPath = pszVRTPathIn;

        /* -------------------------------------------------------------------- */
        /*      Check for an SRS node.                                          */
        /* -------------------------------------------------------------------- */
        if (poDS.getSrs() != null) {
            //TODO
            //if (m_poSRS != null)
            //m_poSRS.Release()
            m_poSRS = new OGRSpatialReference();
            //m_poSRS.setFromUserInput(poDS.getSrs());
            String pszMapping = poDS.getSrs().getDataAxisToSRSAxisMapping();

            if (pszMapping != null) {
                //m_poSRS.setDataAxisToSRSAxisMapping(anMapping);
            } else {
                m_poSRS.SetAxisMappingStrategy(OAMS_TRADITIONAL_GIS_ORDER);
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Check for a GeoTransform node.                                  */
        /* -------------------------------------------------------------------- */
        if (poDS.getGeoTransform() != null) {
            String pszGT = poDS.getGeoTransform();
            String[] papszTokens = pszGT.split(",");

            if (papszTokens.length != 6) {
                throw new RuntimeException("GeoTransform node does not have expected six values.");
            } else {
                for (int i = 0; i < papszTokens.length; i++)
                    m_adfGeoTransform[i] = Double.parseDouble(papszTokens[i]);

                m_bGeoTransformSet = true;
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Check for GCPs.                                                 */
        /* -------------------------------------------------------------------- */
        if (poDS.getGcpList() != null) {
            /*GDALDeserializeGCPListFromXML( psGCPList,
                    &m_pasGCPList,
                                       &m_nGCPCount,
                                       &m_poGCP_SRS );*/
        }

        /* -------------------------------------------------------------------- */
        /*      Apply any dataset level metadata.                               */
        /* -------------------------------------------------------------------- */
        //TODO
        //oMDMD.XMLInit();

        /* -------------------------------------------------------------------- */
        /*      Create dataset mask band.                                       */
        /* -------------------------------------------------------------------- */
        MaskBandType maskBand = poDS.getMaskBand();

        if (maskBand != null && maskBand.getVrtRasterBand() != null) {
            VRTRasterBandType vrtRasterBand = maskBand.getVrtRasterBand();
            String pszSubClass = vrtRasterBand.getSubClass().name();

            VrtRasterBand poBand = InitBand(pszSubClass, 0, false);

            if (poBand != null &&
                    poBand.XMLInit(vrtRasterBand, pszVRTPathIn, this, m_oMapSharedSources)) {
                this.setMaskBand(poBand);
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Create band information objects.                                */
        /* -------------------------------------------------------------------- */
        int l_nBands = 0;
        List<VRTRasterBandType> vrtRasterBandList = poDS.getVrtRasterBand();
        for (VRTRasterBandType vrtRasterBand : vrtRasterBandList) {
            VrtRasterBand poBand = InitBand(vrtRasterBand.getSubClass().getValue(), l_nBands + 1, true);
            if (poBand != null && poBand.XMLInit(vrtRasterBand, pszVRTPathIn, this, m_oMapSharedSources)) {
                l_nBands++;
                SetBand(l_nBands, poBand);
            } else {
                //TODO clean objects
            }
        }


        GroupType group = poDS.getGroup();
        if (group != null) {
            String pszName = group.getName();

            if (pszName == null || !pszName.equals("/")) {
                throw new RuntimeException("Missing name or not equal to '/'");
            }

            //m_poRootGroup = new VRTGroup("/");
            //m_poRootGroup.setIsRootGroup();
            //if (!m_poRootGroup.XMLInit(m_poRootGroup, group, pszVRTPathIn))
            //    throw new RuntimeException();
        }

        return;
    }

    private void setMaskBand(VrtRasterBand poMaskBandIn) {
        m_poMaskBand = null;
        m_poMaskBand = poMaskBandIn;
        m_poMaskBand.SetIsMaskBand();
    }

    private VrtRasterBand InitBand(String pszSubclass, int nBand, boolean bAllowPansharpened) {
        VrtRasterBand poBand = null;

        if (pszSubclass.equals("VRTSourcedRasterBand"))
            poBand = new VrtSourcedRasterBand(this, nBand);
        else if (pszSubclass.equals("VRTDerivedRasterBand"))
            poBand = new VrtDerivedRasterBand(this, nBand);
        else if (pszSubclass.equals("VRTRawRasterBand"))
            poBand = new VrtRawRasterBand(this, nBand, GDALDataType.GDT_Unknown);
        else if (pszSubclass.equals("VRTWarpedRasterBand")) //todo check that dynamic_cast<VRTWarpedDataset*>(this) != nullptr  )
            poBand = new VrtWarpedRasterBand(this, nBand, GDALDataType.GDT_Unknown);
        else if (bAllowPansharpened &&
                pszSubclass.equals("VRTPansharpenedRasterBand"))//todo check that dynamic_cast<VRTWarpedDataset*>(this) != nullptr  )
            poBand = new VrtPansharpenedRasterBand(this, nBand);
        else
            throw new RuntimeException(String.format("VRTRasterBand of unrecognized subclass '%s'.", pszSubclass));

        return poBand;
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

    public VRTDataset SerializeToXML(String pszVRTPathIn) {
        VRTDataset vrtDataset = new VRTDataset();

        if (m_poRootGroup != null)
            return m_poRootGroup.SerializeToXML(vrtDataset, pszVRTPathIn);

        /* -------------------------------------------------------------------- */
        /*      Setup root node and attributes.                                 */
        /* -------------------------------------------------------------------- */
        vrtDataset.setRasterXSize(GetRasterXSize());
        vrtDataset.setRasterYSize(GetRasterYSize());

        /* -------------------------------------------------------------------- */
        /*      SRS                                                             */
        /* -------------------------------------------------------------------- */
        if (m_poSRS != null && !m_poSRS.isEmpty()) {
            SRSType srsType = new SRSType();

            String pszWKT = m_poSRS.exportToWkt();
            //TODO srsType.setSRS(pszWKT);
            //TODO CPLCreateXMLElementAndValue( psDSTree, "SRS", pszWKT );

            List<Integer> mapping = m_poSRS.GetDataAxisToSRSAxisMapping();
            String osMapping = "";
            for (int i = 0; i < mapping.size(); i++) {
                if (osMapping.length() != 0)
                    osMapping += ",";
                osMapping += mapping.get(i);
            }
            srsType.setDataAxisToSRSAxisMapping(osMapping);

            vrtDataset.setSrs(srsType);
        }

        /* -------------------------------------------------------------------- */
        /*      Geotransform.                                                   */
        /* -------------------------------------------------------------------- */
        if (m_bGeoTransformSet) {
            vrtDataset.setGeoTransform(String.format("%d,%d,%d,%d,%d,%d",
                    m_adfGeoTransform[0],
                    m_adfGeoTransform[1],
                    m_adfGeoTransform[2],
                    m_adfGeoTransform[3],
                    m_adfGeoTransform[4],
                    m_adfGeoTransform[5]));
        }

        /* -------------------------------------------------------------------- */
        /*      Metadata                                                        */
        /* -------------------------------------------------------------------- */
        /*
            CPLXMLNode *psMD = oMDMD.Serialize();
    if( psMD != nullptr )
    {
        CPLAddXMLChild( psDSTree, psMD );
    }
         */

        if (m_nGCPCount > 0){
            GdalSerializeGCPListTOXML(vrtDataset, m_pasGCPList, m_nGCPCount, m_poGCP_SRS);
        }

        /* -------------------------------------------------------------------- */
        /*      Serialize bands.                                                */
        /* -------------------------------------------------------------------- */
        List<VRTRasterBandType> vrtRasterBandTypeList = new ArrayList<>();

        for(int iBand = 0; iBand < nBands; iBand++){
            VRTRasterBandType rasterBand = ((VrtRasterBand)papoBands[iBand]).SerializeToXML(vrtDataset, pszVRTPathIn);

            vrtRasterBandTypeList.add(rasterBand);
        }

        vrtDataset.setVrtRasterBand(vrtRasterBandTypeList);

        /* -------------------------------------------------------------------- */
        /*      Serialize dataset mask band.                                    */
        /* -------------------------------------------------------------------- */
        if (m_poMaskBand != null){
            VRTRasterBandType rasterBand = m_poMaskBand.SerializeToXML(vrtDataset, pszVRTPathIn);

            if (rasterBand != null){
                MaskBandType maskBandType = new MaskBandType();
                maskBandType.setVrtRasterBand(rasterBand);

                vrtDataset.setMaskBand(maskBandType);
            }
        }

        return vrtDataset;
    }

    private void GdalSerializeGCPListTOXML(VRTDataset vrtDataset, GDAL_GCP m_pasGCPList, int nGCPCount, OGRSpatialReference poGCP_SRS) {
        //TODO
        /*
        CPLString oFmt;

    CPLXMLNode *psPamGCPList = CPLCreateXMLNode( psParentNode, CXT_Element,
                                                 "GCPList" );

    CPLXMLNode* psLastChild = nullptr;

    if( poGCP_SRS != nullptr && !poGCP_SRS->IsEmpty() )
    {
        char* pszWKT = nullptr;
        poGCP_SRS->exportToWkt(&pszWKT);
        CPLSetXMLValue( psPamGCPList, "#Projection",
                        pszWKT );
        CPLFree(pszWKT);
        const auto& mapping = poGCP_SRS->GetDataAxisToSRSAxisMapping();
        CPLString osMapping;
        for( size_t i = 0; i < mapping.size(); ++i )
        {
            if( !osMapping.empty() )
                osMapping += ",";
            osMapping += CPLSPrintf("%d", mapping[i]);
        }
        CPLSetXMLValue(psPamGCPList, "#dataAxisToSRSAxisMapping",
                      osMapping.c_str());

        psLastChild = psPamGCPList->psChild->psNext;
    }

    for( int iGCP = 0; iGCP < nGCPCount; iGCP++ )
    {
        GDAL_GCP *psGCP = pasGCPList + iGCP;

        CPLXMLNode *psXMLGCP = CPLCreateXMLNode( nullptr, CXT_Element, "GCP" );

        if( psLastChild == nullptr )
            psPamGCPList->psChild = psXMLGCP;
        else
            psLastChild->psNext = psXMLGCP;
        psLastChild = psXMLGCP;

        CPLSetXMLValue( psXMLGCP, "#Id", psGCP->pszId );

        if( psGCP->pszInfo != nullptr && strlen(psGCP->pszInfo) > 0 )
            CPLSetXMLValue( psXMLGCP, "Info", psGCP->pszInfo );

        CPLSetXMLValue( psXMLGCP, "#Pixel",
                        oFmt.Printf( "%.4f", psGCP->dfGCPPixel ) );

        CPLSetXMLValue( psXMLGCP, "#Line",
                        oFmt.Printf( "%.4f", psGCP->dfGCPLine ) );

        CPLSetXMLValue( psXMLGCP, "#X",
                        oFmt.Printf( "%.12E", psGCP->dfGCPX ) );

        CPLSetXMLValue( psXMLGCP, "#Y",
                        oFmt.Printf( "%.12E", psGCP->dfGCPY ) );

        if( psGCP->dfGCPZ != 0.0 )
            CPLSetXMLValue( psXMLGCP, "#Z",
                    oFmt.Printf( "%.12E", psGCP->dfGCPZ ) );
    }
         */
    }
}
