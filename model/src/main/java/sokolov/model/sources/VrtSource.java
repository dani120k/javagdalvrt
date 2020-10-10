package sokolov.model.sources;

import sokolov.model.datasets.GDALRasterIOExtraArg;
import sokolov.model.datasets.GdalRasterBand;
import sokolov.model.enums.GDALDataType;
import sokolov.model.supclasses.CPLHashSet;
import sokolov.model.xmlmodel.SimpleSourceType;
import sokolov.model.xmlmodel.VRTRasterBandType;

import java.util.List;

public interface VrtSource {
    double VRT_NODATA_UNSET = -1234.56;

    void RasterIO(GDALDataType eBandDataType,
                  int nXOff, int nYOff, int nXSize, int nYSize,
                   int nBufXSize, int nBufYSize,
                  GDALDataType eBufType,
                  long nPixelSpace,
                  long nLineSpace,
                  GDALRasterIOExtraArg psExtraArgIn);

    Double GetMinimum(int nXSize, int nYSize, Boolean pbSuccess);

    Double GetMaximum(int nXSize, int nYSize, Boolean pbSuccess );

    void ComputeRasterMinMax(int nXSize, int nYSize,
                        int bApproxOK, Double adfMinMax);

    void ComputeStatistic(int nXSize, int nYSize,
                          int bApproxOK,
                          Double pdfMin, Double pdfMax,
                          Double pdfMean, Double pdfStdDev );

    void GetHistogram(int nXSize, int nYSize,
                 double dfMin, double dfMax,
                 int nBuckets,
                 int bIncludeOutOfRange, int bApproxOK);

    void XmlInit();

    void GetFileList(List<List<String>> ppapszFileList, Integer pnSize,
                Integer pnMaxSize, List<CPLHashSet> hSetFiles);

    boolean IsSimpleSource();

    void FlushCache();

    void serializeToXML(VRTRasterBandType vrtRasterBandType, GdalRasterBand gdalRasterBand, String pszVrtPath);
}
