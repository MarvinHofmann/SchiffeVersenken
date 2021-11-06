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
        //Grid Zeichnen rectangle kacheln dem Pane hinzufügen
        for (int i = 0; i < feld.length; i++) {
            for (int j = 0; j < feld.length - gridS.getBuffer(); j++) {
                schiffeSetzenFeld.getChildren().add(feld[i][j]);
            }
        }
        macheSchiffe();
    }
    
    //Erstellen der Schiffsobjekte nach übergebener anzahl
    private void macheSchiffe(){
        //Erstelle alle Schiffe und Speichere sie in einem Array voller 
        //Schiffsobjekte ab
        schiffArray = new Schiff[anzSchiffe];
        int ctn = 0;
        for (int i = 0; i < schiffTypen[0]; i++) {
            schiffArray[ctn++] = new Schiff(2 * gridS.getKachelgroeße(), gridS.getKachelgroeße());
            System.out.println("Erstelle typ 2");
            
        }
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
        int m = 0; //Merker um schiffe versetzt auszugeben
        //Alle Schiffe dem Pane als rectangle hinzufügen
        //Die Schiffe auf dem Grid zeichnen und mit einer Zeile abstand im Buffer Ablegen
        for (int i = 0; i < schiffArray.length; i++) {
            schiffeSetzenFeld.getChildren().add(schiffArray[i]);
            schiffArray[i].draw(gridS.getPxGroesse(), 2*m);                
            makeHandler(schiffArray[i]);
            m = m + gridS.getKachelgroeße();
        }  
    }
    //Die javaFX handler für die Schiffe starten 
    private void makeHandler(Schiff s){
        s.setOnMouseDragged(event -> dragged(event, s));
        s.setOnMouseReleased(event -> released(event, s));
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
    
    //Verwalten des Zustands angeklickt und gehlaten
    public void dragged(MouseEvent event, Schiff s) {
        schiffeSetzenFeld.setCursor(Cursor.CLOSED_HAND);
        //Berechne den Puffer zur nächsten grenze nach links und unten zurück kommt ein
        //int wert zwichen 0 und 59 welcher minus der aktuellen Position dem Objekt zum setzten 
        //Übergeben wird, so läuft ein schiff nur in den Kacheln und nicht quer darüger
        int snapX =(int) (event.getX() % gridS.getKachelgroeße());
        int snapY =(int) (event.getY() % gridS.getKachelgroeße());
        //setzte x,y Wert für Objetk
        int x = (int) event.getX()-snapX;
        int y = (int) event.getY()-snapY;
        //Kontrolle der Grenzen 
        if(x > gridS.getPxGroesse() - s.getWidth()) {
            s.setFill(Color.RED);
            s.setX(x);
            s.setY(y);
        }else if(y > gridS.getPxGroesse() - s.getHeight()){
            s.setX(x);
            s.setY(gridS.getPxGroesse() - s.getHeight());  
        }else{
            s.setFill(Color.GREEN);
            s.setX(x);
            s.setY(y);   
        }
        //Neu zeichnen
        s.draw();
    }
    //Verwalten des Zustands losgelassen
    public void released(MouseEvent event, Schiff s) { 
        int puff =(int) (s.getX() / gridS.getKachelgroeße());
        int puffY =(int) (s.getY() / gridS.getKachelgroeße());
        System.out.println("Puff: " + puff + " PuffY: " + puffY);
        s.setX(puff * gridS.getKachelgroeße());
        s.setY(puffY * gridS.getKachelgroeße());
        s.setFill(Color.GREEN);
        s.setStroke(Color.GREEN);
        s.draw();
      }
    

}