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

    public void start() {
        //String musicFile = "./src/Musik/music.mp3";
        Media sound = new Media(getClass().getResource("/Musik/music.mp3").toExternalForm());
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
    
    public  void setMusikGame(String file){
        mediaPlayer.stop();
        Media sound1 = new Media(getClass().getResource(file).toExternalForm());
        mediaPlayer1 = new MediaPlayer(sound1);
        mediaPlayer1.play();
        mediaPlayer1.setVolume(var.lautstaerke);
    }
    
    public static void setMusikMenue(){
        mediaPlayer1.stop();
    }

}
