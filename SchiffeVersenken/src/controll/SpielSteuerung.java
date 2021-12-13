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
    protected int[] anzahlSchiffeTyp;
    protected Schiff[] schiffe;
    protected Grid gridSpielfeld;
    protected boolean spielEnde = false;

    public Grid getGridSpielfeld() {
        return gridSpielfeld;
    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void setSpielEnde(boolean spielEnde) {
        this.spielEnde = spielEnde;
    }

    public void setGridSpielfeld(Grid gridSpielfeld) {
        this.gridSpielfeld = gridSpielfeld;
    }
    
    public void setGridSpielfeldSpiel(Grid gridSpielfeld){
        this.gridSpielfeld = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(this.gridSpielfeld.getGrid()[i][j]);
            }
        }
        this.gridSpielfeld.enableMouseClick();
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

    public int[] getAnzahlSchiffeTyp() {
        return anzahlSchiffeTyp;
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        this.spielfeldgroesse = spielfeldgroesse;
    }

    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
    }
    
    

    public void setSchiffe(Schiff[] Schiffe) {
        this.schiffe = Schiffe;
    }
    
    public abstract void setSchiffeSetzen();
    
    public void setzeSchiffe() {
        for(Schiff schiff: schiffe){
            Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff);
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
            //dieGui.zeichneSchiffe(req); // hier funktion auf kiSteuerung umändern 
        }
    }
    
    public abstract void erzeugeEigeneSchiffe();

    public abstract void beginneSpiel();
}
