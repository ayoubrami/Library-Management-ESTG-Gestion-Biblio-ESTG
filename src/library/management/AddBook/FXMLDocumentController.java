
package library.management.AddBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.Component;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import library.management.AlertMaker.AlertMaker;
import library.management.database.DatabaseHandler;

public class FXMLDocumentController implements Initializable {
    @FXML
    private JFXTextField titre;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField auteur;
    @FXML
    private JFXTextField editeur;
    @FXML
    private JFXTextField nb_pages;
    @FXML
    private JFXTextField description;
    @FXML
    private JFXTextField nb_exemp;
    @FXML
    private JFXTextField place;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;

    DatabaseHandler databasehandler;
    
    public void initialize(URL url, ResourceBundle rb) {
        this.databasehandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void Sauvegarder(ActionEvent event) throws SQLException {
        String titre_livre = this.titre.getText();
        String id_livre = this.id.getText();
        String auteur_livre = this.auteur.getText();
        String editeur_livre = this.editeur.getText();
        String nb_pages_livre = this.nb_pages.getText();
        String description_livre = this.description.getText();
        String nb_exemp_livre = this.nb_exemp.getText();
        String place_livre = this.place.getText();
        JFXButton button=new JFXButton("OK");
       
        if (titre_livre.isEmpty()||id_livre.isEmpty()||auteur_livre.isEmpty()||editeur_livre.isEmpty()||description_livre.isEmpty()||place_livre.isEmpty()|| nb_pages_livre.isEmpty()|| nb_exemp_livre.isEmpty()){
            AlertMaker.showMaterialDialog(rootStackPane,rootAnchorPane, Arrays.asList(button), " Erreur !", "Veuillez entrer tous les champs !!");
           return; 
        }
        else if(validnum(nb_pages_livre) && validnum(nb_exemp_livre)){
            try {
                PreparedStatement statement = DatabaseHandler.con.prepareStatement("INSERT INTO LIVRE (id,titre,auteur,editeur,nb_pages,description,nb_exmp,place) VALUES (?,?,?,?,?,?,?,?)");
                statement.setString(1, id_livre);
                statement.setString(2, titre_livre);
                statement.setString(3, auteur_livre);
                statement.setString(4, editeur_livre);
                statement.setInt(5, Integer.parseInt(this.nb_pages.getText()));
                statement.setString(6, description_livre);
                statement.setInt(7, Integer.parseInt(this.nb_exemp.getText()));
                statement.setString(8, place_livre);
                statement.execute();
                databasehandler.Availiablity();
            } catch (SQLException var13) {
                JOptionPane.showMessageDialog((Component)null, "Error:" + var13.getMessage(), "Error Occured", 0);
            }
            AlertMaker.showMaterialDialog(rootStackPane,rootAnchorPane, Arrays.asList(button), " RÉUSSIE ", "Le livre à été bien ajouter.");
        }else {
             this.nb_pages.getStyleClass().add("wrong-caredentails");
             this.nb_exemp.getStyleClass().add("wrong-caredentails");
            AlertMaker.showMaterialDialog(rootStackPane,rootAnchorPane, Arrays.asList(button), " Erreur !", "Quelque chose a mal tourné !!");
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage)rootAnchorPane.getScene().getWindow();
        stage.close();
    }
    public static boolean validnum(String num) {
        Pattern p = Pattern.compile("\\d{0,7}([\\.]\\d{0,4})?");
        Matcher m = p.matcher(num);
        return m.find() && m.group().equals(num);
    }
    
}
