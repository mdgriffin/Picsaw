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
import java.nio.Buffer;
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

    public ImagePuzzleFrame(PicSaw parent, java.net.URI imageSrc) {
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

    /**
     * Based on code found here:
     * http://kalanir.blogspot.ie/2010/02/how-to-split-image-into-chunks-java.html
     */
    private void imageSplitter (java.net.URI imageSrc) throws IOException, URISyntaxException {
        int[] randomSeq = randomIntSequence(rows * cols);

        File file = new File(imageSrc);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage srcImage = ImageIO.read(fis);

        // TODO the frames height should be set relative to the height of the image
        int scaledWidth = 600;
        int scaledHeight = (int)((double)scaledWidth / (double)srcImage.getWidth() * srcImage.getHeight());

        int sliceWidth = scaledWidth / cols;
        int sliceHeight = scaledHeight / rows;

        int srcSliceWidth = srcImage.getWidth() / cols;
        int srcSliceHeight = srcImage.getHeight() / rows;

        BufferedImage[] bufferedImages = new BufferedImage[rows * cols];

        for (int i = 0, x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++, i++) {
                bufferedImages[i] = new BufferedImage(sliceWidth, sliceHeight, srcImage.getType());

                Graphics2D gr = bufferedImages[i].createGraphics();
                gr.drawImage(srcImage, 0, 0, sliceWidth, sliceHeight, srcSliceWidth * y, srcSliceHeight * x, srcSliceWidth * y + srcSliceWidth, srcSliceHeight * x + srcSliceHeight, null);
                gr.dispose();

                imageSlices[randomSeq[i]] = new ImageSlice(this, new ImageIcon(bufferedImages[i]), x, y, randomSeq[i] % cols, randomSeq[i] / rows);
            }
        }
    }

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

    // TODO Image Swapping Not Working
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

    private boolean boardSolved () {
        for (int i = 0; i < imageSlices.length; i++) {
            if (!imageSlices[i].inCorrectPosition()) {
                return false;
            }
        }
        return true;
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
