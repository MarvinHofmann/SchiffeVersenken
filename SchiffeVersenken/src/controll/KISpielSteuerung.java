/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import Server.Client;
import Server.Server;
import shapes.KI;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class KISpielSteuerung extends SpielSteuerung{
    private KI ki = null;
    private Server server;
    private Client client;
    
    public KISpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp);
    }
    
    public KISpielSteuerung(SpielGUIController gui){
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
    }
    
    public void uebergebeRest(int spielfeldgroesse, int[] anzahlSchiffeTyp){
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp);
    }
    
    public void werdeServer(){
        new Thread (() -> {
            server = new Server(dieGui, spielfeldgroesse, anzahlSchiffeTyp);
            server.start();
        }).start();
    }
    
    public void werdeClient(String ip){
        new Thread(() -> {
            client = new Client(ip, dieGui);
            client.start();
        }).start();
    }
    
    public void connectedWithClient(int kategorie){
        if(kategorie == 1){
            String size = "size " + spielfeldgroesse;
            System.out.println("Kategorie 1");
            server.send(size);
        }
        else if(kategorie == 2){
            String ships = "ships" + parseSchiffTypes(anzahlSchiffeTyp);
            System.out.println("Kategorie 2");
            server.sendShips(ships);
        }   
    }

    private String parseSchiffTypes(int[] schifftypes){
        String parsedSchiffe = "";
        for(int i = 0; i < schifftypes.length; i++){
            for(int j = 0; j < schifftypes[i]; j++){
                parsedSchiffe = parsedSchiffe + " " + (i + 2);
            }
        }
        System.out.println(parsedSchiffe);
        return parsedSchiffe;
    }
    
    public KI getKi() {
        return ki;
    }

    public Server getServer() {
        return server;
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
    
}
