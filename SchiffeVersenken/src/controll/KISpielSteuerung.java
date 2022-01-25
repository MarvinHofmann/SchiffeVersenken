/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import Server.Client;
import Server.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class KISpielSteuerung extends SpielSteuerung{
    private KI ki = null;
    private int kiStufe;
    
    /**
     * Konstruktor zum Erstellen eines neuen Spiels
     * 
     * @param gui Bidirektionale Beziehung zwischen Gui und Steuerung
     * @param spielfeldgroesse Spielfeldgröße zwischen 5 und 30
     * @param anzahlSchiffeTyp Anzahl der Schiffe je Typ
     * @param kiStufe KiStufe welche im Modi Menü ausgewählt wurde
     */
    public KISpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp, int kiStufe) {
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        this.kiStufe = kiStufe;
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp, kiStufe);
        eigeneSchiffeGetroffen = 0;
    }
    
    /**
     * Konstruktor für den Client welcher die Spielfeldgröße und die 
     * Schiffstypen zur Erstellung der Steuerung noch nicht kennt.
     * 
     * @param gui Bidirektionale Beziehung zwischen Gui und Steuerung
     */
    public KISpielSteuerung(SpielGUIController gui){
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
    }
    
    /**
     * Hier wird eine KI erzeugt.
     * 
     * @param kistufe KiStufe welche im Modi Menü ausgewählt wurde
     */
    public void erzeugeKI(int kistufe){
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp, kistufe);
    }
    
    /**
     * Werde Server und übergebe Boolean ob geladen wurde oder nicht 
     * @param laden true: Es wurde geladen, False: Es wurde neu gestartet
     */
    public void werdeServer(boolean laden){
        serverT = new Thread (() -> {
            server = new Server(dieGui);
            server.start(laden);
        });
        serverT.setDaemon(true);
        serverT.start();
    }
    
    /**
     * Werde Client. 
     */
    public void werdeClient(){
        clientT = new Thread(() -> {
            client = new Client(dieGui);
            client.start();
        });
        clientT.setDaemon(true);
        clientT.start();
    }

    /**
     * Hier werden die eigenen Schiffe erzeugt.
     */
    @Override
    public void erzeugeEigeneSchiffe() {
        ki.erzeugeEigeneSchiffe();
    }

    /**
     * Beginne das Spiel. Zeige unten die gesamte Anzahl an verschieden
     * Schiffe an.
     * 
     */
    @Override
    public void beginneSpiel() {
        System.out.println("Beginne KISpiel- KI1 startet");
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
        dieGui.getBtnBeenden().setVisible(true);
    }
    
    /**
     * Überpruft ob es ein Spielende im KI Spiel gibt.
     * Wenn ja liefert die Methode zurück wer gewonnen hat.
     * 
     * @return 0: Noch nicht zu Ende, 1: Gegner hat gewonnen, 2: Eigene KI hat gewonnen 
     */
    @Override
    public int ueberpruefeSpielEnde() {
        // Ende
        System.out.println(anzSchiffe + ", " + anzGetroffen + ", " + eigeneSchiffeGetroffen);
        if(anzSchiffe == anzGetroffen){ //schiffe beim Gegner versenkt
            spielEnde = true;
            return 2; //spieler gewinnt
        }
        else if(anzSchiffe == eigeneSchiffeGetroffen){
            spielEnde = true;
            return 1; //gegner hat gewonnen
        }
        return 0;
    }
    
    /**
     * Diese Methode lässt die KI mit passendem Delay schießen.
     * 
     * @param antwortDavor 
     */
    public void schiesseAufGegner(int antwortDavor){
        int[] schuss = ki.schiesse(antwortDavor);
        String message = "shot " + (schuss[0]+1) + " " + (schuss[1]+1);
        System.out.println("Nachricht senden: " + message);
        if (server != null) {
            server.setSpeicher(schuss[0], schuss[1]);
            server.send(message);
            try {
                serverT.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(KISpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (client != null) {
            client.send(message);
            client.setSpeicher(schuss[0], schuss[1]);
            try {
                clientT.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(KISpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println("Hier");
    }
    
    /**
     * Methode zum Verarbeiten von Grafiken. Anzeigen von Wasser oder passendem Schiff
     * 
     * @param wert 1: Wasser, 2: Schiffseil getroffen, 3: Schiff versenkt
     * @param zeile Zeile des Schuss welcher verarbeitet wird
     * @param spalte Spalte des Schuss welcher verarbeitet wird
     * @param feld 0: Rechtes Feld, 1: Linkes Feld 
     */
    public void verarbeiteGrafiken(int wert, int zeile, int spalte, int feld){ // wert: 1 wasser 2 getroffen 3 versenkt
        //System.out.println("------------------------------------------------------------------------------------------------");        
        Image img = new Image("/Images/nop.png");
        if(feld == 0){
            switch(wert){
                case 1:
                    gridSpielfeldRechts.getGrid()[spalte][zeile].setFill(Color.TRANSPARENT);
                    getroffen[zeile][spalte] = 1;
                    break;
                case 2:
                    //System.out.println("----------------------------------Spalte: " + spalte + " Zeile: " + zeile);
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
                    break;
                case 2:
                    gridSpielfeldLinks.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    break;
                case 3:
                    gridSpielfeldLinks.getGrid()[spalte][zeile].setFill(new ImagePattern(img));
                    eigeneSchiffeGetroffen++;
                    break;
            }
        }
        
        final int ende = ueberpruefeSpielEnde();
        if(ende!= 0){
            if(client!=null){
                clientT.interrupt();
            }
            else if(server != null){
                serverT.interrupt();
            }
            Platform.runLater(() -> dieGui.spielEnde(ende));
        }
    }
    
    /**
     * Getter und Setter Methoden
     */
    
    public Thread getClientT() {
        return clientT;
    }

    public Thread getServerT() {
        return serverT;
    }
    
    public KI getKi() {
        return ki;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
    public int getKiStufe(){
        return kiStufe;
    }
    
    @Override
    public void setSchiffeSetzen() {
        this.schiffe = ki.getSchiffArray();
    }

    @Override
    public boolean isFertigSetzen() {
        return ki.isFertig();
    }
    
    public void setAnzahlSchiffe(){
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
    }
}
