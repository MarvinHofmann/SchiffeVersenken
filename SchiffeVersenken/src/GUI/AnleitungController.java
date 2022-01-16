/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import schiffeversenken.SchiffeVersenken;


/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class AnleitungController implements Initializable {

    @FXML
    private ImageView iViewLinks;
    @FXML
    private ImageView iViewRechts;
    @FXML
    private Label labelLinks;
    @FXML
    private Label labelRechts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image i = new Image(new File("./src/Images/giphy.gif").toURI().toString());
        Image iRight = new Image(new File("./src/Images/giphy1.gif").toURI().toString());
        iViewLinks.setImage(i);
        iViewRechts.setImage(iRight);
        labelLinks.setMaxWidth(180);
        labelLinks.setWrapText(true);
        labelRechts.setMaxWidth(180);
        labelRechts.setWrapText(true);
        labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
        labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
    }  
    
    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }
    
}
