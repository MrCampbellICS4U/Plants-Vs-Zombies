package PlantsVsZombies;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.image.BufferedImage;

public class music {
    static Clip clip;

    public static void main(String[] args) {
        playMusic("soundRes/Crazy_Dave.wav");

        playMusic("soundRes/Grasswalk.wav"); // put under button actionlistener
    }

    public static void playMusic(String filepath) {
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

                clip = newclip;

                // Start playing the new clip
                clip.start();
                clip.loop(clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error");
        }
    }
}
