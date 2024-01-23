import java.awt.*;
import java.awt.image.BufferedImage;

// Sun class for the suns that fall from the sky
class Sun extends Rectangle{
    private double vy = 3.0;
    private double yy; 
    /**
     * Constructor for the Sun class
     * @param x the x coordinate of the sun
     * @param y the y coordinate of the sun
     * @param w the width of the sun
     * @param h the height of the sun
     */
    public Sun(int x, int y, int w, int h){
        super(x, y, w, h);
        yy = y;
    }
    /**
     * Draws the sun
     * @param g2 the graphics object
     * @param img the image of the sun
     */
    public void draw(Graphics g2, Image img){
        g2.drawImage(img, x, y, width, height, null);
    }
    /**
     * Moves the sun
     */
    public void move(){
        yy += vy;
        y = (int)yy;
    }
    /**
     * Respawns the sun at the top of the screen
     */
    public void setYY(double yy){
        this.yy = yy;
    }
    /**
     * Respawns the sun at the top of the screen
     */
    public double getYY(){
        return yy;
    }
}
