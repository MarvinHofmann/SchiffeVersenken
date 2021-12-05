/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import shapes.KI;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class KISpielSteuerung extends SpielSteuerung{
    private KI ki = null;
    public KISpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("KISpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        ki = new KI(spielfeldgroesse, anzahlSchiffeTyp);
    }

    @Override
    public void erzeugeEigeneSchiffe() {
        ki.erzeugeEigeneSchiffe();
    }

    @Override
    public void setSchiffeSetzen() {
        
    }

    @Override
    public boolean isFertigSetzen() {
        return ki.isFertig();
    }

    
}
