/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author marvi
 */
public class Grid {
    private int pxGroesse = 600;
    private int kachelAnzahl = 0; //5-30
    private int kachelgroeße = 0;
    private int buffer = 0;
    private Rectangle[][] grid; //Plazierfeld
    public double PXmitBuffer;
    

    public Grid(int kachelA) {
        this.kachelAnzahl = kachelA;
        kachelgroeße = pxGroesse / kachelAnzahl;
        int check = pxGroesse % kachelgroeße;
        pxGroesse = kachelgroeße * kachelAnzahl - check;
        grid = new Rectangle[kachelAnzahl + bufferBerechnen()][kachelAnzahl];
    }
    
    public int bufferBerechnen(){
        
        buffer = (int) kachelAnzahl / 2;
        if (buffer < 5) {
            buffer = 5; // Mindestens 5er Buffer
        }
        return buffer;
    }
    
    public Rectangle[][] macheGrid() {
              
        //von 0 bis zur pixelgröße + buffer für schiffe erzeuge rechtecke immer gleich groß
        for (int i = 0; i < pxGroesse + buffer * kachelgroeße; i += kachelgroeße) {
            for (int j = 0; j < pxGroesse; j += kachelgroeße) {
                //Nach und nach rectangles erzeugen
                Rectangle r = new Rectangle(i, j, kachelgroeße, kachelgroeße);
                grid[i / kachelgroeße][j / kachelgroeße] = r;
                if (i >= kachelAnzahl * kachelgroeße) {
                    //Ende des Setzbaren felds besonders markiert
                    r.setFill(Color.GRAY);
                    r.setStroke(Color.WHITE);
                } else {
                    //wenn noch in normaler spielfeldgr machen normale kästechen weiß mit 
                    //schwarzem rand
                    r.setFill(Color.WHITE);
                    r.setStroke(Color.BLACK);
                }
                r.setOnMouseClicked(event -> clicked(event, r));
                
                
            }
        }       
        return grid;
    }

    private void clicked(MouseEvent event, Rectangle r) {
        
        
    }

    public int getBuffer() {
        return buffer;
    }

    public int getPxGroesse() {
        return pxGroesse;
    }

    public int getKachelAnzahl() {
        return kachelAnzahl;
    }

    public int getKachelgroeße() {
        return kachelgroeße;
    }

    public double getPXmitBuffer() {
        return pxGroesse + (buffer*kachelgroeße);
    }
    
    
    
}
