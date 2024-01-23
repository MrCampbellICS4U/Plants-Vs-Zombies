import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// The normal zombie class that extends the zombie class, for the normal zombie
public class NormalZombie extends Zombie {
	// The filename of the image
	private String filename;
	// The image of the zombie
	private Image img;
	// The boolean that determines if the zombie is moving
	private boolean isMoving = true;	

	/**
	 * Constructor for the normal zombie
	 * @param h health of the zombie
	 * @param x x coordinate of the zombie
	 * @param y y coordinate of the zombie
	 * @param as attack speed of the zombie
	 * @param dmg damage of the zombie
	 * @param ms movement speed of the zombie
	 * @param f filename of the image
	 */
	public NormalZombie(int h, int x, int y, int as, int dmg, int ms, String f) {
		super(h, x, y, as, dmg, ms);
		filename=f;
		img =loadImage(filename);
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
	 * Gets the image of the zombie
	 * @return the image of the zombie
	 */
	public Image getimg() {
		return img;
	}
	
	/**
	 * Sets the moving boolean
	 * @param b the boolean to set the moving boolean to
	 */
	public void setMove(boolean b) {
		isMoving=b;
	}
	
	/**
	 * Gets the moving boolean
	 * @return the moving boolean
	 */
	public boolean getIsMoving() {
		return isMoving;
	}
	
	/**
	 * Gets the x coordinate of the image
	 * @return the x coordinate of the image
	 */
	public int getImgX() {
		// Gets the frame of the zombie sprite animation
		int f = getFrame();
		if(isMoving) {
			// Ensure that the frame stays within the valid range 
			if(f>=7) {
				f%=7;
				setFrame(f);
			}
			// Calculate the x-coordinate of the sprite in image file
			return f*37;
		}
		else {
			// Ensure that the frame stays within the valid range 
			if(f>=7) {
				f%=7;
				setFrame(f);
			}
			// Calculate the x-coordinate of the sprite in image file
			return f*37;
		}
	}

	/**
	 * Gets the y coordinate of the image
	 * @return the y coordinate of the image
	 */
	public int getImgY() {
		if(isMoving) {
			return 100;
		}
		return 185;
	}
	
	/**
	 * Gets the width of the zombie
	 * @return the width of the zombie
	 */
	public int getW() {
		return 35;
	}

	/**
	 * Gets the height of the zombie
	 * @return the height of the zombie
	 */
	public int getH() {
		return 44;
	}

}