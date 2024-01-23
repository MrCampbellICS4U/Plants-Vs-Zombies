import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// The peashooter class that extends the plant class and for the peashooter plant
public class Peashooter extends Plant {
	private String filename;
	private Image img;
	// The boolean that determines if the peashooter is attacking
	private boolean isAttacking = false;
	// The counter that determines when the peashooter shoots
	private int shootCtr=0;
	
	/**
	 * Constructor for the peashooter
	 * @param h health of the peashooter
	 * @param x x coordinate of the peashooter
	 * @param y y coordinate of the peashooter
	 * @param as attack speed of the peashooter
	 * @param dmg damage of the peashooter
	 * @param f filename of the image
	 * @param t type of the peashooter
	 */
	public Peashooter(int h, int x, int y, int as, int dmg, String f, String t) {
		super(h, x, y, as, dmg, t);
		filename=f;
		img =loadImage(f);
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
	 * Gets the image of the peashooter
	 * @return the image of the peashooter
	 */
	public Image getimg() {
		return img;
	}
	
	/**
	 * Sets the attacking boolean
	 * @param b the boolean to set the attacking boolean to
	 */
	public void setAttack(boolean b) {
		isAttacking=b;
	}
	
	/**
	 * Gets the attacking boolean
	 * @return the attacking boolean
	 */
	public boolean getIsAttacking() {
		return isAttacking;
	}
	
	/**
	 * Gets the x coordinate for the peashooter sprite 
	 * @return the x coordinate of the image
	 */
	public int getImgX() {
		// Current frame count of the peashooter animation
		int f = getFrame();
		if(isAttacking) {
			// Ensure that the frame count stays within the valid range
			if(f>=3) {
				f%=3;
				setFrame(f); //Update the frame count
			}
			// Calc and return the x-coordinate of the attacking sprite in the image file
			return f*63;
		}
		// Peashooter is not attacking
		else {
			// Ensure that the frame count stays within the valid range
			if(f>=5) {
				f%=5;
				setFrame(f);
			}
			return f*67;
		}
	}
	/**
	 * Gets the y coordinate for the peashooter sprite 
	 * @return the y coordinate of the image
	 */
	public int getImgY() {
		if(isAttacking) {
			return 80;
		}
		return 0;
	}
	/**
	 * Gets the width of the peashooter sprite
	 * @return the width of the image
	 */
	public int getW() {
		if(isAttacking&&getFrame()==0) {
			return 70;
		}
		else if(isAttacking&&getFrame()%3!=0) {
			return 65;
		}
		else if(isAttacking) {
			return 60;
		}
		return 65;
	}
	/**
	 * Gets the adjustment for the peashooter sprite
	 * @return the adjustment for the image
	 */
	public int getAdjustment() {
		// If the peashooter is attacking, no adjustment is needed
		if(isAttacking) {
			return 0;
		}
		// If the peashooter is not attacking, adjust the x coordinate based on the frame count
		else {
			if(getFrame()==0||getFrame()==4) {
				return -3;
			}
			else if(getFrame()==1||getFrame()==3){
				return 1;
			}
			else if(getFrame()==2) {
				return 3;
			}
			else if(getFrame()==5) {
				return -3;
			}
			else if(getFrame()==6) {
				return -2;
			}
			else {
				return -1;
			}
		}
	}
	
	/**
	 * Gets the height of the peashooter sprite
	 * @return the height of the image
	 */
	public int getH() {
		return 77;
	}
	
	/**
	 * Gets the counter for the peashooter
	 * @return the counter for the peashooter
	 */
	public int getShootCtr() {
		return shootCtr;
	}

	/**
	 * Sets the counter for the peashooter
	 * @param a the counter to set the peashooter counter to
	 */
	public void setShootCtr(int a) {
		shootCtr=a;
	}
}
