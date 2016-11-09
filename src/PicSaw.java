/**
 * Picsaw Image Puzzle Game
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PicSaw extends JFrame {
    public static void main(String[] args) {
        PicSaw gameFrame = new PicSaw();

        gameFrame.setVisible(true);
    }

    public PicSaw () {
        Container pane;

        setTitle     ("PicSaw - Image Puzzle Game");
        setSize      (600,600);
        setResizable (false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pane = getContentPane( );
        pane.setLayout(new FlowLayout());
    }



}
