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
import java.util.Random;

public class ImagePuzzleFrame extends JFrame {
    PicSaw parent;
    JMenu exitBtn;
    // TODO Set attributes from the constructor
    private int rows = 4;
    private  int cols = 4;
    ImageSlice[] imageSlices = new ImageSlice[rows * cols];
    //JLabel[] imageLabels = new JLabel[rows * cols];

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

        for (int i = 0; i < imageSlices.length; i++) {
            mainPane.add(imageSlices[i]);
            imageSlices[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void imageSplitter (String imageSrc) throws IOException, URISyntaxException {
        int[] randomSeq = randomIntSequence(rows * cols);

        File file = new File(getClass().getResource(imageSrc).toURI());
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis); //reading the image file

        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;

        BufferedImage[] bufferedImages = new BufferedImage[rows * cols];

        for (int i = 0, x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++, i++) {
                //Initialize the image array with image chunks
                bufferedImages[i] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = bufferedImages[i].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();

                // TODO Pass the x and y co-ordinates into the ImageSlice constructor
                imageSlices[randomSeq[i]] = new ImageSlice(new ImageIcon(bufferedImages[i]), x, y);
            }
        }

    }

    /**
     * Based on swap and shuffle methods in java.util.Collections
     */
    private static int[] randomIntSequence (int seqLen) {
        int[] seq = new int[seqLen];
        Random random = new Random();

        for (int i = 0; i < seqLen; i++) {
            seq[i] = i;
        }

        for (int i = seqLen; i > 1; i--) {
            int j = random.nextInt(i);
            int temp = seq[i - 1];
            seq[i - 1] = seq[j];
            seq[j] = temp;
        }

        return seq;
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

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }
}
