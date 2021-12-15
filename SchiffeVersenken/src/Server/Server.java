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
    private int setupStep = 1;
    
    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;
    private boolean verbindung = false;
    
    private SpielGUIController dieGui;
    
    public Server(SpielGUIController gui){
        this.dieGui = gui;
        nachrichtAngekommen = false;
    }
    
    public void start(){
        try {
            // Server-Socket erzeugen und an diesen Port binden.
            ServerSocket ss = new ServerSocket(port);

            // Auf eine Client-Verbindung warten und diese akzeptieren.
            // Als Resultat erhält man ein "normales" Socket.
            System.out.println("Waiting for client connection ... " + verbindung);
            Socket s = ss.accept();
            System.out.println("Connection established. : " + verbindung);
            verbindung = true;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
              
            // Standardeingabestrom ebenfalls als BufferedReader verpacken.
            usr = new BufferedReader(new InputStreamReader(System.in));
            
            this.connectedWithClient(setupStep);

            while (true) {
                //Fängt Nachrichten ab und Überprüft
                String line = in.readLine();
                String[] splittetString = line.split(" ");
                if (line.equals("done")) {
                    nachrichtAngekommen = true;
                    //System.out.println("Done wurde eingegeben");
                    verarbeiteKommunikation();
                }
                else{
                    for(int i = 0; i<splittetString.length;i= i + 2){
                        analyze(splittetString[i], splittetString[i+1]);
                    }
                }
                // server.lese(incoming)
                //Sendet nachricht an Server
                // flush sorgt dafür, dass der Writer garantiert alle Zeichen
                // in den unterliegenden Ausgabestrom schreibt.
            }
        }
        catch (Exception e) {
            System.out.println(e.getCause());
        }
    }
    
    public void connectedWithClient(int kategorie){
        if(kategorie == 1){
            String size = "size " + dieGui.getSpielfeldgroesse();
            //System.out.println("Kategorie 1");
            this.send(size);
        }
        else if(kategorie == 2){
            String ships = "ships" + parseSchiffTypes(dieGui.getAnzahlSchiffeTyp());
            //System.out.println("Kategorie 2");
            this.send(ships);
        }   
    }

    public boolean isVerbindung() {
        return verbindung;
    }

    private String parseSchiffTypes(int[] schifftypes){
        String parsedSchiffe = "";
        for(int i = 0; i < schifftypes.length; i++){
            for(int j = 0; j < schifftypes[i]; j++){
                parsedSchiffe = parsedSchiffe + " " + (i + 2);
            }
        }
        //System.out.println(parsedSchiffe);
        return parsedSchiffe;
    }

    public void send(String text) {
        try{
            out.write(String.format("%s%n", text));
            out.flush();   
        }catch (IOException e) {
            System.out.println("Exception bei normal send");
            System.out.println(e.getCause());
        }
    }
    
    /*public void sendShips(String text) {
        System.out.println("uebergebener Text an sendShips: " + text);
        try{
            out.write(String.format("%s%n", text));
            out.flush();   
        }catch (IOException e) {
            System.out.println("Exception bei schiffe schreiben");
            System.out.println(e);
        }
    }*/
    
    public void analyze(String channel, String value){
        System.out.println("Channel: " + channel);
        System.out.println("Wert: " + value);
        
        switch(channel){
            case "shot":
                send("answer 0");
            case "answer":
                send("naechster zug");
            case "save":
                send("speichern");
            case "load":
                send("laden");
        }
    }
    
    public void verarbeiteKommunikation(){
        if(nachrichtAngekommen = true){
            switch(setupStep){
                case 1:
                    setupStep++;
                        this.connectedWithClient(setupStep);
                    break;
                
            }
        }
    }
}
