/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SaveLoad;

import GUI.SpielGUIController;
import controll.LokalesSpielSteuerung;
import controll.OnlineSpielSteuerung;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 *
 * @author TDoes
 */
public class SaveLoad {

    private FileChooser fc = new FileChooser();
    private long[] id = new long[1];
    private int[] styp = new int[4];
    private int[][] getroffenAr;
    private int[][] getroffenGeg;
    private int[][] gridLinksArr;
    private int[][] gridRechtsArr;
    private int[] paramInc = new int[5]; //Haben definierte Länge
    private int[][] getroffenKi;
    private int[] letzterSchussKi = new int[2];
    private int[] angefSchiffKi = new int[2];
    private int[] kiValues = new int[4];
    private int ip;
    private int[] onlineValues = new int[2];

    public SaveLoad() {

    }

    /**
     * Handelt das laden eines Lokalen Spiels oder das des Host eines Online Spiels
     * @return true wenn alles funktioniert hat, false wenn nicht 
     */
    public boolean starteLaden() {
        //oeffne den Filechooser in Dokumente
        fc.setInitialDirectory(new File("c:\\Users\\Public\\Documents"));
        fc.setTitle("Laden"); //Titel des FC ist Laden
        fc.setInitialFileName(LocalDateTime.now().toString()); //Default name ist Uhrzeit
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat file", "*.dat")); //setzte nur auf .dat Dateien als Typ
        //veruche File zu erstellen und dann zu schreiben
        try {
            File save = fc.showOpenDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
            if (save != null) {
                try {
                    FileInputStream fileIn = new FileInputStream(save);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    //Nacheinander lesen, reihenfolge wichtig 
                    //in Param als erstes Modus, da hier entschieden werden muss;

                    paramInc = (int[]) in.readObject();
                    id = (long[]) in.readObject();

                    if (paramInc[1] == 1) {
                        ladeLokal(save);
                    } else if (paramInc[1] == 31 || paramInc[1] == 32) {
                        ladeOnline(save);
                    } else if (paramInc[1] == 21 || paramInc[1] == 22) {
                        //ladeKiSpiel(save);
                    }
                    return true; // Hat funktioniert
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("kein dat file oder nichts gewählt");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Laden fertig");
        return false;
    }

    /**
     * Handelt das Laden fuer den Client hier muss das File im abgelegten Ordner gesucht werden und die Id des Servers mit dem 
     * Dateinamen ueberprueft werden
     * @param id ist die ID des spiels
     * @return true wenn es Funktioniert hat, false wenn nicht 
     */
    public boolean startLadenOnline(long id) {
        File f = new File("c:\\Users\\Public\\Documents\\" + id + ".dat");
        if (f.exists() && !f.isDirectory()) { //Wenn gewählte Datei richtig
            ladeOnline(f);
        } else {
            System.out.println("Beende Spiel Datei nicht gefunden");
        }
        return false;
    }

    /**
     * Funktion zum lesen aus der .dat Datei, bei einem OnlineSpiel
     * @param saveFile File aus dem gelesen werden soll
     */
    public void ladeOnline(File saveFile) {
        System.out.println("lade online Spiel");
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            paramInc = (int[]) in.readObject(); //1.
            id = (long[]) in.readObject();
            //Init
            getroffenAr = new int[paramInc[0]][paramInc[0]];
            gridLinksArr = new int[paramInc[0]][paramInc[0]];
            gridRechtsArr = new int[paramInc[0]][paramInc[0]];
            getroffenGeg = new int[paramInc[0]][paramInc[0]];
            getroffenKi = new int[paramInc[0]][paramInc[0]];
            //
            styp = (int[]) in.readObject(); //2.
            getroffenAr = (int[][]) in.readObject(); //3.
            getroffenGeg = (int[][]) in.readObject(); //4.
            gridLinksArr = (int[][]) in.readObject(); //5.
            gridRechtsArr = (int[][]) in.readObject(); //6.
            onlineValues = (int[]) in.readObject(); //7.
            in.close();
            fileIn.close();
            System.out.println(paramInc[2]);
            System.out.println("Lade online");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funktion zum Lesen aus der Datei bei einem Lokalen Spiel
     * @param saveFile File aus dem gelesen werden soll
     */
    public void ladeLokal(File saveFile) {
        System.out.println("ladelokales Spiel");
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            paramInc = (int[]) in.readObject(); //1.
            id = (long[]) in.readObject();
            //Init
            getroffenAr = new int[paramInc[0]][paramInc[0]];
            gridLinksArr = new int[paramInc[0]][paramInc[0]];
            gridRechtsArr = new int[paramInc[0]][paramInc[0]];
            getroffenGeg = new int[paramInc[0]][paramInc[0]];
            getroffenKi = new int[paramInc[0]][paramInc[0]];
            //
            styp = (int[]) in.readObject(); //2.
            getroffenAr = (int[][]) in.readObject(); //3.
            getroffenGeg = (int[][]) in.readObject(); //4.
            gridLinksArr = (int[][]) in.readObject(); //5.
            gridRechtsArr = (int[][]) in.readObject(); //6.
            getroffenKi = (int[][]) in.readObject(); //7.
            letzterSchussKi = (int[]) in.readObject(); //8.
            angefSchiffKi = (int[]) in.readObject(); //9.
            kiValues = (int[]) in.readObject(); //10.

            System.out.println(id[0]);
            in.close();
            fileIn.close();
            System.out.println("Lade Lokal");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    /**
     * handelt das Speichern eines Lokalen oder Online Host spiels
     * @param gui Spielgui fuer den Modus 
     * @param s Die Spielsteuerung lokal oder Online
     * @return true fuer erfolg false, wenn nicht erfolgreich
     */
    public boolean speicherSpiel(SpielGUIController gui, controll.SpielSteuerung s) {
        //FileChooser Setup
        fc.setInitialDirectory(new File("C:"));
        fc.setTitle("Speichern");
        String filename = "Spielstand";
        fc.setInitialFileName(filename);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat file", "*.dat"));

        try {
            File save = fc.showSaveDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
            if (save != null) {
                if (gui.getModus() == 1) { //lokales Spiel speichern
                    saveLocal(gui, (LokalesSpielSteuerung) s, save);
                } else if (gui.getModus() == 31) { //online SPiel speichern
                    saveOnline(gui, (OnlineSpielSteuerung) s, save);
                }
                return true;
            } else {
                System.out.println("dialog abgebrochen");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Speichern");
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

    /**
     * Speichern des Clients bei eingehender save id nachricht des Servers, speichert 
     * automatisch an den Oeffentlichen Benuzer mit id.dat
     * @param gui Spielgui 
     * @param s Spielsteuerung (OnlineSpielSteuerung)
     * @return true bei erfolg, false wenn nicht 
     */
    public boolean speicherOnlineClient(SpielGUIController gui, controll.SpielSteuerung s) {
        try {
            File fest = new File("c:\\Users\\Public\\Documents\\" + id[0] + ".dat");
            fest.createNewFile();
            if (fest.exists()) {
                System.out.println("FILE wurde erstellt ");
                System.out.println(fest.getAbsolutePath());
            }
            saveOnline(gui, (OnlineSpielSteuerung) s, fest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Funtkion fuer Schreiben in einer Datei bei Lokalem Spiel
     * @param gui fuer Spielmodus
     * @param s Loakale Spielsteuerung fuer alle wichtigen Parameter des Spiels
     * @param file File in das geschrieben werden soll
     */
    private void saveLocal(SpielGUIController gui, controll.LokalesSpielSteuerung s, File file) {

        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), s.getKIGegner().getKiStufe(), s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
        int[] sTyp = s.getAnzahlSchiffeTyp();
        int[][] getr = s.getGetroffen();
        int[][] getrGeg = s.getGetroffenGegner(); // getroffen array gegner
        int[][] gridLinks = makeInt(s.getGridSpielfeldLinks().getGrid());
        int[][] gridRechts = makeInt(s.getKIGegner().getGridSpielfeldLinks().getGrid());
        int[][] getroffenKi = s.getKIGegner().getGetroffenKi();
        int[] letzterSchussKi = s.getKIGegner().getLetzterSchuss();
        int[] angefSchiffKi = s.getKIGegner().getAngefangenesSchiffSchuss();
        int[] kiValues = {s.getKIGegner().getAnzGetroffen(), s.getKiGegner().getRichtungKi(), s.getKiGegner().getAngefangenesSchiff(), s.getKIGegner().getKiStufe()};
        long[] l = {getFileID()};
        System.out.println(l[0]);
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(param);
            objOut.writeObject(l);
            objOut.writeObject(sTyp);
            objOut.writeObject(getr);
            objOut.writeObject(getrGeg);
            objOut.writeObject(gridLinks);
            objOut.writeObject(gridRechts);
            objOut.writeObject(getroffenKi);
            objOut.writeObject(letzterSchussKi);
            objOut.writeObject(angefSchiffKi);
            objOut.writeObject(kiValues);
            objOut.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Funktion zum eigentlichen Schreiben in der Datei
     * @param gui SpielGuiController
     * @param s OnlineSpielSteuerung
     * @param file File in das geschrieben werden soll
     */
    private void saveOnline(SpielGUIController gui, controll.OnlineSpielSteuerung s, File file) {
        int ip = 0;
        if (!gui.getIp().equals("")) {
            ip = ipToInt(gui.getIp());
        }
        id[0] = getFileID();
        System.out.println("AnzGetroffen: " + s.getAnzGetroffen());
        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), ip, s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
        for (int i = 0; i < param.length; i++) {
            System.out.println(param[i]);
        }
        System.out.println(param[2]);
        int[] sTyp = s.getAnzahlSchiffeTyp();
        int[][] getr = s.getGetroffen();
        int[][] getrGeg = s.getGetroffenGegner(); // getroffen array gegner
        int[][] gridLinks = makeInt(s.getGridSpielfeldLinks().getGrid());
        int[][] gridRechts = makeInt(s.getGridSpielfeldRechts().getGrid());
        int[] onlineValues = {s.getEigeneSchiffeGetroffen(), s.getAktiverSpieler()};
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(param);
            objOut.writeObject(id);
            objOut.writeObject(sTyp);
            objOut.writeObject(getr);
            objOut.writeObject(getrGeg);
            objOut.writeObject(gridLinks);
            objOut.writeObject(gridRechts);
            objOut.writeObject(onlineValues);
            objOut.close();
        } catch (Exception e) {
            System.out.println("hier ist ein Fehler aufgetreten");
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Macht aus dem String Ip Adresse eine Integer Ip Adresse zum speichern
     * @param ip String IP Adresse
     * @return Integer wert der IP Adresse
     */
    private int ipToInt(String ip) {
        int ipInteger;
        String[] ipOhnePunkte = ip.split("\\.");
        String ipString = "";
        for (int i = 0; i < ipOhnePunkte.length; i++) {
            if(ipOhnePunkte[i].length() == 3){
                ipString += ipOhnePunkte[i];
            }
            else if(ipOhnePunkte[i].length()==2){
                ipString += "0" + ipOhnePunkte[i];
            }
            else if(ipOhnePunkte[i].length()==1){
                ipString += "00" + ipOhnePunkte[i];
            }
        }
        ipInteger = Integer.valueOf(ipString);
        System.out.println("ipString: " + ipInteger);
        return ipInteger;
    }

    /**
     * erstellt aus einm Rectangle Array ein int[][] array mit dem Inhalt der Ids des Rectangle
     * int[][] arr kann gespeichert werden
     * @param g Rectangle[][] Array welches umgewandelt wird
     * @return int[][] Array 
     */
    public int[][] makeInt(Rectangle[][] g) {
        int[][] save = new int[g.length][g.length];
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g.length; j++) {
                save[i][j] = Integer.valueOf(g[i][j].getId());
            }
        }
        return save;
    }

    public int getIp() {
        return ip;
    }

    public int[] getStyp() {
        return styp;
    }

    public int[][] getGetroffenAr() {
        return getroffenAr;
    }

    public int[][] getGetroffenGeg() {
        return getroffenGeg;
    }

    public int[][] getGridLinksArr() {
        return gridLinksArr;
    }

    public int[][] getGridRechtsArr() {
        return gridRechtsArr;
    }

    public int[] getParamInc() {
        return paramInc;
    }

    public int[][] getGetroffenKi() {
        return getroffenKi;
    }

    public int[] getLetzterSchussKi() {
        return letzterSchussKi;
    }

    public int[] getAngefSchiffKi() {
        return angefSchiffKi;
    }

    public int[] getKiValues() {
        return kiValues;
    }

    public long[] getL() {
        return id;
    }

    public int[] getOnlineValues() {
        return onlineValues;
    }
    
    /**
     * erstellt Id mit Datentyp long aus einer Zufallszahl
     * @return zufaellig generierte id
     */
    private long getFileID() {
        long leftborder = (long) Math.pow(2, 63);
        long rightborder = (long) Math.pow(2, 32);
        long id = leftborder + (long) (Math.random() * (rightborder - leftborder));
        return id;
    }

    /**
     * Setzt die Id nach uebergebener load Nachricht des Servers von String zu long
     * @param incLong String der Zahl
     */
    public void setId(String incLong) {
        long l = Long.parseLong(incLong);
        id[0] = l;
    }
}
