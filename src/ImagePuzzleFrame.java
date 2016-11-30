/**
 *  <h1>This class defines an Image Jigsaw puzzle</h1>
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
import java.util.Random;
import java.beans.XMLEncoder;

public class ImagePuzzleFrame extends JFrame {
    private Container mainPane;
    private Container imagePane;
    PicSaw parent;
    JMenu exitBtn;
    JMenu saveBtn;
    ImageSlice[] imageSlices;
    private ImageSlice sourceSlice;
    private ImageSlice destSlice;

    /**
     *  Create a new ImagePuzzleFrame from the provided image source URI
     *  <p>
     *  Generates a randomized grid from the supplied image, split into the
     *  number of rows and columns specified in the parameters
     *
     *  @param parent   The parent JFrame that instantiated this class
     *  @param imageSrc The source of the image that will be used to create the image grid
     *  @param rows     The number of rows that the image will be split into
     *  @param cols     The number of columns that the image will be split into
     */

    public ImagePuzzleFrame(PicSaw parent, java.net.URI imageSrc, int rows, int cols) {
        this.parent = parent;
        imageSlices = new ImageSlice[rows * cols];

        setUpFrame();

        // splitting the image into slices and generating the grid
        try {
            generatePuzzleGrid(imageSrc, rows, cols);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        pack();

        // centers the Frame
        setLocationRelativeTo(null);
    }


    public ImagePuzzleFrame(PicSaw parent, ImageSlice[] imageSlices) {
        this.parent = parent;
        this.imageSlices = imageSlices;

        setUpFrame();

        GridBagConstraints gridConstraints = new GridBagConstraints();

        for (int i = 0; i < imageSlices.length; i++) {
            gridConstraints.gridx = imageSlices[i].getCurrentXPos();
            gridConstraints.gridy = imageSlices[i].getCurrentYPos();


            imagePane.add(imageSlices[i].clone(this), gridConstraints);
        }

        pack();

        // centers the Frame
        setLocationRelativeTo(null);

    }

    private void setUpFrame () {
        setTitle("PicSaw - Image Puzzle Game");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPane = getContentPane();

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        exitBtn = new JMenu("Exit");
        mainMenuBar.add(exitBtn);
        exitBtn.addMenuListener(new ExitButtonListener());

        saveBtn = new JMenu("Save");
        mainMenuBar.add(saveBtn);
        saveBtn.addMenuListener(new SaveButtonListener());

        imagePane = new Container();

        GridBagLayout gl = new GridBagLayout();

        imagePane.setLayout(gl);

        mainPane.add(imagePane);
    }

    /**
     * Sets of the image  puzzle gird
     *  <p>
     *  Loads the image from the file system
     *  Generates a {@link BufferedImage} array from the src image
     *  setting it's correct width and height and it's correct x and y co-ordinates in the original image
     *  Creates a new {@link ImageSlice} from the buffered image, settings it {@link GridBagConstraints} to a
     *  random x and y position
     *  Adds the {@link ImageSlice} to the imagePane
     *
     * @param imageSrc The URI of the image that will be used to create the puzzle grid
     * @throws IOException
     * @throws URISyntaxException
     */

    private void generatePuzzleGrid (java.net.URI imageSrc, int rows, int cols) throws IOException, URISyntaxException {
        int[] randomSeq = randomIntSequence(rows * cols);
        GridBagConstraints gridConstraints = new GridBagConstraints();
        BufferedImage[] bufferedImages = new BufferedImage[rows * cols];

        File file = new File(imageSrc);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage srcImage = ImageIO.read(fis);

        int scaledWidth = 600;
        int scaledHeight = (int)((double)scaledWidth / (double)srcImage.getWidth() * srcImage.getHeight());

        int sliceWidth = scaledWidth / cols;
        int sliceHeight = scaledHeight / rows;

        int srcSliceWidth = srcImage.getWidth() / cols;
        int srcSliceHeight = srcImage.getHeight() / rows;


        for (int i = 0, y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++, i++) {
                bufferedImages[i] = new BufferedImage(sliceWidth, sliceHeight, srcImage.getType());

                Graphics2D gr = bufferedImages[i].createGraphics();
                gr.drawImage(srcImage, 0, 0, sliceWidth, sliceHeight, srcSliceWidth * x, srcSliceHeight * y, srcSliceWidth * x + srcSliceWidth, srcSliceHeight * y + srcSliceHeight, null);
                gr.dispose();

                int randomX = randomSeq[i] % cols;
                int randomY = randomSeq[i] / rows;

                imageSlices[i] = new ImageSlice(this, new ImageIcon(bufferedImages[i]), x, y, randomX, randomY);

                gridConstraints.gridx = randomX;
                gridConstraints.gridy = randomY;

                imagePane.add(imageSlices[i], gridConstraints);
            }
        }
    }

    /**
     * This method is used to set the two currently selected {@link ImageSlice} elements
     * The two elements positions are then swapped and a method is called to check if the
     *  board has been solved
     *
     * @param slice The {@link ImageSlice} that has been selected
     */

    public void sliceSelected (ImageSlice slice) {
        if  (sourceSlice == null && destSlice == null) {
            sourceSlice = slice;
            sourceSlice.setSelected(true);
        } else if (sourceSlice != null && destSlice == null) {
            destSlice = slice;
            destSlice.setSelected(true);

            // both slices are set, we can now reorder board
            swapGridItems(sourceSlice, destSlice);

            // remove the selected state
            sourceSlice.setSelected(false);
            destSlice.setSelected(false);

            // once the board is reordered
            sourceSlice = null;
            destSlice = null;

            // finally check if the board is correct
            // if correct, show a congratulation  message
            if (boardSolved()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Congratulations! You have solved the board",
                    "Board Solved",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    /**
     * This method swapped the grid position of two {@link ImageSlice} elements
     *
     * @param sourceSlice The source {link ImageSlice} element that is to be swapped
     * @param destSlice   The destination {@link ImageSlice} element
     */

    private void swapGridItems (ImageSlice sourceSlice, ImageSlice destSlice) {
        int sourceX = sourceSlice.getCurrentXPos();
        int sourceY = sourceSlice.getCurrentYPos();
        int destX = destSlice.getCurrentXPos();
        int destY = destSlice.getCurrentYPos();

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

    /**
     * This method determines if the board has been solved
     * <p>
     * Loops over all the {@link ImageSlice} elements and check if the position is correct
     * If all the elements are in the correct position the board has been solved and the
     * game is over
     *
     * @return A boolean that determines whether all the image slices are in their correct position
     */

    private boolean boardSolved () {
        for (int i = 0; i < imageSlices.length; i++) {
            if (!imageSlices[i].inCorrectPosition()) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method generates a random sequence of integers in a range
     * from 0 up to the value supplied in the seqLen parameter
     * <p>
     * Based on swap and shuffle methods in java.util.Collections
     *
     *  @param seqLen  The length of the sequence and the max value in the sequence range
     *  @return        A randomly ordered int array
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

    /**
     *  Implements the MenuListener interface to listen for events on the exit button menu item
     */

    private class ExitButtonListener implements  MenuListener {
        public void menuSelected(MenuEvent e) {
            if (!boardSolved()) {
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
            } else {
                parent.setVisible(true);
                ImagePuzzleFrame.this.dispose();
            }

        }

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }

    /**
     * Handles the saving of the current game state to file
     *
     * @throws Exception
     */

    private void savePuzzle () throws Exception {
        FileDialog picker = new FileDialog(ImagePuzzleFrame.this, "Save Puzzle", FileDialog.SAVE);
        picker.setVisible(true);

        String filePath = picker.getDirectory();
        String filename = picker.getFile();

        if (!filename.endsWith(".dat")) {
            filename += ".data";
        }

        File savedFile = new File(filePath + filename);
        FileOutputStream  outFileStream	= new FileOutputStream(savedFile);
        ObjectOutputStream objectOut = new ObjectOutputStream(outFileStream);
        objectOut.writeObject(imageSlices);
        objectOut.close();
    }

    /**
     * Implements the MenuListener interface to handle selected events
     * on the save button JMenu
     */

    private class SaveButtonListener implements  MenuListener {
        public void menuSelected(MenuEvent e) {
            try {
                savePuzzle();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(
                    ImagePuzzleFrame.this,
                    "Failed to save, please try again",
                    "Error while saving",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        }

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }
}
