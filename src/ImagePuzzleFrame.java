/**
 * Defines an Image Jigsaw puzzle
 */
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;

public class ImagePuzzleFrame extends JFrame {
    PicSaw parent;
    JMenu exitBtn;
    // TODO Set attributes from the constructor
    private int rows = 4;
    private  int cols = 4;

    public ImagePuzzleFrame(PicSaw parent, String imageSrc) {
        this.parent = parent;

        setTitle("PicSaw - Image Puzzle Game");
        setSize(700, 600);
        setResizable(false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container mainPane = getContentPane();
        mainPane.setLayout(new FlowLayout());

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        exitBtn = new JMenu("Exit");

        exitBtn.addMenuListener(new ExitButtonMenuListener());

        mainMenuBar.add(exitBtn);

        // splitting the image into chunks
        try {
            imageSplitter(imageSrc);
        } catch (Exception exc) {
            System.out.println("IO Exception");
        }

        int chunks = rows * cols;
        JLabel[] imageLabels = new JLabel[chunks];

        for (int i = 0; i < chunks; i++) {
            java.net.URL imgURL = getClass().getResource("images/chunk" + i + ".jpg");
            ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage());

            imageLabels[i] = new JLabel(imgIco);
            mainPane.add(imageLabels[i]);
            imageLabels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

    }

    private void imageSplitter (String imageSrc) throws IOException, URISyntaxException {
        File file = new File(getClass().getResource(imageSrc).toURI());
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis); //reading the image file

        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;

        BufferedImage[] imgs = new BufferedImage[chunks];

        int count = 0;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();


            }
        }

        //writing mini images into image files
        // TODO Write image to file
        // TODO Add the original name before the image (Must parse it from the imageSrc)
        // file.getName() will return the file name, just need to remove the file
        // TODO Check if the image chunks have already been created

        for (int i = 0; i < imgs.length; i++) {
            ImageIO.write(imgs[i], "jpg", new File(file.getParent(), "chunk" + i + ".jpg"));
        }

    }


    private class ExitButtonMenuListener implements  MenuListener {
        public void menuSelected(MenuEvent e) {
            int choice =  JOptionPane.showConfirmDialog(
                    ImagePuzzleFrame.this,
                    "Are you sure you want to exit? You will lose any progress you have made with the puzzle",
                    "Please Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                parent.setVisible(true);
                ImagePuzzleFrame.this.dispose();
            } else {
                exitBtn.setSelected(false);
            }
        }

        public void menuDeselected(MenuEvent e) {

        }

        public void menuCanceled(MenuEvent e) {

        }
    }
}
