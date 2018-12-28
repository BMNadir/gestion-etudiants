package gestion_etudiant;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author B.M. Nadir
 */
public class FXMLDocumentController implements Initializable {
    
    ObservableList<String> critereRecherche = FXCollections.observableArrayList("id_etudiant", "nom_etudiant", "prenom_etudiant", "date_naiss", "adresse_etudiant");
    ObservableList<Etudiant> listeEtudiant = FXCollections.observableArrayList();
    
    @FXML
    private StackPane stackPane = new StackPane();
    
    @FXML
    private VBox vBox = new VBox();
    
    @FXML
    private JFXTextField searchArea;
    
    @FXML
    private JFXTextField nom = new JFXTextField();

    @FXML
    private JFXTextField prenom = new JFXTextField();

    @FXML
    private JFXTextField birthdate = new JFXTextField();

    @FXML
    private JFXTextField address = new JFXTextField();
    
    @FXML
    private JFXComboBox combo = new JFXComboBox();
    @FXML
    private TableView<Etudiant> studentTable;
    @FXML
    private TableColumn<Etudiant, String> idCol;
    @FXML
    private TableColumn<Etudiant, String> nomCol;
    @FXML
    private TableColumn<Etudiant, String> prenomCol;
    @FXML
    private TableColumn<Etudiant, String> bDateCol;
    @FXML
    private TableColumn<Etudiant, String> addrCol;
    @FXML
    private Tab suppMod;
    @FXML
    private JFXTextField searchID;
    @FXML
    private JFXButton searchbtn;
    @FXML
    private JFXTextField nomFld;
    @FXML
    private JFXTextField prenomFld;
    @FXML
    private JFXTextField bDateFld;
    @FXML
    private JFXTextField addrFld;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combo.setValue("nom_etudiant");
        combo.setItems(critereRecherche);
        //searchArea.setText("");
        initCol();
        loadStudents ();
    }  

    @FXML
    void addStudent(ActionEvent event) {
        String lName = nom.getText();
        String fName = prenom.getText();
        String bDate = birthdate.getText();
        String addr = address.getText();
        
        //Check if any of the fields is empty 
        if (lName.isEmpty() || fName.isEmpty() || bDate.isEmpty() || addr.isEmpty())
        {
            prompt("Vous devez remplir tous les champs");
            return;
        }
        String sql = "INSERT INTO Etudiant"
                + "(nom_etudiant, prenom_etudiant, date_naiss,adresse_etudiant) "
                + "VALUES(?,?,?,?)";
        if (DbUtil.execAction(sql, lName, fName, bDate, addr))
        {
            prompt("L'etudiant "+ lName +" a été ajouté avec succès");
        }
        nom.setText("");
        prenom.setText("");
        birthdate.setText("");
        address.setText("");
        loadStudents ();
    }
    
      
    
    private void prompt(String msg)
    {
        BoxBlur blur = new BoxBlur(3, 3, 2);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(msg));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {vBox.setEffect(null);});
        dialog.show();
        vBox.setEffect(blur);
    }

    @FXML
    private void comboSelect(ActionEvent event) {
        combo.setPromptText(combo.getValue().toString());
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id")); //Assiciate idCol with id property in Etudiant
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("bDate"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("addr"));
        
    }
    
    @FXML
    public void loadStudents ()
    {
        ResultSet rs = DbUtil.loadStudents(combo.getValue().toString(), searchArea.getText());
        listeEtudiant.clear();
        try {
            while (rs.next())
            {
                String idEtudiant = rs.getString("id_etudiant");
                String nomEtudiant = rs.getString("nom_etudiant");
                String prenomEtudiant = rs.getString("prenom_etudiant");
                String bDateEtudiant = rs.getString("date_naiss");
                String addrEtudiant = rs.getString("adresse_etudiant");
                
                listeEtudiant.add(new Etudiant(idEtudiant, nomEtudiant, prenomEtudiant, bDateEtudiant, addrEtudiant));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        studentTable.getItems().setAll(listeEtudiant);
    }

    @FXML
    private void searchStudent(ActionEvent event)  {
        String id = searchID.getText();
        if (id.isEmpty())
        {
            prompt("Vous devez insérez un ID");
            return;
        }
        try {
            ResultSet search = DbUtil.search("SELECT * FROM Etudiant WHERE id_etudiant=?", searchID.getText());
            if (!search.first())
            {
                prompt("ID non trouvé");
                return;
            }
            else //(search.next())
            {
                nomFld.setText(search.getString("nom_etudiant"));
                prenomFld.setText(search.getString("prenom_etudiant"));
                bDateFld.setText(search.getString("date_naiss"));
                addrFld.setText(search.getString("adresse_etudiant"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void modifyStudent(ActionEvent event) {
        String id = searchID.getText();
        String lName = nomFld.getText();
        String fName = prenomFld.getText();
        String bDate = bDateFld.getText();
        String addr = addrFld.getText();
        if (id.isEmpty())
        {
            prompt("Vous devez sélectionner un étudiant");
            return;
        }
        if (lName.isEmpty() || fName.isEmpty() || bDate.isEmpty() || addr.isEmpty())
        {
            prompt("Vous devez remplir tous les champs");
            return;
        }
        JFXButton yesBtn = new JFXButton("Oui");
        JFXButton noBtn = new JFXButton("Non");
     
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Etes-vous sûr de modifier les infos de "+lName));
        content.setActions(yesBtn,noBtn);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        
        yesBtn.setOnAction((ActionEvent e) -> {
            String sql = "UPDATE Etudiant SET "
                    + " nom_etudiant=?, "
                    + " prenom_etudiant=?, "
                    + " date_naiss=?, "
                    + " adresse_etudiant=? "
                    + " WHERE id_etudiant='"+searchID.getText()+"'";
            if (DbUtil.execAction(sql, lName, fName, bDate, addr))
                {
                    loadStudents ();
                    dialog.close();
                    prompt("Modification réussie ");
                }
        
        });
        noBtn.setOnAction((ActionEvent e) -> dialog.close());
        
        yesBtn.setStyle("-fx-background-color : #fff; -fx-cursor : HAND");
        noBtn.setStyle("-fx-background-color : #fff; -fx-cursor : HAND");
        dialog.show();
        
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        String id = searchID.getText();
        if (id.isEmpty())
        {
            prompt("Vous devez sélectionner un étudiant");
            return;
        }
        JFXButton yesBtn = new JFXButton("Oui");
        JFXButton noBtn = new JFXButton("Non");
     
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Etes-vous sûr de supprimer l'étudiant avec l'ID "+id));
        content.setActions(yesBtn,noBtn);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        
        yesBtn.setOnAction((ActionEvent e) -> {
            String sql = "DELETE FROM Etudiant WHERE id_etudiant='"+id+"'";
            if (DbUtil.delete(sql) > 0)
                {
                    loadStudents ();
                    dialog.close();
                    searchID.setText("");
                    nomFld.setText("");
                    prenomFld.setText("");
                    bDateFld.setText("");
                    addrFld.setText("");
                    prompt("Suppression réussie");
                }
        
        });
        noBtn.setOnAction((ActionEvent e) -> dialog.close());
        
        yesBtn.setStyle("-fx-background-color : #fff; -fx-cursor : HAND");
        noBtn.setStyle("-fx-background-color : #fff; -fx-cursor : HAND");
        dialog.show();
    }
 
    public class Etudiant 
    {
        private SimpleStringProperty nom;
        private SimpleStringProperty id;
        private SimpleStringProperty prenom;
        private SimpleStringProperty BDate;
        private SimpleStringProperty addr;

        public Etudiant(String id, String nom, String prenom, String bDate, String addr) 
        {
            this.id = new SimpleStringProperty(id);
            this.nom = new SimpleStringProperty(nom);
            this.prenom = new SimpleStringProperty(prenom);
            this.BDate = new SimpleStringProperty(bDate);
            this.addr = new SimpleStringProperty(addr);
        }

        public String getNom() {
            return nom.get();
        }

        public String getId() {
            return id.get();
        }

        public String getPrenom() {
            return prenom.get();
        }

        public String getBDate() {
            return BDate.get();
        }

        public String getAddr() {
            return addr.get();
        }
        
        
    }
}
