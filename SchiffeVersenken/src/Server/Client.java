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

    public Client(SpielGUIController spielGuiController) {
        this.dieGui = spielGuiController;
        if (dieGui.getDieKISpielSteuerung() != null) {
            dieGui.getDieKISpielSteuerung().setAktiveKi(1);
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
                System.out.println("Nachricht angekommen: " + line);
                //System.out.println(line);
                String[] splittetString = line.split(" ");
                if (line.equals("done")) {

                } else if (line.equals("ready") && ready == true) {
                    this.send("ready");
                } else if (splittetString[0].equals("size")) {
                    dieGui.setSpielfeldgroesse(Integer.valueOf(splittetString[1]));
                    send("done");
                } else if (splittetString[0].equals("pass")){
                    handleSpieler(0);
                    System.out.println("pass angekommen");
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
                    ready = true;
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
                    handleSpieler(1);
                    System.out.println("Client hat nix getroffen, der Gegner ist dran");
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(1, zeile, spalte, 0);
                    this.send("pass");
                } else if (Integer.valueOf(splittedString[1]) == 1) {
                    handleSpieler(0);
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(2, zeile, spalte, 0);
                    System.out.println("Getroffen");

                } else if (Integer.valueOf(splittedString[1]) == 2) {
                    handleSpieler(0);
                    dieGui.getDieOnlineSpielSteuerung().verarbeiteGrafiken(3, zeile, spalte, 0);
                  System.out.println("versenkt");
                }
                break;
            case "shot":
                if (dieGui.getDieKISpielSteuerung() != null) {
                    antwort = dieGui.getDieKISpielSteuerung().antwort(Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]));
                } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                    antwort = dieGui.getDieOnlineSpielSteuerung().antwort(Integer.valueOf(splittedString[1]), Integer.valueOf(splittedString[2]));
                    
                    if(antwort == 1){
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
                    System.out.println("Getroffen, der Spieler darf nochmal");
                } else {
                    System.out.println("Wasser, der Gegner ist dran");
                }
                String answer = "answer " + antwort;
                System.out.println(answer);;
                this.send(answer);
                break;
            }
    }

    public boolean isVerbindung() {
        return verbindung;
    }
    
    void handleSpieler(int spieler){
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