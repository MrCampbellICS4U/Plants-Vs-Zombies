import java.awt.Image;
import java.awt.Rectangle;

// The zombie superclass to be inherited from
public class Zombie{
	private int health;
	private int posX;
	private int posY;
	private int attackSpeed;
	private int damage;
	private int frame=0;
	private int movementSpeed;

	/**
	 * Constructor for the zombie
	 * @param h health of the zombie
	 * @param x x coordinate of the zombie
	 * @param y y coordinate of the zombie
	 * @param as attack speed of the zombie
	 * @param dmg damage of the zombie
	 * @param ms movement speed of the zombie
	 */
	public Zombie(int h, int x, int y, int as, int dmg, int ms) {
		health=h;
		posX=x;
		posY=y;
		attackSpeed=as;
		damage=dmg;
		movementSpeed=ms;
	}

	/**
	 * Creates a new Rectangle of the bounds of the zombie to act as a hitbox
	 * @return a rectangle of the bounds of the zombie 
	 */
	public Rectangle getBounds() {
		return new Rectangle(posX, posY, 100, 100);
	}

	/**
	 * Gets the health of the zombie
	 * @return the health of the zombie
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets the x coordinate of the zombie
	 * @return the x coordinate of the zombie
	 */
	public int getX() {
		return posX;
	}

	/**
	 * Sets the x coordinate of the zombie
	 * @param x the x coordinate to set the zombie to
	 */
	public void setX(int x) {
		posX=x;
	}

	/**
	 * Sets the y coordinate of the zombie
	 * @param y the y coordinate to set the zombie to
	 */
	public void setY(int y) {
		posY=y;
	}

	/**
	 * Gets the y coordinate of the zombie
	 * @return the y coordinate of the zombie
	 */
	public int getY() {
		return posY;
	}

	/**
	 * Gets the attack speed of the zombie
	 * @return the attack speed of the zombie
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Gets the damage of the zombie
	 * @return the damage of the zombie
	 */
	public int getDamage(){
		return damage;
	}

	/**
	 * Gets the frame of the zombie
	 * @return the frame of the zombie
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * Increments the frame of the zombie
	 */
	public void incrementFrame() {
		frame++;
	}

	/**
	 * Sets the frame of the zombie
	 * @param f the frame to set the zombie to
	 */
	public void setFrame(int f) {
		frame=f;
	}

	/**
	 * Reduces the health of the zombie
	 * @param h the amount to reduce the health by
	 */
	public void reduceHealth(int h) {
		health-=h;
	}

	/**
	 * Moves the zombie
	 */
	public void move() {
		posX-=movementSpeed;
	}

	/**
	 * Sets the movement speed of the zombie
	 * @param ms the movement speed to set the zombie to
	 */
	public void setMovementSpeed(int ms) {
		movementSpeed=ms;
	}

	/**
	 * Gets the movement speed of the zombie
	 * @return the movement speed of the zombie
	 */
	public int getMovementSpeed() {
		return movementSpeed;
	}
}