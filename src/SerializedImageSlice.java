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

    public SerializedImageSlice(ImageIcon imagePiece, int xPos, int yPos, int currentXPos, int currentYPos) {
        this.imagePiece = imagePiece;
        this.xPos = xPos;
        this.yPos = yPos;
        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;
    }

    public ImageIcon getImagePiece () {
        return imagePiece;
    }

    public int getXPos () {
        return xPos;
    }

    public int getYPos () {
        return yPos;
    }

    public int getCurrentXPos () {
        return currentXPos;
    }

    public int getCurrentYPos () {
        return currentYPos;
    }
}
