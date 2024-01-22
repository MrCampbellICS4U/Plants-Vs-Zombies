package PlantsVsZombies;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


// The main class that runs the game
public class PvZ implements ActionListener {
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
    int sunCount = 10000;
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
    ArrayList<Plant> plants = new ArrayList <Plant> ();
    ArrayList<Zombie> zombies = new ArrayList <Zombie>();
    int spawnZombieCtr=0;
    boolean hasPlant[][] = new boolean[9][5];
    boolean hasZombie[] = new boolean[5];
    int shootCtr=0;
    boolean playLose=true;
    boolean playWin=true;
    int[] waves = {2, 3, 4, 5, 10, 12, 1};
    int zombieWaveCtr=6;
    boolean wavePlaying = true;
    int waveFrameCtr=0;
    Clip mainSound;
    
    // Create the Projectile arraylist: 
    ArrayList <Projectile> projectiles = new ArrayList<Projectile>();
    
    void spawnZombie() {
    	if (!checkEndGame()){
            int rand = (int)(Math.random()*5);
            int zombieY = rand*89+124;
            zombies.add(new NormalZombie(50, 1000, zombieY, 10, 5, 100, "/Zombies.png"));
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
            	if(sunCount>=100&&peashooterCard.contains(e.getPoint())) {
            		chosenPlant=0;
            	}
            	else if(sunCount>=50&&walnutCard.contains(e.getPoint())) {
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
            			if(chosenPlant!=-1&&mx<9&&my<5&&!hasPlant[mx][my]) {
            				hasPlant[mx][my]=true;
            				//System.out.println(mx + " " + my);
            				mx*=66;
            				my*=89;
            				if(chosenPlant==0) {
            					plants.add(new Peashooter(1000, mx+2+311, my+2+124, 10, 10, "/Peashooter.png", "Peashooter"));
                                chosenPlant=-1;
                                sunCount-=100;
                                Music.playMusic("soundRes/plant.wav", false);
            				}
            				else if(chosenPlant==1){
            					plants.add(new Walnut(10000, mx+2+311, my+2+124, 10, 10, "/Walnut.png", "Walnut"));
            					chosenPlant=-1;
            					sunCount-=50;
            					Music.playMusic("soundRes/plant2.wav", false);
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
    
    public void resetZombieArray() {
    	for(int i = 0; i < 5; i++) {
    		hasZombie[i]=false;
    	}
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PvZ();
            }
        });
    }

    PvZ() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainPanel = new JPanel();
        cardLayout = new CardLayout();

        Music.playMusic("soundRes/Crazy_Dave.wav", true);
        mainPanel.setLayout(cardLayout);
        for(int i = 0; i < waves[zombieWaveCtr]; i++) {
        	spawnZombie();
        }
        zombieWaveCtr++;
        
        sun = loadImage("/SunImg.png");
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

                for (int i = projectiles.size()-1; i >= 0; i--){
                    Projectile p = projectiles.get(i);
                    // Check if the projectile is out of bounds
                    if (p.getX() > frame.getWidth()){
                        projectiles.remove(p);
                        break;
                    }
                    // Check if the projectile hits a zombie
                    for (int j = zombies.size()-1; j >= 0; j--){
                        Zombie z = zombies.get(j);
                        if (isOverlapping(p.getBounds(), z.getBounds())){
                            z.reduceHealth(p.getDamage());
                            projectiles.remove(p);
                            break;
                        }
                    }
                }
                
                if(checkEndGame()) {
                	won=false;
                	cardLayout.show(mainPanel, "end");
                }
                
                for(int i = zombies.size()-1; i>=0; i--) {
                	Zombie z = zombies.get(i);
                	if(z.getHealth()<0)zombies.remove(i);
                }
                for(int i = plants.size()-1; i>=0; i--) {
                	Plant p = plants.get(i);
                	if(p.getHealth()<0) {
                		hasPlant[(p.getX()-313)/66][(p.getY()-126)/89]=false;
                		plants.remove(i);
                	}
                }
                // Move the projectiles
                for (Projectile p : projectiles){
                    p.move();
                }
                
                if(zombies.size()==0) {
                	if(zombieWaveCtr==7) {
                		//load winning screen;
                		won=true;
                	}
                	else {
                		for(int i = 0; i < waves[zombieWaveCtr]; i++) {
                			spawnZombie();
                		}
                		wavePlaying=true;
                		zombieWaveCtr++;
                		Music.playMusic("soundRes/siren.wav", false);
                	}
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
                g2.setPaint(Color.BLACK);
                g2.setFont(new Font("Monospaced", Font.BOLD, 20));
                int sunCountDigit = Integer.toString(sunCount).length();
                g2.drawString(Integer.toString(sunCount), 520-(sunCountDigit-1)*5, 118);
                g2.drawImage(peashooterCardImg, peashooterCard.x, peashooterCard.y, peashooterCard.width, peashooterCard.height, null);
                g2.drawImage(walnutCardImg, walnutCard.x, walnutCard.y, walnutCard.width, walnutCard.height, null);
                frameCtr++;
                spawnZombieCtr++;
                for(Plant p : plants) {
                	if(p.getType().equals("Peashooter")) {
                		Peashooter ps = (Peashooter)p;
                		if(ps.getShootCtr()%3!=0) {
                			ps.setAttack(false);
                		}
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
                resetZombieArray();
                for(Zombie z : zombies) {
                	int posY = (z.getY()-124)/89;
                	if(z.getX()<1000) {
                		hasZombie[posY]=true;
                	}
                }
                for(Plant p : plants) {
                	if(p.getType().equals("Peashooter")) {
	                	Peashooter ps = (Peashooter)p;
	                	int posY=(ps.getY()-126)/89;
	                	if(hasZombie[posY]) {
	                		ps.setAttack(true);
	                	}
	                	else {
	                		ps.setAttack(false);
	                	}
                	}
                }
                // Checks if the plant is attacking or not
                for (Plant p : plants){
                    if (p.getType().equals("Peashooter")){
                        Peashooter ps = (Peashooter)p;
                        if (ps.getIsAttacking() == true&&p.getFrame()==0&&frameCtr==1){
                        	if(ps.getShootCtr()%3==0) {
                            // Create a new projectile
                        		projectiles.add(new Projectile(10, ps.getX()+ps.getAdjustment()+60, ps.getY()+20, 5));
                        		Music.playMusic("soundRes/PeashooterSound.wav", false);
                        		ps.setShootCtr(0);
                        	}
                        	ps.setShootCtr(ps.getShootCtr()+1);
                        }
                    }
                }  
                if(spawnZombieCtr%10==0) {
                	for(Zombie z : zombies) {
                		z.incrementFrame();
                	}
                }
                if(spawnZombieCtr%40==0) {
                	for(Zombie z : zombies) {
                		//System.out.println(z.getX());
                		z.move();
                	}
                	spawnZombieCtr=0;
                }
                for(Zombie z : zombies) {
                	NormalZombie nz = (NormalZombie)z;
                	int posX=(nz.getX()-311)/66, posY=(nz.getY()-124)/89;
                	if(posX>=0&&posY>=0&&posX<9&&posY<5&&hasPlant[posX][posY]) {
                		nz.setMove(false);
                		nz.setMovementSpeed(0);
                		//System.out.println(posX+" "+posY);
                		for(Plant p : plants) {
                			if((p.getX()-313)/66==posX&&(p.getY()-126)/89==posY) {
                				//System.out.println("!");
                				p.reduceHealth(nz.getDamage());
                			}
                		}
                	}
                	else if(posX>=0&&posY>=0&&posX<9&&posY<5) {
                		nz.setMove(true);
                	}
                	else{
                		nz.setMovementSpeed((int)(Math.random()*4+5));
                		//nz.setMovementSpeed(100);
                		nz.setMove(true);
                	}
                	g.drawImage(nz.getimg(),							
        					nz.getX(), nz.getY(), nz.getX()+50, nz.getY()+80,  //destination
        					nz.getImgX(), nz.getImgY(), nz.getImgX()+nz.getW(), nz.getImgY()+nz.getH(),						
        					null);
                }

                // Draw the projectiles
                for (Projectile p : projectiles){
                    g.drawImage(p.getimg(), p.getX(), p.getY(), null);
                }
                sunObj.draw(g2, sun);
                if(wavePlaying) {
                	if(waveFrameCtr<100) {
                		waveFrameCtr++;
                		String waveFilePath = "/Wave"+Integer.toString(zombieWaveCtr)+".png";
                		Image waveImg = loadImage(waveFilePath);
                		if(zombieWaveCtr!=7) {
                			g.drawImage(waveImg, 300, 200, 400, 200, null);
                		}
                		else {
                			g.drawImage(waveImg, 100, 200, 900, 200, null);
                		}
                	}
                	else {
                		waveFrameCtr=0;
                		wavePlaying=false;
                	}
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
                mainSound=Music.playMusic("soundRes/Grasswalk.wav", true);
                Music.playMusic("soundRes/siren.wav", false);
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

  //Image endBackground = loadImage("/EndImage.png");
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
                g2.drawImage(loadImage("/WinningScreen.jpg"), 0, 0, getWidth(), getHeight(), null);
                if(playWin) {
                	Music.playMusic("soundRes/winmusic.wav", false);
                	mainSound.stop();
                	playWin=false;
                }
                exitButton.x = 450;
                exitButton.y = 575;
                g2.drawImage(loadImage("/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
            } else{
                g2.drawImage(loadImage("/EndScreen.jpg"), 0, 0, getWidth(), getHeight(), null);
                g2.drawImage(loadImage("/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
                g2.drawImage(loadImage("/RetryButton.png"), retryButton.x, retryButton.y, retryButton.width, exitButton.height, null);
                if(playLose) {
            		Music.playMusic("soundRes/losemusic.wav", false);
            		mainSound.stop();
            		playLose=false;
            	}
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

    public boolean checkEndGame(){
    	if(won)return true;
        for (Zombie z : zombies){
            if (z.getX() < 250){
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
        zombieWaveCtr=0;
        cardLayout.show(mainPanel, "game");
        mainSound=Music.playMusic("soundRes/Grasswalk.wav", true);
        playLose=true;
        playWin=true;
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