/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.sun.prism.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import schiffeversenken.SchiffeVersenken;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/**
 * FXML Controller class
 *
 * @author marvi
 */
public class ModiMenueController implements Initializable {

    @FXML
    private Pane modiMenu;
    @FXML
    private Button zurueck;
    @FXML
    private CheckBox KIKI;
    @FXML
    private CheckBox spielerKI;
    @FXML
    private CheckBox onlineSpiel;
    @FXML
    private CheckBox host;
    @FXML
    private TextField textfeld;
    @FXML
    private Pane spielbrett;
    @FXML
    private Pane schiffeWaehlen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        host.setVisible(false);
        textfeld.setVisible(false);
        // TODO
    }    
    
    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }
    
    @FXML
    private void handleOnlineSpiel(ActionEvent event) throws IOException {
        
       host.setVisible(true);
       textfeld.setVisible(true);
            
    }
    
   
   
}
