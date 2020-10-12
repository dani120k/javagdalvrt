package sokolov.model.datasets;

import sokolov.model.common.SupportMethods;
import sokolov.model.enums.*;
import sokolov.model.supclasses.CPLXMLNode;
import sokolov.model.supclasses.GDALRasterAttributeTable;
import sokolov.model.supclasses.VRTOverviewInfo;
import sokolov.model.xmlmodel.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VrtRasterBand extends GdalRasterBand {
    boolean m_bIsMaskBand;
    boolean m_bNoDataValueSet;
    // If set to true, will not report the existence of nodata.
    boolean m_bHideNoDataValue;
    double m_dfNoDataValue;

    GDALColorTableH m_poColorTable;

    GDALColorInterp m_eColorInterp;

    String m_pszUnitType;
    String[] m_papszCategoryNames;

    double m_dfOffset;
    double m_dfScale;

    CPLXMLNode m_psSavedHistograms;

    List<VRTOverviewInfo> m_apoOverviews;

    VrtRasterBand m_poMaskBand;

    GDALRasterAttributeTable m_poRAT;

    public void Initialize(int nXSize, int nYSize) {
        poDS = null;
        nBand = 0;
        eAccess = GdalAccess.GA_ReadOnly;
        eDataType = GDALDataType.GDT_Byte;

        nRasterXSize = nXSize;
        nRasterYSize = nYSize;

        nBlockXSize.set(Math.min(128, nXSize));
        nBlockYSize.set(Math.min(128, nYSize));

        m_bIsMaskBand = false;
        m_bNoDataValueSet = false;
        m_bHideNoDataValue = false;
        m_dfNoDataValue = -10000.0;
        //TODO m_poColorTable.reset();
        m_eColorInterp = GDALColorInterp.GCI_Undefined;
        //TODO m_poRAT.reset();

        m_pszUnitType = null;
        m_papszCategoryNames = null;
        m_dfOffset = 0.0;
        m_dfScale = 1.0;

        m_psSavedHistograms = null;

        m_poMaskBand = null;
    }

    public void SetOffset(double dfNewOffset) {
        poDS.SetNeedsFlush();

        m_dfOffset = dfNewOffset;
    }

    public void SetScale(double dfNewScale) {
        poDS.SetNeedsFlush();

        m_dfScale = dfNewScale;
    }


    public boolean XMLInit(VRTRasterBandType vrtRasterBand, String pszVRTPathIn, VrtDataset vrtDataset, Map<String, GdalDataset> m_oMapSharedSources) {

        /* -------------------------------------------------------------------- */
        /*      Set the band if provided as an attribute.                       */
        /* -------------------------------------------------------------------- */
        Integer pszBand = vrtRasterBand.getBand();
        if (pszBand != null) {
            int nNewBand = pszBand;
            if (nNewBand != nBand) {
                throw new RuntimeException(String.format("Invalid band number. Got %s, expected %d. Ignoring provided one, and using %d instead", pszBand, nBand, nBand));
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Set the band if provided as an attribute.                       */
        /* -------------------------------------------------------------------- */
        DataTypeType pszDataType = vrtRasterBand.getDataType();
        if (pszDataType != null) {
            eDataType = getDataTypeByName(pszDataType);

            if (eDataType == GDALDataType.GDT_Unknown)
                throw new RuntimeException(String.format("Invalid dataType = %s", pszDataType.getValue()));
        }

        /* -------------------------------------------------------------------- */
        /*      Apply any band level metadata.                                  */
        /* -------------------------------------------------------------------- */
        //oMDMD.XMLInit( psTree, TRUE );

        /* -------------------------------------------------------------------- */
        /*      Collect various other items of metadata.                        */
        /* -------------------------------------------------------------------- */
        SetDescription(vrtRasterBand.getDescription());

        Double noDataValue = (vrtRasterBand.getNoDataValue() != null) ? Double.parseDouble(vrtRasterBand.getNoDataValue()) : 0;
        if (noDataValue != null)
            SetNoDataValue(noDataValue);

        Integer hideNoDataValue = vrtRasterBand.getHideNoDataValue();

        if (hideNoDataValue != null)
            m_bHideNoDataValue = (hideNoDataValue == 1) ? true : false;

        SetUnitType(vrtRasterBand.getUnitType());

        Double offset = vrtRasterBand.getOffset();
        if (offset != null)
            SetOffset(vrtRasterBand.getOffset());
        else
            SetOffset(0.0);

        Double scale = vrtRasterBand.getScale();
        if (scale != null)
            SetScale(scale);
        else
            SetScale(1.0);

        ColorInterpType colorInterp = vrtRasterBand.getColorInterp();

        if (colorInterp != null) {
            String pszInterp = colorInterp.getValue();
            SetColorInterpretation(GetColorInterpretationByName(pszInterp));
        }

        /* -------------------------------------------------------------------- */
        /*      Category names.                                                 */
        /* -------------------------------------------------------------------- */
        if (vrtRasterBand.getCategoryNames() != null) {
            m_papszCategoryNames = null;
            CategoryNamesType categoryNames = vrtRasterBand.getCategoryNames();

            String[] categories = new String[categoryNames.getCategoryList().size()];
            categories = categoryNames.getCategoryList().toArray(categories);
            m_papszCategoryNames = categories;
        }

        /* -------------------------------------------------------------------- */
        /*      Collect a color table.                                          */
        /* -------------------------------------------------------------------- */
        if (vrtRasterBand.getColorTable() != null) {
            GDALColorTableH oTable = new GDALColorTableH();

            ColorTableType colorTable = vrtRasterBand.getColorTable();

            List<ColorTableEntryType> colorTableEntryTypeList = colorTable.getColorTableEntryTypeList();
            int iEntry = 0;
            for (ColorTableEntryType colorTableEntryType : colorTableEntryTypeList) {
                GDALColorEntry gdalColorEntry = new GDALColorEntry();

                gdalColorEntry.c1 = (colorTableEntryType.getC1() != null) ? colorTableEntryType.getC1() : 0;
                gdalColorEntry.c2 = (colorTableEntryType.getC2() != null) ? colorTableEntryType.getC2() : 0;
                gdalColorEntry.c3 = (colorTableEntryType.getC3() != null) ? colorTableEntryType.getC3() : 0;
                gdalColorEntry.c4 = (colorTableEntryType.getC4() != null) ? colorTableEntryType.getC4() : 255;

                oTable.setColorEntry(iEntry++, gdalColorEntry);
            }

            SetColorTable(oTable);
        }


        /* -------------------------------------------------------------------- */
        /*      Raster Attribute Table                                          */
        /* -------------------------------------------------------------------- */
        GDALRasterAttributeTableType psRAT = vrtRasterBand.getGdalRasterAttributeTable();
        if (psRAT != null) {
            m_poRAT = new GDALDefaultRasterAttributeTable();
            m_poRAT.XMLInit(psRAT, "");
        }

        /* -------------------------------------------------------------------- */
        /*      Histograms                                                      */
        /* -------------------------------------------------------------------- */
        HistogramsType psHist = vrtRasterBand.getHistograms();
        if (psHist != null) {
            /*CPLXMLNode *psNext = psHist->psNext;
            psHist->psNext = nullptr;

            m_psSavedHistograms = CPLCloneXMLTree( psHist );
            psHist->psNext = psNext;*/
        }

        /* ==================================================================== */
        /*      Overviews                                                       */
        /* ==================================================================== */
        OverviewType overview = vrtRasterBand.getOverview();
        /*for( ; psNode != nullptr; psNode = psNode->psNext )
        {
            if( psNode->eType != CXT_Element
                    || !EQUAL(psNode->pszValue,"Overview") )
                continue;

            / -------------------------------------------------------------------- /
            /      Prepare filename.                                               /
            / -------------------------------------------------------------------- /
            CPLXMLNode* psFileNameNode=CPLGetXMLNode(psNode,"SourceFilename");
        const char *pszFilename =
                psFileNameNode ? CPLGetXMLValue(psFileNameNode,nullptr, nullptr) : nullptr;

            if( pszFilename == nullptr )
            {
                CPLError( CE_Warning, CPLE_AppDefined,
                        "Missing <SourceFilename> element in Overview." );
                return CE_Failure;
            }

            if (STARTS_WITH_CI(pszFilename, "MEM:::") && pszVRTPath != nullptr &&
                    !CPLTestBool(CPLGetConfigOption("VRT_ALLOW_MEM_DRIVER", "NO")))
            {
                CPLError( CE_Failure, CPLE_AppDefined,
                        "<SourceFilename> points to a MEM dataset, which is rather suspect! "
                        "If you know what you are doing, define the VRT_ALLOW_MEM_DRIVER configuration option to YES" );
                return CE_Failure;
            }

            char *pszSrcDSName = nullptr;
            if( pszVRTPath != nullptr
                    && atoi(CPLGetXMLValue( psFileNameNode, "relativetoVRT", "0")) )
            {
                pszSrcDSName = CPLStrdup(
                        CPLProjectRelativeFilename( pszVRTPath, pszFilename ) );
            }
            else
                pszSrcDSName = CPLStrdup( pszFilename );

            / -------------------------------------------------------------------- /
            /      Get the raster band.                                            /
            / -------------------------------------------------------------------- /
        const int nSrcBand = atoi(CPLGetXMLValue( psNode, "SourceBand", "1" ) );

            m_apoOverviews.resize( m_apoOverviews.size() + 1 );
            m_apoOverviews.back().osFilename = pszSrcDSName;
            m_apoOverviews.back().nBand = nSrcBand;

            CPLFree( pszSrcDSName );
        }*/

        /* ==================================================================== */
        /*      Mask band (specific to that raster band)                        */
        /* ==================================================================== */
        MaskBandType psMaskBandNode = vrtRasterBand.getMaskBand();
        VRTRasterBandType psNode = null;

        if (psMaskBandNode != null) {
            psNode = psMaskBandNode.getVrtRasterBand();
        }

        if (psNode != null) {
            if (((VrtDataset) poDS).m_poMaskBand != null) {
                throw new RuntimeException("Illegal mask band at raster band level when a dataset mask band already exists.");
            }

            String pszSubclass = psNode.getSubClass().getValue();

            VrtRasterBand poBand = null;

            if (pszSubclass.equals("VRTSourcedRasterBand"))
                poBand = new VrtSourcedRasterBand(GetDataset(), 0);
            else if (pszSubclass.equals("VRTDerivedRasterBand"))
                poBand = new VrtDerivedRasterBand(GetDataset(), 0);
            else if (pszSubclass.equals("VRTRawRasterBand"))
                poBand = new VrtRawRasterBand(GetDataset(), 0, GDALDataType.GDT_Unknown);
            else if (pszSubclass.equals("VRTWarpedRasterBand"))
                poBand = new VrtWarpedRasterBand(GetDataset(), 0, GDALDataType.GDT_Unknown);
            else {
                throw new RuntimeException(String.format("VRTRasterBand of unrecognized subclass '%s'", pszSubclass));
            }

            //pUniqueHandle ==> null TODO check sources
            if (poBand.XMLInit(psNode, pszVRTPathIn, null, m_oMapSharedSources)) {
                SetMaskBand(poBand);
            } else {
                //clear();
            }
        }

        return true;
    }

    private void SetMaskBand(VrtRasterBand poMaskBandIn) {
        m_poMaskBand = null;
        m_poMaskBand = poMaskBandIn;
        m_poMaskBand.SetIsMaskBand();
    }

    public void SetIsMaskBand() {
        nBand = 0;
        m_bIsMaskBand = true;
    }

    private GDALColorInterp GetColorInterpretationByName(String pszInterp) {
        switch (pszInterp) {
            case "Gray":
                return GDALColorInterp.GCI_GrayIndex;
            case "Palette":
                return GDALColorInterp.GCI_PaletteIndex;
            case "Red":
                return GDALColorInterp.GCI_RedBand;
            case "Green":
                return GDALColorInterp.GCI_GreenBand;
            case "Blue":
                return GDALColorInterp.GCI_BlueBand;
            case "Alpha":
                return GDALColorInterp.GCI_AlphaBand;
            case "Hue":
                return GDALColorInterp.GCI_HueBand;
            case "Saturation":
                return GDALColorInterp.GCI_SaturationBand;
            case "Lightness":
                return GDALColorInterp.GCI_LightnessBand;
            case "Cyan":
                return GDALColorInterp.GCI_CyanBand;
            case "Magenta":
                return GDALColorInterp.GCI_MagentaBand;
            case "Yellow":
                return GDALColorInterp.GCI_YellowBand;
            case "Black":
                return GDALColorInterp.GCI_BlackBand;
            default:
                return GDALColorInterp.GCI_Undefined;
        }
    }

    private GDALDataType getDataTypeByName(DataTypeType pszDataType) {
        switch (pszDataType.getValue()) {
            case "Byte":
                return GDALDataType.GDT_Byte;
            case "UInt16":
                return GDALDataType.GDT_UInt16;
            case "Int16":
                return GDALDataType.GDT_Int16;
            case "UInt32":
                return GDALDataType.GDT_UInt32;
            case "Int32":
                return GDALDataType.GDT_Int32;
            case "Float32":
                return GDALDataType.GDT_Float32;
            case "Float64":
                return GDALDataType.GDT_Float64;
            case "CInt16":
                return GDALDataType.GDT_CInt16;
            case "CInt32":
                return GDALDataType.GDT_CInt32;
            case "CFloat32":
                return GDALDataType.GDT_CFloat32;
            case "CFloat64":
                return GDALDataType.GDT_CFloat64;
            default:
                return GDALDataType.GDT_Unknown;
        }
    }

    private boolean SetUnitType(String pszNewValue) {
        poDS.SetNeedsFlush();

        m_pszUnitType = null;

        if (pszNewValue == null)
            m_pszUnitType = null;
        else
            m_pszUnitType = pszNewValue;

        return true;
    }

    private boolean SetNoDataValue(Double dfNewValue) {
        if (eDataType == GDALDataType.GDT_Float32)
            dfNewValue = GDALAdjustNoDataCloseToFloatMax(dfNewValue);

        m_bNoDataValueSet = true;
        m_dfNoDataValue = dfNewValue;

        poDS.SetNeedsFlush();

        return true;
    }

    private static Double GDALAdjustNoDataCloseToFloatMax(Double dfVal) {
        Double kMaxFloat = Double.MAX_VALUE;
        if (Math.abs(dfVal - -kMaxFloat) < 1e-10 * kMaxFloat)
            return -kMaxFloat;

        if (Math.abs(dfVal - kMaxFloat) < 1e-10 * kMaxFloat)
            return kMaxFloat;

        return dfVal;
    }

    public VRTRasterBandType SerializeToXML(VRTDataset vrtDataset, String pszVRTPathIn) {
        VRTRasterBandType vrtRasterBandType = new VRTRasterBandType();

        /* -------------------------------------------------------------------- */
        /*      Various kinds of metadata.                                      */
        /* -------------------------------------------------------------------- */
        DataTypeType dataTypeType = DataTypeType.valueOf(GDALDataType.getValue(GetRasterDataType()));

        vrtRasterBandType.setDataType(dataTypeType);

        if (nBand > 0)
            //TODO CPLSetXMLValue( psTree, "#band", CPLSPrintf( "%d", GetBand() ) );
            vrtRasterBandType.setBand(nBand);

        /*CPLXMLNode *psMD = oMDMD.Serialize();
        if( psMD != nullptr )
        {
            CPLAddXMLChild( psTree, psMD );
        }*/

        if (getDescription() != null && getDescription().length() > 0)
            vrtRasterBandType.setDescription(getDescription());

        if (m_bNoDataValueSet)
            vrtRasterBandType.setNoDataValue(SupportMethods.VRTSerializeNoData(m_dfNoDataValue, eDataType, 16));

        if (m_bHideNoDataValue)
            vrtRasterBandType.setHideNoDataValue((m_bHideNoDataValue) ? 1 : 0);

        if (m_pszUnitType != null)
            vrtRasterBandType.setUnitType(m_pszUnitType);

        if (m_dfOffset != 0.0)
            vrtRasterBandType.setOffset(m_dfOffset);

        if (m_dfScale != 1.0)
            vrtRasterBandType.setScale(m_dfScale);

        if (m_eColorInterp != GDALColorInterp.GCI_Undefined)
            vrtRasterBandType.setColorInterp(GDALColorInterp.getValue(m_eColorInterp));

        /* -------------------------------------------------------------------- */
        /*      Category names.                                                 */
        /* -------------------------------------------------------------------- */
        if (m_papszCategoryNames != null) {
            CategoryNamesType categoryNamesType = new CategoryNamesType();

            List<String> categoryList = new ArrayList<>();
            for (int iEntry = 0; m_papszCategoryNames[iEntry] != null; iEntry++) {
                categoryList.add(m_papszCategoryNames[iEntry]);
            }
            categoryNamesType.setCategoryList(categoryList);

            vrtRasterBandType.setCategoryNames(categoryNamesType);
        }

        /* -------------------------------------------------------------------- */
        /*      Histograms.                                                     */
        /* -------------------------------------------------------------------- */
        if (m_psSavedHistograms != null)
            //TODO vrtRasterBandType.setHistograms(m_psSavedHistograms);

        /* -------------------------------------------------------------------- */
        /*      Color Table.                                                    */
        /* -------------------------------------------------------------------- */
        if (m_poColorTable != null) {
            ColorTableType colorTableType = new ColorTableType();

            List<ColorTableEntryType> colorTableEntryTypeList = new ArrayList<>();
            for (int i = 0; i < m_poColorTable.GetColorEntryCount(); i++) {
                ColorTableEntryType colorTableEntryType = new ColorTableEntryType();

                GDALColorEntry gdalColorEntry = new GDALColorEntry();
                m_poColorTable.GetColorEntryAsRGB(i, gdalColorEntry);

                colorTableEntryType.setC1(gdalColorEntry.c1);
                colorTableEntryType.setC2(gdalColorEntry.c2);
                colorTableEntryType.setC3(gdalColorEntry.c3);
                colorTableEntryType.setC4(gdalColorEntry.c4);
            }
            colorTableType.setColorTableEntryTypeList(colorTableEntryTypeList);

            vrtRasterBandType.setColorTable(colorTableType);
        }

        /* -------------------------------------------------------------------- */
        /*      Raster Attribute Table                                          */
        /* -------------------------------------------------------------------- */
        if (m_poRAT != null) {
            GDALRasterAttributeTableType psSerializedRAT = m_poRAT.Serialize();

            if (psSerializedRAT != null)
                vrtRasterBandType.setGdalRasterAttributeTable(psSerializedRAT);
        }

        /* ==================================================================== */
        /*      Overviews                                                       */
        /* ==================================================================== */
        /*TODOOverviewType overviewType = new OverviewType();
        for (int iOvr = 0; iOvr < m_apoOverviews.size(); iOvr++) {
            boolean bRelativeToVRT = false;
            String pszRelativePath = null;
            VSIStatBufL sStat;

            if (VSIStatExL(m_apoOverviews[iOvr].osFilename, & sStat,VSI_STAT_EXISTS_FLAG ) !=0 )
            {
                pszRelativePath = m_apoOverviews[iOvr].osFilename;
                bRelativeToVRT = false;
            }
        else
            {
                pszRelativePath =
                        CPLExtractRelativePath(pszVRTPath, m_apoOverviews[iOvr].osFilename,
                                & bRelativeToVRT );
            }

            CPLSetXMLValue(psOVR_XML, "SourceFilename", pszRelativePath);

            CPLCreateXMLNode(
                    CPLCreateXMLNode(CPLGetXMLNode(psOVR_XML, "SourceFilename"),
                            CXT_Attribute, "relativeToVRT"),
                    CXT_Text, bRelativeToVRT ? "1" : "0");

            CPLSetXMLValue(psOVR_XML, "SourceBand",
                    CPLSPrintf("%d", m_apoOverviews[iOvr].nBand));


        }
        vrtRasterBandType.setOverview(overviewType);*/

        /* ==================================================================== */
        /*      Mask band (specific to that raster band)                        */
        /* ==================================================================== */
        if (m_poMaskBand != null){
            VRTRasterBandType psBandTree = m_poMaskBand.SerializeToXML(vrtDataset, pszVRTPathIn);

            if (psBandTree != null){
                MaskBandType maskBandType = new MaskBandType();
                maskBandType.setVrtRasterBand(psBandTree);

                vrtRasterBandType.setMaskBand(maskBandType);
            }
        }

        return vrtRasterBandType;
    }


}
