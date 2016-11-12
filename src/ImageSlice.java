import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageSlice extends JLabel{
    private int xPos;
    private int yPos;
    // TODO Set these attributes from constructor
    //private int currentXPos;
    //private int currentYPos;
    private ImagePuzzleFrame parent;

    public ImageSlice (ImagePuzzleFrame parent, ImageIcon imagePiece, int xPos, int yPos) {
        super(imagePiece);

        this.xPos = xPos;
        this.yPos = yPos;

        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));

        this.addMouseListener(new ImageMouseEvent());
    }

    private void select () {
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
    }

    private void deselect () {
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));
    }

    public int getxPos () {
        return xPos;
    }

    public int getyPos () {
        return yPos;
    }

    // TODO Uncomment and use to determine if all pieces are in their correct position
    // TODO Should this method be static?
    /*
    public boolean inCorrectPosition () {
        if (currentXPos = xPos && currentYPos == yPos) {
            return true;
        }
        return false
    }
    */

    private class ImageMouseEvent extends MouseAdapter {
        public void mousePressed (MouseEvent e) {
            ImageSlice.this.select();
        }

        public void mouseReleased (MouseEvent e) {
            ImageSlice.this.deselect();
        }
    }

}
