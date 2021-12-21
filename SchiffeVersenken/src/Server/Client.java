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

    public Client(SpielGUIController spielGuiController) {
        this.dieGui = spielGuiController;
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
                String[] splittetString = line.split(" ");
                if (line.equals("done")) {

                } else if (line.equals("ready") && ready == true) {
                    this.send("ready");
                } else if (splittetString[0].equals("size")) {
                    dieGui.setSpielfeldgroesse(Integer.valueOf(splittetString[1]));
                    send("done");
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
            System.out.println("No host:" + e.getCause());
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
        String channel = splittedString[0];
        String value = splittedString[1];

        switch (splittedString[0]) {
            case "size":

            case "save":
            //speicher implementieren
            case "load":
            //spiel laden implementieren
            case "answer":
                if (Integer.parseInt(splittedString[1]) == 0) {
                    this.send("pass");
                    System.out.println("Client hat nix getroffen, der Gegner ist dran");

                } else if (Integer.parseInt(splittedString[1]) == 1) {

                } else if (Integer.parseInt(splittedString[1]) == 2) {

                }
            case "shot":
                if (dieGui.getDieKISpielSteuerung() != null) {
                    antwort = dieGui.getDieKISpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                } else if (dieGui.getDieOnlineSpielSteuerung() != null) {
                    antwort = dieGui.getDieOnlineSpielSteuerung().antwort(Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]));
                }
                if (antwort == 1 || antwort == 2) {
                    System.out.println("Getroffen, der Spieler darf nochmal");
                } else {
                    System.out.println("Wasser, der Gegner ist dran");
                }
                String answer = "answer " + antwort;
                System.out.println(answer);
                this.send(answer);
        }
    }

    public boolean isVerbindung() {
        return verbindung;
    }
}
