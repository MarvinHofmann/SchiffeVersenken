/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Musik;

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

    /**
     * Startet den Musikplayer mit richtiger Quelle für das Menü. Die Lautstärke ist die Globale über 
     * var hinterlegte
     */
    public void start() {
        Media sound = new Media(getClass().getResource("/Musik/music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(100);
        mediaPlayer.play();
        mediaPlayer.setVolume(var.lautstaerke);
    }
    
    /**
     * Setzt die Lautstärke des Musikplayers für das Menü
     * @param val lautstärke
     */
    public static void setLautstaerke(double val){
        mediaPlayer.setVolume(val);
    }
    
    /**
     * Setzt die Lautstärke für das Spiel
     * @param val lautstärke
     */
    public static void setLautstaerkeGame(double val){
        mediaPlayer1.setVolume(val);
    }
    
    /**
     * Startet den Musikplayer für das Spiel, der Musikplayer für das 
     * Menü wird gestoppt
     * @param file Musik File, dass abgesprilt wrden Soll
     */
    public  void setMusikGame(String file){
        mediaPlayer.stop();
        Media sound1 = new Media(getClass().getResource(file).toExternalForm());
        mediaPlayer1 = new MediaPlayer(sound1);
        mediaPlayer1.play();
        mediaPlayer1.setVolume(var.lautstaerke);
    }
    
    /**
     * Stoppt die Musik des Spiels, wenn man wieder ins Menü springt
     */
    public static void setMusikMenue(){
        mediaPlayer1.stop();
    }

}
