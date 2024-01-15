package PlantsVsZombies;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Walnut extends Plant {
	
	private String filename;
	private Image img;
	
	
	public Walnut(int h, int x, int y, int as, int dmg, String f, String t) {
		super(h, x, y, as, dmg, t);
		filename=f;
		img =loadImage(f);
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
	
	public Image getimg() {
		return img;
	}
	
	public int getImgX() {
		int f = getFrame();
		if(f>=5) {
			f%=5;
			setFrame(f);
		}
		return f*70+3;
	}
	public int getImgY() {
		if(getHealth()<33) {
			return 164;
		}
		else if(getHealth()<66) {
			return 82;
		}
		else {
			return 0;
		}
	}
	
	public int getW() {
		return 70;
	}
	
	public int getAdjustment() {
		return 0;
	}
	
	public int getH() {
		return 82;
	}

}
