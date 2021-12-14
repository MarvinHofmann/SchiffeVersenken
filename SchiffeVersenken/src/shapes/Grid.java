package shapes;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author marvi
 */
public class Grid {

    private int pxGroesse = 600;
    private int kachelAnzahl = 0; // 5-30
    private int kachelgroeße = 0; // Größe einer einzelnen Kachelgröße
    private Rectangle[][] grid; // Plazierfeld
    private int verschiebung = 50; // Spalt zwischen den beiden Grids
 
    Color c = new Color(0.8,1,0.75,1); // standard constructor, use 0->1.0 values, explicit alpha of 1.0
    /**
     * Konstruktor 
     * Bekommt die Kachelanzahl aus welcher eine Kachelgröße errechnet wird 
     * eine Kachelgröße entspricht einem Quadrat auf der GUI
     * Das 2 Dim Grid vom typ Rectangle wird Deklariert
     *
     * @param kachelAnzahl Gewählte Spielfeldgröße zwischen 0 und 30
     */
    public Grid(int kachelAnzahl) {
        this.kachelAnzahl = kachelAnzahl;
        this.kachelgroeße = this.pxGroesse / this.kachelAnzahl; // Berechung der einzelnen Kachelgröße
        this.pxGroesse = kachelgroeße * kachelAnzahl - this.pxGroesse % this.kachelgroeße; // Wenn nicht ganzzahlig teilbar ändere die Größe 
        grid = new Rectangle[kachelAnzahl][kachelAnzahl]; // Initialisiere grid -> zwei gleichgroße Rectangelfelder [breite][höhe]
    }

    /**
     * Erstellt ein Zweidimensionales Rectangle Array
     *
     * @return grid = 2 Dim Array
     */
    public Rectangle[][] macheGridLinks() {
        //von 0 bis zur pixelgröße* 2, für schiffe erzeuge rechtecke immer gleich groß
        for (int i = 0; i < pxGroesse; i += kachelgroeße) { // Breite des Grids 
            for (int j = 0; j < pxGroesse; j += kachelgroeße) { // Höhe des Grids
                Rectangle r = new Rectangle(i, j, kachelgroeße, kachelgroeße); //Nach und nach rectangles erzeugen
                grid[i / kachelgroeße][j / kachelgroeße] = r;
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setId("0");
            }
        }
        return grid;
    }
    
    
    /**
     * Erstellt das Rechte Grid im Spielfeld oder Schiffe Setzten
     * Das Grid fängt auf der GUI dementsprechend weit rechts an.
     * @return 
     */
    public Rectangle[][] macheGridRechts() {
        //von 0 bis zur pixelgröße* 2, für schiffe erzeuge rechtecke immer gleich groß
        for (int i = (pxGroesse+verschiebung); i < pxGroesse*2+verschiebung; i += kachelgroeße) { // Breite des Grids 
            for (int j = 0; j < pxGroesse; j += kachelgroeße) { // Höhe des Grids
                Rectangle r = new Rectangle(i, j, kachelgroeße, kachelgroeße); //Nach und nach rectangles erzeugen
                grid[((i-pxGroesse-verschiebung) / kachelgroeße)][j / kachelgroeße] = r;
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setId("0");
            }
        }
        return grid;
    }

    /**
     * Aktiviert für jedes Rectangle im Grid ein Mouse Event um auf Clicken
     * reagieren zu können
     */
    public void enableMouseClick() {
        for (int i = 0; i < kachelAnzahl; i ++) {
            for (int j = 0; j < kachelAnzahl; j ++) {
                Rectangle r = grid[i][j];
                r.setOnMouseExited(event -> exit(event, r));
                r.setOnMouseEntered(event -> enter(event, r));
            }
        }
     }

    public int getVerschiebung() {
        return verschiebung;
    }

    /**
     * Maus Event wenn kachel verlassen wird. Farbe wird wird zurückgesetzt
     * @param event
     * @param r 
     */
    private void exit(MouseEvent event, Rectangle r) {
        if (event.getX() > this.getPxGroesse() / 2) {
            if (r.getFill() instanceof Color && r.getFill() != Color.TRANSPARENT) {
                r.setFill(Color.WHITE);
            }
        } else { //Clear das Feld wenn raus 
            for (int i = 0; i < kachelAnzahl; i++) {
                for (int j = 0; j < kachelAnzahl; j++) {
                    if (grid[i][j].getFill() != Color.RED && grid[i][j].getFill() != Color.BLUE) {
                        grid[i][j].setFill(Color.WHITE);
                    }
                }
            }
        }
    }

    
    /**
     * Mouse Event das beim Betreten einer Kachel ausgelöst wird
     * Farbe der kachel wird mit 30% auf ein Grün gelegt das ist dei Kachel auf der die Maus ist
     * @param event
     * @param r Jeweilige kachel
     */
    private void enter(MouseEvent event, Rectangle r) {
        if (r.getFill() != Color.RED && r.getFill() != Color.BLUE && r.getFill() instanceof Color) {
            r.setFill(c);
        }
    }

    //Getter / Setter
    public int getPxGroesse() {
        return pxGroesse;
    }

    public int getKachelAnzahl() {
        return kachelAnzahl;
    }

    public int getKachelgroeße() {
        return kachelgroeße;
    }

    public Rectangle[][] getGrid() {
        return grid;
    }

    /**
     * Gibt das 2-Dim Array für Debug Informationen auf der Konsole aus
     */
    public void print() {
        System.out.println("");
        for (int i = 0; i < kachelAnzahl; i++) {
            for (int j = 0; j < kachelAnzahl; j++) {
                System.out.print(grid[j][i].getId() + "\t|\t");
            }
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }
}
