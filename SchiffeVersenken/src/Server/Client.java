package Server;

import java.io.*;
import java.net.Socket;

public class Client {
    private BufferedReader usr;
    private BufferedReader in;
    private Writer out;
    private String ipAddress;
    // Client-Seite eines sehr einfachen Chat-Programms mit Sockets.
    // (Anstelle von "throws IOException" sollte man Ausnahmen besser
    // gezielt mit try-catch auffangen.)
    public Client(String ip) {
        this.ipAddress = ip;
        try {
            // Verwendete Portnummer (vgl. Server).
	final int port = 50000;

	// Verbindung zum Server mit Name oder IP-Adresse args[0]
	// über Portnummer port herstellen.
	// Als Resultat erhält man ein Socket.
        String ipA = "localhost"; 
	Socket s = new Socket(ipA, port);
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
	    String line = usr.readLine();
	    if (line == null || line.equals("")) break;
	    out.write(String.format("%s%n", line));
	    out.flush();
	    // flush sorgt dafür, dass der Writer garantiert alle Zeichen
	    // in den unterliegenden Ausgabestrom schreibt.

	    line = in.readLine();
	    if (line == null) break;
	    System.out.println("<<< " + line);
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
}
