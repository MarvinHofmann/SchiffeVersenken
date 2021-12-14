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
import schiffeversenken.SchiffeVersenken;

/**
 * FXML Controller class
 *
 * @author marvi
 */
public class CreditsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException  {
        System.out.println("Btton use");
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Einstellungen.fxml");
    }
    
}
