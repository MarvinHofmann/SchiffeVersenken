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
    // Server-Seite eines sehr einfachen Chat-Programms mit Sockets.
    // (Anstelle von "throws IOException" sollte man Ausnahmen besser
    // gezielt mit try-catch auffangen.)
    public static void main (String [] args) throws IOException {
	// Verwendete Portnummer.
	final int port = 50000;

	// Server-Socket erzeugen und an diesen Port binden.
	ServerSocket ss = new ServerSocket(port);

	// Auf eine Client-Verbindung warten und diese akzeptieren.
	// Als Resultat erhält man ein "normales" Socket.
	System.out.println("Waiting for client connection ...");
	Socket s = ss.accept();
	System.out.println("Connection established.");

    }
}
