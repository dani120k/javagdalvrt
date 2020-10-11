package sokolov.javagdalvrt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.datasets.*;
import sokolov.model.enums.GdalAccess;
import sokolov.model.enums.ResolutionStrategy;
import sokolov.model.xmlmodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class App {
    public static void main(String[] args) throws IOException {
        //TiffFileReader tiffFileReader = new TiffFileReader();
        //IIOMetadata iioMetadata = tiffFileReader.extractMetadataFromTiffFile("");

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
        String pszVRTPathIn = null;

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

        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt", "test_mosaic.vrt"));

        VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);

        //GdalDataset resultedDataset = extractFromVRTXml(deserializedVrtDataset, Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt").toString());

        GdalDataset gdalDataset = new GdalDataset();
        gdalDataset.InitXml(deserializedVrtDataset);

        System.out.println();
    }

    private static GdalDataset extractFromVRTXml(VRTDataset deserializedVrtDataset, String path) {
        String subClass = deserializedVrtDataset.getSubClass();

        boolean bIsPansharpened = "VRTPansharpenedDataset".equals(subClass);

        if (!bIsPansharpened &&
        deserializedVrtDataset.getGroup() == null &&
        deserializedVrtDataset.getRasterXSize() == null &&
        deserializedVrtDataset.getRasterYSize() == null &&
        deserializedVrtDataset.getVrtRasterBand() == null){
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

        if (subClass.equals("VRTWarpedDataset")){
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



    public static VRTDataset serializeToVrtDataset(GdalDataset gdalDataset){
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
