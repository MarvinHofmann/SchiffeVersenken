/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.paint.Color;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class LokalesSpielSteuerung extends SpielSteuerung{
    int count=0;
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    
    public LokalesSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("LokalesSpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(gui, anzahlSchiffeTyp);
    }
    
    @Override
    public void erzeugeEigeneSchiffe(){
        dieSteuerungSchiffeSetzen.drawAll(spielfeldgroesse);
    }

    @Override
    public void setSchiffeSetzen() {
        this.schiffe = dieSteuerungSchiffeSetzen.getSchiffArray();
    }

    @Override
    public boolean isFertigSetzen() {
        return dieSteuerungSchiffeSetzen.isFertig();
    }

    public SteuerungSchiffeSetzen getDieSteuerungSchiffeSetzen() {
        return dieSteuerungSchiffeSetzen;
    }

    /**
     * Setze alle Schiffe Zurück
     */
    public void clearSchiffeSetzen() {
        for (Schiff schiff : dieSteuerungSchiffeSetzen.getSchiffArray()) { //Für jedes Schiff
            dieSteuerungSchiffeSetzen.clearId(schiff); //Lösche Marker ID auf Grid
            schiff.setFill(Color.RED); //Setzte die Farbe rot
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.drehen(); //Drehen vertikale Schiff für exaxte Startposition
            }
        }
        dieSteuerungSchiffeSetzen.zeichneSchiffe(true); 
        dieSteuerungSchiffeSetzen.getGridS().print(); //DEBUG
    }    

    public void randomSetzen() {
        clearSchiffeSetzen();
        dieSteuerungSchiffeSetzen.setzeRandomSchiffe();
        for(Schiff schiff: dieSteuerungSchiffeSetzen.getSchiffArray()){
            if(schiff.getRichtung() == Richtung.VERTIKAL  ){
                schiff.dreheGui();
            }    
            schiff.setFill(Color.GREEN); //Setzte die Farbe grün
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridS().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridS().getKachelgroeße());
        }
    }

}
