package GUI;

import Server.Server;
import Server.Client;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import controll.SpielSteuerung;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private SpielSteuerung dieSpielSteuerung = null;
    
    private int modus;
    private String ip = null; // Null wenn Lokales Spiel 
    private Server server; 
    private Client client; 
    private int[] anzahlSchiffeTyp;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
    }    
    
    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip) {
        System.out.println("Übergabe Spielfeldgroesse, Anzahl der jeweiligen Schiffstypen und Modus: " + modus);
        this.modus = modus;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        if(modus == 1){ // Lokales Spiel 
            dieSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Erzeuge SpielSteuerung
            dieSpielSteuerung.erzeugeEigeneSchiffe();
        }
        else if(modus == 21 || modus == 22){ // KI Spiel - 21 ki-host - 22 ki-client 
            if (modus==21) {
                new Thread(() -> {
                    server = new Server(this);
                    server.start();
                    System.out.println(server);
                }).start();
            }
            else if(modus==22){
                new Thread(() -> {
                    client = new Client(ip, this);
                    client.start();
                }).start();
            }
            //dieSpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Hier muss wenn client spielfeldgroesse und anzahlschiffstypen per Netzwerk kommen
            //dieSpielSteuerung.erzeugeEigeneSchiffe();
        }
        else if(modus == 31 || modus == 32){ // Online Spiel - 31 host - 32 client
            if(modus==31){
                new Thread (() -> {
                    server = new Server(this);
                    server.start();
                }).start();
            }
            else if(modus==32){
                new Thread(() -> {
                    client = new Client(ip, this);
                    client.start();
                }).start();
            }
            //dieSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Hier muss wenn client spielfeldgroesse und anzahlschiffstypen per Netzwerk kommen
            //dieSpielSteuerung.erzeugeEigeneSchiffe();
        }
    }
    
    public void zeigeGrid(Rectangle rectangle) {
        spielFeld.getChildren().add(rectangle);
    }

    public void zeigeSchiffe(Schiff schiff) {
        spielFeld.getChildren().add(schiff);
    }
    
    public void zeigeSchiff(Rectangle rec){
        spielFeld.getChildren().add(rec);
    }
    
    @FXML
    private void handleButton(ActionEvent event) {
        // server.send("Hallo Client");
        if((dieSpielSteuerung instanceof LokalesSpielSteuerung && dieSpielSteuerung.isFertigSetzen())){ //dieSteuerungSchiffeSetzen.isFertig()
            spielFeld.getChildren().clear();
            dieSpielSteuerung.setSchiffeSetzen();
            dieSpielSteuerung.erzeugespielfeld();
            dieSpielSteuerung.erzeugeGrid();
            dieSpielSteuerung.setzeSchiffe();
        } 
        else if(dieSpielSteuerung instanceof KISpielSteuerung && dieSpielSteuerung.isFertigSetzen()){ // && dieSpielSteuerung.getKi().isFertig()){
            spielFeld.getChildren().clear();
            dieSpielSteuerung.test();
            //dieSpielSteuerung.setSchiffeSetzen();
            //dieSpielSteuerung.erzeugespielfeld();
            //dieSpielSteuerung.erzeugeGrid();
            //dieSpielSteuerung.setzeSchiffe();
            // Hier wird noch nichts angezeigt da Schiffe leer
        }
        else if(dieSpielSteuerung instanceof OnlineSpielSteuerung && dieSpielSteuerung.isFertigSetzen()){
            spielFeld.getChildren().clear();
            dieSpielSteuerung.setSchiffeSetzen();
            dieSpielSteuerung.erzeugespielfeld();
            dieSpielSteuerung.erzeugeGrid();
            dieSpielSteuerung.setzeSchiffe(); 
        }
    }
    
    public void connectedWithClient(int kategorie){
        System.out.println("Hallo welt");
        if(kategorie == 1){
            server.send("size " + dieSpielSteuerung.getSpielfeldgroesse());
        }
        else if(kategorie == 2){
            server.send("ships " + parseSchiffTypes(anzahlSchiffeTyp));
        }
        
    }

    public Server getServer() {
        return server;
    }
    
    public void communicationwithCient(String Kategorie){
        
    }
    
    private String parseSchiffTypes(int[] schifftypes){
        String parsedSchiffe = "";
        for(int i = 0; i < schifftypes.length; i++){
            for(int j = 0; j < schifftypes[i]; j++){
                parsedSchiffe = parsedSchiffe + " " + (i + 2);
            }
        }
        System.out.println(parsedSchiffe);
        return parsedSchiffe;
    }
    
}
