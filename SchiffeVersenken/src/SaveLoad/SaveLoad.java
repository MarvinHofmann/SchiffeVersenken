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
    private int[] styp = new int[4];
    private int[][] getroffenAr;
    private int[][] getroffenGeg;
    private int[][] gridLinksArr;
    private int[][] gridRechtsArr;
    private int[] paramInc = new int[5]; //Haben definierte Länge
    private int[][] getroffenKi;
    private int[] letzterSchussKi = new int[2];
    private int[] angefSchiffKi = new int[2];
    private int[] kiValues = new int[3];

    public SaveLoad() {

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

                    int[] paramInc = new int[5]; //Haben definierte Länge

                    paramInc = (int[]) in.readObject();

                    if (paramInc[1] == 1) {
                        ladeLokal(save);
                    } else if (paramInc[1] == 31 || paramInc[1] == 32) {
                        ladeOnline(save);
                    } else if (paramInc[1] == 21 || paramInc[1] == 22) {
                        ladeKiSpiel(save);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Laden fertig");
    }

    public void ladeOnline(File saveFile) {

    }

    public void ladeLokal(File saveFile) {
        System.out.println("ladelokales Spiel");
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            paramInc = (int[]) in.readObject(); //1.
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
            in.close();
            fileIn.close();
            System.out.println("Lade Lokal");
        } catch (Exception e) {

        }

    }

    public void ladeKiSpiel(File saveFile) {

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

        //System.out.println("bin hier");
    }

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
        int[] kiValues = {s.getKIGegner().getAnzGetroffen(), s.getKiGegner().getRichtungKi(), s.getKiGegner().getAngefangenesSchiff()};

        try {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(param);
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

    private void saveOnline(SpielGUIController gui, controll.OnlineSpielSteuerung s, File file) {
        int ip = ipToInt(gui.getIp());

        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), ip, s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
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

    private String parseFileName(String file) {
        String filename = file.replace(":", "");
        filename = filename.replace(".", "");

        return filename;
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

}
