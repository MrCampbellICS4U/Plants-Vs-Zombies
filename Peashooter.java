package PlantsVsZombies;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Peashooter extends Plant {
	
	private String filename;
	private Image img;
	private boolean isAttacking = false;
	
	
	public Peashooter(int h, int x, int y, int as, int dmg, String f) {
		super(h, x, y, as, dmg);
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
	
	public void setAttack(boolean b) {
		isAttacking=b;
	}
	
	public boolean getIsAttacking() {
		return isAttacking;
	}
	
	public int getImgX() {
		int f = getFrame();
		if(isAttacking) {
			if(f>=3) {
				f%=3;
				setFrame(f);
			}
			return f*67;
		}
		else {
			if(f>=5) {
				f%=5;
				setFrame(f);
			}
			return f*67;
		}
	}
	public int getImgY() {
		if(isAttacking) {
			return 77;
		}
		return 0;
	}
	
	public int getW() {
		return 67;
	}
	public int getH() {
		return 77;
	}

}
