package sokolov.model.alghorithms;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOpenService {
    public static BufferedImage openFile(String pszVrtPathIn, String fileName) throws IOException {
        Path sourceFileName = Paths.get(pszVrtPathIn, fileName);

        String[] split = sourceFileName.toString().split("\\.");

        if (split[split.length - 1].equals("vrt")){
            return VrtOpener.openVrt(pszVrtPathIn, fileName);
        } else {
            File file = sourceFileName.toFile();
            if (!file.exists()) {
                throw new RuntimeException(String.format("Отсутствует файл-источник"));
            }

            return ImageIO.read(file);
        }
    }
}
