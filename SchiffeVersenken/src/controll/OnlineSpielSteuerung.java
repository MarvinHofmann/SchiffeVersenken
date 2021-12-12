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
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class OnlineSpielSteuerung extends SpielSteuerung{
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    private Server server;
    private Client client;
    
    public OnlineSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(gui, anzahlSchiffeTyp, spielfeldgroesse);
    }
    
    public OnlineSpielSteuerung(SpielGUIController gui){
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
    }
    
    public void uebergebeRest(int spielfeldgroesse, int[] anzahlSchiffeTyp){
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(dieGui, anzahlSchiffeTyp, spielfeldgroesse);
    }
    
    public void erzeugeSteuerungSchiffeSetzen(){
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(dieGui, anzahlSchiffeTyp, spielfeldgroesse);
    }
    
    public void werdeServer(){
        new Thread (() -> {
            server = new Server(dieGui);
            server.start();
        }).start();
    }
    
    public void werdeClient(){
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
    
    public SteuerungSchiffeSetzen getDieSteuerungSchiffeSetzen() {
        return dieSteuerungSchiffeSetzen;
    }
    
    /**
     * Setze alle Schiffe Zurück
     */
    public void clearSchiffeSetzen() {
        for (Schiff schiff : dieSteuerungSchiffeSetzen.getSchiffArray()) { //Für jedes Schiff
            dieSteuerungSchiffeSetzen.clearId(schiff); //Lösche Marker ID auf Grid
            schiff.setFill(Color.RED); //Setzte die Farbe rot
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.drehen(schiff.getIndex()); //Drehen vertikale Schiff für exaxte Startposition
            }
        }
        dieSteuerungSchiffeSetzen.zeichneSchiffe(true); 
        dieSteuerungSchiffeSetzen.getGridS().print(); //DEBUG
    }    

    public void randomSetzen() {
        clearSchiffeSetzen();
        dieSteuerungSchiffeSetzen.setzeRandomSchiffe();
        for(Schiff schiff: dieSteuerungSchiffeSetzen.getSchiffArray()){
            if(schiff.getRichtung() == Richtung.VERTIKAL  ){
                schiff.dreheGui();
            }    
            schiff.setFill(Color.GREEN); //Setzte die Farbe grün
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridS().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridS().getKachelgroeße());
        }
    }

    @Override
    public void beginneSpiel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
