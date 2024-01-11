import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.BufferedImage;

// The main class that runs the game
public class pvp implements ActionListener {
    JFrame frame;
    static int panW = 1300, panH = 800;
    JPanel mainPanel;
    IntroPanel introPanel;
    GamePanel gamepanel;
    CardLayout cardLayout;

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

        BufferedImage introBackground = loadImage("Main_Menu.png");
        introPanel = new IntroPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(introBackground, 0, 0, getWidth(), getHeight(), null);
            }
        };
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        introPanel.setBorder(BorderFactory.createEmptyBorder(650, 10, 100, 10));

        JButton startgame = new JButton("START GAME");
        startgame.setFont(new Font("Condensed Bold", Font.BOLD, 20));
        startgame.setAlignmentX(0.5f);
        Dimension size = new Dimension(175, 90);
        startgame.setMaximumSize(size);
        startgame.setMinimumSize(size);
        introPanel.add(startgame);

        mainPanel.add(introPanel, "intro");

        BufferedImage gameBackground = loadImage("EMPTYLANW.jpg");
        gamepanel = new GamePanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), null);
            }
        };

        mainPanel.add(gamepanel, "game");

        startgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "game");
            }
        });
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
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
    static BufferedImage loadImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(name));
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "An image failed to load: " + name, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return img;
    }
}
