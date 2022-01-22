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
    
    public KISpielSteuerung(SpielGUIController gui){
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
    }
    
    public void erzeugeKI(int kistufe){
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp, kistufe);
    }
    
    public void werdeServer(boolean laden){
        serverT = new Thread (() -> {
            server = new Server(dieGui);
            server.start(laden);
        });
        serverT.setDaemon(true);
        serverT.start();
    }
    
    public void werdeClient(){
        clientT = new Thread(() -> {
            client = new Client(dieGui);
            client.start();
        });
        clientT.setDaemon(true);
        clientT.start();
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
    public void erzeugeEigeneSchiffe() {
        ki.erzeugeEigeneSchiffe();
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
    
    @Override
    public void beginneSpiel() {
        System.out.println("Beginne KISpiel- KI1 startet");
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);    
        if(server != null){
            //dieGui.zeigeStatusLabel(1, false);
            //dieGui.zeigeStatusLabel(2, false);
        }
    }
    
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
}
