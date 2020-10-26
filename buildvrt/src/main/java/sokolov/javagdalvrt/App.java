package sokolov.javagdalvrt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.alghorithms.ResamplingAlghorithmExecutor;
import sokolov.model.alghorithms.maskimplementation.MaskExecutor;
import sokolov.model.alghorithms.pansharpening.PansharpeningAlghorithm;
import sokolov.model.alghorithms.warping.WapredAlghorithm;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.datasets.VrtDataset;
import sokolov.model.datasets.VrtPansharpenedDataset;
import sokolov.model.datasets.VrtWarpedDataset;
import sokolov.model.enums.GdalAccess;
import sokolov.model.xmlmodel.VRTDataset;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {


        System.out.println();

        /*VrtBuilder vrtBuilder = new VrtBuilder("test.tiff",
                2,
                new String[]{"C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\EO1A0880642007232110P1_B07_L1T.TIF", "C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\clearcuts_174016_20101018_clip.tif"},
                new GdalDataset[]{new GdalDataset(0)},
                new int[]{1, 2, 3},
                3,
                0,
                ResolutionStrategy.AVERAGE_RESOLUTION,
                0.0,
                0.0,
                true,
                0.0,
                0.0,
                0.0,
                0.0,
                true,
                true,
                false,
                true,
                1,
                null,
                null,
                "pszoutput",
                "pszresampling",
                new String[]{"option1"});
        //vrtBuilder.build();*/


        /*VrtDataset gdalDataset = (VrtDataset)new VrtBuilder().gdalBuildVRT(
                "C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder",
                2,
                null,
                new String[]{"C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\TrueMarble.250m.21600x21600.E1.tif", "C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\TrueMarble.250m.21600x21600.E2.tif"},
                new GdalBuildVrtOptions(0, 0)
        );*/

        System.out.println();

        //VRTDataset vrtDataset = gdalDataset.SerializeToXML(pszVRTPathIn);

        //VRTDataset vrtDataset = serializeToVrtDataset(gdalDataset);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);/*
        String value = xmlMapper.writeValueAsString(vrtDataset);

        Path pathToVrt = Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt");

        Files.deleteIfExists(pathToVrt);

        Files.write(pathToVrt,
                value.getBytes(),
                StandardOpenOption.CREATE_NEW);*/

        String pszVRTPathIn = "C:\\Users\\forol\\IdeaProjects\\javagdalvrt";
        Path pathToXml = Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt", "pansharpening.vrt");
        byte[] bytes = Files.readAllBytes(pathToXml);

        VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);
        deserializedVrtDataset.setPathToFile(pathToXml.toString());

        //GdalDataset resultedDataset = extractFromVRTXml(deserializedVrtDataset, Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt").toString());

        GdalDataset gdalDataset = new GdalDataset();

        if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTPansharpenedDataset")){
            PansharpeningAlghorithm pansharpeningAlghorithm = new PansharpeningAlghorithm();
            pansharpeningAlghorithm.executePansharpening(gdalDataset, pszVRTPathIn, deserializedVrtDataset);
        } else if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTWarpedDataset")) {
            WapredAlghorithm wapredAlghorithm = new WapredAlghorithm();
            //wapredAlghorithm.executeWarping(gdalDataset);
        } else {
            gdalDataset.InitXml(deserializedVrtDataset);
        }



        /*System.out.println();

        BufferedImage read = ImageIO.read(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\MOS_CZ_KR_250.tif").toFile());

        ResamplingAlghorithmExecutor resamplingAlghorithmExecutor = new ResamplingAlghorithmExecutor();

        int nRasterXSize = read.getRaster().getWidth() / 4;
        int nRasterYSize = read.getRaster().getHeight() / 4;


        byte[][] pixels = new byte[3][nRasterXSize * nRasterYSize];
        DataBuffer dataBuffer = new DataBufferByte(pixels, nRasterXSize * nRasterYSize, new int[]{0, 1, 2});


        ColorModel colorModel = createColorModel(0);
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(nRasterXSize, nRasterYSize);

        WritableRaster raster = colorModel.createCompatibleWritableRaster(
                nRasterXSize, nRasterYSize);


        BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, null);


        //BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_CUSTOM);
        //WritableRaster interleavedRaster = Raster.create(DataBuffer.TYPE_BYTE, nRasterXSize, nRasterYSize, 3, null);
        WritableRaster interleavedRaster = colorModel.createCompatibleWritableRaster(nRasterXSize, nRasterYSize);
        SampleModel model = interleavedRaster.getSampleModel();

        for (int band = 0; band < 3; band++) {
            byte[] nearests = resamplingAlghorithmExecutor.imageRescaling(band + 1,
                    0, 0, read.getRaster().getWidth(), read.getRaster().getHeight(),
                    0, 0, nRasterXSize, nRasterYSize,
                    -100000,
                    read,
                    "bilinear");

            for (int i = 0; i < nearests.length; i++) {
                int x = i % nRasterXSize;
                int y = i / nRasterXSize;

                int value = 0x00420420 ^ 0x00ff0000;

                model.setSample(x, y, band, (band == 0) ? nearests[i] ^ value : nearests[i], interleavedRaster.getDataBuffer());
            }
        }

        bufferedImage.getRaster().setRect(interleavedRaster);

        ImageIO.write(bufferedImage, "tiff", new File(String.format("resultafterbilinearresampling.tiff")));


        byte[] maskBand = new byte[9];

        MaskExecutor maskExecutor = new MaskExecutor();

        maskExecutor.executeMask(bufferedImage.getRaster(),
                maskBand,
                3,
                3,
                3
        );

        ImageIO.write(bufferedImage, "tiff", new File(String.format("resultaftermask.tiff")));

        check();*/
    }

   /* public static void check() throws IOException {
        BufferedImage read = ImageIO.read(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\MOS_CZ_KR_250.tif").toFile());

        ResamplingAlghorithmExecutor resamplingAlghorithmExecutor = new ResamplingAlghorithmExecutor();

        int nRasterXSize = read.getRaster().getWidth() * 4;
        int nRasterYSize = read.getRaster().getHeight() * 4;


        byte[][] pixels = new byte[3][nRasterXSize * nRasterYSize];
        DataBuffer dataBuffer = new DataBufferByte(pixels, nRasterXSize * nRasterYSize, new int[]{0, 1, 2});


        ColorModel colorModel = createColorModel(0);
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(nRasterXSize, nRasterYSize);

        WritableRaster raster = colorModel.createCompatibleWritableRaster(
                nRasterXSize, nRasterYSize);


        BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, null);


        //BufferedImage bufferedImage = new BufferedImage(nRasterXSize, nRasterYSize, BufferedImage.TYPE_CUSTOM);
        //WritableRaster interleavedRaster = Raster.create(DataBuffer.TYPE_BYTE, nRasterXSize, nRasterYSize, 3, null);
        WritableRaster interleavedRaster = colorModel.createCompatibleWritableRaster(nRasterXSize, nRasterYSize);
        SampleModel model = interleavedRaster.getSampleModel();

        for (int band = 0; band < 3; band++) {
            byte[] nearests = resamplingAlghorithmExecutor.imageRescaling(band + 1,
                    0, 0, read.getRaster().getWidth(), read.getRaster().getHeight(),
                    0, 0, nRasterXSize, nRasterYSize,
                    -100000,
                    read,
                    "bilinear");

            for (int i = 0; i < nearests.length; i++) {
                int x = i % nRasterXSize;
                int y = i / nRasterXSize;

                model.setSample(x, y, band, nearests[i], interleavedRaster.getDataBuffer());
            }
        }

        bufferedImage.getRaster().setRect(interleavedRaster);

        ImageIO.write(bufferedImage, "tiff", new File(String.format("resultafterchangecolor.tiff")));


    }*/

    private static ColorModel createColorModel(int n) {
        return new DirectColorModel(24,
                0x00ff0000,       // Red
                0x0000ff00,       // Green
                0x000000ff       // Bl// Alpha
        );
    }

    private static GdalDataset extractFromVRTXml(VRTDataset deserializedVrtDataset, String path) {
        String subClass = deserializedVrtDataset.getSubClass();

        boolean bIsPansharpened = "VRTPansharpenedDataset".equals(subClass);

        if (!bIsPansharpened &&
                deserializedVrtDataset.getGroup() == null &&
                deserializedVrtDataset.getRasterXSize() == null &&
                deserializedVrtDataset.getRasterYSize() == null &&
                deserializedVrtDataset.getVrtRasterBand() == null) {
            throw new RuntimeException("Missing one of rasterXSize, rasterYSize or bands on VRTDataset");
        }

        /* -------------------------------------------------------------------- */
        /*      Create the new virtual dataset object.                          */
        /* -------------------------------------------------------------------- */
        int nXSize = deserializedVrtDataset.getRasterXSize();
        int nYSize = deserializedVrtDataset.getRasterYSize();

        //if (!bIsPansharpened &&
        //deserializedVrtDataset.getVrtRasterBand() != null){// &&
        //TODO !GdalCheckDimenstions(nXSize, nYSize)){
        //    return null;
        //}

        VrtDataset poDS = null;

        if (subClass.equals("VRTWarpedDataset")) {
            poDS = new VrtWarpedDataset();
        } else if (bIsPansharpened) {
            poDS = new VrtPansharpenedDataset(nXSize, nYSize);
        } else {
            poDS = new VrtDataset(nXSize, nYSize);
            poDS.eAccess = GdalAccess.GA_ReadOnly;
        }

        poDS.initDataset(deserializedVrtDataset, path);

        return poDS;
    }


    public static VRTDataset serializeToVrtDataset(GdalDataset gdalDataset) {
        /*VRTDataset vrtDataset = new VRTDataset();

        vrtDataset.setBlockXSize(128L);
        vrtDataset.setBlockYSize(128L);

        vrtDataset.setSubClass("subclass");
        vrtDataset.setSrs(new SRSType());
        vrtDataset.setRasterYSize(gdalDataset.nRasterXSize);
        vrtDataset.setRasterXSize(gdalDataset.nRasterYSize);
        vrtDataset.setPansharpeningOptions(new PansharpeningOptionsType());
        vrtDataset.setOverviewList(new OverviewListType());
        vrtDataset.setMetadata(new MetadataType());
        vrtDataset.setMaskBand(new MaskBandType());
        vrtDataset.setGroup(new GroupType());

        StringBuilder geoTransform = new StringBuilder();
        geoTransform.append("0.0, 1.0, 0.0, 0.0, 1.0, 0.0");
        vrtDataset.setGeoTransform(geoTransform.toString());
        vrtDataset.setGDALWarpOptions(new GDALWarpOptionsType());
        vrtDataset.setGcpList(new GCPListType());

        int rasterCount = gdalDataset.getRasterCount();
        for (int i = 0; i < rasterCount; i++) {
            GdalRasterBand gdalRasterBand = gdalDataset.GetRasterBand(i + 1);

            VRTRasterBandType vrtRasterBandType = new VRTRasterBandType();
            vrtRasterBandType.setSubClass(VRTRasterBandSubClassType.VRTPansharpenedRasterBand);
            System.out.println();
        }



        vrtDataset.setVrtRasterBand(vrtRasterBandType);

        return vrtDataset;*/
        return null;
    }
}
