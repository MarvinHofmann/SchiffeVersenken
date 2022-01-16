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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import schiffeversenken.SchiffeVersenken;

/**
 * FXML Controller class
 *
 * @author marvi
 */
public class AnlController implements Initializable {

    @FXML
    private Button btnLeft;
    @FXML
    private Button btnRight;
    @FXML
    private ImageView linkesGif;
    @FXML
    private ImageView rechtesGif;
    @FXML
    private Label labelLinks;
    @FXML
    private Label labelRechts;

    private static int anleitungsZaehler = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnLeft.setRotate(180);
        zeigeAnleitung(0);
        System.out.println("anlt");
        labelLinks.setMaxWidth(180);
        labelRechts.setMaxWidth(180);
        labelLinks.setWrapText(true);
        labelRechts.setWrapText(true);
    }

    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    @FXML
    private void vorherigeAnleitung(ActionEvent event) {
        anleitungsZaehler--;
        zeigeAnleitung(anleitungsZaehler);
    }

    @FXML
    private void naechsteAnleitung(ActionEvent event) {
        anleitungsZaehler++;
        zeigeAnleitung(anleitungsZaehler);
    }

    public void zeigeAnleitung(int who) {
         
        switch (who) {
            case 0:
                Image i = new Image(new File("./src/Images/gif1Links.gif").toURI().toString());
                Image iRight = new Image(new File("./src/Images/gif1Links.gif").toURI().toString());
                linkesGif.setImage(i);
                rechtesGif.setImage(iRight);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
                break;
            case 1:
                Image zweiLinks = new Image(new File("./src/Images/giphy1.gif").toURI().toString());
                Image zweiRechts = new Image(new File("./src/Images/giphy1.gif").toURI().toString());
                linkesGif.setImage(zweiLinks);
                rechtesGif.setImage(zweiRechts);
                labelLinks.setText("2.er Text");
                labelRechts.setText("2.erTextRecht");
                break;
            case 2:
                Image dreiLinks = new Image(new File("./src/Images/gif1Links.gif").toURI().toString());
                Image dreiRechts = new Image(new File("./src/Images/gif1Links.gif").toURI().toString());
                linkesGif.setImage(dreiLinks);
                rechtesGif.setImage(dreiRechts);
                labelLinks.setText("3.er Text");
                labelRechts.setText("3.erTextRecht");
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }

}
