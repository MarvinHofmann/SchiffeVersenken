package Server;

import GUI.SpielGUIController;
import java.io.*;
import java.net.Socket;

public class Client {

    final int port = 50000;

    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;
    private GUI.SpielGUIController dieGui;
    private int size;
    private int antwort;
    public boolean ready = false;
    int[] schiffe = {0, 0, 0, 0};
    private boolean verbindung;
    private int zeile;
    private int spalte;
    Socket s;

    /**
     * Konstruktor des Clients
     *
     * @param spielGuiController erhaelt ein SpielGuiController Objekt legt den
     * Startspieler/StartKi fest
     */
    public Client(SpielGUIController spielGuiController) {
        this.dieGui = spielGuiController;
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiverSpieler(1);
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            dieGui.getDieOnlineSpielSteuerung().setAktiverSpieler(1);
        }
    }

    /**
     * versucht eine Verbindung zum Server aufzubauen empfangene Nachrichten
     * werden an die Funktion "verarbeiteLine" weitergeleitet
     *
     */
    public void start() {
        try {
            s = new Socket(dieGui.getIp(), port);
            //System.out.println(dieGui.getIp());
            verbindung = true;
            System.out.println("Connection established bei Client. " + verbindung);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
            usr = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String line = in.readLine();
                //System.out.println("Nachricht angekommen: " + line);
                //System.out.println(line)

                if (line == null) {
                    //System.out.println("Line in null");
                    break;
                } else {
                    verarbeiteLine(line);
                }
            }

            s.shutdownOutput();
            s.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.out.println("No host:" + e);
        }
    }

    /**
     * Sendet einen Text an den Server
     *
     * @param text enthaelt den Text der gesendet werden soll
     */
    public void send(String text) {
        try {
            out.write(String.format("%s%n", text));
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Verarbeitet die Nachricht. Je nach inhalt wird ausgewertet und die
     * Uebergabe mit Schiffanzahl, und spielfeldgroesse fuer den Spielstart
     * vorbereitet
     *
     * @param line enthaelt die Nachricht
     *
     */
    private void verarbeiteLine(String line) {
        String[] splittetString = line.split(" ");
        //System.out.println(line);
        System.out.println("Nachricht angekommen: " + line);
        if (line.equals("done")) {
        } else if (line.equals("ready") && ready == true) {
            System.out.println("Nachricht senden: " + "ready");
            this.send("ready");
        } else if (splittetString[0].equals("size")) {
            dieGui.setSpielfeldgroesse(Integer.valueOf(splittetString[1]));
            System.out.println("Nachricht senden: " + "done");
            send("done");
        } else if (splittetString[0].equals("pass")) {
            if (!dieGui.isSpielFertig()) {
                dieGui.zeigeStatusLabel(1, true);
                dieGui.zeigeStatusLabel(2, false);
                handleSpieler(0, 0);
            }
        } else if (splittetString[0].equals("ships")) {
            for (int i = 1; i < splittetString.length; i++) {
                switch (splittetString[i]) {
                    case "2":
                        schiffe[0]++;
                        break;
                    case "3":
                        schiffe[1]++;
                        break;
                    case "4":
                        schiffe[2]++;
                        break;
                    case "5":
                        schiffe[3]++;
                        break;
                }
            }
            dieGui.setAnzahlSchiffeTyp(schiffe);
            dieGui.erstelleSteuerung();
            while (!dieGui.isFertig()) {
                ready = false;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ready = true;
            System.out.println("Nachricht senden: " + "done");
            send("done");
        } else {
            analyze(line);
        }
    }

    public void analyze(String message) {
        String[] splittedString = message.split(" ");

        switch (splittedString[0]) {
            case "save":
                //speicher implementieren
                System.out.println("Nachricht angekommen: " + "save " + " " + splittedString[1]);
                dieGui.getSaveLoad().setId(splittedString[1]);
                dieGui.getSaveLoad().speicherOnlineClient(dieGui, dieGui.getDieOnlineSpielSteuerung());
                dieGui.getStatusLabel1().setVisible(false);
                dieGui.getStatusAllgemein().setVisible(true);
                dieGui.getSaveButton().setVisible(false);
                send("ok");
                break;
            case "load":
                System.out.println("Nachricht angekommen: " + "load " + " " + splittedString[1]);
                dieGui.getSaveLoad().startLadenOnline(Long.parseLong(splittedString[1]));
                dieGui.getSaveButton().setVisible(false);
                dieGui.getBtn_Random().setVisible(false);
                dieGui.getBtn_neuPlatzieren().setVisible(false);
                dieGui.getSpielstart().setVisible(false);
                dieGui.getDieOnlineSpielSteuerung().ladeClient(dieGui.getSaveLoad().getIp(), dieGui.getSaveLoad().getL(), dieGui.getSaveLoad().getParamInc(), dieGui.getSaveLoad().getStyp(), dieGui.getSaveLoad().getGetroffenAr(), dieGui.getSaveLoad().getGetroffenGeg(), dieGui.getSaveLoad().getGridRechtsArr(), dieGui.getSaveLoad().getGridLinksArr(), dieGui.getSaveLoad().getOnlineValues());
                send("ok");
                break;
            case "answer":
                if (Integer.valueOf(splittedString[1]) == 0) {
                    dieGui.zeigeStatusLabel(1, false);
                    dieGui.zeigeStatusLabel(2, true);
                    System.out.println("Client hat nix getroffen");
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 1); // neu
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(1, 0);
                    }
                    System.out.println("Nachricht senden: " + "pass");
                    this.send("pass");
                } else if (Integer.valueOf(splittedString[1]) == 1) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        //System.out.println("----------------------------------------------- zeile: " + zeile + " spalte: " + spalte + " = 2");
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2); // xx
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 1);
                    }
                    System.out.println("Client hat getroffen, der Client darf nochmal");

                } else if (Integer.valueOf(splittedString[1]) == 2) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2); // neu
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 2);
                    }
                    System.out.println("Client hat versenkt, der Client darf nochmal");
                }
                break;
            case "shot":
                dieGui.getStatusAllgemein().setVisible(false);
                if (dieGui.getDieKISpielSteuerung() != null) {
                    antwort = dieGui.getDieKISpielSteuerung().antwort(Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]));
                    if (antwort == 1) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else if (antwort == 2) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                    antwort = dieGui.getDieOnlineSpielSteuerung().antwort(Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]));
                    if (antwort == 1) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else if (antwort == 2) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    } else {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]), 1);
                    }
                }
                if (antwort == 1 || antwort == 2) {
                    System.out.println("Server hat getroffen, der Server darf nochmal");
                } else {
                    System.out.println("Server hat nix getroffen");
                }
                String answer = "answer " + antwort;
                //System.out.println(answer);
                System.out.println("Nachricht senden: " + answer);
                this.send(answer);
                break;
        }
    }

    public boolean isVerbindung() {
        return verbindung;
    }

    private void handleSpieler(int spieler, int antwortDavor) {
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiverSpieler(spieler);
            if (spieler == 0) {
                //System.out.println("Client schiesst");
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
    
    public void end() throws IOException{
        s.close();
    }
}
