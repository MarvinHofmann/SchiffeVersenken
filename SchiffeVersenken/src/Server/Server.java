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
import javafx.application.Platform;
import schiffeversenken.SchiffeVersenken;

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
    
    /**
     * Öffnet einen Socket auf dem Port 50000, der eingehende Verbindungsanfragen annimmt und ankommende Nachrichten empfängt
     * @param laden Wert, ob ein alter Spielstand geladen werden soll
     */
    public void start(boolean laden) {
        try {
            if (laden) {
                setupStep = 4;
            }
            // Server-Socket erzeugen und an diesen Port binden.
            ss = new ServerSocket(port);

            // Auf eine Client-Verbindung warten und diese akzeptieren.
            // Als Resultat erhält man ein "normalen" Socket.
            System.out.println("Waiting for client connection ... " + verbindung);
            s = ss.accept();
            verbindung = true;
            System.out.println("Connection established. : " + verbindung);

            if (dieGui.getDieOnlineSpielSteuerung() != null) {
                dieGui.spielStartButton(true);
            } else if (dieGui.getDieKISpielSteuerung() != null) {
                dieGui.wartenAufVerbindung(false);
                dieGui.spielStartButton(false);
            }
            
            //In und Outputstreams, zum senden und empfangen von Nachrichten
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());

            // Standardeingabestrom ebenfalls als BufferedReader verpacken.
            usr = new BufferedReader(new InputStreamReader(System.in));
            
            //Startet den Spielaufbau mit dem Client
            this.connectedWithClient(setupStep);

            while (true) {
                //Fängt Nachrichten ab und Überprüft
                if (s.isClosed() || ss.isClosed()) {
                    break;  
                }
                String line = in.readLine();
                System.out.println("Nachricht angekommen: " + line);
                if (line == null) {
                    //check die Naxchricht null entspricht, geht dann zurück zum Menü
                    zurueckHauptMenue();
                    break;
                } else if (line.equals("ok")) {
                    if (gespeichert) {
                        // nix
                    } else {
                        verarbeiteKommunikation(); // Antwort auf load 
                    }
                } else if (line.equals("done")) {
                    //Antwort des Clients auf die Übermittelung der Daten zum Spiel
                    nachrichtAngekommen = true;
                    verarbeiteKommunikation();
                } else if (line.equals("ready")) {
                    clientReady = true;
                    dieGui.infoText2LabelVisable(false);
                    dieGui.zeigeStatusLabel(1, true);
                    dieGui.zeigeStatusLabel(2, false);
                    dieGui.getBtnMenue().setVisible(true);
                    handleSpieler(0, 0); // Ki startet zu schiesen 
                } else if (line.equals("pass")) {
                    if (!dieGui.isSpielFertig()) {
                        dieGui.zeigeStatusLabel(1, true);
                        dieGui.zeigeStatusLabel(2, false);
                        handleSpieler(0, 0);
                    }
                } else {
                    //Verarbeitung sonstiger Nachrichten
                    analyze(line);
                }

            }
            System.out.println("Close");
            System.out.println("Client ist weg");

            //zurueckHauptMenue();
        } catch (Exception e) {
            System.out.println("Client ist weg mit Fehler");
            //zurueckHauptMenue();
            System.out.println("Connection closed.");
        }
    }
    
    /**
     * Beendet die Verbindung und lädt die Stage im Hauptmenü neu
     */
    public void zurueckHauptMenue() {
        try {
            System.out.println("mache null und zu");
            //this.send(null);
            s.shutdownOutput();
            s.close();
            ss.close();
        } catch (IOException ex) {
            System.out.println("Fehler beim schließen s.out");
            System.out.println(ex);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Versuche zu schließen");
                    if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().getServerT().interrupt();
                    } else if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getServerT().interrupt();
                    }

                    SchiffeVersenken.getApplicationInstance().restart(); //Startet die Stage neu  
                } catch (Exception ex) {
                    System.out.println("Fehler beim schließen");
                    System.out.println(ex);
                }
            }
        });
    }

    /**
     * Gibt den Wert readyNochSenden zurück, der enthält ob schon ready gesendet wurde
     * @return ob Ready gesendet Wurde 
     */
    public boolean isReadyNochSenden() {
        return readyNochSenden;
    }
    
    /**
     * Gibt den Wert isClientReady zurück, der Angibt ob der Client bereit ist.
     * @return ob der Client bereit für das Spiel ist 
     */
    public boolean isClientReady() {
        return clientReady;
    }
    
    /**
     * Setzt die Variable gespeichert auf den entsprechenden Wert
     * @param gespeichert ob das Spiel gespeichert wurde
     */
    public void setGespeichert(boolean gespeichert) {
        this.gespeichert = gespeichert;
    }
    
    /**
     * Übermittelt nach und nach die für das Spiel notwendigen Informationen, wie die Spielfeldgröße, welche und wieviele Schiffe benötigt werden, an den Client
     * Wenn alles übermittelt wurde und in der GUI gesetzt, wird erst ready.
     * @param kategorie welche Parameter/Informationen an den Client übermittelt werden 
     */
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
            if (dieGui.isSpielbereit()) {
                System.out.println("Nachricht senden: " + "ready"); // Ready nur senden wenn server schiffe gesetzt und im spiel
                this.send("ready");
            } else {
                readyNochSenden = true;
            }
        } else if (kategorie == 4) {
            System.out.println("Nachrichten senden: " + "load" + dieGui.getDieOnlineSpielSteuerung().getId());
            this.send("load " + dieGui.getDieOnlineSpielSteuerung().getId());
        } else if (kategorie == 5) { // ready auf load nach ok , kein ready auf speichern ok
            if (dieGui.isSpielbereit()) {
                System.out.println("Nachricht senden: " + "ready"); // Ready nur senden wenn server schiffe gesetzt und im spiel
                this.send("ready");
            } else {
                readyNochSenden = true;
            }
        }

    }
    /**
     * Gibt den aktuellen Verbindungszustand zurück
     * @return den aktuellen Zustand der Verbindung 
     */
    public boolean isVerbindung() {
        return verbindung;
    }
    
    /**
     * Wandelt ein Array, dass die Schiffsanzahlen beinhaltet, in einen einzigen, entsprechend formatierten String
     * @param Schifftypes Array, das die Schiffe enthält
     * @return String, der die Anzahl aller Schiffstypen enthält
     */
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
    
    /**
     * Sendet einen Text an den Client
     * @param text der Text der gesendet werden soll 
     */
    public void send(String text) {
        try {
            out.write(String.format("%s%n", text));
            out.flush();
        } catch (IOException e) {
            System.out.println("Exception bei normal send");
            System.out.println(e);
        }
    }
     /**
     * Analysiert den einkommenden String und verarbeitet diesen entsprechend nach seinem Inhalt.
     * Hier wird das Speichern, Laden, Schiessen, Antworten verarbeitet, und entsprechend reagiert.
     * @param message enthält die einkommende Nachricht
     */
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
    /**
     * Handelt bei was der aktuelle Schritt in der Infoübergabe ist, und welche Information als nächstes übermittelt wird
     */
    public void verarbeiteKommunikation() {
        //System.out.println("Setipsteopp " + setupStep);
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
    
    /**
     * Steuert welcher Spieler an der Reihe ist
     * @param spieler enthält die 
     * @param antwortDavor 
     */
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
        s.shutdownOutput();
        if (s != null) {
            s.close();
        }
        if (ss != null) {
            ss.close();
        }
    }
}
