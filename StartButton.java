import java.awt.*;

/**
 * custom Jpanel for start button
 * StartButton has two states, one is normal, the other is hover
 */
public class StartButton extends Rectangle {

    /**
     * constructor
     * 
     * @param image1 normal state image
     * @param image2 hover state image
     * 
     * 
     * @param y      y coordinate
     * @param width  width
     * 
     * 
     */
    public StartButton(String nonhover, String hover, int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * returns the image path
     * 
     * @return image1 path
     */
    public static String nonhover() {
        return "ImagesFolder/startbutton.png";
    }

    /**
     * returns image path
     * 
     * @return image2 path
     */
    public static String hover() {
        return "ImagesFolder/startbutton2.png";
    }
}