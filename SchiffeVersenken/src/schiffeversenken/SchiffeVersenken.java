/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schiffeversenken;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann GRUPPENNUMMER: 8
 */ 
public class SchiffeVersenken extends Application {
    
    private static SchiffeVersenken application;
    private Stage stage = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        application = this;
        this.stage = stage;
        stage.setTitle("Schiffe Versenken"); 
        stage.getIcons().add(new Image("/Images/ship.png"));
        setScene("/GUI/Hauptmenue.fxml");  //Hauptmenü Scene laden
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void setScene(String fxml) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        stage.setScene(scene);      // Scene setzen
        stage.show();               // Scene zeigen
    }
    
    public static SchiffeVersenken getApplicationInstance(){
        return application;
    }

    public Stage getStage() {
        return stage;
    }
}
