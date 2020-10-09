package sokolov.javagdalvrt;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.enums.ResolutionStrategy;
import sokolov.model.xmlmodel.*;

import javax.imageio.metadata.IIOMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class App {
    public static void main(String[] args) throws IOException {
        TiffFileReader tiffFileReader = new TiffFileReader();
        //IIOMetadata iioMetadata = tiffFileReader.extractMetadataFromTiffFile("");

        System.out.println();

        VrtBuilder vrtBuilder = new VrtBuilder("test.tiff",
                1,
                new String[]{"C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\NE1_HR_LC.tif"},
                new GdalDataset[]{new GdalDataset(0)},
                new int[]{1, 2, 3},
                3,
                0,
                ResolutionStrategy.AVERAGE_RESOLUTION,
                0.0,
                0.0,
                false,
                0.0,
                0.0,
                100.0,
                100.0,
                false,
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

        GdalDataset gdalDataset = vrtBuilder.gdalBuildVRT(
                "C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder",
                1,
                new GdalDataset[]{},
                new String[]{"C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\EO1A0880642007232110P1_B07_L1T.TIF"},
                new GdalBuildVrtOptions(0, 0)
        );

        System.out.println();

        VRTDataset vrtDataset = serializeToVrtDataset(gdalDataset);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String value = xmlMapper.writeValueAsString(vrtDataset);

        Files.write(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt"),
                value.getBytes(),
                StandardOpenOption.CREATE_NEW);

        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt"));

        VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);

        extractFromVRTXml(deserializedVrtDataset);

        System.out.println();
    }

    private static GdalDataset extractFromVRTXml(VRTDataset deserializedVrtDataset) {

    }

    public static VRTDataset serializeToVrtDataset(GdalDataset gdalDataset){
        VRTDataset vrtDataset = new VRTDataset();

        vrtDataset.setBlockXSize(128L);
        vrtDataset.setBlockYSize(128L);
        vrtDataset.setVrtRasterBand(new VRTRasterBandType());
        vrtDataset.setSubClass("subclass");
        vrtDataset.setSrsType(new SRSType());
        vrtDataset.setRasterYSize(gdalDataset.nRasterXSize);
        vrtDataset.setRasterXSize(gdalDataset.nRasterYSize);
        vrtDataset.setPansharpeningOptions(new PansharpeningOptionsType());
        vrtDataset.setOverviewList(new OverviewListType());
        vrtDataset.setMetadata(new MetadataType());
        vrtDataset.setMaskBand(new MaskBandType());
        vrtDataset.setGroup(new GroupType());

        StringBuilder geoTransform = new StringBuilder();
        vrtDataset.setGeoTransform(geoTransform.toString());
        vrtDataset.setGDALWarpOptions(new GDALWarpOptionsType());
        vrtDataset.setGcpListType(new GCPListType());

        return vrtDataset;
    }
}
