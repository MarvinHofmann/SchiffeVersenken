/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controll.Steuerung;
import controll.SteuerungSchiffeSetzen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import schiffeversenken.SchiffeVersenken;
import shapes.Grid;
import shapes.Schiff;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SchiffeSetzenController implements Initializable {

    @FXML
    private Pane schiffeSetzenFeld;
    
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SchiffeSetzen");
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(this);
    }

    //anzahlschiffe[2er,3er,4er,5er] jeweils als GZ -> 3*4er 1*2er [1,0,3,0]
    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        System.out.println("Übergabe Spielfeldgroesse und Anzahl der jeweiligen Schiffstypen");
        dieSteuerungSchiffeSetzen.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp);  
    }

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        SpielGUIController spielGUIController = loader.getController();
        spielGUIController.uebergebeInformationen(gridS.getKachelAnzahl(), schiffArray); // Hier noch zusätzlich koordinaten array übergeben

        Scene scene = new Scene(root);

        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);      // Scene setzen

        SchiffeVersenken.getApplicationInstance().getStage().show();
        */
        schiffeSetzenFeld.getScene().getRoot().setOnKeyPressed(dieSteuerungSchiffeSetzen);
    }
  

    public void zeigeGrid(Rectangle rectangle) {
        schiffeSetzenFeld.getChildren().add(rectangle);
    }

    public void zeigeSchiffe(Schiff schiff) {
        schiffeSetzenFeld.getChildren().add(schiff);
    }


    //Verwalten des Zustands losgelassen
    

    public void setzeCursor(Cursor CLOSED_HAND) {
        schiffeSetzenFeld.setCursor(Cursor.CLOSED_HAND);

    }

}
