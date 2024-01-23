import java.awt.*;

// This class is used to create the start button
public class StartButton extends Rectangle {    
    /**
     * Constructor for StartButton
     * @param nonhover String of the path of the nonhover image
     * @param hover String of the path of the hover image
     * @param x The x coordinate of the button
     * @param y The y coordinate of the button
     * @param width The width of the button
     * @param height The height of the button
     */
    public StartButton(String nonhover, String hover, int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Returns the path of the nonhover image
     * @return the path of the nonhover image
     */
    public static String nonhover() {
        return "/startbutton.png";
    }

    /**
     * Returns the path of the hover image
     * @return the path of the hover image
     */
    public static String hover() {
        return "/startbutton2.png";
    }
}