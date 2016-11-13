/**
 * Defines an Image Jigsaw puzzle
 */
// TODO the scaled image height should not exceed the height of the window - the frame borders
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
    private Container mainPane;
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
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPane = getContentPane();

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        exitBtn = new JMenu("Exit");
        exitBtn.addMenuListener(new ExitButtonMenuListener());
        mainMenuBar.add(exitBtn);

        imagePane = new Container();

        GridBagLayout gl = new GridBagLayout();
        //gl.setVgap(0);
        //gl.setHgap(0);

        imagePane.setLayout(gl);

        mainPane.add(imagePane);

        // splitting the image into slices
        try {
            generatePuzzleGrid(imageSrc);
        } catch (Exception exc) {
            System.out.println(exc);
        }

        pack();

        // centers the Frame
        setLocationRelativeTo(null);
    }

    /**
     * Based on code found here:
     * http://kalanir.blogspot.ie/2010/02/how-to-split-image-into-chunks-java.html
     */
    private void generatePuzzleGrid (java.net.URI imageSrc) throws IOException, URISyntaxException {
        int[] randomSeq = randomIntSequence(rows * cols);

        File file = new File(imageSrc);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage srcImage = ImageIO.read(fis);

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

                //imageSlices[randomSeq[i]] = new ImageSlice(this, new ImageIcon(bufferedImages[i]), x, y, randomSeq[i] % cols, randomSeq[i] / rows);

                // from my list of randomNums, take one num, this will decide the position in the grid that the element will be placed in
                int randomX = randomSeq[i] % cols;
                int randomY = randomSeq[i] / rows;

                imageSlices[i] = new ImageSlice(this, new ImageIcon(bufferedImages[i]), x, y, randomX, randomY);

                GridBagConstraints gridConstraints = new GridBagConstraints();

                gridConstraints.gridx = randomX;
                gridConstraints.gridy = randomY;

                imagePane.add(imageSlices[i], gridConstraints);
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
            //swapSlices();
            swapGridItems(sourceSlice, destSlice);

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

    private void swapGridItems (ImageSlice sourceSlice, ImageSlice destSlice) {
        int sourceX = sourceSlice.getCurrentXPos();
        int sourceY = sourceSlice.getCurrentYPos();
        int destX = destSlice.getCurrentXPos();
        int destY = destSlice.getCurrentYPos();

        imagePane.remove(sourceSlice);
        imagePane.remove(destSlice);

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.gridx = sourceX;
        gridConstraints.gridy = sourceY;

        imagePane.add(destSlice, gridConstraints);

        destSlice.setCurrentPos(sourceX, sourceY);

        gridConstraints.gridx = destX;
        gridConstraints.gridy = destY;

        imagePane.add(sourceSlice, gridConstraints);

        sourceSlice.setCurrentPos(destX, destY);

        imagePane.revalidate();
        imagePane.repaint();
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
