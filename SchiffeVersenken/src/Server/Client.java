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
    
    private int size;
    private int[] schiffe = {0, 0, 0, 0};
    
    public Client(String ip, SpielGUIController spielGuiController) {
        this.ipAddress = ip;
        this.guiController = spielGuiController;
    }
    
    public void start(){
        try {
        
	Socket s = new Socket(ipAddress, port);
        System.out.println(ipAddress);
	System.out.println("Connection established Hier bei Client.");
	
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new OutputStreamWriter(s.getOutputStream());
        usr = new BufferedReader(new InputStreamReader(System.in));
	
	while (true) {
            
            String line = in.readLine();
            System.out.println(line);
            String[] splittetString = line.split(" ");
            if (line.equals("Done")) {
                System.out.println("A wurde eingegeben");
            }
            else if (splittetString[0].equals("ships")) {
                System.out.println("Schiffe wurden uebergeben");
                for(int i = 1; i < splittetString.length; i++){
                    switch(splittetString[i]){
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
                guiController.erstelleSteuerung(size, schiffe);
            }
            else{
                for(int i = 0; i<splittetString.length;i= i + 2){
                    analyze(splittetString[i], splittetString[i+1]);
                }
            }
            
	    if (line == null){
                System.out.println("Line in null");
                break;
            };
	}

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
        
        switch(channel){
            case "size":
                size = Integer.valueOf(value);
                send("Done");
            case "save":
                send("speichern");
            case "load":
                send("laden");
        }
         
    }
}
 