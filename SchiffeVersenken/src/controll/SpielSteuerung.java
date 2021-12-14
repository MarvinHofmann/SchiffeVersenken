/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.shape.Rectangle;
import shapes.Grid;
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
    protected Grid gridSpielfeldRechts;
    protected Grid gridSpielfeldLinks;
    
    protected boolean spielEnde = false;

    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }
    
    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void setSpielEnde(boolean spielEnde) {
        this.spielEnde = spielEnde;
    }

    public void setGridSpielfeldRechts(Grid gridSpielfeld) {
        this.gridSpielfeldRechts = gridSpielfeld;
    }
    
    public void setGridSpielfeldLinks(Grid gridSpielfeld) {
        this.gridSpielfeldLinks = gridSpielfeld;
    }
    
    public void setGridSpielfeldSpielRechts(Grid gridSpielfeld){
        this.gridSpielfeldRechts = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldRechts.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldRechts.getGrid().length; j++) {
                dieGui.zeigeGridRechts(this.gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
        this.gridSpielfeldRechts.enableMouseClick();
    }
    
    public void setGridSpielfeldSpielLinks(Grid gridSpielfeld){
        this.gridSpielfeldLinks = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldLinks.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldLinks.getGrid().length; j++) {
                dieGui.zeigeGridLinks(this.gridSpielfeldLinks.getGrid()[i][j]);
            }
        }
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
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff);
        }
    }
    
    public void setzeSchiffeKI() {
        for(Schiff schiff: schiffe){
            schiff.draw(schiff.getStartX() * gridSpielfeldLinks.getKachelgroeße(), schiff.getStartY() * gridSpielfeldLinks.getKachelgroeße());
            if(schiff.getRichtung() == Richtung.VERTIKAL){
                schiff.dreheGui();
            }            
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff); // hier funktion auf kiSteuerung umändern 
        }
    }
    
    public abstract void erzeugeEigeneSchiffe();

    public abstract void beginneSpiel();
}
