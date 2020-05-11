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

        TIFFImage tiffImage = TiffReader.readTiff(new File("/home/dani120k/Downloads/setsetset/NEON-DS-Airborne-Remote-Sensing/HARV/RGB_Imagery/HARV_Ortho_wNA.tif"));


        System.out.println();
    }
}
