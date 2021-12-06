/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import schiffeversenken.SchiffeVersenken;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class ModiMenueController implements Initializable {
    // 30% abgerundet bei Typ Schiffe 
    @FXML
    private Pane schiffeWaehlen;
    @FXML
    private Pane spielbrettWaehlen;
    @FXML
    private Pane modiWaehlen;
    
    @FXML
    private CheckBox checkboxKISpiel;
    @FXML
    private CheckBox checkboxHostKI;
    @FXML
    private CheckBox checkboxClientKI;
    @FXML
    private TextField eingabeIPKI;
    @FXML
    private Label labelEigeneIPKI;
    
    @FXML
    private CheckBox checkboxLokalesSpiel;
    
    @FXML
    private CheckBox checkboxOnlineSpiel;
    @FXML
    private CheckBox checkboxHost;
    @FXML
    private CheckBox checkboxClient;
    @FXML
    private TextField eingabeIP;
    @FXML
    private Label labelEigeneIP;
    
    //private TextField auswahlSpielfeldGroesse;
    
    @FXML
    private Label labelIstAnzahl;
    @FXML
    private Label labelSollAnzahl;
    @FXML
    private TextField eingabeZweier;
    @FXML
    private TextField eingabeDreier;
    @FXML
    private TextField eingabeVierer;
    @FXML
    private TextField eingabeFuenfer;
    
    int spielfeldgroesse = 0;
    int[] anzahlSchiffe = new int[4]; // Stelle 0->Zweier, 1->Dreier, 2->Vierer, 3->Fuenfer
    int modus; // 0-> Default, 1-> Lokales Spiel, 2-> KI-Spiel, 3-> OnlineSpiel, 21 -> KISpiel als Host, 22 -> KISpiel als Client, 31 -> OnlineSpiel als Host, 32 -> OnlineSpiel als Client
    String ipAdresse;
    int schiffsteile;
    int benoetigt;
    int anzahl;
    
    
    
    @FXML
    private ComboBox<Integer> dropdown;
    @FXML
    private Label labelbenötigt;
    @FXML
    private Label auswahlSpielfeldgroesse;
    @FXML
    private Label auswahlSchiffePanel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Modimenü");
        
        setKISpielNichtAktiv();
        setOnlineSpielNichtAktiv();
        labelbenötigt.setVisible(false);
        initZahlenwerte();
    }    
    
    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }
    
    @FXML
    private void handleOnlineSpiel(ActionEvent event) {
        if(checkboxOnlineSpiel.isSelected()){
            checkboxHost.setVisible(true);
            checkboxClient.setVisible(true); 
            checkboxLokalesSpiel.setSelected(false);
            checkboxKISpiel.setSelected(false);
            setKISpielNichtAktiv();
            modus = 3;
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
        else{
            setOnlineSpielNichtAktiv();
        }
    }

    @FXML
    private void handleKISpiel(ActionEvent event) {
        if(checkboxKISpiel.isSelected()){
            checkboxHostKI.setVisible(true);
            checkboxClientKI.setVisible(true); 
            checkboxLokalesSpiel.setSelected(false);
            checkboxOnlineSpiel.setSelected(false);
            setOnlineSpielNichtAktiv();
            modus = 2;
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
        else{
            setKISpielNichtAktiv();
            //Änderung: zurücksetzen des Hintergunds bei anderer checkbox
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
    }

    @FXML
    private void handleLokalesSpiel(ActionEvent event) {
        if(checkboxLokalesSpiel.isSelected()){
            checkboxOnlineSpiel.setSelected(false);
            setOnlineSpielNichtAktiv();
            checkboxKISpiel.setSelected(false);
            setKISpielNichtAktiv();
            modus = 1;
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
    }

    @FXML
    private void handleHost(ActionEvent event) {
        if(checkboxHost.isSelected()){
            labelEigeneIP.setVisible(true); 
            checkboxClient.setSelected(false);
            eingabeIP.setVisible(false);
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
            
            modus = 31;
            
        }
        else{
            labelEigeneIP.setVisible(false);
        }
    }

    @FXML
    private void handleClient(ActionEvent event) {
        if(checkboxClient.isSelected()){
            eingabeIP.setVisible(true);
            checkboxHost.setSelected(false);
            labelEigeneIP.setVisible(false);
            
            //Änderug: ausgrauen des Hintergunds bei Client 
            spielbrettWaehlen.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            auswahlSpielfeldgroesse.setStyle("-fx-text-fill: grey");
            schiffeWaehlen.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            eingabeZweier.setEditable(false);eingabeDreier.setEditable(false);eingabeVierer.setEditable(false);eingabeFuenfer.setEditable(false);
            auswahlSchiffePanel.setStyle("-fx-text-fill: grey");
            dropdown.setDisable(true);
            modus = 32;
            
        }
        else{
            eingabeIP.setVisible(false);
            //Änderung: zurücksetzen Hintergrund bei anderer Checkbox
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            auswahlSchiffePanel.setStyle(null);
            auswahlSpielfeldgroesse.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
    }

    @FXML
    private void handleHostKI(ActionEvent event) {
        if(checkboxHostKI.isSelected()){
            labelEigeneIPKI.setVisible(true);
            checkboxClientKI.setSelected(false);
            eingabeIPKI.setVisible(false);
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
            
            modus = 21;
            
        }
        else{
            labelEigeneIPKI.setVisible(false);
        }
    }

    @FXML
    private void handleClientKI(ActionEvent event) {
        if(checkboxClientKI.isSelected()){
            eingabeIPKI.setVisible(true);
            checkboxHostKI.setSelected(false);
            labelEigeneIPKI.setVisible(false);
            
            //Änderug: ausgrauen des Hintergunds bei Client 
            spielbrettWaehlen.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            auswahlSpielfeldgroesse.setStyle("-fx-text-fill: grey");
            schiffeWaehlen.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            eingabeZweier.setEditable(false);eingabeDreier.setEditable(false);eingabeVierer.setEditable(false);eingabeFuenfer.setEditable(false);
            auswahlSchiffePanel.setStyle("-fx-text-fill: grey");
            dropdown.setDisable(true);
            
            modus = 22;
            
        }
        else{
            eingabeIPKI.setVisible(false);
            //Änderung: zurücksetzen Hintergrund bei anderer Checkbox
            spielbrettWaehlen.setBackground(null);
            spielbrettWaehlen.setStyle(null);
            schiffeWaehlen.setBackground(null);
            schiffeWaehlen.setStyle(null);
            auswahlSchiffePanel.setStyle(null);
            auswahlSpielfeldgroesse.setStyle(null);
            eingabeZweier.setEditable(true);eingabeDreier.setEditable(true);eingabeVierer.setEditable(true);eingabeFuenfer.setEditable(true);
            dropdown.setDisable(false);
        }
    }
    
    private void setOnlineSpielNichtAktiv(){
        checkboxHost.setVisible(false);
        checkboxClient.setVisible(false);
        labelEigeneIP.setVisible(false);
        eingabeIP.setVisible(false);
        checkboxHost.setSelected(false);
        checkboxClient.setSelected(false);
    }
    
    private void setKISpielNichtAktiv(){
        checkboxHostKI.setVisible(false);
        checkboxClientKI.setVisible(false);
        labelEigeneIPKI.setVisible(false);
        eingabeIPKI.setVisible(false);
        checkboxHostKI.setSelected(false);
        checkboxClientKI.setSelected(false);
    }
  
    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        //checken ob richtige SPielfeldgröße, Modus und Anzahl der Schiffe ausgewählt wurde
        if ((modus == 1 || modus == 21 || /*modus == 22 || modus == 32 ||*/modus == 31) && spielfeldgroesse >= 5 && spielfeldgroesse <= 30 && benoetigt == anzahl) {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        SpielGUIController spielGUIController = loader.getController();
        spielGUIController.uebergebeInformationen(spielfeldgroesse, anzahlSchiffe, modus, ipAdresse);
        
        Scene scene = new Scene(root);
        
        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
        SchiffeVersenken.getApplicationInstance().getStage().show();
        
        //checken ob Client Ip angegeben wurde falls anderer modus, IP Adresse Aufbau: zahl.zahl.zahl.zahl, hierbei 0 <= zahl <= 255  
        } else if (modus == 22 || modus == 32 && ipAdresse != null) {
            
            int anzahlIp = 0;
            String[] split = ipAdresse.split("\\.");
            
        //checken ob an Stelle split[0-3] die Zahlen zwischen 0 <= zahl <= 255 liegen
            for (int i=0; i<split.length; i++) {
                
                if(Integer.parseInt(split[i]) >= 0 && Integer.parseInt(split[i]) <= 255) {
                   anzahlIp++;
                  //checken ob es split[] aus 4 Stellen besteht für die richtige Form der Ip Adresse 
                   if(anzahlIp == 4) {
                      
                       FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
                       Parent root = loader.load();
                       SpielGUIController spielGUIController = loader.getController();
                       spielGUIController.uebergebeInformationen(spielfeldgroesse, anzahlSchiffe, modus, ipAdresse);
        
                       Scene scene = new Scene(root);
        
                       SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);      // Scene setzen
                       SchiffeVersenken.getApplicationInstance().getStage().show();
                   }
                }
            }
        }
    }

    @FXML
    private void aktualisiereAnzahlZweier(ActionEvent event) {
        anzahlSchiffe[0] = Integer.parseInt(eingabeZweier.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
        double jetzigerStand = spielfeldgroesse*spielfeldgroesse*0.3;
        benoetigt = (int) jetzigerStand;
        anzahl = anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5;
        labelbenötigt.setVisible(true);
        
        anzahlSchiffe();
    }

    @FXML
    private void aktualisiereAnzahlDreier(ActionEvent event) {
        anzahlSchiffe[1] = Integer.parseInt(eingabeDreier.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
        double jetzigerStand = spielfeldgroesse*spielfeldgroesse*0.3;
        benoetigt = (int) jetzigerStand;
        anzahl = anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5;
        labelbenötigt.setVisible(true);
        
        anzahlSchiffe();
    }

    @FXML
    private void aktualisiereAnzahlVierer(ActionEvent event) {
        anzahlSchiffe[2] = Integer.parseInt(eingabeVierer.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
        double jetzigerStand = spielfeldgroesse*spielfeldgroesse*0.3;
        benoetigt = (int) jetzigerStand;
        anzahl = anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5;
        labelbenötigt.setVisible(true);
        
        anzahlSchiffe();
    }

    @FXML
    private void aktualisiereAnzahlFuenfer(ActionEvent event) {
        anzahlSchiffe[3] = Integer.parseInt(eingabeFuenfer.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
        double jetzigerStand = spielfeldgroesse*spielfeldgroesse*0.3;
        benoetigt = (int) jetzigerStand;
        anzahl = anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5;
        labelbenötigt.setVisible(true);
        
        anzahlSchiffe();
    }

    /*private void aktualisiereSpielfeldgroesse(ActionEvent event) {
        spielfeldgroesse = Integer.parseInt(auswahlSpielfeldGroesse.getText());
        labelSollAnzahl.setText("Insgesamt " + spielfeldgroesse*spielfeldgroesse*0.3 + " Schiffsteile");
    }*/

    @FXML
    private void setzeIpAdresseOnline(ActionEvent event) {
        ipAdresse = eingabeIP.getText();
    }

    @FXML
    private void setzeIpAdresseKI(ActionEvent event) {
        ipAdresse = eingabeIPKI.getText();
    }

    @FXML
    private void aktualisiereSpielfeldgroeße(ActionEvent event) {
        spielfeldgroesse = dropdown.getValue();
        double jetzigerStand = spielfeldgroesse*spielfeldgroesse*0.3;
        anzahl = (int) jetzigerStand;
        schiffsteile = anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5;
        labelSollAnzahl.setText("Du benötigst " + anzahl + " Schiffsteile!");
    }
    
    private void anzahlSchiffe() {
        
        if(anzahl == benoetigt) {
            labelbenötigt.setText("Anzahl erreicht!");
        }
        else if (anzahl > benoetigt) {
            labelbenötigt.setText("Zu viele Schiffsteile!");
        } else {
            labelbenötigt.setText("Zu wenig Schiffsteile!");
        }
    }
    
    private void initZahlenwerte() {
        
        for(int i = 5; i<31; i++) {
            dropdown.getItems().add(i);
        }
        
    }
}
