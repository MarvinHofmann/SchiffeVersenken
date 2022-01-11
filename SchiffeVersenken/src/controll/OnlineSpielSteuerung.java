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

    private int grafikTrigger; //treffermarkierungen setzen client, Server 1 = wasser, 2 = treffer, 3 = versenkt
    private int eigeneSchiffeGetroffen;
    private SchiffeSetzen dieSteuerungSchiffeSetzen = null;
    private Server server;
    private Client client;
    private int[][] getroffen;
    private int aktiverSpieler = 0; // 0-> Spieler, 1-> Gegner
    boolean readystate;

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
    }

    public OnlineSpielSteuerung(SpielGUIController gui) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
    }

    public void erzeugeSteuerungSchiffeSetzen() {
        dieSteuerungSchiffeSetzen = new SchiffeSetzen(dieGui, anzahlSchiffeTyp, spielfeldgroesse);
    }

    public void werdeServer() {
        new Thread(() -> {
            server = new Server(dieGui);
            server.start();
        }).start();
    }

    public void werdeClient() {
        new Thread(() -> {
            client = new Client(dieGui);
            client.start();
        }).start();
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

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
    public int getGrafikTrigger(){
        return grafikTrigger;
    }

    public int getEigeneSchiffeGetroffen() {
        return eigeneSchiffeGetroffen;
    }

    public void setEigeneSchiffeGetroffen(int eigeneSchiffeGetroffen) {
        this.eigeneSchiffeGetroffen += eigeneSchiffeGetroffen;
    }
    
    public void setAnzahlSchiffe(){
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
    }
    
    public void setGrafiktrigger( int wert){
        this.grafikTrigger = wert;
    }

    public void setAktiverSpieler(int aktiverSpieler) {
        this.aktiverSpieler = aktiverSpieler;
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
    }
    
    private void clicked(MouseEvent event, Rectangle rectangle) {
        this.grafikTrigger = 0;
        if (aktiverSpieler == 0) {
            int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
            int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
            //int[] gegnerSchuss = {-1, -1};
            String message = "shot " + zeile + " " + spalte;
            if (server != null) {
                server.setSpeicher(zeile, spalte);
                server.send(message);
            } else if (client != null) {
                client.send(message);
                client.setSpeicher(zeile, spalte);
            }
        }
    }

    @Override
    public int ueberpruefeSpielEnde() {
        // Ende
        System.out.println(anzSchiffe + ", " + anzGetroffen + ", " + eigeneSchiffeGetroffen);
        if(anzSchiffe == anzGetroffen){ //schiffe beim Gegner versenkt
            return 2; //spieler gewinnt
        }
        else if(anzSchiffe == eigeneSchiffeGetroffen){
            return 1; //gegner hat gewonnen
        }
        return 0;
    }
    
    public void verarbeiteGrafiken(int wert, int zeile, int spalte, int feld){
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
                    //wasserUmSchiffRechts(zeile, spalte);
                    break;
            }
        }
        else if(feld ==1){
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
        int ende = 0;
        ende = ueberpruefeSpielEnde();
        if(ende!= 0){
            dieGui.spielEnde(ende);
    }
    
    
    }
    
}
