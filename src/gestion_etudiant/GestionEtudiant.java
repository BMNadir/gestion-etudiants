package gestion_etudiant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author NaD!R
 */
public class GestionEtudiant extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login/loginIn.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setHeight(565);
        stage.setWidth(576);
        stage.setTitle("Programme Gestion Etudiants");
        stage.getIcons().add(new Image("file:img/ge.png"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DbUtil.dbConnection();
        launch(args);
        DbUtil.dbDisconnect();
    }
    
}
