/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.shape.Rectangle;
import shapes.Grid;
import shapes.KI;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public abstract class SpielSteuerung {
    protected SpielGUIController dieGui = null;
    protected int spielfeldgroesse;
    protected Schiff[] schiffe;
    protected int[][] spielfeld; // Speichert Schiffe vom Spieler selbst
    protected Grid gridSpielfeld;
    int[] anzahlSchiffeTyp;

    public Grid getGridSpielfeld() {
        return gridSpielfeld;
    }

    public void setGridSpielfeld(Grid gridSpielfeld) {
        this.gridSpielfeld = gridSpielfeld;
    }
    
    public void setGridSpielfeldKI(Grid gridSpielfeld){
        this.gridSpielfeld = gridSpielfeld;
        for (int i = 0; i < gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(gridSpielfeld.getGrid()[i][j]);
            }
        }
        gridSpielfeld.enableMouseClick();
    }

    public SpielSteuerung(GUI.SpielGUIController gui) {
        //System.out.println("Steuerung erzeugt");
        this.dieGui = gui;
    }

    public abstract boolean isFertigSetzen();
    
    public Schiff[] getSchiffe() {
        return schiffe;
    }

    public int getSpielfeldgroesse() {
        return spielfeldgroesse;
    }

    public void setSchiffe(Schiff[] Schiffe) {
        this.schiffe = Schiffe;
    }
    
    public abstract void setSchiffeSetzen();
    
    public void erzeugespielfeld(){
        spielfeld = new int[spielfeldgroesse][spielfeldgroesse];
        int counter = 1; // Schiff[0] -> Bezeichung 1,.... StartX/StartY = 0,...
        for(Schiff schiff: schiffe){
            //System.out.println("Schiff");
            //System.out.println("x: " + schiff.getStartX() + " y: " + schiff.getStartY() + " richtung: " + schiff.getRichtung() + " groesse: " + schiff.getLaenge());
            if(schiff.getRichtung() == Richtung.HORIZONTAL){ // ----
                for(int i = 0; i < schiff.getLaenge(); i++){
                    spielfeld[schiff.getStartY()][schiff.getStartX()+i] = counter*10 + i;
                }
                counter++;
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){ // |||
                for(int i = 0; i < schiff.getLaenge(); i++){
                    spielfeld[schiff.getStartY()+i][schiff.getStartX()] = counter*10 + i;
                }
                counter++;
            }
        }
        
        System.err.println("Spielfeld ausgeben");
        for(int i = 0; i < spielfeldgroesse; i++){
            for(int j = 0; j < spielfeldgroesse; j++){
                System.out.print(spielfeld[i][j] + "\t|\t");
            }
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
    
    public void erzeugeGrid() {
        gridSpielfeld = new Grid(spielfeldgroesse);
        gridSpielfeld.macheGrid();
        for (int i = 0; i < gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(gridSpielfeld.getGrid()[i][j]);
            }
        }
        gridSpielfeld.enableMouseClick();
    }

    public void setzeSchiffe() {
        for(Schiff schiff: schiffe){
            Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            dieGui.zeigeSchiff(req);
        }
    }
    
    public void setzeSchiffeKI() {
        for(Schiff schiff: schiffe){
            schiff.draw(schiff.getStartX() * gridSpielfeld.getKachelgroeße(), schiff.getStartY() * gridSpielfeld.getKachelgroeße());
            if(schiff.getRichtung() == Richtung.VERTIKAL){
                schiff.dreheGui();
            }            
            Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            dieGui.zeigeSchiff(req);
        }
    }
    
    public abstract void erzeugeEigeneSchiffe();
    
    public abstract void test();

    /**
     * Mit drücken des Zurücksetzen Buttons werden alle Schiffe wieder in die originale 
     * Positioin auf die Rechte Seite verlegt.
     */
    public abstract void clearSchiffeSetzen();
    
    public abstract void randomSetzen();

    public abstract KI getKi();
}
