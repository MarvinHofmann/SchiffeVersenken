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
public class OnlineSpielSteuerung extends Steuerung{
    
    public OnlineSpielSteuerung(SpielGUIController gui) {
        super(gui);
        System.out.println("OnlineSteuerung erzeugt");
    }
    
}