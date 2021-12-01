/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import shapes.Grid;
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
    protected Grid gridSpielfeld;

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
            //System.out.println("Schiff");
            //System.out.println("x: " + schiff.getStartX() + " y: " + schiff.getStartY() + " richtung: " + schiff.getRichtung() + " groesse: " + schiff.getLaenge());
            if(schiff.getRichtung() == Richtung.HORIZONTAL){ // ----
                for(int i = 0; i < schiff.getLaenge(); i++){
                    spielfeld[schiff.getStartY()][schiff.getStartX()+i] = counter;
                }
            }
            else if(schiff.getRichtung() == Richtung.VERTIKAL){ // |||
                for(int i = 0; i < schiff.getLaenge(); i++){
                    spielfeld[schiff.getStartY()+i][schiff.getStartX()] = counter;
                }
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
    
    public void erzeugeGrid() {
        gridSpielfeld = new Grid(spielfeldgroesse);
        gridSpielfeld.macheGrid();
        for (int i = 0; i < gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(gridSpielfeld.getGrid()[i][j]);
                Rectangle r = gridSpielfeld.getGrid()[i][j];
                r.setOnMouseDragged(event -> clicked(event, r));
            }
        }
    }

    private void clicked(MouseEvent event, Rectangle r) {
        System.out.println("Clicked");
        r.setFill(Color.AZURE);
        System.out.println("Schuss auf Rectanngle " + (int) r.getX() / gridSpielfeld.getKachelgroeße() + " " + (int) r.getY() / gridSpielfeld.getKachelgroeße());
        //int x = (int) event.getX();
        //int y = (int) event.getY();
    }
}
