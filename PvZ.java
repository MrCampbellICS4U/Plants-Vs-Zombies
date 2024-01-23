// Plants vs Zombies game; ICS 4U1; Jan 2024; Rishi Shah, Kevin Chen, Andy Xu

/* This program is a Plants vs Zombies game. 
The player must plant plants to defend their house from zombies. 
The player wins if they survive all the waves of zombies. 
The player loses if a zombie reaches their house. */
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


// The main class of the game, runs PvZ
public class PvZ implements ActionListener {
    // Setting up the JFrame
    JFrame frame;
    static int panW = 1000, panH = 650;

    // Panels needed for the card layout
    JPanel mainPanel;
    IntroPanel introPanel;
    GamePanel gamepanel;
    EndPanel endpanel;
    CardLayout cardLayout;
    
    // The start button
    StartButton startButton;
    String img1;
    String img2;

    // Timer for the game
    Timer timer; 

    // Setting up the sun, checking if it falls, and creating the sun currency
    Image sun; 
    Sun sunObj;
    boolean isFalling = false;
    int sunCount = 10000;

    // Mouse coordinates
    int mx=-100, my=-100;
    // Frame counter
    long frameCtr=0;

    //-1 is no plant
    //0 is peashooter
    //1 is walnut
    int chosenPlant = -1;

    // Creating the rectangles for the cards and the images for the cards
    Rectangle peashooterCard = new Rectangle(40, 20, 100, 50);
    Image peashooterCardImg = loadImage("/peashooterCard.png");
    Rectangle walnutCard = new Rectangle(160, 20, 100, 50);
    Image walnutCardImg = loadImage("/walnutCard.png");

    // Creating the arraylists for the plants and zombies
    ArrayList<Plant> plants = new ArrayList <Plant> ();
    ArrayList<Zombie> zombies = new ArrayList <Zombie>();

    // Counting the number of zombies spawned 
    int spawnZombieCtr=0;

    // The array that checks if there is a plant in a certain spot
    boolean hasPlant[][] = new boolean[9][5];
    // The array that checks if there is a zombie in a certain spot
    boolean hasZombie[] = new boolean[5];
    // The counter for the shooting of the peashooter
    int shootCtr=0;

    // The array for the number of zombies in each wave
    int[] waves = {2, 3, 4, 5, 10, 12, 15};

    // The counter for the number of waves
    int zombieWaveCtr=0;
    boolean wavePlaying = true;
    int waveFrameCtr=0;

    // Create the Projectile arraylist: 
    ArrayList <Projectile> projectiles = new ArrayList<Projectile>();  

    boolean playLose=true;
    boolean playWin=true;
    Clip mainSound;

    // The Buttons:
    ExitButton exitButton;
    RetryButton retryButton;

    // Boolean to check if you won or not
    boolean won = false;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PvZ();
            }
        });
    }

    /**
     * Constructor for the PvZ class
     */
    PvZ() {
        // Setting up the JFrame
        frame = new JFrame("Plants vs Zombies");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Setting up the main panel and the card layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.addMouseListener(new MouseHandler());
        Music.playMusic("soundRes/Crazy_Dave.wav", true);
        
        // Creating the intropanel to be added onto the main panel via cardlayout
        introPanel = new IntroPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        introPanel.setBorder(BorderFactory.createEmptyBorder(500, 10, 100, 10));

        // Spawning the zombies for the first wave
        for(int i = 0; i < waves[zombieWaveCtr]; i++) {
        	spawnZombie();
        }
        zombieWaveCtr++;
        
        // Loading the sun image and creating the sun object
        sun = loadImage("/SunImg.png");
        sunObj = new Sun(frame.getWidth()/2, -55, 50, 50);

        // Setting up the boolean array for the plants
        for(int i = 0; i < 9; i++) {
        	for(int j = 0; j < 5; j++) {
        		hasPlant[i][j]=false;
        	}
        }  


        // Adding the intropanel to the main panel
        mainPanel.add(introPanel, "intro");

        // Creating the gamepanel and adding to the mainpanel
        gamepanel = new GamePanel();
        mainPanel.add(gamepanel, "game");

        // Creating the endpanel and added to the mainpanel
        endpanel = new EndPanel();
        endpanel.addMouseListener(new EndPanelMouseHandler());
        mainPanel.add(endpanel, "end");

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

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
                            // Reduce the health of the zombie and remove the projectile
                            z.reduceHealth(p.getDamage());
                            projectiles.remove(p);
                            break;
                        }
                    }
                }
                
                // Check if the game is over
                if(checkLoss()) {
                	won=false;
                	cardLayout.show(mainPanel, "end");
                }
                
                // Remove the dead zombies and plants
                for(int i = zombies.size()-1; i>=0; i--) {
                	Zombie z = zombies.get(i);
                	if(z.getHealth()<0)zombies.remove(i);
                }

                // Remove the dead plants
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
                
                // Check if the zombies are all dead
                if(zombies.size()==0) {
                	if(zombieWaveCtr==7) {
                		//load winning screen
                		won=true;
                	}
                	else {
                        // Spawn the next wave of zombies
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
    }

    /**
     * Spawns a zombie after making sure that the game isn't over
     */
    void spawnZombie() {
    	if (!checkLoss()){
            int rand = (int)(Math.random()*5);
            int zombieY = rand*89+124;
            zombies.add(new NormalZombie(50, 1000, zombieY, 10, 5, 100, "/Zombies.png"));
        }
    }
    
    // MouseHandler class for the gamepanel
    private class MouseHandler extends MouseAdapter{
        @Override   
        public void mousePressed(MouseEvent e){
            // Check if the sun is clicked
            if (sunObj.contains(e.getPoint())){
                sunCount+=25;
                respawnSun();
            }
            else {
                // Check if the peashooter card is clicked and if the player has enough sun
            	if(sunCount>=100&&peashooterCard.contains(e.getPoint())) {
            		chosenPlant=0;
            	}
                // Check if the walnut card is clicked and if the player has enough sun
            	else if(sunCount>=50&&walnutCard.contains(e.getPoint())) {
            		chosenPlant=1;
            	}
            	else {
                    // If neither sun, peashooter card, or walnut card is clicked, check if the player is clicking on the grid
            		mx=e.getX();
            		my=e.getY();
            		mx-=311;
            		my-=124;
            		if(mx>=0&&my>=0) {
            			mx=mx/66;
            			my=my/89;
                        // If a plant is selected, and the position is valid, and no plant isn't present, plant the plant
            			if(chosenPlant!=-1&&mx<9&&my<5&&!hasPlant[mx][my]) {
            				hasPlant[mx][my]=true;
            				mx*=66;
            				my*=89;
                            // If the plant is a peashooter, add a peashooter to the arraylist of plants, and reduce the sun count, and play the peashooter sound
            				if(chosenPlant==0) {
            					plants.add(new Peashooter(1000, mx+2+311, my+2+124, 10, 10, "/Peashooter.png", "Peashooter"));
                                chosenPlant=-1;
                                sunCount-=100;
                                Music.playMusic("soundRes/plant.wav", false);
            				}
                            // If the plant is a walnut, add a walnut to the arraylist of plants, and reduce the sun count, and play the walnut sound
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
    
    /**
     * Resets the zombie array
     */
    public void resetZombieArray() {
    	for(int i = 0; i < 5; i++) {
    		hasZombie[i]=false;
    	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    // The intro panel class
    class IntroPanel extends JPanel {
        /**
         * Constructor for the IntroPanel class, which creates the start button and adds the mouse listeners
         */
        IntroPanel() {
            setPreferredSize(new Dimension(panW, panH));
            img1 = StartButton.nonhover();
            img2 = StartButton.hover();
            startButton = new StartButton(img1, img2, 390, 500, 221, 61);
            addMouseMotionListener(new MouseMotionAdapter(){
                @Override 
                public void mouseMoved(MouseEvent e){
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter(){
                @Override 
                public void mousePressed(MouseEvent e){
                    if (startButton.contains(e.getPoint())){
                        cardLayout.show(mainPanel, "game");
                        mainSound=Music.playMusic("soundRes/Grasswalk.wav", true);
                        Music.playMusic("soundRes/siren.wav", false);
                    }
                }
            });
        }
        /**
         * Paints the intro panel with the startbutton and background
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(loadImage("/Main_Menu.png"), 0, 0, getWidth(), getHeight(), null);
            Point mousePosition = getMousePosition();
            // If the mouse is on the start button, draw the hover image, otherwise draw the normal image
            if (mousePosition != null && startButton.contains(mousePosition)) {
                Image img = loadImage(img2);
                g2.drawImage(img, startButton.x, startButton.y, startButton.width, startButton.height, null);
            } else {
                Image img = loadImage(img1);
                g2.drawImage(img, startButton.x, startButton.y, startButton.width, startButton.height, null);
            }
        }
    }

    // The game panel class
    class GamePanel extends JPanel {
        /**
         * Constructor for the GamePanel class, which sets the preferred size of the panel
         */
        GamePanel() {
            setPreferredSize(new Dimension(panW, panH));
        }
        /**
         * Paints the game panel with the background, sun currency, card images, plants, zombies, and waves
         */
        @Override
        public void paintComponent(Graphics g) {
            // Draw the background
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(loadImage("/Background.jpg"), 0, 0, getWidth(), getHeight(), null);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Drawing the sun currency
            g2.setFont(new Font("Monospaced", Font.BOLD, 20));
            int sunCountDigit = Integer.toString(sunCount).length();
            g2.drawString(Integer.toString(sunCount), 520-(sunCountDigit-1)*5, 118);

            // Drawing the card images for the plants
            g2.drawImage(peashooterCardImg, peashooterCard.x, peashooterCard.y, peashooterCard.width, peashooterCard.height, null);
            g2.drawImage(walnutCardImg, walnutCard.x, walnutCard.y, walnutCard.width, walnutCard.height, null);

            // Increasing the frame counter and the zombie spawn counter
            frameCtr++;
            spawnZombieCtr++;
            for(Plant p : plants) {
                // If the plant is a peashooter, load the peashooter image
                if(p.getType().equals("Peashooter")) {
                    // Casting the plant to a peashooter
                    Peashooter ps = (Peashooter)p;
                    if(ps.getShootCtr()%3!=0) {
                        ps.setAttack(false);
                    }
                    g.drawImage(ps.getimg(),							
                            ps.getX()+ps.getAdjustment(), ps.getY()+20, ps.getX()+60+ps.getAdjustment(), ps.getY() + 60,  //destination
                            ps.getImgX(), ps.getImgY(), ps.getImgX()+ps.getW(), ps.getImgY()+ps.getH(),						
                            null);
                }
                // If the plant is a walnut, load the walnut image
                else if(p.getType().equals("Walnut")) {
                    // Casting the plant to a walnut
                    Walnut w = (Walnut)p;
                    g.drawImage(w.getimg(),							
                            w.getX()+w.getAdjustment(), w.getY()+20, w.getX()+60+w.getAdjustment(), w.getY() + 60,  //destination
                            w.getImgX(), w.getImgY(), w.getImgX()+w.getW(), w.getImgY()+w.getH(),						
                            null);
                }
                // Increment the peashooter sprite every 20 frames
                if(frameCtr%20==0) {
                    p.incrementFrame();
                    frameCtr=0;
                }
            }
            resetZombieArray();

            // Converts y to the specific grid position
            for(Zombie z : zombies) {
                int posY = (z.getY()-124)/89;
                if(z.getX()<1000) {
                    hasZombie[posY]=true;
                }
            }

            // Checks to see if there is a zombie in the same row as a peashooter
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
                        // Shoot a projectile if the plant is attacking and the shoot counter is divisible by 3
                        if(ps.getShootCtr()%3==0) {
                            // Create a new projectile and play the music of the plant firing
                            projectiles.add(new Projectile(10, ps.getX()+ps.getAdjustment()+60, ps.getY()+20, 5));
                            Music.playMusic("soundRes/PeashooterSound.wav", false);
                            // Set the shoot counter to 0 to make sure that there isn't a spam of projectiles
                            ps.setShootCtr(0);
                        }
                        // Increment the shoot counter
                        ps.setShootCtr(ps.getShootCtr()+1);
                    }
                }
            }  

            // Increment the frame of the zombie if the frame counter is divisible by 10
            if(spawnZombieCtr%10==0) {
                for(Zombie z : zombies) {
                    z.incrementFrame();
                }
            }
            // Move the zombie if the spawn zombie counter is divisible by 40
            if(spawnZombieCtr%40==0) {
                for(Zombie z : zombies) {
                    z.move();
                }
                // Set the spawn zombie counter to 0
                spawnZombieCtr=0;
            }

            for(Zombie z : zombies) {
                // Casting the zombie to a normal zombie
                NormalZombie nz = (NormalZombie)z;

                // Calculates the grid position of the zombie
                int posX=(nz.getX()-311)/66, posY=(nz.getY()-124)/89;

                // Checks for plant collision based off the same grid position as the zombie
                if(posX>=0&&posY>=0&&posX<9&&posY<5&&hasPlant[posX][posY]) {
                    // Stop the zombie from moving
                    nz.setMove(false);
                    nz.setMovementSpeed(0);
                    // Reduce the health of the plant accordingly
                    for(Plant p : plants) {
                        if((p.getX()-313)/66==posX&&(p.getY()-126)/89==posY) {
                            p.reduceHealth(nz.getDamage());
                        }
                    }
                }
                // If there is no plant in the zombie's grid position, continue moving
                else if(posX>=0&&posY>=0&&posX<9&&posY<5) {
                    nz.setMove(true);
                    if(nz.getMovementSpeed() == 0){
                        nz.setMovementSpeed((int)(Math.random()*4+5));
                    }
                }
                // Set random movement if the zombie is out of bounds
                else{
                    nz.setMovementSpeed((int)(Math.random()*4+5));
                    nz.setMove(true);
                }
                // Draw the zombie
                g.drawImage(nz.getimg(),							
                        nz.getX(), nz.getY(), nz.getX()+50, nz.getY()+80,  //destination
                        nz.getImgX(), nz.getImgY(), nz.getImgX()+nz.getW(), nz.getImgY()+nz.getH(),						
                        null);
            }

            // Draw the projectiles
            for (Projectile p : projectiles){
                g.drawImage(p.getimg(), p.getX(), p.getY(), null);
            }
            
            // Draw the sun
            sunObj.draw(g2, sun);
            
            // Display a wave announcement 
            if(wavePlaying) {
                // To see if the waveframe counter is less than 100 to control the length of the wave announcement
                if(waveFrameCtr<100) {
                    waveFrameCtr++;
                    String waveFilePath = "/Wave"+Integer.toString(zombieWaveCtr)+".png";
                    Image waveImg = loadImage(waveFilePath);
                    // Drawign the wave announcement
                    if(zombieWaveCtr!=7) {
                        g.drawImage(waveImg, 300, 200, 400, 200, null);
                    }
                    else {
                        g.drawImage(waveImg, 100, 200, 900, 200, null);
                    }
                }
                // Reset the waveFrameCtr and set wavePlaying to false
                else {
                    waveFrameCtr=0;
                    wavePlaying=false;
                }
            }
        }
    }

    // The end panel class
    class EndPanel extends JPanel {
        /**
         * Constructor for the EndPanel class, which creates the exit and retry buttons
         */
        EndPanel() {
            exitButton = new ExitButton(550, 500, 100, 50);
            retryButton = new RetryButton(350, 500, 100, 50);
        }
        /**
         * Paints the end panel with the winning or losing screen
         */
        @Override  
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            // Checks to see if the player won or not
            if (won){
                // Draw the winning screen
                g2.drawImage(loadImage("/WinningScreen.jpg"), 0, 0, getWidth(), getHeight(), null);
                // Play the winning music
                if(playWin) {
                	Music.playMusic("soundRes/winmusic.wav", false);
                	mainSound.stop();
                	playWin=false;
                }
                // Update the position of the exitbutton as there is no retry button
                exitButton.x = 450;
                exitButton.y = 575;
                g2.drawImage(loadImage("/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
            } else{
                // Draw the losing screen
                g2.drawImage(loadImage("/EndScreen.jpg"), 0, 0, getWidth(), getHeight(), null);

                // Draw the exit and retry buttons which have the default positions from the constructor
                g2.drawImage(loadImage("/ExitButton.png"), exitButton.x, exitButton.y, exitButton.width, exitButton.height, null);
                g2.drawImage(loadImage("/RetryButton.png"), retryButton.x, retryButton.y, retryButton.width, exitButton.height, null);
                
                // Play the losing music 
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
     * @param filename the name of the image
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

    /**
     * Checks if two rectangles are overlapping
     * @param rect1 The first rectangle
     * @param rect2 The second rectangle
     * @return true if the rectangles are overlapping, false otherwise
     */
    public boolean isOverlapping(Rectangle rect1, Rectangle rect2) {
        return rect1.x < rect2.x + rect2.width && rect1.x + rect1.width > rect2.x && rect1.y < rect2.y + rect2.height && rect1.y + rect1.height > rect2.y;
    }

    /**
     * Checks if the game is over
     * @return true if the zombies hit the house, false otherwise
     */
    public boolean checkLoss(){
    	if(won)return true;
        for (Zombie z : zombies){
            if (z.getX() < 250){
                return true;
            }
        }	
        return false;
    }

    /**
     * The reset game method 
     */
    private void resetGame(){
        // Reset the sun count and respawn the sun
        sunCount = 125;
        respawnSun();
        // Reset the mouse coordinates, frame and spawn zombie counter, chosen plant, and the arraylists
        mx=-100;
        my=-100;
        frameCtr=0;
        chosenPlant = -1;
        plants.clear();
        zombies.clear();
        spawnZombieCtr=0;
        //Reset the grid arrays back to false
        for(int i = 0; i < 9; i++) {
        	for(int j = 0; j < 5; j++) {
        		hasPlant[i][j]=false;
        	}
        }
        // Clear the projectiles arraylist
        projectiles.clear();
        // Reset the zombie wave counter and play the main sound
        zombieWaveCtr=0;
        cardLayout.show(mainPanel, "game");
        mainSound=Music.playMusic("soundRes/Grasswalk.wav", true);
        playLose=true;
        playWin=true;
    }

    // The mousehandler for the endpanel to see if the exit or retry button is clicked
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