import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Rectangle;

// This class will create a projectile object that will be used to shoot at the zombies. 
public class Projectile{
    private int damage;
    private int posX;
    private int posY;
    private int frame = 0;
    private int vx;
    private Image img; 


    /**
     * Constructor for the Projectile class
     * @param dmg the damage of the projectiles
     * @param x the x coordinate of the projectile
     * @param y the y coordinate of the projectile
     * @param v the velocity of the projectile
     */
    public Projectile(int dmg, int x, int y, int v){
        damage = dmg;
        posX = x;
        posY = y;
        vx = v;
        img = loadImage("ImagesFolder/pea.png");
    }

    /**
     * Returns the bounds of the projectile
     * @return the bounds of the projectile
     */    
    public Rectangle getBounds() {
        return new Rectangle(posX, posY, 20, 20);
    }

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
     * Returns the image of the projectile
     * @return the image of the projectile
     */
    public Image getimg() {
        return img;
    }
    /**
     * Returns the damage of the projectile
     * @return the damage of the projectile
     */
    public int getDamage(){
        return damage;
    }
    /**
     * Returns the x coordinate of the projectile
     * @return the x coordinate of the projectile
     */
    public int getX(){
        return posX;
    }
    /**
     * Returns the y coordinate of the projectile
     * @return the y coordinate of the projectile
     */
    public int getY(){
        return posY;
    }
    /**
     * Returns the frame of the projectile
     * @return the frame of the projectile
     */
    public int getFrame(){
        return frame;
    }
    /**
     * Increments the frame of the projectile
     */
    public void incrementFrame(){
        frame++;
    }
    /**
     * Sets the frame of the projectile
     * @param f the frame of the projectile
     */
    public void setFrame(int f){
        frame = f;
    }
    /**
     * Moves the projectile
     */
    public void move(){
        posX += vx;
    }
}
