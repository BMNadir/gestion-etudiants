package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import gestion_etudiant.DbUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
    @FXML
    private JFXProgressBar progressBar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        warning.setVisible(false);
        progressBar.setVisible(false);
    }    

    @FXML
    private void connecter(ActionEvent event) {
        progressBar.setVisible(true);
        Task<Void> task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception {
                try { 
                        String requette = "SELECT * FROM Admin WHERE username=? AND password=?";
                        resultSet = DbUtil.execQuery(requette, uname.getText(), pword.getText());

                        if (!resultSet.next())
                        {
                            warning.setVisible(true);
                            return null;
                        }
                        try {
                            Parent secondView = (StackPane) FXMLLoader.load(getClass().getResource("/gestion_etudiant/FXMLDocument.fxml"));
                            Scene newScene = new Scene(secondView);
                            Stage currentStage = (Stage) rootPane.getScene().getWindow();
                            Platform.runLater(() -> {
                                currentStage.setScene(newScene);
                                this.cancel(true);
                            });
                        } catch (IOException ex) {
                        Logger.getLogger(LoginInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                return null;
            }
        };
        Thread thread = new Thread (task);
        thread.setDaemon(true); // Terminate the running thread if the application exits
        thread.start();
    }
}
