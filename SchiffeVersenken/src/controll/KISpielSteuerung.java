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
    
    public KISpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp, int kiStufe) {
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
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
    
    public void werdeServer(){
        serverT = new Thread (() -> {
            server = new Server(dieGui);
            server.start();
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
    
    public void schiesseAufGegner(int antwortDavor){
        int[] schuss = ki.schiesse(antwortDavor);
        String message = "shot " + schuss[0] + " " + schuss[1];
        System.out.println("Nachricht senden: " + message);
        if (server != null) {
            server.setSpeicher(schuss[0], schuss[1]);
            server.send(message);
        } else if (client != null) {
            client.send(message);
            client.setSpeicher(schuss[0], schuss[1]);
        }
    }
    
}
