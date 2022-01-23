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
    @FXML
    private Label ueberschriftLinks;
    @FXML
    private Label ueberschriftRechts;
    @FXML
    private TextArea labelEins;

    private static int anleitungsZaehler = 0;
    private boolean geladen = false;
    private boolean geladenMitte = false;
    //Images einmal Global Speichern, dann muss nur einmal geladen werden 
    Image modusWahl, groeßenWahl, schiffWahl, setzen, spielen, speichern, laden, reconnect;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLeft.setRotate(180);
        modusWahl = new Image(this.getClass().getResourceAsStream("/Images/einsLinks.gif")); //Lade erstes Bild
        zeigeAnleitung(0);
        labelLinks.setMaxWidth(190);
        labelRechts.setMaxWidth(190);
        //Erzeugt Zeilenumbruch bei Text
        labelLinks.setWrapText(true);
        labelRechts.setWrapText(true);
    }

    /**
     * Hauptmenue Button
     */
    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    /**
     * Button Links um letzte Anleitungskachel zu laden setzt wert wieder auf 3
     * Für Karusel effekt
     */
    @FXML
    private void vorherigeAnleitung(ActionEvent event) {
        anleitungsZaehler--;
        if (anleitungsZaehler == -1) {
            anleitungsZaehler = 6;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    /**
     * Button rechts um nächste Anleitung zu laden
     */
    @FXML
    private void naechsteAnleitung(ActionEvent event) {
        anleitungsZaehler++;
        if (anleitungsZaehler == 7) {
            anleitungsZaehler = 0;
        }
        zeigeAnleitung(anleitungsZaehler);
    }

    /**
     * Zeigt die Verschiedenen Anleitungen an setzt die Bilder und die
     * zugehörigen Texte neu
     *
     * @param who welche Anleitungskachel gewählt wurde
     */
    public void zeigeAnleitung(int who) {

        switch (who) {
            case 0: //Erste Anleitungskachel - Moduswahl
                setEinheiten(true, false, true, false, true);
                ueberschriftLinks.setText("Modus Auswählen");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(544);
                linkesGif.setFitWidth(314);
                linkesGif.setImage(modusWahl);
                labelLinks.setText("Bevor das Spiel beginnen kann, müssen Voreinstellungen getroffen werden. Diese können in dem Modi-Menü eingestellt "
                        + "werden. Im ersten Feld kann der Modus gewählt werden. Hier gibt es drei verschiedene zur Auswahl, die Sie links einsehen können. "
                        + "Wenn Sie in einem der Spielmodi der Client sind, dann müssen Sie keine Einstellungen mehr vornehmen außer die eventuelle Einstellung der KI Stärke und die Ip-Adresse. ");
                break;
            case 1://Zweite Anleitungskachel - Größe Wählen
                setEinheiten(true, true, true, true, false);
                ueberschriftLinks.setText("Größe Wählen");
                ueberschriftRechts.setText("Schiffstypen Wählen");
                linkesGif.setFitHeight(544);
                linkesGif.setFitWidth(314);
                linkesGif.setImage(groeßenWahl);
                rechtesGif.setImage(schiffWahl);
                labelLinks.setText("Nachdem der Modus, durch das drücken der Checkbox eingestellt wurde, geht es weiter zur Auswahl "
                        + "der Spielfeldgröße. Hier sind Größen von 5x5 - 30x30 möglich. Zusätzlich ist es Möglich die Größe des Spielfensters in Pixeln zu wählen. Wenn einer der Modi \"Spieler vs KI\" oder \"KI vs KI\" "
                        + "ausgewählt ist, kann hier auch die KI Schwierigkeit eingestellt werden. "
                        + "Diese geht von 1 (leicht) bis zu 3 (schwierig).");
                labelRechts.setText("Als letzte Einstellung kommt die Anzahl der Schiffe. Hier gibt es eine bereits eingestellte Voreinstellung, die aber "
                        + "beliebig abgeändert werden kann. Die Texte unterhalb der Auswahl geben an, ob die benötigte Anzahl an "
                        + "Schiffen erreicht ist und wenn nicht, wie viele Schiffe zu viel oder zu wenig ausgewählt worden sind. Hier kann auch ein bereits angefangenes Spiel geladen werden mit "
                        + "dem Button \"Laden\".");
                break;
            case 2://Dritte Anleitungskachel - Schiffe Setzten
                setEinheiten(true, false, false, true, false);
                ueberschriftLinks.setText("Schiffe Setzten");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(setzen);
                labelRechts.setText("Nachdem das Spiel gestartet wurde, können nun die einzelnen Schiffe plaziert werden. Diese können entweder zufällig plaziert "
                        + "werden über den Button \"Zufällig setzen\" oder manuell per Click & Drop. Eine Kombination beider ist auch möglich. "
                        + "Hierbei muss aber beachtet werden, dass die "
                        + "Schiffe mindestens ein Feld Abstand zueinander haben und auch nicht diagonal gesetzt werden können.");
                break;
            case 3://Vierte Anleitungskachel - Schießen (Spielen)
                setEinheiten(true, false, false, true, false);
                ladeVierFuenf(); //aufteilen des Ladeaufwand für bessere Performance
                ueberschriftLinks.setText("Schießen");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(spielen);
                labelRechts.setText("Die Spieler schiessen abwechselnd auf das gegenüberliegende Feld. Wenn ein Schiffsteil getroffen wurde, bekommt der Spieler einen "
                        + "weiteren Zug und darf nochmals schiessen. Das Spiel endet in einem Sieg, wenn alle Schiffe des Gegners versenkt wurden. "
                        + "Es ist möglich den Spielstand von einem Spiel zu speichern, indem "
                        + "der Button \"Speichern\" betätigt wird. Wenn das Spiel endet kann wieder ein neues Spiel gestartet oder geladen werden.");
                break;
            case 4://Fuenfte Anleitungskachel - Speichern
                setEinheiten(true, false, false, true, false);
                ueberschriftLinks.setText("Speichern");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(speichern);
                labelRechts.setText("Um ein Spiel zu speichern, kann der Button \"Speichern\" gedrückt werden. Es öffnet sich dann ein Explorer-Fenster\n"
                        + "in welchem ein beliebiger Ordner gewählt werden kann, um einen Spielstand zu sichern. Diese Datei wird beim Laden eines\n"
                        + "Spielstandes wieder benötigt.");
                break;
            case 5://Sechste Anleitungskachel - Laden
                setEinheiten(true, false, false, true, false);
                ueberschriftLinks.setText("Laden");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(laden);
                labelRechts.setText("Um einen Spielstand zu laden, wird der Button \"Laden\" im Modi-Menü gedrückt. Dort öffnet sich dann, wie bereits beim \n"
                        + "Speichern, ein Explorer-Fenster in welchem die Spieldatei ausgewählt werden kann. Nachdem der Spielstand geladen wurde,\n"
                        + "kann wie gewohnt weitergespielt werden.");
                break;
            case 6://Siebte Anleitungskachel - erneut Verbinden
                setEinheiten(true, false, false, true, false);
                ueberschriftLinks.setText("Erneut Verbinden");
                ueberschriftRechts.setText("");
                linkesGif.setFitHeight(540);
                linkesGif.setFitWidth(900);
                linkesGif.setImage(reconnect);
                labelRechts.setText("Tritt man einem Online Spiel bei, bevor der Host die Verbindung aufgebaut hat kann man über den Button \" Erneut Verbinden \""
                        + "nochmals versuchen eine Verbindung aufzubauen. Hat der Host zu diesem Zeitpunkt eine Verbindung gestartet kann dieser Beigetreten werden und "
                        + "alles lädt wie gewöhnlich.");
                break;
        }
    }

    /**
     * Setzt die Anzeigenenden fx Objekter der Scene visible oder nicht, je nach
     * dem ob zwei Images und zwei text geladen werden, oder nicht
     *
     * @param gifL - setzt Image View für Linkes Gif
     * @param gifR - setzt Image View für Linkes Gif
     * @param lL - setzt label für Linkes Gif
     * @param lR - setzt label für Linkes Gif
     * @param ersterRech - setzt Sondertext für erste Anleitungsseite
     */
    public void setEinheiten(boolean gifL, boolean gifR, boolean lL, boolean lR, boolean ersterRech) {
        linkesGif.setVisible(gifL);
        rechtesGif.setVisible(gifR);
        labelLinks.setVisible(lL);
        labelRechts.setVisible(lR);
        labelEins.setVisible(ersterRech);
    }

    /**
     * Lädt GIFs laden und speichern im Hintergrund, wenn 3.Kachel erreicht
     */
    private void ladeVierFuenf() {
        if (geladenMitte == false) {
            speichern = new Image(this.getClass().getResourceAsStream("/Images/Speichern.gif"));
            laden = new Image(this.getClass().getResourceAsStream("/Images/Laden.gif"));
            geladenMitte = true;
        }
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
            spielen = new Image(this.getClass().getResourceAsStream("/Images/gifVier.gif"));
            setzen = new Image(this.getClass().getResourceAsStream("/Images/drittesGIF.gif"));
            groeßenWahl = new Image(this.getClass().getResourceAsStream("/Images/einsRechts.gif"));
            schiffWahl = new Image(this.getClass().getResourceAsStream("/Images/zweiLinks.gif"));
            reconnect = new Image(this.getClass().getResourceAsStream("/Images/reconnect.gif"));
            geladen = true;
        }
    }
}
