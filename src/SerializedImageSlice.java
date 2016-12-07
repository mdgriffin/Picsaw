import javax.swing.ImageIcon;
import java.io.Serializable;

/**
 *  <h1>This class stores a simplified representation of an ImageSlice object</h1>
 *
 *  Stores the key attributes of an ImageSlice. This simplifies the serialization and
 *  of the ImageSlice objects. Used for saving and loading ImageSlices
 */

public class SerializedImageSlice implements Serializable {
    private ImageIcon imagePiece;
    private int xPos;
    private int yPos;
    private int currentXPos;
    private int currentYPos;

    /**
     * Defined a Serializable version of an Image Slice
     *
     * @param imagePiece    The Image Icon that will be inside the Image Slice's JLabel
     * @param xPos          The current x co-ordinate of the Image Slice
     * @param yPos          The current y co-ordinate of the Image Slice
     * @param currentXPos   The correct x co-ordinate of the Image Slice
     * @param currentYPos   The correct y co-ordinate of the Image Slice
     */

    public SerializedImageSlice(ImageIcon imagePiece, int xPos, int yPos, int currentXPos, int currentYPos) {
        this.imagePiece = imagePiece;
        this.xPos = xPos;
        this.yPos = yPos;
        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;
    }

    /**
     * Getter to retrieve the Image Piece
     *
     * @return The Serialized Image Slice's ImageIcon
     */

    public ImageIcon getImagePiece () {
        return imagePiece;
    }

    /**
     * Getter to retrieve the current x position of the Image Piece
     *
     * @return An int representing the current x position of the Image Piece
     */

    public int getXPos () {
        return xPos;
    }

    /**
     * Getter to retrieve the current y position of the Image Piece
     *
     * @return An int representing the current y position of the Image Piece
     */

    public int getYPos () {
        return yPos;
    }

    /**
     * Getter to retrieve the correct x position of the Image Piece
     *
     * @return An int representing the current y position of the Image Piece
     */

    public int getCurrentXPos () {
        return currentXPos;
    }

    /**
     * Getter to retrieve the correct y position of the Image Piece
     *
     * @return An int representing the current y position of the Image Piece
     */

    public int getCurrentYPos () {
        return currentYPos;
    }
}
