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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SaveLoad {

    private FileChooser fc = new FileChooser();
    private long[] id = new long[1]; //Eindeutige ID
    private int[] styp = new int[4]; //Schifftyp array 0-typ2 usw. Und jeweilige menge davon
    private int[][] getroffenAr; //Markier Array wo getroffen und wasser für eigene Schiffe
    private int[][] getroffenGeg; //Markier Array wo getroffen und wasser für gegner Schiffe
    private int[][] gridLinksArr; //Spielfeld Links mit Ids aus der Schiffe und typ abgeleitet werden
    private int[][] gridRechtsArr; //Spielfeld Rechts mit Ids aus der Schiffe und typ abgeleitet werden
    private int[] paramInc = new int[5]; //Wichtige Parameter wie größe, modus..
    private int[][] getroffenKi; //Markier Array wo getroffen und wasser für KI Schiffe
    //Ki Spezifische Parameter
    private int[] letzterSchussKi = new int[2]; 
    private int[] angefSchiffKi = new int[2];
    private int[] kiValues = new int[4];
    //Online Spiel Parameter
    private int[] onlineValues = new int[2];
    private String[] ipAdress = new String[1];

    /**
     * Handelt das Laden eines Lokalen Spiels oder das des Host eines Online Spiels
     * @return true wenn alles funktioniert hat, false wenn nicht 
     */
    public boolean starteLaden() {
        //oeffne den Filechooser in Dokumente
        fc.setInitialDirectory(new File("c:\\Users\\Public\\Documents"));
        fc.setTitle("Laden"); //Titel des FC ist Laden
        fc.setInitialFileName(parseFileName(LocalDateTime.now().toString())); //Default name ist Uhrzeit
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
                    //Überprüfe den Modus und lade dementsprechend
                    if (paramInc[1] == 1) {
                        ladeLokal(save);
                    } else if (paramInc[1] == 31 || paramInc[1] == 32) {
                        ladeOnline(save);
                    } else if (paramInc[1] == 21 || paramInc[1] == 22) {
                        // Lade nichts da KI Spiel nicht geladen werden kann
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
     * Handelt das Laden fuer den Client, hier muss das File im abgelegten Ordner gesucht werden und die Id des Servers mit dem 
     * Dateinamen ueberprueft werden
     * @param id ist die ID des spiels
     * @return true wenn Laden der Datei Funktioniert hat, false wenn nicht 
     */
    public boolean startLadenOnline(long id) {
        File f = new File("c:\\Users\\Public\\Documents\\" + id + ".dat");
        if (f.exists() && !f.isDirectory()) { //Wenn gewählte Datei richtig
            ladeOnline(f);
        } else {
            //Beenden des Programms, wenn Datei nicht gefunden wurde -> Kommunikationsprotokoll
            System.err.println("Warning: Die Datei wurde nicht gefunden, dass Spiel wird beendet");
            System.exit(-1);
        }
        return false;
    }

    /**
     * Funktion zum Lesen aus der .dat Datei, bei einem OnlineSpiel
     * @param saveFile File aus dem gelesen werden soll
     */
    public void ladeOnline(File saveFile) {
        System.out.println("lade online Spiel");
        try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            paramInc = (int[]) in.readObject(); //1.
            id = (long[]) in.readObject();
            //Init alle Arrays die mit dem grid zu tun haben werden mit der Spielfeldgröße
            //gespeichert in param[0] Initialisiert
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
            onlineValues = (int[]) in.readObject(); //7. Spezielle Online Attribute
            ipAdress = (String[]) in.readObject(); //7. Ip Adresse des Host
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
     * Handelt das Speichern eines Lokalen oder Online Host Spiels
     * @param gui Spielgui fuer den Modus 
     * @param s Die Spielsteuerung lokal oder online
     * @return true fuer erfolg false, wenn nicht erfolgreich
     */
    public boolean speicherSpiel(SpielGUIController gui, controll.SpielSteuerung s) {
        //FileChooser Setup
        fc.setInitialDirectory(new File("C:"));
        fc.setTitle("Speichern");
        fc.setInitialFileName(parseFileName(LocalDateTime.now().toString()));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("dat file", "*.dat"));

        try {
            File save = fc.showSaveDialog(schiffeversenken.SchiffeVersenken.getApplicationInstance().getStage().getScene().getWindow());
            if (save != null) {
                System.out.println(gui.getModus());
                if (gui.getModus() == 1) { //lokales Spiel speichern
                    saveLocal(gui, (LokalesSpielSteuerung) s, save);
                } else if (gui.getModus() == 31 || gui.getModus() == 32) { //online SPiel speichern
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
     * Speichern des Clients bei eingehender save id Nachricht des Servers, speichert 
     * automatisch an den Oeffentlichen Benuzer mit id.dat
     * @param gui Spielgui 
     * @param s Spielsteuerung (OnlineSpielSteuerung)
     * @return true bei erfolgreichem Speichern, false wenn nicht
     */
    public boolean speicherOnlineClient(SpielGUIController gui, controll.SpielSteuerung s) {
        try {
            //Speichere die Daten vom Client mit id als name in public documents als .dat Datei
            File spielStandDatei = new File("c:\\Users\\Public\\Documents\\" + id[0] + ".dat");
            spielStandDatei.createNewFile();
            if (spielStandDatei.exists()) {
                //Ausgabe für konsolen benutzer
                System.out.println("Die Datei wurde in " + spielStandDatei.getAbsolutePath() + " erstellt");
            }
            saveOnline(gui, (OnlineSpielSteuerung) s, spielStandDatei);
        } catch (Exception e) {
            //Speichern hat nicht funktioniert
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
        //Alle Werte werden in Arrays geschrieben
        int[] param = {s.getSpielfeldgroesse(), gui.getModus(), s.getKIGegner().getKiStufe(), s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()}; //wichtige Parameter
        int[] sTyp = s.getAnzahlSchiffeTyp(); //Anzahl der Schiffe von welchem Typ
        int[][] getr = s.getGetroffen(); //Speichert wo hin geschossen und getroffen wurde
        int[][] getrGeg = s.getGetroffenGegner(); // getroffen array gegner
        int[][] gridLinks = makeInt(s.getGridSpielfeldLinks().getGrid()); //Speichert eigene Schiffe und patzierung
        int[][] gridRechts = makeInt(s.getKIGegner().getGridSpielfeldLinks().getGrid()); //Gegner Schiffe und platzierung
        int[][] getroffenKi = s.getKIGegner().getGetroffenKi(); //speichert wo KI getroffen hat
        int[] letzterSchussKi = s.getKIGegner().getLetzterSchuss(); //Speichert den letzten schuss der KI
        int[] angefSchiffKi = s.getKIGegner().getAngefangenesSchiffSchuss(); 
        int[] kiValues = {s.getKIGegner().getAnzGetroffen(), s.getKiGegner().getRichtungKi(), s.getKiGegner().getAngefangenesSchiff(), s.getKIGegner().getKiStufe()}; //Spezielle ki parameter
        long[] l = {getFileID()}; //id der Speicherdatei
        try {
            //Alle Arrays werden in einer bestimmten Reihenfolge, die beim lesen wichtig ist in die übergebene Datei geschrieben
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
            //Fehlerfall
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
        ipAdress[0] = gui.getIp();
        id[0] = getFileID();
        System.out.println("AnzGetroffen: " + s.getAnzGetroffen());
        int[] param = {s.getSpielfeldgroesse(), gui.getModus(),s.getAnzGetroffen(), s.getEigeneSchiffeGetroffen()};
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
            objOut.writeObject(ipAdress);
            objOut.close();
        } catch (Exception e) {
            System.out.println("hier ist ein Fehler aufgetreten");
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Erstellt aus einem Rectangle Array ein int[][] Array mit dem Inhalt der Ids des Rectangle
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
   
    /**
     * Erstellt Id mit Datentyp long aus einer Zufallszahl
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
    
    /**
     * Bekommt einen Filenamen und entfernt alle : und . zum Speichern
     * @param fileName - String filename
     * @return filename ohne : und .
     */
    private String parseFileName(String fileName) {
        String filename = fileName.replace(":", "");
        filename = filename.replace(".", "");
        return filename;
    }
    
    
    //Getter und Setter
    
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

    public String[] getIp() {
        return ipAdress;
    }
}
