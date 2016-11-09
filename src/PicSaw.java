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

        setTitle("PicSaw - Image Puzzle Game");
        setSize (600,600);
        setResizable (false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pane = getContentPane( );
        pane.setLayout(new FlowLayout());

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        mainMenuBar.add(new MainMenuItem("New"));
        mainMenuBar.add(new MainMenuItem("Item with submenu", new String[]{"Item 1", "Item 2", "Item 3"}));

        // loading an image
        java.net.URL imgURL = getClass().getResource("images/img1.jpeg");
        ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");

        JLabel iconLabel = new JLabel(imgIco);

        pane.add(iconLabel);

    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private class MainMenuItem extends JMenu {
        public MainMenuItem (String name) {
            super(name);
        }

        public MainMenuItem (String name, String[] subMenuItems) {
            super(name);
            addSubMenuItems(subMenuItems);
        }

        private void addSubMenuItems (String[] items) {
            JMenuItem item;

            for (int i = 0; i < items.length; i++) {
                add(new JMenuItem(items[i]));
            }
        }
    }



}
