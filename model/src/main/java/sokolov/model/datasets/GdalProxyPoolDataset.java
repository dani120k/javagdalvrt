package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.enums.GdalAccess;
import sokolov.model.supclasses.CPLHashSet;
import sokolov.model.supclasses.GDALProxyPoolCacheEntry;
import sokolov.model.supclasses.GDAL_GCP;

import java.util.concurrent.atomic.AtomicInteger;

public class GdalProxyPoolDataset extends GdalProxyDataset {
    Long responsiblePID = -1L;

    String pszProjectionRef = null;
    OGRSpatialReference m_poSRS = null;
    OGRSpatialReference m_poGCPSRS = null;
    double[] adfGeoTransform = new double[]{0, 1, 0, 0, 0, 1};
    boolean             bHasSrcProjection = false;
    boolean             m_bHasSrcSRS = false;
    boolean             bHasSrcGeoTransform = false;
    String pszGCPProjection = null;
    int              nGCPCount = 0;
    GDAL_GCP pasGCPList = null;
    CPLHashSet metadataSet = null;
    CPLHashSet      metadataItemSet = null;

    GDALProxyPoolCacheEntry cacheEntry = null;
    String m_pszOwner = null;


    public GdalProxyPoolDataset(String pszSourceDatasetDescription,
                                int nRasterXSizeIn, int nRasterYSizeIn,
                                GdalAccess eAccessIn, boolean bSharedIn,
                                String pszProjectionRefIn,
                                double[] padfGeoTransform) {
        SetDescription(pszSourceDatasetDescription);

        nRasterXSize = nRasterXSizeIn;
        nRasterYSize = nRasterYSizeIn;
        eAccess = eAccessIn;

        bShared = bSharedIn;
        //m_pszOwner = pszOwner ? CPLStrdup(pszOwner) : nullptr;

        if (padfGeoTransform != null) {
            adfGeoTransform = padfGeoTransform;
            bHasSrcGeoTransform = true;
        } else {
            adfGeoTransform[0] = 0;
            adfGeoTransform[1] = 1;
            adfGeoTransform[2] = 0;
            adfGeoTransform[3] = 0;
            adfGeoTransform[4] = 0;
            adfGeoTransform[5] = 1;
            bHasSrcGeoTransform = false;
        }

        if (pszProjectionRefIn != null) {
            m_poSRS = new OGRSpatialReference();
            m_poSRS.importFromWkt(pszProjectionRefIn);
            m_bHasSrcSRS = true;
        }

        pszGCPProjection = null;
        nGCPCount = 0;
        pasGCPList = null;
        metadataSet = null;
        metadataItemSet = null;
        cacheEntry = null;
    }

    public void AddSrcBandDescription(GDALDataType eDataType, AtomicInteger nBlockXSize, AtomicInteger nBlockYSize) {
        SetBand(nBands + 1, new GdalProxyPoolRasterBand(this, nBands + 1, eDataType, nBlockXSize, nBlockYSize));
    }

    public void setOpenOptions(String[] papszOpenOptionsIn) {
        if (papszOpenOptionsIn == null)
            throw new RuntimeException("Устанавливаются параметры с вхоным null");

        papszOpenOptions = papszOpenOptionsIn;
    }
}
