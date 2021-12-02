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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import schiffeversenken.SchiffeVersenken;
import javafx.scene.layout.Pane;

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

    @FXML
    private TextField auswahlSpielfeldGroesse;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Modimenü");
        
        setKISpielNichtAktiv();
        setOnlineSpielNichtAktiv();
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
            //System.out.println("Modus: " + modus);
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
            //System.out.println("Modus: " + modus);
        }
        else{
            setKISpielNichtAktiv();
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
            //System.out.println("Modus: " + modus);
        }
    }

    @FXML
    private void handleHost(ActionEvent event) {
        if(checkboxHost.isSelected()){
            labelEigeneIP.setVisible(true); 
            checkboxClient.setSelected(false);
            eingabeIP.setVisible(false);
            modus = 31;
            //System.out.println("Modus: " + modus);
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
            modus = 32;
            //System.out.println("Modus: " + modus);
        }
        else{
            eingabeIP.setVisible(false);
        }
    }

    @FXML
    private void handleHostKI(ActionEvent event) {
        if(checkboxHostKI.isSelected()){
            labelEigeneIPKI.setVisible(true);
            checkboxClientKI.setSelected(false);
            eingabeIPKI.setVisible(false);
            modus = 21;
            //System.out.println("Modus: " + modus);
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
            modus = 22;
            //System.out.println("Modus: " + modus);
        }
        else{
            eingabeIPKI.setVisible(false);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        //Auslesen der IpAdresse für die Übergabe
        ipAdresse = eingabeIPKI.getText();
        SpielGUIController spielGUIController = loader.getController();
        spielGUIController.uebergebeInformationen(spielfeldgroesse, anzahlSchiffe, modus, ipAdresse);
        
        Scene scene = new Scene(root);
        
        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
        SchiffeVersenken.getApplicationInstance().getStage().show();
        
    }

    @FXML
    private void aktualisiereAnzahlZweier(ActionEvent event) {
        anzahlSchiffe[0] = Integer.parseInt(eingabeZweier.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
    }

    @FXML
    private void aktualisiereAnzahlDreier(ActionEvent event) {
        anzahlSchiffe[1] = Integer.parseInt(eingabeDreier.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
    }

    @FXML
    private void aktualisiereAnzahlVierer(ActionEvent event) {
        anzahlSchiffe[2] = Integer.parseInt(eingabeVierer.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
    }

    @FXML
    private void aktualisiereAnzahlFuenfer(ActionEvent event) {
        anzahlSchiffe[3] = Integer.parseInt(eingabeFuenfer.getText());
        labelIstAnzahl.setText((anzahlSchiffe[0]*2 + anzahlSchiffe[1]*3 + anzahlSchiffe[2]*4 + anzahlSchiffe[3]*5) + " Schiffsteile ausgewählt");
    }

    @FXML
    private void aktualisiereSpielfeldgroesse(ActionEvent event) {
        spielfeldgroesse = Integer.parseInt(auswahlSpielfeldGroesse.getText());
        labelSollAnzahl.setText("Insgesamt " + spielfeldgroesse*spielfeldgroesse*0.3 + " Schiffsteile");
    }
}
