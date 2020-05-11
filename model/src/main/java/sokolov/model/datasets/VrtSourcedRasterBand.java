package sokolov.model.datasets;

import sokolov.model.sources.VrtComplexSource;
import sokolov.model.sources.VrtSimpleSource;
import sokolov.model.sources.VrtSource;

public class VrtSourcedRasterBand extends VrtRasterBand {

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


    public void AddSource(VrtSource poNewSource){
        nSources++;

        papoSources = static_cast<VRTSource **>(
                CPLRealloc( papoSources, sizeof(void*) * nSources ) );
        papoSources[nSources-1] = poNewSource;

        poDS.SetNeedsFlush();

        if( poNewSource.IsSimpleSource() )
        {
            VrtSimpleSource poSS = (VrtSimpleSource) poNewSource;
            if( GetMetadataItem("NBITS", "IMAGE_STRUCTURE") != null)
            {
                int nBits = atoi(GetMetadataItem("NBITS", "IMAGE_STRUCTURE"));
                if( nBits >= 1 && nBits <= 31 )
                {
                    poSS.SetMaxValue( static_cast<int>((1U << nBits) -1) );
                }
            }

            CheckSource( poSS );
        }
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
}
