package shapes;

import Server.Server;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author marvi
 */
public class Grid {

    private int pxGroesse = 600;
    private int kachelAnzahl = 0; // 5-30
    private int kachelgroeße = 0; // Größe einer einzelnen Kachelgröße
    private Rectangle[][] grid; // Plazierfeld
    private Server serverIn;
    private int verschiebung = 50; // Spalt zwischen den beiden Grids

    /**
     * Konstruktor 1 ohne Server Informationen
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
     * Konstruktor 2 mit Server Objekt
     *
     * @param kachelA Gewählte Spielfeldgröße zwischen 0 und 30 für 1.
     * Konstruktor
     * @param server Server Objekt
     */
    public Grid(int kachelA, Server server) {
        this(kachelA);
        this.serverIn = server;
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
                if (i >= kachelAnzahl * kachelgroeße) { // Ende des setzbaren Felds besonders markiert, kästechen grau mit weißem Rand
                    r.setFill(Color.GRAY);
                    r.setStroke(Color.WHITE);
                } else { // Wenn noch im normalem spielfeld, kästechen weiß mit schwarzem rand
                    r.setFill(Color.WHITE);
                    r.setStroke(Color.BLACK);
                }
                r.setId("0");
            }
        }
        return grid;
    }
    
    public Rectangle[][] macheGridRechts() {
        //von 0 bis zur pixelgröße* 2, für schiffe erzeuge rechtecke immer gleich groß
        for (int i = (pxGroesse+verschiebung); i < pxGroesse*2+verschiebung; i += kachelgroeße) { // Breite des Grids 
            for (int j = 0; j < pxGroesse; j += kachelgroeße) { // Höhe des Grids
                Rectangle r = new Rectangle(i, j, kachelgroeße, kachelgroeße); //Nach und nach rectangles erzeugen
                grid[((i-pxGroesse-verschiebung) / kachelgroeße)][j / kachelgroeße] = r;
                if (i >= kachelAnzahl * kachelgroeße) { // Ende des setzbaren Felds besonders markiert, kästechen grau mit weißem Rand
                    r.setFill(Color.GRAY);
                    r.setStroke(Color.WHITE);
                } else { // Wenn noch im normalem spielfeld, kästechen weiß mit schwarzem rand
                    r.setFill(Color.WHITE);
                    r.setStroke(Color.BLACK);
                }
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
        for (int i = pxGroesse; i < pxGroesse; i += kachelgroeße) {
            for (int j = 0; j < pxGroesse; j += kachelgroeße) {
                Rectangle r = grid[i / kachelgroeße][j / kachelgroeße];
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
            if (r.getFill() != Color.RED && r.getFill() != Color.BLUE && r.getFill() instanceof Color) {
                r.setFill(Color.GRAY);
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
            r.setFill(new Color(0.2, 0.5, 0.0, 0.2));
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
