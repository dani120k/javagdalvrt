package sokolov.model.datasets;

import sokolov.model.enums.*;

import static sokolov.model.enums.GDALColorInterp.GCI_Undefined;

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
    int nBlockXSize = -1;
    int nBlockYSize = -1;
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
                        } catch ( Exception ex )
                        {
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
            } catch ( Exception ex )
            {
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
                } catch (Exception ex )
                {
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
                } catch ( Exception ex )
                {
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
        } catch ( Exception ex )
        {
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


    public void GetBlockSize(Integer pnXSize, Integer pnYSize) {
        //TODO maybe link
        if (nBlockXSize <= 0 || nBlockYSize <= 0) {
            System.out.println(String.format("Invalid block dimension : %d * %d",
                    nBlockXSize, nBlockYSize));
            if (pnXSize != null)
                pnXSize = 0;
            if (pnYSize != null)
                pnYSize = 0;
        } else {
            if (pnXSize != null)
                pnXSize = nBlockXSize;
            if (pnYSize != null)
                pnYSize = nBlockYSize;
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

    protected double GetXSize() {
        return 0;
    }

    protected double GetYSize() {
        return 0;
    }

    protected GdalDataset GetDataset() {
        return null;
    }
}
