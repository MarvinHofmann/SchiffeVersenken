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

    private LokalesSpielSteuerung dieLokalesSpielSteuerung = null;
    private KISpielSteuerung dieKISpielSteuerung = null;
    private OnlineSpielSteuerung dieOnlineSpielSteuerung = null;
    boolean fertig = false;

    private int modus;
    private String ip = null; // Null wenn Lokales Spiel 
    private FileChooser fc = new FileChooser();
    private int kiStufe;
    private int anzahlSchiffe;
    private SaveLoad saveLoad = new SaveLoad();

    private Label outputField;

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

    private boolean offenInfo = false;
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

    Musik.MusikPlayer mp = new MusikPlayer();
    @FXML
    private Label statusAllgemein;
    @FXML
    private Label infoTextVerbindung;
    @FXML
    private Label infoText;
    
    boolean spielbereit = false;

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
    }

    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp, int modus, String ip, int kiStufe) {
        this.modus = modus;
        this.ip = ip;

        if (modus == 1) { // Lokales Spiel 
            dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe); // Erzeuge SpielSteuerung
            dieLokalesSpielSteuerung.erzeugeEigeneSchiffe();
            spielstart.setVisible(true);
        } else if (modus == 21 || modus == 22) { // KI Spiel - 21 ki-host - 22 ki-client 
            infoEins.setText("KI Spiel keine Aktion möglich");
            infoZwei.setText("Blau ist Wasser");
            infoDrei.setText("Rotes Kreuz ist versenkt");
            if (modus == 21) { // host
                dieKISpielSteuerung = new KISpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp, kiStufe);
                dieKISpielSteuerung.erzeugeEigeneSchiffe();
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung.werdeServer(false);
                infoTextVerbindung.setVisible(true);
                statusLabel1.setVisible(false);
                if (dieKISpielSteuerung.isFertigSetzen()) {
                    spielbereit = true;
                    dieKISpielSteuerung.setSchiffeSetzen();
                    dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts());
                    dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks());
                    dieKISpielSteuerung.getGridSpielfeldRechts().print();
                    dieKISpielSteuerung.getGridSpielfeldLinks().print();
                    dieKISpielSteuerung.setzeSchiffeKI();
                    dieKISpielSteuerung.beginneSpiel();
                }
            } else if (modus == 22) { // client
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                spielstart.setVisible(false);
                dieKISpielSteuerung = new KISpielSteuerung(this);
                this.kiStufe = kiStufe;
                dieKISpielSteuerung.werdeClient();
                infoTextVerbindung.setVisible(true);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!dieKISpielSteuerung.getClient().isVerbindung()) {
                    clientWartet.setVisible(true);
                    spielstart.setVisible(false);
                    setzenControll.setVisible(false);
                }
            }
        } else if (modus == 31 || modus == 32) { // Online Spiel - 31 host - 32 client
            if (modus == 31) { // host
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, spielfeldgroesse, anzahlSchiffeTyp);
                dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                dieOnlineSpielSteuerung.werdeServer(false);
            } else if (modus == 32) { // client
                dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this);
                dieOnlineSpielSteuerung.werdeClient();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!dieOnlineSpielSteuerung.getClient().isVerbindung()) {
                    clientWartet.setVisible(true);
                    spielstart.setVisible(false);
                    setzenControll.setVisible(false);
                }
            }
        }
    }
    
    public void label1Visable(boolean bool){
        statusLabel1.setVisible(bool);
    }
    
    public void label2Visable(boolean bool){
        statusLabel2.setVisible(bool);
    }

    public void spielStartButton(boolean bool){
        spielstart.setVisible(bool);
    }
    
    void uebergebeInformationenLokal(int[] styp, int[] paramInc, int[][] gridRechtsArr, int[][] gridLinksArr, int[][] getroffenGeg, int[][] getroffenAr, int[][] getroffenKI, int[] letzterSchussKI, int[] angefSchiffKI, int[] kiValues) {
        // ParamInc: 0 -> spielfeldgroesse(), 1-> Modus(), 2 -> KiStufe(), 3-> AnzGetroffen(), 4-> EigeneSchiffeGetroffen()};
        paneGrid.getChildren().clear();
        setzenControll.getChildren().clear();
        setzenControll.setBorder(Border.EMPTY);
        spielstart.setVisible(false);
        saveButton.setVisible(true);
        infoEins.setText("Feld rechts anklicken");
        infoZwei.setText("Blau ist Wasser");
        infoDrei.setText("Rotes Kreuz ist versenkt");
        dieLokalesSpielSteuerung = new LokalesSpielSteuerung(this, styp, paramInc, gridRechtsArr, gridLinksArr, getroffenGeg, getroffenAr, getroffenKI, letzterSchussKI, angefSchiffKI, kiValues); // Erzeuge SpielSteuerung
        dieLokalesSpielSteuerung.beginneSpiel();
    }

    void uebergebeInformationenOnline(String[] ip, long[] l, int[] paramInc, int[] styp, int[][] getroffenAr, int[][] getroffenGeg, int[][] gridRechtsArr, int[][] gridLinksArr, int[] onlineValues) {
        paneGrid.getChildren().clear();
        setzenControll.getChildren().clear();
        setzenControll.setBorder(Border.EMPTY);
        spielstart.setVisible(false);
        saveButton.setVisible(true);
        infoEins.setText("Feld rechts anklicken");
        infoZwei.setText("Blau ist Wasser");
        infoDrei.setText("Rotes Kreuz ist versenkt");
        this.modus = paramInc[1];
        this.ip = ip[0];
        dieOnlineSpielSteuerung = new OnlineSpielSteuerung(this, styp, paramInc, gridRechtsArr, gridLinksArr, getroffenAr, getroffenGeg, onlineValues, l);
        if (modus == 31) {
            dieOnlineSpielSteuerung.werdeServer(true);
        } else if (modus == 32) {
            dieOnlineSpielSteuerung.werdeClient();
        }
        /*try { // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
            Thread.sleep(500);
        }catch (InterruptedException e) {
                    e.printStackTrace();
                }
        if (!dieOnlineSpielSteuerung.getClient().isVerbindung()) {
            clientWartet.setVisible(true);
            spielstart.setVisible(false);
            setzenControll.setVisible(false);
        }*/
        dieOnlineSpielSteuerung.beginneSpielLaden(); // wenn verbindung da
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

    public void zeigeStatusLabel(int i, boolean bool) {
        if (i == 1) {
            statusLabel1.setVisible(bool);
        } else if (i == 2) {
            statusLabel2.setVisible(bool);
        }
    }
    
    public Label getInfoText(){
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

    public Label getStatusLabel1() {
        return statusLabel1;
    }

    public Label getStatusLabel2() {
        return statusLabel2;
    }

    public void zeichneSchiffe(Schiff schiff) {
        if (dieKISpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieKISpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        } else if (dieOnlineSpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieOnlineSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        } else if (dieLokalesSpielSteuerung != null) {
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    //System.out.println(s);
                    Image img = new Image(s);
                    dieLokalesSpielSteuerung.getGridSpielfeldLinks().getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
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

    public Label getOutputField() {
        return outputField;
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

    public void wartenAufVerbindung(boolean bool){
        infoTextVerbindung.setVisible(bool);
    }
    

    public void erstelleSteuerung() {
        if (modus == 22) {
            System.out.println("Erstelle Steuerung");
            dieKISpielSteuerung.erzeugeKI(kiStufe);
            dieKISpielSteuerung.erzeugeEigeneSchiffe();

            if (dieKISpielSteuerung.isFertigSetzen()) {
                dieKISpielSteuerung.setSchiffeSetzen();
                //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieKISpielSteuerung.setGridSpielfeldSpielRechts(dieKISpielSteuerung.getKi().getGridSpielfeldRechts()); //hier wird gezeichnet :)
                        dieKISpielSteuerung.setGridSpielfeldSpielLinks(dieKISpielSteuerung.getKi().getGridSpielfeldLinks());
                        dieKISpielSteuerung.getGridSpielfeldRechts().print();
                        dieKISpielSteuerung.getGridSpielfeldLinks().print();
                        dieKISpielSteuerung.setzeSchiffeKI(); //hier auch
                        fertig = true;
                        System.out.println("In ersteööe Steuetung true");
                        spielbereit = true;
                        dieKISpielSteuerung.beginneSpiel();
                    }
                });
            }

        } else if (modus == 32) {
            System.out.println(dieOnlineSpielSteuerung.getClient().isVerbindung());
            if (dieOnlineSpielSteuerung.getClient().isVerbindung()) {
                dieOnlineSpielSteuerung.erzeugeSteuerungSchiffeSetzen();

                //Da uns das Threading um die ohren gefolgen ist folgendes:
                Platform.runLater(new Runnable() {  //ka was das macht
                    @Override
                    public void run() { //oder das...
                        dieOnlineSpielSteuerung.erzeugeEigeneSchiffe();
                        fertig = true;
                    }
                });
            }
        }
    }

    @FXML
    private void handleButton(ActionEvent event) {

        if ((dieLokalesSpielSteuerung instanceof LokalesSpielSteuerung && dieLokalesSpielSteuerung.isFertigSetzen())) {
            dieLokalesSpielSteuerung.erzeugeGegnerSchiffe();
            if (dieLokalesSpielSteuerung.gegnerKiIsFertig()) {
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setBorder(Border.EMPTY);
                spielstart.setVisible(false);
                dieLokalesSpielSteuerung.setSchiffeSetzen();

                dieLokalesSpielSteuerung.setGridSpielfeldSpielRechts(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieLokalesSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieLokalesSpielSteuerung.setGridSpielfeldSpielLinks(dieLokalesSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieLokalesSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                dieLokalesSpielSteuerung.getGridSpielfeldLinks().print();
                dieLokalesSpielSteuerung.beginneSpiel();
                saveButton.setVisible(true);
                getBtnBeenden().setVisible(true);
                getBtnMenue().setVisible(true);
                infoEins.setText("Feld rechts anklicken");
                infoZwei.setText("Blau ist Wasser");
                infoDrei.setText("Rotes Kreuz ist versenkt");
            }
        } else if (dieOnlineSpielSteuerung instanceof OnlineSpielSteuerung && dieOnlineSpielSteuerung.isFertigSetzen()) {
            getBtnBeenden().setVisible(true);
            getBtnMenue().setVisible(true);
            spielstart.setVisible(false);
            saveButton.setVisible(true);
            infoEins.setText("Feld rechts anklicken");
            infoZwei.setText("Blau ist Wasser");
            infoDrei.setText("Rotes Kreuz ist versenkt");
            if (modus == 31 && dieOnlineSpielSteuerung.getServer().isVerbindung()) {
                spielbereit = true;
                if(dieOnlineSpielSteuerung.getServer().isReadyNochSenden()){
                    System.out.println("Nachricht senden: " + "ready");
                    dieOnlineSpielSteuerung.getServer().send("ready");
                }
                
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                dieOnlineSpielSteuerung.setSchiffeSetzen();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieOnlineSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                //dieOnlineSpielSteuerung.getGridSpielfeldRechts().print();
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print();
                dieOnlineSpielSteuerung.beginneSpiel();
                getStatusLabel1().setVisible(true);
                getStatusLabel2().setVisible(false);
            } else if (modus == 32 && dieOnlineSpielSteuerung.getClient().isVerbindung()) {
                spielbereit = true;
                if(dieOnlineSpielSteuerung.getClient().isReadyNochSenden()){
                    System.out.println("Nachricht senden: " + "ready");
                    dieOnlineSpielSteuerung.getClient().send("ready");
                }
                
                paneGrid.getChildren().clear();
                setzenControll.getChildren().clear();
                setzenControll.setStyle("-fx-border-width: 0");
                dieOnlineSpielSteuerung.setSchiffeSetzen();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielRechts(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldRechts());
                dieOnlineSpielSteuerung.enableMouseClickSoielfeldGridRechts();
                dieOnlineSpielSteuerung.setGridSpielfeldSpielLinks(dieOnlineSpielSteuerung.getDieSteuerungSchiffeSetzen().getGridSpielfeldLinks());
                dieOnlineSpielSteuerung.setzeSchiffe();
                System.out.println("Eigenes Feld");
                //dieOnlineSpielSteuerung.getGridSpielfeldRechts().print();
                dieOnlineSpielSteuerung.getGridSpielfeldLinks().print();
                dieOnlineSpielSteuerung.beginneSpiel();
                getStatusLabel1().setVisible(false);
                getStatusLabel2().setVisible(true);

            }
        }
    }

    public boolean isSpielbereit() {
        return spielbereit;
    }

    /**
     * Steuert das Verhalten bei click auf den Button Schiffe Neu Plazieren
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
     * Steuert das Verhalten bei click auf den Button Schiffe Zufällig plazieren
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

    @FXML
    private void handleButtonWarten(ActionEvent event) {
        if (modus == 32) {
            dieOnlineSpielSteuerung.werdeClient();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dieOnlineSpielSteuerung.getClient().isVerbindung()) {
                clientWartet.setVisible(false);
                spielstart.setVisible(true);
                setzenControll.setVisible(true);
            }
        } else if (modus == 22) {
            dieKISpielSteuerung.werdeClient();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dieKISpielSteuerung.getClient().isVerbindung()) {
                clientWartet.setVisible(false);
                spielstart.setVisible(false);
                setzenControll.setVisible(true);
            }
        }
    }

    public void spielEnde(int gewinner) { // 1 Spieler, 2 Gegner
        paneGrid.setDisable(true);
        saveButton.setVisible(false);
        infoPane.getChildren().clear();
        Label l = new Label();
        infoPane.getChildren().add(l);
        l.setLayoutX(300);
        l.setLayoutY(60);
        btnMenue.setVisible(true);
        btnBeenden.setVisible(true);
        if (gewinner == 2) {
            System.out.println("Gewonnen");
            l.setStyle(" -fx-font-size: 55; -fx-font-weight: 700 ");
            l.setText("Glückwunsch du hast Gewonnen");
        } else {
            System.out.println("Verloren");
            l.setStyle("-fx-font-size: 55; -fx-font-weight: 700 ");
            l.setText("Schade du hast Verloren");
        }
    }

    @FXML
    private void speicherSpiel(ActionEvent event) {
        if (dieLokalesSpielSteuerung != null) {
            if (saveLoad.speicherSpiel(this, dieLokalesSpielSteuerung)) {
                btnBeenden.setVisible(true);
                btnMenue.setVisible(true);
            }
        } else if (dieOnlineSpielSteuerung != null) {
            if (saveLoad.speicherSpiel(this, dieOnlineSpielSteuerung)) {
                btnBeenden.setVisible(true);
                btnMenue.setVisible(true);
                System.out.println("Nachricht senden: " + "save " + saveLoad.getL()[0]);
                dieOnlineSpielSteuerung.getServer().send("save " + saveLoad.getL()[0]);
            }
        }

    }

    public int[][] makeInt(Rectangle[][] g) {
        int[][] save = new int[g.length][g.length];
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g.length; j++) {
                save[i][j] = Integer.valueOf(g[i][j].getId());
            }
        }
        return save;
    }

    private boolean offen = false;

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

    private void changeMusikHandler(MouseEvent e) {
        var.lautstaerke = slider.getValue() / 100;
        mp.setLautstaerkeGame(slider.getValue() / 100);
    }

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
        }
        SchiffeVersenken.getApplicationInstance().restart();
        mp.setMusikMenue();
        //SchiffeVersenken.getApplicationInstance().setScene("/GUI/Hauptmenue.fxml");
    }

    @FXML
    private void handleButtonBeenden(ActionEvent event) {
        System.exit(0);
    }

    public static void print(int[][] arr) {
        System.out.println("");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public static void printGrid(int[][] arr) {
        System.out.println("");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[j][i] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
}
