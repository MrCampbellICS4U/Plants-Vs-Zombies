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

static Clip clip;
static Clip backgroundMusic;
static Clip plantSound1;
static Clip plantSound2;
static Clip peashooter;
static Clip wavemusic;
static Clip endmusic;


playMusic("soundRes/Crazy_Dave.wav", true);


playMusic("soundRes/Grasswalk.wav", true);


playMusic("soundRes/plant.wav", false);
playMusic("soundRes/plant2.wav", false);

playMusic("soundRes/losemusic.wav", false);
playMusic("soundRes/winmusic.wav", false);
playMusic("soundRes/siren.wav", false);


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

            if (filepath.equals("soundRes/Grasswalk.wav") && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }

            if (filepath.equals("soundRes/Grasswalk.wav")) {
                backgroundMusic = newclip;
            } else if (filepath.equals("soundRes/Crazy_Dave.wav")) {
                backgroundMusic = newclip;
            } 
            else if (filepath.equals("soundRes/losemusic.wav")) {
                losemusic = newclip;
            } 
            else if (filepath.equals("soundRes/winmusic.wav")) {
                winmusic = newclip;
            } 
            else if(filepath.equals("soundRes/siren.wav")){
                wavemusic = newclip;
            }
            else if (filepath.equals("soundRes/plant.wav")) {
                plantSound1 = newclip;
            } else if (filepath.equals("soundRes/plant2.wav")) {
                plantSound2 = newclip;
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
    return clip;
}