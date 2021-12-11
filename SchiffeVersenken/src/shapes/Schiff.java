package shapes;

import controll.SteuerungSchiffeSetzen;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class Schiff extends Rectangle {

    private int startX;
    private int startY;
    private int[] trefferArray;
    private Richtung richtung;
    private int kachelgr;
    private int laenge;
    private int index;
    private SteuerungSchiffeSetzen dieSteuerung;

    /**
     * Erster Konstruktor für Schiff mit höhe und breite in px setzte die größe
     * und die Farbe initialisiere einen eventhandler für ein key event zum
     * drehen
     *
     * @param w breite
     * @param h hoehe
     */
    public Schiff(int w, int h) {
        this.setHeight(h);
        this.setWidth(w);
        this.kachelgr = h;
        this.laenge = w / h;
        this.trefferArray = new int[laenge];
        this.richtung = Richtung.HORIZONTAL;
        this.setFill(Color.RED);
        this.setOnMouseClicked(event -> click(event, this));
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //System.out.println(event.getCode());
                if (event.getCode() == KeyCode.SPACE) {
                    drehen(index);
                }
            }
        });
    }
    
    /**
     * Zweiter Konstruktor
     *
     * @param w breite
     * @param h höhe
     * @param s SteuerungSchiffeSetzen für Funktion clearId und setIdNeu
     * @param i index im array der Schiffsobjekte
     */
    public Schiff(int w, int h, SteuerungSchiffeSetzen s, int i) {
        this(w, h);
        this.dieSteuerung = s;
        this.index = i;
    }
  

    /**
     * Wenn das Schiff angeklickt wurde ist es im Vordergrund für den Handler
     * Durch drücken der Leertaste kann das Schiff gedreht werden
     */
    public void drehen(int index) {
        if (richtung == Richtung.HORIZONTAL) {
            if (startY + getLaenge() <= dieSteuerung.getGridS().getKachelAnzahl()) { //Nur Drehen, wenn das untere Ende im Spielfeld landet
                dieSteuerung.clearId(this); //Bevor gedreht wird lösche die Markierungen hinter dem Schiff
                setRichtung(Richtung.VERTIKAL); //Drehe das Schiff
                dieSteuerung.setIdNeu(this, index); //Setze die neuen Markierungen im Vertikalen Modus
                dieSteuerung.pruefePisition();
                double speicher = this.getWidth();
                this.setWidth(this.getHeight());
                this.setHeight(speicher);
            }else{
                System.out.println("Fehler Schiff zu Groß");
            }
        } else if (richtung == Richtung.VERTIKAL) {
            dieSteuerung.clearId(this); //Lösche Vertikale Markierungen
            setRichtung(Richtung.HORIZONTAL);
            dieSteuerung.setIdNeu(this,index); //Mache neue Horizontale Markierungen
            dieSteuerung.pruefePisition();
            double speicher = this.getWidth();
            this.setWidth(this.getHeight());
            this.setHeight(speicher);

        }
        this.setOnMouseClicked(event -> click(event, this));

    }
    
    public void dreheGui(){
        double speicher = this.getWidth();
        this.setWidth(this.getHeight());
        this.setHeight(speicher);
    }

    /**
     * Zeichnet das Schiff an vorgegebene Werte
     *
     * @param x x Wert
     * @param y y Wert
     */
    public void draw(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Zeichnet das Schiff neu an den aktuellen Wert
     */
    public void draw() {
        this.setX(getX());
        this.setY(getY());
    }

    public int getLaenge() {
        return laenge;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int[] getTrefferArray() {
        return trefferArray;
    }

    public Richtung getRichtung() {
        return richtung;
    }

    public void setTrefferArray(int[] trefferArray) {
        this.trefferArray = trefferArray;
    }

    public void setRichtung(Richtung richtung) {
        this.richtung = richtung;
    }

    /**
     * Durchläuft Array und schaut ob die Schiffsteile getroffen sind: 1 -
     * zerstört, 0 - heil
     *
     * @return true für vollständig versenkt, false für nicht vollständig
     * versenkt
     */
    public boolean checkVersenkt() {
        for (int i = 0; i < trefferArray.length; i++) {
            if (trefferArray[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public void setStart(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Aktualisiere Treffer Array an der jeweiligen Stelle rufe danach check
     * versenkt auf BSP.:
     *
     * [][X][][] - Stelle 1 getroffen wird mit 1 belegt . . [X][X][X][X] - Alle
     * getroffen - Array durchgehend mit eins belegt - check versenkt gibt true
     * zurück
     *
     * muss nicht überprüft werden, da nicht zwei mal auf die gleiche Stelle
     * geschossen werden kann Woher kommt die Stelle wird hier geparsed nach
     * row/col oder schon woanders?
     *
     * @param stelle - Stelle die ins Array eingetragen wird
     * @return gibt true für versenkt und false für nicht versenkt zurück so
     * kann GUI entscheiden
     */
    public boolean handleTreffer(int stelle) {
        trefferArray[stelle] = 1;
        return checkVersenkt();
    }

    public void print() {
        System.out.println("Schiff:");
        System.out.println("StartX: " + startX);
        System.out.println("StartY: " + startY);
        System.out.println("getX: " + getX());
        System.out.println("getY: " + getY());
        System.out.println("Länge: " + laenge);
        System.out.println("Richtung: " + richtung);
    }

    private void click(MouseEvent event, Schiff s) {
        this.requestFocus();
        for (int i = 0; i < dieSteuerung.getSchiffArray().length; i++) {
            dieSteuerung.getSchiffArray()[i].setStrokeWidth(1);
        }
        this.setStrokeWidth(3.5);
        this.setStroke(Color.BLUE);
        this.print();
    }

}
