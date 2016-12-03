/**
 * <h1>Picsaw - Image Jigsaw puzzle game</h1>
 *
 * Splits an image into blocks and displays the blocks in a randomized grid.
 * The user selects a source and destination image block and their location is swapped.
 * <p>
 * Allows the user to select from either pre-defined images or select their own
 * images from the file system.
 *
 * @author  Michael Griffin
 * @version 0.5
 * @since   2016/11/22
 */


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class PicSaw extends JFrame {
    private Container mainPane;
    private Container imagePane;
    private JComboBox difficultySelect;
    private String[] imageUrls = new String[]{"images/img1.jpg", "images/img2.jpg", "images/img3.jpg", "images/img4.jpg"};
    private JLabel[] imageLabels;

    /**
     * Creates a new instance of the PicSaw class and sets it's visibility to true
     *
     * @param args Default String array arguments parameter
     */

    public static void main(String[] args) {
        PicSaw gameFrame = new PicSaw();
        gameFrame.setVisible(true);
    }

    /**
     * Creates an instance of PicSaw setting up the menu, difficulty select and default images grid
     */

    public PicSaw () {
        setTitle("PicSaw - Image Puzzle Game");
        setSize (700,600);
        setResizable (false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPane = getContentPane( );
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        JMenu openImageButton = new JMenu("Open Image");
        openImageButton.addMenuListener(new OpenFileMenuListener());

        JMenu loadPuzzleButton = new JMenu("Load Puzzle");
        loadPuzzleButton.addMenuListener(new LoadPuzzleMenuListener());

        mainMenuBar.add(openImageButton);
        mainMenuBar.add(loadPuzzleButton);

        JPanel difficultyPane = new JPanel();
        difficultyPane.setLayout(new FlowLayout());
        difficultyPane.setMaximumSize(new Dimension(400, 100));

        mainPane.add(difficultyPane);

        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        difficultyPane.add(difficultyLabel);

        difficultySelect = new JComboBox(new String[]{ "Easy", "Moderate", "Hard", "Impossible"});
        difficultySelect.setSelectedIndex(0);
        difficultyPane.add(difficultySelect);

        JLabel titleLabel = new JLabel("Please select from the following images:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getName(), Font.PLAIN, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPane.add(titleLabel);

        mainPane.add(createImageGrid());
    }

    /**
     * Creates and returns a Container containing the grid of default images used by the system.
     *
     * <p>
     *
     *  The default images are stored in the imageUrls class attribute as an array.
     *  The ImageLabels are stored as an instance attribute of the class as an array
     *  An instance of the  ImageGridMouseEvent class is bound to each of the imageLabels
     *
     * @return A <code>Container</code> with a grid of default images
     */

    private Container createImageGrid () {
        imageLabels = new JLabel[4];
        ImageGridMouseEvent mouseEvent = new ImageGridMouseEvent();

        imagePane = new JPanel();
        imagePane.setLayout(new FlowLayout());

        for (int i = 0; i < imageUrls.length; i++) {
            // loading an image
            java.net.URL imgURL = getClass().getResource(imageUrls[i]);
            //ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");
            ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));

            imageLabels[i] = new JLabel(imgIco);

            imageLabels[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
            imageLabels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            imageLabels[i].addMouseListener(mouseEvent);

            imagePane.add(imageLabels[i]);
        }

        return imagePane;
    }

    /**
     *  Gets the Index of the <code>ImageLabel</code> that emitted the click <code>MouseEvent</code>
     *  This can then be used to get the image source of the label clicked which is then passed to the
     *  constructor method of the <code>ImagePuzzleFrame</code> to set up the image puzzle grid
     *
     * @param sourceEvent
     * @return <code>int</code> index of the <code>ImageLabel</code> that emitted the click <code>MouseEvent</code>
     */

    private int getEventSourceLabelIndex (MouseEvent sourceEvent) {
        for (int i = 0; i < imageLabels.length; i++) {
            if (imageLabels[i] == sourceEvent.getSource()) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Gets the difficulty level set in the difficulty combo box
     * <p>
     * Checks the String of the selected item in the difficultySelect element
     * and uses the value to decide on the number of rows and columns that
     * the image should be divided into
     * @return <code>int[]</code> An array with the first element being the number of rows and
     * the second element being the number of columns
     */

    private int[] getDifficultyLevel () {
        int rows = 4;
        int cols = 4;
        String selectedDifficulty = difficultySelect.getSelectedItem().toString();

        if (selectedDifficulty.equals("Moderate")) {
            rows = cols = 6;
        } else if (selectedDifficulty.equals("Hard")) {
            rows =  cols = 8;
        } else if (selectedDifficulty.equals("Impossible")) {
            rows = cols = 16;
        }

        return new int[]{rows, cols};

    }

    /**
     * ImageGridMouseEvent extends the {@link MouseAdapter} class
     * <p>
     * Handles the mousePressed, mouseEntered and mouseExited events
     * Class is instantiated as a mouse event listener on the main image grid
     */

    private class ImageGridMouseEvent extends MouseAdapter {
        public void mousePressed (MouseEvent e) {
            try  {
                java.net.URI  imageSrc = getClass().getResource(imageUrls[getEventSourceLabelIndex(e)]).toURI();
                int[] difficultyLevel = getDifficultyLevel();

                ImagePuzzleFrame puzzle = new ImagePuzzleFrame(PicSaw.this, imageSrc, difficultyLevel[0], difficultyLevel[1]);

                puzzle.setVisible(true);
                PicSaw.this.setVisible(false);
            } catch (URISyntaxException exc) {
                exc.printStackTrace();
            }
        }

        public void mouseEntered (MouseEvent e) {
            imageLabels[getEventSourceLabelIndex(e)].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red));

        }

        public void mouseExited (MouseEvent e) {
            imageLabels[getEventSourceLabelIndex(e)].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
        }
    }

    /**
     *  Implements the {@link MenuListener} to listen for menu events on the Open File Menu item
     *  <p>
     *  Opens a {@link FileDialog} and allows the user to select from image files stored on their system
     *  Once a file has been selected, a new instance of {@link ImagePuzzleFrame} frame will be instantiated
     *  passing in the selected file source as a URI.
     */

    // TODO menu currently buggy, sometimes opens unexpectedly
    private class OpenFileMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            FileDialog picker = new FileDialog(PicSaw.this);
            picker.setFilenameFilter(new ImageFileFilter());
            picker.setVisible(true);

            String filename = picker.getFile();
            String dir = picker.getDirectory();

            if (filename != null) {
                int[] difficultyLevel = getDifficultyLevel();
                ImagePuzzleFrame puzzle = new ImagePuzzleFrame(PicSaw.this, URI.create("File:///" + dir.replace(" ", "%20").replace("\\", "/") + filename.replace(" ", "%20")), difficultyLevel[0], difficultyLevel[1]);
                puzzle.setVisible(true);

                PicSaw.this.setVisible(false);
            }
        }

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }

    private class LoadPuzzleMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            FileDialog picker = new FileDialog(PicSaw.this);
            picker.setFilenameFilter(new DatFileFilter());
            picker.setVisible(true);

            String filename = picker.getFile();
            String dir = picker.getDirectory();

            if (filename != null) {
                try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir + filename));
                ImageSlice[] imageSlices = (ImageSlice[]) ois.readObject();

                ImagePuzzleFrame frame = new ImagePuzzleFrame(PicSaw.this, imageSlices);
                frame.setVisible(true);
                PicSaw.this.setVisible(false);
                ois.close();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }

    /**
     * Returns a FileName filter for filtering out all file types except for image files
     * <p>
     *  Used in conjunction with the {@link FileDialog} that is created when the "Open Image"
     *  menu item is selected. This lanuches a FileDialog where the only files selectable
     *  are files that have a jpg, jpeg, png or gif file extension
     */

    private class ImageFileFilter implements FilenameFilter {
        public boolean accept (File dir, String name) {
            if (name.matches(".+\\.(jpg|jpeg|png|gif)$")) {
                return true;
            }
            return true;
        }
    }

    private class DatFileFilter implements FilenameFilter {
        public boolean accept (File dir, String name) {
            if (name.matches(".+\\.(dat|data)$")) {
                return true;
            }
            return true;
        }
    }
}
