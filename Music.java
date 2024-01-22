package PlantsVsZombies;

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


class Music{
	private static Clip clip;
	private static Clip backgroundMusic;
	private static Clip plantSound1;
	private static Clip plantSound2;
	private static Clip peashooter;
	private static Clip wavemusic;
	private static Clip endmusic;
	private static Clip losemusic;
	private static Clip winmusic;
	public static void main(String[] args) {
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
	            else if(filepath.equals("soundRes/PeashooterSound.wav")){
	            	peashooter = newclip;
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
	            return newclip;
	        }
	    } catch (Exception e) {
	
	        System.out.println(JOptionPane.ERROR_MESSAGE);
	    }
	    return null;
	}
}