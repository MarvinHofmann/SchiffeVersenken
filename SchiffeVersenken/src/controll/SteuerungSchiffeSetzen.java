package controll;

import GUI.SpielGUIController;
import java.util.Random;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import shapes.Grid;
import shapes.Richtung;
import shapes.Schiff;

/**
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SteuerungSchiffeSetzen {

    private SpielGUIController dieGui = null;

    //Spielfeld => 600x600
    private int[] schiffTypen;
    private int anzSchiffe = 0;
    private Schiff[] schiffArray;
    private Grid gridSpielfeld;
    private boolean fertig = true;

    /**
     * Konstruktor
     *
     * @param gui SpielGUIController für Zugriff auf GUI Elemente
     * @param anzahlSchiffeTyp die Übergebenen Schifftypen von der Vorherigen
     * GUI
     */
    public SteuerungSchiffeSetzen(GUI.SpielGUIController gui, int[] anzahlSchiffeTyp) {
        System.out.println("SteuerungSchiffeSetzen erzeugt"); //status
        this.dieGui = gui;
        this.schiffTypen = anzahlSchiffeTyp;
        //Zähle die Schiff, welche Übergeben wurden
        for (int i = 0; i < schiffTypen.length; i++) {
            anzSchiffe += schiffTypen[i];
        }
    }

    //Getter & Setter
    public boolean isFertig() {
        return fertig;
    }

    public SpielGUIController getDieGui() {
        return dieGui;
    }

    public void setDieGui(SpielGUIController dieGui) {
        this.dieGui = dieGui;
    }

    public int[] getSchiffTypen() {
        return schiffTypen;
    }

    public void setSchiffTypen(int[] schiffTypen) {
        this.schiffTypen = schiffTypen;
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

    public Grid getGridS() {
        return gridSpielfeld;
    }

    public void setGridS(Grid gridSpielfeld) {
        this.gridSpielfeld = gridSpielfeld;
    }

    /**
     * Zeichnet das Spielfeld in die GUI
     *
     * @param gr ist die größé des Spielfelds
     */
    public void drawAll(int gr) {
        //Erzeuge neues Grid Objekt mit zweitem Konstruktor 
        //System.out.println("Server " + dieGui.getServer());
        gridSpielfeld = new Grid(gr, dieGui.getServer());
        gridSpielfeld.macheGrid(); //Erstelle 2Dim Array aus Rectangle
        //Grid Zeichnen rectangle kacheln dem Pane hinzufügen
        for (int i = 0; i < gridSpielfeld.getGrid().length; i++) {
            for (int j = 0; j < gridSpielfeld.getGrid().length / 2; j++) {
                dieGui.zeigeGrid(gridSpielfeld.getGrid()[i][j]);
            }
        }
        Line line = new Line(0, gridSpielfeld.getPxGroesse() + 2, 1200.0, gridSpielfeld.getPxGroesse() + 2);
        line.setStrokeWidth(5);
        line.setStroke(Color.DARKGRAY);
        dieGui.zeigeLinie(line);

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
        for (int i = 0; i < schiffTypen[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße(), this, ctn-1);
        }
        for (int i = 0; i < schiffTypen[1]; i++) {
            schiffArray[ctn++] = new Schiff(3 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße(), this, ctn-1);
        }
        for (int i = 0; i < schiffTypen[2]; i++) {
            schiffArray[ctn++] = new Schiff(4 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße(), this, ctn-1);
        }
        for (int i = 0; i < schiffTypen[3]; i++) {
            schiffArray[ctn++] = new Schiff(5 * gridSpielfeld.getKachelgroeße(), gridSpielfeld.getKachelgroeße(), this, ctn-1);
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
     * @param reset Mouds ob Schiffe der GUI hinzugefügt werden oder nicht
     */
    public void zeichneSchiffe(boolean reset) {
        //Alle Schiffe dem Pane als rectangle hinzufügen
        //Die Schiffe auf dem Grid zeichnen rechts Ablegen
        int m = 0; //Merker um schiffe versetzt auszugeben
        int n = gridSpielfeld.getPxGroesse(); //Merker Horizontal
        for (int i = 0; i < schiffArray.length; i++) {
            schiffArray[i].setFill(Color.RED);
            if (!reset) {
                dieGui.zeigeSchiffe(schiffArray[i]);
                makeHandler(schiffArray[i], i);
            }
            if (i == 0) { //Erste Schiff
                schiffArray[i].draw(n, 0); //Rechts neben der Linie auf höhe 0 
            } else {
                int len = schiffArray[i - 1].getLaenge() * gridSpielfeld.getKachelgroeße(); //laenge des vorherigen schiffs
                int mylen = schiffArray[i].getLaenge() * gridSpielfeld.getKachelgroeße();
                n = n + len + gridSpielfeld.getKachelgroeße();      //aktuelle Position + die länge des Letzten + eine Kachel abstand
                if (n + mylen > gridSpielfeld.getPxGroesse() * 2) {           //Wenn n gr0ßer als das Spielfeld
                    m = m + 2 * gridSpielfeld.getKachelgroeße();
                    n = gridSpielfeld.getPxGroesse();
                    schiffArray[i].draw(n, m);
                } else {
                    schiffArray[i].draw(n, m);
                }
            }
        }
    }

    /**
     * Aktiviert für jedes Schiff den Eventhandler Dragged und Release um mit
     * der Maus zu verschieben
     *
     * @param s jeweiliges Schiff für das der Handler erzeugt werden soll
     */
    private void makeHandler(Schiff s,int index) {
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

        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüber
        int snapX = (int) (event.getX() % gridSpielfeld.getKachelgroeße());
        int snapY = (int) (event.getY() % gridSpielfeld.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX() - snapX;
        int y = (int) event.getY() - snapY;

        clearId(s);

        boolean blockiert = false;

        //Abfragen, ob das Schiff aus dem Feld fährt und dies Verhindern
        if (x < 0) { //Nach links raus
            blockiert = true;
            x = 0;
        } else if (y < 0) { //Nach oben raus
            blockiert = true;
            y = 0;
        } else if (x > (1200 - s.getWidth() + 0.5 * gridSpielfeld.getKachelgroeße())) { //nach rechts raus
            blockiert = true;
            x = (int) (1200 - s.getWidth() + 0.5 * gridSpielfeld.getKachelgroeße());
        } else if (y > (600 - s.getHeight()) + 0.5 * gridSpielfeld.getKachelgroeße()) { //nach unten raus
            blockiert = true;
            y = (int) ((600 - s.getHeight()) + 0.5 * gridSpielfeld.getKachelgroeße());
        } else { //wenn alles ok blokiert = false man kan das Schiff bewegen
            blockiert = false;
        }
        //Wenn nicht blokiert bewege das Schiff
        if (!blockiert) {
            if (x <= (gridSpielfeld.getPxGroesse() - s.getWidth())) {
                s.setFill(Color.GREEN);
            } else { //Wenn das Schiff im Rechten Teil ist setzte es Rot => hier nicht plazieren
                s.setFill(Color.RED);
            }
            //Neu setzten der Schiffwerte
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
     */
    public void released(MouseEvent event, Schiff s,int index) {
        //Ermittle den Puffer zur letzten Kachel
        int puff = (int) (s.getX() / gridSpielfeld.getKachelgroeße());
        int puffY = (int) (s.getY() / gridSpielfeld.getKachelgroeße());
        s.setX(puff * gridSpielfeld.getKachelgroeße());
        s.setY(puffY * gridSpielfeld.getKachelgroeße());
        //Ermittle Koordinatenwert der StartPositionen für 2D Array
        int startX = (int) event.getX() / gridSpielfeld.getKachelgroeße();
        int startY = (int) event.getY() / gridSpielfeld.getKachelgroeße();

        if (event.getX() <= gridSpielfeld.getKachelgroeße()) {
            System.out.println("Zu Wenig bewegung gewesen");
        }
        //Überprüfe ob beim Loslassen der Mauszeiger auserhalb des Felds ist
        if (startY >= gridSpielfeld.getKachelAnzahl()) { //Wenn unten raus 
            if (s.getRichtung() == Richtung.VERTIKAL) { //Und Richtung Vertikal 
                startY = puffY; //Setze y auf lezten wert
            } else { //Wenn Richtung horizontal
                startY = puffY; //setze y auf letzten wert
            }
        }
        if (startY < 0) { //Wenn y < 0 oben raus
            startY = 0;
            startX = 0;
            s.draw(startX, startY);
        }
        if (startX < 0) { //Wenn x < 0 links raus
            startX = 0;
            startY = 0;
            s.draw(startX, startY);
        }
        s.setStart(startX, startY);
        s.print();
        setIdNeu(s,index); //Setze die MarkerId unter dem Schiff
        pruefePisition();
        s.draw();
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
        for (Schiff s1 : schiffArray) {
            System.out.println(s1.getX() >= gridSpielfeld.getPxGroesse());
            if (s1.getX() >= gridSpielfeld.getPxGroesse()) {
                s1.setFill(Color.RED);
                dieGui.getOutputField().setText("+++ Noch nicht alle Schiffe platziert +++");
                System.out.println("Noch nicht alle");
                fertig = false;
                fehlend = true;
            } else {
                if (s1.getRichtung() == Richtung.HORIZONTAL) {
                    if (ueberpruefePlatz(s1)) {
                        fertig = true;
                        //dieGui.getOutputField2().setText("");     
                        s1.setFill(Color.GREEN);
                    } else {
                        fertig = false;
                        s1.setFill(Color.RED);
                        //dieGui.getOutputField2().setText("+++ Schiffe falsch plaziert (rot) +++");
                    }
                } else {
                    if (ueberpruefePlatzVertikal(s1)) {
                        fertig = true;
                        //dieGui.getOutputField2().setText("");     
                        s1.setFill(Color.GREEN);
                    } else {
                        fertig = false;
                        s1.setFill(Color.RED);
                        //dieGui.getOutputField2().setText("+++ Schiffe falsch plaziert (rot) +++");
                    }
                }
            }
        }
        if (!fehlend) {
            dieGui.getOutputField().setText("");
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
                gridSpielfeld.getGrid()[i][s.getStartY()].setId("0");
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[s.getStartX()][i].setId("0");
            }
        }
    }

    /**
     * setzt den Marker ID auf dem Grid unter dem plazierten Schiff
     *
     * @param s Schiff für das der Merker gesetzt wird
     */
    public void setIdNeu(Schiff s,int index) {
        int counter = 0;
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[i][s.getStartY()].setId(""+((index+1)*10+ counter));
                counter++;
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                try {
                    gridSpielfeld.getGrid()[s.getStartX()][i].setId(""+((index+1)*10+ counter));
                    counter++;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        gridSpielfeld.print();
    }

    public void setzeRandomSchiffe() {
        int spielfeldgroesse = gridSpielfeld.getKachelAnzahl();
        int wiederholungen = 0;
        boolean allegesetzt = false;
        int anzahlgesetzt = 0;
        Random zufall = new Random();
        int zufallx;
        int zufally;
        int zufallsrichtung; // horizontal--- -> 0, vertikal||| -> 1

        while (!allegesetzt) {
            for (int i = schiffArray.length - 1; i >= 0; i--) {
                if (wiederholungen > 30 && schiffArray[i+1].getLaenge() != 2) {
                    System.out.println("Break");
                    break;
                } else if (wiederholungen > 60 && schiffArray[i+1].getLaenge() == 2) {
                    System.out.println("Break");
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

                    //schiffArray[i].print();
                    System.out.println("Schiff Nr " + i + " x: " + zufallx + " y: " + zufally + " richtung: " + zufallsrichtung + " leange: " + schiffArray[i].getLaenge());

                    if (zufallsrichtung == 0) { // Horizontal ---
                        if (ueberpruefePlatz(schiffArray[i])) {
                            anzahlgesetzt++;
                            System.out.println("Gesetzt");
                            gridSpielfeld.print(); // DEBUG
                        } else {
                            System.out.println("Nicht gesetzt");
                            clearId(schiffArray[i]);
                        }
                    } else if (zufallsrichtung == 1) { // Vertikal |||
                        if (ueberpruefePlatzVertikal(schiffArray[i])) {
                            anzahlgesetzt++;
                            System.out.println("Gesetzt");
                            gridSpielfeld.print(); // DEBUG
                        } else {
                            System.out.println("Nicht gesetzt");
                            clearId(schiffArray[i]);
                        }
                    }
                    wiederholungen++;
                    if (wiederholungen > 30 && schiffArray[i].getLaenge() != 2) {
                        System.out.println("Break");
                        break;
                    } else if (wiederholungen > 60 && schiffArray[i].getLaenge() == 2) {
                        System.out.println("Break");
                        break;
                    }
                }
                System.out.println("Anzahl gebraucht " + wiederholungen);
            }
            if (anzahlgesetzt == schiffArray.length) {
                allegesetzt = true;
                System.out.println("Alle gesetzet");
                gridSpielfeld.print(); // DEBUG
            } else {
                System.out.println("Zurücksetzen");
                wiederholungen = 0;
                clearAll();
                anzahlgesetzt = 0;
                gridSpielfeld.print(); // DEBUG
            }
        }
        fertig = true;
    }

    public void clearAll() {
        for (int i = 0; i < gridSpielfeld.getKachelAnzahl(); i++) {
            for (int j = 0; j < gridSpielfeld.getKachelAnzahl(); j++) {
                gridSpielfeld.getGrid()[i][j].setId("0");
            }
        }
    }

    public boolean setIdNeuAuto(Schiff s , int index) {
        int counter = 0;
        if (s.getRichtung() == Richtung.HORIZONTAL) {
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                if(i < gridSpielfeld.getKachelAnzahl()){
                    if(!gridSpielfeld.getGrid()[i][s.getStartY()].getId().equals("0")){
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[i][s.getStartY()].setId(""+((index+1)*10+ counter));
                counter++;
                //System.out.println("Setze 1: " + i + " / " + s.getStartY());
            }
        } else {
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {    
                if(i < gridSpielfeld.getKachelAnzahl()){
                    if(!gridSpielfeld.getGrid()[s.getStartX()][i].getId().equals("0")){
                        return false;
                    }
                }
                else{
                    return false;
                }
                
            }
            for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                gridSpielfeld.getGrid()[s.getStartX()][i].setId(""+((index+1)*10+ counter));
                counter++;
                //System.out.println("Setze 1: " + s.getStartX() + " / " + i);
            }
        }
        return true;
        //gridSpielfeld.print(); // DEBUG
    }

    /**
     * Die Funktion überpüft für ein Horizontales Schiff die Position hierbei
     * wird geschaut ob ein Marker auf dem Grid in nicht erlaubter nähe zum
     * Schiff ist. Falls ein Marker gefunden wurde wird der status false gesetzt
     * Hierbei werden alle Rand- und Eckenfälle überprüft
     *
     * @param s schiff, welches Überprüft wird
     * @return status: boolean true wenn Schiff ordnungsgemäß plaziert, false
     * wenn falsch plaziert
     */
    private boolean ueberpruefePlatz(Schiff s) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
        if (y - 1 >= 0 && y + 1 <= gridSpielfeld.getKachelAnzahl() - 1 && x + s.getLaenge() <= gridSpielfeld.getKachelAnzahl()) { //Überprüfe auf Rand
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //2.Links/Rechts neben dem Schiff
        if (x - 1 >= 0 && x + 1 <= gridSpielfeld.getKachelAnzahl() - s.getLaenge() && y - 1 >= 0 && y + 1 <= gridSpielfeld.getKachelAnzahl() - 1) { //Randüberprüfung
            for (int i = y - 1; i < y - 1 + 3; i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //3. Randfälle
        if (y - 1 <= 0 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1) { //Am Oberen Rand Zeichnen
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            for (int i = y; i < y + 2; i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        if (x - 1 <= 0 && y - 1 >= 0 && y + 1 <= gridSpielfeld.getKachelAnzahl() - 1) { //Am linken Rand zeichen
            for (int i = y - 1; i < y - 1 + 3; i++) {
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        if (x + 1 == gridSpielfeld.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 >= 0 && y + 1 <= gridSpielfeld.getKachelAnzahl() - 1) { //Wenn rechts raus zeiche zusätlich noch links 
            for (int i = y - 1; i < y - 1 + 3; i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
            }
        }
        if (y + 1 >= gridSpielfeld.getKachelAnzahl() - 1 && x - 1 >= 0 && x + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1) { //Am unteren Rand 
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
            }
            for (int i = y - 1; i < y - 1 + 2; i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //Linke obere Ecke
        if (x - 1 <= 0 && y - 1 <= 0) {
            for (int i = y; i < y + 2; i++) {
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //Rechte obere Ecke
        if (x + 1 == gridSpielfeld.getKachelAnzahl() - s.getLaenge() + 1 && y - 1 <= 0) {
            for (int i = y; i < y + 2; i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y + 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //Rechte untere Ecke
        if (x + 1 == gridSpielfeld.getKachelAnzahl() - s.getLaenge() + 1 && y + 1 >= gridSpielfeld.getKachelAnzahl() - 1) {
            for (int i = y; i > y - 2; i--) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //Linke untere Ecke
        if (x - 1 <= 0 && y + 1 >= gridSpielfeld.getKachelAnzahl() - 1) {
            for (int i = y; i > y - 2; i--) {
                if (!gridSpielfeld.getGrid()[x + s.getLaenge()][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            for (int i = x; i < x + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
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
    private boolean ueberpruefePlatzVertikal(Schiff s) {
        int x = s.getStartX();
        int y = s.getStartY();
        boolean status = true;
        //1.Über/Unter dem Schiff, wenn dort kein Rand ist:
        if (y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1 && x + 1 <= gridSpielfeld.getKachelAnzahl() - 1 && x - 1 >= 0) { //Überprüfe auf Rand
            for (int i = x - 1; i < x + 2; i++) {
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) { //Suche über dem Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Suche unter dem Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //2.Links/Rechts neben dem Schiff
        if (x - 1 >= 0 && x + 1 <= gridSpielfeld.getKachelAnzahl() - 1 && y - 1 >= 0 && y + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1) { //Randüberprüfung
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) { //Links vom Schiff
                    status = false; //Markierung gefunden
                    break; //Breche ab
                }
                if (!gridSpielfeld.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }

        //Sonderfälle:       
        //1. Links
        if (x - 1 <= 0) { //Wenn links raus
            if (y + s.getLaenge() >= gridSpielfeld.getKachelAnzahl()) { //und Unten raus
                System.out.println("Bin Hier");
                for (int i = x; i < x + 2; i++) {
                    if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            if (y - 1 <= 0) { //Und wenn oben raus
                for (int i = x; i < x + 2; i++) {
                    if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }

            if (y + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1 && y - 1 >= 0) {
                for (int i = x; i < x + 2; i++) {
                    if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                    if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
        }
        //2. Rechts raus
        if (x + 1 >= gridSpielfeld.getKachelAnzahl() - 1 && y + s.getLaenge() <= gridSpielfeld.getKachelAnzahl()) {
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }

            }
            if (y - 1 <= 0) { //Und wenn oben raus
                for (int i = x; i > x - 2; i--) {
                    if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            if (y + s.getLaenge() >= gridSpielfeld.getKachelAnzahl() - 1) { //Und wenn unten raus
                for (int i = x; i > x - 2; i--) {
                    if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {//Rechts vom Schiff
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
            if (y + s.getLaenge() <= gridSpielfeld.getKachelAnzahl() - 1 && y - 1 >= 0) { //Rechts raus ohne ohen unten 
                for (int i = x; i > x - 2; i--) {
                    if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                    if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                        status = false; //Markierung gefunden 
                        break; //Brche suche ab
                    }
                }
            }
        }
        //3. Oben Raus
        if (y - 1 <= 0 && x < gridSpielfeld.getKachelAnzahl() - 1 && x - 1 >= 0) {
            for (int i = x - 1; i < x + 2; i++) {
                if (!gridSpielfeld.getGrid()[i][y + s.getLaenge()].getId().equals("0")) {
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x + 1][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) {//Rechts vom Schiff
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        //Unten raus
        if (y >= gridSpielfeld.getKachelAnzahl() - s.getLaenge() && x - 1 >= 0 && x + 1 <= gridSpielfeld.getKachelAnzahl() - 1) {
            for (int i = x - 1; i < x + 2; i++) { //Oben über dem Schiff
                if (!gridSpielfeld.getGrid()[i][y - 1].getId().equals("0")) {
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
            //Neben dem Schiff
            for (int i = y; i < y + s.getLaenge(); i++) {
                if (!gridSpielfeld.getGrid()[x - 1][i].getId().equals("0")) {
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
                if (!gridSpielfeld.getGrid()[x + 1][i].getId().equals("0")) {
                    status = false; //Markierung gefunden 
                    break; //Brche suche ab
                }
            }
        }
        return status;
    }
}
