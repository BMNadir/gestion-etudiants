package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gestion_etudiant.DbUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginInController implements Initializable {
    
    private ResultSet resultSet;
    @FXML
    private StackPane rootPane;

    @FXML
    private JFXTextField uname = new JFXTextField();
    
    @FXML
    private JFXPasswordField pword = new JFXPasswordField();
    
    @FXML
    private Label warning = new Label();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        warning.setVisible(false);
    }    

    @FXML
    private void connecter(ActionEvent event) throws SQLException {
        String requette = "SELECT * FROM Admin WHERE username=? AND password=?";
        resultSet = DbUtil.execQuery(requette, uname.getText(), pword.getText());
        
        if (!resultSet.next())
        {
            warning.setVisible(true);
            return;
        }
        Parent secondView;
        try {
            secondView = (StackPane) FXMLLoader.load(getClass().getResource("/gestion_etudiant/FXMLDocument.fxml"));
            Scene newScene = new Scene(secondView);
            //newScene.getStylesheets().add("/css/style.css");
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(newScene);
            
        } catch (IOException ex) {
            Logger.getLogger(LoginInController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    
}
