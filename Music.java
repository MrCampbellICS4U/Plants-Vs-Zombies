import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class Music {
    // Declare static variables for different clips
    private static Clip clip;
    private static Clip backgroundMusic;
    private static Clip plantSound1;
    private static Clip plantSound2;
    private static Clip peashooter;
    private static Clip wavemusic;
	private static Clip endmusic;
	private
    static Clip losemusic;
    private static Clip winmusic;

    /**
     * takes file name entered and matches it with a pre-existing sound
     * 
     * @param filepath the file name of the sound
     * @param loop     boolea, whether the sound should loop or not
     */
    public static Clip playMusic(String filepath, boolean loop) {
        try {
            // create file object with specified file path
            File musicPath = new File(filepath);

            // check if file exists
            if (musicPath.exists()) {

                // get audio input stream from file and put it into newclip object
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip newclip = AudioSystem.getClip();

                // open the clip and close the old one if it is running
                newclip.open(audioInput);
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                    clip.close();
                }

                // close any backhground music if it is running
                if (filepath.equals("soundRes/Grasswalk.wav") && backgroundMusic.isRunning()) {
                    backgroundMusic.stop();
                    backgroundMusic.close();
                }

                // sets the new clip to the correct music variable based on file path
                if (filepath.equals("soundRes/Grasswalk.wav")) {
                    backgroundMusic = newclip;
                } else if (filepath.equals("soundRes/Crazy_Dave.wav")) {
                    backgroundMusic = newclip;
                } else if (filepath.equals("soundRes/losemusic.wav")) {
                    losemusic = newclip;
                } else if (filepath.equals("soundRes/winmusic.wav")) {
                    winmusic = newclip;
                } else if (filepath.equals("soundRes/siren.wav")) {
                    wavemusic = newclip;
                } else if (filepath.equals("soundRes/PeashooterSound.wav")) {
                    peashooter = newclip;
                } else if (filepath.equals("soundRes/plant.wav")) {
                    plantSound1 = newclip;
                } else if (filepath.equals("soundRes/plant2.wav")) {
                    plantSound2 = newclip;
                }

                // Start playing the new clip, loop if specified
                if (loop) {
                    newclip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    newclip.start();
                }

                // returns new clip
                return newclip;
            }
        } catch (Exception e) {
            // handle exceptions
            System.out.println(JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
