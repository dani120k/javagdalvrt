package sokolov.javagdalvrt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

public class demoUI extends JPanel{


    public demoUI() throws IOException {
    }


    public static void main(String[] args) throws IOException {
        //создаем окно
        JFrame fr = new JFrame();
        fr.setSize(1920, 1080);
        fr.setVisible(true);
        fr.add(new demoUI());
        fr.setResizable(false);
    }

    public void paint(Graphics g){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\pansharped.tiff").toFile());

            g.drawImage(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight(), null);//выводим линию
        } catch (IOException e) {
            e.printStackTrace();
        }





    }
}