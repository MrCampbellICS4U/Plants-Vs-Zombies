
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

    static Clip clip;

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

        introPanel = new IntroPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        introPanel.setBorder(BorderFactory.createEmptyBorder(500, 10, 100, 10));

        playMusic("soundRes/Crazy_Dave.wav");

        JButton startgame = new JButton("START GAME");
        startgame.setBackground(new Color(178, 210, 53, 255));
        startgame.setFont(new Font("Condensed Bold", Font.BOLD, 20));
        startgame.setAlignmentX(0.5f);
        Dimension size = new Dimension(175, 90);
        startgame.setMaximumSize(size);
        startgame.setMinimumSize(size);
        introPanel.add(startgame);

        mainPanel.add(introPanel, "intro");

        gamepanel = new GamePanel();

        mainPanel.add(gamepanel, "game");

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        startgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "game");
                playMusic("soundRes/Grasswalk.wav");

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
        Image introBackground = loadImage("/Main_Menu.png");

        IntroPanel() {
            setPreferredSize(new Dimension(panW, panH));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(introBackground, 0, 0, getWidth(), getHeight(), null);
        }

    }

    class GamePanel extends JPanel {
        Image gameBackground = loadImage("/EMPTYLANW.jpg");
        Image sun = loadImage("/Sun.png");

        GamePanel() {
            setPreferredSize(new Dimension(panW, panH));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // g2.setFont(new Font("Arial", Font.BOLD, 30));
            // g2.drawString("the sun amount" + AmountSun, 20, 20);

            g2.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), null);
            g2.drawImage(sun, 496, 22, 60, 60, this);

        }
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

    public static void playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip newclip = AudioSystem.getClip();
                newclip.open(audioInput);

                if (clip != null && clip.isRunning()) {
                    clip.stop();
                    clip.close();
                }

                clip = newclip;

                // Start playing the new clip
                clip.start();
                clip.loop(clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error");
        }
    }
}
