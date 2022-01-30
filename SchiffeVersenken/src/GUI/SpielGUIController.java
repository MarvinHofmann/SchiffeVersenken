package GUI;

import Musik.MusikPlayer;
import SaveLoad.SaveLoad;
import Server.Server;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import schiffeversenken.SchiffeVersenken;
import shapes.Richtung;
import shapes.Schiff;
import var.var;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SpielGUIController implements Initializable {

    @FXML
    private Button btn_neuPlatzieren;
    @FXML
    private Button btn_Random;
    @FXML
    private Pane paneGrid;
    @FXML
    private Pane setzenControll;
    @FXML
    private Button spielstart;
    @FXML
    private Button clientWartet;
    @FXML
    private Pane boundsRec;
    @FXML
    private Rectangle borderRec;
    @FXML
    private Button saveButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Pane einstellungen;
    @FXML
    private Slider slider;
    @FXML
    private Button buttonInfo; 
    @FXML
    private Pane InfoCard;
    @FXML
    private Text infoDrei;
    @FXML
    private Text infoZwei;
    @FXML
    private Text infoEins;
    @FXML
    private Pane infoPane;
    @FXML
    private Button btnMenue;
    @FXML
    private Button btnBeenden;
    @FXML
    private Label restFuenfer;
    @FXML
    private Label restVierer;
    @FXML
    private Label restDreier;
    @FXML
    private Label restZweier;
    @FXML
    private Label statusLabel1;
    @FXML
    private Label statusLabel2;  
    @FXML
    private Label statusAllgemein;
    @FXML
    private Label infoTextVerbindung;
    @FXML
    private Label infoText;
    @FXML
    private Label infoText2;

    private LokalesSpielSteuerung dieLokalesSpielSteuerung = null;
    private KISpielSteuerung dieKISpielSteuerung = null;
    private OnlineSpielSteuerung dieOnlineSpielSteuerung = null;
    
    boolean fertig = false;
    boolean spielbereit = false;
    private int modus;
    private String ip = null; // Null wenn Lokales Spiel 
    private FileChooser fc = new FileChooser();
    private int kiStufe;
    private int anzahlSchiffe;
    private boolean offenInfo = false;
    private SaveLoad saveLoad = new SaveLoad();
    Musik.MusikPlayer mp = new MusikPlayer();
    private boolean offen = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SpielGUI");
        clientWartet.setVisible(false);
        saveButton.setVisible(false);
        btnBeenden.setVisible(false);
        btnMenue.setVisible(false);
        fc.setInitialDirectory(new File("src/saves/"));
        String musicFile = "/Musik/musicGame.mp3";
        mp.setMusikGame(musicFile);
        einstellungen.setVisible(false);
        InfoCard.setVisible(false);
        slider.setValue(var.lautstaerke * 100);
        slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::changeMusikHandler);
        restZweier.setText("0");
        restDreier.setText("0");
        restVierer.setText("0");
        restFuenfer.setText("0");
        statusLabel1.setVisible(false);
        statusLabel2.setVisible(false);
        spielstart.setVisible(false);
        infoTextVerbindung.setVisible(false);
        infoText2.setVisible(false);
    }

    /**
     * Implementiert die Übergabe der Parameter aus der Modi-Menue Scene in die SpielGui Scene.
     * Anschließend folgt die Erstellung der jeweiligen Spielsteuerung abhängig vom Modus.
     * Zudem werden hier, wenn es sich um einen menschlichen Spieler handelt, die jeweiligen SchiffeSetzen Steuerungen zum eigenen Schifffe 
     * setzen erzuegt, wenn es sich um eine Ki haldelt werden die Schiffe zufällig erzeugt und die Grids übergeben.
     * Außerdem werden hier Server und Client erstellt.
     * 
     * @param spielfeldgroesse Im Modi-Menü ausgewählte Spielfeldgröße zwischen 5 und 30
     * @param anzahlSchiffeTyp Array mit der Anzahl der ausgewählten Schiffe je nach Typ: An Stelle 0 Anzahl Zweier, an Stelle 1 Anzahl Dreier, an Stelle 3 Anzahl Vierer, an Stelle 4 Anzahl Fünfer
     * @param modus Modus: 1: LokalesSpiel, 21: KISpiel Server, 22: KISpiel Client, 31: OnlineSpiel Server, 32: OnlineSpiel Client
     * @param ip Wenn man Client ist, dann ist ip die im Modimenü angegebene IP Adresse des Serves, ansonsten ein leerer String
     * @param kiStufe Schwierigkeitsstufe der Ki, 1: Zufällige Schüsse, 2: Zufällige Schüsse und wenn getroffen Schiff vervollständigen, 3: Schiffe nach Muster und wenn getroffen Schiff vervollstädigen
     */
    public void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip, int kiStufe) {
        this.modus = modus;
        this.ip = ip;

        if (modus == 1) { // Lokales Spiel 
            dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe); // Erzeuge SpielSteuerung
            dieLokalesSpielSteuerung.erzeugeEigeneSchiffe(); // Eigene Schiffe setzen könne
            spielstart.setVisible(true);
        } else if (modus == 21 || modus == 22) { // KI Spiel - 21 ki-host - 22 ki-client 
            infoEins.setText("KI Spiel keine Aktion möglich");  // Infotexte setzen 
            infoZwei.setText("Blau ist Wasser");
            infoDrei.setText("Rotes Kreuz ist versenkt");
            btnMenue.setVisible(false);
            if (modus == 21) { // host
                dieKISpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe); // Erzeuge SpielSteuerung mit Parametern
                dieKISpielSteuerung.erzeugeEigeneSchiffe(); // Eigene Schiffe zufällig erzeugen
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung.werdeServer(false); // Werde Server, false da nicht geladen sondern neu gespielt wird
                infoTextVerbindung.setVisible(true);
                if (dieKISpielSteuerung.isFertigSetzen()) { // Sobald die Ki fertig ist
                    spielbereit = true; // Ist sie spielbereit
                    dieKISpielSteuerung.setSchiffeSetzen(); // schiffe übergeben in Steuerung
                    dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts()); // Grids überspeichern
                    dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks());
                    dieKISpielSteuerung.getGridSpielfeldRechts().print(); // Grids in Konsole ausgeben
                    dieKISpielSteuerung.getGridSpielfeldLinks().print();
                    dieKISpielSteuerung.setzeSchiffeKI(); // Schiffe auf der linken Seite anzeigen
                    dieKISpielSteuerung.beginneSpiel(); // Beginne Spiel 
                }
            } else if (modus == 22) { // client
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung = new KISpielSteuerung(this); // Erzeuge SpielStuerung ohne Parameter, da der Server diese erst übermitteln muss
                this.kiStufe = kiStufe;
                dieKISpielSteuerung.werdeClient(); // Werde Client
                infoTextVerbindung.setVisible(true);
                try {                   // Sleep um genug Zeit für die Verbindung zu haben
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (modus == 31 || modus == 32) { // Online Spiel - 31 host - 32 client
            if (modus == 31) { // host
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp); // Erzeuge SpielSteuerung mit Parametern
                dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                dieOnlineSpielSteuerung.werdeServer(false);
            } else if (modus == 32) { // client
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this); // Erzeuge SpielSteuerung ohne Parametern, da der Server diese erst übermitteln muss
                dieOnlineSpielSteuerung.werdeClient();
                try {                   // Sleep um genug Zeit für die Verbindung zu haben
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Implementiert die Übergabe der Parameter aus der Modi-Menue Scene in die SpielGui Scene, wenn ein 
     * lokales Spiel geladen wird
     * und erstellt anschließend eine neue SpielSteuerung für ein lokales Spiel mit den geladenen
     * Daten. 
     * 
     * @param styp Anzahl Schiffe je Typ
     * @param paramInc 0: spielfeldgroesse, 1: Modus, 2: KiStufe, 3: AnzGetroffen, 4: EigeneSchiffeGetroffen
     * @param gridRechtsArr Zweidemensionales Array mit Schiffs IDs des rechten Grids
     * @param gridLinksArr Zweidemensionales Array mit Schiffs IDs des linken Grids
     * @param getroffenGeg Zweidemensionales Array mit Informatioen wo die KI schonmal hingeschossen hat und was sie dort gefunden hat
     * @param getroffenAr Zweidemensionales Array mit Informationen wo der Spieler schonmal hingeschossen hat und was dort versteckt ist, inklusiv Felder die definitiv Wasser sind 
     * @param getroffenKI Zweidemensionales Array mit Informatioen wo die KI schonmal hingeschossen hat und was sie dort gefunden hat, inlusiv Felder die definitiv Wasser sind
     * @param letzterSchussKI Letzter Schuss der KI, Zeile und Spalte des Schusses
     * @param angefSchiffKI Falls es ein angefangenes Schiff gibt, hier gespeichert die Koordinaten (Zeile und Spalte) davon 
     * @param kiValues 0: AnzGetroffen(), 1: Richtung eines Angefangenen Schiffs, 2: Boolean ob es ein angefangenes Schiff gibt, 3: kiStufe
     */
    public void uebergebeInformationenLokal(int[] styp, int[] paramInc, int[][] gridRechtsArr, int[][] gridLinksArr, int[][] getroffenGeg, int[][] getroffenAr, int[][] getroffenKI, int[] letzterSchussKI, int[] angefSchiffKI, int[] kiValues) {
        paneGrid.getChildren().clear();
        setzenControll.getChildren().clear();
        setzenControll.setBorder(Border.EMPTY);
        spielstart.setVisible(false);
        saveButton.setVisible(true);
        btnMenue.setVisible(true);
        btnBeenden.setVisible(true);
        modus = paramInc[1]; // Modus 1 : Lokales Spiel setzen
        infoEins.setText("Feld rechts anklicken"); // Infotexte setzen
        infoZwei.setText("Blau ist Wasser");
        infoDrei.setText("Rotes Kreuz ist versenkt");
        dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, styp, paramInc, gridRechtsArr, gridLinksArr, getroffenGeg, getroffenAr, getroffenKI, letzterSchussKI, angefSchiffKI, kiValues); // Erzeuge SpielSteuerung mit geladenen Werten
        dieLokalesSpielSteuerung.beginneSpiel(); // Spiel beginnen
    }
    
    
    /**
     * Implementiert die Übergabe der Parameter aus der Modi-Menue Scene in die SpielGui Scene, wenn ein 
     * Online Spiel geladen wird
     * und erstellt anschließend eine neue SpielSteuerung für ein Online Spiel mit den geladenen
     * Daten. 
     * 
     * @param ip IP Adresse, falls eine gespeichert wurde
     * @param l long ID für die gespeicherte ID
     * @param paramInc 0: spielfeldgroesse, 1: Modus, 2: AnzGetroffen, 3: EigeneSchiffeGetroffen
     * @param styp Anzahl Schiffe je Typ
     * @param getroffenAr Zweidemensionales Array mit Informationen wo der Spieler schonmal hingeschossen hat und was dort versteckt ist, inklusiv Felder die definitiv Wasser sind 
     * @param getroffenGeg Zweidemensionales Array mit Informatioen wo die KI schonmal hingeschossen hat und was sie dort gefunden hat
     * @param gridRechtsArr Zweidemensionales Array mit Schiffs IDs des rechten Grids
     * @param gridLinksArr Zweidemensionales Array mit Schiffs IDs des linken Grids
     * @param onlineValues 0: EigeneSchiffe Getroffen, 1: Aktiver Spieler
     */
    public void uebergebeInformationenOnline(String[] ip, long[] l, int[] paramInc, int[] styp, int[][] getroffenAr, int[][] getroffenGeg, int[][] gridRechtsArr, int[][] gridLinksArr, int[] onlineValues) {
        paneGrid.getChildren().clear();
        setzenControll.getChildren().clear();
        setzenControll.setBorder(Border.EMPTY);
        spielstart.setVisible(false);
        saveButton.setVisible(true);
        btnMenue.setVisible(true);
        btnBeenden.setVisible(true);
        infoEins.setText("Feld rechts anklicken"); // Infotexte setzen
        infoZwei.setText("Blau ist Wasser");
        infoDrei.setText("Rotes Kreuz ist versenkt");
        this.modus = 31; // Die Person welche im Menü läd wird automatisch Server
        this.ip = ip[0];
        dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, styp, paramInc, gridRechtsArr, gridLinksArr, getroffenAr, getroffenGeg, onlineValues, l);  // Erzeuge SpielSteuerung mit geladenen Werten
        dieOnlineSpielSteuerung.werdeServer(true); // Werde Server mit Übergabe true da Spiel geladen ist
        dieOnlineSpielSteuerung.beginneSpielLaden(); // Beginne Spielladen
    }

    /**
     * Zeige die jeweiligen Bilder der jeweilig unterschiedlich großen und unterschiedlich liegenden Schiffen an.
     * @param schiff Objekt Schiff erstellt
     */
    public void zeichneSchiffe(Schiff schiff) {
        if (dieKISpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        } else if (dieOnlineSpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        } else if (dieLokalesSpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    Image img = new Image("/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png");
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    Image img = new Image("/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png");
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        }

    }

    /**
     * Hier werden die Steuerungen der Clients vervollständigt, nach dem Übertragen der Daten vom Server zum Client
     */
    public void erstelleSteuerung() {
        if (modus == 22) { // KI Spiel Cient
            System.out.println("Erstelle Steuerung");
            dieKISpielSteuerung.erzeugeKI(kiStufe); // Erzeuge KI
            dieKISpielSteuerung.erzeugeEigeneSchiffe(); // Erzeuge eigene Schiffe, da jetzt bekannt welche Art Schiffe erstellt werden sollen

            if (dieKISpielSteuerung.isFertigSetzen()) { // Wenn Ki fertig erstellt
                dieKISpielSteuerung.setSchiffeSetzen(); // Dann übergebe Schiffe an die Steuerung
                
                Platform.runLater(new Runnable() {  
                    @Override
                    public void run() { 
                        dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts()); // Überschreibe Grid Rechts
                        dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks()); // Überschriebe Grid Links
                        dieKISpielSteuerung.getGridSpielfeldRechts().print(); // Ausgabe der Grids auf der Konsole
                        dieKISpielSteuerung.getGridSpielfeldLinks().print();
                        dieKISpielSteuerung.setzeSchiffeKI(); // Stetze Schiffe der KI
                        fertig = true;
                        spielbereit = true; // spielbereit setzen
                        infoText2.setVisible(true);
                        dieKISpielSteuerung.beginneSpiel(); // Spiel beginnen
                    }
                });
            }

        } else if (modus == 32) { // Online Spiel Client
            if (dieOnlineSpielSteuerung.getClient().isVerbindung()) { // Wenn eine Verbindung besteht
                dieOnlineSpielSteuerung.erzeugeSteuerungSchiffeSetzen(); // Dann erzeuge Steuerung zum Schiffe Setzen

                Platform.runLater(new Runnable() { 
                    @Override
                    public void run() {
                        dieOnlineSpielSteuerung.erzeugeEigeneSchiffe(); // Erzeuge eigene Schiffe
                        fertig = true;
                    }
                });
            }
        }
    }

    /**
     * Wird aufgerufen wenn der Button "Start" nach dem fertigen Schiffe Setzen gedrückt wird.
     * Wechselt zum eigentlichen Spiel.
     * 
     * @param event 
     */
    @FXML
    private void handleButton(ActionEvent event) {
        if ((dieLokalesSpielSteuerung instanceof LokalesSpielSteuerung && dieLokalesSpielSteuerung.isFertigSetzen())) { // Wenn Lokales Spiel fertig gesetzt
            dieLokalesSpielSteuerung.erzeugeGegnerSchiffe(); // Ki erzeugt ihre Schiffe
            if (dieLokalesSpielSteuerung.gegnerKiIsFertig()) { // Wenn Ki fertig mit erzuegen ist
                paneGrid.getChildren().clear(); 
                setzenControll.getChildren().clear();
                setzenControll.setBorder(Border.EMPTY);
                spielstart.setVisible(false);
                dieLokalesSpielSteuerung.setSchiffeSetzen(); // Schiffe setzen

                dieLokalesSpielSteuerung.setGridSpielfeldSpielRechts(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts()); // Zeige Grid rechts
                dieLokalesSpielSteuerung.enableMouseClickSoielfeldGridRechts(); // Auf dem rechten Grid , MouseClick aktivieren um schiessen zu können
                dieLokalesSpielSteuerung.setGridSpielfeldSpielLinks(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks()); // Zeige Grid links
                dieLokalesSpielSteuerung.setzeSchiffe(); // Schiffe zeigen
                System.out.println("Eigenes Feld");
                dieLokalesSpielSteuerung.getGridSpielfeldLinks().print(); // Ausgabe des Grids links auf der Konsole
                dieLokalesSpielSteuerung.beginneSpiel(); // Spiel beginnen
                saveButton.setVisible(true);
                getBtnBeenden().setVisible(true);
                getBtnMenue().setVisible(true);
                infoEins.setText("Feld rechts anklicken"); // Infotexte setzen
                infoZwei.setText("Blau ist Wasser");
                infoDrei.setText("Rotes Kreuz ist Treffer");
            }
        } else if (dieOnlineSpielSteuerung instanceof OnlineSpielSteuerung && dieOnlineSpielSteuerung.isFertigSetzen()) { // Wenn Spieler in Online Spiel seine Schiffe fertig gesetzt hat
            getBtnBeenden().setVisible(true);
            getBtnMenue().setVisible(true);
            spielstart.setVisible(false);
            saveButton.setVisible(true);
            infoEins.setText("Feld rechts anklicken"); // Infotexte setzen
            infoZwei.setText("Blau ist Wasser");
            infoDrei.setText("Rotes Kreuz ist versenkt");
            if (modus == 31 && dieOnlineSpielSteuerung.getServer().isVerbindung()) { // Wenn Server und Verbindung zu Client da
                if ((dieOnlineSpielSteuerung.getServer() != null && !dieOnlineSpielSteuerung.getServer().isClientReady()) || (dieOnlineSpielSteuerung.getClient() != null && !dieOnlineSpielSteuerung.getClient().isServerReady())) {
                    infoText2.setVisible(true); // Anzeigen wenn Partner noch nicht fertig mit Setzen ist
                }
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                dieOnlineSpielSteuerung.setSchiffeSetzen(); // Schiffe setzen
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts()); // Zeige Grid rechts
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts(); // Auf dem rechten Grid , MouseClick aktivieren um schiessen zu können
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks()); // Zeige Grid links
                dieOnlineSpielSteuerung.setzeSchiffe(); // Schiffe zeigen
                System.out.println("Eigenes Feld");
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print(); // Ausgabe des Grids Links auf der Konsole

                spielbereit = true; // Spielbereit setzen
                if (dieOnlineSpielSteuerung.getServer().isReadyNochSenden()) { // wenn Server noch nicht ready gesendet hat
                    System.out.println("Nachricht senden: " + "ready");
                    dieOnlineSpielSteuerung.getServer().send("ready"); // Dann sende ready jetzt
                }
                dieOnlineSpielSteuerung.beginneSpiel(); // beginne Spiel

            } else if (modus == 32 && dieOnlineSpielSteuerung.getClient().isVerbindung()) { // Wenn Client und Verbindung zu Server da
                if ((dieOnlineSpielSteuerung.getServer() != null && !dieOnlineSpielSteuerung.getServer().isClientReady()) || (dieOnlineSpielSteuerung.getClient() != null && !dieOnlineSpielSteuerung.getClient().isServerReady())) {
                    infoText2.setVisible(true); // Anzeigen wenn Partner noch nicht fertig mit Setzen ist
                }

                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                dieOnlineSpielSteuerung.setSchiffeSetzen(); // Schiffe setzen
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts()); // Zeige Grid rechts
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts(); // Auf dem rechten Grid , MouseClick aktivieren um schiessen zu können
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks()); // Zeige Grid links
                dieOnlineSpielSteuerung.setzeSchiffe(); // Schiffe zeigen
                System.out.println("Eigenes Feld");
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print(); // Ausgabe des Grids Links auf der Konsole

                spielbereit = true; // spielbereit setzen
                if (dieOnlineSpielSteuerung.getClient().isReadyNochSenden()) { // wenn client noch nicht ready gesendet hat
                    zeigeStatusLabel(1, false);
                    zeigeStatusLabel(2, true);
                    System.out.println("Nachricht senden: " + "ready");
                    dieOnlineSpielSteuerung.getClient().send("ready"); // Dann sende ready jetzt
                }

                dieOnlineSpielSteuerung.beginneSpiel(); // beginne Spiel
            }
        }
    }

    /**
     * Steuert das Verhalten bei click auf den Button "Schiffe Neu Plazieren"
     *
     * @param internes javafx event, dass die Funktion auslöst
     */
    @FXML
    private void handleButtonNeuPlatzieren(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.clearSchiffeSetzen();
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.clearSchiffeSetzen();
        }
    }

    /**
     * Steuert das Verhalten bei click auf den Button "Schiffe Zufällig plazieren".
     * Die Schifffe werden zufällig plaziert. 
     *
     * @param internes javafx event, dass die Funktion auslöst
     */
    @FXML
    private void handleButtonRandom(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.randomSetzen();
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.randomSetzen();
        }
    }

    /**
     * Funktion wird aufgerufen wenn der Client den "Wieder verbinden" Button aktiviert.
     * Dieser wird eingeblendet, wenn der Client startet bevor der Server gestartet hat.
     * Hier wird dann versucht sich erneut zu verbinden.
     * @param event 
     */
    @FXML
    private void handleButtonWarten(ActionEvent event) {
        if (modus == 32) { // Client vom Online Spiel
            dieOnlineSpielSteuerung.werdeClient(); // Versuchen Client zu werden
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dieOnlineSpielSteuerung.getClient().isVerbindung()) { // Wenn Client geworden
                clientWartet.setVisible(false); // Dann Button wieder ausblenden
                spielstart.setVisible(true);
                setzenControll.setVisible(true);
                infoTextVerbindung.setVisible(false);
            }
        } else if (modus == 22) { // Client vom KI Spiel
            dieKISpielSteuerung.werdeClient(); // Versuchen Client zu werden
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dieKISpielSteuerung.getClient().isVerbindung()) { // Wenn Client geworden
                clientWartet.setVisible(false); // Dann Button wieder ausblenden
                spielstart.setVisible(false);
                setzenControll.setVisible(true);
                infoTextVerbindung.setVisible(false);
            }
        }
    }
    
    /**
     * Die Funktion gibt auf einem Label aus, ob man verloren oder gewonnen hat sobald ein Part alle Schiffe
     * getroffen hat.
     * @param gewinner 1 für Gegner, 2 für Spieler
     */
    public void spielEnde(int gewinner) { 
        paneGrid.setDisable(true);
        saveButton.setVisible(false);
        btnMenue.setVisible(true);
        btnBeenden.setVisible(true);
        spielstart.setVisible(false);
        infoPane.getChildren().clear();
        Label l = new Label();
        infoPane.getChildren().add(l);
        l.setLayoutX(300);
        l.setLayoutY(60);
        if (gewinner == 2) { // Wenn Spieler selbst gewonnen
            if (var.pxGroesse <= 500) {
                l.setLayoutX(20);
                l.setLayoutY(40);
            }
            l.setStyle(" -fx-font-size: 55; -fx-font-weight: 700 ");
            l.setText("Glückwunsch du hast Gewonnen"); // dann gebe aus Gewonnen
        } else { // wenn Gegner gewonnen hat
            if (var.pxGroesse <= 500) {
                l.setLayoutX(20);
                l.setLayoutY(40);
            }
            l.setStyle("-fx-font-size: 55; -fx-font-weight: 700 ");
            l.setText("Schade du hast Verloren"); // dann gebe aus Verloren
        }
    }

    /**
     * Wird aufgerufen wenn Button "Speichern" gedrückt wird. Wenn Spieler berechtigt ist zu speichern dann
     * wird mithilfe von saveLoad das speichern eingeleitet.
     * @param event 
     */
    @FXML
    private void speicherSpiel(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null && dieLokalesSpielSteuerung.getAktiverSpieler() == 0) { // Nur der aktive Spieler darf speichern
            if (saveLoad.speicherSpiel(this, dieLokalesSpielSteuerung)) { // Lokales Spiel Speichern aufrufen
                btnBeenden.setVisible(true);
                btnMenue.setVisible(true);
            }
        } 
        else if (dieOnlineSpielSteuerung != null && dieOnlineSpielSteuerung.getAktiverSpieler() == 0 && !infoText2.isVisible()) { // Nur der aktive Spieler darf speichern
            if (saveLoad.speicherSpiel(this, dieOnlineSpielSteuerung)) { // Online Spiel Speichern aufrufen
                btnBeenden.setVisible(true);
                btnMenue.setVisible(true);
                System.out.println("Nachricht senden: " + "save " + saveLoad.getL()[0]); 
                if(dieOnlineSpielSteuerung.getServer() != null){ // Server speichert
                    dieOnlineSpielSteuerung.getServer().setGespeichert(true); // Speichern dass gerade gespeichert wird
                    dieOnlineSpielSteuerung.getServer().send("save " + saveLoad.getL()[0]); // Sende save id an Client
                } else if (dieOnlineSpielSteuerung.getClient() != null) { // Client speichert
                    dieOnlineSpielSteuerung.getClient().send("save " + saveLoad.getL()[0]); // Sende save id an Sever
                }
            }
        }

    }
     
    /**
     * Öffne Settings oder schließe sie wieder
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
     * Musiklaustärke ändern
     * @param e 
     */
    private void changeMusikHandler(MouseEvent e) {
        var.lautstaerke = slider.getValue() / 100;
        mp.setLautstaerkeGame(slider.getValue() / 100);
    }

    
    /**
     * Methode zum Anzeigen oder Ausblenden der Infotexten
     * @param event 
     */
    @FXML
    private void handleButtonInfo(ActionEvent event) {
        if (offenInfo) {
            InfoCard.setVisible(false);
            offenInfo = false;
        } else {
            offenInfo = true;
            InfoCard.setVisible(true);
        }
    }

    /**
     * Wechsel wärend des Spiels wieder zurück zum Menü und beende dabei alle bestehenden Verbindungen
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleButtonMenue(ActionEvent event) throws IOException {
        if (dieOnlineSpielSteuerung != null) {
            if (dieOnlineSpielSteuerung.getClient() != null) {
                dieOnlineSpielSteuerung.getClienT().interrupt();
                dieOnlineSpielSteuerung.getClient().end();
            } else if (dieOnlineSpielSteuerung.getServer() != null) {
                dieOnlineSpielSteuerung.getServerT().interrupt();
                dieOnlineSpielSteuerung.getServer().end();
            }
        }else if(dieKISpielSteuerung != null){
            if (dieKISpielSteuerung.getClient() != null) {
                dieKISpielSteuerung.getClientT().interrupt();
               dieKISpielSteuerung.getClient().end();
            } else if (dieKISpielSteuerung.getServer() != null) {
                dieKISpielSteuerung.getServerT().interrupt();
                dieKISpielSteuerung.getServer().end();
            }
        }
        var.musik_laueft = false;
        mp.setMusikMenue();
        SchiffeVersenken.getApplicationInstance().restart(); //Startet die Stage neu
    }

    /**
     * Beendet die Applikation mit code 0
     *
     * @param event
     */
    @FXML
    private void handleButtonBeenden(ActionEvent event) throws IOException {
        if (dieOnlineSpielSteuerung != null) {
            if (dieOnlineSpielSteuerung.getClient() != null) {
                dieOnlineSpielSteuerung.getClienT().interrupt();
                dieOnlineSpielSteuerung.getClient().end();
            } else if (dieOnlineSpielSteuerung.getServer() != null) {
                dieOnlineSpielSteuerung.getServerT().interrupt();
                dieOnlineSpielSteuerung.getServer().end();
            }
        }else if(dieKISpielSteuerung != null){
            if (dieKISpielSteuerung.getClient() != null) {
                dieKISpielSteuerung.getClientT().interrupt();
               dieKISpielSteuerung.getClient().end();
            } else if (dieKISpielSteuerung.getServer() != null) {
                dieKISpielSteuerung.getServerT().interrupt();
                dieKISpielSteuerung.getServer().end();
            }
        }
        System.exit(0);
    }

    /**
     * Gibt ein zweidimensionales Array auf der Konsole aus
     *
     * @param arr - array, das ausgegeben werden soll
     */
    public static void print(int[][] arr) {
        System.out.println("");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
    
    
    //Getter und Setter Methoden
     
    public Pane getSetzenControll() {
        return setzenControll;
    }

    public Label getInfoTextVerbindung() {
        return infoTextVerbindung;
    }
    
    public boolean isSpielbereit() {
        return spielbereit;
    }

    public Button getClientWartet() {
        return clientWartet;
    }

    public void infoText2LabelVisable(boolean bool) {
        infoText2.setVisible(bool);
    }

    public void spielStartButton(boolean bool) {
        spielstart.setVisible(bool);
    }
    
    public void setSpielbereit(boolean spielbereit) {
        this.spielbereit = spielbereit;
    }

    public SaveLoad getSaveLoad() {
        return saveLoad;
    }

    public Label getStatusAllgemein() {
        return statusAllgemein;
    }

    public LokalesSpielSteuerung getDieLokalesSpielSteuerung() {
        return dieLokalesSpielSteuerung;
    }

    public Server getServer() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getServer();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getServer();
        } else {
            return null;
        }
    }
    
    public void zeigeLinie(Line line) {
        paneGrid.getChildren().add(line);
    }

    public KISpielSteuerung getDieKISpielSteuerung() {
        return dieKISpielSteuerung;
    }

    public OnlineSpielSteuerung getDieOnlineSpielSteuerung() {
        return dieOnlineSpielSteuerung;
    }

    public boolean isSpielFertig() {
        if (dieLokalesSpielSteuerung != null) {
            return dieLokalesSpielSteuerung.isSpielEnde();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.isSpielEnde();
        } else if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.isSpielEnde();
        }
        return false;
    }
    
    public void zeigeStatusLabel(int i, boolean bool) {
        if (i == 1) {
            statusLabel1.setVisible(bool);
        } else if (i == 2) {
            statusLabel2.setVisible(bool);
        }
    }

    public Label getInfoText() {
        return infoText;
    }

    public int getSpielfeldgroesse() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getSpielfeldgroesse();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getSpielfeldgroesse();
        } else if (dieLokalesSpielSteuerung != null) {
            return dieLokalesSpielSteuerung.getSpielfeldgroesse();
        }
        return 0;
    }

    public int[] getAnzahlSchiffeTyp() {
        if (dieKISpielSteuerung != null) {
            return dieKISpielSteuerung.getAnzahlSchiffeTyp();
        } else if (dieOnlineSpielSteuerung != null) {
            return dieOnlineSpielSteuerung.getAnzahlSchiffeTyp();
        } else if (dieLokalesSpielSteuerung != null) {
            return dieLokalesSpielSteuerung.getAnzahlSchiffeTyp();
        }
        return null;
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        if (dieKISpielSteuerung != null) {
            dieKISpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        } else if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.setSpielfeldgroesse(spielfeldgroesse);
        }
    }

    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        if (dieKISpielSteuerung != null) {
            dieKISpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
            dieKISpielSteuerung.setAnzahlSchiffe();
        } else if (dieOnlineSpielSteuerung != null) {
            dieOnlineSpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
            dieOnlineSpielSteuerung.setAnzahlSchiffe();
        } else if (dieLokalesSpielSteuerung != null) {
            dieLokalesSpielSteuerung.setAnzahlSchiffeTyp(anzahlSchiffeTyp);
        }
    }

    public String getIp() {
        return ip;
    }

    public void zeigeGridRechts(Rectangle rectangle) {
        paneGrid.getChildren().add(rectangle);
    }

    public void zeigeGridLinks(Rectangle rectangle) {
        paneGrid.getChildren().add(rectangle);
    }

    public void zeigeSchiffeLinks(Schiff schiff) {
        paneGrid.getChildren().add(schiff);
    }

    public void zeigeSchiffeRechts(Schiff schiff) {
        paneGrid.getChildren().add(schiff);
    }

    public void zeigeSchiffLinks(Rectangle rec) {
        paneGrid.getChildren().add(rec);
    }

    public void zeigeSchiffRechts(Rectangle rec) {
        paneGrid.getChildren().add(rec);
    }

    public Pane getBoundsRec() {
        return boundsRec;
    }

    public Rectangle getBorderRec() {
        return borderRec;
    }

    public Label getRestFuenfer() {
        return restFuenfer;
    }

    public void setRestFuenfer(String rest) {
        restFuenfer.setText(rest);
    }

    public Label getRestVierer() {
        return restVierer;
    }

    public void setRestVierer(String rest) {
        restVierer.setText(rest);
    }

    public Label getRestDreier() {
        return restDreier;
    }

    public void setRestDreier(String rest) {
        restDreier.setText(rest);
    }

    public Label getRestZweier() {
        return restZweier;
    }

    public void setRestZweier(String rest) {
        restZweier.setText(rest);
    }

    public void setAnzahlSchiffe(int wert) {
        this.anzahlSchiffe = wert;
    }

    public boolean isFertig() {
        return fertig;
    }

    public int getModus() {
        return modus;
    }

    public Button getBtn_neuPlatzieren() {
        return btn_neuPlatzieren;
    }

    public Button getBtn_Random() {
        return btn_Random;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getBtnMenue() {
        return btnMenue;
    }

    public Button getBtnBeenden() {
        return btnBeenden;
    }

    public Button getSpielstart() {
        return spielstart;
    }

    public void wartenAufVerbindung(boolean bool) {
        infoTextVerbindung.setVisible(bool);
    }
    
}
