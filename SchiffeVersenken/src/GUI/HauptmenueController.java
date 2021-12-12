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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import schiffeversenken.SchiffeVersenken;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class HauptmenueController implements Initializable {

    @FXML
    private Button settingsButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Hauptmenü");
    }    

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {       
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/ModiMenue.fxml");
    }

    @FXML
    private void handleButtonAnleitung(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Anleitung.fxml");
    }

    @FXML
    private void handleButtonBeenden(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleButtonSettings(ActionEvent event) {
    }
}
