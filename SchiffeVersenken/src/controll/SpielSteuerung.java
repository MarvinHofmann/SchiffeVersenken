/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import Server.Client;
import Server.Server;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public abstract class SpielSteuerung {

    protected SpielGUIController dieGui = null;
    protected int spielfeldgroesse;
    protected int[] anzahlSchiffeTyp;
    protected Schiff[] schiffe;
    protected Grid gridSpielfeldRechts;
    protected Grid gridSpielfeldLinks;
    protected int anzSchiffe = 0;
    protected int anzGetroffen = 0;
    protected int aktiverSpieler = 0; // 0-> Spieler, 1-> Gegner
    protected int[][] getroffen;
    protected int[][] getroffenGegner;
    protected int eigeneSchiffeGetroffen = 0;
    protected Thread clientT;
    protected Thread serverT;
    protected Server server;
    protected Client client;
    protected boolean readystate;
    protected int grafikTrigger; //treffermarkierungen setzen client, Server 1 = wasser, 2 = treffer, 3 = versenkt

    protected boolean spielEnde = false;

    /**
     * Erzeugt die Schiffe nach den Laden.
     * Anhand der Ids auf dem Grid werden die Schiffe nachgebildet. 
     * Auf der linken Seite gezeichnet. Teilweise getroffen gesetzt falls nötig.
     */
    public void macheEigeneSchiffe() {
        System.out.println("Bin hier mache Schiffe");
        schiffe = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < anzahlSchiffeTyp[0]; i++) {
            schiffe[ctn++] = new Schiff(2 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[1]; i++) {
            schiffe[ctn++] = new Schiff(3 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[2]; i++) {
            schiffe[ctn++] = new Schiff(4 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[3]; i++) {
            schiffe[ctn++] = new Schiff(5 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        int zaehler = 1;
        for (int i = 0; i < gridSpielfeldLinks.getKachelAnzahl(); i++) {
            for (int j = 0; j < gridSpielfeldLinks.getKachelAnzahl(); j++) {
                if (gridSpielfeldLinks.getGrid()[i][j].getId().endsWith("0") && gridSpielfeldLinks.getGrid()[i][j].getId().length() > 1) {
                    int id = (Integer.valueOf(gridSpielfeldLinks.getGrid()[i][j].getId()) / 10) - 1;
                    //System.out.println("id: + " + id);
                    //System.out.println(i + " | " + j);
                    schiffe[id].setStart(i, j);
                    if (j + 1 < gridSpielfeldLinks.getKachelAnzahl()) {
                        if (!gridSpielfeldLinks.getGrid()[i][j + 1].getId().equals("0")) {
                            double speicher = schiffe[id].getWidth();
                            schiffe[id].setWidth(schiffe[id].getHeight());
                            schiffe[id].setHeight(speicher);
                            schiffe[id].setRichtung(Richtung.VERTIKAL);
                        }
                    }
                    dieGui.zeigeSchiffLinks(schiffe[id]);
                    schiffe[id].draw(i * gridSpielfeldLinks.getKachelgroeße(), j * gridSpielfeldLinks.getKachelgroeße());
                    zaehler++;
                }

            }
        }
        for (Schiff schiff : schiffe) {
            //System.out.println(schiff.getStartX());
            //System.out.println(schiff.getStartY());
            //System.out.println(schiff.getIndex());
            if (schiff.getRichtung() == Richtung.HORIZONTAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootH" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    System.out.println(gridSpielfeldLinks.getGrid()[schiff.getStartX() + i][schiff.getStartY()]);
                    gridSpielfeldLinks.getGrid()[schiff.getStartX() + i][schiff.getStartY()].setFill(new ImagePattern(img));
                }
            } else if (schiff.getRichtung() == Richtung.VERTIKAL) {
                for (int i = 0; i < schiff.getLaenge(); i++) {
                    String s = "/Images/bootV" + (int) schiff.getLaenge() + (int) (i + 1) + ".png";
                    Image img = new Image(s);
                    gridSpielfeldLinks.getGrid()[schiff.getStartX()][schiff.getStartY() + i].setFill(new ImagePattern(img));
                }
            }
        }
        for (int i = 0; i < getroffenGegner.length; i++) {
            for (int j = 0; j < getroffenGegner.length; j++) {
                if (getroffenGegner[i][j] == 2) { //1 für wasser 0 nicht def 2 getroffen
                    String s = gridSpielfeldLinks.getGrid()[j][i].getId();
                    int id = ((Integer.valueOf(s)) / 10) - 1;
                    int index = ((Integer.valueOf(s)) % 10);
                    //System.out.println((Integer.valueOf(s) / 10) - 1);
                    //System.out.println((Integer.valueOf(s) / 10));
                    //System.out.println(s);
                    //System.out.println(id + " ?? " + index);
                    schiffe[id].setzteTrefferArray(index);
                }
            }
        } 
    }
    
    /**
     * Hier wird aus einem zweidemensionalen Array mit IDs der Schiffe wieder 
     * ein Grid nachgebildet.
     * 
     * @param arr Geladenes Gridarray mit ids der Schiffe
     * @param seite links: 1, rechts: 2
     * @return 
     */
    public Grid makeGrid(int[][] arr, int seite) {
        Grid tempGrid = new Grid(arr.length);
        if (seite == 0) {
            tempGrid.macheGridLinks();
        } else {
            tempGrid.macheGridRechts();
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                Integer a = arr[i][j];
                tempGrid.getGrid()[i][j].setId(a.toString());
            }
        }
        tempGrid.print();
        return tempGrid;
    }

    /**
     * Aktiviert und erlaubt es auf dem rechten Seite also dem Spielfeld des Spielers zu clicken.
     */
    public void enableMouseClickSoielfeldGridRechts() {
        this.gridSpielfeldRechts.enableMouseClick();
    }

    /**
     * Zeigt jedes Rectangle des Grids auf der Gui an. So wird schlussendliche das gesammte 
     * linke Gitter (Grid) angezeigt.
     * 
     * @param gridSpielfeld Grid von der linken Seite
     */
    public void setGridSpielfeldSpielLinks(Grid gridSpielfeld) {
        this.gridSpielfeldLinks = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldLinks.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldLinks.getGrid().length; j++) {
                dieGui.zeigeGridLinks(this.gridSpielfeldLinks.getGrid()[i][j]);
            }
        }
    }
    
    /**
     * Zeigt jedes Rectangle des Grids auf der Gui an. So wird schlussendliche das gesammte 
     * rechte Gitter (Grid) angezeigt.
     * 
     * @param gridSpielfeld Grid von der rechten Seite
     */
    public void setGridSpielfeldSpielRechts(Grid gridSpielfeld) {
        this.gridSpielfeldRechts = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldRechts.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldRechts.getGrid().length; j++) {
                dieGui.zeigeGridRechts(this.gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
    }

    /**
     * Zeigt alle Schiffe mit Bildern auf der Gui an
     */
    public void setzeSchiffe() {
        for (Schiff schiff : schiffe) {
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff);
        }
    }

    /**
     * Zeigt alle Schiffe mit Bilder auf der Gui an wenn es sich um Schiffe einer Ki handelt.
     */
    public void setzeSchiffeKI() {
        for (Schiff schiff : schiffe) {
            schiff.draw(schiff.getStartX() * gridSpielfeldLinks.getKachelgroeße(), schiff.getStartY() * gridSpielfeldLinks.getKachelgroeße());
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.dreheGui();
            }
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff); // hier funktion auf kiSteuerung umändern 
        }
    }

    /**
     * Diese Funktion setzt das jeweilige getroffene Schiff and er bestimmten Stelle getroffen.
     * So kann im späteren Verlauf herausgefunden werden ob das Schiff vollständig versenkt wurde oder
     * nur angeschossen wurde. Die Funktion gibt zurück ob das Schiff versenkt wurde oder nicht.
     * 
     * @param zeile Zeile des Treffern
     * @param spalte Spalte des Treffers
     * @return Booleanrückgabe ob Schiff vollständig versenkt wurde: true, oder nur angeschossen: false
     */
    public boolean setzeSchiffsteilGetroffen(int zeile, int spalte) {
        String schiffbezeichnung;
        int schiffnr = 0;
        int schiffindex = 0;
        schiffbezeichnung = gridSpielfeldLinks.getGrid()[spalte][zeile].getId();
        boolean versenkt;
        if (schiffbezeichnung.length() == 2) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(1));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        } else if (schiffbezeichnung.length() == 3) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) * 10 + Character.getNumericValue(schiffbezeichnung.charAt(1)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(2));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        }
        versenkt = schiffe[schiffnr].handleTreffer(schiffindex);
        return versenkt;
    }

    /**
     * Diese Funktion wird aufgerufen um dem Gegner eine Antwort auf seinen Schuss zu geben.
     * Der Rückgabe gibt an ob Wasser, ein Schiffsteil getroffen oder ein ganzen Schiff versenkt
     * wurde. Diese Funktion wird in einem Onlinespiel aufgerufen da hier die Arrayindexe noch
     * um -1 verringert werden müssen. 
     * 
     * @param zeile Zeile des Treffers
     * @param spalte Spalte des Treffers
     * @return 0: Wasser getroffen, 1: Schiffsteil getroffen, 2: Schiff versenkt
     */
    public int antwort(int zeile, int spalte) {
        //System.out.println("Schuss Ki auf : Zeile " + zeile + " Spalte: " + spalte);
        //System.out.println(" ID: " + gridSpielfeldLinks.getGrid()[spalte][zeile].getId());
        System.out.println("Spielfeld: " + gridSpielfeldLinks);
        if (gridSpielfeldLinks.getGrid()[spalte-1][zeile-1].getId().equals("0")) {
            return 0;
        } else {
            boolean vernichtet = setzeSchiffsteilGetroffen(zeile-1, spalte-1);
            if (vernichtet) {
                return 2;
            } else {
                return 1;
            }
        }
    }
    
    /**
     * Diese Funktion wird aufgerufen um dem Gegner eine Antwort auf seinen Schuss zu geben.
     * Der Rückgabe gibt an ob Wasser, ein Schiffsteil getroffen oder ein ganzen Schiff versenkt
     * wurde.
     * 
     * @param zeile Zeile des Treffers
     * @param spalte Spalte des Treffers
     * @return 0: Wasser getroffen, 1: Schiffsteil getroffen, 2: Schiff versenkt
     */
    public int antwortLokal(int zeile, int spalte) {
        if (gridSpielfeldLinks.getGrid()[spalte][zeile].getId().equals("0")) {
            return 0;
        } else {
            boolean vernichtet = setzeSchiffsteilGetroffen(zeile, spalte);
            if (vernichtet) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    /**
     * Hiermit wird um ein versenktes Schiff im rechten Grid Wasser gezeichnet.
     * 
     * @param zeile Zeile des versenken Schiffs
     * @param spalte Spalte des versenkten Schiffs
     */
    public void wasserUmSchiffRechts(int zeile, int spalte) {
        //System.out.println("Wasser um Schiff");

        //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
        //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        int richtung; // 0 Horizontal 1 Vertikal
        int laenge;
        int[] position = new int[9]; // Wenn Horizontal: position ist spaltenwert, Wenn Vertikal: position ist zeilenwert
        boolean unterbrechung = false;

        if (spalte - 1 >= 0 && !(gridSpielfeldRechts.getGrid()[spalte - 1][zeile].getFill() instanceof Color)) {
            // System.out.println("-1 :Color: " + gridSpielfeldRechts.getGrid()[spalte-1][zeile].getFill());
            richtung = 0;
        } else if (spalte + 1 < spielfeldgroesse && !(gridSpielfeldRechts.getGrid()[spalte + 1][zeile].getFill() instanceof Color)) {
            // System.out.println("+1 :Color: " + gridSpielfeldRechts.getGrid()[spalte+1][zeile].getFill());
            richtung = 0;
        } else {
            richtung = 1;
        }
        //System.out.println("Richtung: " + richtung);

        int counter = 4;
        int dif;
        if (richtung == 0) {
            if (spalte > spielfeldgroesse - 5) {
                dif = spielfeldgroesse - 1 - spalte;
            } else {
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for (int i = spalte; i <= spalte + dif; i++) {
                if (!(gridSpielfeldRechts.getGrid()[i][zeile].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                } else {
                    break;
                }
            }

            counter = 3;
            if (spalte < 4) {
                dif = spalte;
            } else {
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for (int i = spalte - 1; i >= spalte - dif; i--) {
                if (!(gridSpielfeldRechts.getGrid()[i][zeile].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                } else {
                    break;
                }
            }

            counter++;
            for (int i = 0; i < 9; i++) {
                if (position[i] == 1) {
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        } else if (richtung == 1) {
            if (zeile > spielfeldgroesse - 5) {
                dif = spielfeldgroesse - 1 - zeile;
            } else {
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for (int i = zeile; i <= zeile + dif; i++) {
                if (!(gridSpielfeldRechts.getGrid()[spalte][i].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                } else {
                    break;
                }
            }

            counter = 3;
            if (zeile < 4) {
                dif = zeile;
            } else {
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for (int i = zeile - 1; i >= zeile - dif; i--) {
                if (!(gridSpielfeldRechts.getGrid()[spalte][i].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                } else {
                    break;
                }
            }

            counter++;
            for (int i = 0; i < 9; i++) {
                if (position[i] == 1) {
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }

        if (richtung == 0) {
            for (int i = -4; i <= 4; i++) {
                if (position[i + 4] == 1) {
                    if (zeile - 1 >= 0) {
                        gridSpielfeldRechts.getGrid()[spalte + i][zeile - 1].setFill(Color.TRANSPARENT);
                        getroffen[zeile - 1][spalte + i] = 1;
                        if (dieGui.getDieKISpielSteuerung() != null) {
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile - 1, spalte + i, 1);
                        }
                    }
                    if (zeile + 1 < spielfeldgroesse) {
                        gridSpielfeldRechts.getGrid()[spalte + i][zeile + 1].setFill(Color.TRANSPARENT);
                        getroffen[zeile + 1][spalte + i] = 1;
                        if (dieGui.getDieKISpielSteuerung() != null) {
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile + 1, spalte + i, 1);
                        }
                    }
                    if (spalte - 1 + i >= 0 && gridSpielfeldRechts.getGrid()[spalte - 1 + i][zeile].getFill() instanceof Color) {
                        gridSpielfeldRechts.getGrid()[spalte - 1 + i][zeile].setFill(Color.TRANSPARENT);
                        getroffen[zeile][spalte - 1 + i] = 1;
                        if (dieGui.getDieKISpielSteuerung() != null) {
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte - 1 + i, 1);
                        }
                        if (zeile - 1 >= 0) {
                            gridSpielfeldRechts.getGrid()[spalte - 1 + i][zeile - 1].setFill(Color.TRANSPARENT);
                            getroffen[zeile - 1][spalte - 1 + i] = 1;
                            if (dieGui.getDieKISpielSteuerung() != null) {
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile - 1, spalte - 1 + i, 1);
                            }
                        }
                        if (zeile + 1 < spielfeldgroesse) {
                            gridSpielfeldRechts.getGrid()[spalte - 1 + i][zeile + 1].setFill(Color.TRANSPARENT);
                            getroffen[zeile + 1][spalte - 1 + i] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+1, spalte-1+i, 1);
                            }
                        }
                    }
                    if (spalte + 1 + i < spielfeldgroesse && gridSpielfeldRechts.getGrid()[spalte + 1 + i][zeile].getFill() instanceof Color) {
                        gridSpielfeldRechts.getGrid()[spalte + 1 + i][zeile].setFill(Color.TRANSPARENT);
                        getroffen[zeile][spalte + 1 + i] = 1;
                        if(dieGui.getDieKISpielSteuerung() != null){
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile, spalte+1+i, 1);
                        }
                        if (zeile - 1 >= 0) {
                            gridSpielfeldRechts.getGrid()[spalte + 1 + i][zeile - 1].setFill(Color.TRANSPARENT);
                            getroffen[zeile - 1][spalte + 1 + i] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile-1, spalte+1+i, 1);
                            }
                        }
                        if (zeile + 1 < spielfeldgroesse) {
                            gridSpielfeldRechts.getGrid()[spalte + 1 + i][zeile + 1].setFill(Color.TRANSPARENT);
                            getroffen[zeile + 1][spalte + 1 + i] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+1, spalte+1+i, 1);
                            }
                        }
                    }
                }
            }
        } else if (richtung == 1) {
            for (int i = -4; i <= 4; i++) {
                if (position[i + 4] == 1) {
                    if (spalte - 1 >= 0) {
                        gridSpielfeldRechts.getGrid()[spalte - 1][zeile + i].setFill(Color.TRANSPARENT);
                        getroffen[zeile + i][spalte - 1] = 1;
                        if(dieGui.getDieKISpielSteuerung() != null){
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+i, spalte-1, 1);
                        }
                    }
                    if (spalte + 1 < spielfeldgroesse) {
                        gridSpielfeldRechts.getGrid()[spalte + 1][zeile + i].setFill(Color.TRANSPARENT);
                        getroffen[zeile + i][spalte + 1] = 1;
                        if(dieGui.getDieKISpielSteuerung() != null){
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+i, spalte+1, 1);
                        }
                    }
                    if (zeile - 1 + i >= 0 && gridSpielfeldRechts.getGrid()[spalte][zeile - 1 + i].getFill() instanceof Color) {
                        gridSpielfeldRechts.getGrid()[spalte][zeile - 1 + i].setFill(Color.TRANSPARENT);
                        getroffen[zeile - 1 + i][spalte] = 1;
                        if(dieGui.getDieKISpielSteuerung() != null){
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile-1+i, spalte, 1);
                        }
                        if (spalte - 1 >= 0) {
                            gridSpielfeldRechts.getGrid()[spalte - 1][zeile - 1 + i].setFill(Color.TRANSPARENT);
                            getroffen[zeile - 1 + i][spalte - 1] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile-1+i, spalte-1, 1);
                            }
                        }
                        if (spalte + 1 < spielfeldgroesse) {
                            gridSpielfeldRechts.getGrid()[spalte + 1][zeile - 1 + i].setFill(Color.TRANSPARENT);
                            getroffen[zeile - 1 + i][spalte + 1] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile-1+i, spalte+1, 1);
                            }
                        }
                    }
                    if (zeile + 1 + i < spielfeldgroesse && gridSpielfeldRechts.getGrid()[spalte][zeile + 1 + i].getFill() instanceof Color) {
                        gridSpielfeldRechts.getGrid()[spalte][zeile + 1 + i].setFill(Color.TRANSPARENT);
                        getroffen[zeile + 1 + i][spalte] = 1;
                        if(dieGui.getDieKISpielSteuerung() != null){
                            dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+1+i, spalte, 1);
                        }
                        if (spalte - 1 >= 0) {
                            gridSpielfeldRechts.getGrid()[spalte - 1][zeile + 1 + i].setFill(Color.TRANSPARENT);
                            getroffen[zeile + 1 + i][spalte - 1] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+1+i, spalte-1, 1);
                            }
                        }
                        if (spalte + 1 < spielfeldgroesse) {
                            gridSpielfeldRechts.getGrid()[spalte + 1][zeile + 1 + i].setFill(Color.TRANSPARENT);
                            getroffen[zeile + i + 1][spalte + 1] = 1;
                            if(dieGui.getDieKISpielSteuerung() != null){
                                dieGui.getDieKISpielSteuerung().getKi().setGetroffen(zeile+i+1, spalte+1, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Hiermit wird um ein versenktes Schiff im linken Grid Wasser gezeichnet.
     * 
     * @param zeile Zeile des versenken Schiffs
     * @param spalte Spalte des versenkten Schiffs
     */
    public void wasserUmSchiffLinksKI(int zeile, int spalte, KI ki) {
        //System.out.println("Wasser um Schiff Links");

        //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
        //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        int richtung; // 0 Horizontal 1 Vertikal
        int laenge;
        int[] position = new int[9]; // Wenn Horizontal: position ist spaltenwert, Wenn Vertikal: position ist zeilenwert
        boolean unterbrechung = false;

        if (spalte - 1 >= 0 && !(gridSpielfeldLinks.getGrid()[spalte - 1][zeile].getFill() instanceof Color)) {
            // System.out.println("-1 :Color: " + gridSpielfeldRechts.getGrid()[spalte-1][zeile].getFill());
            richtung = 0;
        } else if (spalte + 1 < spielfeldgroesse && !(gridSpielfeldLinks.getGrid()[spalte + 1][zeile].getFill() instanceof Color)) {
            // System.out.println("+1 :Color: " + gridSpielfeldRechts.getGrid()[spalte+1][zeile].getFill());
            richtung = 0;
        } else {
            richtung = 1;
        }
        //System.out.println("Richtung: " + richtung);

        int counter = 4;
        int dif;
        if (richtung == 0) {
            if (spalte > spielfeldgroesse - 5) {
                dif = spielfeldgroesse - 1 - spalte;
            } else {
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for (int i = spalte; i <= spalte + dif; i++) {
                if (!(gridSpielfeldLinks.getGrid()[i][zeile].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                } else {
                    break;
                }
            }

            counter = 3;
            if (spalte < 4) {
                dif = spalte;
            } else {
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for (int i = spalte - 1; i >= spalte - dif; i--) {
                if (!(gridSpielfeldLinks.getGrid()[i][zeile].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                } else {
                    break;
                }
            }

            counter++;
            for (int i = 0; i < 9; i++) {
                if (position[i] == 1) {
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        } else if (richtung == 1) {
            if (zeile > spielfeldgroesse - 5) {
                dif = spielfeldgroesse - 1 - zeile;
            } else {
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for (int i = zeile; i <= zeile + dif; i++) {
                if (!(gridSpielfeldLinks.getGrid()[spalte][i].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                } else {
                    break;
                }
            }

            counter = 3;
            if (zeile < 4) {
                dif = zeile;
            } else {
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for (int i = zeile - 1; i >= zeile - dif; i--) {
                if (!(gridSpielfeldLinks.getGrid()[spalte][i].getFill() instanceof Color)) {
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                } else {
                    break;
                }
            }

            counter++;
            for (int i = 0; i < 9; i++) {
                if (position[i] == 1) {
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }

        if (richtung == 0) {
            for (int i = -4; i <= 4; i++) {
                if (position[i + 4] == 1) {
                    if (zeile - 1 >= 0) {
                        gridSpielfeldLinks.getGrid()[spalte + i][zeile - 1].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile - 1, spalte + i);
                    }
                    if (zeile + 1 < spielfeldgroesse) {
                        gridSpielfeldLinks.getGrid()[spalte + i][zeile + 1].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile + 1, spalte + i);
                    }
                    if (spalte - 1 + i >= 0 && gridSpielfeldLinks.getGrid()[spalte - 1 + i][zeile].getFill() instanceof Color) {
                        gridSpielfeldLinks.getGrid()[spalte - 1 + i][zeile].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile, spalte - 1 + i);
                        if (zeile - 1 >= 0) {
                            gridSpielfeldLinks.getGrid()[spalte - 1 + i][zeile - 1].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile - 1, spalte - 1 + i);
                        }
                        if (zeile + 1 < spielfeldgroesse) {
                            gridSpielfeldLinks.getGrid()[spalte - 1 + i][zeile + 1].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile + 1, spalte - 1 + i);
                        }
                    }
                    if (spalte + 1 + i < spielfeldgroesse && gridSpielfeldLinks.getGrid()[spalte + 1 + i][zeile].getFill() instanceof Color) {
                        gridSpielfeldLinks.getGrid()[spalte + 1 + i][zeile].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile, spalte + 1 + i);
                        if (zeile - 1 >= 0) {
                            gridSpielfeldLinks.getGrid()[spalte + 1 + i][zeile - 1].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile - 1, spalte + 1 + i);
                        }
                        if (zeile + 1 < spielfeldgroesse) {
                            gridSpielfeldLinks.getGrid()[spalte + 1 + i][zeile + 1].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile + 1, spalte + 1 + i);
                        }
                    }
                }
            }
        } else if (richtung == 1) {
            for (int i = -4; i <= 4; i++) {
                if (position[i + 4] == 1) {
                    if (spalte - 1 >= 0) {
                        gridSpielfeldLinks.getGrid()[spalte - 1][zeile + i].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile + i, spalte - 1);
                    }
                    if (spalte + 1 < spielfeldgroesse) {
                        gridSpielfeldLinks.getGrid()[spalte + 1][zeile + i].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile + i, spalte + 1);
                    }
                    if (zeile - 1 + i >= 0 && gridSpielfeldLinks.getGrid()[spalte][zeile - 1 + i].getFill() instanceof Color) {
                        gridSpielfeldLinks.getGrid()[spalte][zeile - 1 + i].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile - 1 + i, spalte);
                        if (spalte - 1 >= 0) {
                            gridSpielfeldLinks.getGrid()[spalte - 1][zeile - 1 + i].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile - 1 + i, spalte - 1);
                        }
                        if (spalte + 1 < spielfeldgroesse) {
                            gridSpielfeldLinks.getGrid()[spalte + 1][zeile - 1 + i].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile - 1 + i, spalte + 1);
                        }
                    }
                    if (zeile + 1 + i < spielfeldgroesse && gridSpielfeldLinks.getGrid()[spalte][zeile + 1 + i].getFill() instanceof Color) {
                        gridSpielfeldLinks.getGrid()[spalte][zeile + 1 + i].setFill(Color.TRANSPARENT);
                        ki.setGetroffenWasser(zeile + 1 + i, spalte);
                        if (spalte - 1 >= 0) {
                            gridSpielfeldLinks.getGrid()[spalte - 1][zeile + 1 + i].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile + 1 + i, spalte - 1);
                        }
                        if (spalte + 1 < spielfeldgroesse) {
                            gridSpielfeldLinks.getGrid()[spalte + 1][zeile + 1 + i].setFill(Color.TRANSPARENT);
                            ki.setGetroffenWasser(zeile + 1 + i, spalte + 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gibt das zweidemensionale Array getroffen auf der Konsole aus
     */
    public void printGetroffen() {
        System.out.println("");
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                System.out.print(getroffen[i][j] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * Getter und Setter
     */
    
    public int getAnzGetroffen() {
        return anzGetroffen;
    }

    public int getGrafikTrigger() {
        return grafikTrigger;
    }

    public int getEigeneSchiffeGetroffen() {
        return eigeneSchiffeGetroffen;
    }

    public void setEigeneSchiffeGetroffen(int eigeneSchiffeGetroffen) {
        this.eigeneSchiffeGetroffen += eigeneSchiffeGetroffen;
    }

    public void setGrafiktrigger(int wert) {
        this.grafikTrigger = wert;
    }

    public int[][] getGetroffen() {
        return getroffen;
    }
    
    public int[][] getGetroffenGegner() {
        return getroffenGegner;
    }   
    
    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }

    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public int getAktiverSpieler() {
        return aktiverSpieler;
    }
    
    public void setGetroffenGegner(int[][] getroffenGegner) {
        this.getroffenGegner = getroffenGegner;
    }

    public void setGetroffen(int[][] getroffen) {
        this.getroffen = getroffen;
    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void setSpielEnde(boolean spielEnde) {
        this.spielEnde = spielEnde;
    }
    
    public SpielSteuerung(GUI.SpielGUIController gui) {
        //System.out.println("Steuerung erzeugt");
        this.dieGui = gui;
    }

    public Schiff[] getSchiffe() {
        return schiffe;
    }

    public int getSpielfeldgroesse() {
        return spielfeldgroesse;
    }

    public int[] getAnzahlSchiffeTyp() {
        return anzahlSchiffeTyp;
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        this.spielfeldgroesse = spielfeldgroesse;
    }

    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
    }
    
    public KI getKIGegner(){
        if (dieGui.getDieLokalesSpielSteuerung() != null) {
            return dieGui.getDieLokalesSpielSteuerung().getKiGegner();
        }
        return null;
    }

    public void setSchiffe(Schiff[] Schiffe) {
        this.schiffe = Schiffe;
    }
    
    public void setAktiverSpieler(int aktiverSpieler) {
        this.aktiverSpieler = aktiverSpieler;
    }
    
    public void setGridSpielfeldRechts(Grid gridSpielfeld) {
        this.gridSpielfeldRechts = gridSpielfeld;
    }

    public void setGridSpielfeldLinks(Grid gridSpielfeld) {
        this.gridSpielfeldLinks = gridSpielfeld;
    }

    public abstract void erzeugeEigeneSchiffe();

    public abstract void beginneSpiel();

    public abstract int ueberpruefeSpielEnde();
    
    public abstract boolean isFertigSetzen();
    
    public abstract void setSchiffeSetzen();
}
