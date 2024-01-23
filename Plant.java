import java.awt.Image;

// The plant superclass to be inherited from
public class Plant {
	private int health;
	private int posX;
	private int posY;
	private int attackSpeed;
	private int damage;
	private int frame=0;
	private String type;
	
	/**
	 * Constructor for the plant
	 * @param h health of the plant
	 * @param x x coordinate of the plant
	 * @param y y coordinate of the plant
	 * @param as attack speed of the plant
	 * @param dmg damage of the plant
	 * @param t type of the plant
	 */
	public Plant(int h, int x, int y, int as, int dmg, String t) {
		health=h;
		posX=x;
		posY=y;
		attackSpeed=as;
		damage=dmg;
		type = t;
	}

	/**
	 * Gets the health of the plant
	 * @return the health of the plant
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets the x coordinate of the plant
	 * @return the x coordinate of the plant
	 */
	public int getX() {
		return posX;
	}

	/**
	 * Sets the x coordinate of the plant
	 * @param x the x coordinate to set the plant to
	 */
	public void setX(int x) {
		posX=x;
	}

	/**
	 * Sets the y coordinate of the plant
	 * @param y the y coordinate to set the plant to
	 */
	public void setY(int y) {
		posY=y;
	}

	/**
	 * Gets the y coordinate of the plant
	 * @return the y coordinate of the plant
	 */
	public int getY() {
		return posY;
	}

	/**
	 * Gets the attack speed of the plant
	 * @return the attack speed of the plant
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Gets the damage of the plant
	 * @return the damage of the plant
	 */
	public int getDamage(){
		return damage;
	}

	/**
	 * Gets the frame of the plant
	 * @return the frame of the plant
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * Increments the frame of the plant
	 */
	public void incrementFrame() {
		frame++;
	}

	/**
	 * Sets the frame of the plant
	 * @param f the frame to set the plant to
	 */
	public void setFrame(int f) {
		frame=f;
	}

	/**
	 * Reduces the health of the plant
	 * @param h the amount to reduce the health by
	 */
	public void reduceHealth(int h) {
		health-=h;
	}

	/**
	 * Gets the type of the plant
	 * @return the type of the plant 
	 */
	public String getType() {
		return type;
	}
}
