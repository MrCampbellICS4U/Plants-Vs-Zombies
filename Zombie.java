// package PlantsVsZombies;

import java.awt.Image;
import java.awt.Rectangle;
public class Zombie{
	private int health;
	private int posX;
	private int posY;
	private int attackSpeed;
	private int damage;
	private int frame=0;
	private int movementSpeed;

	// Make a hit box around the zombie
	public Zombie(int h, int x, int y, int as, int dmg, int ms) {
		health=h;
		posX=x;
		posY=y;
		attackSpeed=as;
		damage=dmg;
		movementSpeed=ms;
	}

	/**
	 * Returns the bounds of the zombie
	 * @return
	 */
	public Rectangle getBounds() {
		return new Rectangle(posX, posY, 100, 100);
	}

	public int getHealth() {
		return health;
	}
	public int getX() {
		return posX;
	}
	public void setX(int x) {
		posX=x;
	}
	public void setY(int y) {
		posY=y;
	}
	public int getY() {
		return posY;
	}
	public int getAttackSpeed() {
		return attackSpeed;
	}
	public int getDamage(){
		return damage;
	}
	public int getFrame() {
		return frame;
	}
	public void incrementFrame() {
		frame++;
	}
	public void setFrame(int f) {
		frame=f;
	}
	public void reduceHealth(int h) {
		health-=h;
	}
	public void move() {
		posX-=movementSpeed;
	}
}
