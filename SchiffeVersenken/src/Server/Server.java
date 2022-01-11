/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import GUI.SpielGUIController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author marvi
 */
public class Server {

    final int port = 50000;

    private boolean nachrichtAngekommen = true;
    private int steuerung;
    private int setupStep = 1;
    private int antwort;
    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;
    private boolean verbindung = false;
    public boolean clientReady = false;
    private SpielGUIController dieGui;
    
    private int zeile;
    private int spalte;

    public Server(SpielGUIController gui) {
        this.dieGui = gui;
        nachrichtAngekommen = false;
        if (dieGui.getDieKISpielSteuerung() != null) {
            steuerung = 0;
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            steuerung = 1;
        }
    }

    public void start() {
        try {
            // Server-Socket erzeugen und an diesen Port binden.
            ServerSocket ss = new ServerSocket(port);

            // Auf eine Client-Verbindung warten und diese akzeptieren.
            // Als Resultat erhält man ein "normales" Socket.
            System.out.println("Waiting for client connection ... " + verbindung);
            Socket s = ss.accept();
            verbindung = true;
            System.out.println("Connection established. : " + verbindung);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            // Standardeingabestrom ebenfalls als BufferedReader verpacken.
            usr = new BufferedReader(new InputStreamReader(System.in));

            this.connectedWithClient(setupStep);

            while (true) {
                //Fängt Nachrichten ab und Überprüft
                String line = in.readLine();
                if (line.equals("done")) {
                    nachrichtAngekommen = true;
                    verarbeiteKommunikation();
                } else if (line.equals("ready")) {
                    clientReady = true;
                } else if (line.equals("pass")) {
                    handleSpieler(0);
                    System.out.println("dieser Spieler ist dran");
                }
                
                else {
                    System.out.println("Angekommen: " + line);
                    analyze(line);
                }
                // server.lese(incoming)
                //Sendet nachricht an Server
                // flush sorgt dafür, dass der Writer garantiert alle Zeichen
                // in den unterliegenden Ausgabestrom schreibt.
            }
        } catch (Exception e) {
            System.out.println("Testabc");
            e.printStackTrace();
        }
    }

    public void connectedWithClient(int kategorie) {
        if (kategorie == 1) {
            String size = "size " + dieGui.getSpielfeldgroesse();
            this.send(size);
        } else if (kategorie == 2) {
            String ships = "ships" + parseSchiffTypes(dieGui.getAnzahlSchiffeTyp());
            this.send(ships);
        } else if (kategorie == 3) {
            this.send("ready");
        }
    }

    public boolean isVerbindung() {
        return verbindung;
    }

    private String parseSchiffTypes(int[] schifftypes) {
        String parsedSchiffe = "";
        for (int i = 0; i < schifftypes.length; i++) {
            for (int j = 0; j < schifftypes[i]; j++) {
                parsedSchiffe = parsedSchiffe + " " + (i + 2);
            }
        }
        //System.out.println(parsedSchiffe);
        return parsedSchiffe;
    }

    public void send(String text) {
        try {
            out.write(String.format("%s%n", text));
            out.flush();
        } catch (IOException e) {
            System.out.println("Exception bei normal send");
            System.out.println(e);
        }
    }

    
    public void analyze(String message) {
        String[] splittedString = message.split(" ");
        switch (splittedString[0]) {
            case "save":
            //speicher implementation
            case "load":
            //spiel laden
            case "pass":
                handleSpieler(0);
                
            case "answer":
                if (Integer.parseInt(splittedString[1]) == 0) {
                    this.send("pass");
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    System.out.println("Server hat nix getroffen, der Gegner ist dran");
                    handleSpieler(1);
                } else if (Integer.parseInt(splittedString[1]) == 1) {
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    System.out.println("Getroffen, der SPieler ist nochmal dran");
                    handleSpieler(0);

                } else if (Integer.parseInt(splittedString[1]) == 2) {
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    System.out.println("Versenkt, der Spieler ist nochmal dran");
                    handleSpieler(0);
                }
                break;
            case "shot":
                if (dieGui.getDieKISpielSteuerung() != null) {
                    antwort = dieGui.getDieKISpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                    antwort = dieGui.getDieOnlineSpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                    if(antwort==1){
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                    else if(antwort == 2){
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                    else{
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                }
                if (antwort == 1 || antwort == 2) {
                    System.out.println("Der Spieler darf nochmal");
                } else {
                    System.out.println("Wasser");    
                }
                String answer = "answer " + antwort;
                System.out.println(answer);
                System.out.println("verbindung: " + isVerbindung());
                this.send(answer);
                break;
            }

    }

    public void verarbeiteKommunikation() {
        if (nachrichtAngekommen = true) {
            switch (setupStep) {
                case 1:
                    setupStep++;
                    this.connectedWithClient(setupStep);
                    break;
                case 2:
                    setupStep++;
                    this.connectedWithClient(setupStep);
                    break;
            }
        }
    }
    
    private void handleSpieler(int spieler){
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiveKi(spieler);
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            dieGui.getDieOnlineSpielSteuerung().setAktiverSpieler(spieler);
        }
    }
    
    public void setSpeicher(int zeile, int spalte){
        this.spalte = spalte;
        this.zeile = zeile;
    }
}
