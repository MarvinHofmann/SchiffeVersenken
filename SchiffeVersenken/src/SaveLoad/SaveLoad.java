/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SaveLoad;

import GUI.ModiMenueController;
import GUI.SpielGUIController;
import controll.SpielSteuerung;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author TDoes
 */
public class SaveLoad {
    public static void write2File(File file, String content){
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(content);
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void readFromFile(File file){
        try {
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
            sc.close();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void starteLaden(ModiMenueController controller){
        int[] paramInc = new int[6]; //Haben definierte Länge
        int[] typ = new int[4];
        int[][] getroffenAr;
        int[][] getroffenGeg;
        int[][] gridLinksArr;
        int[][] gridRechtsArr;
        try {
            FileInputStream fileIn = new FileInputStream("speicher.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //Nacheinander lesen, reihenfolge wichtig 
            //in Param als erstes Modus, da hier entschieden werden muss;
            paramInc = (int[]) in.readObject();
            
            if(paramInc[0]== 1){//lokales Spiel laden
                typ = (int[]) in.readObject();
                getroffenAr = new int[paramInc[0]][paramInc[0]];
                gridLinksArr = new int[paramInc[0]][paramInc[0]];
                gridRechtsArr = new int[paramInc[0]][paramInc[0]];
                getroffenGeg = new int[paramInc[0]][paramInc[0]];
                getroffenAr = (int[][]) in.readObject();
                getroffenGeg = (int[][]) in.readObject();
                gridLinksArr = (int[][]) in.readObject();
                gridRechtsArr = (int[][]) in.readObject();
            }
            else if(paramInc[0]==2){
                
            }
            
            in.close();
            fileIn.close();
        } catch (Exception i) {
            System.out.println(i);
            return;
        }
    }
    
    
    public void ladeOnline(){
        
    }
    
    public void ladeLokal(){
        
    }
    
    public void speicherSpiel(SpielGUIController gui, controll.SpielSteuerung s){
        System.out.println("bin hier");
        int[] param = {s.getSpielfeldgroesse(), s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};//modus, fehlt noch und kiStufe, auch
        int[] sTyp = s.getAnzahlSchiffeTyp();
        int[][] getr = s.getGetroffen();
        int[][] getrGeg = s.getGetroffenGegner(); // getroffen array gegner
        //int[][] gridLinks = makeInt(s.getGridSpielfeldLinks().getGrid());
        //int[][] gridRechts = makeInt(s.getKIGegner().getGridSpielfeldLinks().getGrid());
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("speicher.dat"));
            objOut.writeObject(param);
            objOut.writeObject(sTyp);
            objOut.writeObject(getr);
            objOut.writeObject(getrGeg);
            //objOut.writeObject(gridLinks);
            //objOut.writeObject(gridRechts);
            objOut.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    private void saveLocal(){
        
    }
    
    private void saveOnline(){
        
    }
    
    private int ipToInt(String ip){
        int ipInteger;
        String[] ipOhnePunkte = ip.split(".");
        String ipString = "";
        for(int i = 0; i < ipOhnePunkte.length;i++){
            ipString += ipOhnePunkte[i];
        }
        
        ipInteger = Integer.valueOf(ipString);
        System.out.println("ipString: " + ipOhnePunkte);
        return 0;
    }
    
    public int[][] makeInt(Rectangle[][] g) {
        int[][] save = new int[g.length][g.length];
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g.length; j++) {
                save[i][j] = Integer.valueOf(g[i][j].getId());
            }
        }
        return save;
    }
}
