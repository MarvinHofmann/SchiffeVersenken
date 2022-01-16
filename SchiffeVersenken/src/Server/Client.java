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

    private Thread thisThread;

    public Client(SpielGUIController spielGuiController) {
        this.dieGui = spielGuiController;
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiverSpieler(1);
        } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
            dieGui.getDieOnlineSpielSteuerung().setAktiverSpieler(1);
        }
    }

    public void start() {
        try {
            Socket s = new Socket(dieGui.getIp(), port);
            //System.out.println(dieGui.getIp());
            verbindung = true;
            System.out.println("Connection established bei Client. " + verbindung);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
            usr = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String line = in.readLine();
                //System.out.println(line);
                System.out.println("Nachricht angekommen: " + line);
                String[] splittetString = line.split(" ");
                if (line.equals("done")) {
                } else if (line.equals("ready") && ready == true) {
                    System.out.println("Nachricht senden: " + "ready");
                    this.send("ready");
                } else if (splittetString[0].equals("size")) {
                    dieGui.setSpielfeldgroesse(Integer.valueOf(splittetString[1]));
                    System.out.println("Nachricht senden: " + "done");
                    send("done");
                } else if (splittetString[0].equals("pass")) {
                    handleSpieler(0, 0);
                }
                else if (splittetString[0].equals("ships")) {
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
                        try { // ACHTUNG SEHR KRIMINELL UND FRAGWÜRDIG
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

                if (line == null) {
                    //System.out.println("Line in null");
                    break;
                }
            }

            s.shutdownOutput();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.out.println("No host:" + e);
        }
    }

    public void send(String text) {
        try {
            out.write(String.format("%s%n", text));
            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void analyze(String message) {
        String[] splittedString = message.split(" ");

        switch (splittedString[0]) {
            case "save":
            //speicher implementieren
            case "load":
            //spiel laden implementieren
            case "answer":
                if (Integer.valueOf(splittedString[1]) == 0) {
                    handleSpieler(1, 0);
                    System.out.println("Client hat nix getroffen");
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    }
                    System.out.println("Nachricht senden: " + "pass");
                    this.send("pass");
                } else if (Integer.valueOf(splittedString[1]) == 1) {
                    handleSpieler(0, 1);
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    }
                    System.out.println("Client hat getroffen, der Client darf nochmal");

                } else if (Integer.valueOf(splittedString[1]) == 2) {
                    handleSpieler(0, 2);
                    if (dieGui.getDieKISpielSteuerung() != null) {
                        dieGui.getDieKISpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                        dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                    }
                    System.out.println("Client hat versenkt, der Client darf nochmal");
                }
                break;
            case "shot":
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

}
