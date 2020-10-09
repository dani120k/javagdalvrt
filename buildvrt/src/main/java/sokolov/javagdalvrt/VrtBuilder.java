package sokolov.javagdalvrt;


import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.FileDirectoryEntry;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;
import org.w3c.dom.Node;
import sokolov.model.datasets.*;
import sokolov.model.enums.*;
import sokolov.model.sources.VrtComplexSource;
import sokolov.model.sources.VrtSimpleSource;
import sokolov.model.supclasses.DfScrObject;

import javax.imageio.ImageIO;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VrtBuilder {
    //TODO should be in enum
    private int GDAL_OF_RASTER = 0x02;
    private int GDAL_OF_VERBOSE_ERROR =  0x40;


    //Special value to indicate that nodata is not set.
    private double VRT_NODATA_UNSET = -1234.56;

    private static int GEOTRSFRM_TOPLEFT_X = 0;
    private static int GEOTRSFRM_WE_RES = 1;
    private static int GEOTRSFRM_ROTATION_PARAM1 = 2;
    private static int GEOTRSFRM_TOPLEFT_Y = 3;
    private static int GEOTRSFRM_ROTATION_PARAM2 = 4;
    private static int GEOTRSFRM_NS_RES = 5;

    //список свойств датасетов was pasDatasetProperties
    private List<DatasetProperty> datasetPropertyList = new ArrayList();
    //список имен файлов
    private List<String> inputFileNamesList = new ArrayList<String>();

    //имкеет ли геотрансформацию
    private boolean bHasGeoTransform;

    private double we_res;
    private double ns_res;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private int nRasterXSize;
    private int nRasterYSize;
    private String pszProjectionRef;
    private String[] papszOpenOptions;
    private boolean bHideNoData;
    private boolean bAllowSrcNoData;
    private String pszResampling;
    private int nBands;
    private int[] panBandList;
    private BandProperty[] pasBandProperties;
    private boolean bAllowVRTNoData;
    private boolean bAddAlpha;
    private boolean bHasDatasetMask;
    private int nMaxBandNo;
    private DatasetProperty[] pasDatasetProperties;
    private int nSubdataset;
    private boolean bSeparate;
    private boolean bFirst;
    private boolean bUserExtent;
    private ResolutionStrategy resolutionStrategy;
    private int nSrcDataCount;
    private double[] padfSrcNoData;
    private int nSrcNoDataCount;
    private int nVRTNoDataCount;
    private double[] padfVRTNoData;
    private boolean bAllowProjectionDifference;
    private String pszOutputFilename;
    private int nInputFiles;
    private GdalDataset[] pahSrcDS;
    private String[] ppszInputFilenames;
    private boolean bTargetAlignedPixels;
    private boolean bHasRunBuild;

    private String pszSrcNoData;
    private String pszVRTNoData;
    private String pszOutputSRS;

    public VrtBuilder(String pszOutputFilenameIn,
                      int nInputFilesIn,
                      String[] ppszInputFilenamesIn,
                      GdalDataset[] pahSrcDSIn,
                      int[] panBandListIn, int nBandCount, int nMaxBandNoIn,
                      ResolutionStrategy resolutionStrategyIn,
                      double we_resIn, double ns_resIn,
                      boolean bTargetAlignedPixelsIn,
                      double minXIn, double minYIn, double maxXIn, double maxYIn,
                      boolean bSeparateIn, boolean bAllowProjectionDifferenceIn,
                      boolean bAddAlphaIn, boolean bHideNoDataIn, int nSubdatasetIn,
                      String pszSrcNoDataIn, String pszVRTNoDataIn,
                      String pszOutputSRSIn,
                      String pszResamplingIn,
                      String[] papszOpenOptionsIn) {
        this.pszOutputFilename = pszOutputFilenameIn;
        this.nInputFiles = nInputFilesIn;
        this.pahSrcDS = null;
        this.inputFileNamesList = null;
        this.papszOpenOptions = papszOpenOptionsIn;

        if (ppszInputFilenamesIn != null) {
            this.ppszInputFilenames = new String[nInputFiles];
            for (int i = 0; i < nInputFiles; i++) {
                this.ppszInputFilenames[i] = ppszInputFilenamesIn[i];
            }
        } else if (pahSrcDSIn != null) {
            this.pahSrcDS = pahSrcDSIn;
            this.ppszInputFilenames = new String[nInputFiles];
            for (int i = 0; i < nInputFiles; i++) {
                this.ppszInputFilenames[i] = pahSrcDSIn[i].getDescription();
            }
        }

        this.nBands = nBandCount;
        this.panBandList = null;
        if (nBandCount != 0) {
            this.panBandList = panBandListIn;
        }
        this.nMaxBandNo = nMaxBandNoIn;

        this.resolutionStrategy = resolutionStrategyIn;
        this.we_res = we_resIn;
        this.ns_res = ns_resIn;
        this.bTargetAlignedPixels = bTargetAlignedPixelsIn;
        this.minX = minXIn;
        this.minY = minYIn;
        this.maxX = maxXIn;
        this.maxY = maxYIn;
        this.bSeparate = bSeparateIn;
        this.bAllowProjectionDifference = bAllowProjectionDifferenceIn;
        this.bAddAlpha = bAddAlphaIn;
        this.bHideNoData = bHideNoDataIn;
        this.nSubdataset = nSubdatasetIn;
        this.pszSrcNoData = (pszSrcNoDataIn != null) ? pszSrcNoDataIn : null;
        this.pszVRTNoData = (pszVRTNoDataIn != null) ? pszVRTNoDataIn : null;
        this.pszOutputSRS = (pszOutputSRSIn != null) ? pszOutputSRSIn : null;
        this.pszResampling = (pszResamplingIn != null) ? pszResamplingIn : null;

        this.bUserExtent = false;
        this.pszProjectionRef = null;
        this.pasBandProperties = null;
        this.bFirst = true;
        this.bHasGeoTransform = false;
        this.nRasterXSize = 0;
        this.nRasterYSize = 0;
        this.pasDatasetProperties = null;
        this.bAllowSrcNoData = true;
        this.padfSrcNoData = null;
        this.nSrcNoDataCount = 0;
        this.bAllowVRTNoData = true;
        this.padfVRTNoData = null;
        this.nVRTNoDataCount = 0;
        this.bHasRunBuild = false;
        this.bHasDatasetMask = false;
    }

    /**
     * TODO input dataset should by high level interface
     *
     * @param vrtDataset
     */
    public void createVrtSeparate(VrtDataset vrtDataset) {
        int iBand = 1;
        for (int i = 0; i < ppszInputFilenames.length; i++) {
            DatasetProperty psDatasetProperties = pasDatasetProperties[i];

            if (psDatasetProperties.isFileOK() == false)
                continue;

            DfScrObject dfScrObject = new DfScrObject();
            if (bHasGeoTransform) {

                //TODO check because of ckip
                //if (!getSrcDstWin(psDatasetProperties,
                //        we_res, ns_res, minX, minY, maxX, maxY,
                //        dfScrObject))
                //    continue;
            } else {
                dfScrObject.dfSrcXOff = dfScrObject.dfSrcYOff = dfScrObject.dfDstXOff = dfScrObject.dfDstYOff = 0;
                dfScrObject.dfSrcXSize = dfScrObject.dfDstXSize = nRasterXSize;
                dfScrObject.dfSrcYSize = dfScrObject.dfDstYSize = nRasterYSize;
            }

            String dsFileName = ppszInputFilenames[i];

            vrtDataset.AddBand(psDatasetProperties.firstBandType, null);

            GdalProxyPoolDataset hProxyDS =
                    new GdalProxyPoolDataset(dsFileName,
                            psDatasetProperties.nRasterXSize,
                            psDatasetProperties.nRasterYSize,
                            GdalAccess.GA_ReadOnly, true, pszProjectionRef,
                            psDatasetProperties.adfGeoTransform);


            hProxyDS.setOpenOptions(papszOpenOptions);

            hProxyDS.AddSrcBandDescription(psDatasetProperties.firstBandType,
                    psDatasetProperties.nBlockXSize,
                    psDatasetProperties.nBlockYSize);

            VrtSourcedRasterBand hVRTBand = (VrtSourcedRasterBand)vrtDataset.GetRasterBand(iBand);

            if (bHideNoData)
                hVRTBand.setMetadataItem("HideNoDataValue", "1", null);

            VrtSourcedRasterBand poVRTBand = hVRTBand;

            VrtSimpleSource poSimpleSource;
            if (bAllowSrcNoData && psDatasetProperties.pabHasNoData[0]) {
                hVRTBand.setRasterNoDataValue(psDatasetProperties.padfNoDataValues[0]);
                poSimpleSource = new VrtComplexSource();
                poSimpleSource.setNoDataValue(psDatasetProperties.padfNoDataValues[0]);
            } else
                poSimpleSource = new VrtSimpleSource();

            if (pszResampling != null)
                poSimpleSource.setResampling(pszResampling);

            poVRTBand.configureSource(poSimpleSource,
                    hProxyDS.GetRasterBand(1),
                    false,
                    dfScrObject.dfSrcXOff, dfScrObject.dfSrcYOff,
                    dfScrObject.dfSrcXSize, dfScrObject.dfSrcYSize,
                    dfScrObject.dfDstXOff, dfScrObject.dfDstYOff,
                    dfScrObject.dfDstXSize, dfScrObject.dfDstYSize);

            if (psDatasetProperties.pabHasOffset[0])
                poVRTBand.SetOffset(psDatasetProperties.padfOffset[0]);

            if (psDatasetProperties.pabHasScale[0])
                poVRTBand.SetScale(psDatasetProperties.padfScale[0]);

            poVRTBand.AddSource(poSimpleSource);

            GDALDereferenceDataset(hProxyDS);

            iBand++;
        }
    }

    /**
     * TODO input dataset is VrtDatasetH
     */
    public void createVrtNonSeparate(VrtDataset hVRTDS) {
        for (int j = 0; j < nBands; j++) {
            GdalRasterBand hBand;
            int nSelBand = panBandList[j] - 1;
            hVRTDS.AddBand(pasBandProperties[nSelBand].getDataType(), null);

            //TODO maybe some checks with type of getRasterBand
            hBand = hVRTDS.GetRasterBand(j + 1);

            hBand.SetColorInterpretation(pasBandProperties[nSelBand].getColorInterpretation());

            if (pasBandProperties[nSelBand].getColorInterpretation() == GDALColorInterp.GCI_PaletteIndex) {
                hBand.SetColorTable(pasBandProperties[nSelBand].getColorTable());
            }

            if (bAllowVRTNoData && pasBandProperties[nSelBand].isbHasNoData())
                hBand.setRasterNoDataValue(pasBandProperties[nSelBand].getNoDataValue());

            if (bHideNoData)
                hBand.setMetadataItem("HideNoDataValue", "1", null);

            if (pasBandProperties[nSelBand].isbHasOffset())
                hBand.SetOffset(pasBandProperties[nSelBand].getDfOffset());

            if (pasBandProperties[nSelBand].isbHasScale())
                hBand.SetScale(pasBandProperties[nSelBand].getDfScale());
        }

        VrtSourcedRasterBand poMaskVRTBand = null;
        if (bAddAlpha) {
            GdalRasterBand hBand;
            hVRTDS.AddBand(GDALDataType.GDT_Byte, null);
            hBand = hVRTDS.GetRasterBand(nBands + 1);
            hBand.SetColorInterpretation(GDALColorInterp.GCI_AlphaBand);
        } else if (bHasDatasetMask) {
            hVRTDS.CreateMaskBand(BandMask.GMF_PER_DATASET.getValue());
            poMaskVRTBand = (VrtSourcedRasterBand) hVRTDS.GetRasterBand(1).GetMaskBand();
        }

        for (int i = 0; i < inputFileNamesList.size(); i++) {
            DatasetProperty psDatasetProperties = datasetPropertyList.get(i);

            if (psDatasetProperties.isFileOK() == false)
                continue;

            DfScrObject dfScrObject = new DfScrObject();
            if (!getSrcDstWin(psDatasetProperties,
                    we_res, ns_res, minX, minY, maxX, maxY,
                    dfScrObject))
                continue;

            String dsFileName = inputFileNamesList.get(i);

            GdalProxyPoolDataset hProxyDS =
                    new GdalProxyPoolDataset(dsFileName,
                            psDatasetProperties.nRasterXSize,
                            psDatasetProperties.nRasterYSize,
                            GdalAccess.GA_ReadOnly, true, pszProjectionRef,
                            psDatasetProperties.adfGeoTransform);

            hProxyDS.setOpenOptions(papszOpenOptions);

            for (int j = 0; j < nMaxBandNo; j++) {
                hProxyDS.AddSrcBandDescription(pasBandProperties[j].getDataType(),
                        psDatasetProperties.nBlockXSize,
                        psDatasetProperties.nBlockYSize);
            }
            if (bHasDatasetMask && !bAddAlpha) {
                ((GdalProxyPoolRasterBand)hProxyDS.GetRasterBand(1)).AddSrcMaskBandDescription(GDALDataType.GDT_Byte,
                        psDatasetProperties.nMaskBlockXSize,
                        psDatasetProperties.nMaskBlockYSize);
            }

            for (int j = 0; j < nBands; j++) {
                VrtSourcedRasterBand hVRTBand =  (VrtSourcedRasterBand)hVRTDS.GetRasterBand(j + 1);

                /* Place the raster band at the right position in the VRT */
                int nSelBand = panBandList[j] - 1;
                VrtSourcedRasterBand poVRTBand = hVRTBand;

                VrtSimpleSource poSimpleSource;
                if (bAllowSrcNoData && psDatasetProperties.pabHasNoData[nSelBand]) {
                    poSimpleSource = new VrtComplexSource();
                    poSimpleSource.setNoDataValue(psDatasetProperties.padfNoDataValues[nSelBand]);
                } else
                    poSimpleSource = new VrtSimpleSource();
                if (pszResampling != null)
                    poSimpleSource.setResampling(pszResampling);

                poVRTBand.configureSource(poSimpleSource,
                        hProxyDS.GetRasterBand(nSelBand + 1),
                        false,
                        dfScrObject.dfSrcXOff, dfScrObject.dfSrcYOff,
                        dfScrObject.dfSrcXSize, dfScrObject.dfSrcYSize,
                        dfScrObject.dfDstXOff, dfScrObject.dfDstYOff,
                        dfScrObject.dfDstXSize, dfScrObject.dfDstYSize);

                poVRTBand.AddSource(poSimpleSource);
            }

            if (bAddAlpha) {
                VrtSourcedRasterBand hVRTBand = (VrtSourcedRasterBand) hVRTDS.GetRasterBand(nBands + 1);
                /* Little trick : we use an offset of 255 and a scaling of 0, so that in areas covered */
                /* by the source, the value of the alpha band will be 255, otherwise it will be 0 */
                hVRTBand.AddComplexSource(
                        hProxyDS.GetRasterBand(1),
                        dfScrObject.dfSrcXOff, dfScrObject.dfSrcYOff,
                        dfScrObject.dfSrcXSize, dfScrObject.dfSrcYSize,
                        dfScrObject.dfDstXOff, dfScrObject.dfDstYOff,
                        dfScrObject.dfDstXSize, dfScrObject.dfDstYSize,
                        255, 0, VRT_NODATA_UNSET, 0);
            } else if (bHasDatasetMask) {
                VrtSimpleSource poSimpleSource = new VrtSimpleSource();
                if (pszResampling != null)
                    poSimpleSource.setResampling(pszResampling);

                poMaskVRTBand.configureSource(poSimpleSource,
                        hProxyDS.GetRasterBand(1),
                        true,
                        dfScrObject.dfSrcXOff, dfScrObject.dfSrcYOff,
                        dfScrObject.dfSrcXSize, dfScrObject.dfSrcYSize,
                        dfScrObject.dfDstXOff, dfScrObject.dfDstYOff,
                        dfScrObject.dfDstXSize, dfScrObject.dfDstYSize);

                poMaskVRTBand.AddSource(poSimpleSource);
            }

            GDALDereferenceDataset(hProxyDS);
        }
    }

    /**
     * \brief Subtract one from dataset reference count.
     * <p>
     * GDALDataset::Dereference()
     */
    private void GDALDereferenceDataset(GdalProxyPoolDataset hProxyDS) {
        //ffsk
        //return --m_nRefCount;
    }

    private boolean getSrcDstWin(DatasetProperty psDP,
                                 double we_res, double ns_res,
                                 double minX, double minY, double maxX, double maxY,
                                 DfScrObject dfScrObject) {
        //TODO dome links to changed double
        /* Check that the destination bounding box intersects the source bounding box */
        if (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_X] +
                psDP.nRasterXSize *
                        psDP.adfGeoTransform[GEOTRSFRM_WE_RES] < minX)
            return false;
        if (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_X] > maxX)
            return false;
        if (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_Y] +
                psDP.nRasterYSize *
                        psDP.adfGeoTransform[GEOTRSFRM_NS_RES] > maxY)
            return false;
        if (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_Y] < minY)
            return false;

        dfScrObject.dfSrcXSize = psDP.nRasterXSize;
        dfScrObject.dfSrcYSize = psDP.nRasterYSize;
        if (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_X] < minX) {
            dfScrObject.dfSrcXOff = (minX - psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_X]) /
                    psDP.adfGeoTransform[GEOTRSFRM_WE_RES];
            dfScrObject.dfDstXOff = 0.0;
        } else {
            dfScrObject.dfSrcXOff = 0.0;
            dfScrObject.dfDstXOff =
                    ((psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_X] - minX) / we_res);
        }
        if (maxY < psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_Y]) {
            dfScrObject.dfSrcYOff = (psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_Y] - maxY) /
                    -psDP.adfGeoTransform[GEOTRSFRM_NS_RES];
            dfScrObject.dfDstYOff = 0.0;
        } else {
            dfScrObject.dfSrcYOff = 0.0;
            dfScrObject.dfDstYOff =
                    ((maxY - psDP.adfGeoTransform[GEOTRSFRM_TOPLEFT_Y]) / -ns_res);
        }
        dfScrObject.dfDstXSize = (psDP.nRasterXSize *
                psDP.adfGeoTransform[GEOTRSFRM_WE_RES] / we_res);
        dfScrObject.dfDstYSize = (psDP.nRasterYSize *
                psDP.adfGeoTransform[GEOTRSFRM_NS_RES] / ns_res);

        return true;
    }


    private boolean analyzeRaster(GdalDataset hDs, DatasetProperty psDatasetProperties) {
        String dsFileName = hDs.getDescription();
        String[] papszMetadata = hDs.GetMetadata("SUBDATASETS");

        if (CSLCount(papszMetadata) > 0 && hDs.getRasterCount() == 0) {
            pasDatasetProperties = new DatasetProperty[nInputFiles + CSLCount(papszMetadata)];

            if (nSubdataset < 0) {
                int count = 1;
                String subdatasetNameKey;
                for (String papszMetadatum : papszMetadata) {
                    //TODO some strange staff
                }
            } else {
                String subdatasetNameKey;
                //TODO another strange staff
            }
            return false;
        }

        String proj = hDs.GetProjectionRef();
        double[] padfGeoTransform = psDatasetProperties.adfGeoTransform;
        boolean bGotGeoTransform = hDs.GetGeoTransform(padfGeoTransform); //== CE_None;//idk
        if (bSeparate) {
            if (bFirst) {
                bHasGeoTransform = bGotGeoTransform;
                if (!bHasGeoTransform) {
                    if (bUserExtent) {
                        System.out.println("User extent ignored by gdalbuildvrt -separate with ungeoreferenced images.");
                    }

                    if (resolutionStrategy == ResolutionStrategy.USER_RESOLUTION) {
                        System.out.println("User resolution ignored by gdalbuildvrt -separate with ungeoreferenced images.");
                    }
                }
            } else if (bHasGeoTransform != bGotGeoTransform) {
                System.out.println(String.format("gdalbuildvrt -separate cannot stack ungeoreferenced and georeferenced images. Skipping %s", dsFileName));
                return false;
            } else if (!bHasGeoTransform &&
                    (nRasterXSize != hDs.GetRasterXSize() ||
                            nRasterYSize != hDs.GetRasterYSize())) {
                System.out.println(String.format("gdalbuildvrt -separate cannot stack ungeoreferenced images that have not the same dimensions. Skipping %s", dsFileName));
                return false;
            }
        } else {
            if (!bGotGeoTransform) {
                System.out.println(String.format("gdalbuildvrt does not support ungeoreferenced image. Skipping %s", dsFileName));
                return false;
            }
            bHasGeoTransform = true;
        }

        if (bGotGeoTransform) {
            if (padfGeoTransform[GEOTRSFRM_ROTATION_PARAM1] != 0 ||
                    padfGeoTransform[GEOTRSFRM_ROTATION_PARAM2] != 0) {
                System.out.println(String.format("gdalbuildvrt does not support rotated geo transforms. Skipping %s", dsFileName));
                return false;
            }

            if (padfGeoTransform[GEOTRSFRM_NS_RES] >= 0) {
                //TODO
                //System.out.println(String.format("gdalbuildvrt does not support positive NS resolution. Skipping %s", dsFileName));
                //return false;
            }
        }

        psDatasetProperties.nRasterXSize = hDs.GetRasterXSize();
        psDatasetProperties.nRasterYSize = hDs.GetRasterYSize();
        if (bFirst & bSeparate & !bGotGeoTransform) {
            nRasterXSize = hDs.GetRasterXSize();
            nRasterYSize = hDs.GetRasterYSize();
        }

        double ds_minX = padfGeoTransform[GEOTRSFRM_TOPLEFT_X];
        double ds_maxY = padfGeoTransform[GEOTRSFRM_TOPLEFT_Y];
        double ds_maxX = ds_minX +
                hDs.GetRasterXSize() *
                        padfGeoTransform[GEOTRSFRM_WE_RES];
        double ds_minY = ds_maxY +
                hDs.GetRasterYSize() *
                        padfGeoTransform[GEOTRSFRM_NS_RES];

        GdalRasterBand rasterBand = hDs.GetRasterBand(1);
        //TODO maybe link
        rasterBand.GetBlockSize(psDatasetProperties.nBlockXSize, psDatasetProperties.nBlockYSize);

        int _nBands = hDs.getRasterCount();

        //if provided band list
        if (nBands != 0 && _nBands != 0 && nMaxBandNo != 0 && _nBands > nMaxBandNo) {
            // Limit number of bands used to nMaxBandNo
            _nBands = nMaxBandNo;
        }

        if (_nBands == 0) {
            System.out.println(String.format("Skipping %s as it has no bands", dsFileName));
            return false;
        } else if (_nBands > 1 && bSeparate) {
            System.out.println(String.format("%s has %d bands. Only the first one will be taken into account in the -separate case",
                    dsFileName, _nBands));
            _nBands = 1;
        }

        psDatasetProperties.firstBandType = hDs.GetRasterBand(1).GetRasterDataType();

        psDatasetProperties.bHasDatasetMask = hDs.GetRasterBand(1).getMaskFlags(); //== GMF_PER_DATASET;
        if (psDatasetProperties.bHasDatasetMask)
            bHasDatasetMask = true;
        //TODO maybe link
        hDs.GetRasterBand(1).GetBlockSize(psDatasetProperties.nMaskBlockXSize, psDatasetProperties.nMaskBlockYSize);

        int j;
        for (j = 0; j < _nBands; j++) {
            if (nSrcDataCount > 0) {
                psDatasetProperties.pabHasNoData[j] = true;
                if (j < nSrcDataCount)
                    psDatasetProperties.padfNoDataValues[j] = padfSrcNoData[j];
                else
                    psDatasetProperties.padfNoDataValues[j] = padfSrcNoData[nSrcNoDataCount - 1];
            } else {
                boolean bHasNoData = false;

                //TODO maybe send link &bHasNoData
                //TODO was deleted because of null
                if (psDatasetProperties.padfNoDataValues == null){
                    psDatasetProperties.padfNoDataValues = new double[_nBands];
                }
                if (psDatasetProperties.pabHasNoData == null){
                    psDatasetProperties.pabHasNoData = new boolean[_nBands];
                }


                psDatasetProperties.padfNoDataValues[j] = hDs.GetRasterBand(j + 1).GetNoDataValue(bHasNoData);
                psDatasetProperties.pabHasNoData[j] = !bHasNoData;
            }

            boolean bHasOffset = false;
            //TODO link &bHasOffset

            //TODO for null values
            if (psDatasetProperties.padfOffset == null)
                psDatasetProperties.padfOffset = new double[_nBands];
            if (psDatasetProperties.pabHasOffset == null)
                psDatasetProperties.pabHasOffset = new boolean[_nBands];


            psDatasetProperties.padfOffset[j] = hDs.GetRasterBand(j + 1).getRasterOffset(bHasOffset);
            psDatasetProperties.pabHasOffset[j] = bHasOffset != true && psDatasetProperties.padfOffset[j] != 0.0;

            boolean bHasScale = false;
            //TODO link &bHasScale
            if (psDatasetProperties.padfScale == null)
                psDatasetProperties.padfScale = new double[_nBands];
            if (psDatasetProperties.pabHasScale == null)
                psDatasetProperties.pabHasScale = new boolean[_nBands];

            psDatasetProperties.padfScale[j] = hDs.GetRasterBand(j + 1).getRasterScale(bHasScale);
            psDatasetProperties.pabHasScale[j] = (!bHasScale) && psDatasetProperties.padfScale[j] != 1.0;
        }

        if (bFirst) {
            if (proj != null)
                pszProjectionRef = proj;
            if (!bUserExtent) {
                minX = ds_minX;
                minY = ds_minY;
                maxX = ds_maxX;
                maxY = ds_maxY;
            }

            //if not provided an explicit band list, take the one of the first dataset
            if (nBands == 0) {
                nBands = _nBands;
                //TODO clear panBandList
                panBandList = new int[nBands];
                for (j = 0; j < nBands; j++) {
                    panBandList[j] = j + 1;
                    if (nMaxBandNo < j + 1)
                        nMaxBandNo = j + 1;
                }
            }

            if (!bSeparate) {
                pasBandProperties = new BandProperty[nMaxBandNo];
                for (j = 0; j < nMaxBandNo; j++) {
                    GdalRasterBand hRasterBand = hDs.GetRasterBand(j + 1);
                    if (pasBandProperties == null)

                    pasBandProperties[j].colorInterpretation = hRasterBand.GetColorInterpretation();
                    pasBandProperties[j].dataType = hRasterBand.GetRasterDataType();

                    if (pasBandProperties[j].colorInterpretation == GDALColorInterp.GCI_PaletteIndex) {
                        pasBandProperties[j].colorTable = hRasterBand.GetRasterColorTable();
                        if (pasBandProperties[j].colorTable != null) {
                            //TODO clone this ??for what??
                            pasBandProperties[j].colorTable = pasBandProperties[j].colorTable;
                        }
                    } else
                        pasBandProperties[j].colorTable = null;

                    if (nVRTNoDataCount > 0) {
                        pasBandProperties[j].bHasNoData = true;
                        if (j < nVRTNoDataCount)
                            pasBandProperties[j].noDataValue = padfVRTNoData[j];
                        else
                            pasBandProperties[j].noDataValue = padfVRTNoData[nVRTNoDataCount - 1];
                    } else {
                        boolean bHasNoData = false;
                        //TODO &bHasNoData
                        pasBandProperties[j].noDataValue = ((GdalRasterBand) hRasterBand).GetNoDataValue(bHasNoData);
                        pasBandProperties[j].bHasNoData = !bHasNoData;
                    }

                    boolean bHasOffset = false;
                    //TODO &bHasOffset
                    pasBandProperties[j].dfOffset = hRasterBand.getRasterOffset(bHasOffset);
                    pasBandProperties[j].bHasOffset = !bHasOffset && pasBandProperties[j].dfScale != 1.0;

                    boolean bHasScale = false;
                    //TODO &bHasScale
                    pasBandProperties[j].dfScale = hRasterBand.getRasterScale(bHasScale);
                    pasBandProperties[j].bHasScale = !bHasScale && pasBandProperties[j].dfScale != 1.0;
                }
            }
        } else {
            if ((proj != null && pszProjectionRef == null) ||
                    (proj == null && pszProjectionRef != null) ||
                    (proj != null && pszProjectionRef != null && projAreEqual(proj, pszProjectionRef) == false)) {
                if (!bAllowProjectionDifference) {
                    String osExpected = getProjectionName(pszProjectionRef);
                    String osGot = getProjectionName(proj);

                    System.out.println(String.format("gdalbuildvrt does not support heterogeneous projection: expected %s, got %s. Skipping %s",
                            osExpected,
                            osGot,
                            dsFileName));
                    return false;
                }
            }

            if (!bSeparate) {
                if (nMaxBandNo > _nBands) {
                    System.out.println(String.format("gdalbuildvrt does not support heterogeneous band numbers: expected %d, got %d. Skipping %s",
                            _nBands, nMaxBandNo, dsFileName));

                    return false;
                }

                for (j = 0; j < nMaxBandNo; j++) {
                    GdalRasterBand hRasterBand = hDs.GetRasterBand(j + 1);
                    if (pasBandProperties[j].colorInterpretation != hRasterBand.GetColorInterpretation()) {
                        System.out.println(String.format("gdalbuildvrt does not support heterogeneous band color interpretation: expected %s, got %s. Skipping %s",
                                pasBandProperties[j].colorInterpretation.getColorInterpretationName(),
                                hRasterBand.GetColorInterpretation().getColorInterpretationName(),
                                dsFileName));

                        return false;
                    }

                    if (pasBandProperties[j].dataType != hRasterBand.GetRasterDataType()) {
                        System.out.println(String.format("gdalbuildvrt does not support heterogeneous band data type: expected %s, got %s. Skipping %s",
                                pasBandProperties[j].dataType.getDataTypeName(),
                                hRasterBand.GetRasterDataType().getDataTypeName(),
                                dsFileName));
                        return false;
                    }

                    if (pasBandProperties[j].colorTable != null) {
                        GDALColorTableH colorTable = hRasterBand.GetRasterColorTable();
                        int nRefColorEntryCount = pasBandProperties[j].colorTable.getColorEntryPoint();
                        if (colorTable == null || colorTable.getColorEntryPoint() != nRefColorEntryCount) {
                            System.out.println(String.format("gdalbuildvrt does not support rasters with different color tables (different number of color table entries). Skipping %s", dsFileName));
                            return false;
                        }

                        /* Check that the palette are the same too */
                        /* We just warn and still process the file. It is not a technical no-go, but the user */
                        /* should check that the end result is OK for him. */
                        for (int i = 0; i < nRefColorEntryCount; i++) {
                            GDALColorEntry psEntry = colorTable.getColorEntry(i);
                            GDALColorEntry psEntryRef = pasBandProperties[j].colorTable.getColorEntry(i);
                            if (psEntry.c1 != psEntryRef.c1 || psEntry.c2 != psEntryRef.c2 ||
                                    psEntry.c3 != psEntryRef.c3 || psEntry.c4 != psEntryRef.c4) {
                                boolean bFirstWarningPCT = true;
                                if (bFirstWarningPCT)
                                    System.out.println(String.format(
                                            "%s has different values than the first raster for some entries in the color table.\n" +
                                                    "The end result might produce weird colors.\n" +
                                                    "You're advised to pre-process your rasters with other tools, such as pct2rgb.py or gdal_translate -expand RGB\n" +
                                                    "to operate gdalbuildvrt on RGB rasters instead", dsFileName));
                                else
                                    System.out.println(String.format(
                                            "%s has different values than the first raster for some entries in the color table.",
                                            dsFileName));
                                bFirstWarningPCT = false;
                                break;
                            }
                        }
                    }

                    if (psDatasetProperties.pabHasOffset[j] != pasBandProperties[j].bHasOffset ||
                            (pasBandProperties[j].bHasOffset &&
                                    psDatasetProperties.padfOffset[j] != pasBandProperties[j].dfOffset)) {
                        System.out.println(String.format("gdalbuildvrt does not support heterogeneous band offset: expected (%d,%f), got (%d,%f). Skipping %s",
                                pasBandProperties[j].bHasOffset,
                                pasBandProperties[j].dfOffset,
                                psDatasetProperties.pabHasOffset[j],
                                psDatasetProperties.padfOffset[j],
                                dsFileName));

                        return false;
                    }

                    if (psDatasetProperties.pabHasScale[j] != pasBandProperties[j].bHasScale ||
                            (pasBandProperties[j].bHasScale &&
                                    psDatasetProperties.padfScale[j] != pasBandProperties[j].dfScale)) {
                        System.out.println(String.format("gdalbuildvrt does not support heterogeneous band scale: expected (%d,%f), got (%d,%f). Skipping %s",
                                pasBandProperties[j].bHasScale,
                                pasBandProperties[j].dfScale,
                                psDatasetProperties.pabHasScale[j],
                                psDatasetProperties.padfScale[j],
                                dsFileName));

                        return false;
                    }
                }
            }

            if (!bUserExtent) {
                if (ds_minX < minX) minX = ds_minX;
                if (ds_minY < minY) minY = ds_minY;
                if (ds_maxX > maxX) maxX = ds_maxX;
                if (ds_maxY > maxY) maxY = ds_maxY;
            }
        }

        if (resolutionStrategy == ResolutionStrategy.AVERAGE_RESOLUTION) {
            we_res += padfGeoTransform[GEOTRSFRM_WE_RES];
            ns_res += padfGeoTransform[GEOTRSFRM_NS_RES];
        } else if (resolutionStrategy == ResolutionStrategy.HIGHEST_RESOLUTION) {
            we_res = Math.min(we_res, padfGeoTransform[GEOTRSFRM_WE_RES]);
            //ns_res is negative, the highest resolution is the max value.
            ns_res = Math.max(ns_res, padfGeoTransform[GEOTRSFRM_NS_RES]);
        } else {
            we_res = Math.max(we_res, padfGeoTransform[GEOTRSFRM_WE_RES]);
            // ns_res is negative, the lowest resolution is the min value.
            ns_res = Math.min(ns_res, padfGeoTransform[GEOTRSFRM_NS_RES]);
        }

        return true;
    }

    private String getProjectionName(String pszProjectionRef) {
        return null;
    }

    private boolean projAreEqual(String proj, String pszProjectionRef) {
        return false;
    }

    int CSLCount(String[] papszStrList) {
        //TODO fix it
        if (papszStrList == null)
            return 0;

        return papszStrList.length;
    }

    public GdalDataset build() throws IOException {
        if (bHasRunBuild)
            return null;
        bHasRunBuild = true;

        //if( pfnProgress == nullptr )
        //    pfnProgress = GDALDummyProgress;

        bUserExtent = (minX != 0 || minY != 0 || maxX != 0 || maxY != 0);
        if (bUserExtent) {
            if (minX >= maxX || minY >= maxY) {
                System.out.println("Invalid user extent");
                return null;
            }
        }

        if (resolutionStrategy == ResolutionStrategy.USER_RESOLUTION) {
            if (we_res <= 0 || ns_res <= 0) {
                System.out.println("Invalid user resolution");
                return null;
            }

            /* We work with negative north-south resolution in all the following code */
            ns_res = -ns_res;
        } else {
            we_res = ns_res = 0;
        }


        pasDatasetProperties = new DatasetProperty[nInputFiles];
        for(int i = 0; i < nInputFiles; i++){
            pasDatasetProperties[i] = new DatasetProperty();
        }

        if (pszSrcNoData != null) {
            if (pszSrcNoData.equals("none")) {
                bAllowSrcNoData = false;
            } else {
                String[] papszTokens = tokenizeString(pszSrcNoData);
                nSrcNoDataCount = CSLCount(papszTokens);
                padfSrcNoData = new double[nSrcNoDataCount];

                for (int i = 0; i < nSrcNoDataCount; i++) {
                    if (!argIsNumeric(papszTokens[i]) &&
                            !papszTokens[i].equals("nan") &&
                            !papszTokens[i].equals("-inf") &&
                            !papszTokens[i].equals("inf")) {
                        System.out.println("Invalid -srcnodata value");

                        return null;
                    }

                    padfSrcNoData[i] = CPLAtofM(papszTokens[i]);
                }
            }
        }

        if (pszVRTNoData != null) {
            if (pszVRTNoData.equals("none")) {
                bAllowVRTNoData = false;
            } else {
                String[] papszTokens = tokenizeString(pszVRTNoData);
                nVRTNoDataCount = CSLCount(papszTokens);
                padfVRTNoData = new double[nVRTNoDataCount];

                for (int i = 0; i < nVRTNoDataCount; i++) {
                    if (!argIsNumeric(papszTokens[i]) &&
                            !papszTokens[i].equals("nan") &&
                            !papszTokens[i].equals("-inf") &&
                            !papszTokens[i].equals("inf")) {
                        System.out.println("Invalid -vrtnodata value");

                        return null;
                    }
                    padfVRTNoData[i] = CPLAtofM(papszTokens[i]);
                }
            }
        }

        int nCountValid = 0;
        for (int i = 0; ppszInputFilenames != null && i < nInputFiles; i++) {
            String dsFileName = ppszInputFilenames[i];

            //if (!pfnProgress( 1.0 * (i+1) / nInputFiles, nullptr, pProgressData))
            //{                return nullptr;            }

            GdalDataset hDS = (pahSrcDS != null) ?
                    pahSrcDS[i] : gdalOpenEx(ppszInputFilenames[i],
                    GDAL_OF_RASTER | GDAL_OF_VERBOSE_ERROR, null,
                    papszOpenOptions, null);

            //todo removed because of null
            pasDatasetProperties[i].setIsFileOK(false);

            //TODO
            if (hDS != null) {
                //TODO skip analyze while test
                bHasGeoTransform = true;
                if (analyzeRaster(hDS, pasDatasetProperties[i])) {
                    pasDatasetProperties[i].setIsFileOK(true);
                    nCountValid++;
                    bFirst = false;
                }

                if (pahSrcDS == null)
                    gdalClose(hDS);
            } else {
                System.out.println(String.format("Can't open %s. Skipping it", dsFileName));
            }
        }

        if (nCountValid == 0)
            return null;

        if (bHasGeoTransform) {
            if (resolutionStrategy == ResolutionStrategy.AVERAGE_RESOLUTION) {
                we_res /= nCountValid;
                ns_res /= nCountValid;
            }

            if (bTargetAlignedPixels) {
                minX = Math.floor(minX / we_res) * we_res;
                maxX = Math.ceil(maxX / we_res) * we_res;
                minY = Math.floor(minY / -ns_res) * -ns_res;
                maxY = Math.ceil(maxY / -ns_res) * -ns_res;
            }

            nRasterXSize = (int) (0.5 + (maxX - minX) / we_res);
            nRasterYSize = (int) (0.5 + (maxY - minY) / -ns_res);
        }

        //TODO delete because nRasterXSize = nRasterYSize == 0
        if (nRasterXSize == 0 && nRasterYSize == 0) {
            System.out.println("Computed VRT dimension is invalid. You've probably specified inappropriate resolution.");
            return null;
        }

        VrtDataset hVRTDs = VRTCreate(nRasterXSize, nRasterYSize);
        hVRTDs.SetDescription(pszOutputFilename);

        if (pszOutputSRS != null) {
            hVRTDs.SetProjection(pszOutputSRS);
        } else if (pszProjectionRef != null){
            //TODO
            //hVRTDs.SetProjection(pszProjectionRef);
            hVRTDs.SetProjection("testprojection");
        }

        if (bHasGeoTransform)
        {
            double[] adfGeoTransform = new double[6];
            adfGeoTransform[GEOTRSFRM_TOPLEFT_X] = minX;
            adfGeoTransform[GEOTRSFRM_WE_RES] = we_res;
            adfGeoTransform[GEOTRSFRM_ROTATION_PARAM1] = 0;
            adfGeoTransform[GEOTRSFRM_TOPLEFT_Y] = maxY;
            adfGeoTransform[GEOTRSFRM_ROTATION_PARAM2] = 0;
            adfGeoTransform[GEOTRSFRM_NS_RES] = ns_res;
            hVRTDs.SetGeoTransform(adfGeoTransform);
        }

        if (bSeparate)
            createVrtSeparate(hVRTDs);
        else
            createVrtNonSeparate(hVRTDs);

        return (GdalDataset) hVRTDs;
    }



    private VrtDataset VRTCreate(int nRasterXSize, int nRasterYSize) {
        VrtDataset poDS = new VrtDataset(nRasterXSize, nRasterYSize);
        poDS.eAccess = GdalAccess.GA_Update;
        return poDS;
    }

    private void gdalClose(GdalDataset hDS) {
        //i guess doesnt make sense in java
    }

    private boolean argIsNumeric(String pszArg) {
        return cplGetValueType(pszArg) != CPLValueType.CPL_VALUE_STRING;
    }

    private CPLValueType cplGetValueType(String pszArg) {
        try{
            Double.parseDouble(pszArg);

            return CPLValueType.CPL_VALUE_REAL;
        }catch (Exception ex){}

        try{
            Long.parseLong(pszArg);

            return CPLValueType.CPL_VALUE_INTEGER;
        }catch (Exception ex){}

        return CPLValueType.CPL_VALUE_STRING;
    }

    /* -------------------------------------------------------------------- */
    /*      Convert number to string.  This function is locale agnostic     */
    /*      (i.e. it will support "," or "." regardless of current locale)  */
    /* -------------------------------------------------------------------- */
    private double CPLAtofM(String papszToken) {
        return Double.parseDouble(papszToken);
    }

/**
 * Tokenize a string.
 *
 * This function will split a string into tokens based on specified'
 * delimiter(s) with a variety of options.  The returned result is a
 * string list that should be freed with CSLDestroy() when no longer
 * needed.
 */
    private String[] tokenizeString(String pszSrcNoData) {
        return pszSrcNoData.split("(?<=\") *(?=\")");
    }

    public GdalDataset gdalBuildVRT(String pszDest,
                                     int nSrcCount,
                                    GdalDataset[] pahSrcDS,
                                     String[] papszSrcDSNames,
                                     GdalBuildVrtOptions psOptionsIn) throws IOException {
        if( pszDest == null )
            pszDest = "";

        if( nSrcCount == 0 )
        {
            System.out.println("No input dataset specified.");

            return null;
        }

        GdalBuildVrtOptions psOptions =
                (psOptionsIn != null) ? psOptionsIn :
                        new GdalBuildVrtOptions(null, null);

        if (psOptions.we_res != 0 && psOptions.ns_res != 0 &&
                psOptions.pszResolution != null && !psOptions.pszResolution.equals("user"))
        {
            System.out.println(String.format("-tr option is not compatible with -resolution %s", psOptions.pszResolution));
            return null;
        }

        if (psOptions.bTargetAlignedPixels && psOptions.we_res == 0.0 && psOptions.ns_res == 0)
        {
            System.out.println("-tap option cannot be used without using -tr");
            return null;
        }

        if (psOptions.bAddAlpha && psOptions.bSeparate)
        {
            System.out.println("-addalpha option is not compatible with -separate.");
            return null;
        }

        ResolutionStrategy eStrategy = ResolutionStrategy.AVERAGE_RESOLUTION;
        if ( psOptions.pszResolution == null || psOptions.pszResolution.equals("user"))
        {
            if ( psOptions.we_res != 0 || psOptions.ns_res != 0)
                eStrategy = ResolutionStrategy.USER_RESOLUTION;
            else if ( psOptions.pszResolution != null && psOptions.pszResolution.equals("user") )
            {
                System.out.println("-tr option must be used with -resolution user.");
                return null;
            }
        }
        else if ( psOptions.pszResolution.equals("average") )
            eStrategy = ResolutionStrategy.AVERAGE_RESOLUTION;
        else if ( psOptions.pszResolution.equals("highest") )
            eStrategy = ResolutionStrategy.HIGHEST_RESOLUTION;
        else if ( psOptions.pszResolution.equals("lowest") )
            eStrategy = ResolutionStrategy.LOWEST_RESOLUTION;

        /* If -srcnodata is specified, use it as the -vrtnodata if the latter is not */
        /* specified */
        if (psOptions.pszSrcNoData != null && psOptions.pszVRTNoData == null)
            psOptions.pszVRTNoData = psOptions.pszSrcNoData;

        VrtBuilder oBuilder = new VrtBuilder(pszDest, nSrcCount, papszSrcDSNames, pahSrcDS,
                            psOptions.panBandList, psOptions.nBandCount, psOptions.nMaxBandNo,
                eStrategy, psOptions.we_res, psOptions.ns_res, psOptions.bTargetAlignedPixels,
                psOptions.xmin, psOptions.ymin, psOptions.xmax, psOptions.ymax,
                psOptions.bSeparate, psOptions.bAllowProjectionDifference,
                psOptions.bAddAlpha, psOptions.bHideNoData, psOptions.nSubdataset,
                psOptions.pszSrcNoData, psOptions.pszVRTNoData,
                psOptions.pszOutputSRS, psOptions.pszResampling,
                psOptions.papszOpenOptions);

        GdalDataset hDstDS = oBuilder.build();

        //GDALBuildVRTOptionsFree(psOptions);

        return hDstDS;
    }





    public GdalDataset gdalOpenEx(String pszFilename,
                                  int nOpenFlags,
                                  String papszAllowedDrivers,
                                  String[] papszOpenOptions,
                                  String[] papszSiblingFiles) throws IOException {
        GdalDataset poDs = new GdalDataset();

        /*TiffFileReader tiffFileReader = new TiffFileReader();
        IIOMetadata iioMetadata = tiffFileReader.extractMetadataFromTiffFile(pszFilename);
        Map<String, Object> headerMap = new HashMap<>();

        String[] names = iioMetadata.getMetadataFormatNames();
        for (int i = 0; i < names.length; i++) {
            Node asTree = iioMetadata.getAsTree(names[i]);


            headerMap.put(names[i], asTree);
        }*/


        BufferedImage image = ImageIO.read(Paths.get(pszFilename).toFile());



        //TIFFImage tiffImage = TiffReader.readTiff(Paths.get(pszFilename).toFile());

        /*Map<String, Object> headersMap = new HashMap<>();
        for (FileDirectory fileDirectory : tiffImage.getFileDirectories()) {
            for (FileDirectoryEntry directoryEntry : fileDirectory.getEntries()) {
                headersMap.put(directoryEntry.getFieldTag().name(), directoryEntry.getValues());
            }
        }*/

        poDs.nRasterXSize = image.getWidth();
        poDs.nRasterYSize = image.getHeight();

        WritableRaster raster = image.getRaster();
        poDs.nBands = raster.getNumBands();

        //TODO addBand
        poDs.papoBands = new GdalRasterBand[raster.getNumBands()];
        for (int i = 0; i < raster.getNumBands(); i++) {
            poDs.papoBands[i] = new GdalRasterBand();
        }

        if  (poDs.nRasterXSize <= 0 || poDs.nRasterYSize <= 0 )
        {
            throw new RuntimeException("Raster size cant be less then zero");
        }

        //foreach Band
        //poDs.AddBand();

        image.flush();
        image = null;

        return poDs;
    }
}
