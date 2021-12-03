/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;
import Server.Server;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author marvi
 */
public class Grid {
    private int pxGroesse = 600;
    private int kachelAnzahl = 0; // 5-30
    private int kachelgroeße = 0; // Größe einer einzelnen Kachelgröße
    private Rectangle[][] grid; // Plazierfeld
    private Server server;
    
    public Grid(int kachelAnzahl) {
        this.kachelAnzahl = kachelAnzahl;
        this.kachelgroeße = this.pxGroesse / this.kachelAnzahl; // Berechung der einzelnen Kachelgröße
        this.pxGroesse = kachelgroeße * kachelAnzahl - this.pxGroesse % this.kachelgroeße; // Wenn nicht ganzzahlig teilbar ändere die Größe 
        grid = new Rectangle[kachelAnzahl * 2][kachelAnzahl]; // Initialisiere grid -> zwei gleichgroße Rectangelfelder [breite][höhe]
    }
    
    public Grid(int kachelA, Server server){
        this(kachelA);
        this.server = server;
    }
      
    
    public Rectangle[][] macheGrid() {
        //von 0 bis zur pixelgröße* 2, für schiffe erzeuge rechtecke immer gleich groß
        for (int i = 0; i < pxGroesse * 2; i += kachelgroeße) { // Breite des Grids 
            for (int j = 0; j < pxGroesse; j += kachelgroeße) { // Höhe des Grids
                Rectangle r = new Rectangle(i, j, kachelgroeße, kachelgroeße); //Nach und nach rectangles erzeugen
                grid[i / kachelgroeße][j / kachelgroeße] = r;
                if (i >= kachelAnzahl * kachelgroeße) { // Ende des setzbaren Felds besonders markiert, kästechen grau mit weißem Rand
                    r.setFill(Color.GRAY); 
                    r.setStroke(Color.WHITE);
                } 
                else { // Wenn noch im normalem spielfeld, kästechen weiß mit schwarzem rand
                    r.setFill(Color.WHITE); 
                    r.setStroke(Color.BLACK);
                }              
            }
        }       
        return grid;
    }

    public void enableMouseClick(){
        //System.out.println("Aktiviere Schus");
        for (int i = 0; i < pxGroesse * 2; i += kachelgroeße) {
            for (int j = 0; j < pxGroesse; j += kachelgroeße) {
                Rectangle r = grid[i / kachelgroeße][j / kachelgroeße]; 
                r.setOnMouseClicked(event -> clicked(event, r));
            }
        }
    }
    
    private void clicked(MouseEvent event, Rectangle r) {
        //System.out.println("Rectangele wurde gecklicked");   
        //System.out.println("Schuss auf Rectanngle " + (int) r.getX() / kachelgroeße + " " + (int) r.getY() / kachelgroeße);
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
