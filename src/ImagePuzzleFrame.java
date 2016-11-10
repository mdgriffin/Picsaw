/**
 * Defines an Image Jigsaw puzzle
 */
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class ImagePuzzleFrame extends JFrame {
    PicSaw parent;
    JMenu exitBtn;

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

        exitBtn = new JMenu("Exit");

        exitBtn.addMenuListener(new ExitButtonMenuListener());

        mainMenuBar.add(exitBtn);

        // loading the image
        java.net.URL imgURL = getClass().getResource(imageSrc);
        ImageIcon imgIco = new ImageIcon(new ImageIcon(imgURL).getImage());

        JLabel imageLabel = new JLabel(imgIco);

        mainPane.add(imageLabel);

        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private class ExitButtonMenuListener implements  MenuListener {
        public void menuSelected(MenuEvent e) {
            int choice =  JOptionPane.showConfirmDialog(
                    ImagePuzzleFrame.this,
                    "Are you sure you want to exit? You will lose any progress you have made with the puzzle",
                    "Please Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                parent.setVisible(true);
                ImagePuzzleFrame.this.dispose();
            } else {
                exitBtn.setSelected(false);
            }
        }

        public void menuDeselected(MenuEvent e) {

        }

        public void menuCanceled(MenuEvent e) {

        }
    }
}
