/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import GUI.SpielGUIController;
import javafx.scene.paint.Color;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public abstract class SpielSteuerung {
    protected SpielGUIController dieGui = null;
    protected int spielfeldgroesse;
    protected int[] anzahlSchiffeTyp;
    protected Schiff[] schiffe;
    protected Grid gridSpielfeldRechts;
    protected Grid gridSpielfeldLinks;
    protected int anzSchiffe = 0;
    protected int anzGetroffen = 0;
    protected int aktiverSpieler = 0; // 0-> Spieler, 1-> Gegner
    protected int[][] getroffen;
    
    protected boolean spielEnde = false;

    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }
    
    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public boolean isSpielEnde() {
        return spielEnde;
    }

    public void setSpielEnde(boolean spielEnde) {
        this.spielEnde = spielEnde;
    }

    public void setGridSpielfeldRechts(Grid gridSpielfeld) {
        this.gridSpielfeldRechts = gridSpielfeld;
    }
    
    public void setGridSpielfeldLinks(Grid gridSpielfeld) {
        this.gridSpielfeldLinks = gridSpielfeld;
    }
    
    public void setGridSpielfeldSpielRechts(Grid gridSpielfeld){
        this.gridSpielfeldRechts = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldRechts.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldRechts.getGrid().length; j++) {
                dieGui.zeigeGridRechts(this.gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
    }
    
    public void enableMouseClickSoielfeldGridRechts(){
                this.gridSpielfeldRechts.enableMouseClick();
    }
    
    public void setGridSpielfeldSpielLinks(Grid gridSpielfeld){
        this.gridSpielfeldLinks = gridSpielfeld;
        for (int i = 0; i < this.gridSpielfeldLinks.getGrid().length; i++) {
            for (int j = 0; j < this.gridSpielfeldLinks.getGrid().length; j++) {
                dieGui.zeigeGridLinks(this.gridSpielfeldLinks.getGrid()[i][j]);
            }
        }
    }

    public SpielSteuerung(GUI.SpielGUIController gui) {
        //System.out.println("Steuerung erzeugt");
        this.dieGui = gui;
    }

    public abstract boolean isFertigSetzen();
    
    public Schiff[] getSchiffe() {
        return schiffe;
    }

    public int getSpielfeldgroesse() {
        return spielfeldgroesse;
    }

    public int[] getAnzahlSchiffeTyp() {
        return anzahlSchiffeTyp;
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        this.spielfeldgroesse = spielfeldgroesse;
    }

    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
    }
    
    

    public void setSchiffe(Schiff[] Schiffe) {
        this.schiffe = Schiffe;
    }
    
    public abstract void setSchiffeSetzen();
    
    public void setzeSchiffe() {
        for(Schiff schiff: schiffe){
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff);
        }
    }
    
    public void setzeSchiffeKI() {
        for(Schiff schiff: schiffe){
            schiff.draw(schiff.getStartX() * gridSpielfeldLinks.getKachelgroeße(), schiff.getStartY() * gridSpielfeldLinks.getKachelgroeße());
            if(schiff.getRichtung() == Richtung.VERTIKAL){
                schiff.dreheGui();
            }            
            //Rectangle req = new Rectangle((schiff.getX()), schiff.getY(), schiff.getWidth(), schiff.getHeight());
            //dieGui.zeigeSchiff(req);
            dieGui.zeichneSchiffe(schiff); // hier funktion auf kiSteuerung umändern 
        }
    }
    
    public boolean setzeSchiffsteilGetroffen(int zeile, int spalte){
        String schiffbezeichnung;
        int schiffnr = 0;
        int schiffindex = 0;
        schiffbezeichnung = gridSpielfeldLinks.getGrid()[spalte][zeile].getId();
        boolean versenkt;
        if(schiffbezeichnung.length() == 2){
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(1));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        }
        else if(schiffbezeichnung.length() == 3){
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0))*10 + Character.getNumericValue(schiffbezeichnung.charAt(1)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(2));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        }  
        versenkt = schiffe[schiffnr].handleTreffer(schiffindex);
        return versenkt;
    }
        
    public int antwort(int zeile, int spalte){
        //System.out.println("Schuss Ki auf : Zeile " + zeile + " Spalte: " + spalte + " ID: " + gridSpielfeld.getGrid()[spalte][zeile].getId());
        if(gridSpielfeldLinks.getGrid()[spalte][zeile].getId().equals("0")){
            return 0;
        }
        else{
            boolean vernichtet = setzeSchiffsteilGetroffen(zeile, spalte);
            if(vernichtet){
                return 2;
            }
            else{
                return 1;
            }
        } 
    }
    
    public abstract void erzeugeEigeneSchiffe();

    public abstract void beginneSpiel();
    
    public abstract int ueberpruefeSpielEnde();
    
    public void wasserUmSchiffRechts(int zeile, int spalte){
        //System.out.println("Wasser um Schiff");
        
        //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
        //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        
        int richtung; // 0 Horizontal 1 Vertikal
        int laenge;
        int[] position = new int[9]; // Wenn Horizontal: position ist spaltenwert, Wenn Vertikal: position ist zeilenwert
        boolean unterbrechung = false;
        
        if(spalte-1 >= 0 && !(gridSpielfeldRechts.getGrid()[spalte-1][zeile].getFill() instanceof Color)){
            // System.out.println("-1 :Color: " + gridSpielfeldRechts.getGrid()[spalte-1][zeile].getFill());
            richtung = 0;
        }
        else if(spalte+1 < spielfeldgroesse && !(gridSpielfeldRechts.getGrid()[spalte+1][zeile].getFill() instanceof Color)){
            // System.out.println("+1 :Color: " + gridSpielfeldRechts.getGrid()[spalte+1][zeile].getFill());
            richtung = 0;
        } 
        else{ 
            richtung = 1;
        }
        //System.out.println("Richtung: " + richtung);
        
        int counter = 4;
        int dif;
        if(richtung == 0){
            if(spalte > spielfeldgroesse-5){
                dif = spielfeldgroesse-1-spalte;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for(int i = spalte; i <= spalte+dif; i++){
                if(!(gridSpielfeldRechts.getGrid()[i][zeile].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                }
                else{
                    break;
                }
            }
            
            counter = 3;
            if(spalte < 4){
                dif = spalte;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for(int i = spalte-1; i >= spalte-dif; i--){
                if(!(gridSpielfeldRechts.getGrid()[i][zeile].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                }
                else{
                    break;
                }
            }
            
            counter++;
            for(int i = 0; i < 9; i++){
                if(position[i] == 1){
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }
        else if(richtung == 1){
            if(zeile > spielfeldgroesse-5){
                dif = spielfeldgroesse-1-zeile;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for(int i = zeile; i <= zeile+dif; i++){
                if(!(gridSpielfeldRechts.getGrid()[spalte][i].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                }
                else{
                    break;
                }
            }
            
            counter = 3;
            if(zeile < 4){
                dif = zeile;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for(int i = zeile-1; i >= zeile-dif; i--){
                if(!(gridSpielfeldRechts.getGrid()[spalte][i].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                }
                else{
                    break;
                }
            }
            
            counter++;
            for(int i = 0; i < 9; i++){
                if(position[i] == 1){
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }
        
        if(richtung == 0){ //hier noch verpessern dasss wenn nicht getroffen nicht meht in die Richtung
            for(int i = -4; i <= 4; i++){
                if(position[i+4] == 1){
                    if(zeile-1 >= 0){
                        gridSpielfeldRechts.getGrid()[spalte+i][zeile-1].setFill(Color.TRANSPARENT);
                        getroffen[zeile-1][spalte+i] = 1;
                    }
                    if(zeile+1 < spielfeldgroesse){
                        gridSpielfeldRechts.getGrid()[spalte+i][zeile+1].setFill(Color.TRANSPARENT);
                        getroffen[zeile+1][spalte+i] = 1;
                    }
                    if(spalte-1+i >= 0 && gridSpielfeldRechts.getGrid()[spalte-1+i][zeile].getFill() instanceof Color){
                        gridSpielfeldRechts.getGrid()[spalte-1+i][zeile].setFill(Color.TRANSPARENT);
                        getroffen[zeile][spalte-1+i] = 1;
                        if(zeile-1 >= 0){
                            gridSpielfeldRechts.getGrid()[spalte-1+i][zeile-1].setFill(Color.TRANSPARENT);
                            getroffen[zeile-1][spalte-1+i] = 1;
                        }
                        if(zeile+1 < spielfeldgroesse){
                            gridSpielfeldRechts.getGrid()[spalte-1+i][zeile+1].setFill(Color.TRANSPARENT);
                            getroffen[zeile+1][spalte-1+i] = 1;
                        }
                    } 
                    if(spalte+1+i < spielfeldgroesse && gridSpielfeldRechts.getGrid()[spalte+1+i][zeile].getFill() instanceof Color){
                        gridSpielfeldRechts.getGrid()[spalte+1+i][zeile].setFill(Color.TRANSPARENT);
                        getroffen[zeile][spalte+1+i] = 1;
                        if(zeile-1 >= 0){
                            gridSpielfeldRechts.getGrid()[spalte+1+i][zeile-1].setFill(Color.TRANSPARENT);
                            getroffen[zeile-1][spalte+1+i] = 1;
                        }
                        if(zeile+1 < spielfeldgroesse){
                            gridSpielfeldRechts.getGrid()[spalte+1+i][zeile+1].setFill(Color.TRANSPARENT);
                            getroffen[zeile+1][spalte+1+i] = 1;
                        }
                    }
                }
            }
        }
        else if(richtung == 1){ 
            for(int i = -4; i <= 4; i++){
                if(position[i+4] == 1){
                    if(spalte-1 >= 0){
                        gridSpielfeldRechts.getGrid()[spalte-1][zeile+i].setFill(Color.TRANSPARENT);
                        getroffen[zeile+i][spalte-1] = 1;
                    }
                    if(spalte+1 < spielfeldgroesse){
                        gridSpielfeldRechts.getGrid()[spalte+1][zeile+i].setFill(Color.TRANSPARENT);
                        getroffen[zeile+i][spalte+1] = 1;
                    }
                    if(zeile-1+i >= 0 && gridSpielfeldRechts.getGrid()[spalte][zeile-1+i].getFill() instanceof Color){
                        gridSpielfeldRechts.getGrid()[spalte][zeile-1+i].setFill(Color.TRANSPARENT);
                        getroffen[zeile-1+i][spalte] = 1;
                        if(spalte-1 >= 0){
                            gridSpielfeldRechts.getGrid()[spalte-1][zeile-1+i].setFill(Color.TRANSPARENT);
                            getroffen[zeile-1+i][spalte-1] = 1;
                        }
                        if(spalte+1 < spielfeldgroesse){
                            gridSpielfeldRechts.getGrid()[spalte+1][zeile-1+i].setFill(Color.TRANSPARENT);
                            getroffen[zeile-1+i][spalte+1] = 1;
                        }
                    } 
                    if(zeile+1+i < spielfeldgroesse && gridSpielfeldRechts.getGrid()[spalte][zeile+1+i].getFill() instanceof Color){
                        gridSpielfeldRechts.getGrid()[spalte][zeile+1+i].setFill(Color.TRANSPARENT);
                        getroffen[zeile+1+i][spalte] = 1;
                        if(spalte-1 >= 0){
                            gridSpielfeldRechts.getGrid()[spalte-1][zeile+1+i].setFill(Color.TRANSPARENT);
                            getroffen[zeile+1+i][spalte-1] = 1;
                        }
                        if(spalte+1 < spielfeldgroesse){
                            gridSpielfeldRechts.getGrid()[spalte+1][zeile+1+i].setFill(Color.TRANSPARENT);
                            getroffen[zeile+i+1][spalte+1] = 1;
                        }
                    }
                }
            }
        }
    }
    
    public void wasserUmSchiffLinksKI(int zeile, int spalte, KI ki){
        //System.out.println("Wasser um Schiff Links");
        
        //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
        //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        
        int richtung; // 0 Horizontal 1 Vertikal
        int laenge;
        int[] position = new int[9]; // Wenn Horizontal: position ist spaltenwert, Wenn Vertikal: position ist zeilenwert
        boolean unterbrechung = false;
        
        if(spalte-1 >= 0 && !(gridSpielfeldLinks.getGrid()[spalte-1][zeile].getFill() instanceof Color)){
            // System.out.println("-1 :Color: " + gridSpielfeldRechts.getGrid()[spalte-1][zeile].getFill());
            richtung = 0;
        }
        else if(spalte+1 < spielfeldgroesse && !(gridSpielfeldLinks.getGrid()[spalte+1][zeile].getFill() instanceof Color)){
            // System.out.println("+1 :Color: " + gridSpielfeldRechts.getGrid()[spalte+1][zeile].getFill());
            richtung = 0;
        } 
        else{ 
            richtung = 1;
        }
        //System.out.println("Richtung: " + richtung);
        
        int counter = 4;
        int dif;
        if(richtung == 0){
            if(spalte > spielfeldgroesse-5){
                dif = spielfeldgroesse-1-spalte;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for(int i = spalte; i <= spalte+dif; i++){
                if(!(gridSpielfeldLinks.getGrid()[i][zeile].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                }
                else{
                    break;
                }
            }
            
            counter = 3;
            if(spalte < 4){
                dif = spalte;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for(int i = spalte-1; i >= spalte-dif; i--){
                if(!(gridSpielfeldLinks.getGrid()[i][zeile].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                }
                else{
                    break;
                }
            }
            
            counter++;
            for(int i = 0; i < 9; i++){
                if(position[i] == 1){
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }
        else if(richtung == 1){
            if(zeile > spielfeldgroesse-5){
                dif = spielfeldgroesse-1-zeile;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif1: " + dif);
            for(int i = zeile; i <= zeile+dif; i++){
                if(!(gridSpielfeldLinks.getGrid()[spalte][i].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter++;
                }
                else{
                    break;
                }
            }
            
            counter = 3;
            if(zeile < 4){
                dif = zeile;
            }
            else{
                dif = 4;
            }
            //System.out.println("Dif2: " + dif);
            for(int i = zeile-1; i >= zeile-dif; i--){
                if(!(gridSpielfeldLinks.getGrid()[spalte][i].getFill() instanceof Color)){
                    position[counter] = 1;
                    //System.out.println("Poistion an Index: " + counter + " ist " + 1);
                    counter--;
                }
                else{
                    break;
                }
            }
            
            counter++;
            for(int i = 0; i < 9; i++){
                if(position[i] == 1){
                    counter++;
                }
                //System.out.print(position[i] + " ");
            }
            //System.out.println("");
        }
        
        if(richtung == 0){
            for(int i = -4; i <= 4; i++){
                if(position[i+4] == 1){ // Problem mit color aber nur horizontal
                    if(zeile-1 >= 0){
                        gridSpielfeldLinks.getGrid()[spalte+i][zeile-1].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile-1, spalte+i);
                    }
                    if(zeile+1 < spielfeldgroesse){
                        gridSpielfeldLinks.getGrid()[spalte+i][zeile+1].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile+1, spalte+i);
                    }
                    if(spalte-1+i >= 0 && gridSpielfeldLinks.getGrid()[spalte-1+i][zeile].getFill() instanceof Color){
                        gridSpielfeldLinks.getGrid()[spalte-1+i][zeile].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile, spalte-1+i);
                        if(zeile-1 >= 0){
                            gridSpielfeldLinks.getGrid()[spalte-1+i][zeile-1].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile-1, spalte-1+i);
                        }
                        if(zeile+1 < spielfeldgroesse){
                            gridSpielfeldLinks.getGrid()[spalte-1+i][zeile+1].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile+1, spalte-1+i);
                        }
                    } 
                    if(spalte+1+i < spielfeldgroesse && gridSpielfeldLinks.getGrid()[spalte+1+i][zeile].getFill() instanceof Color){
                        gridSpielfeldLinks.getGrid()[spalte+1+i][zeile].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile, spalte+1+i);
                        if(zeile-1 >= 0){
                            gridSpielfeldLinks.getGrid()[spalte+1+i][zeile-1].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile-1, spalte+1+i);
                        }
                        if(zeile+1 < spielfeldgroesse){
                            gridSpielfeldLinks.getGrid()[spalte+1+i][zeile+1].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile+1, spalte+1+i);
                        }
                    }
                }
            }
        }
        else if(richtung == 1){ 
            for(int i = -4; i <= 4; i++){
                if(position[i+4] == 1){
                    if(spalte-1 >= 0){
                        gridSpielfeldLinks.getGrid()[spalte-1][zeile+i].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile+i, spalte-1);
                    }
                    if(spalte+1 < spielfeldgroesse){
                        gridSpielfeldLinks.getGrid()[spalte+1][zeile+i].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile+i, spalte+1);
                    }
                    if(zeile-1+i >= 0 && gridSpielfeldLinks.getGrid()[spalte][zeile-1+i].getFill() instanceof Color){
                        gridSpielfeldLinks.getGrid()[spalte][zeile-1+i].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile-1+i, spalte);
                        if(spalte-1 >= 0){
                            gridSpielfeldLinks.getGrid()[spalte-1][zeile-1+i].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile-1+i, spalte-1);
                        }
                        if(spalte+1 < spielfeldgroesse){
                            gridSpielfeldLinks.getGrid()[spalte+1][zeile-1+i].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile-1+i, spalte+1);
                        }
                    } 
                    if(zeile+1+i < spielfeldgroesse && gridSpielfeldLinks.getGrid()[spalte][zeile+1+i].getFill() instanceof Color){
                        gridSpielfeldLinks.getGrid()[spalte][zeile+1+i].setFill(Color.TRANSPARENT);
                        ki.setGetroffen(zeile+1+i, spalte);
                        if(spalte-1 >= 0){
                            gridSpielfeldLinks.getGrid()[spalte-1][zeile+1+i].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile+1+i, spalte-1);
                        }
                        if(spalte+1 < spielfeldgroesse){
                            gridSpielfeldLinks.getGrid()[spalte+1][zeile+1+i].setFill(Color.TRANSPARENT);
                            ki.setGetroffen(zeile+1+i, spalte+1);
                        }
                    }
                }
            }
        }
    }
}
