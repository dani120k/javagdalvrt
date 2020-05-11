package sokolov.javagdalvrt;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Iterator;

public class TiffFileReader {
    private IIOMetadata extractMetadataFromTiffFile(String pathToFile) throws IOException {
        pathToFile = "/home/dani120k/Downloads/javagdalvrt/EO1A0880642007232110P1_B07_L1T.TIF";
        File file = new File(pathToFile);
        ImageInputStream iis = ImageIO.createImageInputStream(
                new BufferedInputStream(
                        new FileInputStream(file)));
        Iterator<ImageReader> readers =
                ImageIO.getImageReadersByFormatName("tif");
        IIOImage image = null;
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(iis, true);
            try {
                image = reader.readAll(0, null);
            } catch (javax.imageio.IIOException iioex) {}

            IIOMetadata metadata = image.getMetadata();
            String[] names = metadata.getMetadataFormatNames();
            for (int i = 0; i < names.length; i++) {
                System.out.println("Format name: " + names[ i]);
                System.out.println(metadata.getAsTree(names[i]));
            }

            return metadata;
        }

        return null;
    }


}
