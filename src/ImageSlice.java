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

    public ImageSlice (ImagePuzzleFrame parent, ImageIcon imagePiece, int xPos, int yPos, int currentXPos, int currentYPos) {
        super(imagePiece);

        this.parent = parent;

        this.xPos = xPos;
        this.yPos = yPos;

        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;

        this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));

        this.addMouseListener(new ImageMouseEvent());
    }

    public void setSelected (boolean selected) {
        this.selected = selected;
        setSelected();
    }

    public void setSelected () {
        if (selected) {
            this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red));
        } else {
            this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
        }
    }

    public void setCurrentPos (int currentXPos, int currentYPos) {
        this.currentXPos = currentXPos;
        this.currentYPos = currentYPos;
    }

    public boolean inCorrectPosition () {
        if (currentXPos == xPos && currentYPos == yPos) {
            return true;
        }
        return false;
    }

    private class ImageMouseEvent extends MouseAdapter {

        public void mousePressed (MouseEvent e) {
            parent.sliceSelected(ImageSlice.this);
        }

        public void mouseEntered (MouseEvent e) {
            ImageSlice.this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.green));
        }

        public void mouseExited (MouseEvent e) {
            setSelected();
        }
    }

}
