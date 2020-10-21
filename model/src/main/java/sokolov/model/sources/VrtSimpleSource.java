package sokolov.model.sources;

import sokolov.model.datasets.GDALRasterIOExtraArg;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.datasets.GdalRasterBand;
import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.CPLHashSet;
import sokolov.model.xmlmodel.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VrtSimpleSource implements VrtSource {
    @Override
    public void RasterIO(GDALDataType eBandDataType, int nXOff, int nYOff, int nXSize, int nYSize, int nBufXSize, int nBufYSize, GDALDataType eBufType, long nPixelSpace, long nLineSpace, GDALRasterIOExtraArg psExtraArgIn) {

    }

    @Override
    public Double GetMinimum(int nXSize, int nYSize, Boolean pbSuccess) {
        return null;
    }

    @Override
    public Double GetMaximum(int nXSize, int nYSize, Boolean pbSuccess) {
        return null;
    }

    @Override
    public void ComputeRasterMinMax(int nXSize, int nYSize, int bApproxOK, Double adfMinMax) {

    }

    @Override
    public void ComputeStatistic(int nXSize, int nYSize, int bApproxOK, Double pdfMin, Double pdfMax, Double pdfMean, Double pdfStdDev) {

    }

    @Override
    public void GetHistogram(int nXSize, int nYSize, double dfMin, double dfMax, int nBuckets, int bIncludeOutOfRange, int bApproxOK) {
    }

    @Override
    public void GetFileList(List<List<String>> ppapszFileList, Integer pnSize, Integer pnMaxSize, List<CPLHashSet> hSetFiles) {
    }

    @Override
    public boolean IsSimpleSource() {
        return false;
    }

    @Override
    public void FlushCache() {
    }


    GdalRasterBand m_poRasterBand;
    // When poRasterBand is a mask band, poMaskBandMainBand is the band
    // from which the mask band is taken.
    GdalRasterBand m_poMaskBandMainBand;

    double m_dfSrcXOff;
    double m_dfSrcYOff;
    double m_dfSrcXSize;
    double m_dfSrcYSize;

    double m_dfDstXOff;
    double m_dfDstYOff;
    double m_dfDstXSize;
    double m_dfDstYSize;

    boolean m_bNoDataSet;
    double m_dfNoDataValue;
    String m_osResampling;

    int m_nMaxValue;

    boolean m_bRelativeToVRTOri;
    String m_osSourceFileNameOri;

    int m_nExplicitSharedStatus; // -1 unknown, 0 = unshared, 1 = shared

    public VrtSimpleSource() {
        m_poRasterBand = null;
        m_poMaskBandMainBand = null;
        m_dfSrcXOff = 0.0;
        m_dfSrcYOff = 0.0;
        m_dfSrcXSize = 0.0;
        m_dfSrcYSize = 0.0;
        m_dfDstXOff = 0.0;
        m_dfDstYOff = 0.0;
        m_dfDstXSize = 0.0;
        m_dfDstYSize = 0.0;
        m_bNoDataSet = false;
        //m_dfNoDataValue = VRT_NODATA_UNSET;
        m_nMaxValue = 0;
        m_bRelativeToVRTOri = false;
        m_nExplicitSharedStatus = -1;
    }

    public VrtSimpleSource(VrtSimpleSource poSrcSource,
                           Double dfXDstRatio, Double dfYDstRatio) {
        m_poRasterBand = poSrcSource.m_poRasterBand;
        m_poMaskBandMainBand = poSrcSource.m_poMaskBandMainBand;
        m_dfSrcXOff = poSrcSource.m_dfSrcXOff;
        m_dfSrcYOff = poSrcSource.m_dfSrcYOff;
        m_dfSrcXSize = poSrcSource.m_dfSrcXSize;
        m_dfSrcYSize = poSrcSource.m_dfSrcYSize;
        m_dfDstXOff = poSrcSource.m_dfDstXOff * dfXDstRatio;
        m_dfDstYOff = poSrcSource.m_dfDstYOff * dfYDstRatio;
        m_dfDstXSize = poSrcSource.m_dfDstXSize * dfXDstRatio;
        m_dfDstYSize = poSrcSource.m_dfDstYSize * dfYDstRatio;
        m_bNoDataSet = poSrcSource.m_bNoDataSet;
        m_dfNoDataValue = poSrcSource.m_dfNoDataValue;
        m_nMaxValue = poSrcSource.m_nMaxValue;
        m_bRelativeToVRTOri = false;
        m_nExplicitSharedStatus = poSrcSource.m_nExplicitSharedStatus;
    }

    public void flushCache() {
        if (m_poMaskBandMainBand != null) {
            m_poMaskBandMainBand.flushCache();
        } else if (m_poRasterBand != null) {
            m_poRasterBand.flushCache();
        }
    }

    public void UnsetPreservedRelativeFilenames() {
        m_bRelativeToVRTOri = false;
        m_osSourceFileNameOri = "";
    }


    public void setNoDataValue(double dfNewNoDataValue) {
        if (dfNewNoDataValue == VRT_NODATA_UNSET) {
            m_bNoDataSet = false;
            m_dfNoDataValue = VRT_NODATA_UNSET;
            return;
        }

        m_bNoDataSet = true;
        m_dfNoDataValue = dfNewNoDataValue;
    }

    public void setResampling(String pszResampling) {
        m_osResampling = (pszResampling != null) ? pszResampling : "";
    }

    public void SetSrcMaskBand(GdalRasterBand poNewSrcBand) {
        m_poRasterBand = poNewSrcBand.GetMaskBand();
        m_poMaskBandMainBand = poNewSrcBand;
    }

    public void SetSrcBand(GdalRasterBand poNewSrcBand) {
        m_poRasterBand = poNewSrcBand;
    }

    public void SetSrcWindow(double dfNewXOff, double dfNewYOff, double dfNewXSize, double dfNewYSize) {
        m_dfSrcXOff = RoundIfCloseToInt(dfNewXOff);
        m_dfSrcYOff = RoundIfCloseToInt(dfNewYOff);
        m_dfSrcXSize = RoundIfCloseToInt(dfNewXSize);
        m_dfSrcYSize = RoundIfCloseToInt(dfNewYSize);
    }

    public void SetDstWindow(double dfNewXOff, double dfNewYOff, double dfNewXSize, double dfNewYSize) {
        m_dfDstXOff = RoundIfCloseToInt(dfNewXOff);
        m_dfDstYOff = RoundIfCloseToInt(dfNewYOff);
        m_dfDstXSize = RoundIfCloseToInt(dfNewXSize);
        m_dfDstYSize = RoundIfCloseToInt(dfNewYSize);
    }

    public void SetMaxValue(int i) {

    }

    static double RoundIfCloseToInt(double dfValue) {
        double dfClosestInt = Math.floor(dfValue + 0.5);
        return (Math.abs(dfValue - dfClosestInt) < 1e-3) ? dfClosestInt : dfValue;
    }

    //CPLErr VRTSimpleSource::XMLInit( CPLXMLNode *psSrc, const char *pszVRTPath,
    public void XmlInit() {

    }

    /*
    public void GetFileList(List<List<String>> ppapszFileList, Integer pnSize,
                            Integer pnMaxSize, List<CPLHashSet> hSetFiles) {
        String pszFilename = null;
        if (m_poRasterBand != null && m_poRasterBand.GetDataset() != null &&
                (pszFilename = m_poRasterBand.GetDataset().getDescription()) != null) {
            if (pszFilename.indexOf("/vsicurl/http") != -1 ||
                    pszFilename.indexOf("/vsicurl/ftp") != -1) {
                // Testing the existence of remote resources can be excruciating
                // slow, so just suppose they exist.
            } else {
                VSIStatBufL sStat;
                if (VSIStatExL(pszFilename, & sStat,VSI_STAT_EXISTS_FLAG ) !=0 )
                return;
            }

            if (CPLHashSetLookup(hSetFiles, pszFilename) != null)
                return;

            if (pnSize + 1 >= pnMaxSize) {
                pnMaxSize = Math.max(pnSize + 2, 2 + 2 * (pnMaxSize));
                ppapszFileList = static_cast < char **>(CPLRealloc(
                        * ppapszFileList, sizeof( char*) *( * pnMaxSize) ) );
            }

            ( * ppapszFileList)[*pnSize] =CPLStrdup(pszFilename);
            ( * ppapszFileList)[( * pnSize + 1)] =null;
            CPLHashSetInsert(hSetFiles, ( * ppapszFileList)[ * pnSize]);

            ( * pnSize)++;
        }
    }*/

    public GdalRasterBand GetBand() {
        return (m_poMaskBandMainBand == null) ? null : m_poRasterBand;
    }

    public Boolean IsSameExceptBandNumber(VrtSimpleSource poOtherSource) {
        return m_dfSrcXOff == poOtherSource.m_dfSrcXOff &&
                m_dfSrcYOff == poOtherSource.m_dfSrcYOff &&
                m_dfSrcXSize == poOtherSource.m_dfSrcXSize &&
                m_dfSrcYSize == poOtherSource.m_dfSrcYSize &&
                m_dfDstXOff == poOtherSource.m_dfDstXOff &&
                m_dfDstYOff == poOtherSource.m_dfDstYOff &&
                m_dfDstXSize == poOtherSource.m_dfDstXSize &&
                m_dfDstYSize == poOtherSource.m_dfDstYSize &&
                m_bNoDataSet == poOtherSource.m_bNoDataSet &&
                m_dfNoDataValue == poOtherSource.m_dfNoDataValue &&
                GetBand() != null && poOtherSource.GetBand() != null &&
                GetBand().GetDataset() != null &&
                poOtherSource.GetBand().GetDataset() != null &&
                GetBand().GetDataset().getDescription().equals(
                        poOtherSource.GetBand().GetDataset().getDescription());
    }

    //TODO dfxOut and dfyOut should returned from this method
    public void SrcToDst(Double dfX, Double dfY,
                         Double dfXOut, Double dfYOut) {
        dfXOut = ((dfX - m_dfSrcXOff) / m_dfSrcXSize) * m_dfDstXSize + m_dfDstXOff;
        dfYOut = ((dfY - m_dfSrcYOff) / m_dfSrcYSize) * m_dfDstYSize + m_dfDstYOff;
    }

    //TODO dfxOut and dfyOut should returned from this method
    public void DstToSrc(Double dfX, Double dfY,
                         Double dfXOut, Double dfYOut) {
        dfXOut = ((dfX - m_dfDstXOff) / m_dfDstXSize) * m_dfSrcXSize + m_dfSrcXOff;
        dfYOut = ((dfY - m_dfDstYOff) / m_dfDstYSize) * m_dfSrcYSize + m_dfSrcYOff;
    }

    public boolean GetSrcDstWindow(int nXOff, int nYOff, int nXSize, int nYSize,
                                   int nBufXSize, int nBufYSize,
                                   Double pdfReqXOff, Double pdfReqYOff,
                                   Double pdfReqXSize, Double pdfReqYSize,
                                   Integer pnReqXOff, Integer pnReqYOff,
                                   Integer pnReqXSize, Integer pnReqYSize,
                                   Integer pnOutXOff, Integer pnOutYOff,
                                   Integer pnOutXSize, Integer pnOutYSize) {
        if (m_dfSrcXSize == 0.0 || m_dfSrcYSize == 0.0 ||
                m_dfDstXSize == 0.0 || m_dfDstYSize == 0.0) {
            return false;
        }

        Boolean bDstWinSet = m_dfDstXOff != -1 || m_dfDstXSize != -1
                || m_dfDstYOff != -1 || m_dfDstYSize != -1;

        /* -------------------------------------------------------------------- */
        /*      If the input window completely misses the portion of the        */
        /*      virtual dataset provided by this source we have nothing to do.  */
        /* -------------------------------------------------------------------- */
        if (bDstWinSet) {
            if (nXOff >= m_dfDstXOff + m_dfDstXSize
                    || nYOff >= m_dfDstYOff + m_dfDstYSize
                    || nXOff + nXSize < m_dfDstXOff
                    || nYOff + nYSize < m_dfDstYOff)
                return false;
        }

        /* -------------------------------------------------------------------- */
        /*      This request window corresponds to the whole output buffer.     */
        /* -------------------------------------------------------------------- */
        pnOutXOff = 0;
        pnOutYOff = 0;
        pnOutXSize = nBufXSize;
        pnOutYSize = nBufYSize;

        /* -------------------------------------------------------------------- */
        /*      If the input window extents outside the portion of the on       */
        /*      the virtual file that this source can set, then clip down       */
        /*      the requested window.                                           */
        /* -------------------------------------------------------------------- */
        Boolean bModifiedX = false;
        Boolean bModifiedY = false;
        double dfRXOff = nXOff;
        double dfRYOff = nYOff;
        double dfRXSize = nXSize;
        double dfRYSize = nYSize;

        if (bDstWinSet) {
            if (dfRXOff < m_dfDstXOff) {
                dfRXSize = dfRXSize + dfRXOff - m_dfDstXOff;
                dfRXOff = m_dfDstXOff;
                bModifiedX = true;
            }

            if (dfRYOff < m_dfDstYOff) {
                dfRYSize = dfRYSize + dfRYOff - m_dfDstYOff;
                dfRYOff = m_dfDstYOff;
                bModifiedY = true;
            }

            if (dfRXOff + dfRXSize > m_dfDstXOff + m_dfDstXSize) {
                dfRXSize = m_dfDstXOff + m_dfDstXSize - dfRXOff;
                bModifiedX = true;
            }

            if (dfRYOff + dfRYSize > m_dfDstYOff + m_dfDstYSize) {
                dfRYSize = m_dfDstYOff + m_dfDstYSize - dfRYOff;
                bModifiedY = true;
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Translate requested region in virtual file into the source      */
        /*      band coordinates.                                               */
        /* -------------------------------------------------------------------- */
        double dfScaleX = m_dfSrcXSize / m_dfDstXSize;
        double dfScaleY = m_dfSrcYSize / m_dfDstYSize;

        pdfReqXOff = (dfRXOff - m_dfDstXOff) * dfScaleX + m_dfSrcXOff;
        pdfReqYOff = (dfRYOff - m_dfDstYOff) * dfScaleY + m_dfSrcYOff;
        pdfReqXSize = dfRXSize * dfScaleX;
        pdfReqYSize = dfRYSize * dfScaleY;

        //TODO
        /*
            if (!CPLIsFinite(pdfReqXOff) ||
                !CPLIsFinite(pdfReqYOff) ||
                !CPLIsFinite(pdfReqXSize) ||
                !CPLIsFinite(pdfReqYSize) ||)
         */
        if (
                pdfReqXOff > Integer.MAX_VALUE ||
                        pdfReqYOff > Integer.MAX_VALUE ||
                        pdfReqXSize < 0 ||
                        pdfReqYSize < 0) {
            return false;
        }

        /* -------------------------------------------------------------------- */
        /*      Clamp within the bounds of the available source data.           */
        /* -------------------------------------------------------------------- */
        if (pdfReqXOff < 0) {
            pdfReqXSize += pdfReqXOff;
            pdfReqXOff = 0.0;
            bModifiedX = true;
        }
        if (pdfReqYOff < 0) {
            pdfReqYSize += pdfReqYOff;
            pdfReqYOff = 0.0;
            bModifiedY = true;
        }

        pnReqXOff = (int) Math.floor(pdfReqXOff);
        pnReqYOff = (int) Math.floor(pdfReqYOff);

        if (pdfReqXSize > Integer.MAX_VALUE)
            pnReqXSize = Integer.MAX_VALUE;
        else
            pnReqXSize = (int) Math.floor(pdfReqXSize + 0.5);

        if (pdfReqYSize > Integer.MAX_VALUE)
            pnReqYSize = Integer.MAX_VALUE;
        else
            pnReqYSize = (int) Math.floor(pdfReqYSize + 0.5);

        /* -------------------------------------------------------------------- */
        /*      Clamp within the bounds of the available source data.           */
        /* -------------------------------------------------------------------- */

        if (pnReqXSize == 0)
            pnReqXSize = 1;
        if (pnReqYSize == 0)
            pnReqYSize = 1;

        if (pnReqXSize > Integer.MAX_VALUE - pnReqXOff ||
                pnReqXOff + pnReqXSize > m_poRasterBand.GetXSize()) {
            pnReqXSize = m_poRasterBand.GetXSize() - pnReqXOff;
            bModifiedX = true;
        }
        if (pdfReqXOff + pdfReqXSize > m_poRasterBand.GetXSize()) {
            pdfReqXSize = m_poRasterBand.GetXSize() - pdfReqXOff;
            bModifiedX = true;
        }

        if (pnReqYSize > Integer.MAX_VALUE - pnReqYOff ||
                pnReqYOff + pnReqYSize > m_poRasterBand.GetYSize()) {
            pnReqYSize = m_poRasterBand.GetYSize() - pnReqYOff;
            bModifiedY = true;
        }
        if (pdfReqYOff + pdfReqYSize > m_poRasterBand.GetYSize()) {
            pdfReqYSize = m_poRasterBand.GetYSize() - pdfReqYOff;
            bModifiedY = true;
        }

        /* -------------------------------------------------------------------- */
        /*      Don't do anything if the requesting region is completely off    */
        /*      the source image.                                               */
        /* -------------------------------------------------------------------- */
        if (pnReqXOff >= m_poRasterBand.GetXSize()
                || pnReqYOff >= m_poRasterBand.GetYSize()
                || pnReqXSize <= 0 || pnReqYSize <= 0) {
            return false;
        }

        /* -------------------------------------------------------------------- */
        /*      If we haven't had to modify the source rectangle, then the      */
        /*      destination rectangle must be the whole region.                 */
        /* -------------------------------------------------------------------- */
        if (!bModifiedX && !bModifiedY)
            return true;

        /* -------------------------------------------------------------------- */
        /*      Now transform this possibly reduced request back into the       */
        /*      destination buffer coordinates in case the output region is     */
        /*      less than the whole buffer.                                     */
        /* -------------------------------------------------------------------- */
        double dfDstULX = 0.0;
        double dfDstULY = 0.0;
        double dfDstLRX = 0.0;
        double dfDstLRY = 0.0;

        SrcToDst(pdfReqXOff, pdfReqYOff, dfDstULX, dfDstULY);
        SrcToDst(pdfReqXOff + pdfReqXSize, pdfReqYOff + pdfReqYSize,
                dfDstLRX, dfDstLRY);
//#if DEBUG_VERBOSE
//        CPLDebug( "VRT", "dfDstULX=%g dfDstULY=%g dfDstLRX=%g dfDstLRY=%g",
//                dfDstULX, dfDstULY, dfDstLRX, dfDstLRY );
//#endif

        if (bModifiedX) {
            double dfScaleWinToBufX =
                    nBufXSize / (double) (nXSize);

            double dfOutXOff = (dfDstULX - nXOff) * dfScaleWinToBufX;
            if (dfOutXOff <= 0)
                pnOutXOff = 0;
            else if (dfOutXOff > Integer.MAX_VALUE)
                pnOutXOff = Integer.MAX_VALUE;
            else
                pnOutXOff = (int) (dfOutXOff + 0.001);

            // Apply correction on floating-point source window
            {
                double dfDstDeltaX = (dfOutXOff - pnOutXOff) / dfScaleWinToBufX;
                double dfSrcDeltaX = dfDstDeltaX / m_dfDstXSize * m_dfSrcXSize;
                pdfReqXOff -= dfSrcDeltaX;
                pdfReqXSize = Math.min(pdfReqXSize + dfSrcDeltaX,
                        Integer.MAX_VALUE);
            }

            double dfOutRightXOff = (dfDstLRX - nXOff) * dfScaleWinToBufX;
            if (dfOutRightXOff < dfOutXOff)
                return false;
            if (dfOutRightXOff > Integer.MAX_VALUE)
                dfOutRightXOff = Integer.MAX_VALUE;
            pnOutXSize = (int) (Math.ceil(dfOutRightXOff - 0.001) - pnOutXOff);

            if (pnOutXSize > Integer.MAX_VALUE - pnOutXOff ||
                    pnOutXOff + pnOutXSize > nBufXSize)
                pnOutXSize = nBufXSize - pnOutXOff;

            // Apply correction on floating-point source window
            {
                double dfDstDeltaX = (Math.ceil(dfOutRightXOff) - dfOutRightXOff) / dfScaleWinToBufX;
                double dfSrcDeltaX = dfDstDeltaX / m_dfDstXSize * m_dfSrcXSize;
                pdfReqXSize = Math.min(pdfReqXSize + dfSrcDeltaX,
                        Integer.MAX_VALUE);
            }
        }

        if (bModifiedY) {
            double dfScaleWinToBufY =
                    nBufYSize / (double) (nYSize);

            double dfOutYOff = (dfDstULY - nYOff) * dfScaleWinToBufY;
            if (dfOutYOff <= 0)
                pnOutYOff = 0;
            else if (dfOutYOff > Integer.MAX_VALUE)
                pnOutYOff = Integer.MAX_VALUE;
            else
                pnOutYOff = (int) (dfOutYOff + 0.001);

            // Apply correction on floating-point source window
            {
                double dfDstDeltaY = (dfOutYOff - pnOutYOff) / dfScaleWinToBufY;
                double dfSrcDeltaY = dfDstDeltaY / m_dfDstYSize * m_dfSrcYSize;
                pdfReqYOff -= dfSrcDeltaY;
                pdfReqYSize = Math.min(pdfReqYSize + dfSrcDeltaY,
                        Integer.MAX_VALUE);
            }

            double dfOutTopYOff = (dfDstLRY - nYOff) * dfScaleWinToBufY;
            if (dfOutTopYOff < dfOutYOff)
                return false;
            if (dfOutTopYOff > Integer.MAX_VALUE)
                dfOutTopYOff = Integer.MAX_VALUE;
            pnOutYSize = (int) (Math.ceil(dfOutTopYOff - 0.001)) - pnOutYOff;

            if (pnOutYSize > Integer.MAX_VALUE - pnOutYOff ||
                    pnOutYOff + pnOutYSize > nBufYSize)
                pnOutYSize = nBufYSize - pnOutYOff;

            // Apply correction on floating-point source window
            {
                double dfDstDeltaY = (Math.ceil(dfOutTopYOff) - dfOutTopYOff) / dfScaleWinToBufY;
                double dfSrcDeltaY = dfDstDeltaY / m_dfDstYSize * m_dfSrcYSize;
                pdfReqYSize = Math.min(pdfReqYSize + dfSrcDeltaY,
                        Integer.MAX_VALUE);
            }
        }

        if (pnOutXSize < 1 || pnOutYSize < 1)
            return false;

        return true;
    }

    /**
     public boolean NeedMaxValAdjustment() {
     if (m_nMaxValue == 0)
     return false;

     String pszNBITS =
     m_poRasterBand.GetMetadataItem("NBITS", "IMAGE_STRUCTURE");
     int nBits = (pszNBITS != null) ? atoi(pszNBITS) : 0;
     if (nBits >= 1 && nBits <= 31) {
     int nBandMaxValue = (int) ((1 U << nBits)-1);
     return nBandMaxValue > m_nMaxValue;
     }
     return true;
     }*/

    /**
     public void RasterIO(GDALDataType eBandDataType,
     int nXOff, int nYOff, int nXSize, int nYSize,
     void *pData, int nBufXSize, int nBufYSize,
     GDALDataType eBufType,
     GSpacing nPixelSpace,
     GSpacing nLineSpace,
     GDALRasterIOExtraArg *psExtraArgIn) {
     GDALRasterIOExtraArg sExtraArg;
     INIT_RASTERIO_EXTRA_ARG(sExtraArg);
     GDALRasterIOExtraArg * psExtraArg = &sExtraArg;

     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (!GetSrcDstWindow(nXOff, nYOff, nXSize, nYSize,
     nBufXSize, nBufYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) )
     {
     return;
     }


     if (!m_osResampling.empty()) {
     psExtraArg -> eResampleAlg = GDALRasterIOGetResampleAlg(m_osResampling);
     } else if (psExtraArgIn != nullptr) {
     psExtraArg -> eResampleAlg = psExtraArgIn -> eResampleAlg;
     }
     psExtraArg -> bFloatingPointWindowValidity = TRUE;
     psExtraArg -> dfXOff = dfReqXOff;
     psExtraArg -> dfYOff = dfReqYOff;
     psExtraArg -> dfXSize = dfReqXSize;
     psExtraArg -> dfYSize = dfReqYSize;

     GByte * pabyOut =
     static_cast < unsigned char *>(pData)
     + nOutXOff * nPixelSpace
     + static_cast < GPtrDiff_t > (nOutYOff) * nLineSpace;

     CPLErr eErr = CE_Failure;

     if (GDALDataTypeIsConversionLossy(m_poRasterBand -> GetRasterDataType(),
     eBandDataType)) {
     const int nBandDTSize = GDALGetDataTypeSizeBytes(eBandDataType);
     void*pTemp = VSI_MALLOC3_VERBOSE(nOutXSize, nOutYSize, nBandDTSize);
     if (pTemp) {
     eErr =
     m_poRasterBand -> RasterIO(
     GF_Read,
     nReqXOff, nReqYOff, nReqXSize, nReqYSize,
     pTemp,
     nOutXSize, nOutYSize,
     eBandDataType, 0, 0, psExtraArg);
     if (eErr == CE_None) {
     GByte * pabyTemp = static_cast < GByte * > (pTemp);
     for (int iY = 0; iY < nOutYSize; iY++) {
     GDALCopyWords(pabyTemp + static_cast < size_t > (iY) *
     nBandDTSize * nOutXSize,
     eBandDataType, nBandDTSize,
     pabyOut +
     static_cast < GPtrDiff_t > (iY * nLineSpace),
     eBufType,
     static_cast < int>(nPixelSpace),
     nOutXSize);
     }
     }
     VSIFree(pTemp);
     }
     } else {
     eErr =
     m_poRasterBand -> RasterIO(
     GF_Read,
     nReqXOff, nReqYOff, nReqXSize, nReqYSize,
     pabyOut,
     nOutXSize, nOutYSize,
     eBufType, nPixelSpace, nLineSpace, psExtraArg);
     }

     if (NeedMaxValAdjustment()) {
     for (int j = 0; j < nOutYSize; j++) {
     for (int i = 0; i < nOutXSize; i++) {
     int nVal = 0;
     GDALCopyWords(pabyOut + j * nLineSpace + i * nPixelSpace,
     eBufType, 0,
     & nVal, GDT_Int32, 0,
     1 );
     if (nVal > m_nMaxValue)
     nVal = m_nMaxValue;
     GDALCopyWords( & nVal, GDT_Int32, 0,
     pabyOut + j * nLineSpace + i * nPixelSpace,
     eBufType, 0,
     1);
     }
     }
     }

     return eErr;
     }

     public Double GetMinimum(int nXSize, int nYSize, Boolean pbSuccess) {
     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (!GetSrcDstWindow(0, 0, nXSize, nYSize,
     nXSize, nYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) ||
     nReqXOff != 0 || nReqYOff != 0 ||
     nReqXSize != m_poRasterBand -> GetXSize() ||
     nReqYSize != m_poRasterBand -> GetYSize())
     {
     *pbSuccess = FALSE;
     return 0;
     }

     const double dfVal = m_poRasterBand -> GetMinimum(pbSuccess);
     if (NeedMaxValAdjustment() && dfVal > m_nMaxValue)
     return m_nMaxValue;
     return dfVal;
     }

     public Double GetMaximum(int nXSize, int nYSize, Boolean pbSuccess) {
     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (!GetSrcDstWindow(0, 0, nXSize, nYSize,
     nXSize, nYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) ||
     nReqXOff != 0 || nReqYOff != 0 ||
     nReqXSize != m_poRasterBand -> GetXSize() ||
     nReqYSize != m_poRasterBand -> GetYSize())
     {
     *pbSuccess = FALSE;
     return 0;
     }

     const double dfVal = m_poRasterBand -> GetMaximum(pbSuccess);
     if (NeedMaxValAdjustment() && dfVal > m_nMaxValue)
     return m_nMaxValue;
     return dfVal;
     }

     public void ComputeRasterMinMax(int nXSize, int nYSize,
     int bApproxOK, Double adfMinMax) {
     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (!GetSrcDstWindow(0, 0, nXSize, nYSize,
     nXSize, nYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) ||
     nReqXOff != 0 || nReqYOff != 0 ||
     nReqXSize != m_poRasterBand -> GetXSize() ||
     nReqYSize != m_poRasterBand -> GetYSize())
     {
     return CE_Failure;
     }

     const CPLErr eErr =
     m_poRasterBand -> ComputeRasterMinMax(bApproxOK, adfMinMax);
     if (NeedMaxValAdjustment()) {
     if (adfMinMax[0] > m_nMaxValue)
     adfMinMax[0] = m_nMaxValue;
     if (adfMinMax[1] > m_nMaxValue)
     adfMinMax[1] = m_nMaxValue;
     }
     return eErr;
     }*/

    /*                         ComputeStatistics()                          */

    /************************************************************************/

    /*
    public void ComputeStatistics(
            int nXSize, int nYSize,
            int bApproxOK,
            Double pdfMin, Double pdfMax,
            Double pdfMean, Double pdfStdDev) {
        // The window we will actually request from the source raster band.
        double dfReqXOff = 0.0;
        double dfReqYOff = 0.0;
        double dfReqXSize = 0.0;
        double dfReqYSize = 0.0;
        int nReqXOff = 0;
        int nReqYOff = 0;
        int nReqXSize = 0;
        int nReqYSize = 0;

        // The window we will actual set _within_ the pData buffer.
        int nOutXOff = 0;
        int nOutYOff = 0;
        int nOutXSize = 0;
        int nOutYSize = 0;

        if (NeedMaxValAdjustment() ||
                !GetSrcDstWindow(0, 0, nXSize, nYSize,
                        nXSize, nYSize,
                        & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
                          &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
                          &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) ||
        nReqXOff != 0 || nReqYOff != 0 ||
                nReqXSize != m_poRasterBand -> GetXSize() ||
                        nReqYSize != m_poRasterBand -> GetYSize())
        {
            return CE_Failure;
        }

        return m_poRasterBand -> ComputeStatistics(bApproxOK, pdfMin, pdfMax,
                pdfMean, pdfStdDev,
                pfnProgress, pProgressData);
    }*/

/************************************************************************/
    /*                            GetHistogram()                            */

    /************************************************************************/

    /**
     public void GetHistogram(
     int nXSize, int nYSize,
     double dfMin, double dfMax,
     int nBuckets, ,
     int bIncludeOutOfRange, int bApproxOK) {
     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (NeedMaxValAdjustment() ||
     !GetSrcDstWindow(0, 0, nXSize, nYSize,
     nXSize, nYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) ||
     nReqXOff != 0 || nReqYOff != 0 ||
     nReqXSize != m_poRasterBand -> GetXSize() ||
     nReqYSize != m_poRasterBand -> GetYSize())
     {
     return CE_Failure;
     }

     return m_poRasterBand -> GetHistogram(dfMin, dfMax, nBuckets,
     panHistogram,
     bIncludeOutOfRange, bApproxOK,
     pfnProgress, pProgressData);
     }*/

/************************************************************************/
    /*                          DatasetRasterIO()                           */

    /************************************************************************/

    /**
     public void DatasetRasterIO(
     GDALDataType eBandDataType,
     int nXOff, int nYOff, int nXSize, int nYSize,
     void *pData, int nBufXSize, int nBufYSize,
     GDALDataType eBufType,
     int nBandCount, int *panBandMap,
     GSpacing nPixelSpace, GSpacing nLineSpace,
     GSpacing nBandSpace,
     GDALRasterIOExtraArg*psExtraArgIn) {
     if (!EQUAL(GetType(), "SimpleSource")) {
     CPLError(CE_Failure, CPLE_NotSupported,
     "DatasetRasterIO() not implemented for %s", GetType());
     return CE_Failure;
     }

     GDALRasterIOExtraArg sExtraArg;
     INIT_RASTERIO_EXTRA_ARG(sExtraArg);
     GDALRasterIOExtraArg * psExtraArg = &sExtraArg;

     // The window we will actually request from the source raster band.
     double dfReqXOff = 0.0;
     double dfReqYOff = 0.0;
     double dfReqXSize = 0.0;
     double dfReqYSize = 0.0;
     int nReqXOff = 0;
     int nReqYOff = 0;
     int nReqXSize = 0;
     int nReqYSize = 0;

     // The window we will actual set _within_ the pData buffer.
     int nOutXOff = 0;
     int nOutYOff = 0;
     int nOutXSize = 0;
     int nOutYSize = 0;

     if (!GetSrcDstWindow(nXOff, nYOff, nXSize, nYSize,
     nBufXSize, nBufYSize,
     & dfReqXOff, &dfReqYOff, &dfReqXSize, &dfReqYSize,
     &nReqXOff, &nReqYOff, &nReqXSize, &nReqYSize,
     &nOutXOff, &nOutYOff, &nOutXSize, &nOutYSize ) )
     {
     return CE_None;
     }

     GDALDataset * poDS = m_poRasterBand -> GetDataset();
     if (poDS == nullptr)
     return CE_Failure;

     if (!m_osResampling.empty()) {
     psExtraArg -> eResampleAlg = GDALRasterIOGetResampleAlg(m_osResampling);
     } else if (psExtraArgIn != nullptr) {
     psExtraArg -> eResampleAlg = psExtraArgIn -> eResampleAlg;
     }
     psExtraArg -> bFloatingPointWindowValidity = TRUE;
     psExtraArg -> dfXOff = dfReqXOff;
     psExtraArg -> dfYOff = dfReqYOff;
     psExtraArg -> dfXSize = dfReqXSize;
     psExtraArg -> dfYSize = dfReqYSize;

     GByte * pabyOut =
     static_cast < unsigned char *>(pData)
     + nOutXOff * nPixelSpace
     + static_cast < GPtrDiff_t > (nOutYOff) * nLineSpace;

     CPLErr eErr = CE_Failure;

     if (GDALDataTypeIsConversionLossy(m_poRasterBand -> GetRasterDataType(),
     eBandDataType)) {
     const int nBandDTSize = GDALGetDataTypeSizeBytes(eBandDataType);
     void*pTemp = VSI_MALLOC3_VERBOSE(nOutXSize, nOutYSize,
     nBandDTSize * nBandCount);
     if (pTemp) {
     eErr = poDS -> RasterIO(
     GF_Read,
     nReqXOff, nReqYOff, nReqXSize, nReqYSize,
     pTemp,
     nOutXSize, nOutYSize,
     eBandDataType, nBandCount, panBandMap,
     0, 0, 0, psExtraArg);
     if (eErr == CE_None) {
     GByte * pabyTemp = static_cast < GByte * > (pTemp);
     const size_t nSrcBandSpace = static_cast < size_t > (nOutYSize) *
     nOutXSize * nBandDTSize;
     for (int iBand = 0; iBand < nBandCount; iBand++) {
     for (int iY = 0; iY < nOutYSize; iY++) {
     GDALCopyWords(pabyTemp + iBand * nSrcBandSpace +
     static_cast < size_t > (iY) * nBandDTSize * nOutXSize,
     eBandDataType, nBandDTSize,
     pabyOut +
     static_cast < GPtrDiff_t > (iY * nLineSpace +
     iBand * nBandSpace),
     eBufType, static_cast < int>(nPixelSpace),
     nOutXSize);
     }
     }
     }
     VSIFree(pTemp);
     }
     } else {
     eErr = poDS -> RasterIO(
     GF_Read,
     nReqXOff, nReqYOff, nReqXSize, nReqYSize,
     pabyOut,
     nOutXSize, nOutYSize,
     eBufType, nBandCount, panBandMap,
     nPixelSpace, nLineSpace, nBandSpace, psExtraArg);
     }

     if (NeedMaxValAdjustment()) {
     for (int k = 0; k < nBandCount; k++) {
     for (int j = 0; j < nOutYSize; j++) {
     for (int i = 0; i < nOutXSize; i++) {
     int nVal = 0;
     GDALCopyWords(
     pabyOut + k * nBandSpace + j * nLineSpace +
     i * nPixelSpace,
     eBufType, 0,
     & nVal, GDT_Int32, 0,
     1 );

     if (nVal > m_nMaxValue)
     nVal = m_nMaxValue;

     GDALCopyWords(
     & nVal, GDT_Int32, 0,
     pabyOut + k * nBandSpace + j * nLineSpace +
     i * nPixelSpace,
     eBufType, 0,
     1 );
     }
     }
     }
     }

     return eErr;
     }*/

/************************************************************************/
    /*                          SetResampling()                             */

    /************************************************************************/

    public void SetResampling(String pszResampling) {
        m_osResampling = (pszResampling != null) ? pszResampling : "";
    }

    public void serializeToXML(VRTRasterBandType vrtRasterBandType, GdalRasterBand gdalRasterBand, String pszVRTPath){
        SimpleSourceType simpleSourceType = this.serializeToXml(vrtRasterBandType, gdalRasterBand, pszVRTPath);

        if (vrtRasterBandType.getSimpleSource() == null)
            vrtRasterBandType.setSimpleSource(new ArrayList<>());

        vrtRasterBandType.getSimpleSource().add(simpleSourceType);
    }

    public SimpleSourceType serializeToXml(VRTRasterBandType vrtRasterBandType, GdalRasterBand gdalRasterBand, String pszVRTPath) {
        m_poRasterBand = gdalRasterBand;
        SimpleSourceType simpleSourceType = new SimpleSourceType();

        if (m_poRasterBand == null)
            return null;

        GdalDataset poDs = null;

        if (m_poMaskBandMainBand != null) {
            poDs = m_poMaskBandMainBand.GetDataset();
            if (poDs == null || m_poMaskBandMainBand.GetBand() < 1)
                return null;
        } else {
            poDs = m_poRasterBand.GetDataset();
            if (poDs == null || m_poRasterBand.GetBand() < 1)
                return null;
        }

        if (m_osResampling != null && !m_osResampling.isEmpty()) {
            simpleSourceType.setResampling(m_osResampling);
        }

        //VSIStatBufL sStat;
        String osTmp;
        boolean bRelativeToVRT = false;
        String pszRelativePath = null;

        if (m_bRelativeToVRTOri) {
            pszRelativePath = m_osSourceFileNameOri;
            bRelativeToVRT = m_bRelativeToVRTOri;
        } else if (poDs.getDescription().indexOf("/vsicurl/http") != -1 ||
                poDs.getDescription().indexOf("/vsicurl/ftp") != -1) {
            pszRelativePath = poDs.getDescription();
            bRelativeToVRT = false;
        } else if (false != false){ //VSIStatExL(poDs.getDescription(), sStat, VSI_STAT_EXISTS_FLAG) != 0) {
            // If this isn't actually a file, don't even try to know if it is a
            // relative path. It can't be !, and unfortunately CPLIsFilenameRelative()
            // can only work with strings that are filenames To be clear
            // NITF_TOC_ENTRY:CADRG_JOG-A_250K_1_0:some_path isn't a relative file
            // path.

            pszRelativePath = poDs.getDescription();
            bRelativeToVRT = false;

            //TODO some shit with relative paths
            /*
            for( size_t i = 0;
             i < sizeof(apszSpecialSyntax) / sizeof(apszSpecialSyntax[0]);
             ++i )
        {
            const char* const pszSyntax = apszSpecialSyntax[i];
            CPLString osPrefix(pszSyntax);
            osPrefix.resize(strchr(pszSyntax, ':') - pszSyntax + 1);
            if( pszSyntax[osPrefix.size()] == '"' )
                osPrefix += '"';
            if( EQUALN(pszRelativePath, osPrefix, osPrefix.size()) )
            {
                if( STARTS_WITH_CI(pszSyntax + osPrefix.size(), "{ANY}") )
                {
                    const char* pszLastPart = strrchr(pszRelativePath, ':') + 1;
                    // CSV:z:/foo.xyz
                    if( (pszLastPart[0] == '/' || pszLastPart[0] == '\\') &&
                        pszLastPart - pszRelativePath >= 3 &&
                        pszLastPart[-3] == ':' )
                        pszLastPart -= 2;
                    CPLString osPrefixFilename(pszRelativePath);
                    osPrefixFilename.resize(pszLastPart - pszRelativePath);
                    pszRelativePath =
                        CPLExtractRelativePath( pszVRTPath, pszLastPart,
                                                &bRelativeToVRT );
                    osTmp = osPrefixFilename + pszRelativePath;
                    pszRelativePath = osTmp.c_str();
                }
                else if( STARTS_WITH_CI(pszSyntax + osPrefix.size(),
                                        "{FILENAME}") )
                {
                    CPLString osFilename(pszRelativePath + osPrefix.size());
                    size_t nPos = 0;
                    if( osFilename.size() >= 3 && osFilename[1] == ':' &&
                        (osFilename[2] == '\\' || osFilename[2] == '/') )
                        nPos = 2;
                    nPos =
                        osFilename.find(
                            pszSyntax[osPrefix.size() + strlen("{FILENAME}")],
                            nPos );
                    if( nPos != std::string::npos )
                    {
                        const CPLString osSuffix = osFilename.substr(nPos);
                        osFilename.resize(nPos);
                        pszRelativePath =
                            CPLExtractRelativePath( pszVRTPath, osFilename,
                                        &bRelativeToVRT );
                        osTmp = osPrefix + pszRelativePath + osSuffix;
                        pszRelativePath = osTmp.c_str();
                    }
                }
                break;
            }
        }
             */


        } else {
            pszRelativePath =
                    CPLExtractRelativePath(pszVRTPath, poDs.getDescription(),
                            bRelativeToVRT);//&bRelativeToVRT
        }

        SourceFilenameType sourceFilenameType = new SourceFilenameType();
        sourceFilenameType.setRelativeToVRT((bRelativeToVRT) ? 1 : 0);
        //TODO some shit
        simpleSourceType.setSourceFilename(sourceFilenameType);

        String pszShared = null;//TODO CPLGetConfigOption("VRT_SHARED_SOURCE", null);
        if ((pszShared == null && m_nExplicitSharedStatus == 0) || (pszShared != null && !CPLTestBool(pszShared))) {
            sourceFilenameType.setShared(OGRBooleanType.OGR_FALSE);
        }

        String[][] papszOpenOptions = null;//TODO poDs.GetOpenOptions();
        //GDALSerializeOpenOptionsToXML(simpleSourceType, papszOpenOptions);

        if (m_poMaskBandMainBand != null) {
            //CPLSetXMLValue( psSrc, "SourceBand",
            //        CPLSPrintf("mask,%d",m_poMaskBandMainBand->GetBand()) );
        } else {
            //CPLSetXMLValue( psSrc, "SourceBand",
            //       CPLSPrintf("%d",m_poRasterBand->GetBand()) );
        }

        SourcePropertiesType sourcePropertiesType = new SourcePropertiesType();
        sourcePropertiesType.setRasterXSize(m_poRasterBand.GetXSize());
        sourcePropertiesType.setRasterYSize(m_poRasterBand.GetYSize());
        sourcePropertiesType.setDataTypeType(DataTypeType.valueOf(GDALDataType.getValue(m_poRasterBand.GetRasterDataType())));

        simpleSourceType.setSourceProperties(sourcePropertiesType);

        AtomicInteger nBlockXSize = new AtomicInteger(0);
        AtomicInteger nBlockYSize = new AtomicInteger(0);
        m_poRasterBand.GetBlockSize(nBlockXSize, nBlockYSize);

        if (m_dfSrcXOff != -1 ||
                m_dfSrcYOff != -1 ||
                m_dfSrcXSize != -1 ||
                m_dfSrcYSize != -1) {
            RectType srcRect = new RectType();

            srcRect.setxOff(m_dfSrcXOff);
            srcRect.setyOff(m_dfSrcYOff);
            srcRect.setxSize(m_dfSrcXSize);
            srcRect.setySize(m_dfSrcYSize);

            simpleSourceType.setSrcRect(srcRect);
        }

        if (m_dfDstXOff != -1 ||
                m_dfDstYOff != -1 ||
                m_dfDstXSize != 1 ||
                m_dfDstYSize != -1) {
            RectType dstRect = new RectType();

            dstRect.setxOff(m_dfDstXOff);
            dstRect.setyOff(m_dfDstYOff);
            dstRect.setxSize(m_dfDstXSize);
            dstRect.setySize(m_dfDstYSize);

            simpleSourceType.setDstRect(dstRect);
        }

        return simpleSourceType;
    }

    private boolean CPLTestBool(String value) {
        return !(value.equals("NO") ||
                value.equals("FALSE") ||
                value.equals("OFF") ||
                value.equals("0"));
    }

    private String CPLExtractRelativePath(String pszVRTPath, String description, boolean bRelativeToVRT) {
        return "test CPLExtractRelativePath";
    }
}
