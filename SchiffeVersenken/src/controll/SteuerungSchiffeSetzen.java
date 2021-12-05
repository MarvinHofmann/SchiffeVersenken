/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SteuerungSchiffeSetzen {

    private SpielGUIController dieGui = null;

    //Spielfeld => 600x600
    private int[] schiffTypen;
    private int anzSchiffe = 0;
    private Schiff[] schiffArray;
    private Grid gridSpielfeld;
    private boolean fertig = true;

    public boolean isFertig() {
        return fertig;
    }

    public SteuerungSchiffeSetzen(GUI.SpielGUIController gui, int[] anzahlSchiffeTyp) {
        System.out.println("SteuerungSchiffeSetzen erzeugt");
        this.dieGui = gui;
        this.schiffTypen = anzahlSchiffeTyp;
        for (int i = 0; i < schiffTypen.length; i++) {
            anzSchiffe += schiffTypen[i];
        }
    }

    public SpielGUIController getDieGui() {
        return dieGui;
    }

    public void setDieGui(SpielGUIController dieGui) {
        this.dieGui = dieGui;
    }

    public int[] getSchiffTypen() {
        return schiffTypen;
    }

    public void setSchiffTypen(int[] schiffTypen) {
        this.schiffTypen = schiffTypen;
    }

    public int getAnzSchiffe() {
        return anzSchiffe;
    }

    public void setAnzSchiffe(int anzSchiffe) {
        this.anzSchiffe = anzSchiffe;
    }

    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public void setSchiffArray(Schiff[] schiffArray) {
        this.schiffArray = schiffArray;
    }

    public Grid getGridS() {
        return gridSpielfeld;
    }

    public void setGridS(Grid gridSpielfeld) {
        this.gridSpielfeld = gridSpielfeld;
    }

    public void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        this.schiffTypen = anzahlSchiffeTyp;
        for (int i = 0; i < schiffTypen.length; i++) {
            anzSchiffe += schiffTypen[i];
        }
        /*DEBUG
        for (int i = 0; i < schiffTypen.length; i++) {
            System.out.println(schiffTypen[i]);
        }
        */
        drawAll(spielfeldgroesse);
    }

    /**
     * Zeichnet das Spielfeld in die GUI
     * @param gr ist die größé des Spielfelds
     */
    public void drawAll(int gr) {
        //Erzeuge neues Grid Objekt mit zweitem Konstruktor 
        System.out.println("Server " + dieGui.getServer());
        gridSpielfeld = new Grid(gr, dieGui.getServer());
        gridSpielfeld.macheGrid(); //Erstelle 2Dim Array aus Rectangle
        //Grid Zeichnen rectangle kacheln dem Pane hinzufügen
        for (int i = 0; i < gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(gridSpielfeld.getGrid()[i][j]);
            }
        }
        macheSchiffe(); //Erstellt alle Schiffobjekte 
    }

    /**
     * Erstellt die Schiffe, welche im Modi Menü ausgewählt wurden schiffTypen[]
     * beinahltet die Schiffe der verschiedenen Größen welche im Modi Menü
     * ausgewählt worden
     */
    private void macheSchiffe() {
        //Erstelle alle Schiffe und Speichere sie in einem Array voller 
        //Schiffsobjekte ab
        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < schiffTypen[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < schiffTypen[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < schiffTypen[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < schiffTypen[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        //DEBUG
        /*for (int i = 0; i < schiffArray.length; i++) {
            System.out.println(schiffArray[i]);
        }*/
        //
        int m = 0; //Merker um schiffe versetzt auszugeben
        //Alle Schiffe dem Pane als rectangle hinzufügen
        //Die Schiffe auf dem Grid zeichnen und mit einer Zeile abstand im Buffer Ablegen
        for (int i = 0; i < schiffArray.length; i++) {
            dieGui.zeigeSchiffe(schiffArray[i]);
            schiffArray[i].draw(gridSpielfeld.getPxGroesse(), 2 * m);
            makeHandler(schiffArray[i]);
            m = m + gridSpielfeld.getKachelgroeße();
        }
    }

    /**
     * Aktiviert für jedes Schiff den Eventhandler Dragged und Release um mit
     * der Maus zu verschieben
     *
     * @param s jeweiliges Schiff für das der Handler erzeugt werden soll
     */
    private void makeHandler(Schiff s) {
        s.setOnMouseDragged(event -> dragged(event, s));
        s.setOnMouseReleased(event -> released(event, s));
    }

    /**
     * Eventhandler für das Anheben Wenn ein schiff mit Gedrückter Maus
     * angehoben wird kann es im Grid verschoben werden Dabei wird darauf
     * geachte, dass die Schiffe sich nur in den Linien bewegen und das
     * Spielfeld nicht verlassen
     *
     * @param event Mouse Event drag
     * @param s Schiff
     */
    public void dragged(MouseEvent event, Schiff s) {
        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüger
        int snapX = (int) (event.getX() % gridSpielfeld.getKachelgroeße());
        int snapY = (int) (event.getY() % gridSpielfeld.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX() - snapX;
        int y = (int) event.getY() - snapY;
        
        for (int i = x/gridSpielfeld.getKachelgroeße(); i < x/gridSpielfeld.getKachelgroeße() + s.getLaenge(); i++) {
            if (gridSpielfeld.getGrid()[i][y/gridSpielfeld.getKachelgroeße()].getFill() == Color.BROWN) {
                 gridSpielfeld.getGrid()[i][y/gridSpielfeld.getKachelgroeße()].setFill(Color.WHITE);
            }
        }
        
        
        
        boolean blockiert = false;

        //Abfragen, ob das Schiff aus dem Feld fährt und dies Verhindern
        if (x < 0) { //Nach links raus
            blockiert = true;
        } else if (y < 0) { //Nach oben raus
            blockiert = true;
        } else if (x > (1200 - s.getWidth() + 0.5 * gridSpielfeld.getKachelgroeße())) { //nach rechts raus
            blockiert = true;
        } else if (y > (600 - s.getHeight()) + 0.5 * gridSpielfeld.getKachelgroeße()) { //nach unten raus
            blockiert = true;
        } else { //wenn alles ok blokiert = false man kan das Schiff bewegen
            blockiert = false;
        }
        //Wenn nicht blokiert bewege das Schiff
        if (!blockiert) {
            if (x <= (gridSpielfeld.getPxGroesse() - s.getWidth())) {
                s.setFill(Color.GREEN);
            } else { //Wenn das Schiff im Rechten Teil ist setzte es Rot => hier nicht plazieren
                s.setFill(Color.RED);
            }
            //Neu setzten der Schiffwerte
            s.setX(x);
            s.setY(y);
        }

        //Neu zeichnen
        //drawWasser(s, Color.WHITE);
        s.draw();
    }

    /**
     * Setzt alle Felder die nicht als Wasser gezeichnet sind = Weiß
     */
    public void cleanFeld() {
        for (int i = 0; i < gridSpielfeld.getKachelAnzahl(); i++) {
            for (int j = 0; j < gridSpielfeld.getKachelAnzahl(); j++) {
                if (!gridSpielfeld.getGrid()[i][j].getFill().equals(Color.NAVY)) {
                    gridSpielfeld.getGrid()[i][j].setFill(Color.WHITE);
                }
            }
        }
    }

    /**
     * Zeichnet den Wasserrand des Schiffs
     *
     * @param s Schiff
     * @param c Farbe
     */
    public void drawWasser(Schiff s, Color c) {
        int aktX = (int) s.getX() / gridSpielfeld.getKachelgroeße();
        int aktY = (int) s.getY() / gridSpielfeld.getKachelgroeße();

        //Spielfeld cleanen bei jedem draw
        cleanFeld();
        if (aktX < gridSpielfeld.getKachelAnzahl() - s.getWidth() / gridSpielfeld.getKachelgroeße()) {
            for (int i = -1; i < s.getWidth() / gridSpielfeld.getKachelgroeße() + 1; i++) {
                //Von -1 links vom Schiff bis eins rechts vom schiff mache obere und unter linie
                //
                //  [-1 / -1][0 / -1  ][ +1 / -1][+2/-1]
                //  [-1 /  y][SCHIFF_1][SCHIFF_1][schiffbreite / y]
                //  [-1 / +1][0 / +1  ][ +1 / +1][+2/+1]
                //
                if (aktX + i > 0 && aktY - 1 > 0) {
                   gridSpielfeld.getGrid()[aktX + i][aktY + 1].setFill(c);
                   gridSpielfeld.getGrid()[aktX + i][aktY - 1].setFill(c); 
                }
            }
            //Kachel rechts
            gridSpielfeld.getGrid()[aktX + (int) s.getWidth() / gridSpielfeld.getKachelgroeße()][aktY].setFill(c);
            //Kachel links
            if (aktX -1 > 0) {
                gridSpielfeld.getGrid()[aktX - 1][aktY].setFill(c);
            }
        }
    }

    /**
     * Handle den release Zustand Besonders: Snap so wird geschaut, dass immer
     * genau in den Grids gelandet wird und nicht neben/über den Linien
     */
    public void released(MouseEvent event, Schiff s) {
        int puff = (int) (s.getX() / gridSpielfeld.getKachelgroeße());
        int puffY = (int) (s.getY() / gridSpielfeld.getKachelgroeße());
        s.setX(puff * gridSpielfeld.getKachelgroeße());
        s.setY(puffY * gridSpielfeld.getKachelgroeße());
        s.setFill(Color.GREEN);
        s.setStroke(Color.GREEN);
        //Ermittle Koordinatenwert der StartPositionen für 2D Array
        int startX = (int) event.getX() / gridSpielfeld.getKachelgroeße();
        int startY = (int) event.getY() / gridSpielfeld.getKachelgroeße();
        //System.out.println(startX + " " + startY);
        s.setStart(startX, startY);
        
        System.out.println(s.getRichtung());
        System.out.println(s.getLaenge());
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = startX; i < startX + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[i][startY].setFill(Color.BROWN);            
            }
        }else{
            for (int i = startY; i < startY + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[startX][i].setFill(Color.BROWN);
            }
        }
        for (Schiff s1 : schiffArray) {
            if (ueberpruefePlatz(s1)) {
                fertig = true;
                s1.setFill(Color.GREEN);
            }else {
                fertig = false;
                s1.setFill(Color.RED);
            }
        }
       
        s.draw();
    }

    /**
     * Schaut ob der gewählte Platz ok ist
     */
    private boolean ueberpruefePlatz(Schiff s) {
        int x = s.getStartX();
        int y = s.getStartY();
        System.out.println("X: " + x + " Y: "  +y);
        System.out.println(gridSpielfeld.getKachelAnzahl());
        boolean status = true;
        // TODO: schaue ob um das Schiff Herum ein Grid Brown ist => dann bereits Belegt
    
        //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
        if (y - 1 >= 0 && y + 1 <= gridSpielfeld.getKachelAnzahl()-1 && x+s.getLaenge() <= gridSpielfeld.getKachelAnzahl()) { //Überprüfe auf Rand
            System.out.println("Zustand 1: nicht oben/unten drüber oder rechts raus => Mitte");
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (gridSpielfeld.getGrid()[i][y-1].getFill() == Color.BROWN) { //Suche über dem Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[i][y-1].setFill(Color.RED); //DEBUG
                }
                if(gridSpielfeld.getGrid()[i][y+1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    break; //Brche suche ab
                    //gridSpielfeld.getGrid()[i][y+1].setFill(Color.BLUE); //DEBUG
                }
            }
        }
        //2.Links/Rechts neben dem Schiff
        if (x-1 >= 0 && x + 1 <= gridSpielfeld.getKachelAnzahl()-s.getLaenge() && y - 1 >= 0 && y+1 <= gridSpielfeld.getKachelAnzahl()-1) { //Randüberprüfung
            System.out.println("Zustand 2: nicht links rechts raus drüber/drutnter raus => Mitte");
            for (int i = y-1; i <y-1 + 3; i++) {    
                if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                }
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    break; //Brche suche ab
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                }
            }
        }
        //3. Randfälle
        if (y-1 <= 0 && x -1 >= 0 && x+s.getLaenge() <=gridSpielfeld.getKachelAnzahl()-1) { //Am Oberen Rand Zeichnen
            System.out.println("Zustand 3: wenn oben raus:");
            for (int i = x; i < x + s.getLaenge(); i++) {
                if(gridSpielfeld.getGrid()[i][y+1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    break; //Brche suche ab
                    //gridSpielfeld.getGrid()[i][y+1].setFill(Color.BLUE); //DEBUG
                }
            }
            for (int i = y; i <y + 2; i++) {    
                    if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                }
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    break; //Brche suche ab
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                }
            }
        }if (x - 1 <= 0 && y-1>=0 && y+1<=gridSpielfeld.getKachelAnzahl()-1) { //Am linken Rand zeichen
            System.out.println("Zustand 3: wenn links raus und nicht unten raus:");
            for (int i = y-1; i <y-1 + 3; i++) {    
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    break; //Brche suche ab
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                }
            }
        }if (x + 1 == gridSpielfeld.getKachelAnzahl() -s.getLaenge() +1 && y-1>=0 && y+1<=gridSpielfeld.getKachelAnzahl()-1) { //Wenn rechts raus zeiche zusätlich noch links 
            System.out.println("Zustand 4: Wenn rechts raus und nicht oben oder unten");
            for (int i = y-1; i <y-1 + 3; i++) {    
                if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                }
            }
        }if (y+1 >= gridSpielfeld.getKachelAnzahl()-1 && x-1>=0 && x+s.getLaenge() <=gridSpielfeld.getKachelAnzahl()-1) { //Am unteren Rand 
            System.out.println("Zustand 5: Wenn  Unten raus ");
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (gridSpielfeld.getGrid()[i][y-1].getFill() == Color.BROWN) { //Suche über dem Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[i][y-1].setFill(Color.RED); //DEBUG
                }  
            }
            for (int i = y-1; i <y-1 + 2; i++) {    
                    if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    break; //Breche ab
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                }
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                    break; //Brche suche ab
                }
            }
        }
        //Linke obere Ecke
        if (x-1<=0 && y-1<=0) {
            for (int i = y; i <y + 2; i++) {    
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                    break; //Brche suche ab
                }
            }for (int i = x; i < x + s.getLaenge(); i++) {
                if(gridSpielfeld.getGrid()[i][y+1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[i][y+1].setFill(Color.BLUE); //DEBUG
                    break; //Brche suche ab
                }
            }
        }
        //Rechte obere Ecke
        if (x + 1 == gridSpielfeld.getKachelAnzahl() -s.getLaenge() +1 && y-1<=0) {
            for (int i = y; i <y + 2; i++) {    
                if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                    break; //Breche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if(gridSpielfeld.getGrid()[i][y+1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[i][y+1].setFill(Color.BLUE); //DEBUG
                    break; //Brche suche ab
                }
            }
        }
        //Rechte untere Ecke
        if (x + 1 == gridSpielfeld.getKachelAnzahl() -s.getLaenge() +1 && y+1>=gridSpielfeld.getKachelAnzahl()-1) {
            for (int i = y; i >y - 2; i--) {    
                if (gridSpielfeld.getGrid()[x-1][i].getFill() == Color.BROWN) { //Links vom Schiff
                    status = false; //Braunes Feld gefunden
                    //gridSpielfeld.getGrid()[x-1][i].setFill(Color.BLACK); //DEBUG
                    break; //Breche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if(gridSpielfeld.getGrid()[i][y-1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[i][y-1].setFill(Color.BLUE); //DEBUG
                    break; //Brche suche ab
                }
            }
        }
        //Linke untere Ecke
        if (x-1<=0 && y+1>=gridSpielfeld.getKachelAnzahl()-1) {
            for (int i = y; i > y - 2; i--) {    
                if(gridSpielfeld.getGrid()[x+s.getLaenge()][i].getFill() == Color.BROWN){//Rechts vom Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[x+s.getLaenge()][i].setFill(Color.BURLYWOOD); //DEBUG
                    break; //Brche suche ab
                }
            }for (int i = x; i < x + s.getLaenge(); i++) {
                if(gridSpielfeld.getGrid()[i][y-1].getFill() == Color.BROWN){//Suche unter dem Schiff
                    status = false; //Braunes Feld gefunden 
                    //gridSpielfeld.getGrid()[i][y-1].setFill(Color.BLUE); //DEBUG
                    break; //Brche suche ab
                }
            }
        }
        return status;
    }
}
