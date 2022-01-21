package schiffeversenken;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Marvin Hofmann, Emely Mayer-Walcher, Torben Doese, Lea-Marie Kindermann GRUPPENNUMMER: 8
 */ 
public class SchiffeVersenken extends Application {
    private static SchiffeVersenken application;
    private Stage stage = null;
    
    /**
     * Erste Methode, welche die GUI startet 
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        application = this;
        this.stage = stage;
        stage.setResizable(false); //Größe nicht veränderbar
        stage.setTitle("Schiffe Versenken"); //Titel des Fensters
        stage.getIcons().add(new Image("/Images/ship.png")); //Icon für Fenster und Taskleiste
        setScene("/GUI/Hauptmenue.fxml");  //Hauptmenü Scene laden
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Setzt eine neue Scene auf der Stage
     * @param pfadFXML - pfad zur Ladenden scene *.fxml file
     * @throws IOException 
     */
    public void setScene(String pfadFXML) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(pfadFXML));
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
    
    /**
     * Zum restarten der ganzen stage nach einem Spiel, wenn auf Hauptmenü gecklickt 
     * wird. Sichere Variante um alles im Hintergrund rückzusetzten 
     */
    public void restart(){
        stage.close();
        stage = new Stage();
        try {
            start(stage);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
