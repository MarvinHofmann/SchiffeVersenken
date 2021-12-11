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
    
    private int size;
    private int[] schiffe;
    private SpielGUIController spielGui;
    
    
    public Server(SpielGUIController gui, int size, int[] schiffe) {
        this.spielGui = gui;
        nachrichtAngekommen = false;
        this.size = size;
        this.schiffe = schiffe;
        
        
    }
    
    public void start(){
        try {
            
            
            // Server-Socket erzeugen und an diesen Port binden.
            ServerSocket ss = new ServerSocket(port);

            // Auf eine Client-Verbindung warten und diese akzeptieren.
            // Als Resultat erhält man ein "normales" Socket.
            System.out.println("Waiting for client connection ...");
            Socket s = ss.accept();
            System.out.println("Connection established.");
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new OutputStreamWriter(s.getOutputStream());
              
            // Standardeingabestrom ebenfalls als BufferedReader verpacken.
            usr = new BufferedReader(new InputStreamReader(System.in));
            
            
            spielGui.getDieKISpielSteuerung().connectedWithClient(setupStep);
            
            
            while (true) {
                //Fängt Nachrichten ab und Überprüft
                String line = in.readLine();
                String[] splittetString = line.split(" ");
                if (line.equals("Done")) {
                    nachrichtAngekommen = true;
                    System.out.println("Done wurde eingegeben");
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

        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        
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
    
    public void sendShips(String text) {
        System.out.println("uebergebener Text an sendShips: " + text);
        try{
            out.write(String.format("%s%n", text));
            out.flush();   
        }catch (IOException e) {
            System.out.println("Exception bei schiffe schreiben");
            System.out.println(e);
        }
    }
    
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
                    spielGui.getDieKISpielSteuerung().connectedWithClient(setupStep);
                    break;
                
            }
        }
    }
}
