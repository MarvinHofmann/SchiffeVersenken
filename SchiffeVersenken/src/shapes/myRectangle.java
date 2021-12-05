/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author marvi
 */
public class myRectangle extends Rectangle{
    //belegt oder frei 
    String status = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
