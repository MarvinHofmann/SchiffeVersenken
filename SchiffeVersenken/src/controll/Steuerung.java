/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class Steuerung {
    private SpielGUIController dieGui = null;
    private int spielfeldgroesse;
    private int[] anzahlSchiffe;

    public Steuerung(GUI.SpielGUIController gui) {
        System.out.println("Steuerung erzeugt");
        this.dieGui = gui;
    }

    public void initialisiereSpiel() {
        System.out.println("Initialisiere Spiel");
    }

    public int getSpielfeldgroesse() {
        return spielfeldgroesse;
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        this.spielfeldgroesse = spielfeldgroesse;
    }

    public int[] getAnzahlSchiffe() {
        return anzahlSchiffe;
    }

    public void setAnzahlSchiffe(int[] anzahlSchiffe) {
        this.anzahlSchiffe = anzahlSchiffe;
    }
    
    
}
