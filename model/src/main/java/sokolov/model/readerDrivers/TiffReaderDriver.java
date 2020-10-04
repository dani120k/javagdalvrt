package sokolov.model.readerDrivers;

import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;

import java.io.File;
import java.io.IOException;

public class TiffReaderDriver {
    public static void main(String[] args) throws IOException {
        open();
    }

    public static void open() throws IOException {
        //GdalDataset gdalDataset = new GdalDataset();

        TIFFImage tiffImage = TiffReader.readTiff(new File("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\EO1A0880642007232110P1_B07_L1T.TIF"));


        System.out.println();
    }
}
