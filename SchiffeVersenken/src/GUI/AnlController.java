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
    Image i1;
    Image i2;
    Image zweiLinks;
    Image zweiRechts;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLeft.setRotate(180);
        zeigeAnleitung(0);
        labelLinks.setMaxWidth(180);
        labelRechts.setMaxWidth(180);
        labelLinks.setWrapText(true);
        labelRechts.setWrapText(true);
        load();
    }

    public void load() {
        i1 = new Image(this.getClass().getResourceAsStream("/Images/gifLinksEins.gif"));
        i2 = new Image(this.getClass().getResourceAsStream("/Images/dreiLinks.gif"));
        zweiLinks = new Image(this.getClass().getResourceAsStream("/Images/dreiLinks.gif"));
        zweiRechts = new Image(this.getClass().getResourceAsStream("/Images/giphy1.gif"));
        linkesGif.setImage(i1);
        rechtesGif.setImage(i2);
    }

    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    @FXML
    private void vorherigeAnleitung(ActionEvent event) {
        anleitungsZaehler--;
        if (anleitungsZaehler == -1) {
            anleitungsZaehler = 3;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    @FXML
    private void naechsteAnleitung(ActionEvent event) {
        anleitungsZaehler++;
        if (anleitungsZaehler == 3) {
            anleitungsZaehler = 0;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    public void zeigeAnleitung(int who) {

        switch (who) {
            case 0:
                linkesGif.setImage(i1);
                rechtesGif.setImage(i2);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
                break;
            case 1:
                linkesGif.setImage(zweiLinks);
                rechtesGif.setImage(zweiRechts);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                break;
            case 2:
                linkesGif.setImage(i1);
                rechtesGif.setImage(i2);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                break;
            case 3:
                break;
            case 4:
                break;
        }

    }

}
