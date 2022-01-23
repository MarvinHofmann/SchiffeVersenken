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
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class LokalesSpielSteuerung extends SpielSteuerung {

    private SchiffeSetzen dieSteuerungSchiffeSetzen = null;
    private KI kiGegner;

    public LokalesSpielSteuerung(SpielGUIController gui, int spielfeldgroesse, int[] anzahlSchiffeTyp, int kiStufe) {
        super(gui);
        System.out.println("LokalesSpielSteuerung erzeugt");
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        this.dieSteuerungSchiffeSetzen = new SchiffeSetzen(gui, anzahlSchiffeTyp, spielfeldgroesse);
        this.kiGegner = new KI(spielfeldgroesse, anzahlSchiffeTyp, kiStufe);
        getroffen = new int[spielfeldgroesse][spielfeldgroesse];
        getroffenGegner = new int[spielfeldgroesse][spielfeldgroesse];
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
    }

    public LokalesSpielSteuerung(SpielGUIController gui, int[] styp, int[] paramInc, int[][] gridRechtsArr, int[][] gridLinksArr, int[][] getroffenGegAr, int[][] getroffenAr,int[][] getroffenKI, int[]letzterSchussKI, int[] angefSchiffKI, int[] kiValues) {
        // ParamInc: 0 -> spielfeldgroesse(), 1-> Modus(), 2 -> KiStufe(), 3-> AnzGetroffen(), 4-> EigeneSchiffeGetroffen()};
        super(gui);
        System.out.println("LokalesSpielSteuerung erzeugt bei Spiel laden");
        this.anzahlSchiffeTyp = styp;
        this.spielfeldgroesse = paramInc[0];
        this.anzGetroffen = paramInc[3];
        this.eigeneSchiffeGetroffen = paramInc[4];
        for (int i = 0; i < anzahlSchiffeTyp.length; i++) {
            this.anzSchiffe += anzahlSchiffeTyp[i];
        }
        //*************KI*********************** 
        //getroffenKi[][] zwei dim array Treffer
        //int[] letzter schuss 2 großes array aus KI letzterSchuss
        //int[] angefSchiff 2 großes Array aus KI angefangenesSchiffSchuss
        //KI values array: 0. anzGetroffen int, 1. Richtung 0-> Horizontal, 1 ->Vertikal, 2. Angefangenes Schiff 1-> true 0-> false, 3. Stufe
        this.kiGegner = new KI(spielfeldgroesse, anzahlSchiffeTyp, paramInc[2]);
        kiGegner.setKiStufe(kiValues[3]);
        kiGegner.setLetzterSchuss(letzterSchussKI);
        kiGegner.setAngefangenesSchiffSchuss(angefSchiffKI);
        kiGegner.setGetroffen(getroffenKI);
        kiGegner.setAnzGetroffen(kiValues[0]);
        if (kiValues[2] == 0) kiGegner.setAngefangesSchiff(false);
        else kiGegner.setAngefangesSchiff(true);
        
        if (kiValues[1] == 0) kiGegner.setAngefangenesSchiffRichtung(Richtung.HORIZONTAL);
        else kiGegner.setAngefangenesSchiffRichtung(Richtung.VERTIKAL);
        
        this.getroffen = getroffenAr;
        this.getroffenGegner = getroffenGegAr;
        kiGegner.setGridSpielfeldLinks(makeGrid(gridRechtsArr, 0));
        gridSpielfeldRechts = makeGrid(gridRechtsArr, 1);
        gridSpielfeldLinks = makeGrid(gridLinksArr, 0);
        gridSpielfeldRechts.Draw(getroffenAr);
        gridSpielfeldLinks.Draw(getroffenGegAr);
        macheEigeneSchiffe();
        gridSpielfeldLinks.DrawGetroffen(getroffenGegAr);
        setGridSpielfeldSpielLinks(gridSpielfeldLinks);
        setGridSpielfeldSpielRechts(gridSpielfeldRechts);
        macheKIGegnerSchiffe();
        gridSpielfeldRechts.enableMouseClick();
    }
    /**
     * Erzeugt und zeichnet die Schiffe der KI beim laden des Spiels
     */
    public void macheKIGegnerSchiffe() {
        kiGegner.setSchiffArray(new Schiff[anzSchiffe]);
        Schiff[] s = kiGegner.getSchiffArray();
        int ctn = 0;
        for (int i = 0; i < anzahlSchiffeTyp[0]; i++) {
            s[ctn++] = new Schiff(2 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[1]; i++) {
            s[ctn++] = new Schiff(3 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[2]; i++) {
            s[ctn++] = new Schiff(4 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[3]; i++) {
            s[ctn++] = new Schiff(5 * gridSpielfeldRechts.getKachelgroeße(), gridSpielfeldRechts.getKachelgroeße(), ctn - 1);
        }
        int zaehler = 1;
        for (int i = 0; i < kiGegner.getGridSpielfeldLinks().getKachelAnzahl(); i++) {
            for (int j = 0; j < kiGegner.getGridSpielfeldLinks().getKachelAnzahl(); j++) {
                if (kiGegner.getGridSpielfeldLinks().getGrid()[i][j].getId().endsWith("0") && gridSpielfeldRechts.getGrid()[i][j].getId().length() > 1) {
                    int id = (Integer.valueOf(kiGegner.getGridSpielfeldLinks().getGrid()[i][j].getId()) / 10) - 1;
                    s[id].setStart(i, j);
                    if (j + 1 < kiGegner.getGridSpielfeldLinks().getKachelAnzahl()) {
                        if (!kiGegner.getGridSpielfeldLinks().getGrid()[i][j + 1].getId().equals("0")) {
                            double speicher = s[id].getWidth();
                            s[id].setWidth(s[id].getHeight());
                            s[id].setHeight(speicher);
                            s[id].setRichtung(Richtung.VERTIKAL);
                        }
                    }
                    zaehler++;
                }
            }
        }
        kiGegner.setSchiffArray(s);
        for (int i = 0; i < getroffen.length; i++) {
            for (int j = 0; j < getroffen.length; j++) {
                if (getroffen[i][j] == 2) { //1 für wasser 0 nicht def 2 getroffen
                    int id = (Integer.valueOf(gridSpielfeldRechts.getGrid()[j][i].getId()) / 10) - 1;
                    int index = (Integer.valueOf(gridSpielfeldRechts.getGrid()[j][i].getId()) % 10);
                    s[id].setzteTrefferArray(index);
                }
            }
        }
    }
    
    @Override
    public void erzeugeEigeneSchiffe() {
        dieSteuerungSchiffeSetzen.drawAll();
    }

    public void erzeugeGegnerSchiffe() {
        kiGegner.erzeugeEigeneSchiffe();
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

    public KI getKiGegner() {
        return kiGegner;
    }

    /**
     * Setze alle Schiffe Zurück
     */
    public void clearSchiffeSetzen() {
        Schiff schiff;
        for (int i = 0; i < dieSteuerungSchiffeSetzen.getSchiffArray().length; i++) {
            schiff = dieSteuerungSchiffeSetzen.getSchiffArray()[i];
            schiff.setStroke(Color.RED);
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
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
        for (Schiff schiff : dieSteuerungSchiffeSetzen.getSchiffArray()) {
            if (schiff.getRichtung() == Richtung.VERTIKAL) {
                schiff.dreheGui();
            }
            schiff.setStroke(Color.GREEN);
            schiff.draw(schiff.getStartX() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße(), schiff.getStartY() * dieSteuerungSchiffeSetzen.getGridSpielfeldLinks().getKachelgroeße());
            schiff.setGesetzt(true);
        }
    }

    public void makeHandler(Rectangle r) {
        r.setOnMouseClicked(event -> clicked(event, r));
    }

    @Override
    public void beginneSpiel() {
        for (int i = 0; i < spielfeldgroesse; i++) {
            for (int j = 0; j < spielfeldgroesse; j++) {
                makeHandler(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }
        System.out.println("Beginne LokalesSpiel- Spieler startet");
        dieGui.setRestFuenfer("" + anzahlSchiffeTyp[3]);
        dieGui.setRestVierer("" + anzahlSchiffeTyp[2]);
        dieGui.setRestDreier("" + anzahlSchiffeTyp[1]);
        dieGui.setRestZweier("" + anzahlSchiffeTyp[0]);
        dieGui.zeigeStatusLabel(1, true);
    }

    private void clicked(MouseEvent event, Rectangle rectangle) {
        //System.out.println("Clicked");
        int zeile = (int) event.getY() / gridSpielfeldRechts.getKachelgroeße();
        int spalte = (int) (event.getX() - gridSpielfeldRechts.getPxGroesse() - gridSpielfeldRechts.getVerschiebung()) / gridSpielfeldRechts.getKachelgroeße();
        int[] gegnerSchuss = {-1, -1};
        int antwort = 0;
        int ende = 0;
        //System.out.println("Zeile: " + zeile + " Spalte: " + spalte);
        //System.out.println("Array: " + getroffen[zeile][spalte]);

        if (aktiverSpieler == 0 && getroffen[zeile][spalte] == 0 && ende == 0) { // Manchmal ungesetzete felder obwohl geclickt
            antwort = kiGegner.antwort(zeile, spalte);
            //System.out.println("Antwort= " + antwort);
            if (antwort == 0) { // 0 is wasser, 1 schiffteil, 2 ist schiff versenkt
                rectangle.setFill(Color.TRANSPARENT);
                getroffen[zeile][spalte] = 1;
            } else if (antwort == 1 || antwort == 2) {
                Image img = new Image("/Images/nop.png");
                rectangle.setFill(new ImagePattern(img));
                if (antwort == 2) {
                    anzGetroffen++;
                    wasserUmSchiffRechts(zeile, spalte);
                }
                getroffen[zeile][spalte] = 2;
            }
            ende = ueberpruefeSpielEnde();
            // weiterschießen da getroffen
            if (antwort != 0) {
                aktiverSpieler = 0;
                dieGui.zeigeStatusLabel(1, true);
                dieGui.zeigeStatusLabel(2, false);
            } else {
                aktiverSpieler = 1;
                dieGui.zeigeStatusLabel(1, false);
                dieGui.zeigeStatusLabel(2, true);
            }
            //System.out.println("Schiff: Zeile: " + zeile + " Spalte: " + spalte);
            //System.out.println("Spalte-Zeile " + gridSpielfeldRechts.getGrid()[spalte][zeile].getFill());
        }
        if (aktiverSpieler == 1 && ende == 0) {
            //gegnerSchuss = kiGegner.schiesseStufeDrei(0);
            //gegnerSchuss = kiGegner.schiesseStufeEins();
            gegnerSchuss = kiGegner.schiesse(0);
            //gegnerSchuss = kiGegner.schiesseReihe();
            antwort = antwortLokal(gegnerSchuss[0], gegnerSchuss[1]);
            if (antwort == 0) {
                getroffenGegner[gegnerSchuss[0]][gegnerSchuss[1]] = 1;
                gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.TRANSPARENT);
            } else if (antwort == 1 || antwort == 2) {
                Image img = new Image("/Images/nop.png");
                gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(new ImagePattern(img));
                if (antwort == 2) {
                    kiGegner.setAnzGetroffenHoeher();
                    wasserUmSchiffLinksKI(gegnerSchuss[0], gegnerSchuss[1], kiGegner);
                }
                kiGegner.setGetroffenSchiff(gegnerSchuss[0], gegnerSchuss[1]);
                getroffenGegner[gegnerSchuss[0]][gegnerSchuss[1]] = 2;
            }
            ende = ueberpruefeSpielEnde();

            // Weiter schießen da gteroffen
            while (antwort != 0 && ende == 0) {
                //gegnerSchuss = kiGegner.schiesseStufeDrei(antwort);
                //gegnerSchuss = kiGegner.schiesseStufeEins();
                gegnerSchuss = kiGegner.schiesse(antwort);
                //gegnerSchuss = kiGegner.schiesseReihe();
                antwort = antwortLokal(gegnerSchuss[0], gegnerSchuss[1]);
                if (antwort == 0) {
                    getroffenGegner[gegnerSchuss[0]][gegnerSchuss[1]] = 1;
                    gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(Color.TRANSPARENT);
                } else if (antwort == 1 || antwort == 2) {
                    Image img = new Image("/Images/nop.png");
                    gridSpielfeldLinks.getGrid()[gegnerSchuss[1]][gegnerSchuss[0]].setFill(new ImagePattern(img));
                    if (antwort == 2) {
                        kiGegner.setAnzGetroffenHoeher();
                        wasserUmSchiffLinksKI(gegnerSchuss[0], gegnerSchuss[1], kiGegner);
                    }
                    kiGegner.setGetroffenSchiff(gegnerSchuss[0], gegnerSchuss[1]);
                    getroffenGegner[gegnerSchuss[0]][gegnerSchuss[1]] = 2;
                }
                ende = ueberpruefeSpielEnde();
            }
            aktiverSpieler = 0;
            dieGui.zeigeStatusLabel(1, true);
            dieGui.zeigeStatusLabel(2, false);
        }
        ende = ueberpruefeSpielEnde();
        if (ende != 0) {
            dieGui.spielEnde(ende);
        }
    }

    @Override
    public int ueberpruefeSpielEnde() {
        //System.out.println("Ki Anzahl getroffen: " + kiGegner.getAnzGetroffen());
        if (anzSchiffe == kiGegner.getAnzGetroffen()) {
            //System.out.println("Gegner gewonnen");
            return 1;
        } else if (anzSchiffe == anzGetroffen) {
            //System.out.println("Spieler gewonnen");
            return 2;
        }
        return 0;
    }
}
