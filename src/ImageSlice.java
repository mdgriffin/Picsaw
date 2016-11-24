/**
 *  <h1>This class extends to JLabel to define a single image slice</h1>
 *
 *  It holds the value of the current x and y position within the grid and
 *  the images slices correct x and y position
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageSlice extends JLabel {
    private boolean selected = false;
    private int xPos;
    private int yPos;
    private int currentXPos;
    private int currentYPos;
    private ImagePuzzleFrame parent;

    /**
     * Creates a new ImageSlice
     *
     * @param parent       The parent element that instantiated the ImageSlice
     * @param imagePiece   The image to be placed within the ImageSlice
     * @param xPos         The correct x position of the slice within the grid
     * @param yPos         The correct y position of the slice within the grid
     * @param currentXPos  The current x position of the slice within the grid
     * @param currentYPos  The current y position of the slice within the grid
     */

    public ImageSlice (ImagePuzzleFrame parent, ImageIcon imagePiece, int xPos, int yPos, int currentXPos, int currentYPos) {
        super(imagePiece);

        this.parent = parent;

        this.xPos = xPos;
        this.yPos = yPos;

        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;

        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));

        this.addMouseListener(new ImageMouseEvent());
    }

    /**
     * Sets the selected property of the ImageSlice
     *
     * @param selected A boolean representing the whether the image slice is selected
     */

    public void setSelected (boolean selected) {
        this.selected = selected;
        setSelected();
    }

    /**
     *  Sets the border around the ImageSlice
     *  <p>
     *  Checks the selected attribute of the ImageSlice, if the ImageSlice
     *  is selected it sets a red border around the slice, otherwise a white
     *  border is created around the slice
     */

    public void setSelected () {
        if (selected) {
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        } else {
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
        }
    }

    /**
     *  Sets the currentX and currentY position of the slice
     *
     *  @param currentXPos The Image slice's current x position
     *  @param currentYPos The Image slice's current y position
     */

    public void setCurrentPos (int currentXPos, int currentYPos) {
        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;
    }

    /**
     * Gets the Image slice's current x position attribute
     *
     * @return The Image slice's current x position
     */

    public int getCurrentXPos () {
        return currentXPos;
    }

    /**
     * Gets the Image slice's current y position attribute
     *
     * @return The Image slice's current y position
     */

    public int getCurrentYPos () {
        return currentYPos;
    }

    /**
     *  Checks the values of the current x and y position with the correct x
     *  and y position. If the current x and y values match the correct x and y
     *  values then the slice is in it's correct position
     *
     * @return A boolean representing whether the slice is in the correct position
     */

    public boolean inCorrectPosition () {
        if (currentXPos == xPos && currentYPos == yPos) {
            return true;
        }
        return false;
    }

    /**
     * Extends the MouseAdapter class to handle mouse events on the Image Slice
     */

    private class ImageMouseEvent extends MouseAdapter {

        public void mousePressed (MouseEvent e) {
            parent.sliceSelected(ImageSlice.this);
        }

        public void mouseEntered (MouseEvent e) {
            ImageSlice.this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
        }

        public void mouseExited (MouseEvent e) {
            setSelected();
        }
    }

}
