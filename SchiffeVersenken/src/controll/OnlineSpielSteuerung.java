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
import java.util.HashSet;
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
        macheEigeneSchiffe();
        gridSpielfeldLinks.DrawGetroffen(getroffenGegAr);
        setGridSpielfeldSpielLinks(gridSpielfeldLinks);
        setGridSpielfeldSpielRechts(gridSpielfeldRechts);
        gridSpielfeldRechts.enableMouseClick();
        beginneSpielLaden(); //wenn verbindung da
    }

    public OnlineSpielSteuerung(SpielGUIController gui) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
    }

    public void erzeugeSteuerungSchiffeSetzen() {
        dieSteuerungSchiffeSetzen = new SchiffeSetzen(dieGui, anzahlSchiffeTyp, spielfeldgroesse);
    }

    public void werdeServer(boolean laden) {
        serverT = new Thread(() -> {
            server = new Server(dieGui);
            server.start(laden);
        });
        serverT.setDaemon(true);
        serverT.start();
    }

    public void werdeClient() {
        clienT = new Thread(() -> {
            client = new Client(dieGui);
            client.start();
        });
        clienT.setDaemon(true);
        clienT.start();
    }

    @Override
    public void erzeugeEigeneSchiffe() {
        dieSteuerungSchiffeSetzen.drawAll();
    }

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
    
    public void setAnzahlSchiffe(){
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

    /**
     * Setze alle Schiffe Zurück
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

    public void makeHandler(Rectangle r) {
        r.setOnMouseClicked(event -> clicked(event, r));
    }

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
        if (client != null && client.isReadyNochSenden()) {
            System.out.println("Nachricht senden: " + "ready");
            client.send("ready");
        }
        else if(server != null && server.isReadyNochSenden()){
            System.out.println("Nachricht senden: " + "ready");
            server.send("ready");
        }
    }
    
    private void clicked(MouseEvent event, Rectangle rectangle) {
        this.grafikTrigger = 0;
        if (aktiverSpieler == 0) {
            if((server != null && server.isClientReady()) || (client != null && client.isServerReady())){
                int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
                int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
                //int[] gegnerSchuss = {-1, -1};
                if(getroffen[zeile][spalte] == 0){    
                    String message = "shot " + (zeile+1) + " " + (spalte+1);
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

    @Override
    public int ueberpruefeSpielEnde() {
        // Ende
        System.out.println(anzSchiffe + ", " + anzGetroffen + ", " + eigeneSchiffeGetroffen);
        if(anzSchiffe == anzGetroffen){ //schiffe beim Gegner versenkt
            spielEnde = false;
            return 2; //spieler gewinnt
        }
        else if(anzSchiffe == eigeneSchiffeGetroffen){
            spielEnde = false;
            return 1; //gegner hat gewonnen
        }
        return 0;
    }
    
    public void verarbeiteGrafiken(int wert, int zeile, int spalte, int feld){ // wert: 1 wasser 2 getroffen 3 versenkt        
        Image img = new Image("/Images/nop.png");
        if(feld == 0){
            switch(wert){
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
        }
        else if(feld ==1){
            spalte = spalte -1;
            zeile = zeile -1;
            switch(wert){
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
        if(ende!= 0){
            if(client!=null){
                clienT.interrupt();
            }
            else if(server != null){
                serverT.interrupt();
                
            }
            Platform.runLater(() -> dieGui.spielEnde(ende));
        }
    }

  
}
