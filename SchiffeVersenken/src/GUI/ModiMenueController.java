package GUI;

import SaveLoad.SaveLoad;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @FXML
    private ComboBox<String> pxDropdown;

    private TextField[] labels = new TextField[4];
    private int spielfeldgroesse = 0;
    private int[] anzahlSchiffeTyp = new int[4]; // Stelle 0->Zweier, 1->Dreier, 2->Vierer, 3->Fuenfer
    private int modus; // 0-> Default, 1-> Lokales Spiel, 2-> KI-Spiel, 3-> OnlineSpiel, 21 -> KISpiel als Host, 22 -> KISpiel als Client, 31 -> OnlineSpiel als Host, 32 -> OnlineSpiel als Client
    private String ipAdresse;
    private String eigeneIp;
    private int kiStufe = 0;

    private SaveLoad saveload = new SaveLoad();

    private FileChooser fc = new FileChooser();

    private int benoetigteAnzahlSchiffsteile;
    private int istAnzahlSchiffsteile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fc.setInitialDirectory(new File("src/saves/"));
        sliderKI.setVisible(false);
        titelKiStaerke.setVisible(false);
        setKISpielNichtAktiv();
        setOnlineSpielNichtAktiv();
        statusMeldung();
        initDropDownMenue();
        initPxMenue();
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

        eigeneIp = null;

        try {
            eigeneIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ModiMenueController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Erstellen des Dropdown-Menü zur Wahl der Spielfeldgröße Zahlen zwischen 5
     * und 30 möglich
     */
    private void initDropDownMenue() {
        for (int i = 5; i < 31; i++) {
            dropdown.getItems().add(i);
        }
    }

    /**
     * Erstellen des Dropdown-Menü zur Auswahl der Fenstergröße Drei
     * verschiedene Größen:
     */
    private void initPxMenue() {
        pxDropdown.getItems().add("1000x700");
        pxDropdown.getItems().add("1350x800 (empfohlen)");
        pxDropdown.getItems().add("1500x800");
    }

    /**
     * regelt die Sichtbarkeit von Checkboxen und Label des Online-Spiel
     * Bereichs
     */
    private void setOnlineSpielNichtAktiv() {
        checkboxHost.setVisible(false);
        checkboxClient.setVisible(false);
        labelEigeneIP.setVisible(false);
        eingabeIP.setVisible(false);
        checkboxHost.setSelected(false);
        checkboxClient.setSelected(false);
    }

    /**
     * regelt die Sichtbarkeit von Checkboxen und Label des KI-Spiel Bereichs
     */
    private void setKISpielNichtAktiv() {
        checkboxHostKI.setVisible(false);
        checkboxClientKI.setVisible(false);
        labelEigeneIPKI.setVisible(false);
        eingabeIPKI.setVisible(false);
        checkboxHostKI.setSelected(false);
        checkboxClientKI.setSelected(false);
    }

    /**
     * Wird aufgerufen, wenn eine der Checkboxen aktiviert wird, so kann die
     * rechte Hälfte ausgeblendet werden, wenn nichts ausgewählt werden soll als
     * Client. Wählt man z.B Host, dann wird alles wieder sichtbar
     *
     * @param anzeigen
     */
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

    /**
     * Setzt die Statusmeldungen für die verschiedenen Modi, sowie die
     * Schiffsanzahl
     */
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

    /**
     * Wenn die Checkbox Lokales Spiel markiert wurde, verstecke alles links und
     * speichere den Modus in der späteren, zu übergebenen, Variable
     *
     * @param event JavaFx event
     */
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

    /**
     * Verwaltet cklick auf Online Spiel Checkbox
     *
     * @param event JavaFx Event
     */
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

    /**
     * Verwaltet cklick auf KI Spiel Checkbox
     *
     * @param event JavaFx Event
     */
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

    /**
     * Verwaltet cklick auf Online Spiel Host Checkbox
     *
     * @param event JavaFx Event
     */
    @FXML
    private void handleHost(ActionEvent event) {
        if (checkboxHost.isSelected()) {
            labelEigeneIP.setText(eigeneIp);
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

    /**
     * Verwaltet cklick auf Online Spiel Client Checkbox
     *
     * @param event JavaFx Event
     */
    @FXML
    private void handleClient(ActionEvent event) {
        if (checkboxClient.isSelected()) {
            labelEigeneIP.setText(eigeneIp);
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

    /**
     * Verwaltet cklick auf KI Spiel Host Checkbox
     *
     * @param event JavaFx Event
     */
    @FXML
    private void handleHostKI(ActionEvent event) {
        if (checkboxHostKI.isSelected()) {
            labelEigeneIPKI.setText(eigeneIp);
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

    /**
     * Verwaltet cklick auf KI Spiel Client Checkbox
     *
     * @param event JavaFx Event
     */
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

    /**
     * Berechnet aus den in den Label gesetzten werten die Gesamtzahl der
     * Schiffsteile
     */
    private void berechneSchiffTeile() {
        istAnzahlSchiffsteile = anzahlSchiffeTyp[0] * 2 + anzahlSchiffeTyp[1] * 3 + anzahlSchiffeTyp[2] * 4 + anzahlSchiffeTyp[3] * 5;
        labelIstAnzahl.setText(istAnzahlSchiffsteile + " Schiffsteile ausgewählt");
        statusMeldung();
    }

    //Aktualisiert das Array, welches die Anzahl der verschieden Schiffe speichert
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

    //*************************************************************************
    /**
     * Speichert die eingegebene IP-Adresse im Online Client Label
     *
     * @param event JavaFx event
     */
    @FXML
    private void setzeIpAdresseOnline(ActionEvent event) {
        ipAdresse = eingabeIP.getText();
    }

    /**
     * Speichert die eingegebene IP-Adresse im KI Client Label
     *
     * @param event JavaFx event
     */
    @FXML
    private void setzeIpAdresseKI(ActionEvent event) {
        ipAdresse = eingabeIPKI.getText();
    }

    /**
     * Aktualisiert die Variable nach Wahl in der Combobox, setzt die neue
     * Stausmeldung und brechnet die neue benötigte Anzahl an Schiffsteilen
     *
     * @param event JavaFx
     */
    @FXML
    private void aktualisiereSpielfeldgroeße(ActionEvent event) {
        spielfeldgroesse = dropdown.getValue();
        benoetigteAnzahlSchiffsteile = (int) ((int) spielfeldgroesse * spielfeldgroesse * 0.3); // 30% abgerundet bei Typ Schiffe 
        labelSollAnzahl.setText("Du benötigst " + benoetigteAnzahlSchiffsteile + " Schiffsteile!");
        setzeSchiffe();
        statusMeldung();
    }

    /**
     * Funktion zum automatischen errechnen der Schiffsteile und eintragen in
     * die Label
     */
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

    /**
     * Wird ausgelöst bei drücken des Startbutton. Es wird unterschieden,
     * welcher Modus gewählt wurde und beding unterschieden, welche Steuerung
     * mit welchen Parametern gewählt wird sowie welche Fenstergröße gestartet
     * werden soll
     *
     * @param event JavaFx event
     * @throws IOException Exception bei falschem Pfad
     */
    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        boolean go = true;
        if ((modus == 1 || modus == 21 || modus == 31) && spielfeldgroesse != 0 && benoetigteAnzahlSchiffsteile == istAnzahlSchiffsteile) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(setGroesse()));
            Parent root = loader.load();
            SpielGUIController spielGUIController = loader.getController();
            spielGUIController.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp, modus, "", kiStufe);

            Scene scene = new Scene(root);

            SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
            SchiffeVersenken.getApplicationInstance().getStage().show();
        } else if ((modus == 22 || modus == 32) && ipAdresse != null) { // IP Adresse Aufbau: zahl.zahl.zahl.zahl, 0 <= zahl <= 255
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(setGroesse()));
                Parent root = loader.load();
                SpielGUIController spielGUIController = loader.getController();
                spielGUIController.uebergebeInformationen(0, null, modus, ipAdresse, kiStufe);

                Scene scene = new Scene(root);

                SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
                SchiffeVersenken.getApplicationInstance().getStage().show();
            }
            if (ipAdresse.equals("localhost")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(setGroesse()));
                Parent root = loader.load();
                SpielGUIController spielGUIController = loader.getController();
                spielGUIController.uebergebeInformationen(0, null, modus, "127.0.0.1", kiStufe);

                Scene scene = new Scene(root);

                SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
                SchiffeVersenken.getApplicationInstance().getStage().show();
            } else {
                labelStatus.setText("Ungültige IP-Adresse");
            }
        }
    }

    /**
     * Verwaltet das Drücken des Button von "Zurück" und lädt die
     * Hauptmenü-Scene
     *
     * @param event JavaFx event
     * @throws IOException
     */
    @FXML
    private void handleButtonZurueck(ActionEvent event) throws IOException {
        SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    /**
     * Verwaltet den Button für "Spielstand laden", initiiert das Lesen aus der
     * Datei
     *
     * @param event JavaFx Event
     * @throws IOException wenn falscher Pfad angegeben
     */
    @FXML
    private void ladeSpiel(ActionEvent event) throws IOException {
        if (saveload.starteLaden()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(setGroesse()));
            Parent root = loader.load();
            SpielGUIController spielGUIController = loader.getController();

            if (saveload.getParamInc()[1] == 1) {
                
                spielGUIController.uebergebeInformationenLokal(saveload.getStyp(), saveload.getParamInc(), saveload.getGridRechtsArr(), saveload.getGridLinksArr(), saveload.getGetroffenGeg(), saveload.getGetroffenAr(), saveload.getGetroffenKi(), saveload.getLetzterSchussKi(), saveload.getAngefSchiffKi(), saveload.getKiValues());
            } else if (saveload.getParamInc()[1] == 31 || saveload.getParamInc()[1] == 32) { //Host von Online Spiel
                
                spielGUIController.uebergebeInformationenOnline(saveload.getIp(), saveload.getL(), saveload.getParamInc(), saveload.getStyp(), saveload.getGetroffenAr(), saveload.getGetroffenGeg(), saveload.getGridRechtsArr(), saveload.getGridLinksArr(), saveload.getOnlineValues());
            }
            Scene scene = new Scene(root);

            SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);
            SchiffeVersenken.getApplicationInstance().getStage().show();
        }
    }

    /**
     * Registriert die Sliderbewegung und setzt die KI Stufe auf den geänderten
     * Wert
     *
     * @param event JavaFx event
     */
    @FXML
    private void handleSliderKI(MouseEvent event) {
        kiStufe = (int) sliderKI.getValue();
    }

    /**
     * Handelt die Klicks auf den Increment- und Decrement Button
     *
     * @param modus 1 Anzahl verringern, 2 Anzahl vergrößern
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

    /**
     * Setzt die globalen Variablen für die pxGröße und Höhe, sowie die
     * Verschiebung und wählt je nach Comboboxauswahl die GUI, welche gewählt
     * werden soll
     *
     * @return - Pfad zur Datei, welche geladen werden soll
     */
    public String setGroesse() {
        int kachelAnzahl = 0;
        if (dropdown.getValue() == null) { //Kann 0 sein, wenn man Spiel beitritt
            kachelAnzahl = 10;
        } else {
            kachelAnzahl = dropdown.getValue();
        }
        String normal = "/GUI/SpielGUI.fxml";
        String groß = "/GUI/großeSpielGUI.fxml";
        String klein = "/GUI/kleineSpielGUI.fxml";
        if (pxDropdown.getValue() != null) { //wenn eine Fenstergröße ausgewählt wurde
            if (pxDropdown.getValue().equals("1000x700")) {
                int verschieb = 500 / kachelAnzahl;
                var.var.pxGroesse = verschieb * kachelAnzahl;
                var.var.hoehe = verschieb * kachelAnzahl;
                verschieb = 500 - verschieb * kachelAnzahl;
                var.var.verschiebung = 2 * verschieb + 10;
                return klein;
            } else if (pxDropdown.getValue().equals("1500x800")) {
                int verschieb = 750 / kachelAnzahl;
                var.var.pxGroesse = verschieb * kachelAnzahl;
                var.var.hoehe = verschieb * kachelAnzahl;
                verschieb = 750 - verschieb * kachelAnzahl;
                var.var.verschiebung = 2 * verschieb + 8;
                return groß;
            }
        } //Standdart wert, wenn nichts gewählt wird oder 1350x800 ausgewählt
        int verschieb = 600 / kachelAnzahl;
        var.var.pxGroesse = verschieb * kachelAnzahl;
        var.var.hoehe = verschieb * kachelAnzahl;
        var.var.verschiebung = 50;
        return normal;
    }
}
