package GUI;

import Server.Server;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import shapes.Richtung;
import shapes.Schiff;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SpielGUIController implements Initializable {

    private LokalesSpielSteuerung dieLokalesSpielSteuerung = null;
    private KISpielSteuerung dieKISpielSteuerung = null;
    private OnlineSpielSteuerung dieOnlineSpielSteuerung = null;

    private int modus;
    private String ip = null; // Null wenn Lokales Spiel 
    private FileChooser fc = new FileChooser();
    private int kiStufe;
    
    @FXML
    private Label outputField;

    @FXML
    private Button btn_neuPlatzieren;
    @FXML
    private Button btn_Random;
    @FXML
    private Pane paneGrid;
    @FXML
    private Pane setzenControll;
    @FXML
    private Button spielstart;
    private Button clientWartet;
    @FXML
    private Pane boundsRec;
    @FXML
    private Rectangle borderRec;
    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
        //clientWartet.setVisible(false);
        saveButton.setVisible(false);
        fc.setInitialDirectory(new File("src/saves/"));
    }

    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip, int kiStufe) {
        //System.out.println("Übergabe Spielfeldgroesse, Anzahl der jeweiligen Schiffstypen und Modus: " + modus);
        this.modus = modus;
        this.ip = ip;

        if (modus == 1) { // Lokales Spiel 
            dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe); // Erzeuge SpielSteuerung
            dieLokalesSpielSteuerung.erzeugeEigeneSchiffe();
        } else if (modus == 21 || modus == 22) { // KI Spiel - 21 ki-host - 22 ki-client 
            if (modus == 21) { // host
                dieKISpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe);
                dieKISpielSteuerung.erzeugeEigeneSchiffe();
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung.werdeServer();
                if (dieKISpielSteuerung.isFertigSetzen()) {
                    dieKISpielSteuerung.setSchiffeSetzen();
                    dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts());
                    dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks());
                    dieKISpielSteuerung.getGridSpielfeldRechts().print();
                    dieKISpielSteuerung.getGridSpielfeldLinks().print();
                    dieKISpielSteuerung.setzeSchiffeKI();
                }
            } else if (modus == 22) { // client
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung = new KISpielSteuerung(this);
                this.kiStufe = kiStufe;
                dieKISpielSteuerung.werdeClient();
                try{ // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!dieKISpielSteuerung.getClient().isVerbindung()){
                    clientWartet.setVisible(true);
                    spielstart.setVisible(false);
                    setzenControll.setVisible(false);
                }
            }
        } else if (modus == 31 || modus == 32) { // Online Spiel - 31 host - 32 client
            if (modus == 31) { // host
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp);
                dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                dieOnlineSpielSteuerung.werdeServer();
            } else if (modus == 32) { // client
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this);
                dieOnlineSpielSteuerung.werdeClient();
                try{ // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(!dieOnlineSpielSteuerung.getClient().isVerbindung()){
                    clientWartet.setVisible(true);
                    spielstart.setVisible(false);
                    setzenControll.setVisible(false);
                }
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

    public void zeigeGridRechts(Rectangle rectangle) {
        paneGrid.getChildren().add(rectangle);
    }
    
    public void zeigeGridLinks(Rectangle rectangle) {
        paneGrid.getChildren().add(rectangle);
    }

    public void zeigeSchiffeLinks(Schiff schiff) {
        paneGrid.getChildren().add(schiff);
    }
    
    public void zeigeSchiffeRechts(Schiff schiff) {
        paneGrid.getChildren().add(schiff);
    }

    public void zeigeSchiffLinks(Rectangle rec) {
        paneGrid.getChildren().add(rec);
    }
    
    public void zeigeSchiffRechts(Rectangle rec) {
        paneGrid.getChildren().add(rec);
    }

    public Pane getBoundsRec() {
        return boundsRec;
    }

    public Rectangle getBorderRec() {
        return borderRec;
    }
    
    public void zeichneSchiffe(Schiff schiff) {
        if (dieKISpielSteuerung != null) {
            if(schiff.getRichtung() == Richtung.HORIZONTAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootH"+(int)schiff.getLaenge() + (int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()+i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootV" +(int)schiff.getLaenge()+ (int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY()+i].setFill(new ImagePattern(img));
                }
            }
        } 
        else if (dieOnlineSpielSteuerung != null) {
            if(schiff.getRichtung() == Richtung.HORIZONTAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootH" + (int)schiff.getLaenge() +(int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()+i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootV" +(int)schiff.getLaenge()+  (int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY()+i].setFill(new ImagePattern(img));
                }
            }
        } 
        else if (dieLokalesSpielSteuerung != null) {
            if(schiff.getRichtung() == Richtung.HORIZONTAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootH" + (int)schiff.getLaenge() + (int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()+i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){
                for(int i = 0; i < schiff.getLaenge(); i++){
                    String s = "/Images/bootV" + (int)schiff.getLaenge() + (int)(i+1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY()+i].setFill(new ImagePattern(img));
                }
            }
        }

        
    }

    public void zeigeLinie(Line line) {
        paneGrid.getChildren().add(line);
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
            dieKISpielSteuerung.erzeugeKI(kiStufe);
            dieKISpielSteuerung.erzeugeEigeneSchiffe();
            
            if (dieKISpielSteuerung.isFertigSetzen()){
                dieKISpielSteuerung.setSchiffeSetzen();
                //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts()); //hier wird gezeichnet :)
                        dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks());
                        dieKISpielSteuerung.getGridSpielfeldRechts().print();
                        dieKISpielSteuerung.getGridSpielfeldLinks().print();
                        dieKISpielSteuerung.setzeSchiffeKI(); //hier auch
                    }
                });
            }

        } 
        else if (modus == 32) {
            System.out.println(dieOnlineSpielSteuerung.getClient().isVerbindung());
            if(dieOnlineSpielSteuerung.getClient().isVerbindung()){
                dieOnlineSpielSteuerung.erzeugeSteuerungSchiffeSetzen();
            
            //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                    }
                });
            }  
        }
    }

    @FXML
    private void handleButton(ActionEvent event) {
        saveButton.setVisible(true);
        if ((dieLokalesSpielSteuerung instanceof LokalesSpielSteuerung && dieLokalesSpielSteuerung.isFertigSetzen())) {
            dieLokalesSpielSteuerung.erzeugeGegnerSchiffe();
            if(dieLokalesSpielSteuerung.gegnerKiIsFertig()){
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setBorder(Border.EMPTY);
                spielstart.setVisible(false);
                //spielFeld.setStyle("-fx-background-image: ");
                dieLokalesSpielSteuerung.setSchiffeSetzen();

                dieLokalesSpielSteuerung.setGridSpielfeldSpielRechts(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieLokalesSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieLokalesSpielSteuerung.setGridSpielfeldSpielLinks(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieLokalesSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                //dieLokalesSpielSteuerung.getGridSpielfeldRechts().print();
                dieLokalesSpielSteuerung.getGridSpielfeldLinks().print();
                dieLokalesSpielSteuerung.beginneSpiel();
            }
        } else if (dieOnlineSpielSteuerung instanceof OnlineSpielSteuerung && dieOnlineSpielSteuerung.isFertigSetzen()) {
            if(modus == 31 && dieOnlineSpielSteuerung.getServer().isVerbindung()){  
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieOnlineSpielSteuerung.setSchiffeSetzen();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieOnlineSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                //dieOnlineSpielSteuerung.getGridSpielfeldRechts().print();
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print();
                dieOnlineSpielSteuerung.beginneSpiel();
            }
            else if(modus == 32 && dieOnlineSpielSteuerung.getClient().isVerbindung()){
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieOnlineSpielSteuerung.setSchiffeSetzen();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieOnlineSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                //dieOnlineSpielSteuerung.getGridSpielfeldRechts().print();
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print();
                dieOnlineSpielSteuerung.beginneSpiel();
            }
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

    private void handleButtonWarten(ActionEvent event) {
        if (modus == 32) {
            dieOnlineSpielSteuerung.werdeClient();
            try{ // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
            }
            if(dieOnlineSpielSteuerung.getClient().isVerbindung()){
                clientWartet.setVisible(false);
                spielstart.setVisible(true);
                setzenControll.setVisible(true);
            }
        }
        else if (modus == 22) {
            dieKISpielSteuerung.werdeClient();
            try{ // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
            }
            if(dieKISpielSteuerung.getClient().isVerbindung()){
                clientWartet.setVisible(false);
                spielstart.setVisible(true);
                setzenControll.setVisible(true);
            }
        }
    }

    public void spielEnde(int gewinner) { // 1 Spieler, 2 Gegner
        paneGrid.getChildren().clear();
        if(gewinner == 2){
            System.out.println("Gewonnen");
        }
        else{
            System.out.println("Verloren");
        }
    }

    @FXML
    private void speicherSpiel(ActionEvent event) {
        fc.setTitle("Speichern");
           fc.setInitialFileName("Speicherstand");
           fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("text file", "*.txt"));
           
           try{
               File save = fc.showSaveDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
               
               if(save != null){
                   SaveLoad.SaveLoad.write2File(save, "SpeichernABCD");
               }
           }
           catch(Exception e){
               e.printStackTrace();
           }
    }
}
