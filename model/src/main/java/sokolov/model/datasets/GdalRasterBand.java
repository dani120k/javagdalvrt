package sokolov.model.datasets;

import sokolov.model.enums.*;
import sokolov.model.supclasses.GByte;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static sokolov.model.datasets.GDALRIOResampleAlg.*;
import static sokolov.model.enums.GDALColorInterp.GCI_Undefined;
import static sokolov.model.enums.GDALRWFlag.GF_Read;
import static sokolov.model.enums.GDALRWFlag.GF_Write;
import static sokolov.model.enums.GdalAccess.GA_Update;

public class GdalRasterBand extends GdalMajorObject {
    private int GMO_VALID = 0x0001;
    private int GMO_IGNORE_UNIMPLEMENTED = 0x0002;
    private int GMO_SUPPORT_MD = 0x0004;
    private int GMO_SUPPORT_MDMD = 0x0008;
    private int GMO_MD_DIRTY = 0x0010;
    private int GMO_PAM_CLASS = 0x0020;


    GdalDataset poDS = null;
    int nBand = 0;
    int nRasterXSize = 0;
    int nRasterYSize = 0;
    GDALDataType eDataType = GDALDataType.GDT_Byte;
    GdalAccess eAccess = GdalAccess.GA_ReadOnly;
    AtomicInteger nBlockXSize = new AtomicInteger(-1);
    AtomicInteger nBlockYSize = new AtomicInteger(-1);
    int nBlocksPerRow = 0;
    int nBlocksPerColumn = 0;

    int nBlockReads = 0;
    int bForceCachedIO = 0;

    GdalRasterBand poMask = null;
    boolean bOwnMask = false;
    boolean nMaskFlags = true;


    /**
     * \fn GDALRasterBand::SetNoDataValue(double)
     * \brief Set the no data value for this band.
     * <p>
     * Depending on drivers, changing the no data value may or may not have an
     * effect on the pixel values of a raster that has just been created. It is
     * thus advised to explicitly called Fill() if the intent is to initialize
     * the raster to the nodata value.
     * In ay case, changing an existing no data value, when one already exists and
     * the dataset exists or has been initialized, has no effect on the pixel whose
     * value matched the previous nodata value.
     * <p>
     * To clear the nodata value, use DeleteNoDataValue().
     * <p>
     * This method is the same as the C function GDALSetRasterNoDataValue().
     *
     * @param dfNoData the value to set.
     * @return CE_None on success, or CE_Failure on failure.  If unsupported
     * by the driver, CE_Failure is returned by no error message will have
     * been emitted.
     */
    public void setRasterNoDataValue(double dfNoData) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetNoDataValue() not supported for this dataset.");
            throw new RuntimeException();
        }
    }

    /**
     * \fn GDALRasterBand::SetOffset(double)
     * \brief Set scaling offset.
     * <p>
     * Very few formats implement this method.   When not implemented it will
     * issue a CPLE_NotSupported error and return CE_Failure.
     * <p>
     * This method is the same as the C function GDALSetRasterOffset().
     *
     * @param dfNewOffset the new offset.
     * @return CE_None or success or CE_Failure on failure.
     */
    public void SetOffset(double dfNewOffset) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetOffset() not supported on this raster band.");
            throw new RuntimeException();
        }
    }

    /**
     * \fn GDALRasterBand::SetColorInterpretation(GDALColorInterp)
     * \brief Set color interpretation of a band.
     * <p>
     * This method is the same as the C function GDALSetRasterColorInterpretation().
     *
     * @param eColorInterp the new color interpretation to apply to this band.
     * @return CE_None on success or CE_Failure if method is unsupported by format.
     */
    public void SetColorInterpretation(GDALColorInterp eColorInterp) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetColorInterpretation() not supported for this dataset.");
            throw new RuntimeException("SetColorInterpretation() not supported for this dataset.");
        }
    }

    /**
     * \fn GDALRasterBand::SetColorTable(GDALColorTable*)
     * \brief Set the raster color table.
     * <p>
     * The driver will make a copy of all desired data in the colortable.  It
     * remains owned by the caller after the call.
     * <p>
     * This method is the same as the C function GDALSetRasterColorTable().
     *
     * @param poCT the color table to apply.  This may be NULL to clear the color
     *             table (where supported).
     * @return CE_None on success, or CE_Failure on failure.  If the action is
     * unsupported by the driver, a value of CE_Failure is returned, but no
     * error is issued.
     */
    public void SetColorTable(GDALColorTableH poCT) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetColorTable() not supported for this dataset.");
            throw new RuntimeException("SetColorTable() not supported for this dataset.");
        }

    }

    /**
     * \fn GDALRasterBand::SetScale(double)
     * \brief Set scaling ratio.
     * <p>
     * Very few formats implement this method.   When not implemented it will
     * issue a CPLE_NotSupported error and return CE_Failure.
     * <p>
     * This method is the same as the C function GDALSetRasterScale().
     *
     * @param dfNewScale the new scale.
     * @return CE_None or success or CE_Failure on failure.
     */
    public void SetScale(double dfNewScale) {
        if (!((GetMOFlags() & GMO_IGNORE_UNIMPLEMENTED) == 0)) {
            System.out.println("SetScale() not supported on this raster band.");
            throw new RuntimeException("SetScale() not supported on this raster band.");
        }
    }

    private int GetMOFlags() {
        return 0;
    }


    public GdalRasterBand GetMaskBand() {
        if (poMask != null)
            return poMask;

        /* -------------------------------------------------------------------- */
        /*      Check for a mask in a .msk file.                                */
        /* -------------------------------------------------------------------- */
        if (poDS != null && poDS.oOvManager.HaveMaskFile()) {
            poMask = poDS.oOvManager.GetMaskBand(nBand);
            if (poMask != null) {
                nMaskFlags = poDS.oOvManager.GetMaskFlags(nBand);
                return poMask;
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Check for NODATA_VALUES metadata.                               */
        /* -------------------------------------------------------------------- */
        if (poDS != null) {
            String pszNoDataValues = poDS.GetMetadataItem("NODATA_VALUES");
            if (pszNoDataValues != null) {
                String[] papszNoDataValues
                        = CSLTokenizeStringComplex(pszNoDataValues, " ", false, false);

                // Make sure we have as many values as bands.
                if (CSLCount(papszNoDataValues) == poDS.GetRasterCount()
                        && poDS.GetRasterCount() != 0) {
                    // Make sure that all bands have the same data type
                    // This is clearly not a fundamental condition, just a
                    // condition to make implementation easier.
                    GDALDataType eDT = GDALDataType.GDT_Unknown;
                    int i = 0;  // Used after for.
                    for (; i < poDS.GetRasterCount(); ++i) {
                        if (i == 0)
                            eDT = poDS.GetRasterBand(1).GetRasterDataType();
                        else if (eDT != poDS.GetRasterBand(i + 1).GetRasterDataType()) {
                            break;
                        }
                    }
                    if (i == poDS.GetRasterCount()) {
                        nMaskFlags = ((BandMask.GMF_ALPHA.getValue() | BandMask.GMF_PER_DATASET.getValue()) == 0) ? true : false;
                        try {
                            poMask = new GDALNoDataValuesMaskBand(poDS);
                        } catch (Exception ex) {
                            System.out.println("Out of memory");
                            poMask = null;
                        }
                        bOwnMask = true;
                        return poMask;
                    } else {
                        System.out.println("All bands should have the same type in order the NODATA_VALUES metadata item to be used as a mask.");
                        throw new RuntimeException("All bands should have the same type in order the NODATA_VALUES metadata item to be used as a mask.");
                    }
                } else {
                    System.out.println("NODATA_VALUES metadata item doesn't have the same number of values as the number of bands.  Ignoring it for mask.");
                    throw new RuntimeException("NODATA_VALUES metadata item doesn't have the same number of values as the number of bands.  Ignoring it for mask.");
                }
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Check for nodata case.                                          */
        /* -------------------------------------------------------------------- */
        boolean bHaveNoData = false;
        //TODO link bHaveNoData
        double dfNoDataValue = GetNoDataValue(bHaveNoData);

        if (bHaveNoData &&
                GdalNoDataMaskBand.IsNoDataInRange(dfNoDataValue, eDataType)) {
            nMaskFlags = (BandMask.GMF_NODATA.getValue() == 0) ? true : false;
            try {
                poMask = new GdalNoDataMaskBand(this);
            } catch (Exception ex) {
                System.out.println("Out of memory");
                poMask = null;
            }
            bOwnMask = true;
            return poMask;
        }

        /* -------------------------------------------------------------------- */
        /*      Check for alpha case.                                           */
        /* -------------------------------------------------------------------- */
        if (poDS != null
                && poDS.GetRasterCount() == 2
                && this == poDS.GetRasterBand(1)
                && poDS.GetRasterBand(2).GetColorInterpretation() == GDALColorInterp.GCI_AlphaBand) {
            if (poDS.GetRasterBand(2).GetRasterDataType() == GDALDataType.GDT_Byte) {
                nMaskFlags = ((BandMask.GMF_ALPHA.getValue() | BandMask.GMF_PER_DATASET.getValue()) == 0) ? true : false;
                poMask = poDS.GetRasterBand(2);
                return poMask;
            } else if (poDS.GetRasterBand(2).GetRasterDataType() == GDALDataType.GDT_UInt16) {
                nMaskFlags = ((BandMask.GMF_ALPHA.getValue() | BandMask.GMF_PER_DATASET.getValue()) == 0) ? true : false;
                try {
                    poMask = new GdalRescaledAlphaBand(poDS.GetRasterBand(2));
                } catch (Exception ex) {
                    System.out.println("Out of memory");
                    poMask = null;
                }
                bOwnMask = true;
                return poMask;
            }
        }

        if (poDS != null
                && poDS.GetRasterCount() == 4
                && (this == poDS.GetRasterBand(1)
                || this == poDS.GetRasterBand(2)
                || this == poDS.GetRasterBand(3))
                && poDS.GetRasterBand(4).GetColorInterpretation() == GDALColorInterp.GCI_AlphaBand) {
            if (poDS.GetRasterBand(4).GetRasterDataType() == GDALDataType.GDT_Byte) {
                nMaskFlags = ((BandMask.GMF_ALPHA.getValue() | BandMask.GMF_PER_DATASET.getValue()) == 0) ? true : false;
                poMask = poDS.GetRasterBand(4);
                return poMask;
            } else if (poDS.GetRasterBand(4).GetRasterDataType() == GDALDataType.GDT_UInt16) {
                nMaskFlags = ((BandMask.GMF_ALPHA.getValue() | BandMask.GMF_PER_DATASET.getValue()) == 0) ? true : false;
                try {
                    poMask = new GdalRescaledAlphaBand(poDS.GetRasterBand(4));
                } catch (Exception ex) {
                    System.out.println("Out of memory");
                    poMask = null;
                }
                bOwnMask = true;
                return poMask;
            }
        }

        /* -------------------------------------------------------------------- */
        /*      Fallback to all valid case.                                     */
        /* -------------------------------------------------------------------- */
        nMaskFlags = (BandMask.GMF_ALL_VALID.getValue() == 0) ? true : false;
        try {
            poMask = new GdalAllValidMaskBand(this);
        } catch (Exception ex) {
            System.out.println("Out of memory");
            poMask = null;
        }
        bOwnMask = true;

        return poMask;
    }

    private int CSLCount(String[] papszNoDataValues) {
        return 0;
    }

    private String[] CSLTokenizeStringComplex(String pszNoDataValues, String s, boolean b, boolean b1) {
        return new String[0];
    }


    public void GetBlockSize(AtomicInteger pnXSize, AtomicInteger pnYSize) {
        //TODO maybe link
        if (nBlockXSize.get() <= 0 || nBlockYSize.get() <= 0) {
            System.out.println(String.format("Invalid block dimension : %d * %d",
                    nBlockXSize.get(), nBlockYSize.get()));
            if (pnXSize != null)
                pnXSize.set(0);
            if (pnYSize != null)
                pnYSize.set(0);
        } else {
            if (pnXSize != null)
                pnXSize.set(nBlockXSize.get());
            if (pnYSize != null)
                pnYSize.set(nBlockYSize.get());
        }
    }

    public GDALDataType GetRasterDataType() {
        return eDataType;
    }

    public boolean getMaskFlags() {

        // If we don't have a band yet, force this now so that the masks value
        // will be initialized.
        if (poMask == null)
            GetMaskBand();

        return nMaskFlags;
    }

    /**
     * \brief Fetch the no data value for this band.
     * <p>
     * If there is no out of data value, an out of range value will generally
     * be returned.  The no data value for a band is generally a special marker
     * value used to mark pixels that are not valid data.  Such pixels should
     * generally not be displayed, nor contribute to analysis operations.
     * <p>
     * The no data value returned is 'raw', meaning that it has no offset and
     * scale applied.
     * <p>
     * This method is the same as the C function GDALGetRasterNoDataValue().
     *
     * @param pbSuccess pointer to a boolean to use to indicate if a value
     *                  is actually associated with this layer.  May be NULL (default).
     * @return the nodata value for this band.
     */
    public double GetNoDataValue(Boolean pbSuccess) {
        //TODO this link doesnt work
        if (pbSuccess != null)
            pbSuccess = false;

        return -1e10;
    }

    /**
     * \brief Fetch the raster value offset.
     * <p>
     * This value (in combination with the GetScale() value) can be used to
     * transform raw pixel values into the units returned by GetUnitType().
     * For example this might be used to store elevations in GUInt16 bands
     * with a precision of 0.1, and starting from -100.
     * <p>
     * Units value = (raw value * scale) + offset
     * <p>
     * Note that applying scale and offset is of the responsibility of the user,
     * and is not done by methods such as RasterIO() or ReadBlock().
     * <p>
     * For file formats that don't know this intrinsically a value of zero
     * is returned.
     * <p>
     * This method is the same as the C function GDALGetRasterOffset().
     *
     * @param pbSuccess pointer to a boolean to use to indicate if the
     *                  returned value is meaningful or not.  May be NULL (default).
     * @return the raster offset.
     */
    public double getRasterOffset(Boolean pbSuccess) {
        //TODO link doesnt work yet
        if (pbSuccess != null)
            pbSuccess = false;

        return 0.0;
    }

    /**
     * \brief Fetch the raster value scale.
     * <p>
     * This value (in combination with the GetOffset() value) can be used to
     * transform raw pixel values into the units returned by GetUnitType().
     * For example this might be used to store elevations in GUInt16 bands
     * with a precision of 0.1, and starting from -100.
     * <p>
     * Units value = (raw value * scale) + offset
     * <p>
     * Note that applying scale and offset is of the responsibility of the user,
     * and is not done by methods such as RasterIO() or ReadBlock().
     * <p>
     * For file formats that don't know this intrinsically a value of one
     * is returned.
     * <p>
     * This method is the same as the C function GDALGetRasterScale().
     *
     * @param pbSuccess pointer to a boolean to use to indicate if the
     *                  returned value is meaningful or not.  May be NULL (default).
     * @return the raster scale.
     */
    public double getRasterScale(Boolean pbSuccess) {
        //TODO link doesnt work yet
        if (pbSuccess != null)
            pbSuccess = false;

        return 1.0;
    }

    public GDALColorInterp GetColorInterpretation() {
        return GCI_Undefined;
    }

    public GDALColorTableH GetRasterColorTable() {
        return null;
    }

    public int GetXSize() {
        return 0;
    }

    public int GetYSize() {
        return 0;
    }

    public GdalDataset GetDataset() {
        return null;
    }

    public void flushCache() {
        //TODO call from VrtSimpleSource
    }

    /*
    public void RasterIO(GDALRWFlag eRWFlag,
                         int nXOff, int nYOff, int nXSize, int nYSize, int nBufXSize, int nBufYSize,
                         GDALDataType eBufType,
                         long nPixelSpace,
                         long nLineSpace,
                         GDALRasterIOExtraArg psExtraArg) {
        GDALRasterIOExtraArg sExtraArg;
        if (psExtraArg == null) {
            INIT_RASTERIO_EXTRA_ARG(sExtraArg);
            psExtraArg = sExtraArg;
        } else if (psExtraArg.nVersion != RASTERIO_EXTRA_ARG_CURRENT_VERSION) {
            throw new RuntimeException("Unhandled version of GDALRasterIOExtraArg");
        }

        GDALRasterIOExtraArgSetResampleAlg(psExtraArg, nXSize, nYSize,
                nBufXSize, nBufYSize);


        if (nXSize < 1 || nYSize < 1 || nBufXSize < 1 || nBufYSize < 1) {


            return;
        }

        if (eRWFlag == GF_Write) {
            if (eFlushBlockErr != CE_None) {
                throw new RuntimeException(
                        "An error occurred while writing a dirty block "
                        "from GDALRasterBand::RasterIO");
                CPLErr eErr = eFlushBlockErr;
                eFlushBlockErr = CE_None;
                return eErr;
            }
            if (eAccess != GA_Update) {
                throw new RuntimeException(
                        "Write operation not permitted on dataset opened "
                        "in read-only mode");
                return;
            }
        }

        if (nPixelSpace == 0) {
            nPixelSpace = GDALGetDataTypeSizeBytes(eBufType);
        }

        if (nLineSpace == 0) {
            nLineSpace = nPixelSpace * nBufXSize;
        }

        if (nXOff < 0 || nXOff > Integer.MAX_VALUE - nXSize || nXOff + nXSize > nRasterXSize
                || nYOff < 0 || nYOff > Integer.MAX_VALUE - nYSize || nYOff + nYSize > nRasterYSize) {
            throw new RuntimeException("Access window out of range in RasterIO().  Requested\n (%d,%d) of size %dx%d on raster of %dx%d.");
            return;
        }

        if (eRWFlag != GF_Read && eRWFlag != GF_Write) {
            throw new RuntimeException("eRWFlag = %d, only GF_Read (0) and GF_Write (1) are legal.");
            return;
        }


        boolean bCallLeaveReadWrite = CPL_TO_BOOL(EnterReadWrite(eRWFlag));

        CPLErr eErr;
        if (bForceCachedIO == 0)
            eErr = IRasterIO(eRWFlag, nXOff, nYOff, nXSize, nYSize,
                    nBufXSize, nBufYSize, eBufType,
                    nPixelSpace, nLineSpace, psExtraArg);
        else
            eErr = IRasterIO(eRWFlag, nXOff, nYOff, nXSize, nYSize,
                    pData, nBufXSize, nBufYSize, eBufType,
                    nPixelSpace, nLineSpace, psExtraArg);

        if (bCallLeaveReadWrite) LeaveReadWrite();

        return eErr;
    }*/

    /*
    public void IRasterIO(GDALRWFlag eRWFlag,
                          int nXOff, int nYOff, int nXSize, int nYSize,
                          int nBufXSize, int nBufYSize,
                          GDALDataType eBufType,
                          long nPixelSpace, long nLineSpace,
                          GDALRasterIOExtraArg psExtraArg) {
        if (eRWFlag == GF_Write && eFlushBlockErr != CE_None) {
            throw new RuntimeException(
                    "An error occurred while writing a dirty block "
                    "from GDALRasterBand::IRasterIO");
            return;
        }
        if (nBlockXSize <= 0 || nBlockYSize <= 0) {
            throw new RuntimeException("Invalid block size");
            return;
        }

        int nBandDataSize = GDALGetDataTypeSizeBytes(eDataType);
        int nBufDataSize = GDALGetDataTypeSizeBytes(eBufType);
        GByte pabySrcBlock = null;
        GDALRasterBlock poBlock = null;
        int nLBlockX = -1;
        int nLBlockY = -1;
        int iBufYOff = 0;
        int iBufXOff = 0;
        int iSrcY = 0;
        Boolean bUseIntegerRequestCoords =
                (psExtraArg.bFloatingPointWindowValidity != 0 ||
                        (nXOff == psExtraArg.dfXOff &&
                                nYOff == psExtraArg.dfYOff &&
                                nXSize == psExtraArg.dfXSize &&
                                nYSize == psExtraArg.dfYSize));

        if (nPixelSpace == nBufDataSize
                && nLineSpace == nPixelSpace * nXSize
                && nBlockXSize == GetXSize()
                && nBufXSize == nXSize
                && nBufYSize == nYSize
                && bUseIntegerRequestCoords) {
            for (iBufYOff = 0; iBufYOff < nBufYSize; iBufYOff++) {
                iSrcY = iBufYOff + nYOff;

                if (iSrcY < nLBlockY * nBlockYSize
                        || iSrcY - nBlockYSize >= nLBlockY * nBlockYSize) {
                    nLBlockY = iSrcY / nBlockYSize;
                    Boolean bJustInitialize =
                            eRWFlag == GF_Write
                                    && nXOff == 0 && nXSize == nBlockXSize
                                    && nYOff <= nLBlockY * nBlockYSize
                                    && nYOff + nYSize - nBlockYSize >= nLBlockY * nBlockYSize;

                    // Is this a partial tile at right and/or bottom edges of
                    // the raster, and that is going to be completely written?
                    // If so, do not load it from storage, but zero it so that
                    // the content outsize of the validity area is initialized.
                    Boolean bMemZeroBuffer = false;
                    if (eRWFlag == GF_Write && !bJustInitialize &&
                            nXOff == 0 && nXSize == nBlockXSize &&
                            nYOff <= nLBlockY * nBlockYSize &&
                            nYOff + nYSize == GetYSize() &&
                            nLBlockY * nBlockYSize > GetYSize() - nBlockYSize) {
                        bJustInitialize = true;
                        bMemZeroBuffer = true;
                    }

                    if (poBlock != null)
                        poBlock.DropLock();

                    Integer nErrorCounter = CPLGetErrorCounter();
                    poBlock = GetLockedBlockRef(0, nLBlockY, bJustInitialize);
                    if (poBlock == null) {
                        if (strstr(CPLGetLastErrorMsg(), "IReadBlock failed") == nullptr) {
                            CPLError(CE_Failure, CPLE_AppDefined,
                                    "GetBlockRef failed at X block offset %d, "
                                    "Y block offset %d%s",
                                    0, nLBlockY,
                                    (nErrorCounter != CPLGetErrorCounter()) ?
                                            CPLSPrintf(": %s", CPLGetLastErrorMsg()) : "");
                        }
                        eErr = CE_Failure;
                        break;
                    }

                    if (eRWFlag == GF_Write)
                        poBlock.MarkDirty();

                    pabySrcBlock = static_cast < GByte * > (poBlock -> GetDataRef());
                    if (bMemZeroBuffer) {
                        memset(pabySrcBlock, 0,
                                static_cast < GPtrDiff_t > (nBandDataSize) * nBlockXSize * nBlockYSize);
                    }
                }

                // To make Coverity happy. Should not happen by design.
                if (pabySrcBlock == null) {
                    CPLAssert(false);
                    eErr = CE_Failure;
                    break;
                }

            const auto nSrcByteOffset =
                        (static_cast < GPtrDiff_t > (iSrcY - nLBlockY * nBlockYSize) * nBlockXSize + nXOff)
                                * nBandDataSize;

                if (eDataType == eBufType) {
                    if (eRWFlag == GF_Read)
                        memcpy(
                                static_cast < GByte * > (pData)
                                        + static_cast < GPtrDiff_t > (iBufYOff) * nLineSpace,
                                pabySrcBlock + nSrcByteOffset,
                                static_cast < size_t > (nLineSpace));
                    else
                        memcpy(
                                pabySrcBlock + nSrcByteOffset,
                                static_cast < GByte * > (pData)
                                        + static_cast < GPtrDiff_t > (iBufYOff) * nLineSpace,
                                static_cast < size_t > (nLineSpace));
                } else {
                    // Type to type conversion.

                    if (eRWFlag == GF_Read)
                        GDALCopyWords(
                                pabySrcBlock + nSrcByteOffset,
                                eDataType, nBandDataSize,
                                static_cast < GByte * > (pData)
                                        + static_cast < GPtrDiff_t > (iBufYOff) * nLineSpace,
                                eBufType,
                                static_cast < int>(nPixelSpace), nBufXSize );
                else
                    GDALCopyWords(
                            static_cast < GByte * > (pData)
                                    + static_cast < GPtrDiff_t > (iBufYOff) * nLineSpace,
                            eBufType, static_cast < int>(nPixelSpace),
                            pabySrcBlock + nSrcByteOffset,
                            eDataType, nBandDataSize, nBufXSize );
                }

                if (psExtraArg -> pfnProgress != nullptr &&
                        !psExtraArg -> pfnProgress(1.0 * (iBufYOff + 1) / nBufYSize, "",
                                psExtraArg -> pProgressData)) {
                    eErr = CE_Failure;
                    break;
                }
            }

            if (poBlock)
                poBlock -> DropLock();

            return eErr;
        }

        if ((nBufXSize < nXSize || nBufYSize < nYSize)
                && GetOverviewCount() > 0 && eRWFlag == GF_Read) {
            GDALRasterIOExtraArg sExtraArg;
            GDALCopyRasterIOExtraArg( & sExtraArg, psExtraArg);

        const int nOverview =
                    GDALBandGetBestOverviewLevel2(this, nXOff, nYOff, nXSize, nYSize,
                            nBufXSize, nBufYSize, & sExtraArg );
            if (nOverview >= 0) {
                GDALRasterBand * poOverviewBand = GetOverview(nOverview);
                if (poOverviewBand == nullptr)
                    return CE_Failure;

                return poOverviewBand -> RasterIO(
                        eRWFlag, nXOff, nYOff, nXSize, nYSize,
                        pData, nBufXSize, nBufYSize, eBufType,
                        nPixelSpace, nLineSpace, & sExtraArg );
            }
        }

        if (eRWFlag == GF_Read &&
                nBufXSize < nXSize / 100 && nBufYSize < nYSize / 100 &&
                nPixelSpace == nBufDataSize &&
                nLineSpace == nPixelSpace * nBufXSize &&
                CPLTestBool(CPLGetConfigOption("GDAL_NO_COSTLY_OVERVIEW", "NO"))) {
            memset(pData, 0, static_cast < size_t > (nLineSpace * nBufYSize));
            return CE_None;
        }

        int iSrcX = 0;

        if ( // nPixelSpace == nBufDataSize &&
                nXSize == nBufXSize
                        && nYSize == nBufYSize
                        && bUseIntegerRequestCoords) {


            int nLBlockXStart = nXOff / nBlockXSize;
            int nXSpanEnd = nBufXSize + nXOff;

            int nYInc = 0;
            for (iBufYOff = 0, iSrcY = nYOff;
                 iBufYOff < nBufYSize;
                 iBufYOff += nYInc, iSrcY += nYInc) {
                GPtrDiff_t iSrcOffset = 0;
                int nXSpan = 0;

                GPtrDiff_t iBufOffset =
                        static_cast < GPtrDiff_t > (iBufYOff) *
                                static_cast < GPtrDiff_t > (nLineSpace);
                nLBlockY = iSrcY / nBlockYSize;
                nLBlockX = nLBlockXStart;
                iSrcX = nXOff;
                while (iSrcX < nXSpanEnd) {
                    nXSpan = nLBlockX * nBlockXSize;
                    if (nXSpan < Integer.MAX_VALUE - nBlockXSize)
                        nXSpan += nBlockXSize;
                    else
                        nXSpan = Integer.MAX_VALUE;
                    int nXRight = nXSpan;
                    nXSpan = (nXSpan < nXSpanEnd ? nXSpan : nXSpanEnd) - iSrcX;
                    nXSpanSize = nXSpan * nPixelSpace;

                    boolean bJustInitialize =
                            eRWFlag == GF_Write
                                    && nYOff <= nLBlockY * nBlockYSize
                                    && nYOff + nYSize - nBlockYSize >= nLBlockY * nBlockYSize
                                    && nXOff <= nLBlockX * nBlockXSize
                                    && nXOff + nXSize >= nXRight;

                    // Is this a partial tile at right and/or bottom edges of
                    // the raster, and that is going to be completely written?
                    // If so, do not load it from storage, but zero it so that
                    // the content outsize of the validity area is initialized.
                    boolean bMemZeroBuffer = false;
                    if (eRWFlag == GF_Write && !bJustInitialize &&
                            nXOff <= nLBlockX * nBlockXSize &&
                            nYOff <= nLBlockY * nBlockYSize &&
                            (nXOff + nXSize >= nXRight ||
                                    (nXOff + nXSize == GetXSize() &&
                                            nXRight > GetXSize())) &&
                            (nYOff + nYSize - nBlockYSize >= nLBlockY * nBlockYSize ||
                                    (nYOff + nYSize == GetYSize() &&
                                            nLBlockY * nBlockYSize > GetYSize() - nBlockYSize))) {
                        bJustInitialize = true;
                        bMemZeroBuffer = true;
                    }

                    GUInt32 nErrorCounter = CPLGetErrorCounter();
                    poBlock = GetLockedBlockRef(nLBlockX, nLBlockY,
                            bJustInitialize);
                    if (poBlock == null) {
                        if (strstr(CPLGetLastErrorMsg(), "IReadBlock failed") == nullptr) {
                            CPLError(CE_Failure, CPLE_AppDefined,
                                    "GetBlockRef failed at X block offset %d, "
                                    "Y block offset %d%s",
                                    nLBlockX, nLBlockY,
                                    (nErrorCounter != CPLGetErrorCounter()) ?
                                            CPLSPrintf(": %s", CPLGetLastErrorMsg()) : "");
                        }
                        return (CE_Failure);
                    }

                    if (eRWFlag == GF_Write)
                        poBlock.MarkDirty();

                    pabySrcBlock = static_cast < GByte * > (poBlock -> GetDataRef());
                    if (bMemZeroBuffer) {
                        memset(pabySrcBlock, 0,
                                static_cast < GPtrDiff_t > (nBandDataSize) * nBlockXSize * nBlockYSize);
                    }
                    iSrcOffset =
                            (static_cast < GPtrDiff_t > (iSrcX)
                                    - static_cast < GPtrDiff_t > (nLBlockX * nBlockXSize)
                                    + (static_cast < GPtrDiff_t > (iSrcY)
                                    - static_cast < GPtrDiff_t > (nLBlockY) * nBlockYSize)
                                    * nBlockXSize)
                                    * nBandDataSize;
                    // Fill up as many rows as possible for the loaded block.
                    int kmax = Math.min(
                            nBlockYSize - (iSrcY % nBlockYSize), nBufYSize - iBufYOff);
                    for (int k = 0; k < kmax; k++) {
                        if (eDataType == eBufType
                                && nPixelSpace == nBufDataSize) {
                            if (eRWFlag == GF_Read)
                                memcpy(static_cast < GByte * > (pData) + iBufOffset
                                                + static_cast < GPtrDiff_t > (k) * nLineSpace,
                                        pabySrcBlock + iSrcOffset,
                                        nXSpanSize);
                            else
                                memcpy(pabySrcBlock + iSrcOffset,
                                        static_cast < GByte * > (pData) + iBufOffset
                                                + static_cast < GPtrDiff_t > (k) * nLineSpace,
                                        nXSpanSize);
                        } else {
                            if (eRWFlag == GF_Read)
                                GDALCopyWords(
                                        pabySrcBlock + iSrcOffset,
                                        eDataType, nBandDataSize,
                                        static_cast < GByte * > (pData) + iBufOffset
                                                + static_cast < GPtrDiff_t > (k) * nLineSpace,
                                        eBufType, static_cast < int>(nPixelSpace),
                                    nXSpan );
                        else
                            GDALCopyWords(
                                    static_cast < GByte * > (pData) + iBufOffset +
                                            static_cast < GPtrDiff_t > (k) * nLineSpace,
                                    eBufType, static_cast < int>(nPixelSpace),
                                    pabySrcBlock + iSrcOffset,
                                    eDataType, nBandDataSize, nXSpan );
                        }

                        iSrcOffset += nBlockXSize * nBandDataSize;
                    }

                    iBufOffset = CPLUnsanitizedAdd < GPtrDiff_t > (iBufOffset, nXSpanSize);
                    nLBlockX++;
                    iSrcX += nXSpan;

                    poBlock.DropLock();
                    poBlock = null;
                }

                nYInc = nBlockYSize - (iSrcY % nBlockYSize);
            }

            return;
        }


        double dfXOff = nXOff;
        double dfYOff = nYOff;
        double dfXSize = nXSize;
        double dfYSize = nYSize;
        if (psExtraArg.bFloatingPointWindowValidity != 0) {
            dfXOff = psExtraArg.dfXOff;
            dfYOff = psExtraArg.dfYOff;
            dfXSize = psExtraArg.dfXSize;
            dfYSize = psExtraArg.dfYSize;
        }

        double dfSrcXInc = dfXSize / (double) (nBufXSize);
        double dfSrcYInc = dfYSize / (double) (nBufYSize);

        if (eRWFlag == GF_Write) {
            GByte pabyDstBlock = null;

            for (int iDstY = nYOff; iDstY < nYOff + nYSize; iDstY++) {
                GPtrDiff_t iBufOffset = 0;
                GPtrDiff_t iDstOffset = 0;
                iBufYOff = (int) ((iDstY - nYOff) / dfSrcYInc);

                for (int iDstX = nXOff; iDstX < nXOff + nXSize; iDstX++) {
                    iBufXOff = (int) ((iDstX - nXOff) / dfSrcXInc);
                    iBufOffset =
                            static_cast < GPtrDiff_t > (iBufYOff)
                                    * static_cast < GPtrDiff_t > (nLineSpace)
                                    + iBufXOff * static_cast < GPtrDiff_t > (nPixelSpace);

                    // FIXME: this code likely doesn't work if the dirty block gets
                    // flushed to disk before being completely written.
                    // In the meantime, bJustInitialize should probably be set to
                    // FALSE even if it is not ideal performance wise, and for
                    // lossy compression.

                    if (iDstX < nLBlockX * nBlockXSize
                            || iDstX - nBlockXSize >= nLBlockX * nBlockXSize
                            || iDstY < nLBlockY * nBlockYSize
                            || iDstY - nBlockYSize >= nLBlockY * nBlockYSize) {
                        nLBlockX = iDstX / nBlockXSize;
                        nLBlockY = iDstY / nBlockYSize;

                        boolean bJustInitialize =
                                nYOff <= nLBlockY * nBlockYSize
                                        && nYOff + nYSize - nBlockYSize >= nLBlockY * nBlockYSize
                                        && nXOff <= nLBlockX * nBlockXSize
                                        && nXOff + nXSize - nBlockXSize >= nLBlockX * nBlockXSize;
                        if (poBlock != null)
                            poBlock.DropLock();

                        poBlock = GetLockedBlockRef(nLBlockX, nLBlockY,
                                bJustInitialize);
                        if (poBlock == null) {
                            return;
                        }

                        poBlock.MarkDirty();

                        pabyDstBlock = (GByte) (poBlock.GetDataRef());
                    }

                    // To make Coverity happy. Should not happen by design.
                    if (pabyDstBlock == null) {
                        break;
                    }

                    iDstOffset =
                            (static_cast < GPtrDiff_t > (iDstX)
                                    - static_cast < GPtrDiff_t > (nLBlockX) * nBlockXSize
                                    + (static_cast < GPtrDiff_t > (iDstY)
                                    - static_cast < GPtrDiff_t > (nLBlockY) * nBlockYSize)
                                    * nBlockXSize)
                                    * nBandDataSize;

                    if (eDataType == eBufType) {
                        memcpy(
                                pabyDstBlock + iDstOffset,
                                static_cast < GByte * > (pData) + iBufOffset,
                                nBandDataSize);
                    } else {

                        GDALCopyWords(
                                static_cast < GByte * > (pData) + iBufOffset, eBufType, 0,
                                pabyDstBlock + iDstOffset, eDataType, 0,
                                1);
                    }
                }
            }
        } else {
            if (psExtraArg.eResampleAlg != GRIORA_NearestNeighbour) {
                if ((psExtraArg.eResampleAlg == GRIORA_Cubic ||
                        psExtraArg.eResampleAlg == GRIORA_CubicSpline ||
                        psExtraArg.eResampleAlg == GRIORA_Bilinear ||
                        psExtraArg.eResampleAlg == GRIORA_Lanczos) &&
                        GetColorTable() != null) {
                    throw new RuntimeException(
                            "Resampling method not supported on paletted band. " +
                                    "Falling back to nearest neighbour");
                } else if (psExtraArg.eResampleAlg == GRIORA_Gauss &&
                        GDALDataTypeIsComplex(eDataType)) {
                    throw new RuntimeException(
                            "Resampling method not supported on complex data type " +
                                    "band. Falling back to nearest neighbour");
                } else {
                    return RasterIOResampled(eRWFlag,
                            nXOff, nYOff, nXSize, nYSize, nBufXSize, nBufYSize,
                            eBufType,
                            nPixelSpace, nLineSpace,
                            psExtraArg);
                }
            }

            double dfSrcX = 0.0;
            double dfSrcY = 0.0;
            int nLimitBlockY = 0;
            boolean bByteCopy = eDataType == eBufType && nBandDataSize == 1;
            int nStartBlockX = -nBlockXSize;
            double EPS = 1e-10;

            for (iBufYOff = 0; iBufYOff < nBufYSize; iBufYOff++) {
                // Add small epsilon to avoid some numeric precision issues.
                dfSrcY = (iBufYOff + 0.5) * dfSrcYInc + dfYOff + EPS;
                dfSrcX = 0.5 * dfSrcXInc + dfXOff + EPS;
                iSrcY = (int) (Math.min(Math.max(0.0, dfSrcY),
                        (double) (nRasterYSize - 1)));

                GPtrDiff_t iBufOffset =
                        static_cast < GPtrDiff_t > (iBufYOff)
                                * static_cast < GPtrDiff_t > (nLineSpace);

                if (iSrcY >= nLimitBlockY) {
                    nLBlockY = iSrcY / nBlockYSize;
                    nLimitBlockY = nLBlockY * nBlockYSize;
                    if (nLimitBlockY < Integer.MAX_VALUE - nBlockYSize)
                        nLimitBlockY += nBlockYSize;
                    else
                        nLimitBlockY = Integer.MAX_VALUE;
                    // Make sure a new block is loaded.
                    nStartBlockX = -nBlockXSize;
                } else if ((int) (dfSrcX) < nStartBlockX) {
                    // Make sure a new block is loaded.
                    nStartBlockX = -nBlockXSize;
                }

                GPtrDiff_t iSrcOffsetCst =
                        (iSrcY - nLBlockY * nBlockYSize)
                                * static_cast < GPtrDiff_t > (nBlockXSize);

                GPtrDiff_t iSrcOffset = 0;

                for (iBufXOff = 0;
                     iBufXOff < nBufXSize;
                     iBufXOff++, dfSrcX += dfSrcXInc) {
                    // TODO?: try to avoid the clamping for most iterations
                    iSrcX = (int) (Math.min(Math.max(0.0, dfSrcX),
                            (double) (nRasterXSize - 1)));

                    if (iSrcX >= nBlockXSize + nStartBlockX) {
                        nLBlockX = iSrcX / nBlockXSize;
                        nStartBlockX = nLBlockX * nBlockXSize;

                        if (poBlock != null)
                            poBlock.DropLock();

                        poBlock = GetLockedBlockRef(nLBlockX, nLBlockY, false);
                        if (poBlock == null) {
                            eErr = CE_Failure;
                            break;
                        }

                        pabySrcBlock = (GByte) (poBlock.GetDataRef());
                    }
                    GPtrDiff_t nDiffX = (GPtrDiff_t) (iSrcX - nStartBlockX);

                    // To make Coverity happy.  Should not happen by design.
                    if (pabySrcBlock == null) {
                        CPLAssert(false);
                        eErr = CE_Failure;
                        break;
                    }

                    if (bByteCopy) {
                        iSrcOffset = nDiffX + iSrcOffsetCst;
                        static_cast<GByte *>(pData)[iBufOffset] =
                                pabySrcBlock[iSrcOffset];
                    } else if (eDataType == eBufType) {
                        iSrcOffset =
                                (nDiffX + iSrcOffsetCst)
                                        * nBandDataSize;
                        memcpy(static_cast < GByte * > (pData) + iBufOffset,
                                pabySrcBlock + iSrcOffset, nBandDataSize);
                    } else {
                        // Type to type conversion ... ouch, this is expensive way
                        // of handling single words.
                        iSrcOffset =
                                (nDiffX + iSrcOffsetCst)
                                        * nBandDataSize;
                        GDALCopyWords(
                                pabySrcBlock + iSrcOffset, eDataType, 0,
                                static_cast < GByte * > (pData) + iBufOffset, eBufType, 0,
                                1);
                    }

                    iBufOffset += static_cast < int>(nPixelSpace);
                }
                if (eErr == CE_Failure)
                    break;

                if (psExtraArg.pfnProgress != null &&
                        !psExtraArg.pfnProgress(1.0 * (iBufYOff + 1) / nBufYSize, "",
                                psExtraArg.pProgressData)) {
                    eErr = CE_Failure;
                    break;
                }
            }
        }

        if (poBlock != null)
            poBlock.DropLock();

        return;
    }*/
}
