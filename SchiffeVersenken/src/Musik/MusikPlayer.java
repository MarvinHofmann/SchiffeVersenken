/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Musik;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import var.var;

/**
 *
 * @author marvi
 */
public class MusikPlayer {

    static MediaPlayer mediaPlayer;
    static MediaPlayer mediaPlayer1;

    public static void start() {
        String musicFile = "music.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(100);
        mediaPlayer.play();
        mediaPlayer.setVolume(var.lautstaerke);
    }
    
    public static void setLautstaerke(double val){
        mediaPlayer.setVolume(val);
    }
    
    public static void setLautstaerke1(double val){
        mediaPlayer1.setVolume(val);
    }
    
    public static void setMusik(String file){
        Media sound = new Media(new File(file).toURI().toString());
        mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
        mediaPlayer1.setVolume(var.lautstaerke);
        mediaPlayer.stop();
    }
    

}
