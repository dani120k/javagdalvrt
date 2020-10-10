package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.sources.VrtComplexSource;
import sokolov.model.sources.VrtSimpleSource;
import sokolov.model.sources.VrtSource;
import sokolov.model.supclasses.VRTImageReadFunc;
import sokolov.model.xmlmodel.SimpleSourceType;
import sokolov.model.xmlmodel.VRTDataset;
import sokolov.model.xmlmodel.VRTRasterBandType;

import java.util.ArrayList;
import java.util.List;

public class VrtSourcedRasterBand extends VrtRasterBand {
    int            m_nRecursionCounter;
    String      m_osLastLocationInfo;
    String[] m_papszSourceList;
    int            nSources;
    VrtSource[] papoSources; //TODO switch to List
    boolean            bSkipBufferInitialization;

    public VrtSourcedRasterBand(){

    }

    public VrtSourcedRasterBand(GdalDataset poDSIn, int nBandIn){
        this.m_nRecursionCounter = 0;
        this.m_papszSourceList = null;
        this.nSources = 0;
        this.papoSources = null;
        this.bSkipBufferInitialization = false;
        Initialize(poDSIn.GetRasterXSize(), poDSIn.GetRasterYSize());

        this.poDS = poDSIn;
        this.nBand = nBandIn;
    }

    public VrtSourcedRasterBand(GdalDataset gdalDataset,
                                int rasterCount,
                                GDALDataType eType,
                                int rasterXSize,
                                int rasterYSize){
        //TODO
    }

    public void configureSource(VrtSimpleSource poSimpleSource,
                                GdalRasterBand poSrcBand,
                                boolean bAddAsMaskBand,
                                double dfSrcXOff, double dfSrcYOff,
                                double dfSrcXSize, double dfSrcYSize,
                                double dfDstXOff, double dfDstYOff,
                                double dfDstXSize,
                                double dfDstYSize){
        /* -------------------------------------------------------------------- */
        /*      Default source and dest rectangles.                             */
        /* -------------------------------------------------------------------- */
        if( dfSrcYSize == -1 )
        {
            dfSrcXOff = 0;
            dfSrcYOff = 0;
            dfSrcXSize = poSrcBand.GetXSize();
            dfSrcYSize = poSrcBand.GetYSize();
        }

        if( dfDstYSize == -1 )
        {
            dfDstXOff = 0;
            dfDstYOff = 0;
            dfDstXSize = nRasterXSize;
            dfDstYSize = nRasterYSize;
        }

        if( bAddAsMaskBand )
            poSimpleSource.SetSrcMaskBand( poSrcBand );
        else
            poSimpleSource.SetSrcBand( poSrcBand );

        poSimpleSource.SetSrcWindow( dfSrcXOff, dfSrcYOff,
                dfSrcXSize, dfSrcYSize );
        poSimpleSource.SetDstWindow( dfDstXOff, dfDstYOff,
                dfDstXSize, dfDstYSize );

        CheckSource( poSimpleSource );

        /* -------------------------------------------------------------------- */
        /*      If we can get the associated GDALDataset, add a reference to it.*/
        /* -------------------------------------------------------------------- */
        if( poSrcBand.GetDataset() != null )
            poSrcBand.GetDataset().Reference();
    }


    public void AddFuncSource(VRTImageReadFunc vrtImageReadFunc,
                              double dfNoDataValue
                              ){

    }

    public void AddSource(VrtSource poNewSource){
        nSources++;

        if (nSources > 1) {
            VrtSource[] vrtSourceArray = new VrtSource[nSources];
            for (int i = 0; i < nSources - 1; i++) {
                vrtSourceArray[i] = papoSources[i];
            }

            papoSources = null;
            papoSources = vrtSourceArray;
        } else {
            papoSources = new VrtSource[nSources];
        }

        papoSources[nSources-1] = poNewSource;

        poDS.SetNeedsFlush();

        if( poNewSource.IsSimpleSource() )
        {
            VrtSimpleSource poSS = (VrtSimpleSource) poNewSource;
            if( GetMetadataItem("NBITS", "IMAGE_STRUCTURE") != null)
            {
                int nBits = Integer.parseInt(GetMetadataItem("NBITS", "IMAGE_STRUCTURE"));
                if( nBits >= 1 && nBits <= 31 )
                {
                    poSS.SetMaxValue( (int)((1 << nBits) -1) );
                }
            }

            CheckSource( poSS );
        }
    }

    private void CheckSource(VrtSimpleSource poSS) {

    }

    public void AddComplexSource(GdalRasterBand poSrcBand,
                                 double dfSrcXOff, double dfSrcYOff,
                                 double dfSrcXSize, double dfSrcYSize,
                                 double dfDstXOff, double dfDstYOff,
                                 double dfDstXSize, double dfDstYSize,
                                 double dfScaleOff,
                                 double dfScaleRatio,
                                 double dfNoDataValueIn,
                                 int nColorTableComponent){
        /* -------------------------------------------------------------------- */
        /*      Create source.                                                  */
        /* -------------------------------------------------------------------- */
        VrtComplexSource poSource = new VrtComplexSource();

        configureSource( poSource,
                poSrcBand,
                false,
                dfSrcXOff, dfSrcYOff,
                dfSrcXSize, dfSrcYSize,
                dfDstXOff, dfDstYOff,
                dfDstXSize, dfDstYSize );

        /* -------------------------------------------------------------------- */
        /*      Set complex parameters.                                         */
        /* -------------------------------------------------------------------- */
        if( dfNoDataValueIn != VRT_NODATA_UNSET )
            poSource.setNoDataValue( dfNoDataValueIn );

        if( dfScaleOff != 0.0 || dfScaleRatio != 1.0 )
            poSource.SetLinearScaling(dfScaleOff, dfScaleRatio);

        poSource.SetColorTableComponent(nColorTableComponent);

        /* -------------------------------------------------------------------- */
        /*      add to list.                                                    */
        /* -------------------------------------------------------------------- */
        AddSource( poSource );
    }

    public VRTRasterBandType SerializeToXML(VRTDataset vrtDataset, String pszVrtPath){
        VRTRasterBandType vrtRasterBandType = super.SerializeToXML(vrtDataset, pszVrtPath);

        /* -------------------------------------------------------------------- */
        /*      Process Sources.                                                */
        /* -------------------------------------------------------------------- */
        List<SimpleSourceType> simpleSourceTypeList = new ArrayList<>();

        for(int iSource = 0; iSource < nSources; iSource++){
            //TODO can be another type?
            //check for null is only for test
            SimpleSourceType psXMLSrc = null;
            if (papoSources[iSource] != null)
                psXMLSrc = papoSources[iSource].serializeToXML(vrtRasterBandType, this, pszVrtPath);

            if (psXMLSrc != null){
                simpleSourceTypeList.add(psXMLSrc);
            }
        }

        vrtRasterBandType.setSimpleSource(simpleSourceTypeList);

        return vrtRasterBandType;
    }
}
