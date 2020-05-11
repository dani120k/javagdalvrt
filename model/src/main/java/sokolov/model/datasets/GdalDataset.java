package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.enums.GdalAccess;
import sokolov.model.supclasses.GDALDefaultOverviews;
import sokolov.model.supclasses.GdalDatasetPrivate;
import sokolov.model.supclasses.GdalDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * A dataset encapsulating one or more raster bands.
 */
public class GdalDataset extends GdalMajorObject {
    public GdalDriver poDriver = null;
    public GdalAccess eAccess = GdalAccess.GA_ReadOnly;

    int nRasterXSize = 512;
    int nRasterYSize = 512;
    int nBands = 0;
    GdalRasterBand[] papoBands = null;
    int nOpenFlags = 0;
    int nRefCount = 1;
    boolean bForceCachedIO = false;
    boolean bShared = false;
    boolean bIsInternal = true;
    boolean bSupressOnClose = false;

    //TODO idk private
    private GdalDatasetPrivate m_poPrivate;


    GDALDefaultOverviews oOvManager;

    String[] papszOpenOptions = null;

    // Set of all datasets created in the constructor of GDALDataset.
// In the case of a shared dataset, memorize the PID of the thread
// that marked the dataset as shared, so that we can remove it from
// the phSharedDatasetSet in the destructor of the dataset, even
// if GDALClose is called from a different thread.
    static Map<GdalDataset, Long> poAllDatasetMap = null;


    public GdalDataset() {

    }

    public GdalDataset(int bForceCachedIO) {

    }

    public void AddToDatasetOpenList() {
        bIsInternal = false;

        if (poAllDatasetMap == null)
            poAllDatasetMap = new HashMap<>();

        poAllDatasetMap.put(this, -1L);
    }

    /*                          RasterInitialize()                          */
    /*                                                                      */
    /*      Initialize raster size                                          */
    public void RasterInitialize(int nXSize, int nYSize) {
        if (nXSize <= 0 || nYSize <= 0)
            throw new RuntimeException(String.format("Ошибка. Инициализирован растер с размерами %s и %s", nXSize, nYSize));

        this.nRasterXSize = nXSize;
        this.nRasterYSize = nYSize;
    }


    /**
     * \fn GDALDataset::AddBand(GDALDataType, char**)
     * \brief Add a band to a dataset.
     * <p>
     * This method will add a new band to the dataset if the underlying format
     * supports this action.  Most formats do not.
     * <p>
     * Note that the new GDALRasterBand is not returned.  It may be fetched
     * after successful completion of the method by calling
     * GDALDataset::GetRasterBand(GDALDataset::GetRasterCount()) as the newest
     * band will always be the last band.
     *
     * @param eType        the data type of the pixels in the new band.
     * @param papszOptions a list of NAME=VALUE option strings.  The supported
     *                     options are format specific.  NULL may be passed by default.
     */
    public void AddBand(GDALDataType eType,
                        String[] papszOptions) {
        System.out.println("Dataset does not support the AddBand() method.");
    }

    public void GDALAddBand(GdalDataset hDataset,
                            GDALDataType eType,
                            String[] papszOptions) {
        AddBand(eType, papszOptions);
    }

    public void SetBand(int nNewBand, GdalRasterBand poBand) {
        /* -------------------------------------------------------------------- */
        /*      Do we need to grow the bands list?                              */
        /* -------------------------------------------------------------------- */
        if (nBands < nNewBand || papoBands == null) {
            GdalRasterBand[] papoNewBands = null;

            if (papoBands == null)
                papoNewBands = new GdalRasterBand[Math.max(nNewBand, nBands)];
            else {
                GdalRasterBand[] gdalRasterBands = papoBands.clone();
                papoNewBands = new GdalRasterBand[Math.max(nNewBand, nBands)];
                for (int i = 0; i < gdalRasterBands.length; i++) {
                    papoNewBands[i] = gdalRasterBands[i];
                }
            }

            if (papoNewBands == null) {
                System.out.println("Cannot allocate band array");
                return;
            }

            papoBands = papoNewBands;

            for (int i = nBands; i < nNewBand; ++i)
                papoBands[i] = null;

            nBands = Math.max(nBands, nNewBand);
        }

        /* -------------------------------------------------------------------- */
        /*      Set the band.  Resetting the band is currently not permitted.   */
        /* -------------------------------------------------------------------- */
        if (papoBands[nNewBand - 1] != null) {
            System.out.println(String.format("Cannot set band %d as it is already set", nNewBand));
            return;
        }

        papoBands[nNewBand - 1] = poBand;
        /* -------------------------------------------------------------------- */
        /*      Set back reference information on the raster band.  Note        */
        /*      that the GDALDataset is a friend of the GDALRasterBand          */
        /*      specifically to allow this.                                     */
        /* -------------------------------------------------------------------- */
        poBand.nBand = nNewBand;
        poBand.poDS = this;
        poBand.nRasterXSize = nRasterXSize;
        poBand.nRasterYSize = nRasterYSize;
        poBand.eAccess = eAccess;  // Default access to be same as dataset.
    }

    /**
     * \brief Fetch raster width in pixels.
     * <p>
     * Equivalent of the C function GDALGetRasterXSize().
     *
     * @return the width in pixels of raster bands in this GDALDataset.
     */
    public int GetRasterXSize() {
        return nRasterYSize;
    }

    /**
     * \brief Fetch raster height in pixels.
     * <p>
     * Equivalent of the C function GDALGetRasterYSize().
     *
     * @return the height in pixels of raster bands in this GDALDataset.
     */
    public int GetRasterYSize() {
        return nRasterYSize;
    }

    /**
     * \brief Fetch a band object for a dataset.
     * <p>
     * See GetBands() for a C++ iterator version of this method.
     * <p>
     * Equivalent of the C function GDALGetRasterBand().
     *
     * @param nBandId the index number of the band to fetch, from 1 to
     *                GetRasterCount().
     * @return the nBandId th band object
     */
    public GdalRasterBand GetRasterBand(int nBandId) {
        if (papoBands != null) {
            if (nBandId < 1 || nBandId > nBands) {
                System.out.println(String.format("GDALDataset::GetRasterBand(%d) - Illegal band #", nBandId));
                return null;
            }

            return papoBands[nBandId - 1];
        }

        return null;
    }

    /**
     * \brief Fetch the number of raster bands on this dataset.
     * <p>
     * Same as the C function GDALGetRasterCount().
     *
     * @return the number of raster bands.
     */
    public int getRasterCount() {
        return (papoBands != null) ? nBands : 0;
    }

    /**
     * \brief Fetch the projection definition string for this dataset.
     * <p>
     * Same as the C function GDALGetProjectionRef().
     * <p>
     * The returned string defines the projection coordinate system of the
     * image in OpenGIS WKT format.  It should be suitable for use with the
     * OGRSpatialReference class.
     * <p>
     * When a projection definition is not available an empty (but not NULL)
     * string is returned.
     * <p>
     * \note Startig with GDAL 3.0, this is a compatibility layer around
     * GetSpatialRef()
     *
     * @return a pointer to an internal projection reference string.  It should
     * not be altered, freed or expected to last for long.
     */
    public String GetProjectionRef() {
        return GetProjectionRefFromSpatialRef(GetSpatialRef());
    }

    /**
     * \brief Fetch the spatial reference for this dataset.
     * <p>
     * Same as the C function GDALGetSpatialRef().
     * <p>
     * When a projection definition is not available, null is returned
     *
     * @return a pointer to an internal object. It should not be altered or freed.
     * Its lifetime will be the one of the dataset object, or until the next
     * call to this method.
     * @since GDAL 3.0
     */
    private OGRSpatialReference GetSpatialRef() {
        return null;
    }

    public String GetProjectionRefFromSpatialRef(OGRSpatialReference poSRS) {
        if (poSRS == null || m_poPrivate == null) {
            return "";
        }

        String pszWKT = poSRS.exportToWkt();
        if (pszWKT == null) {
            return "";
        }

        if (m_poPrivate.m_pszWKTCached != null &&
                pszWKT.equals(m_poPrivate)) {
            return m_poPrivate.m_pszWKTCached;
        }

        m_poPrivate.m_pszWKTCached = pszWKT;

        return m_poPrivate.m_pszWKTCached;
    }

    /**
     * \brief Set the projection reference string for this dataset.
     * <p>
     * The string should be in OGC WKT or PROJ.4 format.  An error may occur
     * because of incorrectly specified projection strings, because the dataset
     * is not writable, or because the dataset does not support the indicated
     * projection.  Many formats do not support writing projections.
     * <p>
     * This method is the same as the C GDALSetProjection() function.
     * <p>
     * \note Startig with GDAL 3.0, this is a compatibility layer around
     * SetSpatialRef()
     *
     * @param pszProjection projection reference string.
     * @return CE_Failure if an error occurs, otherwise CE_None.
     */
    public void SetProjection(String pszProjection) {
        if (pszProjection != null && pszProjection.toCharArray()[0] != '\0'){
            OGRSpatialReference oSRS;
            oSRS.SetAxisMappingStrategy(OAMS_TRADITIONAL_GIS_ORDER);
            if (!oSRS.SetFromUserInput(pszProjection))
                return;

            SetSpatialRef(oSRS);
        }
        else
            SetSpatialRef(null);
    }

    /**
     * \brief Set the spatial reference system for this dataset.
     *
     * An error may occur because the dataset
     * is not writable, or because the dataset does not support the indicated
     * projection. Many formats do not support writing projections.
     *
     * This method is the same as the C GDALSetSpatialRef() function.
     *
     * @since GDAL 3.0

     * @param poSRS spatial reference system object. nullptr can potentially be
     * passed for drivers that support unsetting the SRS.
     *
     * @return CE_Failure if an error occurs, otherwise CE_None.
     */
    public void SetSpatialRef(OGRSpatialReference poSRS){
        if (!(GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED))
            System.out.println("Dataset does not support the SetSpatialRef() method.");
    }

    /**
     * \brief Fetch the affine transformation coefficients.
     *
     * Fetches the coefficients for transforming between pixel/line (P,L) raster
     * space, and projection coordinates (Xp,Yp) space.
     *
     * \code
     *   Xp = padfTransform[0] + P*padfTransform[1] + L*padfTransform[2];
     *   Yp = padfTransform[3] + P*padfTransform[4] + L*padfTransform[5];
     * \endcode
     *
     * In a north up image, padfTransform[1] is the pixel width, and
     * padfTransform[5] is the pixel height.  The upper left corner of the
     * upper left pixel is at position (padfTransform[0],padfTransform[3]).
     *
     * The default transform is (0,1,0,0,0,1) and should be returned even when
     * a CE_Failure error is returned, such as for formats that don't support
     * transformation to projection coordinates.
     *
     * This method does the same thing as the C GDALGetGeoTransform() function.
     *
     * @param padfTransform an existing six double buffer into which the
     * transformation will be placed.
     *
     * @return CE_None on success, or CE_Failure if no transform can be fetched.
     */
    public boolean GetGeoTransform(double[] padfTransform){
        if (padfTransform == null)
            throw new RuntimeException("padfTransform cant be null");

        padfTransform[0] = 0.0;  // X Origin (top left corner)
        padfTransform[1] = 1.0;  // X Pixel size */
        padfTransform[2] = 0.0;

        padfTransform[3] = 0.0;  // Y Origin (top left corner)
        padfTransform[4] = 0.0;
        padfTransform[5] = 1.0;  // Y Pixel Size

        return true;
    }

    /**
     * \fn GDALDataset::SetGeoTransform(double*)
     * \brief Set the affine transformation coefficients.
     *
     * See GetGeoTransform() for details on the meaning of the padfTransform
     * coefficients.
     *
     * This method does the same thing as the C GDALSetGeoTransform() function.
     *
     * @param padfTransform a six double buffer containing the transformation
     * coefficients to be written with the dataset.
     *
     * @return CE_None on success, or CE_Failure if this transform cannot be
     * written.
     */
    public boolean SetGeoTransform(double[] padfTransform){
        if (!(GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED)) {
            System.out.println("SetGeoTransform() not supported for this dataset.");
            return false;
        }

        return true;
    }

    /**
     * \brief Fetch the driver to which this dataset relates.
     *
     * This method is the same as the C GDALGetDatasetDriver() function.
     *
     * @return the driver on which the dataset was created with GDALOpen() or
     * GDALCreate().
     */

    public GdalDriver GetDriver(){
        return poDriver;
    }

    /**
     * \brief Add one to dataset reference count.
     *
     * The reference is one after instantiation.
     *
     * This method is the same as the C GDALReferenceDataset() function.
     *
     * @return the post-increment reference count.
     */
    public int Reference(){
        return ++nRefCount;
    }

    /**
     * \brief Subtract one from dataset reference count.
     *
     * The reference is one after instantiation.  Generally when the reference
     * count has dropped to zero the dataset may be safely deleted (closed).
     *
     * This method is the same as the C GDALDereferenceDataset() function.
     *
     * @return the post-decrement reference count.
     */
    public int Dereference(){
        return --nRefCount;
    }

    /**
     * \brief Adds a mask band to the dataset
     *
     * The default implementation of the CreateMaskBand() method is implemented
     * based on similar rules to the .ovr handling implemented using the
     * GDALDefaultOverviews object. A TIFF file with the extension .msk will
     * be created with the same basename as the original file, and it will have
     * one band.
     * The mask images will be deflate compressed tiled images with the same
     * block size as the original image if possible.
     * It will have INTERNAL_MASK_FLAGS_xx metadata items set at the dataset
     * level, where xx matches the band number of a band of the main dataset. The
     * value of those items will be the one of the nFlagsIn parameter.
     *
     * Note that if you got a mask band with a previous call to GetMaskBand(), it
     * might be invalidated by CreateMaskBand(). So you have to call GetMaskBand()
     * again.
     *
     * @since GDAL 1.5.0
     *
     * @param nFlagsIn 0 or combination of GMF_PER_DATASET / GMF_ALPHA.
     *                 GMF_PER_DATASET will be always set, even if not explicitly
     *                 specified.
     * @return CE_None on success or CE_Failure on an error.
     *
     *
     */
    public void CreateMaskBand(int nFlagsIn){
        if( oOvManager.IsInitialized() )
        {
            CPLErr eErr = oOvManager.CreateMaskBand(nFlagsIn, -1);
            if (eErr != CE_None)
                return eErr;

            // Invalidate existing raster band masks.
            for( int i = 0; i < nBands; ++i )
            {
                GdalRasterBand poBand = papoBands[i];
                poBand.bOwnMask = false;
                poBand.poMask = null;
            }

            return;
        }

        System.out.println("CreateMaskBand() not supported for this dataset.");
        throw new RuntimeException("CreateMaskBand() not supported for this dataset.");
    }

    public int GetRasterCount() {
        return (papoBands != null) ? nBands : 0;
    }
}
