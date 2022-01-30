/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

import java.util.Random;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 *
 * @author esmay
 */
public class KI {

    private int spielfeldgroesse;
    private int[] anzahlSchiffeTyp;
    private boolean fertig = false;
    private Schiff[] schiffArray;
    private int anzSchiffe = 0;
    private Grid gridSpielfeldRechts;
    private Grid gridSpielfeldLinks;
    
    private int[][] getroffen; // 0 noch nicht bekannt, 1 ist Wasser, 2 ist Schif
    private int[] letzterSchuss = new int[2];
    private int[] angefangenesSchiffSchuss = new int[2];
    private int anzGetroffen;
    private boolean angefangesSchiff = false;
    private Richtung angefangenesSchiffRichtung;
    private int variable = 0;
    private int kiStufe;

    /**
     * Konstruktor für die KI 
     * 
     * @param spielfeldgroesse Spielfeldgröße zwischen 5 und 30
     * @param anzahlSchiffeTyp Anzahl der Schiffe je Typ
     * @param kiStufe Ki-Stufe welche im Modi-Menü ausgewählt wurde
     */
    public KI(int spielfeldgroesse, int[] anzahlSchiffeTyp, int kiStufe) {
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        this.kiStufe = kiStufe;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        gridSpielfeldRechts = new Grid(spielfeldgroesse);
        gridSpielfeldLinks = new Grid(spielfeldgroesse);
        gridSpielfeldRechts.macheGridRechts();
        gridSpielfeldLinks.macheGridLinks();
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
    }

    /**
     * Erzeugen der eigenen Schiffe und zufälliges Setzen der Schiffe auf dem Spielfeld.
     */
    public void erzeugeEigeneSchiffe() {
        int wiederholungen = 0;
        boolean allegesetzt = false;
        int anzahlgesetzt = 0;
        Random zufall = new Random();
        int zufallx;
        int zufally;
        int zufallsrichtung; // horizontal--- -> 0, vertikal||| -> 1

        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < anzahlSchiffeTyp[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße());
        }
        for (int i = 0; i < anzahlSchiffeTyp[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße());
        }

        while (!allegesetzt) {
            for (int i = schiffArray.length - 1; i >= 0; i--) {
                if (wiederholungen > 30 && schiffArray[i + 1].getLaenge() != 2) {
                    break;
                } else if (wiederholungen > 60 && schiffArray[i + 1].getLaenge() == 2) {
                    break;
                }
                wiederholungen = 0;
                while (anzahlgesetzt < schiffArray.length - i) { // anzahlgesetzt = 0 
                    do {
                        zufallx = zufall.nextInt(spielfeldgroesse);
                        zufally = zufall.nextInt(spielfeldgroesse);
                        schiffArray[i].setStart(zufallx, zufally);
                        zufallsrichtung = zufall.nextInt(2);
                        if (zufallsrichtung == 0) {
                            schiffArray[i].setRichtung(Richtung.HORIZONTAL);
                        } else if (zufallsrichtung == 1) {
                            schiffArray[i].setRichtung(Richtung.VERTIKAL);
                        }
                    } while (!setIdNeu(schiffArray[i], i)); 
                    if (zufallsrichtung == 0) { // Horizontal ---
                        if (ueberpruefePlatzHorizontal(schiffArray[i])) {
                            anzahlgesetzt++;
                        } else {
                            clearId(schiffArray[i]);
                        }
                    } else if (zufallsrichtung == 1) { // Vertikal |||
                        if (ueberpruefePlatzVertikal(schiffArray[i])) {
                            anzahlgesetzt++;
                        } else {
                            clearId(schiffArray[i]);
                        }
                    }
                    wiederholungen++;
                    if (wiederholungen > 30 && schiffArray[i].getLaenge() != 2) {
                        break;
                    } else if (wiederholungen > 60 && schiffArray[i].getLaenge() == 2) {
                        break;
                    }
                }
            }
            if (anzahlgesetzt == schiffArray.length) {
                allegesetzt = true;
                fertig = true;
            } else {
                wiederholungen = 0;
                clearAll();
                anzahlgesetzt = 0;
            }
        }
    }

    /**
     * Hier wird ausgewählt welche Schwierigkeitsstufe an KI schießt.
     * Übergeben wird die Antwort des Schusses davor, 
     * denn Stufe 2 und 3 vervollständigen jedes angefange Schiff erst komplett
     * bevor ein neues gesucht wird. Daher ist diese Übergabe wichtig.
     * 
     * @param antwortDavor Antwort letzer Schuss
     * @return Schuss (Array mit Größe 2 speichert Zeile und Spalte des Schusses)
     */
    public int[] schiesse(int antwortDavor){
        if(kiStufe == 1){
            return schiesseStufeEins();
        }
        else if(kiStufe == 2){
            return schiesseStufeZwei(antwortDavor);
        }
        else if(kiStufe == 3){
            return schiesseStufeDrei(antwortDavor);
        }
        return null;
    }

    /**
     * Schießt von oben links der Reihe nach nach unten rechts.
     * 
     * @return Schuss (Array mit Größe 2 speichert Zeile und Spalte des Schusses)
     */
    public int[] schiesseReihe() {
        int[] schuss = new int[2]; // [Zeile row, Spalte col]
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                if (getroffen[i][j] == 0) {
                    schuss[0] = i;
                    schuss[1] = j;
                    getroffen[i][j] = 1;
                    return schuss;
                }
            }
        }
        return null;
    }

    /**
     * Schießt zufällig unabhängig davon ob ein Schiff getroffen und noch nicht versenkt wurde
     * oder nicht.
     * 
     * @return Schuss (Array mit Größe 2 speichert Zeile und Spalte des Schusses) 
     */
    public int[] schiesseStufeEins() {
        Random zufallx = new Random();
        Random zufally = new Random();
        int[] schuss = new int[2]; // [Zeile row, Spalte col]
        int stelleX;
        int stelleY;

        for (int j = 0; j < getroffen.length * getroffen.length; j++) {
            //Erstellung zufallsPunkt
            stelleX = zufallx.nextInt(spielfeldgroesse - 1);
            stelleY = zufally.nextInt(spielfeldgroesse - 1);

            //schauen ob der Punkt bereits aufgetaucht ist
            if (getroffen[stelleX][stelleY] != 0) {

                variable = 0;

                while (getroffen[stelleX + variable][stelleY] != 0) {
                    if (stelleX + variable == getroffen.length - 1) {
                        //um eine Zeile nach unten verschieben und dort weitersuchen

                        variable = 0;
                        stelleX = 0;
                        stelleY += 1;

                        if (stelleY > getroffen.length - 1) {
                            stelleY = 0;
                        }

                    } else {
                        variable++;
                    }

                }
                schuss[0] = stelleX + variable;
                schuss[1] = stelleY;
                getroffen[stelleX + variable][stelleY] = 1;
                return schuss;

            } else {
                schuss[0] = stelleX;
                schuss[1] = stelleY;
                getroffen[stelleX][stelleY] = 1;
                return schuss;
            }

        }
        return null;
    }

    /**
     * Schießt zufällig, außer die KI hat im Schuss zuvor getroffen, dann vervollständigt sie 
     * dieses Schiff bis es untergegangen ist. Erst dann wird wieder zufällig geschossen.
     * 
     * @param antwortDavor 0: Wasser 1: Schiffsteil getroffen 2: Schiff versenkt
     * @return Schuss (Array mit Größe 2 speichert Zeile und Spalte des Schusses)
     */
    public int[] schiesseStufeZwei(int antwortDavor){
        Random zufallx = new Random();
        Random zufally = new Random();
        int[] schuss = new int[2]; // [Zeile row, Spalte col]
        int stelleX;
        int stelleY;

        if(angefangesSchiff == false && antwortDavor == 1){
            angefangesSchiff = true;
            angefangenesSchiffSchuss = letzterSchuss;
            angefangenesSchiffRichtung = null;
        }
        else if(antwortDavor == 2){
            angefangesSchiff = false;
            angefangenesSchiffSchuss = new int[2];
            angefangenesSchiffRichtung = null;
        }
        
        if(angefangesSchiff == false){
            for (int j = 0; j < getroffen.length * getroffen.length; j++) {
                //Erstellung zufallsPunkt
                stelleX = zufallx.nextInt(spielfeldgroesse - 1);
                stelleY = zufally.nextInt(spielfeldgroesse - 1);

                //schauen ob der Punkt bereits aufgetaucht ist
                if (getroffen[stelleX][stelleY] != 0) {

                    variable = 0;

                    while (getroffen[stelleX + variable][stelleY] != 0) {
                        if (stelleX + variable == getroffen.length - 1) {
                            //um eine Zeile nach unten verschieben und dort weitersuchen

                            variable = 0;
                            stelleX = 0;
                            stelleY += 1;

                            if (stelleY > getroffen.length - 1) {
                                stelleY = 0;
                            }

                        } else {
                            variable++;
                        }

                    }
                    schuss[0] = stelleX + variable;
                    schuss[1] = stelleY;
                    getroffen[stelleX + variable][stelleY] = 1;
                    letzterSchuss = schuss;
                    return schuss;

                } 
                else {
                    schuss[0] = stelleX;
                    schuss[1] = stelleY;
                    getroffen[stelleX][stelleY] = 1;
                    letzterSchuss = schuss;
                    return schuss;
                }
            }
        }
        else if(angefangesSchiff == true){
            if(angefangenesSchiffRichtung == null){
                if (angefangenesSchiffSchuss[1] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Rechts
                    System.out.println("Rechts: "+ getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1]);
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] = 1;
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[1] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Links
                    System.out.println("Links: " + getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1]);
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] = 1;
                        return schuss;
                    }
                }
                if(angefangenesSchiffSchuss[0] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Unten
                    System.out.println("Unten: " + getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]]);
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[0] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Oben
                    System.out.println("Oben: " + getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]]);
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
            }
            if(angefangenesSchiffRichtung == Richtung.HORIZONTAL){
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[1] + i >= spielfeldgroesse){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + i;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] = 1;
                        return schuss;
                    }
                }
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[1] - i < 0){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - i;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] = 1;
                        return schuss;
                    }
                }
            }
            else if(angefangenesSchiffRichtung == Richtung.VERTIKAL){
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[0] + i >= spielfeldgroesse){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + i;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[0] - i < 0){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - i;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
            }
               
        }
        System.out.println("Null");
        return null;
    }
    
    /**
     * Schießt nach einem Kreuzmuster und vervollständigt anschließend im Schachbrettmuster alle Felder.
     * Hat die KI im Schuss zuvor getroffen, dann vervollständigt sie 
     * dieses Schiff bis es untergegangen ist. Erst dann wird wieder im Muster weitergeschossen.
     * 
     * @param antwortDavor 0: Wasser 1: Schiffsteil getroffen 2: Schiff versenkt
     * @return Schuss (Array mit Größe 2 speichert Zeile und Spalte des Schusses)
     */
    public int[] schiesseStufeDrei(int antwortDavor) {  // Dekt noch nicht alle 2er felder ab
        int[] schuss = new int[2]; // [Zeile row, Spalte col]

        if(angefangesSchiff == false && antwortDavor == 1){
            angefangesSchiff = true;
            angefangenesSchiffSchuss = letzterSchuss;
            angefangenesSchiffRichtung = null;
        }
        else if(antwortDavor == 2){
            angefangesSchiff = false;
            angefangenesSchiffSchuss = new int[2];
            angefangenesSchiffRichtung = null;
        }

        if(angefangesSchiff == false){
            for(int i = 0; i < getroffen.length; i+= 1){
                if (getroffen[i][i] == 0) {
                    schuss[0] = i;
                    schuss[1] = i;
                    getroffen[i][i] = 1;
                    letzterSchuss = schuss;
                    return schuss;
                }
            }
        
            for(int i = 0; i < getroffen.length; i+= 1){
                if (getroffen.length - (i+1) - (getroffen.length+1) % 2 >= 0 && getroffen[i][getroffen.length - (i+1) - (getroffen.length+1) % 2] == 0) {
                    schuss[0] = i;
                    schuss[1] = getroffen.length - (i+1) - (getroffen.length+1) % 2;
                    getroffen[i][getroffen.length - (i+1) - (getroffen.length+1) % 2] = 1;
                    letzterSchuss = schuss;
                    return schuss;
                }
            }
        
            for (int i = 0; i < getroffen.length; i += 2) {
                for (int j = 0; j < getroffen.length; j += 2) {
                    if (getroffen[i][j] == 0) {
                        schuss[0] = i;
                        schuss[1] = j;
                        getroffen[i][j] = 1;
                        letzterSchuss = schuss;
                        return schuss;
                    }
                }
            }

            for (int i = 1; i < getroffen.length; i += 2) {
                for (int j = 1; j < getroffen.length; j += 2) {
                    if (getroffen[i][j] == 0) {
                        schuss[0] = i;
                        schuss[1] = j;
                        getroffen[i][j] = 1;
                        letzterSchuss = schuss;
                        return schuss;
                    }
                }
            }
        } 
        else if (angefangesSchiff) {
            if(angefangenesSchiffRichtung == null){
                if (angefangenesSchiffSchuss[1] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Rechts
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] = 1;
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[1] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Links
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] = 1;
                        return schuss;
                    }
                }
                if(angefangenesSchiffSchuss[0] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Unten
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[0] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Oben
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
            }
            if(angefangenesSchiffRichtung == Richtung.HORIZONTAL){
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[1] + i >= spielfeldgroesse){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + i;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] + i] = 1;
                        return schuss;
                    }
                }
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[1] - i < 0){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - i;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - i] = 1;
                        return schuss;
                    }
                }
            }
            else if(angefangenesSchiffRichtung == Richtung.VERTIKAL){
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[0] + i >= spielfeldgroesse){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + i;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + i][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
                for(int i = 1; i < 5; i++){
                    if(angefangenesSchiffSchuss[0] - i < 0){
                        break;
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] == 1){
                        break;
                    }
                    else if(getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - i;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - i][angefangenesSchiffSchuss[1]] = 1;
                        return schuss;
                    }
                }
            }
        }
        
        System.out.println("Null");
        return null;
    }

    /**
     * Mit dieser Funktion gibt die KI dem Spieler eine Antwort, ob er getroffen hat oder nicht, bzw. ob ein Schiff
     * vollständig versenkt wurde.
     * 
     * @param zeile Zeile des Schusses
     * @param spalte Spalte des Schusses
     * @return 0: Wasser, 1: Schiffsteil getroffen, 2: Schiff versenkt
     */
    public int antwort(int zeile, int spalte) {
        if (gridSpielfeldLinks.getGrid()[spalte][zeile].getId().equals("0")) {
            return 0;
        } else {
            boolean vernichtet = setzeSchiffsteilGetroffen(zeile, spalte);
            if (vernichtet) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    /**
     * Trifft der Gegner ein Schiffsteil, so wird dieses Teil des Schiffs getroffen gesetzt.
     * Daher setzt diese Funktion das entsprechende Trefferarray des getroffenen Schiffs an dieser Stelle 1.
     * 
     * @param zeile Zeile des Schuss bei dem es einen Treffer gab
     * @param spalte Spalte des Schuss bei dem es einen Treffer gab
     * @return true wenn Schiff vollständig versenkt, false wenn noch nicht ganz versenkt
     */
    public boolean setzeSchiffsteilGetroffen(int zeile, int spalte) {
        String schiffbezeichnung;
        int schiffnr = 0;
        int schiffindex = 0;
        schiffbezeichnung = gridSpielfeldLinks.getGrid()[spalte][zeile].getId();
        boolean versenkt;
        if (schiffbezeichnung.length() == 2) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(1));
        } else if (schiffbezeichnung.length() == 3) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) * 10 + Character.getNumericValue(schiffbezeichnung.charAt(1)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(2));
        }
        versenkt = schiffArray[schiffnr].handleTreffer(schiffindex);
        return versenkt;
    }

    /**
     * Löscht die passenden Ids zu einem Schiff vom Grid
     * 
     * @param s Schiff
     */
    public void clearId(Schiff s) {
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[i][s.getStartY()].setId("0");
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[s.getStartX()][i].setId("0");
            }
        }
    }

    /**
     * Alle ids auf dem Grid löschen. Wird benötigt um nochmals alle Schiffe von neu zu setzen.
     */
    public void clearAll() {
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                gridSpielfeldLinks.getGrid()[i][j].setId("0");
            }
        }
    }

    /**
     * Diese Funktion schaut ob am Zufallsplatz schon ein Schiff zu finden ist, wenn ja, liefert die Funktion true zurück und 
     * macht nichts. Andersnfalls setzt die Methode die entsprechende Id aufs Grid.
     * 
     * @param s Schiff
     * @param index index des Schiffs beim Erstellen
     * @return Boolean ob Platz an dieser Stelle noch frei
     */
    public boolean setIdNeu(Schiff s, int index) {
        int counter = 0;
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                if (i < gridSpielfeldLinks.getKachelAnzahl()) {
                    if (!gridSpielfeldLinks.getGrid()[i][s.getStartY()].getId().equals("0")) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[i][s.getStartY()].setId("" + ((index + 1) * 10 + counter));
                counter++;
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                if (i < gridSpielfeldLinks.getKachelAnzahl()) {
                    if (!gridSpielfeldLinks.getGrid()[s.getStartX()][i].getId().equals("0")) {
                        return false;
                    }
                } else {
                    return false;
                }

            }
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[s.getStartX()][i].setId("" + ((index + 1) * 10 + counter));
                counter++;
            }
        }
        return true;
    }

    /**
     * Überprüft ob das horizontale Schiff an dieser Stelle liegen kann ohne ein anderes zu berühren oder zu überschneiden,
     * auch nicht an den Ecken. 
     * 
     * @param s Schiff
     * @return true: Wenn legal Platz, fallse: Wenn illegaler Platz
     */
    private boolean ueberpruefePlatzHorizontal(Schiff s) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        try {
            //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
            if (y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl()) { //Überprüfe auf Rand
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //2.Links/Rechts neben dem Schiff
            if (x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Randüberprüfung
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //3. Randfälle
            if (y - 1 <= 0 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am Oberen Rand Zeichnen
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            if (x - 1 <= 0 && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am linken Rand zeichen
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Wenn rechts raus zeiche zusätlich noch links 
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                }
            }
            if (y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am unteren Rand 
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                }
                for (int i = y - 1; i < y - 1 + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //Linke obere Ecke
            if (x - 1 <= 0 && y - 1 <= 0) {
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //Rechte obere Ecke
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 <= 0) {
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //Rechte untere Ecke
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = y; i > y - 2; i--) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //Linke untere Ecke
            if (x - 1 <= 0 && y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = y; i > y - 2; i--) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
        } catch (Exception e) {
        }
        return status;
    }

    /**
     * Überprüft ob das vertikale Schiff an dieser Stelle liegen kann ohne ein anderes zu berühren oder zu überschneiden,
     * auch nicht an den Ecken. 
     * 
     * @param s Schiff
     * @return true: Wenn legal Platz, fallse: Wenn illegaler Platz
     */
    private boolean ueberpruefePlatzVertikal(Schiff s) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        try {
            //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
            if (y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0) { //Überprüfe auf Rand
                for (int i = x - 1; i < x + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Suche unter dem Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //2.Links/Rechts neben dem Schiff
            if (x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Randüberprüfung
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        status = false; //Markierung gefunden
                        break; //Breche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }

            //Sonderfälle:       
            //1. Links
            if (x - 1 <= 0) { //Wenn links raus
                if (y + s.getLaenge() >= gridSpielfeldLinks.getKachelAnzahl()) { //und Unten raus
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                if (y - 1 <= 0) { //Und wenn oben raus
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }

                if (y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0) {
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }
            }
            //2. Rechts raus
            if (x + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl()) {
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }

                }
                if (y - 1 <= 0) { //Und wenn oben raus
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }
                if (y + s.getLaenge() >= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Und wenn unten raus
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }
                if (y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0) { //Rechts raus ohne ohen unten 
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                            status = false; //Markierung gefunden 
                            break; //Brche suche ab
                        }
                    }
                }
            }
            //3. Oben Raus
            if (y - 1 <= 0 && x < gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0) {
                for (int i = x - 1; i < x + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            //Unten raus
            if (y >= gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() && x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = x - 1; i < x + 2; i++) { //Oben über dem Schiff
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
                //Neben dem Schiff
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
        } catch (Exception e) {
        }
        return status;
    }

    /**
     * Getroffes Array auf der Konsole ausgeben
     */
    public void printGetroffen() {
        System.out.println("");
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                System.out.print(getroffen[i][j] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
        
    /**
     * Getter und Setter Methoden 
     */
    
    public int getRichtungKi(){
        if (angefangenesSchiffRichtung == Richtung.HORIZONTAL) {
            return 0;
        }else if (angefangenesSchiffRichtung == Richtung.VERTIKAL){
            return 1;
        }
        return -1; //Fehler
    }
    
    public int getAngefangenesSchiff(){
        if (angefangesSchiff) {
            return 1;
        }else{
            return 0;
        }
    }

    public void setSpielfeldgroesse(int spielfeldgroesse) {
        this.spielfeldgroesse = spielfeldgroesse;
    }

    public void setAnzahlSchiffeTyp(int[] anzahlSchiffeTyp) {
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
    }

    public void setFertig(boolean fertig) {
        this.fertig = fertig;
    }

    public void setSchiffArray(Schiff[] schiffArray) {
        this.schiffArray = schiffArray;
    }

    public void setAnzSchiffe(int anzSchiffe) {
        this.anzSchiffe = anzSchiffe;
    }

    public void setLetzterSchuss(int[] letzterSchuss) {
        this.letzterSchuss = letzterSchuss;
    }

    public void setAngefangenesSchiffSchuss(int[] angefangenesSchiffSchuss) {
        this.angefangenesSchiffSchuss = angefangenesSchiffSchuss;
    }

    public void setAnzGetroffen(int anzGetroffen) {
        this.anzGetroffen = anzGetroffen;
    }

    public void setAngefangesSchiff(boolean angefangesSchiff) {
        this.angefangesSchiff = angefangesSchiff;
    }

    public void setAngefangenesSchiffRichtung(Richtung angefangenesSchiffRichtung) {
        this.angefangenesSchiffRichtung = angefangenesSchiffRichtung;
    }

    public void setVariable(int variable) {
        this.variable = variable;
    }

    public void setKiStufe(int kiStufe) {
        this.kiStufe = kiStufe;
    }
    
    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }

    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public void setGridSpielfeldRechts(Grid gridSpielfeldRechts) {
        this.gridSpielfeldRechts = gridSpielfeldRechts;
    }

    public void setGridSpielfeldLinks(Grid gridSpielfeldLinks) {
        this.gridSpielfeldLinks = gridSpielfeldLinks;
    }

    public void setGetroffen(int[][] getroffen) {
        this.getroffen = getroffen;
    }

    public int[] getAnzahlSchiffeTyp() {
        return anzahlSchiffeTyp;
    }

    public int getAnzSchiffe() {
        return anzSchiffe;
    }

    public int[][] getGetroffenKi() {
        return getroffen;
    }

    public int[] getLetzterSchuss() {
        return letzterSchuss;
    }

    public int[] getAngefangenesSchiffSchuss() {
        return angefangenesSchiffSchuss;
    }

    public boolean isAngefangesSchiff() {
        return angefangesSchiff;
    }

    public Richtung getAngefangenesSchiffRichtung() {
        return angefangenesSchiffRichtung;
    }

    public int getVariable() {
        return variable;
    }
    
    public void setGetroffenWasser(int x, int y) {
        this.getroffen[x][y] = 1;
    }
    
    public void setGetroffenSchiff(int x, int y) {
        this.getroffen[x][y] = 2;
    }
    
    public int getKiStufe(){
        return kiStufe;
    }

    public int getAnzGetroffen() {
        return anzGetroffen;
    }

    public void setAnzGetroffenHoeher() {
        this.anzGetroffen++;
    }

    public void setGetroffen(int zeile, int spalte, int wert) {
        getroffen[zeile][spalte] = wert;
    }
    
    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public boolean isFertig() {
        return fertig;
    }

}
