package GUI;

import Musik.MusikPlayer;
import var.var;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import schiffeversenken.SchiffeVersenken;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class HauptmenueController implements Initializable {

    @FXML
    private Pane einstellungen;
    @FXML
    private Slider slider;
    @FXML
    private Button settingsButton;

    private boolean offen = false;

    Musik.MusikPlayer mp = new MusikPlayer(); //Erzeugt ein Musikplayer
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        einstellungen.setVisible(false); //Versteckt initial die Kachel der Einstellungen
        slider.setValue(var.lautstaerke); //Setzt die Musik initial auf 0
        slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::changeMusikHandler);
        if (!var.musik_laueft) {
            var.musik_laueft = true;
            mp.start(); //Startet die Musik
        }
    }

    /**
     * Wechselt die Scene in das Modi Menü bei druck auf den Button
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/ModiMenue.fxml");
    }

    /**
     * Wechselt die Scene zur Anleitung auf druck des Button
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleButtonAnleitung(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Anl.fxml");
    }

    /**
     * Beendet die Applikation mit Status 0
     * @param event 
     */
    @FXML
    private void handleButtonBeenden(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Oeffnet und schließt die Einstellungskachel 
     * @param event 
     */
    @FXML
    private void handleButtonSettings(ActionEvent event) {
        if (offen) {
            einstellungen.setVisible(false);
            offen = false;
        } else {
            offen = true;
            einstellungen.setVisible(true);
        }

    }

    /**
     * Ändert die Musiklautstärke bei betätigen des Sliders
     * @param e - entsprechende Event
     */
    private void changeMusikHandler(MouseEvent e) {
        var.lautstaerke = slider.getValue() / 100;
        mp.setLautstaerke(slider.getValue() / 100);
    }

    /**
     * Öffnet die Credits bei Drücken auf den Button
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleButtonCredits(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Credits.fxml");
    }


}
