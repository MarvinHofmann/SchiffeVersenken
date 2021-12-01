/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public abstract class Steuerung {
    protected SpielGUIController dieGui = null;
    protected int spielfeldgroesse;
    protected Schiff[] Schiffe;
    protected int[][] spielfeld;

    public Steuerung(GUI.SpielGUIController gui) {
        //System.out.println("Steuerung erzeugt");
        this.dieGui = gui;
    }

    public Schiff[] getSchiffe() {
        return Schiffe;
    }

    public void setSchiffe(Schiff[] Schiffe) {
        this.Schiffe = Schiffe;
    }
    
    public void erzeugespielfeld(){
        spielfeld = new int[spielfeldgroesse][spielfeldgroesse];
        int counter = 1; // Schiff[0] -> Bezeichung 1,.... StartX/StartY = 1,...
        for(Schiff schiff: Schiffe){
            System.out.println("Schiff");
            //System.out.println("x: " + schiff.getStartX() + " y: " + schiff.getStartY() + " richtung: " + schiff.getRichtung() + " groesse: " + schiff.getLaenge());
            if(schiff.getRichtung() == Richtung.HORIZONTAL){ // ----
                System.err.println("Fehler");
                for(int i = 0; i < schiff.getLaenge(); i++){
                    System.out.println("i: " + i);
                    spielfeld[schiff.getStartX()+i][schiff.getStartY()] = counter;
                }
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){ // |||
                
            }
        }
        
        System.err.println("Spielfeld ausgeben");
        for(int i = 0; i < spielfeldgroesse; i++){
            for(int j = 0; j < spielfeldgroesse; j++){
                System.out.print(spielfeld[i][j] + " | ");
            }
            System.out.println("\n-------------------------------------------");
        }
    }
    
}
