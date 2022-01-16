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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import schiffeversenken.SchiffeVersenken;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class ModiMenueController implements Initializable {

    @FXML
    private Pane schiffeWaehlen;
    @FXML
    private Pane spielbrettWaehlen;
    @FXML
    private Pane modiWaehlen;

    @FXML
    private CheckBox checkboxKISpiel;
    @FXML
    private CheckBox checkboxHostKI;
    @FXML
    private CheckBox checkboxClientKI;
    @FXML
    private TextField eingabeIPKI;
    @FXML
    private Label labelEigeneIPKI;
    @FXML
    private CheckBox checkboxLokalesSpiel;
    @FXML
    private CheckBox checkboxOnlineSpiel;
    @FXML
    private CheckBox checkboxHost;
    @FXML
    private CheckBox checkboxClient;
    @FXML
    private TextField eingabeIP;
    @FXML
    private Label labelEigeneIP;
    @FXML
    private Label labelIstAnzahl;
    @FXML
    private Label labelSollAnzahl;
    @FXML
    private TextField eingabeZweier;
    @FXML
    private TextField eingabeDreier;
    @FXML
    private TextField eingabeVierer;
    @FXML
    private TextField eingabeFuenfer;
    @FXML
    private Label titelSpielfeldgroesse;
    @FXML
    private Label titelSchiffeWaehlen;
    @FXML
    private Label labelZweier;
    @FXML
    private Label labelDreier;
    @FXML
    private Label labelVierer;
    @FXML
    private Label labelFuenfer;
    @FXML
    private ComboBox<Integer> dropdown;
    @FXML
    private Label labelStatus;

    private int spielfeldgroesse = 0;
    private int[] anzahlSchiffeTyp = new int[4]; // Stelle 0->Zweier, 1->Dreier, 2->Vierer, 3->Fuenfer
    private int modus; // 0-> Default, 1-> Lokales Spiel, 2-> KI-Spiel, 3-> OnlineSpiel, 21 -> KISpiel als Host, 22 -> KISpiel als Client, 31 -> OnlineSpiel als Host, 32 -> OnlineSpiel als Client
    private String ipAdresse;
    private int kiStufe = 0;

    private FileChooser fc = new FileChooser();

    private int benoetigteAnzahlSchiffsteile;
    private int istAnzahlSchiffsteile;
    @FXML
    private Button loadButton;
    @FXML
    private Slider sliderKI;
    @FXML
    private Label titelKiStaerke;
    @FXML
    private Button decZwei;
    @FXML
    private Button decDrei;
    @FXML
    private Button decVier;
    @FXML
    private Button decFuenf;
    @FXML
    private Button incZwei;
    @FXML
    private Button incDrei;
    @FXML
    private Button incVier;
    @FXML
    private Button incFuenf;

    private TextField[] labels = new TextField[4];

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Modimenü");
        fc.setInitialDirectory(new File("src/saves/"));
        sliderKI.setVisible(false);
        titelKiStaerke.setVisible(false);
        setKISpielNichtAktiv();
        setOnlineSpielNichtAktiv();
        statusMeldung();
        initDropDownMenue();
        kiStufe = 1;
        decDrei.setOnAction(e -> aendereZahl(1, 1));
        decZwei.setOnAction(e -> aendereZahl(1, 0));
        decVier.setOnAction(e -> aendereZahl(1, 2));
        decFuenf.setOnAction(e -> aendereZahl(1, 3));

        incDrei.setOnAction(e -> aendereZahl(2, 1));
        incZwei.setOnAction(e -> aendereZahl(2, 0));
        incVier.setOnAction(e -> aendereZahl(2, 2));
        incFuenf.setOnAction(e -> aendereZahl(2, 3));
        labels[0] = eingabeZweier;
        labels[1] = eingabeDreier;
        labels[2] = eingabeVierer;
        labels[3] = eingabeFuenfer;
    }

    private void initDropDownMenue() {
        for (int i = 5; i < 31; i++) {
            dropdown.getItems().add(i);
        }
    }

    private void setOnlineSpielNichtAktiv() {
        checkboxHost.setVisible(false);
        checkboxClient.setVisible(false);
        labelEigeneIP.setVisible(false);
        eingabeIP.setVisible(false);
        checkboxHost.setSelected(false);
        checkboxClient.setSelected(false);
    }

    private void setKISpielNichtAktiv() {
        checkboxHostKI.setVisible(false);
        checkboxClientKI.setVisible(false);
        labelEigeneIPKI.setVisible(false);
        eingabeIPKI.setVisible(false);
        checkboxHostKI.setSelected(false);
        checkboxClientKI.setSelected(false);
    }

    private void setEingabe(boolean anzeigen) {
        if (anzeigen) {
            spielbrettWaehlen.setBackground(null);
            schiffeWaehlen.setBackground(null);
            titelSchiffeWaehlen.setStyle(null);
            titelSpielfeldgroesse.setStyle(null);
            eingabeZweier.setVisible(true);
            eingabeDreier.setVisible(true);
            eingabeVierer.setVisible(true);
            eingabeFuenfer.setVisible(true);
            dropdown.setVisible(true);
            labelIstAnzahl.setVisible(true);
            labelSollAnzahl.setVisible(true);
            labelZweier.setVisible(true);
            labelDreier.setVisible(true);
            labelVierer.setVisible(true);
            labelFuenfer.setVisible(true);
            decDrei.setVisible(true);
            decVier.setVisible(true);
            decFuenf.setVisible(true);
            decZwei.setVisible(true);
            incDrei.setVisible(true);
            incVier.setVisible(true);
            incFuenf.setVisible(true);
            incZwei.setVisible(true);
        } else {
            spielbrettWaehlen.setBackground(new Background(new BackgroundFill(Color.rgb(79, 89, 107, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
            schiffeWaehlen.setBackground(new Background(new BackgroundFill(Color.rgb(79, 89, 107, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
            titelSchiffeWaehlen.setStyle("-fx-text-fill: grey");
            titelSpielfeldgroesse.setStyle("-fx-text-fill: grey");
            eingabeZweier.setVisible(false);
            eingabeDreier.setVisible(false);
            eingabeVierer.setVisible(false);
            eingabeFuenfer.setVisible(false);
            dropdown.setVisible(false);
            labelIstAnzahl.setVisible(false);
            labelSollAnzahl.setVisible(false);
            labelZweier.setVisible(false);
            labelDreier.setVisible(false);
            labelVierer.setVisible(false);
            labelFuenfer.setVisible(false);
            decDrei.setVisible(false);
            decVier.setVisible(false);
            decFuenf.setVisible(false);
            decZwei.setVisible(false);
            incDrei.setVisible(false);
            incVier.setVisible(false);
            incFuenf.setVisible(false);
            incZwei.setVisible(false);
        }
    }

    private void statusMeldung() {
        if (modus == 1 || modus == 21 || modus == 31) {
            if (spielfeldgroesse != 0) {
                if (istAnzahlSchiffsteile == benoetigteAnzahlSchiffsteile) {
                    labelStatus.setText("Anzahl erreicht!");
                } else if (istAnzahlSchiffsteile > benoetigteAnzahlSchiffsteile) {
                    labelStatus.setText("Zu viele Schiffsteile!");
                } else {
                    labelStatus.setText("Zu wenig Schiffsteile!");
                }
            } else {
                labelStatus.setText("Spielfeldgröße auswählen");
            }
        } else if (modus == 22 || modus == 32) {
            labelStatus.setText("Hier muss nichts gewählt werden");
        } else {
            labelStatus.setText("Modus wählen!");
        }

    }

    @FXML
    private void handleLokalesSpiel(ActionEvent event) {
        if (checkboxLokalesSpiel.isSelected()) {
            checkboxOnlineSpiel.setSelected(false);
            setOnlineSpielNichtAktiv();
            checkboxKISpiel.setSelected(false);
            setKISpielNichtAktiv();
            modus = 1;
            sliderKI.setVisible(true);
            titelKiStaerke.setVisible(true);

            setEingabe(true);
        } else {
            modus = 0;
            sliderKI.setVisible(false);
            titelKiStaerke.setVisible(false);
        }
        statusMeldung();
    }

    @FXML
    private void handleOnlineSpiel(ActionEvent event) {
        if (checkboxOnlineSpiel.isSelected()) {
            checkboxHost.setVisible(true);
            checkboxClient.setVisible(true);
            checkboxLokalesSpiel.setSelected(false);
            checkboxKISpiel.setSelected(false);
            setKISpielNichtAktiv();
            modus = 3;
            setEingabe(true);
        } else {
            setOnlineSpielNichtAktiv();
            setEingabe(true);
            modus = 0;
        }
        statusMeldung();
        sliderKI.setVisible(false);
        titelKiStaerke.setVisible(false);
    }

    @FXML
    private void handleKISpiel(ActionEvent event) {
        if (checkboxKISpiel.isSelected()) {
            checkboxHostKI.setVisible(true);
            checkboxClientKI.setVisible(true);
            checkboxLokalesSpiel.setSelected(false);
            checkboxOnlineSpiel.setSelected(false);
            setOnlineSpielNichtAktiv();
            modus = 2;
            sliderKI.setVisible(true);
            titelKiStaerke.setVisible(true);
            setEingabe(true);
        } else {
            setKISpielNichtAktiv();
            setEingabe(true);
            modus = 0;
            sliderKI.setVisible(false);
            titelKiStaerke.setVisible(false);
        }
        statusMeldung();
    }

    @FXML
    private void handleHost(ActionEvent event) {
        if (checkboxHost.isSelected()) {
            labelEigeneIP.setVisible(true);
            checkboxClient.setSelected(false);
            eingabeIP.setVisible(false);
            modus = 31;
            setEingabe(true);
        } else {
            labelEigeneIP.setVisible(false);
            modus = 0;
        }
        statusMeldung();
    }

    @FXML
    private void handleClient(ActionEvent event) {
        if (checkboxClient.isSelected()) {
            eingabeIP.setVisible(true);
            checkboxHost.setSelected(false);
            labelEigeneIP.setVisible(false);
            modus = 32;
            setEingabe(false);
        } else {
            eingabeIP.setVisible(false);
            setEingabe(true);
            modus = 0;
        }
        statusMeldung();
    }

    @FXML
    private void handleHostKI(ActionEvent event) {
        if (checkboxHostKI.isSelected()) {
            labelEigeneIPKI.setVisible(true);
            checkboxClientKI.setSelected(false);
            eingabeIPKI.setVisible(false);
            modus = 21;
            setEingabe(true);
        } else {
            labelEigeneIPKI.setVisible(false);
            modus = 0;
        }
        statusMeldung();
    }

    @FXML
    private void handleClientKI(ActionEvent event) {
        if (checkboxClientKI.isSelected()) {
            eingabeIPKI.setVisible(true);
            checkboxHostKI.setSelected(false);
            labelEigeneIPKI.setVisible(false);
            modus = 22;
            setEingabe(false);
        } else {
            eingabeIPKI.setVisible(false);
            setEingabe(true);
            modus = 0;
        }
        statusMeldung();
    }

    private void berechneSchiffTeile() {
        istAnzahlSchiffsteile = anzahlSchiffeTyp[0] * 2 + anzahlSchiffeTyp[1] * 3 + anzahlSchiffeTyp[2] * 4 + anzahlSchiffeTyp[3] * 5;
        labelIstAnzahl.setText(istAnzahlSchiffsteile + " Schiffsteile ausgewählt");
        statusMeldung();
    }

    @FXML
    private void aktualisiereAnzahlZweier(ActionEvent event) {
        try {
            anzahlSchiffeTyp[0] = Integer.parseInt(eingabeZweier.getText());
        } catch (Exception e) {
            eingabeZweier.setText("");
        }
        berechneSchiffTeile();

    }

    @FXML
    private void aktualisiereAnzahlDreier(ActionEvent event) {
        try {
            anzahlSchiffeTyp[1] = Integer.parseInt(eingabeDreier.getText());
        } catch (Exception e) {
            eingabeDreier.setText("");
        }
        berechneSchiffTeile();
    }

    @FXML
    private void aktualisiereAnzahlVierer(ActionEvent event) {
        try {
            anzahlSchiffeTyp[2] = Integer.parseInt(eingabeVierer.getText());
        } catch (Exception e) {
            eingabeVierer.setText("");
        }
        berechneSchiffTeile();
    }

    @FXML
    private void aktualisiereAnzahlFuenfer(ActionEvent event) {
        try {
            anzahlSchiffeTyp[3] = Integer.parseInt(eingabeFuenfer.getText());
        } catch (Exception e) {
            eingabeFuenfer.setText("");
        }
        berechneSchiffTeile();
    }

    @FXML
    private void setzeIpAdresseOnline(ActionEvent event) {
        ipAdresse = eingabeIP.getText();
    }

    @FXML
    private void setzeIpAdresseKI(ActionEvent event) {
        ipAdresse = eingabeIPKI.getText();
    }

    @FXML
    private void aktualisiereSpielfeldgroeße(ActionEvent event) {
        spielfeldgroesse = dropdown.getValue();
        benoetigteAnzahlSchiffsteile = (int) ((int) spielfeldgroesse * spielfeldgroesse * 0.3); // 30% abgerundet bei Typ Schiffe 
        labelSollAnzahl.setText("Du benötigst " + benoetigteAnzahlSchiffsteile + " Schiffsteile!");
        setzeSchiffe();
        statusMeldung();
    }

    public void setzeSchiffe() {
        int anzahlSchiff = benoetigteAnzahlSchiffsteile / 14;
        int rest = benoetigteAnzahlSchiffsteile % 14;

        //System.out.println("Jedes Schiff: " + anzahlSchiff + " mal");
        eingabeFuenfer.setText(Integer.toString(anzahlSchiff));
        eingabeDreier.setText(Integer.toString(anzahlSchiff));
        eingabeZweier.setText(Integer.toString(anzahlSchiff));
        eingabeVierer.setText(Integer.toString(anzahlSchiff));
        anzahlSchiffeTyp[3] = anzahlSchiff;
        anzahlSchiffeTyp[2] = anzahlSchiff;
        anzahlSchiffeTyp[1] = anzahlSchiff;
        anzahlSchiffeTyp[0] = anzahlSchiff;

        if (rest - 5 >= 0) {
            rest = rest - 5;
            anzahlSchiffeTyp[3] += 1;
            eingabeFuenfer.setText(Integer.toString(anzahlSchiff + 1));
        }
        if (rest - 4 >= 0) {
            rest = rest - 4;
            anzahlSchiffeTyp[2] += 1;
            eingabeVierer.setText(Integer.toString(anzahlSchiff + 1));
        }
        if (rest - 3 >= 0) {
            rest = rest - 3;
            anzahlSchiffeTyp[1] += 1;
            eingabeDreier.setText(Integer.toString(anzahlSchiff + 1));
        }
        if (rest - 2 >= 0) {
            rest = rest - 2;
            anzahlSchiffeTyp[0] += 1;
            eingabeZweier.setText(Integer.toString(anzahlSchiff + 1));
        }
        if (rest == 1) {
            rest = rest - 1;
            if (anzahlSchiff == 0) {
                anzahlSchiffeTyp[0] += 1;
                anzahlSchiffeTyp[1] += 1;
                anzahlSchiffeTyp[2] -= 1;
                eingabeZweier.setText(Integer.toString(Integer.parseInt(eingabeZweier.getText()) + 1));
                eingabeDreier.setText(Integer.toString(Integer.parseInt(eingabeDreier.getText()) + 1));
                eingabeVierer.setText(Integer.toString(Integer.parseInt(eingabeVierer.getText()) - 1));
            } else {
                anzahlSchiffeTyp[1] += 1;
                anzahlSchiffeTyp[0] -= 1;
                eingabeDreier.setText(Integer.toString(Integer.parseInt(eingabeDreier.getText()) + 1));
                eingabeZweier.setText(Integer.toString(Integer.parseInt(eingabeZweier.getText()) - 1));
            }
        }
        istAnzahlSchiffsteile = anzahlSchiffeTyp[0] * 2 + anzahlSchiffeTyp[1] * 3 + anzahlSchiffeTyp[2] * 4 + anzahlSchiffeTyp[3] * 5;
        labelIstAnzahl.setText(istAnzahlSchiffsteile + " Schiffsteile ausgewählt");
    }

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        boolean go = true;
        if ((modus == 1 || modus == 21 || modus == 31) && spielfeldgroesse != 0 && benoetigteAnzahlSchiffsteile == istAnzahlSchiffsteile) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
            Parent root = loader.load();
            SpielGUIController spielGUIController = loader.getController();
            spielGUIController.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp, modus, "", kiStufe);

            Scene scene = new Scene(root);

            SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
            SchiffeVersenken.getApplicationInstance().getStage().show();
        } else if ((modus == 1 || modus == 21 || modus == 31)) { // DEBUG für MARVIN
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
            Parent root = loader.load();
            SpielGUIController spielGUIController = loader.getController();
            spielGUIController.uebergebeInformationen(10, new int[]{2, 0, 0, 0}, modus, "", kiStufe);

            Scene scene = new Scene(root);

            SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
            SchiffeVersenken.getApplicationInstance().getStage().show();
        } else if ((modus == 22 || modus == 32) && ipAdresse != null) { // IP Adresse Aufbau: zahl.zahl.zahl.zahl, 0 <= zahl <= 255  
            System.out.println("Ki Stärke: " + kiStufe);
            int anzahlIp = 0;
            String[] split = ipAdresse.split("\\.");

            for (int i = 0; i < split.length; i++) { //checken ob an Stelle split[0-3] die Zahlen zwischen 0 <= zahl <= 255 liegen
                try {
                    if (Integer.parseInt(split[i]) >= 0 && Integer.parseInt(split[i]) <= 255) {
                        anzahlIp++;
                    } else {
                        go = false;
                    }
                } catch (Exception e) {
                    go = false;
                }
            }
            if (anzahlIp == 4 && go) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
                Parent root = loader.load();
                SpielGUIController spielGUIController = loader.getController();
                spielGUIController.uebergebeInformationen(0, null, modus, ipAdresse, kiStufe);

                Scene scene = new Scene(root);

                SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
                SchiffeVersenken.getApplicationInstance().getStage().show();
            }
            if (ipAdresse.equals("localhost")) { // DEBUG für DIDI
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
                Parent root = loader.load();
                SpielGUIController spielGUIController = loader.getController();
                spielGUIController.uebergebeInformationen(0, null, modus, ipAdresse, kiStufe);

                Scene scene = new Scene(root);

                SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
                SchiffeVersenken.getApplicationInstance().getStage().show();
            } else {
                labelStatus.setText("Ungültige IP-Adresse");
            }
        }
    }

    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    @FXML
    private void ladeSpiel(ActionEvent event) {
        fc.setTitle("Laden");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("text file", "*.txt"));

        try {
            File load = fc.showOpenDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());

            if (load != null) {
                SaveLoad.SaveLoad.readFromFile(load);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSliderKI(MouseEvent event) {
        kiStufe = (int) sliderKI.getValue();
    }

    /**
     * Handelt die Klicks auf den Increment und Decrement Button 
     * @param modus 1 Anzahl verringern 2 Anzahl vergrößern
     * @param schiffTyp Jeweilige Typ - zweier, dreier, vierer, fuenfer
     */
    private void aendereZahl(int modus, int schiffTyp) {
        switch (modus) {
            case 1:
                if (anzahlSchiffeTyp[schiffTyp] - 1 >= 0) {
                    anzahlSchiffeTyp[schiffTyp] = anzahlSchiffeTyp[schiffTyp] - 1;
                    labels[schiffTyp].setText(Integer.toString(anzahlSchiffeTyp[schiffTyp]));
                    berechneSchiffTeile();
                }
                break;
            case 2:
                anzahlSchiffeTyp[schiffTyp] = anzahlSchiffeTyp[schiffTyp] + 1;
                labels[schiffTyp].setText(Integer.toString(anzahlSchiffeTyp[schiffTyp]));
                berechneSchiffTeile();
                break;
        }
    }

}
