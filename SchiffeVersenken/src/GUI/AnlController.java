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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private boolean geladen = false;
    private static int anleitungsZaehler = 0;
    //Images einmal Global Speichern, dann muss nur einmal geladen werden 
    Image i1;
    Image i2;
    Image zweiLinks;
    Image zweiRechts;
    Image gifSetz;
    Image gifVier;
    @FXML
    private Label ueberschriftLinks;
    @FXML
    private Label ueberschriftRechts;
    @FXML
    private TextArea labelEins;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLeft.setRotate(180);
         i1 = new Image(this.getClass().getResourceAsStream("/Images/einsLinks.gif")); //Lade erstes Bild
        zeigeAnleitung(0);
        labelLinks.setMaxWidth(190);
        labelRechts.setMaxWidth(190);
        //Erzeugt Zeilenumbruch bei Text
        labelLinks.setWrapText(true);
        labelRechts.setWrapText(true);
    }

    @FXML
    /**
     * Hauptmenue Button
     */
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    @FXML
    /**
     * Button Links um letzte Anleitungskachel zu laden
     * setzt wert wieder auf 3 Für Karusel effekt
     */
    private void vorherigeAnleitung(ActionEvent event) {
        anleitungsZaehler--;
        if (anleitungsZaehler == -1) {
            anleitungsZaehler = 3;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    @FXML
    /**
     * Button rechts um nächste Anleitung zu laden
     */
    private void naechsteAnleitung(ActionEvent event) {
        anleitungsZaehler++;
        if (anleitungsZaehler == 4) {
            anleitungsZaehler = 0;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    /**
     * Zeigt die Verschiedenen Anleitungen an setzt die Bilder und die
     * zugehörigen Texte neu
     *
     * @param who welche Anleitungskachel gewähtl wurde
     */
    public void zeigeAnleitung(int who) {

        switch (who) {
            case 0: //Erste Aleitungskachel
                setEinheiten(true, false, true, false,true);
                ueberschriftLinks.setText("Modus Auswählen");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(544);
                linkesGif.setFitWidth(314);
                linkesGif.setImage(i1);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam");
                break;
            case 1://Zweite Aleitungskachel
                setEinheiten(true, true, true, true,false);
                ueberschriftLinks.setText("Größe Wählen");
                ueberschriftRechts.setText("Schiffstypen Wählen");
                linkesGif.setFitHeight(544);
                linkesGif.setFitWidth(314);
                linkesGif.setImage(i2);
                rechtesGif.setImage(zweiRechts);
                labelLinks.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                break;
            case 2://Dritte Aleitungskachel
                setEinheiten(true, false, false, true,false);
                ueberschriftLinks.setText("Schiffe Setzten");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(500);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(gifSetz);
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                break;
            case 3://Vierte Aleitungskachel
                setEinheiten(true, false, false, true,false);
                ueberschriftLinks.setText("Schießen");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(500);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(gifVier);
                labelRechts.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                break;
            case 4:
                break;
        }

    }
    
    /**
     * Setzt die Anzeigenenden fx Objekter der Scene visible oder nicht, je nach dem ob zwei Images 
     * und zwei text geladen werden, oder nicht 
     * @param gifL setzt Image View für Linkes Gif
     * @param gifR setzt Image View für Linkes Gif
     * @param lL setzt label für Linkes Gif
     * @param lR setzt label für Linkes Gif
     * @param ersterRech setzt Sondertext für erste Anleitungsseite 
     */
    public void setEinheiten(boolean gifL, boolean gifR, boolean lL, boolean lR, boolean ersterRech){
        linkesGif.setVisible(gifL);
        rechtesGif.setVisible(gifR);
        labelLinks.setVisible(lL);
        labelRechts.setVisible(lR);
        labelEins.setVisible(ersterRech);
    }

    @FXML
    /**
     * lädt die Bilder im Hintergrund, wenn eine Mausbewegung in der Anleitung
     * erkannt wird. Die Anleitung Scene lädt wesentlich schneller
     *
     * @param event Das Maus event , dass die Funktion triggert
     */
    private void ladeBilder(MouseEvent event) throws InterruptedException {
        if (geladen == false) {
            gifVier = new Image(this.getClass().getResourceAsStream("/Images/gifVier.gif"));
            
            gifSetz = new Image(this.getClass().getResourceAsStream("/Images/drittesGIF.gif"));
            
            i2 = new Image(this.getClass().getResourceAsStream("/Images/einsRechts.gif"));
            
            zweiRechts = new Image(this.getClass().getResourceAsStream("/Images/zweiLinks.gif"));
            geladen = true;
        }
    }

}
