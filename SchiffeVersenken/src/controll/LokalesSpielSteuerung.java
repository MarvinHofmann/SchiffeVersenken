/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import shapes.KI;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class LokalesSpielSteuerung extends SpielSteuerung{
   
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    private KI kiGegner;
    private int[][] getroffen;
    private int aktiverSpieler = 0; // 0-> Spieler, 1-> KI
    
    public LokalesSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("LokalesSpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(gui, anzahlSchiffeTyp, spielfeldgroesse);
        kiGegner = new KI(spielfeldgroesse, anzahlSchiffeTyp);
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
    }
    
    @Override
    public void erzeugeEigeneSchiffe(){
        dieSteuerungSchiffeSetzen.drawAll();
    }
    
    
    public void erzeugeGegnerSchiffe() {
        kiGegner.erzeugeEigeneSchiffe();
        System.out.println("Gegnerfeld");
        kiGegner.getGridSpielfeld().print();
    }

    @Override
    public void setSchiffeSetzen() {
        this.schiffe = dieSteuerungSchiffeSetzen.getSchiffArray();
    }

    @Override
    public boolean isFertigSetzen() {
        return dieSteuerungSchiffeSetzen.isFertig();
    }
    
    public boolean gegnerKiIsFertig() {
        return kiGegner.isFertig();
    }

    public SteuerungSchiffeSetzen getDieSteuerungSchiffeSetzen() {
        return dieSteuerungSchiffeSetzen;
    }

    /**
     * Setze alle Schiffe Zurück
     */
    public void clearSchiffeSetzen() {
        for (int i = 0; i < dieSteuerungSchiffeSetzen.getSchiffArray().length; i++) { //Für jedes Schiff
            Schiff schiff = dieSteuerungSchiffeSetzen.getSchiffArray()[i];
            dieSteuerungSchiffeSetzen.clearId(schiff); //Lösche Marker ID auf Grid
            schiff.setFill(Color.RED); //Setzte die Farbe rot
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.drehen(i); //Drehen vertikale Schiff für exaxte Startposition
            }
        }
        dieSteuerungSchiffeSetzen.zeichneSchiffe(true); 
        dieSteuerungSchiffeSetzen.clearAll();
        //dieSteuerungSchiffeSetzen.getGridS().print(); //DEBUG
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
    
    public void makeHandler(Rectangle r){
        r.setOnMouseClicked(event -> clicked(event, r));
    }


    @Override
    public void beginneSpiel() {
        for(int i = spielfeldgroesse; i < 2*spielfeldgroesse; i++){
            for(int j = 0; j < spielfeldgroesse; j++){
                makeHandler(gridSpielfeld.getGrid()[i][j]);
            }
        }
        System.out.println("Beginne Spiel- Spieler startet");
        /*while(!spielEnde){
            while(aktiverSpieler == 0){
                if(gridSpielfeld.getLetzterShot()[0] != -1 && gridSpielfeld.getLetzterShot()[1] != -1){
                    System.out.println("Schuss von Spieler auf Zeile: " + gridSpielfeld.getLetzterShot()[0] + " Spalte: " + gridSpielfeld.getLetzterShot()[1]);
                    //aktiverSpieler = 1;
                }
            }
            //kiGegner.shot();
        }*/
    }
    
    public int antwort(int zeile, int spalte){
        //System.out.println("Schuss Ki auf : Zeile " + zeile + " Spalte: " + spalte + " ID: " + gridSpielfeld.getGrid()[spalte][zeile].getId());
        if(gridSpielfeld.getGrid()[spalte][zeile].getId().equals("0")){
            return 0;
        }
        else{
            return 1;
        } 
    }

    private void clicked(MouseEvent event, Rectangle rectangle) {
        int zeile = (int) event.getY() / gridSpielfeld.getKachelgroeße();
        int spalte = (int) (event.getX() - gridSpielfeld.getPxGroesse()) / gridSpielfeld.getKachelgroeße();
        int[] gegnerSchuss = {-1,-1};
        
        if(aktiverSpieler == 0){
            if(kiGegner.antwort(zeile, spalte) == 0){ // 0 is wasser, 1 schiffteil, 2 ist schiff versenkt
                rectangle.setFill(Color.BLUE);
            }
            else if(kiGegner.antwort(zeile, spalte) == 1 || kiGegner.antwort(zeile, spalte) == 2){
                String s = "/Images/nop.png";
                Image img = new Image(s);
                rectangle.setFill(new ImagePattern(img));
                //rectangle.setFill(Color.RED);
            }
            aktiverSpieler = 1;
        }
        if(aktiverSpieler == 1){
            gegnerSchuss = kiGegner.schiesse();
            if(antwort(gegnerSchuss[0], gegnerSchuss[1]) == 0){
                gridSpielfeld.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.BLUE);
            }
            else if(antwort(gegnerSchuss[0], gegnerSchuss[1]) == 1 || antwort(gegnerSchuss[0], gegnerSchuss[1]) == 2){
                //gridSpielfeld.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.RED);
                String s = "/Images/nop.png";
                Image img = new Image(s);
                gridSpielfeld.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(new ImagePattern(img));
            }
            aktiverSpieler = 0;
        }
    }
}
