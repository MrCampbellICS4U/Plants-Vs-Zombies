
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroButton {

    StartButton startButton;
    String img1;
    String img2;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IntroButton();
        });
    }

    IntroButton() {
        JFrame frame = new JFrame("IntroButton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        img1 = StartButton.nonhover();
        img2 = StartButton.hover();
        startButton = new StartButton(img1, img2, 100, 100, 221, 61);

        frame.add(new DrawPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class DrawPanel extends JPanel {
        DrawPanel() {
            setPreferredSize(new Dimension(800, 600));
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (startButton.contains(e.getPoint())) {
                        System.out.println("Start button pressed");
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            Point mousePosition = getMousePosition();
            if (mousePosition != null && startButton.contains(mousePosition)) {
                Image img = loadImage(img2);
                g2.drawImage(img, startButton.x, startButton.y, startButton.width, startButton.height, null);
            } else {
                Image img = loadImage(img1);
                g2.drawImage(img, startButton.x, startButton.y, startButton.width, startButton.height, null);
            }

        }
    }

    Image loadImage(String filename) {
        Image image = null;
        java.net.URL imageURL = this.getClass().getResource(filename);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            image = icon.getImage();
        } else {
            JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }
}
