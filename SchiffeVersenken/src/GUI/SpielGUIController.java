package GUI;

import Server.Server;
import Server.Client;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import controll.SpielSteuerung;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
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
    private int spielfeldgroesse;
         
    @FXML
    private Label outputField;
    @FXML
    private Label outputField2;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
    }    
    
    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip) {
        System.out.println("Übergabe Spielfeldgroesse, Anzahl der jeweiligen Schiffstypen und Modus: " + modus);
        
        this.modus = modus;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        this.spielfeldgroesse = spielfeldgroesse;
        
        if(modus == 1){ // Lokales Spiel 
            dieSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Erzeuge SpielSteuerung
            dieSpielSteuerung.erzeugeEigeneSchiffe();
        }
        else if(modus == 21 || modus == 22){ // KI Spiel - 21 ki-host - 22 ki-client 
            if (modus==21) {
                new Thread(() -> {
                    server = new Server(this, this.spielfeldgroesse, anzahlSchiffeTyp);
                    server.start();
                }).start();
            }
            else if(modus==22){
                new Thread(() -> {
                    client = new Client(ip, this);
                    client.start();
                }).start();
            }
            
        }
        else if(modus == 31 || modus == 32){ // Online Spiel - 31 host - 32 client
            if(modus==31){
                new Thread (() -> {
                    server = new Server(this, this.spielfeldgroesse, anzahlSchiffeTyp);
                    server.start();
                }).start();
            }
            else if(modus==32){
                new Thread(() -> {
                    client = new Client(ip, this);
                    client.start();
                }).start();
            }
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
    
    public void zeigeLinie(Line line){
        spielFeld.getChildren().add(line);
    }

    @FXML
    private void handleButton(ActionEvent event) {
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
        if(kategorie == 1){
            String size = "size " + spielfeldgroesse;
            System.out.println("Kategorie 1");
            server.send(size);
        }
        else if(kategorie == 2){
            String ships = "ships" + parseSchiffTypes(anzahlSchiffeTyp);
            System.out.println("Kategorie 2");
            server.sendShips(ships);
        }
        
    }

    public Server getServer() {
        return server;
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
  
    public Label getOutputField() {
        return outputField;
    }
    public Label getOutputField2() {
        return outputField2;
    }
    
    public void erstelleSteuerung(int groesse, int[] schiffe){
        if(modus == 22){
            this.spielfeldgroesse = groesse;
            this.anzahlSchiffeTyp = schiffe;
        
            System.out.println("size: " + groesse);
        
            for(int i = 0; i < 4; i++){
                System.out.println("Anzahl " + i + "er: " + anzahlSchiffeTyp[i]);
            }
            dieSpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); 
            dieSpielSteuerung.erzeugeEigeneSchiffe();
            }
        else if(modus == 32){
            this.spielfeldgroesse = groesse;
            this.anzahlSchiffeTyp = schiffe;
        
            System.out.println("size: " + groesse);
        
            for(int i = 0; i < 4; i++){
                System.out.println("Anzahl " + i + "er: " + anzahlSchiffeTyp[i]);
            }
            dieSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); 
            dieSpielSteuerung.erzeugeEigeneSchiffe();
        }
    }
}
