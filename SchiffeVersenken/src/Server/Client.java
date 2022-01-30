package Server;

import GUI.SpielGUIController;
import java.io.*;
import java.net.Socket;
import javafx.application.Platform;
import schiffeversenken.SchiffeVersenken;

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

    private boolean readyNochSenden = false;
    private boolean serverReady = false;

    /**
     * Konstruktor des Clients, hier wird nach Spielstart der zu startende Spieler festgelegt, in dem Fall des Clients, der Server also der Gegner
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
     * Versucht eine Verbindung zum Server aufzubauen, empfangene Nachrichten
     * werden an die Funktion "verarbeiteLine" weitergeleitet
     *
     */
    public void start() {
        try {
            //Labels, zum Verbindungsstatus
            dieGui.getInfoTextVerbindung().setVisible(true);
            dieGui.getSetzenControll().setVisible(false);
            s = new Socket(dieGui.getIp(), port);
            verbindung = true;
            dieGui.getInfoTextVerbindung().setVisible(false);
            if (dieGui.getDieOnlineSpielSteuerung() != null) {
                dieGui.getSetzenControll().setVisible(true);
            }
            
          
            if (dieGui.getDieOnlineSpielSteuerung() != null) {
                dieGui.spielStartButton(true);
            } else if (dieGui.getDieKISpielSteuerung() != null) {
                dieGui.wartenAufVerbindung(false);
                dieGui.spielStartButton(false);
            }
            
            //Definition von Reader, und Outputstream um ankommende Nachrichten zu lesen und schreiben
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
            usr = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                //einlesen der ankommenden Nachricht
                String line = in.readLine();
                System.out.println("Nachricht angekommen: " + line);

                if (line == null) {
                    //Beenden der Verbindung
                    zurueckMenue();
                    s.shutdownOutput();
                    s.close();
                } else {
                    verarbeiteLine(line);
                }
            }
        } catch (Exception e) {
            if (verbindung) {
                //Beenden der Verbindung, nach Verbindungsabbruch
                zurueckMenue();
                System.out.println("Connection closed.");
            }
            dieGui.getClientWartet().setVisible(true);
            dieGui.getSpielstart().setVisible(false);
            dieGui.getSetzenControll().setVisible(false);
            dieGui.getInfoTextVerbindung().setVisible(true);
        }
    }

   /**
    * Sorgt beim Aufruf dafür, dass das jeweilige Client Objekt angehalten und beendet wird.
    * Anschließend wird die Stage neu gestartet, um ein erneutes Online-Spiel zu ermöglichen
    * 
    */
    public void zurueckMenue() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().getClienT().interrupt();
                        dieGui.getDieOnlineSpielSteuerung().getClient().end();
                    } else if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getClientT().interrupt();
                        dieGui.getDieKISpielSteuerung().getClient().end();
                    }
                    SchiffeVersenken.getApplicationInstance().restart(); //Startet die Stage neu  
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    /**
     * Sendet einen Text an den Server
     *
     * @param text enthaelt den Text der gesendet werden soll
     */
    public void send(String text) {
        try {
            //Schreiben des Strings in den Outputstream
            out.write(String.format("%s%n", text));
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * Gibt den Wert readyNochSenden zurück, der enthält ob schon ready gesendet wurde
     * @return wert, ob schon ready gesendet wurde
     */
    public boolean isReadyNochSenden() {
        return readyNochSenden;
    }

    /**
     * Verarbeitet die Nachricht. Je nach Inhalt wird ausgewertet und die
     * Uebergabe mit Schiffanzahl und spielfeldgroesse fuer den Spielstart
     * vorbereitet
     *
     * @param line enthaelt die Nachricht
     *
     */
    private void verarbeiteLine(String line) {
        String[] splittetString = line.split(" ");
        if (line.equals("done")) {
        } else if (line.equals("ready") && ready == true) {
            serverReady = true;
            dieGui.infoText2LabelVisable(false);
            if (dieGui.isSpielbereit()) {
                System.out.println("Nachricht senden: " + "ready"); // Ready nur senden wenn server schiffe gesetzt und im spiel
                dieGui.zeigeStatusLabel(1, false);
                dieGui.zeigeStatusLabel(2, true);
                dieGui.getBtnMenue().setVisible(true);
                this.send("ready");
            } else {
                readyNochSenden = true;
            }
        } else if (line.equals("ok")) {
        } else if (splittetString[0].equals("size")) {
            dieGui.setSpielfeldgroesse(Integer.valueOf(splittetString[1]));
            int akt = var.var.pxGroesse;
            int verschieb = akt/ Integer.valueOf(splittetString[1]);
            var.var.pxGroesse = verschieb * Integer.valueOf(splittetString[1]);
            var.var.hoehe = verschieb * Integer.valueOf(splittetString[1]);
            verschieb = akt - verschieb * Integer.valueOf(splittetString[1]);
            var.var.verschiebung = 2 * verschieb + 4;
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
    
    /**
     * Gibt an, ob das Spiel gestartet werden kann
     * @return Rückgabe, ob beide bereit sind, und das Spiel losgehen kann 
     */
    public boolean isServerReady() {
        return serverReady;
    }
    
    /**
     * Analysiert den einkommenden String und verarbeitet diesen entsprechend nach seinem Inhalt.
     * Hier wird das Speichern, Laden, Schiessen und Antworten verarbeitet und entsprechend reagiert.
     * @param message enthält die einkommende Nachricht
     */
    public void analyze(String message) {
        String[] splittedString = message.split(" ");

        switch (splittedString[0]) {
            case "save":
                //Initiert den Speichervorgang mit einer bestimmten ID
                dieGui.getSaveLoad().setId(splittedString[1]);
                dieGui.getSaveLoad().speicherOnlineClient(dieGui, dieGui.getDieOnlineSpielSteuerung());
                dieGui.getStatusAllgemein().setVisible(true);
                dieGui.getSaveButton().setVisible(true);
                send("ok");
                break;
            case "load":
                ready = true;
                //Initiert den Ladevorgang eines Spielstandes mit einer bestimmten ID
                dieGui.getSaveLoad().startLadenOnline(Long.parseLong(splittedString[1]));
                dieGui.getSaveButton().setVisible(true);
                dieGui.getBtn_Random().setVisible(false);
                dieGui.getBtn_neuPlatzieren().setVisible(false);
                dieGui.getSpielstart().setVisible(false);
                dieGui.getDieOnlineSpielSteuerung().ladeClient(dieGui.getSaveLoad().getIp(), dieGui.getSaveLoad().getL(), dieGui.getSaveLoad().getParamInc(), dieGui.getSaveLoad().getStyp(), dieGui.getSaveLoad().getGetroffenAr(), dieGui.getSaveLoad().getGetroffenGeg(), dieGui.getSaveLoad().getGridRechtsArr(), dieGui.getSaveLoad().getGridLinksArr(), dieGui.getSaveLoad().getOnlineValues());
                send("ok");
                break;
            case "answer":
                //verarbeitet die Antwort auf einen Schuss, je nachdem darf nochmal geschossen werden, oder nicht.
                //Entsprechend der Antwort werden die Grafiken gesetzt
                if (Integer.valueOf(splittedString[1]) == 0) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 1);
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(1, 0);
                    }
                    dieGui.zeigeStatusLabel(1, false);
                    dieGui.zeigeStatusLabel(2, true);
                    this.send("pass");
                } else if (Integer.valueOf(splittedString[1]) == 1) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2);
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 1);
                    }

                } else if (Integer.valueOf(splittedString[1]) == 2) {
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte, 2);
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    }
                    if (!dieGui.isSpielFertig()) {
                        handleSpieler(0, 2);
                    }
                }
                break;
            case "shot":
                //verarbeitet, wohin der Gegner hgeschossen hat, ob getroffen wurde oder nicht, und sendet die Antwort an den Server
                //Setzt die entsprechenden Grafiken auf der eigenen Spielfeldseite 
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
                String answer = "answer " + antwort;
                this.send(answer);
                break;
        }
    }
    
    /**
     * Gibt den aktuellen Verbindungszustand zurück
     * @return Enthält den aktuellen Verbindungszustand
     */
    public boolean isVerbindung() {
        return verbindung;
    }
    
    /**
     * Verarbeitet die aktiven Spieler, im Falle eines KI Online Spiels wird der nächste Schuss der KI initiiert
     * @param spieler enthält 0 oder 1 für entweder den Client oder Server
     * @param antwortDavor 
     */
    private void handleSpieler(int spieler, int antwortDavor) {
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiverSpieler(spieler);
            if (spieler == 0) {
                dieGui.getDieKISpielSteuerung().schiesseAufGegner(antwortDavor);
            }
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            dieGui.getDieOnlineSpielSteuerung().setAktiverSpieler(spieler);
        }
    }
    
    /**
     * Speichert die Zeile und Spalte des letzten Schusses, um bei einer Antwort die Grafiken laden zu können
     * @param zeile beinhaltet die Zeile des Schusses
     * @param spalte beinhaltet die Spalte des Schusses
     */
    public void setSpeicher(int zeile, int spalte) {
        this.spalte = spalte;
        this.zeile = zeile;
    }
    
    /**
     * Beendet die Verbindung des Objekts
     * @throws IOException bei fehlender Socket Instanz
     */
    public void end() throws IOException {
        if (s != null) {
            s.close();
        }
    }
}
