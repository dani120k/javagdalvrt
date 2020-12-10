package sokolov.javagdalvrt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.geotools.referencing.operation.matrix.AffineTransform2D;
import org.geotools.referencing.operation.matrix.Matrix3;
import sokolov.model.alghorithms.VrtOpener;
import sokolov.model.alghorithms.pansharpening.PansharpeningAlghorithm;
import sokolov.model.alghorithms.warping.WapredAlghorithm;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.datasets.VrtDataset;
import sokolov.model.datasets.VrtPansharpenedDataset;
import sokolov.model.datasets.VrtWarpedDataset;
import sokolov.model.enums.GdalAccess;
import sokolov.model.xmlmodel.VRTDataset;

import javax.imageio.ImageIO;
import javax.media.jai.WarpAffine;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException, NoninvertibleTransformException {
        //VRTReader vrtReader = new VRTReader(Paths.get("/Users/danilsokolov/IdeaProjects/javagdalvrt/test_mosaic.vrt").toFile());

        //AffineTransform affine = new AffineTransform2D(new Matrix3(-907000, 250, 0, -933000, 0, -250, 0, 0, 1));
        //AffineTransform inverse = affine.createInverse();

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
        //Path pathToXml = Paths.get(pszVRTPathIn,"pansharpening.vrt");

        BufferedImage bufferedImage = VrtOpener.openVrt(pszVRTPathIn, "vrtfromvrt.vrt");

        ImageIO.write(bufferedImage, "tif", new File("res.tif"));

        /*byte[] bytes = Files.readAllBytes(pathToXml);

        VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);
        deserializedVrtDataset.setPathToFile(pathToXml.toString());

        GdalDataset gdalDataset = new GdalDataset();

        if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTPansharpenedDataset")){
            PansharpeningAlghorithm pansharpeningAlghorithm = new PansharpeningAlghorithm();
            pansharpeningAlghorithm.executePansharpening(gdalDataset, pszVRTPathIn, deserializedVrtDataset);
        } else if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTWarpedDataset")) {
            WapredAlghorithm wapredAlghorithm = new WapredAlghorithm();
            wapredAlghorithm.executeWarping(gdalDataset, pszVRTPathIn, deserializedVrtDataset);
        } else {
            gdalDataset.InitXml(deserializedVrtDataset);

            ImageIO.write(gdalDataset.bufferedImage, "tiff", new File(String.format("resterf.tiff")));
        }*/
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
}
