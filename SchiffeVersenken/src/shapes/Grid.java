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
    private Rectangle[][] grid; //Plazierfeld
    public double PXmitBuffer;
    

    public Grid(int kachelA) {
        this.kachelAnzahl = kachelA;
        kachelgroeße = pxGroesse / kachelAnzahl;
        int check = pxGroesse % kachelgroeße;
        pxGroesse = kachelgroeße * kachelAnzahl - check;
        grid = new Rectangle[kachelAnzahl * 2][kachelAnzahl];
    }
      
    
    public Rectangle[][] macheGrid() {
              
        //von 0 bis zur pixelgröße + buffer für schiffe erzeuge rechtecke immer gleich groß
        for (int i = 0; i < pxGroesse * 2; i += kachelgroeße) {
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
            }
        }       
        return grid;
    }

    public void enableMouseClick(){
        for (int i = 0; i < pxGroesse * 2; i += kachelgroeße) {
            for (int j = 0; j < pxGroesse; j += kachelgroeße) {
                //Nach und nach rectangles erzeugen
                Rectangle r = grid[i / kachelgroeße][j / kachelgroeße];
                r.setOnMouseClicked(event -> clicked(event, r));    
            }
        }
    }
    
    private void clicked(MouseEvent event, Rectangle r) {
        System.out.println("Rectangele wurde gecklicked");   
        System.out.println("Schuss auf Rectanngle " + (int) r.getX() / kachelgroeße + " " + (int) r.getY() / kachelgroeße);
        r.setFill(Color.RED);
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

    public Rectangle[][] getGrid() {
        return grid;
    }
    
    
}
