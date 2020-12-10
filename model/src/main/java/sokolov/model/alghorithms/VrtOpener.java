package sokolov.model.alghorithms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.alghorithms.pansharpening.PansharpeningAlghorithm;
import sokolov.model.alghorithms.warping.WapredAlghorithm;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.xmlmodel.VRTDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VrtOpener {
    private static XmlMapper xmlMapper;

    static {
        xmlMapper = new XmlMapper();
        xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static BufferedImage openVrt(String pszVRTPathIn, String vrtFileName) throws IOException {
        Path pathToXml = Paths.get(pszVRTPathIn,vrtFileName);
        byte[] bytes = Files.readAllBytes(pathToXml);

        VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);
        deserializedVrtDataset.setPathToFile(pathToXml.toString());

        GdalDataset gdalDataset = new GdalDataset();

        if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTPansharpenedDataset")){
            PansharpeningAlghorithm pansharpeningAlghorithm = new PansharpeningAlghorithm();
            pansharpeningAlghorithm.executePansharpening(gdalDataset, pszVRTPathIn, deserializedVrtDataset);

            return gdalDataset.bufferedImage;
        } else if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTWarpedDataset")) {
            WapredAlghorithm wapredAlghorithm = new WapredAlghorithm();
            wapredAlghorithm.executeWarping(gdalDataset, pszVRTPathIn, deserializedVrtDataset);

            return gdalDataset.bufferedImage;
        } else {
            gdalDataset.InitXml(deserializedVrtDataset);

            //ImageIO.write(gdalDataset.bufferedImage, "tiff", new File(String.format("resterf.tiff")));

            return gdalDataset.bufferedImage;
        }
    }
}
