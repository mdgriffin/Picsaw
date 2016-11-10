/**
 * Picsaw Image Puzzle Game
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PicSaw extends JFrame {
    private Container mainPane;
    private String[] imageUrls = new String[]{"images/img1.jpg", "images/img2.jpg", "images/img3.jpg", "images/img4.jpg"};
    private JLabel[] imageLabels;

    public static void main(String[] args) {
        PicSaw gameFrame = new PicSaw();
        gameFrame.setVisible(true);
    }

    public PicSaw () {
        setTitle("PicSaw - Image Puzzle Game");
        setSize (630,600);
        //setResizable (false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPane = getContentPane( );
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        mainMenuBar.add(new MainMenuItem("New"));
        mainMenuBar.add(new MainMenuItem("Item with submenu", new String[]{"Item 1", "Item 2", "Item 3"}));

        JLabel titleLabel = new JLabel("Please select from the following images:");

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel.setFont(new Font(titleLabel.getName(), Font.PLAIN, 22));

        mainPane.add(titleLabel);

        mainPane.add(createImageGrid());



    }

    private Container createImageGrid () {
        //java.net.URL imgURL;

        Container imagePane = new Container();
        imagePane.setLayout(new FlowLayout());

        imageLabels = new JLabel[4];

        // loading an image
        //java.net.URL imgURL = getClass().getResource("images/img1.jpeg");
        //ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");

        for (int i = 0; i < imageUrls.length; i++) {
            // loading an image
            java.net.URL imgURL = getClass().getResource(imageUrls[i]);
            //ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");
            ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));

            imageLabels[i] = new JLabel(imgIco);

            imagePane.add(imageLabels[i]);
        }

        return imagePane;
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
