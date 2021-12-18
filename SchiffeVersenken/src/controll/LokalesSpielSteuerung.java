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
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class LokalesSpielSteuerung extends SpielSteuerung{
   
    private SchiffeSetzen dieSteuerungSchiffeSetzen = null;
    private KI kiGegner;
    
    public LokalesSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        super(gui);
        System.out.println("LokalesSpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        this.dieSteuerungSchiffeSetzen = new SchiffeSetzen(gui, anzahlSchiffeTyp, spielfeldgroesse);
        this.kiGegner = new KI(spielfeldgroesse, anzahlSchiffeTyp);
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
    }
    
    @Override
    public void erzeugeEigeneSchiffe(){
        dieSteuerungSchiffeSetzen.drawAll();
    }
    
    
    public void erzeugeGegnerSchiffe() {
        kiGegner.erzeugeEigeneSchiffe();
        System.out.println("Gegnerfeld");
        kiGegner.getGridSpielfeldLinks().print();
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

    public SchiffeSetzen getDieSteuerungSchiffeSetzen() {
        return dieSteuerungSchiffeSetzen;
    }

    /**
     * Setze alle Schiffe Zurück
     */
    public void clearSchiffeSetzen() {
        Schiff schiff;
        for (int i = 0; i < dieSteuerungSchiffeSetzen.getSchiffArray().length; i++) { 
            schiff = dieSteuerungSchiffeSetzen.getSchiffArray()[i];
            schiff.setFill(Color.RED);
            if(schiff.getRichtung() == Richtung.VERTIKAL){
                schiff.setRichtung(Richtung.HORIZONTAL);
                schiff.dreheGui();
            }
            schiff.setGesetzt(false);
        }
        dieSteuerungSchiffeSetzen.zeichneSchiffe(true); 
        dieSteuerungSchiffeSetzen.clearAll();
        dieSteuerungSchiffeSetzen.setFertig(false);
        
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
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße());
            schiff.setGesetzt(true);
        }
    }
    
    public void makeHandler(Rectangle r){
        r.setOnMouseClicked(event -> clicked(event, r));
    }


    @Override
    public void beginneSpiel() {
        for(int i = 0; i < spielfeldgroesse; i++){
            for(int j = 0; j < spielfeldgroesse; j++){
                makeHandler(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
        System.out.println("Beginne LokalesSpiel- Spieler startet");
    }

    private void clicked(MouseEvent event, Rectangle rectangle) {
        //System.out.println("Clicked");
        int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
        int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
        int[] gegnerSchuss = {-1,-1};
        int antwort;
        
        if(aktiverSpieler == 0 && getroffen[zeile][spalte] == 0){
            antwort = kiGegner.antwort(zeile, spalte);
            if(antwort == 0){ // 0 is wasser, 1 schiffteil, 2 ist schiff versenkt
                rectangle.setFill(Color.TRANSPARENT);
            }
            else if(antwort == 1 || antwort == 2){
                Image img = new Image("/Images/nop.png");
                rectangle.setFill(new ImagePattern(img));
                if(antwort == 2){
                    anzGetroffen++;
                    wasserUmSchiff(zeile,spalte);
                }
            }
            getroffen[zeile][spalte] = 1;
            aktiverSpieler = 1;
            //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
            //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        }
        if(aktiverSpieler == 1){
            gegnerSchuss = kiGegner.schiesse(false, null);
            antwort = antwort(gegnerSchuss[0], gegnerSchuss[1]);
            if(antwort == 0){
                gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.TRANSPARENT);
            }
            else if(antwort == 1 || antwort == 2){
                Image img = new Image("/Images/nop.png");
                gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(new ImagePattern(img));
                if(antwort == 2){
                    kiGegner.setAnzGetroffenHoeher();
                }
            } // Weiter schießen da gteroffen
            while(antwort != 0){
                gegnerSchuss = kiGegner.schiesse(true, gegnerSchuss);
                antwort = antwort(gegnerSchuss[0], gegnerSchuss[1]); 
                if(antwort == 0){
                    gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.TRANSPARENT);
                }
                else if(antwort == 1 || antwort == 2){
                    Image img = new Image("/Images/nop.png");
                    gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(new ImagePattern(img));
                    if(antwort == 2){
                        kiGegner.setAnzGetroffenHoeher();
                    }
                }
            }
            aktiverSpieler = 0;
        }
        int ende = ueberpruefeSpielEnde();
        if(ende != 0){
            dieGui.spielEnde(ende);
        }
    }

    @Override
    public int ueberpruefeSpielEnde() {
        if(anzSchiffe == kiGegner.getAnzGetroffen()){
            //System.out.println("Gegner gewonnen");
            return 1;
        }
        else if(anzSchiffe == anzGetroffen){
            //System.out.println("Spieler gewonnen");
            return 2;
        }
        return 0;
    }
}
