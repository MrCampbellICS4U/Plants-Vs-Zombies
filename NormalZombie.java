package PlantsVsZombies;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class NormalZombie extends Zombie {
	
	private String filename;
	private Image img;
	private boolean isMoving = false;
	
	
	public NormalZombie(int h, int x, int y, int as, int dmg, int ms, String f) {
		super(h, x, y, as, dmg, ms);
		filename=f;
		img =loadImage(filename);
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
	
	public void setMove(boolean b) {
		isMoving=b;
	}
	
	public boolean getIsMoving() {
		return isMoving;
	}
	
	public int getImgX() {
		int f = getFrame();
		if(isMoving) {
			if(f>=7) {
				f%=7;
				setFrame(f);
			}
			return f*37;
		}
		else {
			if(f>=7) {
				f%=7;
				setFrame(f);
			}
			return f*37;
		}
	}
	public int getImgY() {
		if(isMoving) {
			return 100;
		}
		return 185;
	}
	
	public int getW() {
		return 35;
	}
	public int getH() {
		return 44;
	}

}
