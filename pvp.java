package PlantsVsZombies;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/*
 * 
 * 22.5x14.5
 * 
 * 1000x650
 * 
 * 44.5
 * 
 * 311, 124
 * 
 * width: 66
 * height: 89
 * 
 */

// The main class that runs the game
public class pvp implements ActionListener {
    JFrame frame;
    static int panW = 1000, panH = 650;
    JPanel mainPanel;
    IntroPanel introPanel;
    GamePanel gamepanel;
    CardLayout cardLayout;
    Image sun; 
    Timer timer; 
    Sun sunObj;
    boolean isFalling = false;
    int sunCount = 0;
    int mx=-100, my=-100;
    long frameCtr=0;
    //-1 is no plant
    //0 is peashooter
    //1 is walnut
    int chosenPlant = -1;
    Rectangle peashooterCard = new Rectangle(40, 20, 100, 50);
    Image peashooterCardImg = loadImage("/peashooterCard.png");
    Rectangle walnutCard = new Rectangle(160, 20, 100, 50);
    Image walnutCardImg = loadImage("/walnutCard.png");
    ArrayList<Plant> plants = new ArrayList();
    
    private class MouseHandler extends MouseAdapter{
        @Override   
        public void mousePressed(MouseEvent e){
            if (sunObj.contains(e.getPoint())){
                sunCount+=25;
                respawnSun();
            }
            else {
            	if(peashooterCard.contains(e.getPoint())) {
            		chosenPlant=0;
            	}
            	else if(walnutCard.contains(e.getPoint())) {
            		chosenPlant=1;
            	}
            	else {
            		mx=e.getX();
            		my=e.getY();
            		mx-=311;
            		my-=124;
            		if(mx>=0&&my>=0) {
            			System.out.println(mx + " " + my);
            			mx=mx/66;
            			my=my/89;
            			System.out.println(mx + " " + my);
            			if(mx<9&&my<5) {
            				mx*=66;
            				my*=89;
            				System.out.println(mx + " " + my);
            				if(chosenPlant==0) {
            					plants.add(new Peashooter(100, mx+2+311, my+2+124, 10, 10, "/Peashooter.png", "Peashooter"));
            					chosenPlant=-1;
            				}
            				else if(chosenPlant==1){
            					plants.add(new Walnut(100, mx+2+311, my+2+124, 10, 10, "/Walnut.png", "Walnut"));
            					chosenPlant=-1;
            				}
            			}
            		}
            	}
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
        int rand = (int)(Math.random() * 500);
        return rand == 0;
    }

    /**
     * Checks if the sun is out of bounds
     * @return true if the sun is out of bounds, false otherwise
     */
    public boolean checkBoundaries(){
        return sunObj.y > frame.getHeight();
    }
    
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
        
        sun = loadImage("/SunImg.png");
        sunObj = new Sun(frame.getWidth()/2, -55, 50, 50);
        
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
        
        mainPanel.addMouseListener(new MouseHandler());
        
        
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

        Image gameBackground = loadImage("/Background.jpg");
        gamepanel = new GamePanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), null);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                sunObj.draw(g2, sun);
                g2.setPaint(new Color(255,255,255));
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                g2.drawString("Sun Count: " + sunCount, 40, 40);
                g2.drawImage(peashooterCardImg, peashooterCard.x, peashooterCard.y, peashooterCard.width, peashooterCard.height, null);
                g2.drawImage(walnutCardImg, walnutCard.x, walnutCard.y, walnutCard.width, walnutCard.height, null);
                frameCtr++;
                for(Plant p : plants) {
                	if(p.getType().equals("Peashooter")) {
                		Peashooter ps = (Peashooter)p;
                		g.drawImage(ps.getimg(),							
            					ps.getX()+ps.getAdjustment(), ps.getY()+20, ps.getX()+60+ps.getAdjustment(), ps.getY() + 60,  //destination
            					ps.getImgX(), ps.getImgY(), ps.getImgX()+ps.getW(), ps.getImgY()+ps.getH(),						
            					null);
                	}
                	else if(p.getType().equals("Walnut")) {
                		Walnut w = (Walnut)p;
                		g.drawImage(w.getimg(),							
            					w.getX()+w.getAdjustment(), w.getY()+20, w.getX()+60+w.getAdjustment(), w.getY() + 60,  //destination
            					w.getImgX(), w.getImgY(), w.getImgX()+w.getW(), w.getImgY()+w.getH(),						
            					null);
                	}
                	if(frameCtr%20==0) {
                		p.incrementFrame();
                		frameCtr=0;
                	}
                }
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
    Image loadImage(String filename) {
		Image image = null;	
		java.net.URL imageURL = this.getClass().getResource(filename);
		if (imageURL != null) {
			ImageIcon icon = new ImageIcon(imageURL);
			image = icon.getImage();
		} else { 
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename , "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
}
