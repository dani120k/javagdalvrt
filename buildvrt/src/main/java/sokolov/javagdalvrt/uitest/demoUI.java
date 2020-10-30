package sokolov.javagdalvrt.uitest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sokolov.model.alghorithms.pansharpening.PansharpeningAlghorithm;
import sokolov.model.alghorithms.warping.WapredAlghorithm;
import sokolov.model.datasets.GdalDataset;
import sokolov.model.xmlmodel.VRTDataset;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class demoUI extends JFrame{
    private static String pathToFile;

    private static BufferedImage bufferedImage;

    public demoUI() throws IOException {


        super("Системное меню");
        System.out.println("demoui");
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        // Создание строки главного меню
        JMenuBar menuBar = new JMenuBar();
        // Добавление в главное меню выпадающих пунктов меню
        menuBar.add(createFileMenu());
        // Подключаем меню к интерфейсу приложения
        setJMenuBar(menuBar);
        // Открытие окна
        setSize(1800, 900);
        setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new demoUI();
    }

    private JMenu createFileMenu()
    {
        // Создание выпадающего меню
        JMenu file = new JMenu("Файл");
        // Пункт меню "Открыть" с изображением
        JMenuItem open = new JMenuItem("Открыть",
                new ImageIcon("images/open.png"));
        // Пункт меню из команды с выходом из программы
        JMenuItem exit = new JMenuItem(new ExitAction());
        // Добавление к пункту меню изображения
        exit.setIcon(new ImageIcon("images/exit.png"));
        // Добавим в меню пункта open
        file.add(open);
        // Добавление разделителя
        file.addSeparator();
        file.add(exit);

        JFrame jFrame = this;

        open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println ("ActionListener.actionPerformed : open");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(Paths.get(System.getProperty("user.dir")).toFile());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("VRT FILES", "vrt", "vrt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(fileChooser);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());

                    pathToFile = selectedFile.getAbsolutePath();

                    loadVrt(pathToFile);

                    SwingUtilities.updateComponentTreeUI(jFrame);
                }
            }
        });
        return file;
    }

    private static void loadVrt(String pathToVrt){
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            File file = Paths.get(pathToFile).toFile();

            String pszVRTPathIn = file.getParentFile().getAbsolutePath();
            Path pathToXml = Paths.get(file.getAbsolutePath());
            byte[] bytes = Files.readAllBytes(pathToXml);

            VRTDataset deserializedVrtDataset = xmlMapper.readValue(bytes, VRTDataset.class);
            deserializedVrtDataset.setPathToFile(pathToXml.toString());

            //GdalDataset resultedDataset = extractFromVRTXml(deserializedVrtDataset, Paths.get("C:\\Users\\forol\\IdeaProjects\\javagdalvrt\\destfolder", "test.vrt").toString());

            GdalDataset gdalDataset = new GdalDataset();

            if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTPansharpenedDataset")) {
                PansharpeningAlghorithm pansharpeningAlghorithm = new PansharpeningAlghorithm();
                pansharpeningAlghorithm.executePansharpening(gdalDataset, pszVRTPathIn, deserializedVrtDataset);
            } else if (deserializedVrtDataset.getSubClass() != null && deserializedVrtDataset.getSubClass().equals("VRTWarpedDataset")) {
                WapredAlghorithm wapredAlghorithm = new WapredAlghorithm();
                //wapredAlghorithm.executeWarping(gdalDataset);
            } else {
                gdalDataset.InitXml(deserializedVrtDataset);
            }

            System.out.println("result of vrts "  + (gdalDataset.bufferedImage != null));

            bufferedImage = gdalDataset.bufferedImage;
        }catch (Exception ex){
            System.out.println("Возникла ошибка: " + ex.getMessage());
        }
    }

    //--------------------------------------------------------
    /**
     * Вложенный класс завершения работы приложения
     */
    class ExitAction extends AbstractAction
    {
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Выход");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    public void paint(Graphics g){

        System.out.println("drawing" + (bufferedImage == null));

        if (bufferedImage != null) {
            boolean b = g.drawImage(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight(), null);

            System.out.println("result of draw image "  + b);
            /*BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(Paths.get(pathToFile).toFile());

                g.drawImage(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight(), null);//выводим линию
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }





    }
}