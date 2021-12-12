package GUI;

import Server.Server;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import shapes.Schiff;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SpielGUIController implements Initializable {

    @FXML
    private Pane spielFeld;

    private LokalesSpielSteuerung dieLokalesSpielSteuerung = null;
    private KISpielSteuerung dieKISpielSteuerung = null;
    private OnlineSpielSteuerung dieOnlineSpielSteuerung = null;

    private int modus;
    private String ip = null; // Null wenn Lokales Spiel 

    @FXML
    private Label outputField;

    @FXML
    private Button btn_neuPlatzieren;
    @FXML
    private Button btn_Random;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
    }

    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip) {
        //System.out.println("Übergabe Spielfeldgroesse, Anzahl der jeweiligen Schiffstypen und Modus: " + modus);
        this.modus = modus;
        this.ip = ip;

        if (modus == 1) { // Lokales Spiel 
            dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Erzeuge SpielSteuerung
            dieLokalesSpielSteuerung.erzeugeEigeneSchiffe();
        } else if (modus == 21 || modus == 22) { // KI Spiel - 21 ki-host - 22 ki-client 
            if (modus == 21) { // host
                dieKISpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp);
                dieKISpielSteuerung.erzeugeEigeneSchiffe();
                spielFeld.getChildren().clear();
                if (dieKISpielSteuerung.isFertigSetzen()) {
                    spielFeld.getChildren().clear();
                    dieKISpielSteuerung.setSchiffeSetzen();
                    dieKISpielSteuerung.setGridSpielfeldSpiel(dieKISpielSteuerung.getKi().getGridSpielfeld());
                    dieKISpielSteuerung.getGridSpielfeld().print();
                    dieKISpielSteuerung.setzeSchiffeKI();
                }
                dieKISpielSteuerung.werdeServer();
            } else if (modus == 22) { // client
                spielFeld.getChildren().clear();
                dieKISpielSteuerung = new KISpielSteuerung(this);
                dieKISpielSteuerung.werdeClient();
            }
        } else if (modus == 31 || modus == 32) { // Online Spiel - 31 host - 32 client
            if (modus == 31) { // host
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp);
                dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                dieOnlineSpielSteuerung.werdeServer();
            } else if (modus == 32) { // client
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this);
                dieOnlineSpielSteuerung.werdeClient();
            }
        }
    }

    public Server getServer() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getServer();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getServer();
        } else {
            return null;
        }
    }
    
    public int getSpielfeldgroesse() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getSpielfeldgroesse();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getSpielfeldgroesse();
        } else if (dieLokalesSpielSteuerung != null) {
            return dieLokalesSpielSteuerung.getSpielfeldgroesse();
        }
        return 0;
    }
    
    public int[] getAnzahlSchiffeTyp() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getAnzahlSchiffeTyp();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getAnzahlSchiffeTyp();
        } else if (dieLokalesSpielSteuerung != null) {
            return dieLokalesSpielSteuerung.getAnzahlSchiffeTyp();
        }
        return null;
    }
    
    public void setSpielfeldgroesse(int spielfeldgroesse) {
        if (dieKISpielSteuerung != null) {
            dieKISpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        } else if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        }
    }
    
    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        if (dieKISpielSteuerung != null) {
            dieKISpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
        } else if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
        }
    }

    public String getIp() {
        return ip;
    }

    public void zeigeGrid(Rectangle rectangle) {
        spielFeld.getChildren().add(rectangle);
    }

    public void zeigeSchiffe(Schiff schiff) {
        spielFeld.getChildren().add(schiff);
    }

    public void zeigeSchiff(Rectangle rec) {
        spielFeld.getChildren().add(rec);
    }

    public void zeigeLinie(Line line) {
        spielFeld.getChildren().add(line);
    }

    public KISpielSteuerung getDieKISpielSteuerung() {
        return dieKISpielSteuerung;
    }

    public OnlineSpielSteuerung getDieOnlineSpielSteuerung() {
        return dieOnlineSpielSteuerung;
    }

    public Label getOutputField() {
        return outputField;
    }

    public void erstelleSteuerung() {
        if (modus == 22) {
            dieKISpielSteuerung.erzeugeKI();
            //dieKISpielSteuerung.uebergebeRest(spielfeldgroesse, anzahlSchiffeTyp);
            
            dieKISpielSteuerung.erzeugeEigeneSchiffe();
            
            if (dieKISpielSteuerung.isFertigSetzen()){
                dieKISpielSteuerung.setSchiffeSetzen();
                
                //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieKISpielSteuerung.setGridSpielfeldSpiel(dieKISpielSteuerung.getKi().getGridSpielfeld()); //hier wird gezeichnet :)
                        dieKISpielSteuerung.getGridSpielfeld().print();
                        dieKISpielSteuerung.setzeSchiffeKI(); //hier auch
                    }
                });
            }
        } 
        else if (modus == 32) {
            dieOnlineSpielSteuerung.erzeugeSteuerungSchiffeSetzen();
            //dieOnlineSpielSteuerung.uebergebeRest(spielfeldgroesse, anzahlSchiffeTyp);
            
            //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                    }
                });
        }

    }

    @FXML
    private void handleButton(ActionEvent event) {
        if ((dieLokalesSpielSteuerung instanceof LokalesSpielSteuerung && dieLokalesSpielSteuerung.isFertigSetzen())) { //dieSteuerungSchiffeSetzen.isFertig()
            spielFeld.getChildren().clear();
            dieLokalesSpielSteuerung.setSchiffeSetzen();
            dieLokalesSpielSteuerung.setGridSpielfeldSpiel(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeld());
            dieLokalesSpielSteuerung.setzeSchiffe();
            System.out.println("Eigenes Feld");
            dieLokalesSpielSteuerung.getGridSpielfeld().print();
            dieLokalesSpielSteuerung.erzeugeGegnerSchiffe();
            if(dieLokalesSpielSteuerung.gegnerKiIsFertig()){
                dieLokalesSpielSteuerung.beginneSpiel();
            }
        } else if (dieOnlineSpielSteuerung instanceof OnlineSpielSteuerung && dieOnlineSpielSteuerung.isFertigSetzen()) {
            spielFeld.getChildren().clear();
            dieOnlineSpielSteuerung.setSchiffeSetzen();
            dieOnlineSpielSteuerung.setGridSpielfeld(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeld());
            dieOnlineSpielSteuerung.setzeSchiffe();
            System.out.println("Eigenes Feld");
            dieOnlineSpielSteuerung.getGridSpielfeld().print();
        }
    }

    /**
     * Steuert das Verhalten bei click auf den Button Schiffe Neu Plazieren
     *
     * @param internes javafx event, dass die Funktion auslöst
     */
    @FXML
    private void handleButtonNeuPlatzieren(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.clearSchiffeSetzen();
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.clearSchiffeSetzen();
        }
    }

    /**
     * Steuert das Verhalten bei click auf den Button Schiffe Zufällig plazieren
     *
     * @param internes javafx event, dass die Funktion auslöst
     */
    @FXML
    private void handleButtonRandom(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.randomSetzen();
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.randomSetzen();
        }
    }
}
