package PlantsVsZombies;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Peashooter extends Plant {
	
	private String filename;
	private Image img;
	private boolean isAttacking = false;
	private int shootCtr=0;
	
	
	public Peashooter(int h, int x, int y, int as, int dmg, String f, String t) {
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
			return f*63;
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
			return 80;
		}
		return 0;
	}
	
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
	
	public int getAdjustment() {
		if(isAttacking) {
			return 0;
		}
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
	
	public int getH() {
		return 77;
	}
	
	public int getShootCtr() {
		return shootCtr;
	}
	public void setShootCtr(int a) {
		shootCtr=a;
	}
}
