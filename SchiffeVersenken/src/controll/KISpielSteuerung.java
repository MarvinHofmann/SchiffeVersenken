/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import Server.Client;
import Server.Server;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class KISpielSteuerung extends SpielSteuerung{
    private KI ki = null;
    private Server server;
    private Client client;
    private int aktiveKi;
    
    public KISpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp, int kiStufe) {
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp, kiStufe);
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
    }
    
    public KISpielSteuerung(SpielGUIController gui){
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
    }
    
    public void erzeugeKI(int kistufe){
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp, kistufe);
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
    
    public KI getKi() {
        return ki;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }
    
    @Override
    public void erzeugeEigeneSchiffe() {
        ki.erzeugeEigeneSchiffe();
    }

    @Override
    public void setSchiffeSetzen() {
        this.schiffe = ki.getSchiffArray();
    }

    public void setAktiveKi(int aktiveKi) {
        this.aktiveKi = aktiveKi;
    }

    
    
    @Override
    public boolean isFertigSetzen() {
        return ki.isFertig();
    }
    
    @Override
    public void beginneSpiel() {
        System.out.println("Beginne KISpiel- KI1 startet");
    }
    
    public int antwort(int zeile, int spalte){
        //System.out.println("Schuss Ki auf : Zeile " + zeile + " Spalte: " + spalte + " ID: " + gridSpielfeld.getGrid()[spalte][zeile].getId());
        if(gridSpielfeldLinks.getGrid()[spalte][zeile].getId().equals("0")){
            return 0;
        }
        else{
            return 1;
        } 
    }

    @Override
    public int ueberpruefeSpielEnde() {
        /*if(anzSchiffe == andererPart){
            //System.out.println("Gegner gewonnen");
            return 1;
        }
        else if(anzSchiffe == anzGetroffen){
            //System.out.println("Spieler gewonnen");
            return 2;
        }
        return 0;*/
        return 0;
    }
    
}
