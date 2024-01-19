// package PlantsVsZombies;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    EndPanel endpanel;
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
    Image peashooterCardImg = loadImage("ImagesFolder/peashooterCard.png");
    Rectangle walnutCard = new Rectangle(160, 20, 100, 50);
    Image walnutCardImg = loadImage("ImagesFolder/walnutCard.png");
    ArrayList<Plant> plants = new ArrayList <Plant> ();
    ArrayList<Zombie> zombies = new ArrayList <Zombie>();
    int spawnZombieCtr=0;
    boolean hasPlant[][] = new boolean[9][5];

    // Create the Projectile arraylist: 
    ArrayList <Projectile> projectiles = new ArrayList<Projectile>();
    
    // Music:
    static Clip clip;
    void spawnZombie() {
        if (!checkLoss()){
            int rand = (int)(Math.random()*5);
            int zombieY = rand*89+124;
            zombies.add(new NormalZombie(100, 1000, zombieY, 10, 10, 3, "ImagesFolder/Zombies.png"));
        }
    }

    // The Buttons:
    ExitButton exitButton;
    RetryButton retryButton;

    // Boolean to check if you won or not
    boolean won = false;
    
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
            			mx=mx/66;
            			my=my/89;
            			if(mx<9&&my<5&&!hasPlant[mx][my]) {
            				hasPlant[mx][my]=true;
            				mx*=66;
            				my*=89;
            				if(chosenPlant==0) {
            					plants.add(new Peashooter(100, mx+2+311, my+2+124, 10, 10, "ImagesFolder/Peashooter.png", "Peashooter"));
            					
                                // TODO: FOR TESTING PURPOSES ONLY, REMOVE LATER
                                for (Plant p : plants){
                                    if (p.getType().equals("Peashooter")){
                                        Peashooter ps = (Peashooter)p;
                                        ps.setAttack(true);
                                    }
                                }
                                chosenPlant=-1;
            				}
            				else if(chosenPlant==1){
            					plants.add(new Walnut(100, mx+2+311, my+2+124, 10, 10, "ImagesFolder/Walnut.png", "Walnut"));
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
        
        playMusic("soundRes/Crazy_Dave.wav");
        mainPanel.setLayout(cardLayout);
        
        sun = loadImage("ImagesFolder/SunImg.png");
        sunObj = new Sun(frame.getWidth()/2, -55, 50, 50);
        for(int i = 0; i < 9; i++) {
        	for(int j = 0; j < 5; j++) {
        		hasPlant[i][j]=false;
        	}
        }  

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

                // Checks if the plant is attacking or not
                for (Plant p : plants){
                    if (p.getType().equals("Peashooter")){
                        Peashooter ps = (Peashooter)p;
                        if (ps.getIsAttacking() == true){
                            // Create a new projectile
                            projectiles.add(new Projectile(10, ps.getX()+ps.getAdjustment()+60, ps.getY()+20, 5));
                            ps.setAttack(false);
                        }
                    }
                }  

                for (int i = 0; i < projectiles.size(); i++){
                    Projectile p = projectiles.get(i);
                    // Check if the projectile is out of bounds
                    if (p.getX() > frame.getWidth()){
                        projectiles.remove(p);
                        break;
                    }
                    // Check if the projectile hits a zombie
                    for (int j = 0; j < zombies.size(); j++){
                        Zombie z = zombies.get(j);
                        if (isOverlapping(p.getBounds(), z.getBounds())){
                            z.reduceHealth(p.getDamage());
                            projectiles.remove(p);
                            break;
                        }
                    }
                }  

                if (checkLoss()){
                    won = false;
                    cardLayout.show(mainPanel, "end");
                } 

                // Move the projectiles
                for (Projectile p : projectiles){
                    p.move();
                }

                mainPanel.repaint();
            }
        });
        
        timer.start();
        
        mainPanel.addMouseListener(new MouseHandler());
        
        Image introBackground = loadImage("ImagesFolder/Main_Menu.png");
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

        Image gameBackground = loadImage("ImagesFolder/Background.jpg");
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
                spawnZombieCtr++;
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
                if(spawnZombieCtr%10==0) {
                	for(Zombie z : zombies) {
                		z.incrementFrame();
                	}
                }
                if(spawnZombieCtr%40==0) {
                	for(Zombie z : zombies) {
                		z.move();
                	}
                }
                if(spawnZombieCtr%200==0) {
            		spawnZombie();
            		spawnZombieCtr=0;
            	}
                for(Zombie z : zombies) {
                	NormalZombie nz = (NormalZombie)z;
                	g.drawImage(nz.getimg(),							
        					nz.getX(), nz.getY(), nz.getX()+50, nz.getY()+80,  //destination
        					nz.getImgX(), nz.getImgY(), nz.getImgX()+nz.getW(), nz.getImgY()+nz.getH(),						
        					null);
                }

                // Draw the projectiles
                for (Projectile p : projectiles){
                    g.drawImage(p.getimg(), p.getX(), p.getY(), null);
                }
            }
        };

        mainPanel.add(gamepanel, "game");

        endpanel = new EndPanel();
        endpanel.addMouseListener(new EndPanelMouseHandler());
        mainPanel.add(endpanel, "end");

        startgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "game");
                playMusic("soundRes/Grasswalk.wav");
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

    class EndPanel extends JPanel {
        EndPanel() {
            exitButton = new ExitButton(550, 500, 100, 50);
            retryButton = new RetryButton(350, 500, 100, 50);
        }

        @Override  
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            if (won){
                g2.drawImage(loadImage("ImagesFolder/WinningScreen.jpg"), 0, 0, getWidth(), getHeight(), null);
                exitButton.x = 450;
                exitButton.y = 575;
                g2.drawImage(loadImage("ImagesFolder/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
            } else{
                g2.drawImage(loadImage("ImagesFolder/EndScreen.jpg"), 0, 0, getWidth(), getHeight(), null);
                g2.drawImage(loadImage("ImagesFolder/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
                g2.drawImage(loadImage("ImagesFolder/RetryButton.png"), retryButton.x, retryButton.y, retryButton.width, exitButton.height, null);
            }
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
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename , "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

    public boolean isOverlapping(Rectangle rect1, Rectangle rect2) {
        return rect1.x < rect2.x + rect2.width && rect1.x + rect1.width > rect2.x && rect1.y < rect2.y + rect2.height && rect1.y + rect1.height > rect2.y;
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

    public boolean checkLoss(){
        for (Zombie z : zombies){
            if (z.getX() < 275){
                return true;
            }
        }
        return false;
    }

    private void resetGame(){
        sunCount = 0;
        respawnSun();
        mx=-100;
        my=-100;
        frameCtr=0;
        chosenPlant = -1;
        plants.clear();
        zombies.clear();
        spawnZombieCtr=0;
        for(int i = 0; i < 9; i++) {
        	for(int j = 0; j < 5; j++) {
        		hasPlant[i][j]=false;
        	}
        }
        projectiles.clear();
        cardLayout.show(mainPanel, "game");
        playMusic("soundRes/Grasswalk.wav");
    }

    private class EndPanelMouseHandler extends MouseAdapter{
        @Override   
        public void mousePressed(MouseEvent e){
            if (exitButton.contains(e.getPoint())){
                System.exit(0);
            }
            else if (retryButton.contains(e.getPoint())){
                resetGame();
            }
        }
    }
}
