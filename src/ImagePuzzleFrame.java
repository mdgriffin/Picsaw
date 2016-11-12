/**
 * Defines an Image Jigsaw puzzle
 */
// TODO Images need to be resized to not be excessively large
// TODO the size of frame needs to be set based on the aspect ratio of the image
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
    private Container imagePane;
    PicSaw parent;
    JMenu exitBtn;
    // TODO Set row and col attributes from the constructor
    private int rows = 4;
    private  int cols = 4;
    ImageSlice[] imageSlices = new ImageSlice[rows * cols];
    //JLabel[] imageLabels = new JLabel[rows * cols];
    private ImageSlice sourceSlice;
    private ImageSlice destSlice;

    public ImagePuzzleFrame(PicSaw parent, String imageSrc) {
        this.parent = parent;

        setTitle("PicSaw - Image Puzzle Game");
        setSize(700, 600);
        setResizable(false);
        // centers the Frame
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container mainPane = getContentPane();

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        exitBtn = new JMenu("Exit");
        exitBtn.addMenuListener(new ExitButtonMenuListener());
        mainMenuBar.add(exitBtn);

        // splitting the image into slices
        try {
            imageSplitter(imageSrc);
        } catch (Exception exc) {
            System.out.println(exc);
        }

        imagePane = new Container();

        FlowLayout fl = new FlowLayout();
        fl.setVgap(0);
        fl.setHgap(0);

        imagePane.setLayout(fl);

        mainPane.add(imagePane);

        addSlicesToImagePane();
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

                // TODO Pass the randomized x and y co-ordinates into the ImageSlice constructor
                imageSlices[randomSeq[i]] = new ImageSlice(this, new ImageIcon(bufferedImages[i]), x, y);
            }
        }

    }

    // once both slices are set, then the source and destination should be set to null
    // and then the board should reordered
    // needs to check if the board is correct
    public void sliceSelected (ImageSlice slice) {
        if  (sourceSlice == null && destSlice == null) {
            sourceSlice = slice;
            sourceSlice.setSelected(true);
        } else if (sourceSlice != null && destSlice == null) {
            destSlice = slice;
            destSlice.setSelected(true);

            // both slices are set, we can now reorder board
            swapSlices();

            // remove the selected state
            // TODO change method to selected(boolean)
            sourceSlice.setSelected(false);
            destSlice.setSelected(false);

            // once the board is reordered
            sourceSlice = null;
            destSlice = null;

            // finally check if the board is correct
            // if correct, show a congratulation  message
            // JOP
        }
    }

    private void swapSlices () {
        // source and destination slices are set
        if (sourceSlice != null && destSlice != null) {
            int sourceSliceIndex = getSliceIndex(sourceSlice);
            int destSliceIndex = getSliceIndex(destSlice);

            ImageSlice tempSlice = imageSlices[sourceSliceIndex];
            imageSlices[sourceSliceIndex] = imageSlices[destSliceIndex];
            imageSlices[destSliceIndex] = tempSlice;

            imagePane.removeAll();
            addSlicesToImagePane();

            imagePane.repaint();
        }
    }

    private void addSlicesToImagePane () {
        for (int i = 0; i < imageSlices.length; i++) {
            imagePane.add(imageSlices[i]);
            imageSlices[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private int getSliceIndex (ImageSlice slice) {
        for (int i = 0; i < imageSlices.length; i++) {
            if (slice == imageSlices[i]) {
                return i;
            }
        }

        // if we can't find the slice, return -1;
        return -1;
    }

    /**
     * Based on swap and shuffle methods in java.util.Collections
     */
    public static int[] randomIntSequence (int seqLen) {
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
