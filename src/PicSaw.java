/**
 * Picsaw Image Puzzle Game
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PicSaw extends JFrame {
    private Container mainPane;

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
        Container imagePane = new Container();

        imagePane.setLayout(new FlowLayout());

        // loading an image
        java.net.URL imgURL = getClass().getResource("images/img1.jpeg");
        //ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");

        ImageIcon imgIco1 = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));
        ImageIcon imgIco2 = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));
        ImageIcon imgIco3 = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));
        ImageIcon imgIco4 = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));


        JLabel iconLabel1 = new JLabel(imgIco1);
        JLabel iconLabel2 = new JLabel(imgIco2);
        JLabel iconLabel3 = new JLabel(imgIco3);
        JLabel iconLabel4 = new JLabel(imgIco4);

        imagePane.add(iconLabel1);
        imagePane.add(iconLabel2);
        imagePane.add(iconLabel3);
        imagePane.add(iconLabel4);

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
