import javax.swing.*;

public class ImageSlice extends JLabel {
    private int xPos;
    private int yPos;

    public ImageSlice (ImageIcon imagePiece, int xPos, int yPos) {
        super(imagePiece);

        this.xPos = xPos;
        this.yPos = yPos;
    }

}
