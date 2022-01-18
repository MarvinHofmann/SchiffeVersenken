/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SaveLoad;

import GUI.ModiMenueController;
import GUI.SpielGUIController;
import controll.KISpielSteuerung;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import controll.SpielSteuerung;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 *
 * @author TDoes
 */
public class SaveLoad {

    private FileChooser fc = new FileChooser();

    public SaveLoad() {

    }

    public static void write2File(File file, String content) {
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(content);
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void readFromFile(File file) {

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void starteLaden(ModiMenueController controller) {
        fc.setInitialDirectory(new File("c:"));
        fc.setTitle("Laden");
        fc.setInitialFileName(LocalDateTime.now().toString());
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat file", "*.dat"));

        try {
            File save = fc.showOpenDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
            if (save != null) {
                 try {                     
                        FileInputStream fileIn = new FileInputStream(save);
                        ObjectInputStream in = new ObjectInputStream(fileIn);
            //Nacheinander lesen, reihenfolge wichtig 
            //in Param als erstes Modus, da hier entschieden werden muss;
            
                        int[] paramInc = new int[6]; //Haben definierte Länge
              
                        paramInc = (int[]) in.readObject(); 
                        
                        if(paramInc[0] == 1){
                            ladeLokal(save);
                        }
                        else if(paramInc[0] == 31 || paramInc[0] ==32){
                            ladeOnline(save);
                        }
                        else if(paramInc[0] == 21 || paramInc[0] ==22){
                            ladeKiSpiel(save);
                        }
                        
                        
                        in.close();
                        fileIn.close();
                        
                 }
                 catch(Exception e){
                 }
            }
        } catch (Exception e) {

        }
      
        /*    if (paramInc[0] == 1) {//lokales Spiel laden
                
            } else if (paramInc[0] == 2) {

            }

            
        } catch (Exception i) {
            System.out.println(i);
            
        }
        */
    }

    public void ladeOnline(File saveFile) {

    }

    public void ladeLokal(File saveFile) {
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
                        
            int[] paramInc = new int[6]; //Haben definierte Länge
              
            paramInc = (int[]) in.readObject();
        
            int[] typ = new int[4];
            int[][] getroffenAr;
            int[][] getroffenGeg;
            int[][] gridLinksArr;
            int[][] gridRechtsArr;
        
            typ = (int[]) in.readObject();
            getroffenAr = new int[paramInc[0]][paramInc[0]];
            gridLinksArr = new int[paramInc[0]][paramInc[0]];
            gridRechtsArr = new int[paramInc[0]][paramInc[0]];
            getroffenGeg = new int[paramInc[0]][paramInc[0]];
            getroffenAr = (int[][]) in.readObject();
            getroffenGeg = (int[][]) in.readObject();
            gridLinksArr = (int[][]) in.readObject();
            gridRechtsArr = (int[][]) in.readObject();
            
            
            
            in.close();
            fileIn.close();
        }
        catch(Exception e){
            
        }
        
        
    }
    
    public void ladeKiSpiel(File saveFile){
        
    }

    public void speicherSpiel(SpielGUIController gui, controll.SpielSteuerung s) {
        
        //FileChooser Setup
        fc.setInitialDirectory(new File("C:"));
        fc.setTitle("Speichern");
        String filename = parseFileName(LocalDateTime.now().toString()); 
        fc.setInitialFileName(filename);        
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat file", "*.dat"));
        
        
        try {
            File save = fc.showSaveDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
            if (save != null) {
                if (gui.getModus() == 1) { //lokales Spiel speichern
                    saveLocal(gui, (LokalesSpielSteuerung) s, save);
                } else if (gui.getModus() == 31 || gui.getModus() == 32) { //online SPiel speichern
                    saveOnline(gui, (OnlineSpielSteuerung) s, save);
                } else {
                    saveKi();
                }
            }
        } catch (Exception e) {
              System.out.println(e);
        }

        System.out.println("bin hier");

    }

    private void saveLocal(SpielGUIController gui, controll.LokalesSpielSteuerung s, File file) {
        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), s.getKIGegner().getKiStufe(), s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
        int[] sTyp = s.getAnzahlSchiffeTyp();
        int[][] getr = s.getGetroffen();
        int[][] getrGeg = s.getGetroffenGegner(); // getroffen array gegner
        int[][] gridLinks = makeInt(s.getGridSpielfeldLinks().getGrid());
        int[][] gridRechts = makeInt(s.getKIGegner().getGridSpielfeldLinks().getGrid());

        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(param);
            objOut.writeObject(sTyp);
            objOut.writeObject(getr);
            objOut.writeObject(getrGeg);
            objOut.writeObject(gridLinks);
            objOut.writeObject(gridRechts);
            objOut.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void saveOnline(SpielGUIController gui, controll.OnlineSpielSteuerung s, File file) {
        int ip = ipToInt(gui.getIp());
    
        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), ip, s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
        int[] sTyp = s.getAnzahlSchiffeTyp();
        int[][] getr = s.getGetroffen();
        
    }

    private int ipToInt(String ip) {
        int ipInteger;
        String[] ipOhnePunkte = ip.split(".");
        String ipString = "";
        for (int i = 0; i < ipOhnePunkte.length; i++) {
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

    private void saveKi() {

    }
    
    private String parseFileName(String file){
        String filename = file.replace(":", "");
        filename = filename.replace(".", "");
        
        return filename;
    }
}
