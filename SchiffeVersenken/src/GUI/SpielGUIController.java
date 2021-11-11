/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controll.Steuerung;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
        dieSteuerung = new Steuerung(this);
    }    

    void uebergebeInformationen(int spielfeldgroesse, Schiff[] sA) {
        System.out.println("Übergabe Spielfeldgroesse und Koordinaten Schiffe");
        dieSteuerung.setSpielfeldgroesse(spielfeldgroesse);
    }
    
}
