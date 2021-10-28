/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author marvi
 */
public class HauptmenueController implements Initializable {
    
    private Label label;
    @FXML
    private Button startButton;
    @FXML
    private Button buttonAnleitung;
    @FXML
    private Button buttonEnde;
    
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonStart(ActionEvent event) {
        System.out.println("Spieler möchte Starten");
    }

    @FXML
    private void handleButtonAnleitung(ActionEvent event) {
        System.out.println("Spieler möchte Anleitung");
    }

    @FXML
    private void handleButtonBeenden(ActionEvent event) {
        System.exit(0);
    }
    
}
