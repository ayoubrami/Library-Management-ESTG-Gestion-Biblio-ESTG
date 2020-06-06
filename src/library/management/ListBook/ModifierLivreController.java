
package library.management.ListBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static library.management.AddBook.FXMLDocumentController.validnum;
import library.management.AlertMaker.AlertMaker;
import library.management.ListBook.List_BookController.Book;
import library.management.database.DatabaseHandler;
import static library.management.database.DatabaseHandler.con;
import library.management.utils.Utilities;

public class ModifierLivreController implements Initializable {

    private AnchorPane rootPane;
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
    
    DatabaseHandler database;
    PreparedStatement statement;
    private Book selectedBook;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       database=DatabaseHandler.getInstance();
    }    

    @FXML
    void Sauvegarder(ActionEvent event) {
        String titre_livre=titre.getText();
        String id_livre=id.getText();
        String auteur_livre=auteur.getText();
        String editeur_livre=editeur.getText();
        String nb_pages_livre=nb_pages.getText();
        String description_livre=description.getText();
        String nb_exemp_livre=nb_exemp.getText();
        String place_livre=place.getText();
        JFXButton button= new JFXButton("OK");
        
        if(titre_livre.isEmpty()||id_livre.isEmpty()||auteur_livre.isEmpty()||editeur_livre.isEmpty()||nb_pages_livre.isEmpty()||description_livre.isEmpty()||nb_exemp_livre.isEmpty()||place_livre.isEmpty()){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Erreur !", "Veuillez entrer tous les champs !!");
            return;
        }else if(validnum(nb_pages_livre) && validnum(nb_exemp_livre)){
                try{
                statement = con.prepareStatement("update LIVRE set id=?, titre=?, auteur=?, editeur=?, nb_pages=?, description=?, nb_exmp=?, place=? where ID='"+id.getText()+"' ");
                statement.setString(1, id_livre);
                statement.setString(2, titre_livre);
                statement.setString(3, auteur_livre);
                statement.setString(4, editeur_livre);
                statement.setInt(5, Integer.parseInt(nb_pages.getText()));
                statement.setString(6, description_livre);
                statement.setInt(7,Integer.parseInt(nb_exemp.getText()));
                statement.setString(8, place_livre);
                statement.execute();
                }catch(SQLException ex){
                JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
                } 
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " RÉUSSIE ", "Le livre à été bien ajouter.");
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) ->{
                Stage stage=(Stage) rootAnchorPane.getScene().getWindow();
                stage.close();                 
                database.Availiablity();
                database.SendReservationMail();
                Utilities.loadwindow(getClass().getResource("/library/management/ListBook/List_Book.fxml"), "Les Livres", null);

              });
        }else{
            this.nb_pages.getStyleClass().add("wrong-caredentails");
             this.nb_exemp.getStyleClass().add("wrong-caredentails");
            AlertMaker.showMaterialDialog(rootStackPane,rootAnchorPane, Arrays.asList(button), " Erreur !", "Quelque chose a mal tourné !!");
        }
    }

    @FXML
    void annuler(ActionEvent event) {
        Stage stage=(Stage) rootAnchorPane.getScene().getWindow();
        stage.close();
    }
    public void setData(Book book) {
         try {
            selectedBook=book;
            String query = "select * from Livre where id = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, selectedBook.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                titre.setText(rs.getString("titre"));
                id.setText(rs.getString("id"));
                auteur.setText(rs.getString("titre"));
                editeur.setText(rs.getString("editeur"));
                nb_pages.setText(String.valueOf(rs.getInt("nb_pages")));
                description.setText(rs.getString("description"));
                nb_exemp.setText(String.valueOf(rs.getInt("nb_exmp")));
                place.setText(rs.getString("place"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(List_BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
            }

}

 

    
    

