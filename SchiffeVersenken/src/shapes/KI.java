/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author esmay
 */
public class KI {
    private int spielfeldgroesse;
    private Schiff[] schiffArray;
    private int[] anzahlSchiffeTyp;
    private boolean fertig = true;

    public KI(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        this.spielfeldgroesse = spielfeldgroesse;
        this.anzahlSchiffeTyp = anzahlSchiffeTyp;
    }

    public void erzeugeEigeneSchiffe() {
        // Methode in der KI ihre Schiffe setztz
    }

    public Schiff[] getSchiffArray() {
        return schiffArray;
    }

    public boolean isFertig() {
        return fertig;
    }
    
    
}
