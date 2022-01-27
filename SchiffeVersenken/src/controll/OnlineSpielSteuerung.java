/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.paint.Color;
import Server.Client;
import Server.Server;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class OnlineSpielSteuerung extends SpielSteuerung {

    private SchiffeSetzen dieSteuerungSchiffeSetzen = null;
    Thread serverT;
    Thread clienT;
    long id;

    /**
     * Konstruktor zum Erstellen eines neuen Spiels.
     * 
     * @param gui Bidirektionale Beziehung zwischen Gui und Steuerung
     * @param spielfeldgroesse Spielfeldgröße zwischen 5 und 30
     * @param anzahlSchiffeTyp  Anzahl der Schiffe je Typ
     */
    public OnlineSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        dieSteuerungSchiffeSetzen = new SchiffeSetzen(gui, anzahlSchiffeTyp, spielfeldgroesse);
        eigeneSchiffeGetroffen = 0;
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
    }

    /**
     * Konstruktor zum Erstellen eines geladenen Spiels
     * 
     * @param gui Bidirektionale Beziehung zwischen Gui und Steuerung
     * @param styp Anzahl der Schiffe je Typ
     * @param paramInc 0: spielfeldgroesse, 1: Modus, 2: AnzGetroffen, 3: EigeneSchiffeGetroffen
     * @param gridRechtsArr Zweidemensionales Array mit Schiff-IDs des rechten Grids
     * @param gridLinksArr Zweidemensionales Array mit Schiff-IDs des linken Grids
     * @param getroffenAr Zweidemensionales Array mit Informationen wo der Spieler schonmal hingeschossen hat und was dort versteckt ist, inklusiv Felder die definitiv Wasser sind 
     * @param getroffenGegAr Zweidemensionales Array mit Informatioen wo der Gegner schonmal hingeschossen hat und was dort gefunden wurde
     * @param onlineValues 0: EigeneSchiffe Getroffen, 1: Aktiver Spieler
     * @param l long ID für die gespeicherte ID
     */
    public OnlineSpielSteuerung(SpielGUIController gui, int[] styp, int[] paramInc, int[][] gridRechtsArr, int[][] gridLinksArr, int[][] getroffenAr, int[][] getroffenGegAr, int[] onlineValues, long[] l) {
        super(gui);
        System.out.println("OnlineSpielSteuerung erzeugt bei Spiel laden");
        this.anzahlSchiffeTyp = styp;
        this.spielfeldgroesse = paramInc[0];
        this.anzGetroffen = paramInc[2];
        this.eigeneSchiffeGetroffen = paramInc[3];
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        this.getroffen = getroffenAr;
        this.getroffenGegner = getroffenGegAr;
        this.id = l[0];
        this.eigeneSchiffeGetroffen = onlineValues[0];
        this.aktiverSpieler = 0;
        gridSpielfeldRechts = makeGrid(gridRechtsArr, 1);
        gridSpielfeldLinks = makeGrid(gridLinksArr, 0);
        gridSpielfeldRechts.Draw(getroffenAr);
        gridSpielfeldLinks.Draw(getroffenGegAr);
        macheEigeneSchiffe();
        gridSpielfeldLinks.DrawGetroffen(getroffenGegAr);
        setGridSpielfeldSpielLinks(gridSpielfeldLinks);
        setGridSpielfeldSpielRechts(gridSpielfeldRechts);
        gridSpielfeldRechts.enableMouseClick();
    }
    
    /**
     * Konstruktor für den Client welcher die Spielfeldgröße und die 
     * Schiffstypen zur Erstellung der Steuerung noch nicht kennt.
     * 
     * @param gui Bidirektionale Beziehung zwischen Gui und Steuerung
     */
    public OnlineSpielSteuerung(SpielGUIController gui) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
    }

    /**
     * Methode für den Client wenn geladen wird.
     * 
     * @param ip IP-Adresse falls vorhanden
     * @param l long ID für die gespeicherte ID
     * @param paramInc 0: spielfeldgroesse, 1: Modus, 2: AnzGetroffen, 3: EigeneSchiffeGetroffen
     * @param styp Anzahl der Schiffe je Typ
     * @param getroffenAr Zweidemensionales Array mit Informationen wo der Spieler schonmal hingeschossen hat und was dort versteckt ist, inklusiv Felder die definitiv Wasser sind 
     * @param getroffenGegAr Zweidemensionales Array mit Informatioen wo der Gegner schonmal hingeschossen hat und was dort gefunden wurde
     * @param gridRechtsArr Zweidemensionales Array mit Schiffs IDs des rechten Grids
     * @param gridLinksArr Zweidemensionales Array mit Schiffs IDs des linken Grids
     * @param onlineValues 0: EigeneSchiffe Getroffen, 1: Aktiver Spieler 
     */
    public void ladeClient(String[] ip, long[] l, int[] paramInc, int[] styp, int[][] getroffenAr, int[][] getroffenGegAr, int[][] gridRechtsArr, int[][] gridLinksArr, int[] onlineValues) {
        System.out.println("OnlineSpielSteuerung erzeugt bei Spiel laden");
        this.anzahlSchiffeTyp = styp;
        this.spielfeldgroesse = paramInc[0];
        this.anzGetroffen = paramInc[2];
        this.eigeneSchiffeGetroffen = paramInc[3];
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        this.getroffen = getroffenAr;
        this.getroffenGegner = getroffenGegAr;
        this.eigeneSchiffeGetroffen = onlineValues[0];
        this.aktiverSpieler = 1;
        gridSpielfeldRechts = makeGrid(gridRechtsArr, 1);
        gridSpielfeldLinks = makeGrid(gridLinksArr, 0);
        gridSpielfeldRechts.Draw(getroffenAr);
        gridSpielfeldLinks.Draw(getroffenGegAr);
        Platform.runLater(new Runnable() {  //ka was das macht
            @Override
            public void run() { //oder das...
                macheEigeneSchiffe();
                gridSpielfeldLinks.DrawGetroffen(getroffenGegAr);
                setGridSpielfeldSpielLinks(gridSpielfeldLinks);
                setGridSpielfeldSpielRechts(gridSpielfeldRechts);
                gridSpielfeldRechts.enableMouseClick();
            }
        });
        beginneSpielLaden(); //wenn verbindung da
    }

    /**
     * Erzeuge eigene Schiffe um diese dann setzen zu können als Mensch
     */
    public void erzeugeSteuerungSchiffeSetzen() {
        dieSteuerungSchiffeSetzen = new SchiffeSetzen(dieGui, anzahlSchiffeTyp, spielfeldgroesse);
    }

    /**
     * Werde Server und übergebe Boolean ob geladen wurde oder nicht 
     * @param laden true: Es wurde geladen, False: Es wurde neu gestartet
     */
    public void werdeServer(boolean laden) {
        serverT = new Thread(() -> {
            server = new Server(dieGui);
            server.start(laden);
        });
        serverT.setDaemon(true);
        serverT.start();
    }

    /**
     * Werde Client. 
     */
    public void werdeClient() {
        clienT = new Thread(() -> {
            client = new Client(dieGui);
            client.start();
        });
        clienT.setDaemon(true);
        clienT.start();
    }

    /**
     * Hier werden die eigenen Schiffe auf die Gui gesetzt.
     */
    @Override
    public void erzeugeEigeneSchiffe() {
        dieSteuerungSchiffeSetzen.drawAll();
    }

    /**
     * Setze alle Schiffe zurück
     */
    public void clearSchiffeSetzen() {
        Schiff schiff;
        for (int i = 0; i < dieSteuerungSchiffeSetzen.getSchiffArray().length; i++) {
            schiff = dieSteuerungSchiffeSetzen.getSchiffArray()[i];
            schiff.setStroke(Color.RED);
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.setRichtung(Richtung.HORIZONTAL);
                schiff.dreheGui();
            }
        }
        dieSteuerungSchiffeSetzen.zeichneSchiffe(true);
        dieSteuerungSchiffeSetzen.clearAll();
        dieSteuerungSchiffeSetzen.setFertig(false);
    }

    /**
     * Methode zum zufälligen Setzen der Schiffe auf der Gui.
     */
    public void randomSetzen() {
        clearSchiffeSetzen();
        dieSteuerungSchiffeSetzen.setzeRandomSchiffe();
        for (Schiff schiff : dieSteuerungSchiffeSetzen.getSchiffArray()) {
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.dreheGui();
            }
            schiff.setStroke(Color.GREEN);
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße());
        }
    }

    /**
     * Make Handler ermöglicht mit der Maus auf das Rectangle zu klicken.
     * 
     * @param r Rectangle für welches der Handler erstellt wird
     */
    public void makeHandler(Rectangle r) {
        r.setOnMouseClicked(event -> clicked(event, r));
    }

    /**
     * Beginne das Spiel in dem die Handler für die jeweilgen Kacheln erzeugt werden, um dem Spieler
     * zu ermöglichen auf die Kacheln zu clicken. Außerdem wird das Statuslabel gezeigt welches
     * angibt, dass der Spieler beginnen kann. 
     */
    @Override
    public void beginneSpiel() {
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                makeHandler(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
        getroffenGegner = new int[spielfeldgroesse][spielfeldgroesse];
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
    }

    /**
     * Beginne das Spiel nach dem Laden in dem die Handler für die jeweilgen Kacheln erzeugt werden, um dem Spieler
     * zu ermöglichen auf die Kacheln zu klicken. Außerdem wird das Statuslabel gezeigt welches
     * angibt, dass der Spieler beginnen kann, und wenn noch nicht geschehen, dem Gegner ready gesendet werden kann.
     */
    public void beginneSpielLaden() {
        System.out.println("");
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                makeHandler(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
        dieGui.setSpielbereit(true);
        dieGui.getBtnMenue().setVisible(true);
        dieGui.getBtnBeenden().setVisible(true);
        if (client != null && client.isReadyNochSenden()) {
            System.out.println("Nachricht senden: " + "ready");
            client.send("ready");
        } else if (server != null && server.isReadyNochSenden()) {
            System.out.println("Nachricht senden: " + "ready");
            server.send("ready");
        }
    }

    /**
     * JavaFX Methode für das Event bei welchem der Spieler auf eine Kachel im Grid klickt.
     * Ist er berechtigt zu schießen, dann wird sein Schuss ausgeführt und dem Gegner per
     * Netzwerkprotokol shot gesendet. Danach wird auf die Antwort answer des Gegners gewartet.
     * Hat einer von beiden einen Treffer, darf dieser so
     * lange weiter schießen bis es ins Wasser trifft.
     * 
     * @param event
     * @param rectangle Auf welches geklickt wird
     */
    private void clicked(MouseEvent event, Rectangle rectangle) {
        this.grafikTrigger = 0;
        if (aktiverSpieler == 0) {
            if ((server != null && server.isClientReady()) || (client != null && client.isServerReady())) {
                int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
                int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
                //int[] gegnerSchuss = {-1, -1};
                if (getroffen[zeile][spalte] == 0) {
                    String message = "shot " + (zeile + 1) + " " + (spalte + 1);
                    if (server != null) {
                        System.out.println("Nachricht senden: " + message);
                        server.setSpeicher(zeile, spalte);
                        server.send(message);
                    } else if (client != null) {
                        System.out.println("Nachricht senden: " + message);
                        client.send(message);
                        client.setSpeicher(zeile, spalte);
                    }
                }
            }

        }
    }

    /**
     * Überpruft ob es ein Spielende im Online Spiel gibt.
     * Wenn ja liefert die Methode zurück wer gewonnen hat.
     * 
     * @return 0: Noch nicht zu Ende, 1: Gegner hat gewonnen, 2: Eigene KI hat gewonnen 
     */
    @Override
    public int ueberpruefeSpielEnde() {
        // Ende
        //System.out.println(anzSchiffe + ", " + anzGetroffen + ", " + eigeneSchiffeGetroffen);
        if (anzSchiffe == anzGetroffen) { //schiffe beim Gegner versenkt
            spielEnde = false;
            return 2; //spieler gewinnt
        } else if (anzSchiffe == eigeneSchiffeGetroffen) {
            spielEnde = false;
            return 1; //gegner hat gewonnen
        }
        return 0;
    }

    /**
     * Methode zum Verarbeiten von Grafiken. Anzeigen von Wasser oder passendem Schiff
     * 
     * @param wert 1: Wasser, 2: Schiffseil getroffen, 3: Schiff versenkt
     * @param zeile Zeile des Schuss welcher verarbeitet wird
     * @param spalte Spalte des Schuss welcher verarbeitet wird
     * @param feld 0: rechtes Feld, 1: linkes Feld 
     */
    public void verarbeiteGrafiken(int wert, int zeile, int spalte, int feld) { // wert: 1 wasser 2 getroffen 3 versenkt        
        Image img = new Image("/Images/nop.png");
        if (feld == 0) {
            switch (wert) {
                case 1:
                    gridSpielfeldRechts.getGrid()[spalte][zeile].setFill(Color.TRANSPARENT);
                    getroffen[zeile][spalte] = 1;
                    break;
                case 2:
                    gridSpielfeldRechts.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    getroffen[zeile][spalte] = 2;
                    break;
                case 3:
                    gridSpielfeldRechts.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    getroffen[zeile][spalte] = 2;
                    anzGetroffen++;
                    System.out.println("Wasser hinzufügen: + " + zeile + " " + spalte);
                    wasserUmSchiffRechts(zeile, spalte);
                    break;
            }
        } else if (feld == 1) {
            spalte = spalte - 1;
            zeile = zeile - 1;
            switch (wert) {
                case 1:
                    gridSpielfeldLinks.getGrid()[spalte][zeile].setFill(Color.TRANSPARENT);
                    getroffenGegner[zeile][spalte] = 1;
                    break;
                case 2:
                    gridSpielfeldLinks.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    getroffenGegner[zeile][spalte] = 2;
                    break;
                case 3:
                    gridSpielfeldLinks.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    getroffenGegner[zeile][spalte] = 2;
                    eigeneSchiffeGetroffen++;
                    break;
            }
        }
        final int ende = ueberpruefeSpielEnde();
        if (ende != 0) {
            if (client != null) {
                clienT.interrupt();
            } else if (server != null) {
                serverT.interrupt();

            }
            Platform.runLater(() -> dieGui.spielEnde(ende));
        }
    }

    /**
     * Getter und Setter Methoden
     */
    
    @Override
    public void setSchiffeSetzen() {
        this.schiffe = dieSteuerungSchiffeSetzen.getSchiffArray();
    }

    @Override
    public boolean isFertigSetzen() {
        return dieSteuerungSchiffeSetzen.isFertig();
    }

    public int[] getAnzahlSchiffeTyp() {
        return anzahlSchiffeTyp;
    }

    public long getId() {
        return id;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public void setAnzahlSchiffe() {
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
    }

    public Thread getServerT() {
        return serverT;
    }

    public Thread getClienT() {
        return clienT;
    }

    public int getEigeneSchiffeGetroffen() {
        return eigeneSchiffeGetroffen;
    }

    public SchiffeSetzen getDieSteuerungSchiffeSetzen() {
        return dieSteuerungSchiffeSetzen;
    }
}
