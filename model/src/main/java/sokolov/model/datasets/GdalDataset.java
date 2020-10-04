package sokolov.model.datasets;

import sokolov.model.enums.GDALDataType;
import sokolov.model.enums.GdalAccess;
import sokolov.model.enums.OSRAxisMappingStrategy;
import sokolov.model.supclasses.GDALDefaultOverviews;
import sokolov.model.supclasses.GdalDatasetPrivate;
import sokolov.model.supclasses.GdalDriver;

import java.util.HashMap;
import java.util.Map;

import static sokolov.model.enums.GdalAccess.GA_ReadOnly;
import static sokolov.model.enums.GdalAccess.GA_Update;

/**
 * A dataset encapsulating one or more raster bands.
 */
public class GdalDataset extends GdalMajorObject {
    public int GMO_VALID = 0x0001;
    public int GMO_IGNORE_UNIMPLEMENTED = 0x0002;
    public int GMO_SUPPORT_MD = 0x0004;
    public int GMO_SUPPORT_MDMD = 0x0008;
    public int GMO_MD_DIRTY = 0x0010;
    public int GMO_PAM_CLASS = 0x0020;

    public GdalDriver poDriver = null;
    public GdalAccess eAccess = GA_ReadOnly;

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
        if (pszProjection != null && pszProjection.toCharArray()[0] != '\0') {
            OGRSpatialReference oSRS = new OGRSpatialReference();
            oSRS.SetAxisMappingStrategy(OSRAxisMappingStrategy.OAMS_TRADITIONAL_GIS_ORDER);
            if (!oSRS.SetFromUserInput(pszProjection))
                return;

            SetSpatialRef(oSRS);
        } else
            SetSpatialRef(null);
    }

    /**
     * \brief Set the spatial reference system for this dataset.
     * <p>
     * An error may occur because the dataset
     * is not writable, or because the dataset does not support the indicated
     * projection. Many formats do not support writing projections.
     * <p>
     * This method is the same as the C GDALSetSpatialRef() function.
     *
     * @param poSRS spatial reference system object. nullptr can potentially be
     *              passed for drivers that support unsetting the SRS.
     * @return CE_Failure if an error occurs, otherwise CE_None.
     * @since GDAL 3.0
     */
    public void SetSpatialRef(OGRSpatialReference poSRS) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0))
            System.out.println("Dataset does not support the SetSpatialRef() method.");
    }

    /**
     * \brief Fetch the affine transformation coefficients.
     * <p>
     * Fetches the coefficients for transforming between pixel/line (P,L) raster
     * space, and projection coordinates (Xp,Yp) space.
     * <p>
     * \code
     * Xp = padfTransform[0] + P*padfTransform[1] + L*padfTransform[2];
     * Yp = padfTransform[3] + P*padfTransform[4] + L*padfTransform[5];
     * \endcode
     * <p>
     * In a north up image, padfTransform[1] is the pixel width, and
     * padfTransform[5] is the pixel height.  The upper left corner of the
     * upper left pixel is at position (padfTransform[0],padfTransform[3]).
     * <p>
     * The default transform is (0,1,0,0,0,1) and should be returned even when
     * a CE_Failure error is returned, such as for formats that don't support
     * transformation to projection coordinates.
     * <p>
     * This method does the same thing as the C GDALGetGeoTransform() function.
     *
     * @param padfTransform an existing six double buffer into which the
     *                      transformation will be placed.
     * @return CE_None on success, or CE_Failure if no transform can be fetched.
     */
    public boolean GetGeoTransform(double[] padfTransform) {
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
     * <p>
     * See GetGeoTransform() for details on the meaning of the padfTransform
     * coefficients.
     * <p>
     * This method does the same thing as the C GDALSetGeoTransform() function.
     *
     * @param padfTransform a six double buffer containing the transformation
     *                      coefficients to be written with the dataset.
     * @return CE_None on success, or CE_Failure if this transform cannot be
     * written.
     */
    public boolean SetGeoTransform(double[] padfTransform) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetGeoTransform() not supported for this dataset.");
            return false;
        }

        return true;
    }

    private int GetMOFlags() {
        return 0;
    }

    /**
     * \brief Fetch the driver to which this dataset relates.
     * <p>
     * This method is the same as the C GDALGetDatasetDriver() function.
     *
     * @return the driver on which the dataset was created with GDALOpen() or
     * GDALCreate().
     */

    public GdalDriver GetDriver() {
        return poDriver;
    }

    /**
     * \brief Add one to dataset reference count.
     * <p>
     * The reference is one after instantiation.
     * <p>
     * This method is the same as the C GDALReferenceDataset() function.
     *
     * @return the post-increment reference count.
     */
    public int Reference() {
        return ++nRefCount;
    }

    /**
     * \brief Subtract one from dataset reference count.
     * <p>
     * The reference is one after instantiation.  Generally when the reference
     * count has dropped to zero the dataset may be safely deleted (closed).
     * <p>
     * This method is the same as the C GDALDereferenceDataset() function.
     *
     * @return the post-decrement reference count.
     */
    public int Dereference() {
        return --nRefCount;
    }

    /**
     * \brief Adds a mask band to the dataset
     * <p>
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
     * <p>
     * Note that if you got a mask band with a previous call to GetMaskBand(), it
     * might be invalidated by CreateMaskBand(). So you have to call GetMaskBand()
     * again.
     *
     * @param nFlagsIn 0 or combination of GMF_PER_DATASET / GMF_ALPHA.
     *                 GMF_PER_DATASET will be always set, even if not explicitly
     *                 specified.
     * @return CE_None on success or CE_Failure on an error.
     * @since GDAL 1.5.0
     */
    public void CreateMaskBand(int nFlagsIn) {
        if (oOvManager.IsInitialized()) {
            oOvManager.CreateMaskBand(nFlagsIn, -1);

            // Invalidate existing raster band masks.
            for (int i = 0; i < nBands; ++i) {
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

    /**
     * \brief Open a raster or vector file as a GDALDataset.
     * <p>
     * This function will try to open the passed file, or virtual dataset
     * name by invoking the Open method of each registered GDALDriver in turn.
     * The first successful open will result in a returned dataset.  If all
     * drivers fail then NULL is returned and an error is issued.
     * <p>
     * Several recommendations :
     * <ul>
     * <li>If you open a dataset object with GDAL_OF_UPDATE access, it is not
     * recommended to open a new dataset on the same underlying file.</li>
     * <li>The returned dataset should only be accessed by one thread at a time. If
     * you want to use it from different threads, you must add all necessary code
     * (mutexes, etc.)  to avoid concurrent use of the object. (Some drivers, such
     * as GeoTIFF, maintain internal state variables that are updated each time a
     * new block is read, thus preventing concurrent use.) </li>
     * </ul>
     * <p>
     * For drivers supporting the VSI virtual file API, it is possible to open a
     * file in a .zip archive (see VSIInstallZipFileHandler()), in a
     * .tar/.tar.gz/.tgz archive (see VSIInstallTarFileHandler()) or on a HTTP / FTP
     * server (see VSIInstallCurlFileHandler())
     * <p>
     * In some situations (dealing with unverified data), the datasets can be opened
     * in another process through the \ref gdal_api_proxy mechanism.
     * <p>
     * In order to reduce the need for searches through the operating system
     * file system machinery, it is possible to give an optional list of files with
     * the papszSiblingFiles parameter.
     * This is the list of all files at the same level in the file system as the
     * target file, including the target file. The filenames must not include any
     * path components, are essentially just the output of VSIReadDir() on the
     * parent directory. If the target object does not have filesystem semantics
     * then the file list should be NULL.
     *
     * @param pszFilename         the name of the file to access.  In the case of
     *                            exotic drivers this may not refer to a physical file, but instead contain
     *                            information for the driver on how to access a dataset.  It should be in UTF-8
     *                            encoding.
     * @param nOpenFlags          a combination of GDAL_OF_ flags that may be combined
     *                            through logical or operator.
     *                            <ul>
     *                            <li>Driver kind: GDAL_OF_RASTER for raster drivers, GDAL_OF_VECTOR for vector
     *                            drivers, GDAL_OF_GNM for Geographic Network Model drivers.
     *                            If none of the value is specified, all kinds are implied.</li>
     *                            <li>Access mode: GDAL_OF_READONLY (exclusive)or GDAL_OF_UPDATE.</li>
     *                            <li>Shared mode: GDAL_OF_SHARED. If set, it allows the sharing of GDALDataset
     *                            handles for a dataset with other callers that have set GDAL_OF_SHARED.
     *                            In particular, GDALOpenEx() will first consult its list of currently
     *                            open and shared GDALDataset's, and if the GetDescription() name for one
     *                            exactly matches the pszFilename passed to GDALOpenEx() it will be
     *                            referenced and returned, if GDALOpenEx() is called from the same thread.</li>
     *                            <li>Verbose error: GDAL_OF_VERBOSE_ERROR. If set, a failed attempt to open
     *                            the file will lead to an error message to be reported.</li>
     *                            </ul>
     * @param papszAllowedDrivers NULL to consider all candidate drivers, or a NULL
     *                            terminated list of strings with the driver short names that must be
     *                            considered.
     * @param papszOpenOptions    NULL, or a NULL terminated list of strings with open
     *                            options passed to candidate drivers. An option exists for all drivers,
     *                            OVERVIEW_LEVEL=level, to select a particular overview level of a dataset.
     *                            The level index starts at 0. The level number can be suffixed by "only" to
     *                            specify that only this overview level must be visible, and not sub-levels.
     *                            Open options are validated by default, and a warning is emitted in case the
     *                            option is not recognized. In some scenarios, it might be not desirable (e.g.
     *                            when not knowing which driver will open the file), so the special open option
     *                            VALIDATE_OPEN_OPTIONS can be set to NO to avoid such warnings. Alternatively,
     *                            since GDAL 2.1, an option name can be preceded by the @ character to indicate
     *                            that it may not cause a warning if the driver doesn't declare this option.
     * @param papszSiblingFiles   NULL, or a NULL terminated list of strings that are
     *                            filenames that are auxiliary to the main filename. If NULL is passed, a
     *                            probing of the file system will be done.
     * @return A GDALDatasetH handle or NULL on failure.  For C++ applications
     * this handle can be cast to a GDALDataset *.
     * @since GDAL 2.0
     */
    public GdalDataset gdalOpenEx(String pszFilename,
                                  long nOpenFlags,
                                  String[] papszAllowedDrivers,
                                  String[] papszOpenOptions,
                                  String[] papszSiblingFiles) {
        GdalDataset gdalDataset = new GdalDataset();

        //TODO read From tiff file

        return gdalDataset;
    }

    public void SetNeedsFlush() {

    }
}
