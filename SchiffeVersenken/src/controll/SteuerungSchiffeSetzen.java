/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import shapes.Grid;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SteuerungSchiffeSetzen implements EventHandler<KeyEvent>{
    private SpielGUIController dieGui = null;

    //Spielfeld => 600x600
    private int[] schiffTypen;
    private int buffer = 0;
    private int anzSchiffe = 0;
    private Rectangle[][] grid; //Plazierfeld
    Schiff[] schiffArray;
    Grid gridS;
    
    public SteuerungSchiffeSetzen(GUI.SpielGUIController gui) {
        System.out.println("SteuerungSchiffeSetzen erzeugt");
        this.dieGui = gui;
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

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public int getAnzSchiffe() {
        return anzSchiffe;
    }

    public void setAnzSchiffe(int anzSchiffe) {
        this.anzSchiffe = anzSchiffe;
    }

    public Rectangle[][] getGrid() {
        return grid;
    }

    public void setGrid(Rectangle[][] grid) {
        this.grid = grid;
    }

    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public void setSchiffArray(Schiff[] schiffArray) {
        this.schiffArray = schiffArray;
    }

    public Grid getGridS() {
        return gridS;
    }

    public void setGridS(Grid gridS) {
        this.gridS = gridS;
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
    
    private void drawAll(int gr) {
        gridS = new Grid(gr);
        Rectangle[][] feld = gridS.macheGrid();
        grid = feld;
        //System.out.println(feld.length);
        //Grid Zeichnen rectangle kacheln dem Pane hinzufügen
        for (int i = 0; i < feld.length; i++) {
            for (int j = 0; j < feld.length / 2; j++) {
                dieGui.zeigeGrid(feld[i][j]);
            }
        }
        macheSchiffe();
    }
    
     private void macheSchiffe() {
        //Erstelle alle Schiffe und Speichere sie in einem Array voller 
        //Schiffsobjekte ab
        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < schiffTypen[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            //System.out.println("Erstelle typ 2");
        }
        for (int i = 0; i < schiffTypen[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            //System.out.println("Erstelle typ 3");
        }
        for (int i = 0; i < schiffTypen[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            //System.out.println("Erstelle typ 4");
        }
        for (int i = 0; i < schiffTypen[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            //System.out.println("Erstelle typ 5");
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
            schiffArray[i].draw(gridS.getPxGroesse(), 2 * m);
            makeHandler(schiffArray[i]);
            m = m + gridS.getKachelgroeße();
        }
    }
    
    private void makeHandler(Schiff s) {
        s.setOnMouseDragged(event -> dragged(event, s));
        s.setOnMouseReleased(event -> released(event, s));
    }
     
    public void dragged(MouseEvent event, Schiff s) {
        //schiffeSetzenFeld.setCursor(Cursor.CLOSED_HAND);
        dieGui.setzeCursor(Cursor.CLOSED_HAND);
        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüger
        int snapX = (int) (event.getX() % gridS.getKachelgroeße());
        int snapY = (int) (event.getY() % gridS.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX() - snapX;
        int y = (int) event.getY() - snapY;
        boolean blockiert = false;
        
        //Kontrolle der Grenzen 
        //Wenn in buffer mache Schiff rot
        /*if (x > gridS.getPxGroesse() - s.getWidth()) {
            s.setFill(Color.RED);
            s.setX(x);
            s.setY(y);
            //Wenn man unten raus will setzte grenze y geht nicht weiter als 600
        } else if (y > gridS.getPxGroesse() - s.getHeight()) {
            s.setX(x);
            s.setY(gridS.getPxGroesse() - s.getHeight());
            //Grenze oben nicht kliener als 0
        } else if (y < 0) {
            s.setX(x);
            s.setY(0);
            //Grenze links nicht kleiner als o
        } else if (x < 0) {
            s.setX(0);
            s.setY(y);
            //Wenn keine grenze erreicht setze normal Schiff ist grün
        } else {
            s.setFill(Color.GREEN);
            s.setX(x);
            s.setY(y);
        }*/
        
        if(x < 0){
            blockiert = true;
        }
        else if(y < 0){
            blockiert = true;
        }
        else if(x > (1200 - s.getWidth() + 0.5 * gridS.getKachelgroeße())){
            blockiert = true;
        }
        else if(y > (600 - s.getHeight()) + 0.5 * gridS.getKachelgroeße()){
            blockiert = true;
        }
        else{
            blockiert = false;
        }
        
        if(!blockiert){
            if(x <= (600 - s.getWidth())){
                s.setFill(Color.GREEN);
            }
            else{
                s.setFill(Color.RED);
            }
            s.setX(x);
            s.setY(y);
        }  
        
        //Neu zeichnen
        //drawWasser(s, Color.WHITE);
        s.draw();
    }
    
    public void cleanFeld(){
        for (int i = 0; i < gridS.getKachelAnzahl(); i++) {
            for (int j = 0; j < gridS.getKachelAnzahl(); j++) {
                if (!grid[i][j].getFill().equals(Color.NAVY)) {

                    grid[i][j].setFill(Color.WHITE);
                }
            }
        }
    }

    public void drawWasser(Schiff s, Color c) {
        int aktX = (int) s.getX() / gridS.getKachelgroeße();
        int aktY = (int) s.getY() / gridS.getKachelgroeße();

        //Spielfeld cleanen bei jedem draw
        cleanFeld();
        if (aktX < gridS.getKachelAnzahl() - s.getWidth() / gridS.getKachelgroeße()) {
            for (int i = -1; i < s.getWidth() / gridS.getKachelgroeße() + 1; i++) {
                //Von -1 links vom Schiff bis eins rechts vom schiff mache obere und unter linie
                //
                //  [-1 / -1][0 / -1  ][ +1 / -1][+2/-1]
                //  [-1 /  y][SCHIFF_1][SCHIFF_1][schiffbreite / y]
                //  [-1 / +1][0 / +1  ][ +1 / +1][+2/+1]
                //
                grid[aktX + i][aktY + 1].setFill(c);
                grid[aktX + i][aktY - 1].setFill(c);
            }
            //Kachel rechts
            grid[aktX + (int) s.getWidth() / gridS.getKachelgroeße()][aktY].setFill(c);
            //Kachel links
            grid[aktX - 1][aktY].setFill(c);

        }

    }

    //Verwalten des Zustands losgelassen
    public void released(MouseEvent event, Schiff s) {
        int puff = (int) (s.getX() / gridS.getKachelgroeße());
        int puffY = (int) (s.getY() / gridS.getKachelgroeße());
        s.setX(puff * gridS.getKachelgroeße());
        s.setY(puffY * gridS.getKachelgroeße());
        s.setFill(Color.GREEN);
        s.setStroke(Color.GREEN);
        //Ermittle Koordinatenwert der StartPositionen für 2D Array
        int startX = (int) event.getX() / gridS.getKachelgroeße();
        int startY = (int) event.getY() / gridS.getKachelgroeße();
        //System.out.println(startX + " " + startY);
        s.setStart(startX, startY);
        drawWasser(s, Color.NAVY);
        s.draw();
    }
    
    @Override
    public void handle(KeyEvent event) {
        System.out.println("Huhu");
        /*for (int i = 0; i < schiffArray.length; i++) {
            System.out.println("Schiff" + i + ": ");
            System.out.println(schiffArray[i].getHeight());
            System.out.println(schiffArray[i].getWidth());
            System.out.println(schiffArray[i].getStartX());
            System.out.println(schiffArray[i].getStartY());
            System.out.println("####################");
        }*/
    }

    
    
}
