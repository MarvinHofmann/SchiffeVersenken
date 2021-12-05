package Server;

import GUI.SpielGUIController;
import java.io.*;
import java.net.Socket;

public class Client {
    final int port = 50000;
    
    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;
    private String ipAddress;
    private GUI.SpielGUIController guiController;
    // Client-Seite eines sehr einfachen Chat-Programms mit Sockets.
    // (Anstelle von "throws IOException" sollte man Ausnahmen besser
    // gezielt mit try-catch auffangen.)
    public Client(String ip, SpielGUIController spielGuiController) {
        this.ipAddress = ip;
        this.guiController = spielGuiController;
    }
    
    public void start(){
        try {
            // Verwendete Portnummer (vgl. Server).
	// Verbindung zum Server mit Name oder IP-Adresse args[0]
	// über Portnummer port herstellen.
	// Als Resultat erhält man ein Socket.
	Socket s = new Socket(ipAddress, port);
        System.out.println(ipAddress);
	System.out.println("Connection established Hier bei Client.");
	// Ein- und Ausgabestrom des Sockets ermitteln
	// und als BufferedReader bzw. Writer verpacken
	// (damit man zeilen- bzw. zeichenweise statt byteweise arbeiten kann).
	/*BufferedReader in =
		new BufferedReader(new InputStreamReader(s.getInputStream()));
	Writer out = new OutputStreamWriter(s.getOutputStream());

	// Standardeingabestrom ebenfalls als BufferedReader verpacken.
	BufferedReader usr = 
			new BufferedReader(new InputStreamReader(System.in));
                        */
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new OutputStreamWriter(s.getOutputStream());
        usr = new BufferedReader(new InputStreamReader(System.in));
	// Abwechselnd vom Benutzer lesen und ins Socket schreiben
	// bzw. vom Socket lesen und auf den Bildschirm schreiben.
	// Abbruch bei EOF oder Leerzeile vom Benutzer bzw. bei EOF vom Socket.
	while (true) {
            
            String line = in.readLine();
            String[] splittetString = line.split(" ");
            for(int i = 0; i<splittetString.length;i= i + 2){
                analyze(splittetString[i], splittetString[i+1]);
            }
            if (line.equals("a")) {
                System.out.println("A wurde eingegeben");
            }
            
	    // flush sorgt dafür, dass der Writer garantiert alle Zeichen
	    // in den unterliegenden Ausgabestrom schreibt.

	    
	    if (line == null) break;
	    
	}

	// EOF ins Socket "schreiben".
	s.shutdownOutput();
	System.out.println("Connection closed.");
        
        } catch (Exception e) {
            
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
    
    public void analyze(String channel, String value){
        System.out.println("Channel: " + channel);
        System.out.println("Wert: " + value);
        send("Done ok");
        notifyAll();
    }
}
