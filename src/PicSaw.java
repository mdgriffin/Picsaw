/**
 * Picsaw Image Puzzle Game
 */
import java.awt.*;
import java.awt.event.*;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class PicSaw extends JFrame {
    private Container mainPane;
    private Container imagePane;
    private String[] imageUrls = new String[]{"images/img1.jpg", "images/img2.jpg", "images/img3.jpg", "images/img4.jpg"};
    private JLabel[] imageLabels;

    public static void main(String[] args) {
        PicSaw gameFrame = new PicSaw();
        gameFrame.setVisible(true);
    }

    public PicSaw () {
        setTitle("PicSaw - Image Puzzle Game");
        setSize (700,600);
        setResizable (false);
        // centers the Frame
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPane = getContentPane( );
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);

        MainMenuItem openImageBuutton = new MainMenuItem("Open Image");
        openImageBuutton.addMenuListener(new OpenFileMenuListener());

        mainMenuBar.add(openImageBuutton);
        mainMenuBar.add(new MainMenuItem("Item with submenu", new String[]{"Item 1", "Item 2", "Item 3"}));

        JLabel titleLabel = new JLabel("Please select from the following images:");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getName(), Font.PLAIN, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        mainPane.add(titleLabel);

        mainPane.add(createImageGrid());
    }

    private Container createImageGrid () {
        imageLabels = new JLabel[4];
        ImageGridMouseEvent mouseEvent = new ImageGridMouseEvent();

        imagePane = new Container();
        imagePane.setLayout(new FlowLayout());

        for (int i = 0; i < imageUrls.length; i++) {
            // loading an image
            java.net.URL imgURL = getClass().getResource(imageUrls[i]);
            //ImageIcon imgIco = new ImageIcon(imgURL, "Image Description");
            ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT));

            imageLabels[i] = new JLabel(imgIco);

            imageLabels[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
            imageLabels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            imageLabels[i].addMouseListener(mouseEvent);

            imagePane.add(imageLabels[i]);
        }

        return imagePane;
    }

    private int getEventSourceLabelIndex (MouseEvent sourceEvent) {
        for (int i = 0; i < imageLabels.length; i++) {
            if (imageLabels[i] == sourceEvent.getSource()) {
                return i;
            }
        }

        return 0;
    }

    private class ImageGridMouseEvent extends MouseAdapter {
        public void mousePressed (MouseEvent e) {
            try  {
                java.net.URI  imageSrc = getClass().getResource(imageUrls[getEventSourceLabelIndex(e)]).toURI();
                ImagePuzzleFrame puzzle = new ImagePuzzleFrame(PicSaw.this, imageSrc);

                puzzle.setVisible(true);
                PicSaw.this.setVisible(false);
            } catch (URISyntaxException exc) {
                System.out.println(exc);
            }
        }

        public void mouseEntered (MouseEvent e) {
            imageLabels[getEventSourceLabelIndex(e)].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red));

        }

        public void mouseExited (MouseEvent e) {
            imageLabels[getEventSourceLabelIndex(e)].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white));
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

    private class OpenFileMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files Only", "jpg", "jpeg", "png", "gif");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(PicSaw.this);

            if(returnVal == JFileChooser.APPROVE_OPTION) {
                // TODO check the returned file is an image
                ImagePuzzleFrame puzzle = new ImagePuzzleFrame(PicSaw.this, chooser.getSelectedFile().toURI());
                puzzle.setVisible(true);

                PicSaw.this.setVisible(false);
            }
        }

        public void menuDeselected(MenuEvent e) {}

        public void menuCanceled(MenuEvent e) {}
    }
}
