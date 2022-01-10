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
    private int[][] getroffen; // 0 noch nicht bekannt, 1 ist Wasser, 2 ist Schiff
    private int anzGetroffen;
    private int[] letzterSchuss = new int[2];
    private int[] angefangenesSchiffSchuss = new int[2];
    private boolean angefangesSchiff = false;
    private Richtung angefangenesSchiffRichtung;

    private int variable = 0;
    private int kiStufe;

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

    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }

    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public void setGetroffenWasser(int x, int y) {
        this.getroffen[x][y] = 1;
    }
    
    public void setGetroffenSchiff(int x, int y) {
        this.getroffen[x][y] = 2;
    }
    
    public void printGetroffen() {
        System.out.println("");
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                System.out.print(getroffen[i][j] + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public int getAnzGetroffen() {
        return anzGetroffen;
    }

    public void setAnzGetroffenHoeher() {
        this.anzGetroffen++;
    }

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
                    //System.out.println("Break");
                    break;
                } else if (wiederholungen > 60 && schiffArray[i + 1].getLaenge() == 2) {
                    //System.out.println("Break");
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
                    } while (!setIdNeu(schiffArray[i], i)); // !gridSpielfeld.getGrid()[zufally][zufallx].getId().equals(1));

                    //schiffArray[i].print();
                    //System.out.println("Schiff Nr " + i + " x: " + zufallx + " y: " + zufally + " richtung: " + zufallsrichtung + " leange: " + schiffArray[i].getLaenge());
                    if (zufallsrichtung == 0) { // Horizontal ---
                        if (ueberpruefePlatzHorizontal(schiffArray[i])) {
                            anzahlgesetzt++;
                            //System.out.println("Gesetzt");
                            //gridSpielfeld.print(); // DEBUG
                        } else {
                            //System.out.println("Nicht gesetzt");
                            clearId(schiffArray[i]);
                        }
                    } else if (zufallsrichtung == 1) { // Vertikal |||
                        if (ueberpruefePlatzVertikal(schiffArray[i])) {
                            anzahlgesetzt++;
                            //System.out.println("Gesetzt");
                            //gridSpielfeld.print(); // DEBUG
                        } else {
                            //System.out.println("Nicht gesetzt");
                            clearId(schiffArray[i]);
                        }
                    }
                    wiederholungen++;
                    if (wiederholungen > 30 && schiffArray[i].getLaenge() != 2) {
                        //System.out.println("Break");
                        break;
                    } else if (wiederholungen > 60 && schiffArray[i].getLaenge() == 2) {
                        //System.out.println("Break");
                        break;
                    }
                }
                //System.out.println("Anzahl gebraucht " + wiederholungen);
            }
            if (anzahlgesetzt == schiffArray.length) {
                allegesetzt = true;
                //System.out.println("Alle gesetzet");
                //gridSpielfeld.print(); // DEBUG
                fertig = true;
            } else {
                //System.out.println("Zurücksetzen");
                wiederholungen = 0;
                clearAll();
                anzahlgesetzt = 0;
                //gridSpielfeld.print(); // DEBUG
            }
        }
    }

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

    public int[] schiesseReihe() {
        //System.out.println("Schiesse");
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

    public int[] schiesseStufeZwei(int antwortDavor){
        Random zufallx = new Random();
        Random zufally = new Random();
        int[] schuss = new int[2]; // [Zeile row, Spalte col]
        int stelleX;
        int stelleY;
        /*System.out.println("DEBUG STATS");
        System.out.println("AntwortDavor: " + antwortDavor);
        System.out.println("Angefangenes Schiff: " + angefangesSchiff);
        System.out.println("Angefangenes Schiff Schuss: " + angefangenesSchiffSchuss[0] + " " + angefangenesSchiffSchuss[1]);
        System.out.println("Angefangenes Schiff Richtung: " + angefangenesSchiffRichtung);*/

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
            //System.out.println("Normal schießen");
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
            //System.out.println("Angefangenes Schiff gefunden");
            if(angefangenesSchiffRichtung == null){
                //System.out.println("Noch keine Richtung gefunden");
                if (angefangenesSchiffSchuss[1] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Rechts
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                        //System.out.println("Richtung rechts setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] = 1;
                        //System.out.println("Richtung rechts testen");
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[1] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Links
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                        //System.out.println("Richtung links setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] = 1;
                        //System.out.println("Richtung links testen");
                        return schuss;
                    }
                }
                if(angefangenesSchiffSchuss[0] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Unten
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                        //System.out.println("Richtung unten setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] = 1;
                        //System.out.println("Richtung unten testen");
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[0] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Oben
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                        //System.out.println("Richtung oben setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] = 1;
                        //System.out.println("Richtung oben testen");
                        return schuss;
                    }
                }
            }
            if(angefangenesSchiffRichtung == Richtung.HORIZONTAL){
                //System.out.println("Richtung HORIZONTAL");
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
                        //System.out.println("Schiesse weiter rechts");
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
                        //System.out.println("Schiesse weiter links");
                        return schuss;
                    }
                }
            }
            else if(angefangenesSchiffRichtung == Richtung.VERTIKAL){
                //System.out.println("Richtung VERTIKAL");
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
                        //System.out.println("Schiesse weiter unten");
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
                        //System.out.println("Schiesse weiter oben");
                        return schuss;
                    }
                }
            }
               
        }
        System.out.println("Null");
        return null;
    }
    
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
            /*   
            for(int z = 4; z > 1; z--){
                int x = z;
                for (int i = 0; i<getroffen.length; i++) {
                    for (int j = x; j < getroffen.length; j+=5) {
                        if(getroffen[i][j] == 0){
                            schuss[0] = i;
                            schuss[1] = j;
                            getroffen[i][j] = 1;
                            letzterSchuss = schuss;
                            return schuss;
                        }
                    }
                    x--;
                    if (x == -1) {
                        x = z;
                    }
                }
            }*/
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
            //System.out.println("Angefangenes Schiff gefunden");
            if(angefangenesSchiffRichtung == null){
                //System.out.println("Noch keine Richtung gefunden");
                if (angefangenesSchiffSchuss[1] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Rechts
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                        //System.out.println("Richtung rechts setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] + 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] +1] = 1;
                        //System.out.println("Richtung rechts testen");
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[1] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Links
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 2){
                        angefangenesSchiffRichtung = Richtung.HORIZONTAL;
                        //System.out.println("Richtung links setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0];
                        schuss[1] = angefangenesSchiffSchuss[1] - 1;
                        getroffen[angefangenesSchiffSchuss[0]][angefangenesSchiffSchuss[1] - 1] = 1;
                        //System.out.println("Richtung links testen");
                        return schuss;
                    }
                }
                if(angefangenesSchiffSchuss[0] + 1 < spielfeldgroesse && angefangenesSchiffRichtung == null) {
                    // Unten
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                        //System.out.println("Richtung unten setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] + 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] + 1][angefangenesSchiffSchuss[1]] = 1;
                        //System.out.println("Richtung unten testen");
                        return schuss;
                    }
                }
                if (angefangenesSchiffSchuss[0] - 1 >= 0 && angefangenesSchiffRichtung == null) {
                    // Oben
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 2){
                        angefangenesSchiffRichtung = Richtung.VERTIKAL;
                        //System.out.println("Richtung oben setzen");
                    }
                    if(getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] == 0){
                        schuss[0] = angefangenesSchiffSchuss[0] - 1;
                        schuss[1] = angefangenesSchiffSchuss[1];
                        getroffen[angefangenesSchiffSchuss[0] - 1][angefangenesSchiffSchuss[1]] = 1;
                        //System.out.println("Richtung oben testen");
                        return schuss;
                    }
                }
            }
            if(angefangenesSchiffRichtung == Richtung.HORIZONTAL){
                //System.out.println("Richtung HORIZONTAL");
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
                        //System.out.println("Schiesse weiter rechts");
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
                        //System.out.println("Schiesse weiter links");
                        return schuss;
                    }
                }
            }
            else if(angefangenesSchiffRichtung == Richtung.VERTIKAL){
                //System.out.println("Richtung VERTIKAL");
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
                        //System.out.println("Schiesse weiter unten");
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
                        //System.out.println("Schiesse weiter oben");
                        return schuss;
                    }
                }
            }
        }
        
        System.out.println("Null");
        return null;
    }

    public int antwort(int zeile, int spalte) {
        //System.out.println("Schuss Spieler auf : Zeile " + zeile + " Spalte: " + spalte + " ID: " + gridSpielfeld.getGrid()[spalte][zeile].getId());
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

    public boolean setzeSchiffsteilGetroffen(int zeile, int spalte) {
        String schiffbezeichnung;
        int schiffnr = 0;
        int schiffindex = 0;
        schiffbezeichnung = gridSpielfeldLinks.getGrid()[spalte][zeile].getId();
        boolean versenkt;
        if (schiffbezeichnung.length() == 2) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(1));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        } else if (schiffbezeichnung.length() == 3) {
            schiffnr = Character.getNumericValue(schiffbezeichnung.charAt(0)) * 10 + Character.getNumericValue(schiffbezeichnung.charAt(1)) - 1;
            schiffindex = Character.getNumericValue(schiffbezeichnung.charAt(2));
            //System.out.println("Schiffnr: " + schiffnr + " Index: " + schiffindex);
        }
        versenkt = schiffArray[schiffnr].handleTreffer(schiffindex);
        return versenkt;
    }

    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public boolean isFertig() {
        return fertig;
    }

    public void clearId(Schiff s) {
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[i][s.getStartY()].setId("0");
                //System.out.println("Setze 0: " + i + " / " + s.getStartY());
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[s.getStartX()][i].setId("0");
                //System.out.println("Setze 0: " + s.getStartX() + " / " + i);
            }
        }
    }

    public void clearAll() {
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                gridSpielfeldLinks.getGrid()[i][j].setId("0");
            }
        }
    }

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
                //System.out.println("Setze 1: " + i + " / " + s.getStartY());
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
                //System.out.println("Setze 1: " + s.getStartX() + " / " + i);
            }
        }
        return true;
        //gridSpielfeld.print(); // DEBUG
    }

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
            System.out.println(e);
        }
        return status;
    }

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
            System.out.println(e);
        }
        return status;
    }

}
