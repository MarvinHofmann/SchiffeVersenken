/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author marvi GRUPPENNUMMER: 8
 */
public class Schiff extends Rectangle {

    private int startX;
    private int startY;
    private int[] trefferArray;
    private Richtung richtung;
    int kachelgr;
    int laenge;

    public Schiff(int w, int h) {
        this.setHeight(h);
        this.setWidth(w);
        this.kachelgr = h;
        this.laenge = w / h;
        this.trefferArray = new int[laenge];
        this.richtung = Richtung.HORIZONTAL;
        this.setOnMouseClicked(event -> this.requestFocus());
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
                if (event.getCode() == KeyCode.A) {
                    drawInverse();
                }             
            }
        });
    }
    private void drawInverse() {
        System.out.println("Vorher StartX " + startX);
        System.out.println("Vorher StartY " + startY);
        System.out.println("X: " + getX());
        System.out.println("Y: " + getY());
        
        
        Rotate rotate = new Rotate(); 
        rotate.setAngle(90);
        rotate.setPivotX(getX()); 
        
        rotate.setPivotY(getY()); 
        
        this.getTransforms().addAll(rotate); 
        int newX =(int) rotate.getPivotX();
        int newY =(int) rotate.getPivotY();
        this.setX(newX);
        this.setY(newY);
        this.setStart(newX / kachelgr, newY / kachelgr);
        
        System.out.println("newY " + rotate.getPivotY());
        System.out.println("newX " + rotate.getPivotX());
        System.out.println("kachelgr " + kachelgr);
        System.out.println("new StartX " + startX);
        System.out.println("new StartY " + startY);
        setRichtung(Richtung.HORIZONTAL);
        this.setOnMouseClicked(event -> this.requestFocus());
    }
    public int getLaenge() {
        return laenge;
    }

    public void draw(int x, int y) {
        this.setStroke(Color.RED);
        this.setX(x);
        this.setY(y);
    }

    public void draw() {
        this.setX(getX());
        this.setY(getY());
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
     * Durchläuft Array und schaut ob die Schiffsteile getroffen sind: 1 ->
     * zerstört, 0 -> heil
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

    public void setStart(int start, int startY) {
        this.startX = start;
        this.startY = startY;
    }

    /**
     * Aktualisiere Treffer Array an der jeweiligen Stelle rufe danach check
     * versenkt auf BSP.:
     *
     * [][X][][] -> Stelle 1 getroffen wird mit 1 belegt . . [X][X][X][X] ->
     * Alle getroffen -> Array durchgehend mit eins belegt -> check versenkt
     * gibt true zurück
     *
     * muss nicht überprüft werden, da nicht zwei mal auf die gleiche Stelle
     * geschossen werden kann Woher kommt die Stelle wird hier geparsed nach
     * row/col oder schon woanders?
     *
     * @param stelle -> Stelle die ins Array eingetragen wird
     * @return gibt true für versenkt und false für nicht versenkt zurück so
     * kann GUI entscheiden
     */
    public boolean handleTreffer(int stelle) {
        trefferArray[stelle] = 1;
        return checkVersenkt();
    }

}
