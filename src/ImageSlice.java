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
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.Serializable;

public class ImageSlice extends JLabel implements Cloneable {
    private ImagePuzzleFrame parent;
    private ImageIcon imagePiece;
    private int xPos;
    private int yPos;
    private int currentXPos;
    private int currentYPos;
    private int dragInitialX = 0;
    private int dragInitialY = 0;
    private static boolean sliceDragging = false;

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
        this.imagePiece = imagePiece;

        this.xPos = xPos;
        this.yPos = yPos;

        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;

        addMouseListener(new ImageMouseEvent());
        addMouseMotionListener(new ImageMouseMotionEvent());
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
     *
     * @param parent The parent ImagePuzzleFrame of the new ImageSlice
     * @return A new instance of this object with the same aatributes
     */

    public ImageSlice clone (ImagePuzzleFrame parent) {
        return new ImageSlice(parent, imagePiece, xPos, yPos, currentXPos, currentYPos);
    }

    /**
     * Creates or removes a border around the image slice to indicate whether
     * the image slice is highlighted or not
     *
     * @param isHighlighted A boolean indicating whether the imageSlice should be highlighted
     */


    public void highlight (boolean isHighlighted) {
        setPreferredSize(new Dimension(getWidth(), getHeight()));

        if (isHighlighted) {
            setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
        } else {
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * Extends the MouseAdapter class to handle mouse events on the Image Slice
     */

    private class ImageMouseEvent extends MouseAdapter {

        public void mousePressed (MouseEvent e) {
            sliceDragging = true;

            dragInitialX = e.getX();
            dragInitialY = e.getY();
            ImageSlice.this.parent.sliceDragStart(ImageSlice.this, (int)ImageSlice.this.getLocation().getX(), (int)ImageSlice.this.getLocation().getY());
        }

        public void mouseReleased(MouseEvent e) {
            sliceDragging = false;
            ImageSlice.this.parent.sliceDragEnd(ImageSlice.this);
        }

        public void mouseEntered (MouseEvent e) {
            highlight(true);

            if (sliceDragging) {
                ImageSlice.this.parent.setDestSlice(ImageSlice.this);
            }

        }

        public void mouseExited (MouseEvent e) {
            highlight(false);

            if (sliceDragging) {
                ImageSlice.this.parent.setDestSlice(null);
            }
        }

    }

    /**
     * Extends MouseMotionAdapter to handle mouse motion events
     */

    private class ImageMouseMotionEvent extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            ImageSlice.this.parent.sliceDragging((int)ImageSlice.this.getLocation().getX() + e.getX() - dragInitialX, (int)ImageSlice.this.getLocation().getY() + e.getY() - dragInitialY);
        }
    }

}
