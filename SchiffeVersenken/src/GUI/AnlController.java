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
    Image speichern;
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
            anleitungsZaehler = 4;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    @FXML
    /**
     * Button rechts um nächste Anleitung zu laden
     */
    private void naechsteAnleitung(ActionEvent event) {
        anleitungsZaehler++;
        if (anleitungsZaehler == 5) {
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
                labelLinks.setText("Bevor das Spiel beginnen kann, müssen Voreinstellungen getroffen werden. Diese können in dem Modi-Menü eingestellt " +
                "werden. Im ersten Feld kann der Modus gewählt werden. Hier gibt es drei verschiedene zur Auswahl, die Sie links einsehen können. "
                + "Wenn Sie in einem der Spielmodi der Client sind, dann müssen Sie keine Einstellungen mehr vornehmen außer die eventuelle Einstellung der KI Stärke und die Ip-Adresse. ");
                break;
            case 1://Zweite Aleitungskachel
                setEinheiten(true, true, true, true,false);
                ueberschriftLinks.setText("Größe Wählen");
                ueberschriftRechts.setText("Schiffstypen Wählen");
                linkesGif.setFitHeight(544);
                linkesGif.setFitWidth(314);
                linkesGif.setImage(i2);
                rechtesGif.setImage(zweiRechts);
                labelLinks.setText("Nachdem der Modus, durch das drücken der Checkbox eingestellt wurde, geht es weiter zur Auswahl " +
                "der Spielfeldgröße. Hier sind Größen von 5x5 - 30x30 möglich. Wenn einer der Modi \"Spieler vs KI\" oder \"KI vs KI\" " +
                "ausgewählt ist, kann hier auch die KI Schwierigkeit eingestellt werden. " +
                "Diese geht von 1 (leicht) bis zu 3 (schwierig).");
                labelRechts.setText("Als letzte Einstellung kommt die Anzahl der Schiffe. Hier gibt es eine bereits eingestellte Voreinstellung, die aber " +
                "beliebig abgeändert werden kann. Die Texte unterhalb der Auswahl geben an, ob die benötigte Anzahl an " +
                "Schiffen erreicht ist und wenn nicht, wie viele Schiffe zu viel oder zu wenig ausgewählt worden sind. Hier kann auch ein bereits angefangenes Spiel geladen werden mit "
                        + "dem Button \"Laden\".");
                break;
            case 2://Dritte Aleitungskachel
                setEinheiten(true, false, false, true,false);
                ueberschriftLinks.setText("Schiffe Setzten");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(gifSetz);
                labelRechts.setText("Nachdem das Spiel gestartet wurde, können nun die einzelnen Schiffe plaziert werden. Diese können entweder zufällig plaziert " +
                "werden über den Button \"Zufällig setzen\" oder manuell per Click & Drop. Eine Kombination beider ist auch möglich. " +
                "Hierbei muss aber beachtet werden, dass die " +
                "Schiffe mindestens ein Feld Abstand zueinander haben und auch nicht diagonal gesetzt werden können.");
                break;
            case 3://Vierte Aleitungskachel
                setEinheiten(true, false, false, true,false);
                ueberschriftLinks.setText("Schießen");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(gifVier);
                labelRechts.setText("Die Spieler schiessen abwechselnd auf das gegenüberliegende Feld. Wenn ein Schiffsteil getroffen wurde, bekommt der Spieler einen " +
                "weiteren Zug und darf nochmals schiessen. Das Spiel endet in einem Sieg, wenn alle Schiffe des Gegners versenkt wurden. " +
                "Es ist möglich den Spielstand von einem Spiel zu speichern, indem " +
                "der Button \"Speichern\" betätigt wird. Wenn das Spiel endet kann wieder ein neues Spiel gestartet oder geladen werden.");
                break;
            case 4:
                setEinheiten(true, false, false, true,false);
                ueberschriftLinks.setText("Speichern und Laden");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(speichern);
                labelRechts.setText("Die Spieler schiessen abwechselnd auf das gegenüberliegende Feld. Wenn ein Schiffsteil getroffen wurde, bekommt der Spieler einen " +
                "weiteren Zug und darf nochmals schiessen. Das Spiel endet in einem Sieg, wenn alle Schiffe des Gegners versenkt wurden. " +
                "Es ist möglich den Spielstand von einem Spiel zu speichern, indem " +
                "der Button \"Speichern\" betätigt wird. Wenn das Spiel endet kann wieder ein neues Spiel gestartet oder geladen werden.");
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
            speichern = new Image(this.getClass().getResourceAsStream("/Images/SpeichernLaden.gif"));
            zweiRechts = new Image(this.getClass().getResourceAsStream("/Images/zweiLinks.gif"));
            geladen = true;
        }
    }

}
