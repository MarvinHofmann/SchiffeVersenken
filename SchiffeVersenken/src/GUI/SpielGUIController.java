/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Server.Server;
import Server.Client;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import controll.Steuerung;
import controll.SteuerungSchiffeSetzen;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import shapes.Schiff;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SpielGUIController implements Initializable {

    @FXML
    private Pane spielFeld;

    private Steuerung dieSteuerung = null;
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    
    private int modus;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(this);
    }    

    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus) {
        System.out.println("Übergabe Spielfeldgroesse, Anzahl der jeweiligen Schiffstypen und Modus: " + modus);
        this.modus = modus; //2 ki 21 ki-host 22 ki-client
        if(modus == 1){ // Lokales Spiel 
            dieSteuerungSchiffeSetzen.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp);
            dieSteuerung = new LokalesSpielSteuerung(this);
        }
        else if(modus == 2 || modus == 21 || modus == 22){ // KI Spiel
            dieSteuerungSchiffeSetzen.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp); 
            dieSteuerung = new KISpielSteuerung(this);
            if (modus==21) {
                new Thread(() -> {
                new Server();
            }).start();
            }
            else if(modus==22){
                new Thread(() -> {
                new Client();
            }).start();
            }
       
        }
        else if(modus == 3 || modus == 31 || modus == 32){ // Online Spiel
            dieSteuerungSchiffeSetzen.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp); 
            dieSteuerung = new OnlineSpielSteuerung(this);
            new Thread(() -> {
                new Server();
            }).start();
        }
    }
    
    public void zeigeGrid(Rectangle rectangle) {
        spielFeld.getChildren().add(rectangle);
    }

    public void zeigeSchiffe(Schiff schiff) {
        spielFeld.getChildren().add(schiff);
    }
    
    public void setzeCursor(Cursor CLOSED_HAND) {
        spielFeld.setCursor(Cursor.CLOSED_HAND);

    }

    @FXML
    private void handleButton(ActionEvent event) {
        spielFeld.getChildren().clear();
    }
    
    
}
