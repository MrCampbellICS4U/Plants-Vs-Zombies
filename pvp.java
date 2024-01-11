
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.BufferedImage;

// The main class that runs the game
public class pvp implements ActionListener {
    JFrame frame;
    static int panW = 1000, panH = 650;
    JPanel mainPanel;
    IntroPanel introPanel;
    GamePanel gamepanel;
    CardLayout cardLayout;
    Timer timer;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new pvp();
            }
        });
    }

    pvp() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        Image introBackground = loadImage("/Main_Menu.png");
        introPanel = new IntroPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(introBackground, 0, 0, getWidth(), getHeight(), null);
            }
        };
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        introPanel.setBorder(BorderFactory.createEmptyBorder(500, 10, 100, 10));

        JButton startgame = new JButton("START GAME");
        startgame.setFont(new Font("Condensed Bold", Font.BOLD, 20));
        startgame.setAlignmentX(0.5f);
        Dimension size = new Dimension(175, 90);
        startgame.setMaximumSize(size);
        startgame.setMinimumSize(size);
        introPanel.add(startgame);

        mainPanel.add(introPanel, "intro");

        Image gameBackground = loadImage("/EMPTYLANW.jpg");
        gamepanel = new GamePanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), null);
            }
        };

        mainPanel.add(gamepanel, "game");

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        startgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "game");
            }
        });

        timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    class IntroPanel extends JPanel {
        IntroPanel() {
            setPreferredSize(new Dimension(panW, panH));
        }
    }

    class GamePanel extends JPanel {

    }

    /**
     * Loads an image
     * 
     * @param name the name of the image
     * @return the image
     */
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
