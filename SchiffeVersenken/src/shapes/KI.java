/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.Random;

/**
 *
 * @author esmay
 */
public class KI {
    private int spielfeldgroesse;
    private Schiff[] schiffArray;
    private int[] anzahlSchiffeTyp;
    private boolean fertig = true;
    private int anzahlSchiffe;
    private Grid gridSpielfeld;
    private int[][] feldEigen;

    public KI(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzahlSchiffe += anzahlSchiffeTyp[i];
        }
        gridSpielfeld = new Grid(spielfeldgroesse);
        gridSpielfeld.macheGrid(); 
        feldEigen = new int[this.spielfeldgroesse][this.spielfeldgroesse];
    }

    public void erzeugeEigeneSchiffe() {
        boolean allegesetzt = false;
        int anzahlgesetzt = 0;
        Random zufall = new Random();
        int zufallx;
        int zufally; 
        int zufallsrichtung; // horizontal--- -> 0, vertikal||| -> 1
        
        schiffArray = new Schiff[anzahlSchiffe];
        int ctn = 0;
        for (int i = 0; i < anzahlSchiffeTyp[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße());
        }
        
        while(!allegesetzt){
            for(int i = 0; i < schiffArray.length; i++){
                while(anzahlgesetzt < i+1){
                    do{
                        zufallx = zufall.nextInt(spielfeldgroesse);
                        zufally = zufall.nextInt(spielfeldgroesse);
                        //zufallx = 0;
                        //zufally = 0;
                    }
                    while(feldEigen[zufally][zufallx] != 0);

                    zufallsrichtung = zufall.nextInt(2);
                    //zufallsrichtung = 0;

                    System.out.println("x: " + zufallx + " y: " + zufally + " richtung: " + zufallsrichtung);
                    switch(schiffArray[i].getLaenge()){
                        case 2:
                            if(zufallsrichtung == 0){ // Horizontal---
                                if(zufallx < spielfeldgroesse-1 && feldEigen[zufally][zufallx+1] == 0){ // 9 ist spielfeldgroesse -1
                                    setFeldEigen(zufally, zufallx, i, 2, zufallsrichtung);
                                    anzahlgesetzt++;
                                    //legeWasserUmSchiff(zufally, zufallx, 2, zufallsrichtung);
                                }
                                else if(zufally < spielfeldgroesse-1 && feldEigen[zufally+1][zufallx] == 0){
                                    zufallsrichtung = 1;
                                    setFeldEigen(zufally, zufallx, i, 2, zufallsrichtung);
                                    anzahlgesetzt++;
                                    //legeWasserUmSchiff(zufally, zufallx, 2, zufallsrichtung);
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            else if(zufallsrichtung == 1){ // Vertikal|||
                                if(zufally < spielfeldgroesse-1 && feldEigen[zufally+1][zufallx] == 0 ){
                                    setFeldEigen(zufally, zufallx, i, 2, zufallsrichtung);
                                    anzahlgesetzt++;
                                    //legeWasserUmSchiff(zufally, zufallx, 2, zufallsrichtung);
                                }
                                else if(zufallx < spielfeldgroesse-1 && feldEigen[zufally][zufallx+1] == 0 ){
                                    zufallsrichtung = 0;
                                    setFeldEigen(zufally, zufallx, i, 2, zufallsrichtung);
                                    anzahlgesetzt++;
                                    //legeWasserUmSchiff(zufally, zufallx, 2, zufallsrichtung);
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            break;
                        case 3:
                            if(zufallsrichtung == 0){ // Horizontal---
                                if(zufallx < spielfeldgroesse-2 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0){
                                    setFeldEigen(zufally, zufallx, i, 3, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufally < spielfeldgroesse-2 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0){
                                    zufallsrichtung = 1;
                                    setFeldEigen(zufally, zufallx, i, 3, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            else if(zufallsrichtung == 1){ // Vertikal|||
                                if(zufally < spielfeldgroesse-2 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0){
                                    setFeldEigen(zufally, zufallx, i, 3, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufallx < spielfeldgroesse-2 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0){
                                    zufallsrichtung = 0;
                                    setFeldEigen(zufally, zufallx, i, 3, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            break;
                        case 4:
                            if(zufallsrichtung == 0){ // Horizontal---
                                if(zufallx < spielfeldgroesse-3 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0 && feldEigen[zufally][zufallx+3] == 0){
                                    setFeldEigen(zufally, zufallx, i, 4, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufally < spielfeldgroesse-3 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0 && feldEigen[zufally+3][zufallx] == 0){
                                    zufallsrichtung = 1;
                                    setFeldEigen(zufally, zufallx, i, 4, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            else if(zufallsrichtung == 1){ // Vertikal|||
                                if(zufally < spielfeldgroesse-3 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0 && feldEigen[zufally+3][zufallx] == 0){
                                    setFeldEigen(zufally, zufallx, i, 4, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufallx < spielfeldgroesse-3 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0 && feldEigen[zufally][zufallx+3] == 0){
                                    zufallsrichtung = 0;
                                    setFeldEigen(zufally, zufallx, i, 4, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            break;
                        case 5:
                            if(zufallsrichtung == 0){ // Horizontal---
                                if(zufallx < spielfeldgroesse-4 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0 && feldEigen[zufally][zufallx+3] == 0 && feldEigen[zufally][zufallx+4] == 0){
                                    setFeldEigen(zufally, zufallx, i, 5, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufally < spielfeldgroesse-4 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0 && feldEigen[zufally+3][zufallx] == 0 && feldEigen[zufally+4][zufallx] == 0){
                                    zufallsrichtung = 1;
                                    setFeldEigen(zufally, zufallx, i, 5, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            else if(zufallsrichtung == 1){ // Vertikal|||
                                if(zufally < spielfeldgroesse-4 && feldEigen[zufally+1][zufallx] == 0 && feldEigen[zufally+2][zufallx] == 0 && feldEigen[zufally+3][zufallx] == 0 && feldEigen[zufally+4][zufallx] == 0){
                                    setFeldEigen(zufally, zufallx, i, 5, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else if(zufallx < spielfeldgroesse-4 && feldEigen[zufally][zufallx+1] == 0 && feldEigen[zufally][zufallx+2] == 0 && feldEigen[zufally][zufallx+3] == 0 && feldEigen[zufally][zufallx+4] == 0){
                                    zufallsrichtung = 0;
                                    setFeldEigen(zufally, zufallx, i, 5, zufallsrichtung);
                                    anzahlgesetzt++;
                                }
                                else{
                                    System.out.println("Nicht gesetzt");
                                }
                            }
                            break;
                        default:
                            System.err.println("Fehler Schiffgroesse");
                    }
                }
            }
            allegesetzt = true;
        }
    }
    
    private void setFeldEigen(int zufally, int zufallx, int i, int oft, int richtung){
        for(int j = 0; j < oft; j++){
            if(richtung == 1){
                feldEigen[zufally+j][zufallx] = (i+1)*10+j+1;
            }
            else if(richtung == 0){
                feldEigen[zufally][zufallx+j] = (i+1)*10+j+1;
            }
        }
    }

    public void ausgebenEingenesFeld(){
        System.err.println("Spielfeld ausgeben");
        for(int i = 0; i < spielfeldgroesse; i++){
            for(int j = 0; j < spielfeldgroesse; j++){
                System.out.print(feldEigen[i][j] + "\t|\t");
            }
            System.out.println("\n---------------------------------------------------------------------------------------------------------");
        }
    }
    
    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public boolean isFertig() {
        return fertig;
    }
    
    
}
