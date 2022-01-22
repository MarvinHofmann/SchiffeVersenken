package controll;

import GUI.SpielGUIController;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.StrokeType;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SchiffeSetzen {

    private SpielGUIController dieGui = null;

    //Spielfeld => 600x600
    private int spielfeldgroesse;
    private int[] anzahlSchiffeTyp;
    private int anzSchiffe = 0;
    private Schiff[] schiffArray;
    private Grid gridSpielfeldRechts;
    private Grid gridSpielfeldLinks;
    private boolean fertig = false;

    /**
     * Konstruktor
     *
     * @param gui SpielGUIController für Zugriff auf GUI Elemente
     * @param anzahlSchiffeTyp die Übergebenen Schifftypen von der Vorherigen
     * GUI
     */
    public SchiffeSetzen(GUI.SpielGUIController gui, int[] anzahlSchiffeTyp, int spielfeldgroesse) {
        System.out.println("SchiffeSetzen erzeugt"); //status
        this.dieGui = gui;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
        this.spielfeldgroesse = spielfeldgroesse;
        //Zähle die Schiff, welche Übergeben wurden
        for (int i = 0; i < this.anzahlSchiffeTyp.length; i++) {
            anzSchiffe += this.anzahlSchiffeTyp[i];
        }
    }

    //Getter & Setter
    
    public boolean isFertig() {
        for (Schiff schiff : schiffArray) {
            if (schiff.getStartX() == -1 || schiff.getStartY() == -1) {
                return false;
            }
        }
        return fertig;
    }
    
    public void setInfo(String s){
        dieGui.getInfoText().setText(s);
    }

    public void setFertig(boolean fertig) {
        this.fertig = fertig;
    }

    public SpielGUIController getDieGui() {
        return dieGui;
    }

    public void setDieGui(SpielGUIController dieGui) {
        this.dieGui = dieGui;
    }

    public int[] getSchiffTypen() {
        return anzahlSchiffeTyp;
    }

    public void setSchiffTypen(int[] schiffTypen) {
        this.anzahlSchiffeTyp = schiffTypen;
    }

    public Grid getGridSpielfeldRechts() {
        return gridSpielfeldRechts;
    }

    public Grid getGridSpielfeldLinks() {
        return gridSpielfeldLinks;
    }

    public int getAnzSchiffe() {
        return anzSchiffe;
    }

    public void setAnzSchiffe(int anzSchiffe) {
        this.anzSchiffe = anzSchiffe;
    }

    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public void setSchiffArray(Schiff[] schiffArray) {
        this.schiffArray = schiffArray;
    }

    public void setGridRechts(Grid gridSpielfeld) {
        this.gridSpielfeldRechts = gridSpielfeld;
    }

    public void setGridLinks(Grid gridSpielfeld) {
        this.gridSpielfeldLinks = gridSpielfeld;
    }

    /**
     * Zeichnet das Spielfeld in die GUI
     */
    public void drawAll() {
        //Erzeuge neues Grid Objekt mit zweitem Konstruktor 
        gridSpielfeldRechts = new Grid(spielfeldgroesse);
        gridSpielfeldLinks = new Grid(spielfeldgroesse);
        gridSpielfeldRechts.macheGridRechts(); //Erstelle 2Dim Array aus Rectangle
        gridSpielfeldLinks.macheGridLinks(); //Erstelle 2Dim Array aus Rectangle
        //Grid Zeichnen rectangle kacheln dem Pane hinzufügen
        for (int i = 0; i < gridSpielfeldLinks.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeldLinks.getGrid().length; j++) {
                dieGui.zeigeGridRechts(gridSpielfeldLinks.getGrid()[i][j]);
            }
        }
        for (int i = 0; i < gridSpielfeldRechts.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeldRechts.getGrid().length; j++) {
                dieGui.zeigeGridLinks(gridSpielfeldRechts.getGrid()[i][j]);
            }
        }

        macheSchiffe(); //Erstellt alle Schiffobjekte 
    }

    /**
     * Erstellt die Schiffe, welche im Modi Menü ausgewählt wurden schiffTypen[]
     * beinahltet die Schiffe der verschiedenen Größen welche im Modi Menü
     * ausgewählt worden
     */
    private void macheSchiffe() {
        //Erstelle alle Schiffe und Speichere sie in einem Array voller 
        //Schiffsobjekte ab
        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < anzahlSchiffeTyp[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße(), this, ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße(), this, ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße(), this, ctn - 1);
        }
        for (int i = 0; i < anzahlSchiffeTyp[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridSpielfeldLinks.getKachelgroeße(), gridSpielfeldLinks.getKachelgroeße(), this, ctn - 1);
        }
        zeichneSchiffe(false);
    }

    /**
     * Zeichnet die Schiffe auf die Rechte Seite der GUI zum ziehen wenn der
     * Übergabeparameter true ist, werden die Schiffe nur neu nach rechts
     * gezeichnet es wurde der reset button gedrückt Wenn false wurde nicht
     * resetet, die Schiffe werden zum ersten mal erzeugt also initialisiere
     * handler und füge dem Pane hinzu
     *
     * @param reset ist true, wenn schiffe mit clear Button resetet wurden d.h
     * sie brauchen kein Handler mehr
     */
    public void zeichneSchiffe(boolean reset) {
        //Alle Schiffe dem Pane als rectangle hinzufügen
        //Die Schiffe auf dem Grid zeichnen rechts Ablegen
        int hoehe = 0; //Merker um schiffe versetzt auszugeben
        int breitenWert = gridSpielfeldRechts.getPxGroesse() + gridSpielfeldRechts.getVerschiebung(); //Merker Horizontal
        int langeLastS; //Laengen in pixel
        int laengeAktS;
        for (int i = 0; i < schiffArray.length; i++) {

            if (!reset) {
                dieGui.zeigeSchiffeRechts(schiffArray[i]);
                makeHandler(schiffArray[i], i);
            }
            if (i == 0) { //Erste Schiff
                schiffArray[i].draw(breitenWert, 0); //Rechts neben der Linie auf höhe 0 
            } else {
                langeLastS = schiffArray[i - 1].getLaenge() * gridSpielfeldRechts.getKachelgroeße(); //laenge des vorherigen schiffs
                laengeAktS = schiffArray[i].getLaenge() * gridSpielfeldRechts.getKachelgroeße();
                breitenWert = breitenWert + langeLastS + gridSpielfeldRechts.getKachelgroeße();      //aktuelle Position + die länge des Letzten + eine Kachel abstand
                if (breitenWert + laengeAktS > gridSpielfeldRechts.getPxGroesse() * 2 + gridSpielfeldRechts.getVerschiebung()) {           //Wenn breite + naechste Schiff gr0ßer als das Spielfeld
                    hoehe = hoehe + 2 * gridSpielfeldRechts.getKachelgroeße(); //nehme uebernaechste Zeile
                    if (hoehe >= gridSpielfeldRechts.getPxGroesse()) {
                        schiffArray[i].draw(gridSpielfeldRechts.getPxGroesse() + gridSpielfeldRechts.getVerschiebung() , hoehe - gridSpielfeldRechts.getKachelgroeße());
                    } else {
                        breitenWert = gridSpielfeldRechts.getPxGroesse() + gridSpielfeldRechts.getVerschiebung();
                        schiffArray[i].draw(breitenWert, hoehe);
                    }
                } else {
                    schiffArray[i].draw(breitenWert, hoehe);
                }
            }
            //FÜR BILDER AB START
            String str = "/Images/boot" + schiffArray[i].getLaenge() + "FullH.png";
            Image img = new Image(str);
            schiffArray[i].setFill(new ImagePattern(img));
            schiffArray[i].setStrokeWidth(2);
            schiffArray[i].setStroke(Color.RED);
        }
    }

    /**
     * Aktiviert für jedes Schiff den Eventhandler Dragged und Release um mit
     * der Maus zu verschieben
     *
     * @param s jeweiliges Schiff für das der Handler erzeugt werden soll
     * @param index Nummer des Schiffs um Id anzupassen
     */
    private void makeHandler(Schiff s, int index) {
        s.setOnMouseDragged(event -> dragged(event, s));
        s.setOnMouseReleased(event -> released(event, s, index));
    }

    /**
     * Eventhandler für das Anheben Wenn ein schiff mit Gedrückter Maus
     * angehoben wird kann es im Grid verschoben werden Dabei wird darauf
     * geachte, dass die Schiffe sich nur in den Linien bewegen und das
     * Spielfeld nicht verlassen
     *
     * @param event Mouse Event drag
     * @param s Schiff
     */
    public void dragged(MouseEvent event, Schiff s) throws NullPointerException {
        setInfo("");//reset Info Text
        s.requestFocus();
        s.setCursor(Cursor.OPEN_HAND);
        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüber
        Bounds boundGrid = dieGui.getBoundsRec().getBoundsInParent();
        Bounds boundSchiff = s.getBoundsInLocal();

        dieGui.getBorderRec().setStroke(Color.RED);
        dieGui.getBorderRec().setStrokeWidth(10);
        dieGui.getBorderRec().setStrokeType(StrokeType.OUTSIDE);

        int snapX = (int) (event.getX() % gridSpielfeldLinks.getKachelgroeße());
        int snapY = (int) (event.getY() % gridSpielfeldRechts.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX() - snapX;
        int y = (int) event.getY() - snapY;
        if (s.getStartX() != -1) {
            clearId(s);
        }

        if (!s.isGesetzt()) {
            if (boundGrid.contains(boundSchiff)) {
                s.setGesetzt(true);
                s.setEffect(null);
            } else {
                s.draw((int) event.getX() - (int) s.getWidth() / 2, (int) event.getY() - (int) s.getHeight() / 2); //om der Mitte greifen wenn man das schiff rechts nimmt
            }
        }

        boolean blockiert = false;
        final double size = 600;
        final double heigth = 625;
        //Abfragen, ob das Schiff aus dem Feld fährt und dies Verhindern
        if (x < 0) { //Nach links raus
            blockiert = true;
            x = 0;
        } else if (y < 0) { //Nach oben raus
            blockiert = true;
            y = 0;
        } else if (x > (size - s.getWidth() + 0.5 * gridSpielfeldRechts.getKachelgroeße())) { //nach rechts raus
            blockiert = true;
            x = (int) (size - s.getWidth() + 0.5 * gridSpielfeldRechts.getKachelgroeße());
        } else if (y > (heigth - s.getHeight()) + 0.5 * gridSpielfeldRechts.getKachelgroeße()) { //nach unten raus
            blockiert = true;
            y = (int) ((heigth - s.getHeight()) + 0.5 * gridSpielfeldRechts.getKachelgroeße());
        } else { //wenn alles ok blokiert = false man kan das Schiff bewegen
            blockiert = false;
        }
        //Wenn nicht blokiert bewege das Schiff
        if (!blockiert) {
            s.setStroke(Color.GREEN);
            s.setX(x);
            s.setY(y);
            s.draw();
        }
    }

    /**
     * Handle den release Zustand Besonders: Snap so wird geschaut, dass immer
     * genau in den Grids gelandet wird und nicht neben/über den Linien
     *
     * @param event Mausevent loslassen
     * @param s Schiff
     * @param index index des Schiff
     */
    public void released(MouseEvent event, Schiff s, int index) {
        dieGui.getBorderRec().setStroke(Color.TRANSPARENT);
        //Ermittle den Puffer zur letzten Kachel
        s.setPlaziert(true);
        int puff = (int) (s.getX() / gridSpielfeldLinks.getKachelgroeße());
        int puffY = (int) (s.getY() / gridSpielfeldLinks.getKachelgroeße());
        s.setX(puff * gridSpielfeldLinks.getKachelgroeße());
        s.setY(puffY * gridSpielfeldLinks.getKachelgroeße());
        //Ermittle Koordinatenwert der StartPositionen für 2D Array
        int last = s.getStartX();
        int lasty = s.getStartY();
        int startX = (int) s.getX() / gridSpielfeldLinks.getKachelgroeße();
        int startY = (int) s.getY() / gridSpielfeldLinks.getKachelgroeße();

        Bounds boundGrid = dieGui.getBoundsRec().getBoundsInParent();
        Bounds boundSchiff = s.getBoundsInLocal();

        final double size = 600;
        final double heigth = 625;
        //Fall Schiff in der Mitte
        //1. Schiff Horizontal und rechts draußen
        if (s.getX() + s.getLaenge() * gridSpielfeldLinks.getKachelgroeße() > size && s.getRichtung() == Richtung.HORIZONTAL) {
            startX = (int) s.getX() / gridSpielfeldLinks.getKachelgroeße() - s.getLaenge();
            if (startX > gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge()) {
                int diff = (int) s.getX() - gridSpielfeldLinks.getPxGroesse();
                startX = startX - diff / gridSpielfeldLinks.getKachelgroeße();
            }
            //2. Rechts draußen und oben drüber
            if (s.getY() >= size) {
                startY = gridSpielfeldLinks.getKachelAnzahl() - 1;
                //3. Unten drunter
            } else if (s.getY() <= 0) {
                startY = 0;
            } else { //sonsnt
                startY = (int) s.getY() / gridSpielfeldLinks.getKachelgroeße();
            }
            //s.draw((int) startX * gridSpielfeldLinks.getKachelgroeße(), (int) s.getStartY() * gridSpielfeldLinks.getKachelgroeße());
        } else if (s.getX() > size && s.getRichtung() == Richtung.VERTIKAL) { //Wenn die schiffe Vertikal rechts sind
            startX = gridSpielfeldLinks.getKachelAnzahl() - 1;
            System.out.println(startX);
            //s.draw((int) startX * gridSpielfeldLinks.getKachelgroeße(), (int) s.getY());
        } else if (!boundGrid.contains(boundSchiff)) { // Fall, wenn der Benutzer komplett am Rad dreht
            //Falls komplett unkontroliert oben Links hinzeichnen
            startX = 0;
            startY = 0;
            s.draw(0, 0);
        }
        s.setStart(startX, startY);
        s.draw((int) startX * gridSpielfeldLinks.getKachelgroeße(), (int) s.getStartY() * gridSpielfeldLinks.getKachelgroeße());
        if (pruefeBelegt(s)) {//Wenn dort kein schiff ist setze markierung sonst überflüssig
            System.out.println("BELEGT");
            System.out.println(last + " | " + lasty);
            s.setStart(last, lasty);
            s.setX(last * gridSpielfeldLinks.getKachelgroeße());
            s.setY(lasty * gridSpielfeldLinks.getKachelgroeße());
        }
        setIdNeu(s, index); //Setze die MarkerId unter dem Schiff   
        pruefePisition();// checkt die um das Schiff ob ein Schiff kollidiert
        gridSpielfeldLinks.print();
        s.draw();
    }

    /**
     * Überprüft ob an der angeforderten stelle bereits ein schiff zurück gibt
     *
     * @param s schiff das gesetzt werden will
     * @return true für ist belegt false für ist frei
     */
    public boolean pruefeBelegt(Schiff s) {
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {

                System.out.println(!gridSpielfeldLinks.getGrid()[i][s.getStartY()].getId().equals("0"));
                if (!gridSpielfeldLinks.getGrid()[i][s.getStartY()].getId().equals("0")) {
                    System.out.println("return true");
                    System.out.println(gridSpielfeldLinks.getGrid()[i][s.getStartY()].getId());
                    return true;
                }
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                System.out.println(!gridSpielfeldLinks.getGrid()[s.getStartX()][i].getId().equals("0"));
                System.out.println(gridSpielfeldLinks.getGrid()[s.getStartX()][i].getId());
                if (!gridSpielfeldLinks.getGrid()[s.getStartX()][i].getId().equals("0")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Überprüft ob alle Schiffe richtig gesetzt sind und verhindert bei
     * falschen schiff den Spielstart Die Funktion liefert einen Fehlertext auf
     * der GUI und markiert falsch gesetzte Schiffe rot
     */
    public void pruefePisition() {
        //Überprüfe für alle Schiffe ob die aktuelle Position ok is, oder ob ein Schiff zu nahe
        //an einem anderen ist
        //fertig blockiert/ gibt den Button zum Spielstart frei
        boolean fehlend = false;
        boolean nichtFertig = false; //setzt globale fertig zurück für den fall das letzte schiff richtig plaziert
        for (Schiff s1 : schiffArray) {
            if (s1.getX() >= gridSpielfeldLinks.getPxGroesse()) {
                s1.setStroke(Color.RED);
                fehlend = true;
            } else {
                if (s1.getRichtung() == Richtung.HORIZONTAL) {
                    if (ueberpruefePlatz(s1, false)) {
                        s1.setStroke(Color.GREEN);
                    } else {
                        nichtFertig = true;
                        s1.setStroke(Color.RED);
                    }
                } else {
                    if (ueberpruefePlatzVertikal(s1, false)) {
                        s1.setStroke(Color.GREEN);
                    } else {
                        nichtFertig = true;
                        s1.setStroke(Color.RED);
                    }
                }
            }
        }
        if (nichtFertig) {
            fertig = false;
        } else {
            fertig = true;
        }
    }

    /**
     * Löscht die Ids auf dem Grid die das Schiff temporär markieren
     *
     * @param s schiff unter dem Markiert wird
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
     * setzt den Marker ID auf dem Grid unter dem plazierten Schiff
     *
     * @param s Schiff für das der Merker gesetzt wird
     * @param index Index des Schiffs
     */
    public void setIdNeu(Schiff s, int index) {
        int counter = 0;
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeldLinks.getGrid()[i][s.getStartY()].setId("" + ((index + 1) * 10 + counter));
                counter++;
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                try {
                    gridSpielfeldLinks.getGrid()[s.getStartX()][i].setId("" + ((index + 1) * 10 + counter));
                    counter++;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //gridSpielfeld.print();
    }

    /**
     * Die Funktion setzt mithilfe von Random schiffe auf das Grid. Verrent die
     * Funktion sich und kann die schiffe nicht mehr plazieren startet sie von
     * neu, dafür werden die Wiederholungen gezählt Nach 30 Versuchen eines
     * Schiffes werden alle Schiffe neu plaziert
     */
    public void setzeRandomSchiffe() {
        int spielfeldgroesse = gridSpielfeldLinks.getKachelAnzahl();
        int wiederholungen = 0;
        boolean allegesetzt = false;
        int anzahlgesetzt = 0;
        Random zufall = new Random();
        int zufallx;
        int zufally;
        int zufallsrichtung; // horizontal--- -> 0, vertikal||| -> 1

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
                    } while (!setIdNeuAuto(schiffArray[i], i)); // !gridSpielfeld.getGrid()[zufally][zufallx].getId().equals(1));

                    if (zufallsrichtung == 0) { // Horizontal ---
                        if (ueberpruefePlatz(schiffArray[i], true)) {
                            anzahlgesetzt++;
                        } else {
                            clearId(schiffArray[i]);
                        }
                    } else if (zufallsrichtung == 1) { // Vertikal |||
                        if (ueberpruefePlatzVertikal(schiffArray[i], true)) {
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
            } else {
                wiederholungen = 0;
                clearAll();
                anzahlgesetzt = 0;
            }
        }
        fertig = true;
    }

    /**
     * Löscht alle Ids auf dem Spielfeld
     */
    public void clearAll() {
        fertig = false;
        for (Schiff s : schiffArray) {
            s.setStart(-1, -1);
        }
        for (int i = 0; i < gridSpielfeldLinks.getKachelAnzahl(); i++) {
            for (int j = 0; j < gridSpielfeldLinks.getKachelAnzahl(); j++) {
                gridSpielfeldLinks.getGrid()[i][j].setId("0");
            }
        }
    }

    /**
     * Regelt die Id Vergabe bei Automatischem setzten lassen
     *
     * @param s jeweilige Schiff
     * @param index der Index aus dem Array aller Schiffe
     * @return false, wenn an der Stelle an die Schiff möchte schon belegt ist,
     * true wenn frei
     */
    public boolean setIdNeuAuto(Schiff s, int index) {
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
     * Die Funktion überpüft für ein Horizontales Schiff die Position hierbei
     * wird geschaut ob ein Marker auf dem Grid in nicht erlaubter nähe zum
     * Schiff ist. Falls ein Marker gefunden wurde wird der status false gesetzt
     * Hierbei werden alle Rand- und Eckenfälle überprüft
     *
     * @param s schiff, welches Überprüft wird
     * @param auto true wenn schiffe random durch computer plaziert werden,
     * false wenn spieler selber plaziert
     * @return status: boolean true wenn Schiff ordnungsgemäß plaziert, false
     * wenn falsch plaziert
     */
    private boolean ueberpruefePlatz(Schiff s, boolean auto) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        try {

            if (gridSpielfeldLinks.getKachelAnzahl() <= 6 && !auto) {
                return !checkKollisionMini(s);
            }
            //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
            if (y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl()) { //Überprüfe auf Rand
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
            //2.Links/Rechts neben dem Schiff
            if (x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Randüberprüfung
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }
            //3. Randfälle
            if (y - 1 <= 0 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am Oberen Rand Zeichnen
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }
            if (x - 1 <= 0 && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am linken Rand zeichen
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 >= 0 && y + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Wenn rechts raus zeiche zusätlich noch links 
                for (int i = y - 1; i < y - 1 + 3; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                }
            }
            if (y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Am unteren Rand 
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        return false;
                    }
                }
                for (int i = y - 1; i < y - 1 + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }
            //Linke obere Ecke
            if (x - 1 <= 0 && y - 1 <= 0) {
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
            //Rechte obere Ecke
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 <= 0) {
                for (int i = y; i < y + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
            //Rechte untere Ecke
            if (x + 1 == gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() + 1 && y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = y; i > y - 2; i--) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
            //Linke untere Ecke
            if (x - 1 <= 0 && y + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = y; i > y - 2; i--) {
                    if (!gridSpielfeldLinks.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
                for (int i = x; i < x + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
        } catch (Exception e) {
        }
        return status;
    }

    /**
     * Die Funktion überpüft für ein Vertikales Schiff die Position nach dem
     * Loslassen hierbei wird geschaut ob ein Marker auf dem Grid in nicht
     * erlaubter nähe zum Schiff ist.
     *
     * @param s schiff, welches Überprüft wird
     * @return status: boolean true wenn Schiff ordnungsgemäß plaziert, false
     * wenn falsch plaziert
     */
    private boolean ueberpruefePlatzVertikal(Schiff s, boolean auto) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        try {
            if (gridSpielfeldLinks.getKachelAnzahl() <= 6 && !auto) {
                return !checkKollisionMini(s);
            }
            //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
            if (y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0) { //Überprüfe auf Rand
                for (int i = x - 1; i < x + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Suche unter dem Schiff
                        return false;
                    }
                }
            }
            //2.Links/Rechts neben dem Schiff
            if (x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Randüberprüfung
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }

            //Sonderfälle:       
            //1. Links
            if (x - 1 <= 0) { //Wenn links raus
                if (y + s.getLaenge() >= gridSpielfeldLinks.getKachelAnzahl()) { //und Unten raus
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                            return false;
                        }
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
                if (y - 1 <= 0) { //Und wenn oben raus
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            return false;
                        }
                    }
                }

                if (y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0) {
                    for (int i = x; i < x + 2; i++) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                            return false;
                        }
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            return false;
                        }
                    }
                }
            }
            //2. Rechts raus
            if (x + 1 >= gridSpielfeldLinks.getKachelAnzahl() - 1 && y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl()) {
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }

                }
                if (y - 1 <= 0) { //Und wenn oben raus
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                            return false;
                        }
                    }
                }
                if (y + s.getLaenge() >= gridSpielfeldLinks.getKachelAnzahl() - 1) { //Und wenn unten raus
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                            return false;
                        }
                    }
                }
                if (y + s.getLaenge() <= gridSpielfeldLinks.getKachelAnzahl() - 1 && y - 1 >= 0) { //Rechts raus ohne ohen unten 
                    for (int i = x; i > x - 2; i--) {
                        if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                            return false;
                        }
                        if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                            return false;
                        }
                    }
                }
            }
            //3. Oben Raus
            if (y - 1 <= 0 && x < gridSpielfeldLinks.getKachelAnzahl() - 1 && x - 1 >= 0) {
                for (int i = x - 1; i < x + 2; i++) {
                    if (!gridSpielfeldLinks.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                        return false;
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                        return false;
                    }
                }
            }
            //Unten raus
            if (y >= gridSpielfeldLinks.getKachelAnzahl() - s.getLaenge() && x - 1 >= 0 && x + 1 <= gridSpielfeldLinks.getKachelAnzahl() - 1) {
                for (int i = x - 1; i < x + 2; i++) { //Oben über dem Schiff
                    if (!gridSpielfeldLinks.getGrid()[i][y - 1].getId().equals("0")) {
                        return false;
                    }
                }
                //Neben dem Schiff
                for (int i = y; i < y + s.getLaenge(); i++) {
                    if (!gridSpielfeldLinks.getGrid()[x - 1][i].getId().equals("0")) {
                        return false;
                    }
                    if (!gridSpielfeldLinks.getGrid()[x + 1][i].getId().equals("0")) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
        }
        return status;
    }

    /**
     * Überprüft die Ränder und schaut ob Schiffe kolidieren bei kleinen
     * Spielfeldern
     *
     * @param pSchiff schiff für das geschaut wird ob es kollidiert
     * @return true wenn schiff kollidiert false wenn nicht
     */
    private boolean checkKollisionMini(Schiff pSchiff) {
        boolean collisionDetected = false;
        for (Schiff s : schiffArray) {
            if (s != pSchiff) {
                if (pSchiff.getBoundsInParent().intersects(s.getBoundsInParent())) {
                    collisionDetected = true;
                }
            }
        }
        System.out.println(collisionDetected);
        return collisionDetected;
    }
}
