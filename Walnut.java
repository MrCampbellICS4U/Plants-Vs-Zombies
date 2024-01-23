import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// Walnut class that extends the plant class, for the walnut plant
public class Walnut extends Plant {
	private String filename;
	private Image img;
	
	/**
	 * Constructor for the walnut
	 * @param h health of the walnut
	 * @param x x coordinate of the walnut
	 * @param y y coordinate of the walnut
	 * @param as attack speed of the walnut
	 * @param dmg damage of the walnut
	 * @param f filename of the image
	 * @param t type of the plant
	 */
	public Walnut(int h, int x, int y, int as, int dmg, String f, String t) {
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
	 * Gets the image of the walnut
	 * @return the image of the walnut
	 */
	public Image getimg() {
		return img;
	}
	
	/**
	 * Gets the x coordinate of the image
	 * @return the x coordinate of the image
	 */
	public int getImgX() {
		int f = getFrame();
		if(f>=5) {
			f%=5;
			setFrame(f);
		}
		return f*70+3;
	}

	/**
	 * Gets the y coordinate of the image
	 * @return the y coordinate of the image
	 */
	public int getImgY() {
		if(getHealth()<3333) {
			return 164;
		}
		else if(getHealth()<6666) {
			return 82;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Gets the width of the image
	 * @return 70, which is the width of the image
	 */
	public int getW() {
		return 70;
	}
	
	/**
	 * Gets the adjustment required for the image
	 * @return 0, no adjustment required
	 */
	public int getAdjustment() {
		return 0;
	}
	
	/**
	 * Gets the height of the image
	 * @return 82, which is the height of the image
	 */
	public int getH() {
		return 82;
	}
}
