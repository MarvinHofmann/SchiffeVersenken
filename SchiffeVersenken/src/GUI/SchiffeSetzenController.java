/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controll.Steuerung;
import controll.SteuerungSchiffeSetzen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import schiffeversenken.SchiffeVersenken;
import shapes.Grid;
import shapes.Schiff;

/**
 * FXML Controller class
 *
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann
 */
public class SchiffeSetzenController implements Initializable {

    @FXML
    private Pane schiffeSetzenFeld;
    
    private SteuerungSchiffeSetzen dieSteuerungSchiffeSetzen = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SchiffeSetzen");
        dieSteuerungSchiffeSetzen = new SteuerungSchiffeSetzen(this);
    }

    //anzahlschiffe[2er,3er,4er,5er] jeweils als GZ -> 3*4er 1*2er [1,0,3,0]
    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        System.out.println("Übergabe Spielfeldgroesse und Anzahl der jeweiligen Schiffstypen");
        dieSteuerungSchiffeSetzen.uebergebeInformationen(spielfeldgroesse, anzahlSchiffeTyp);  
    }

    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        SpielGUIController spielGUIController = loader.getController();
        spielGUIController.uebergebeInformationen(gridS.getKachelAnzahl(), schiffArray); // Hier noch zusätzlich koordinaten array übergeben

        Scene scene = new Scene(root);

        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);      // Scene setzen
<<<<<<< Updated upstream
        SchiffeVersenken.getApplicationInstance().getStage().show();
        
        for (int i = 0; i < schiffArray.length; i++) {
            System.out.println("Schiff" + i + ": ");
            System.out.println(schiffArray[i].getHeight());
            System.out.println(schiffArray[i].getWidth());
            System.out.println(schiffArray[i].getStartX());
            System.out.println(schiffArray[i].getStartY());
            System.out.println("####################");
        }
    }

    //Verwalten des Zustands angeklickt und gehlaten
    public void dragged(MouseEvent event, Schiff s) {
        schiffeSetzenFeld.setCursor(Cursor.CLOSED_HAND);
        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüger
        int snapX = (int) (event.getX() % gridS.getKachelgroeße());
        int snapY = (int) (event.getY() % gridS.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX() - snapX;
        int y = (int) event.getY() - snapY;

        //Kontrolle der Grenzen 
        //Wenn in buffer mache Schiff rot
        if (x > gridS.getPxGroesse() - s.getWidth()) {
            s.setFill(Color.RED);
            s.setX(x);
            s.setY(y);
            //Wenn man unten raus will setzte grenze y geht nicht weiter als 600
        } else if (y > gridS.getPxGroesse() - s.getHeight()) {
            s.setX(x);
            s.setY(gridS.getPxGroesse() - s.getHeight());
            //Grenze oben nicht kliener als 0
        } else if (y < 0) {
            s.setX(x);
            s.setY(0);
            //Grenze links nicht kleiner als o
        } else if (x < 0) {
            s.setX(0);
            s.setY(y);
            //Wenn keine grenze erreicht setze normal Schiff ist grün
        } else {
            s.setFill(Color.GREEN);
            s.setX(x);
            s.setY(y);
        }
        //Neu zeichnen
        drawWasser(s, Color.WHITE);
        s.draw();
=======
        SchiffeVersenken.getApplicationInstance().getStage().show();*/
        
        schiffeSetzenFeld.getScene().getRoot().setOnKeyPressed(dieSteuerungSchiffeSetzen);
>>>>>>> Stashed changes
    }

    public void zeigeGrid(Rectangle rectangle) {
        schiffeSetzenFeld.getChildren().add(rectangle);
    }

    public void zeigeSchiffe(Schiff schiff) {
        schiffeSetzenFeld.getChildren().add(schiff);
    }

<<<<<<< Updated upstream
    //Verwalten des Zustands losgelassen
    public void released(MouseEvent event, Schiff s) {
        int puff = (int) (s.getX() / gridS.getKachelgroeße());
        int puffY = (int) (s.getY() / gridS.getKachelgroeße());
        s.setX(puff * gridS.getKachelgroeße());
        s.setY(puffY * gridS.getKachelgroeße());
        s.setFill(Color.GREEN);
        s.setStroke(Color.GREEN);
        //Ermittle Koordinatenwert der StartPositionen für 2D Array
        int startX = (int) s.getX() / gridS.getKachelgroeße();
        int startY = (int) s.getY() / gridS.getKachelgroeße();
        System.out.println(startX + " " + startY);
        s.setStart(startX, startY);
        drawWasser(s, Color.NAVY);
        s.draw();
=======
    public void setzeCursor(Cursor CLOSED_HAND) {
        schiffeSetzenFeld.setCursor(Cursor.CLOSED_HAND);
>>>>>>> Stashed changes
    }

}
