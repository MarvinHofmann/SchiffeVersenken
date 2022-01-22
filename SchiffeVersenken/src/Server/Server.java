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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    Socket s;
    ServerSocket ss;
    private Thread thisThread;

    private int zeile;
    private int spalte;
    private boolean gespeichert = false;
    
    private boolean readyNochSenden = false;

    public Server(SpielGUIController gui) {
        this.dieGui = gui;
        nachrichtAngekommen = false;
        if (dieGui.getDieKISpielSteuerung() != null) {
            steuerung = 0;
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            steuerung = 1;
        }
    }

    public void start(boolean laden){
        try {
            if (laden) {
                setupStep = 4;
            }
            // Server-Socket erzeugen und an diesen Port binden.
            ss = new ServerSocket(port);

            // Auf eine Client-Verbindung warten und diese akzeptieren.
            // Als Resultat erhält man ein "normales" Socket.
            System.out.println("Waiting for client connection ... " + verbindung);
            s = ss.accept();
            verbindung = true;
            System.out.println("Connection established. : " + verbindung);
            
            if(dieGui.getDieOnlineSpielSteuerung() != null){
                dieGui.spielStartButton(true);
            }
            else if(dieGui.getDieKISpielSteuerung() != null){
                dieGui.wartenAufVerbindung(false);
                dieGui.spielStartButton(false);
            }
            
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            // Standardeingabestrom ebenfalls als BufferedReader verpacken.
            usr = new BufferedReader(new InputStreamReader(System.in));

            this.connectedWithClient(setupStep);

            while (true) {
                //Fängt Nachrichten ab und Überprüft
                if (s.isClosed() || ss.isClosed()) {
                    break;
                }
                String line = in.readLine();
                System.out.println("Nachricht angekommen: " + line);
                if (line == null) {
                    s.close();
                    break;
                }else if(line.equals("ok")){
                    if(gespeichert){
                        // nix
                    }
                    else{
                        verarbeiteKommunikation(); // Antwort auf load 
                    }
                } 
                else if (line.equals("done")) {
                    nachrichtAngekommen = true;
                    verarbeiteKommunikation();
                } else if (line.equals("ready")) {
                    clientReady = true;
                    dieGui.infoText2LabelVisable(false);
                    dieGui.zeigeStatusLabel(1, true);
                    dieGui.zeigeStatusLabel(2, false);
                    handleSpieler(0, 0); // Ki startet zu schiesen 
                } else if (line.equals("pass")) {
                    if (!dieGui.isSpielFertig()) {
                        dieGui.zeigeStatusLabel(1, true);
                        dieGui.zeigeStatusLabel(2, false);
                        handleSpieler(0, 0);
                    }
                } else {
                    analyze(line);
                }
                
                // in den unterliegenden Ausgabestrom schreibt.
            }
            System.out.println("Close");
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(-1);
            try {
                end();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    public boolean isReadyNochSenden() {
        return readyNochSenden;
    }
    
    public boolean isClientReady(){
        return clientReady;
    }

    public void setGespeichert(boolean gespeichert) {
        this.gespeichert = gespeichert;
    }

    public void connectedWithClient(int kategorie) {
        if (kategorie == 1) {
            String size = "size " + dieGui.getSpielfeldgroesse();
            System.out.println("Nachricht senden: " + size);
            this.send(size);
        } else if (kategorie == 2) {
            String ships = "ships" + parseSchiffTypes(dieGui.getAnzahlSchiffeTyp());
            System.out.println("Nachricht senden: " + ships);
            this.send(ships);
        } else if (kategorie == 3) {
            if(dieGui.isSpielbereit()){
                System.out.println("Nachricht senden: " + "ready"); // Ready nur senden wenn server schiffe gesetzt und im spiel
                this.send("ready");    
            }
            else{
                readyNochSenden = true;
            }
        } else if (kategorie == 4) {
            System.out.println("Nachrichten senden: " + "load" + dieGui.getDieOnlineSpielSteuerung().getId());
            this.send("load " + dieGui.getDieOnlineSpielSteuerung().getId());
        } else if (kategorie == 5) { // ready auf load nach ok , kein ready auf speichern ok
            if(dieGui.isSpielbereit()){
                System.out.println("Nachricht senden: " + "ready"); // Ready nur senden wenn server schiffe gesetzt und im spiel
                this.send("ready");    
            }
            else{
                readyNochSenden = true;
            }
        }

    }

    public boolean isVerbindung() {
        return verbindung;
    }

    private String parseSchiffTypes(int[] schifftypes) {
        String parsedSchiffe = "";
        for (int i = schifftypes.length; i > 0; i--) {
            System.out.println("schiffe uebergabe i: " + i);
            for (int j = 0; j < schifftypes[i - 1]; j++) {
                parsedSchiffe = parsedSchiffe + " " + (i + 1);
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
            System.out.println("Nachricht angekommen: " + "save " + " " + splittedString[1]);
            dieGui.getSaveLoad().setId(splittedString[1]);
            dieGui.getSaveLoad().speicherOnlineClient(dieGui, dieGui.getDieOnlineSpielSteuerung());
            dieGui.getStatusAllgemein().setVisible(true);
            dieGui.getSaveButton().setVisible(true);
            send("ok");
            break;
            case "answer":
                if (Integer.parseInt(splittedString[1]) == 0) {
                    System.out.println("Server hat nix getroffen");
                    System.out.println("Nachricht senden: " + "pass");
                    dieGui.zeigeStatusLabel(1, false);
                    dieGui.zeigeStatusLabel(2, true);
                    this.send("pass");
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 1); // neu
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(1, 0);
                    }
                } else if (Integer.parseInt(splittedString[1]) == 1) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2); // neu
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    }
                    System.out.println("Server hat getroffen, der Server darf nochmal");
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 1);
                    }
                } else if (Integer.parseInt(splittedString[1]) == 2) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2); // neu
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    }
                    System.out.println("Server hat versenkt, der Server darf nochmal");
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 2);
                    }
                }
                break;
            case "shot":
                if (dieGui.getDieKISpielSteuerung() != null) {
                    antwort = dieGui.getDieKISpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                    if (antwort == 1) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else if (antwort == 2) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                    antwort = dieGui.getDieOnlineSpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                    if (antwort == 1) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else if (antwort == 2) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                }
                if (antwort == 1 || antwort == 2) {
                    System.out.println("Client hat getroffen, der Client darf nochmal");
                } else {
                    System.out.println("Client hat nix getroffen");
                }
                String answer = "answer " + antwort;
                //System.out.println(answer);
                //System.out.println("verbindung: " + isVerbindung());
                System.out.println("Nachricht senden: " + answer);
                this.send(answer);
                break;
        }

    }

    public void verarbeiteKommunikation() {
        System.out.println("Setipsteopp " + setupStep);
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
                case 4:
                    setupStep++;
                    this.connectedWithClient(setupStep);
                    break;
            }
        }
    }

    private void handleSpieler(int spieler, int antwortDavor) {
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiverSpieler(spieler);
            if (spieler == 0) {
                //System.out.println("Server schießt");
                dieGui.getDieKISpielSteuerung().schiesseAufGegner(antwortDavor);
            }
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            dieGui.getDieOnlineSpielSteuerung().setAktiverSpieler(spieler);
        }
    }

    public void setSpeicher(int zeile, int spalte) {
        this.spalte = spalte;
        this.zeile = zeile;
    }

    public void end() throws IOException {
        if (s != null) {
            s.close();
        }
        if (s!=null) {
            ss.close();
        }
    }
}
