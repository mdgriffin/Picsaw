/**
 * Defines an Image Jigsaw puzzle
 */
import javax.swing.*;
import java.awt.*;

public class ImagePuzzleFrame extends JFrame {
    PicSaw parent;

    public ImagePuzzleFrame(PicSaw parent, String imageSrc) {
        this.parent = parent;

        setTitle("PicSaw - Image Puzzle Game");
        setSize(700, 600);
        setResizable(false);
        // centers the Frame
        setLocationRelativeTo(null);

        //setBackground(Color.black);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container mainPane = getContentPane();
        mainPane.setLayout(new FlowLayout());

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        JMenu backBtn = new JMenu("Back");

        mainMenuBar.add(backBtn);

        // loading the image
        java.net.URL imgURL = getClass().getResource(imageSrc);
        ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage());

        JLabel imageLabel = new JLabel(imgIco);

        mainPane.add(imageLabel);

        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
