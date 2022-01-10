/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import var.var;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    @FXML
    private Button CreditsButton;

    private boolean offen = false;

    Musik.MusikPlayer mp;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Hauptmenü");
        einstellungen.setVisible(false);
        slider.setValue(0);
        slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::changeMusikHandler);
        mp.start();
    }

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/ModiMenue.fxml");
    }

    @FXML
    private void handleButtonAnleitung(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Anleitung.fxml");
    }

    @FXML
    private void handleButtonBeenden(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleButtonSettings(ActionEvent event) {
        System.out.println(offen);
        if (offen) {
            einstellungen.setVisible(false);
            offen = false;
        } else {
            offen = true;
            einstellungen.setVisible(true);
        }

    }

    private void changeMusikHandler(MouseEvent e) {
        var.lautstaerke = slider.getValue() / 100;
        mp.setLautstaerke(slider.getValue() / 100);
    }

    @FXML
    private void oeffneCredits(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Credits.fxml");
    }

}
