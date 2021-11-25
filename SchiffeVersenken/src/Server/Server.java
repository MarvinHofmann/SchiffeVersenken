/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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

    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;

    public Server() {
        try {
            final int port = 50000;

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

            while (true) {
                //Fängt Nachrichten ab und Überprüft
                String line = in.readLine();
                String[] splittetString = line.split(" ");
                for(int i = 0; i<splittetString.length;i= i + 2){
                    analyze(splittetString[i], splittetString[i+1]);
                }

                if (line.equals("a")) {
                    System.out.println("A wurde eingegeben");
                }
                // server.lese(incoming)
                send("Hello i´m under the water");
                //Sendet nachricht an Server
                send("I´m drowning");
                // flush sorgt dafür, dass der Writer garantiert alle Zeichen
                // in den unterliegenden Ausgabestrom schreibt.
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void send(String text) {
        try{
            out.write(String.format("%s%n", text));
            out.flush();   
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void analyze(String channel, String value){
        System.out.println("Channel: " + channel);
        System.out.println("Wert: " + value);
    }
}
