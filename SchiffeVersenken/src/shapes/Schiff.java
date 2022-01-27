package shapes;

import controll.SchiffeSetzen;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class Schiff extends Rectangle {

    private int startX = -1;
    private int startY = -1;
    private int[] trefferArray;
    private Richtung richtung;
    private int kachelgr;
    private int laenge;
    private int index;
    private boolean gesetzt;
    private boolean plaziert = false;
    private SchiffeSetzen dieSteuerung;

    /**
     * Erster Konstruktor für Schiff mit Höhe und Breite in px setze die Größe
     * und die Farbe. Initialisiere einen Event-Handler für ein Key-Event zum
     * Drehen
     *
     * @param w - breite
     * @param h - hoehe
     */
    public Schiff(int w, int h) {
        this.setHeight(h);
        this.setWidth(w);
        this.kachelgr = h;
        this.laenge = w / h;
        this.trefferArray = new int[laenge];
        this.richtung = Richtung.HORIZONTAL;
        this.setFill(Color.TRANSPARENT);
        this.setOnMouseClicked(event -> click(event, this));
        //Erstellt für jedes Schiff ein Eventhandler für die Leertaste zu Schiff drehen
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    drehen(index);
                }
            }
        });
    }

    /**
     * Zweiter Konstruktor, welcher zusätzlich noch die
     *
     * @param w - breite
     * @param h - höhe
     * @param s - SteuerungSchiffeSetzen für Funktion clearId und setIdNeu
     * @param i - index im Array der Schiffsobjekte
     */
    public Schiff(int w, int h, SchiffeSetzen s, int i) {
        this(w, h);
        this.dieSteuerung = s;
        this.index = i;
    }

    /**
     * Konstruktor, welcher zusätzlich noch den Index des Schiffs setzt
     *
     * @param w - breite
     * @param h - höhe
     * @param i - index im Array der Schiffsobjekte
     */
    public Schiff(int w, int h, int i) {
        this(w, h);
        this.index = i;
    }

    /**
     * Wenn das Schiff angeklickt wurde, ist es im Vordergrund für den Handler.
     * Durch drücken der Leertaste kann das Schiff gedreht werden.
     *
     * @param index index des Schiffs im Schiffarray
     */
    public void drehen(int index) {
        // Wenn Schiffe noch nicht links plaziert wurden und beim ersten Start gedreht werden
        // setzte Startwert auf aktuellen Platz des Schiffs

        if (this.getX() + this.getLaenge() * dieSteuerung.getGridSpielfeldLinks().getKachelgroeße() > dieSteuerung.getGridSpielfeldLinks().getPxGroesse()) {
            //Wenn das Schiff noch nicht ganz Links ist darf man nicht drehen -> Verlasse die Funktion
            dieSteuerung.setInfo("Drehen nur Links möglich!");
            return;
        }
        if ((startX == -1 || plaziert == false)) {
            startX = (int) getX() / kachelgr;
            startY = (int) getY() / kachelgr;
        }
        if (richtung == Richtung.HORIZONTAL) { //Drehe schiff von Horizontal nach Vertikal
            if (startY + getLaenge() <= dieSteuerung.getGridSpielfeldLinks().getKachelAnzahl()) { //Nur Drehen, wenn das untere Ende im Spielfeld landet
                //Nächste bedingung gleich, verhindert, dass gleichzeitig gedreht und bewegt werden kann und das schiff aus dem Grid fällt
                if ((int) getY() / kachelgr + getLaenge() <= dieSteuerung.getGridSpielfeldLinks().getKachelAnzahl()) { //Überprüfe nochmals die bedingung 
                    if (kollision(this, 1)) { // fall 1 horizontal
                        dieSteuerung.setInfo("Zu wenig Platz zum drehen!");
                        return;
                    }
                    dieSteuerung.clearId(this); //Bevor gedreht wird lösche die Markierungen hinter dem Schiff
                    setRichtung(Richtung.VERTIKAL); //Drehe das Schiff
                    setStart((int) getX() / kachelgr, (int) getY() / kachelgr);
                    double speicher = this.getWidth(); //drehen im HIntergrund
                    this.setWidth(this.getHeight());
                    this.setHeight(speicher);
                    dieSteuerung.setIdNeu(this, index); //Setze die neuen Markierungen im Vertikalen Modus
                    dieSteuerung.pruefePisition();
                }
            } else {
                dieSteuerung.setInfo("Zu wenig Platz zum drehen!");
            }
        } else if (richtung == Richtung.VERTIKAL) {
            if (startX + getLaenge() <= dieSteuerung.getGridSpielfeldLinks().getKachelAnzahl()) { //Nur drehen, wenn rechts Platz hat
                if (kollision(this, 2)) { // fall 2 vertikal
                    dieSteuerung.setInfo("Zu wenig Platz zum drehen!");
                    return;
                }
                dieSteuerung.clearId(this);
                setRichtung(Richtung.HORIZONTAL);
                double speicher = this.getWidth();
                this.setWidth(this.getHeight());
                this.setHeight(speicher);
                setStart((int) getX() / kachelgr, (int) getY() / kachelgr);
                dieSteuerung.setIdNeu(this, index); //Mache neue Horizontale Markierungen
                dieSteuerung.pruefePisition();

            } else {
                dieSteuerung.setInfo("Zu wenig Platz zum drehen!");
            }
        }
        this.setOnMouseClicked(event -> click(event, this));
        dreheBild();
    }

    /**
     * Überprüft ob an den stellen, an die das Schiff gedreht werden soll schon ein Schiff liegt.
     * @param s schiff, welches gedreht wird
     * @param fall 1 horizontal soll vertikal werden; 2: vertikal soll horizontal werden
     * @return true wenn kollision entdeckt, false wenn keine
     */
    private boolean kollision(Schiff s, int fall) {
        switch (fall) {
            case 1:
                for (int i = s.getStartY(); i < s.getStartY() + s.getLaenge(); i++) {
                    String idSchiff = (dieSteuerung.getGridSpielfeldLinks().getGrid()[s.getStartX()][s.getStartY()].getId());
                    if (!dieSteuerung.getGridSpielfeldLinks().getGrid()[startX][i].getId().equals("0") && !(dieSteuerung.getGridSpielfeldLinks().getGrid()[startX][i].getId().equals(idSchiff))) {
                        return true;
                    }
                }
                break;
            case 2:
                for (int i = s.getStartX(); i < s.getStartX() + s.getLaenge(); i++) {
                    String idSchiff = (dieSteuerung.getGridSpielfeldLinks().getGrid()[startX][s.getStartY()].getId());
                    if (!dieSteuerung.getGridSpielfeldLinks().getGrid()[i][startY].getId().equals("0") && !(dieSteuerung.getGridSpielfeldLinks().getGrid()[i][startY].getId().equals(idSchiff))) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    /**
     * Dreht beim automatischen Plazieren die Schiffe logisch und graphisch
     */
    public void dreheGui() {
        double speicher = this.getWidth();
        this.setWidth(this.getHeight());
        this.setHeight(speicher);
        String str = "/Images/boot" + this.getLaenge() + "Full.png";
        Image img = new Image(str);
        this.setFill(new ImagePattern(img));
    }

    /**
     * Dreht das Schiff-Image beim Drehen mit der Leertaste
     */
    public void dreheBild() {
        if (this.getRichtung() == Richtung.HORIZONTAL) {
            String pfadZuBild = "/Images/boot" + (int) this.getLaenge() + "FullH.png";
            Image img = new Image(pfadZuBild);
            this.setFill(new ImagePattern(img));
        } else if (this.getRichtung() == Richtung.VERTIKAL) {
            String pfadZuBild = "/Images/boot" + (int) this.getLaenge() + "Full.png";
            Image img = new Image(pfadZuBild);
            this.setFill(new ImagePattern(img));
        }
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

    /**
     * Durchläuft das Array und schaut ob die Schiffsteile getroffen sind: 1 -
     * getroffen, 0 - heil
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

    /**
     * Aktualisiere Treffer-Array an der jeweiligen Stelle rufe danach check
     * versenkt
     *
     * @param stelle - Stelle die ins Array eingetragen wird
     * @return gibt true für versenkt und false für nicht versenkt zurück so
     * kann GUI entscheiden
     */
    public boolean handleTreffer(int stelle) {
        trefferArray[stelle] = 1;
        return checkVersenkt();
    }

    /**
     * Zum nachbauen des TrefferArrays nach laden
     *
     * @param stelle 0 ist Kopf des Schiffs schiff.lenght-1 letzte stelle
     */
    public void setzteTrefferArray(int stelle) {
        trefferArray[stelle] = 1;
    }

    /**
     * Setzt ein Schiff in den Fokus falls es angeklickt wurde, so kann es nach
     * einem Klick gedreht werden
     *
     * @param event
     * @param s Schiff welches in Vordergrund gerueckt werden soll
     */
    private void click(MouseEvent event, Schiff s) {
        this.requestFocus();
    }

    //Getter / Setter 
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isGesetzt() {
        return gesetzt;
    }

    public void setGesetzt(boolean gesetzt) {
        this.gesetzt = gesetzt;
    }

    public boolean isPlaziert() {
        return plaziert;
    }

    public void setPlaziert(boolean plaziert) {
        this.plaziert = plaziert;
    }

    public void setStart(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Gibt Informationen ueber Schiff auf der Konsole aus
     */
    public void print() {
        System.out.println("Schiff:");
        System.out.println("Index: " + index);
        System.out.println("StartX: " + startX);
        System.out.println("StartY: " + startY);
        System.out.println("getX: " + getX());
        System.out.println("getY: " + getY());
        System.out.println("Länge: " + laenge);
        System.out.println("Richtung: " + richtung);
        System.out.println("Bounds: " + this.getBoundsInParent());
    }
}
