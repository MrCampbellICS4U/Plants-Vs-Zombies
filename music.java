import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    private static Clip clip;
    private static Clip backgroundmusic;
    private static Clip plantSound1;
    private static Clip plantSound2;
    private static Clip peashooter;
    private static Clip wavemusic;
    private static Clip winmusic;
    private static Clip losemusic;

    public static Clip playBackgroundMusic(String filepath, boolean loop) {
        return playMusic(filepath, true);
    }

    public static Clip playWinMusic(String filepath, boolean loop) {
        return playMusic(filepath, true);
    }

    public static Clip playLoseMusic(String filepath, boolean loop) {
        return playMusic(filepath, false);
    }

    public static Clip playWaveMusic(String filepath, boolean loop) {
        return playMusic(filepath, false);
    }

    public static Clip playPlantSound1(String filepath) {
        return playMusic(filepath, false);
    }

    public static Clip playPlantSound2(String filepath) {
        return playMusic(filepath, false);
    }

    public static Clip playPeashooter(String filepath) {
        return playMusic(filepath, false);
    }

    public static Clip playMusic(String filepath, boolean loop) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip newclip = AudioSystem.getClip();
                newclip.open(audioInput);

                if (clip != null && clip.isRunning()) {
                    clip.stop();
                    clip.close();
                }

                // Start playing the new clip
                if (loop) {
                    newclip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    newclip.start();
                }
            }
        } catch (Exception e) {

            System.out.println(JOptionPane.ERROR_MESSAGE);
        }
    }
}