package sokolov.javagdalvrt;

import sokolov.model.datasets.GdalDataset;
import sokolov.model.enums.ResolutionStrategy;

import javax.imageio.metadata.IIOMetadata;
import java.io.IOException;

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
                new String[]{"C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\TrueMarble.250m.21600x21600.E1.tif"},
                new GdalBuildVrtOptions(0, 0)
        );
    }
}
