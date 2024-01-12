import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.*;

// This class is the testing class for the Sun class.

public class FallingSun {
    // Instance variables
    JFrame frame;
    DrawingPanel mainPanel; 
    BufferedImage sun; 
    Timer timer; 
    Sun sunObj;

    // Checks if the sun is falling or not. 
    boolean isFalling = false;
    int sunCount = 0;

    public static void main (String [] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FallingSun();
            }
        });    
    }

    /**
     * Constructor for objects of class FallingSun
     */
    public FallingSun() {
        frame = new JFrame("Sun");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        sun = loadImage("ImagesFolder/SunImg.png");
        sunObj = new Sun(frame.getWidth()/2, -55, 50, 50);
        mainPanel = new DrawingPanel();
        frame.add(mainPanel);
        mainPanel.addMouseListener(new MouseHandler());

        timer = new Timer(5, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // Check if the sun should fall or not
                if (checkFall() == true){
                    isFalling = true;
                }
                // If the sun is falling, move it down
                if (isFalling == true){
                    sunObj.move();
                }
                // If the sun is out of bounds, respawn it
                if (checkBoundaries() == true){
                    respawnSun();
                }
                mainPanel.repaint();
            }
        });
        timer.start();
    }

    private class MouseHandler extends MouseAdapter{
        @Override   
        public void mousePressed(MouseEvent e){
            if (sunObj.contains(e.getPoint())){
                sunCount+=25;
                respawnSun();
            }
        }
    }

    /**
     * Respawns the sun at the top of the screen
     */
    public void respawnSun(){
        isFalling = false;
        sunObj.setYY(-55);
        sunObj.y = (int)sunObj.getYY();
        sunObj.x = (int)(Math.random() * (frame.getWidth()-50));
    }

    /**
     * Checks if the sun should fall or not
     * @return true if the sun should fall, false otherwise
     */
    public boolean checkFall(){
        int rand = (int)(Math.random() * 200);
        return rand == 0;
    }

    /**
     * Checks if the sun is out of bounds
     * @return true if the sun is out of bounds, false otherwise
     */
    public boolean checkBoundaries(){
        return sunObj.y > frame.getHeight();
    }

    private class DrawingPanel extends JPanel{
        // Instance variables
        int panW, panH; 
        /**
         * Constructor to setup DrawingPanel
         */
        public DrawingPanel(){
            panW = frame.getWidth();
            panH = frame.getHeight();
            this.setPreferredSize(new Dimension(panW, panH));
            this.setBackground(Color.DARK_GRAY);
        }
        /**
         * Draws the sun and the sun count
         */
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            sunObj.draw(g2, sun);
            g2.setPaint(new Color(255,255,255));
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("Sun Count: " + sunCount, 40, 40);
        }
    }

    /**
     * Loads an image from a file
     * @param filename the name of the file
     * @return the image
     */
    static BufferedImage loadImage(String filename){
        BufferedImage img = null; 
        try{
            img = ImageIO.read(new File(filename));
        } catch(IOException e){
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return img; 
    } 
}
