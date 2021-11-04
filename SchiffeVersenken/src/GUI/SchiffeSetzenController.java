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
import javafx.scene.layout.Pane;
import schiffeversenken.SchiffeVersenken;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SchiffeSetzenController implements Initializable {

    @FXML
    private Pane schiffeSetzenFeld;
    
    int spielfeldgroesse;
    int[] anzahlSchiffe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SchiffeSetzen");
    }    

    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffe) {
        System.out.println("Übergabe Spielfeldgroesse und Anzahl der jeweiligen Schiffstypen");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffe = anzahlSchiffe;
    }

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        SpielGUIController spielGUIController = loader.getController(); 
        spielGUIController.uebergebeInformationen(spielfeldgroesse); // Hier noch zusätzlich koordinaten array übergeben
        
        Scene scene = new Scene(root);
        
        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);      // Scene setzen
        SchiffeVersenken.getApplicationInstance().getStage().show();
    }
    
}
