package sokolov.model.datasets;

public class GDALProxyPoolOverviewRasterBand {
    GdalProxyPoolRasterBand poMainBand = null;
    int                      nOverviewBand = 0;

    GdalRasterBand poUnderlyingMainRasterBand = null;
    int                      nRefCountUnderlyingMainRasterBand = 0;


}
