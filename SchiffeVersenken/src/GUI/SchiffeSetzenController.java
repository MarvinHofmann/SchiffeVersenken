/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


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
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie
 * Kindermann
 */
public class SchiffeSetzenController implements Initializable {

    @FXML
    private Pane schiffeSetzenFeld;

    //Spielfeld => 600x600
    //geseetzt werden dummy rectangles
    private int[] schiffTypen;
    private int buffer = 0;
    private int anzSchiffe = 0;
    private Rectangle[][] grid; //Plazierfeld
    Schiff[] schiffArray;
    Grid gridS;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("SchiffeSetzen");
    }

    //anzahlschiffe[2er,3er,4er,5er] jeweils als GZ -> 3*4er 1*2er [1,0,3,0]
    void uebergebeInformationen(int spielfeldgroesse, int[] anzahlSchiffeTyp) {
        System.out.println("Übergabe Spielfeldgroesse und Anzahl der jeweiligen Schiffstypen");
        
        this.schiffTypen = anzahlSchiffeTyp;
        for (int i = 0;i< schiffTypen.length; i++) {
            anzSchiffe += schiffTypen[i];
        }
        //DEBUG
        for (int i = 0; i < schiffTypen.length; i++) {
            System.out.println(schiffTypen[i]);
        }
        //
        drawAll(spielfeldgroesse);
    }
    
    private void drawAll(int gr){
        gridS = new Grid(gr);
        Rectangle[][] feld = gridS.macheGrid();
        System.out.println(feld.length);
        for (int i = 0; i < feld.length; i++) {
            for (int j = 0; j < feld.length - gridS.getBuffer(); j++) {
                schiffeSetzenFeld.getChildren().add(feld[i][j]);
            }
        }
        macheSchiffe();
    }
    
    
    private void macheSchiffe(){
        //Erstelle alle Schiffe und Speichere sie in einem Array voller 
        //Schiffsobjekte ab
        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < schiffTypen[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            System.out.println("Erstelle typ 2");
            schiffeSetzenFeld.getChildren().add(schiffArray[i]);
        }
        schiffArray[0].draw(0,0);
        schiffArray[1].draw(600,0);
        schiffArray[1].draw(0,600);
        for (int i = 0; i < schiffTypen[1]; i++) {
            schiffArray[ctn++] = new Schiff(3*gridS.getKachelgroeße(), gridS.getKachelgroeße());
            System.out.println("Erstelle typ 3");
        }
        for (int i = 0; i < schiffTypen[2]; i++) {
            schiffArray[ctn++] = new Schiff(4*gridS.getKachelgroeße(), gridS.getKachelgroeße());
            System.out.println("Erstelle typ 4");
        }
        for (int i = 0; i < schiffTypen[3]; i++) {
            schiffArray[ctn++] = new Schiff(5* gridS.getKachelgroeße(),gridS.getKachelgroeße());
            System.out.println("Erstelle typ 5");
        }
        //DEBUG
        for (int i = 0; i < schiffArray.length; i++) {
            System.out.println(schiffArray[i]);
        }
        //
        schiffArray[0].setOnMouseDragged(event -> dragged(event, schiffArray[0]));
        schiffArray[1].setOnMouseDragged(event -> dragged(event, schiffArray[1]));
    }
   
    
    @FXML
    private void handleButtonStart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SpielGUI.fxml"));
        Parent root = loader.load();
        SpielGUIController spielGUIController = loader.getController();
        spielGUIController.uebergebeInformationen(gridS.getKachelAnzahl()); // Hier noch zusätzlich koordinaten array übergeben

        Scene scene = new Scene(root);

        SchiffeVersenken.getApplicationInstance().getStage().setScene(scene);      // Scene setzen
        SchiffeVersenken.getApplicationInstance().getStage().show();
    }

    private void clicked(MouseEvent event, Rectangle r) {
        
    }
    
    public void dragged(MouseEvent event, Schiff s) {
        schiffeSetzenFeld.setCursor(Cursor.CROSSHAIR);
        //s.setX((s.getX() + event.getX()));
        //s.setY((s.getY() + event.getY()));
        //s.setX(event.getX());
        //s.setY(event.getY());
        double puff = (s.getX() + event.getX()) / gridS.getPxGroesse();
        double puffY = (s.getY() + event.getY()) % gridS.getPxGroesse();
        s.setX((s.getX() + event.getX()) - puff);
        s.setY((s.getY() + event.getY()) );
        System.out.println("Y: " + s.getY());
        System.out.println("X: " + s.getX());
        System.out.println("Höhe " + s.getHeight());
        System.out.println("Breite " + s.getWidth());
        System.out.println();
        s.draw();
        
    }

}
