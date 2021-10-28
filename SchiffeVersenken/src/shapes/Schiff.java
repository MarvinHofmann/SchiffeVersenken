/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author marvi
 */
public abstract class Schiff {
    protected int[] trefferArray;

    protected int groesse;
   
    /*
    durchläuft Array und schaut ob getroffen 
        1 -> treffer 
        0 -> heil
    Wenn alle alle getroffen dann versenkt
    */
    public boolean checkVersenkt(){
        for(int i = 0; i<trefferArray.length; i++){
            if (trefferArray[i] == 0) {
                return false;
            }
        }
        return true;
    }
    
    /*
    Aktualisiere Treffer Array an der jeweiligen Stelle
    rufe danach check versenkt auf
    BSP.:
    
        [][X][][] -> Stelle 1 getroffen wird mit 1 belegt
        .
        .
        [X][X][X][X] -> Alle getroffen -> Array durchgehend mit eins belegt -> check versenkt gibt true zurück
    
    muss nicht überprüft werden, da nicht zwei mal auf die gleiche Stelle geschossen
    werden kann
    Woher kommt die Stelle wird hier geparsed nach row/col oder schon woanders?
    */
    public boolean handleTreffer(int stelle){
        trefferArray[stelle] = 1;
        return checkVersenkt(); //gibt true für versenkt und false für nicht versenkt zurück so kann GUI entscheiden
    }  
}
