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
import javafx.scene.input.MouseEvent;
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
    private Server server;
    private Client client;
    //private int[][] getroffen;
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
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
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
            schiff.setFill(Color.RED);
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
            schiff.setFill(Color.GREEN); //Setzte die Farbe grün
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße());
        }
    }

    public void makeHandler(Rectangle r) {
        r.setOnMouseClicked(event -> clicked(event, r));
    }

    @Override
    public void beginneSpiel() {
        System.out.println("Beginne OnlineSpiel- Spieler startet");
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                makeHandler(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
    }

    public int antwort(int zeile, int spalte) {
        //System.out.println("Schuss Ki auf : Zeile " + zeile + " Spalte: " + spalte + " ID: " + gridSpielfeld.getGrid()[spalte][zeile].getId());
        if (gridSpielfeldLinks.getGrid()[spalte][zeile].getId().equals("0")) {
            return 0;
        } else {
            return 1;
        }
    }

    private void clicked(MouseEvent event, Rectangle rectangle) {
        if (aktiverSpieler == 0) {
            int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
            int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
            //int[] gegnerSchuss = {-1, -1};
            rectangle.setFill(Color.TRANSPARENT);
            String message = "shot " + zeile + " " + spalte;
            if (server != null) {
                server.send(message);
            } else if (client != null) {
                client.send(message);
            }
        }

    }

    public void handleMessage(String message) {
        System.out.println("Handle Message: " + message);
        String[] splittetString = message.split(" ");
        System.out.println("Splitted String[0] " + splittetString[0]);
        System.out.println(server);
        if (server != null) {
            switch (splittetString[0]) {
                case "save":
                //speicher implementation
                case "load":
                //spiel laden
                case "answer":
                    if (Integer.parseInt(splittetString[1]) == 0) {
                        server.send("pass");
                        System.out.println("Server hat nix getroffen, der Gegner ist dran");
                        aktiverSpieler = 1;
                    } else if (Integer.parseInt(splittetString[1]) == 1) {
                        aktiverSpieler = 0;
                        System.out.println("Getroffen, der SPieler ist nochmal dran");

                    } else if (Integer.parseInt(splittetString[1]) == 2) {
                        aktiverSpieler = 0;
                        System.out.println("Versenkt, der Spieler ist nochmal dran");
                    }
                case "shot":
                    System.out.println("shot: bei Server");
                    int antwort = antwort(Integer.parseInt(splittetString[1]), Integer.parseInt(splittetString[2]));
                    if (antwort == 1 || antwort == 2) {
                        aktiverSpieler = 1;
                    } else {
                        aktiverSpieler = 0;
                    }
                    String answer = "answer " + antwort;
                    System.out.println(answer);
                    server.send(answer);
            }
        } else if (client != null) {
            switch (splittetString[0]) {
                case "save":
                //speicher implementieren
                case "load":
                //spiel laden implementieren
                case "answer":
                    if (Integer.parseInt(splittetString[1]) == 0) {
                        client.send("pass");
                        System.out.println("Client hat nix getroffen, der Gegner ist dran");
                        aktiverSpieler = 1;
                    } else if (Integer.parseInt(splittetString[1]) == 1) {
                        aktiverSpieler = 0;

                    } else if (Integer.parseInt(splittetString[1]) == 2) {
                        aktiverSpieler = 0;
                    }
                case "shot":
                    int antwort = antwort(Integer.parseInt(splittetString[1]), Integer.parseInt(splittetString[2]));
                    if (antwort == 1 || antwort == 2) {
                        aktiverSpieler = 1;
                    } else {
                        aktiverSpieler = 0;
                    }
                    String answer = "answer " + antwort;
                    System.out.println(answer);
                    client.send(answer);
            }
        }
    }

    @Override
    public int ueberpruefeSpielEnde() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
