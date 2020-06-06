
package library.management.MainUser.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import library.management.AlertMaker.AlertMaker;
import library.management.ListBook.List_BookController.Book;
import library.management.Login.LoginController;
import library.management.database.DatabaseHandler;

public class ReserveController implements Initializable {
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Book> tableview;
    @FXML
    private TableColumn<Book,String> titrecol;
    @FXML
    private TableColumn<Book,String> auteurcol;
    @FXML
    private TableColumn<Book,String> editeurcol;
    @FXML
    private TableColumn<Book,String> nb_pagescol;
    @FXML
    private TableColumn<Book,String> desccol;

    DatabaseHandler database=DatabaseHandler.getInstance();
    ObservableList<Book> List=FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCol();
        loadData();
        ssearchOpertaion();
      //  database.Availiablity();
    }    

    @FXML
    private void handleReservation(ActionEvent event) {
        Book book=(Book)tableview.getSelectionModel().getSelectedItem();
        int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
        JFXButton button=new JFXButton("ok");
    if(selectedIndex>=0){
        if(database.validetoreserve(LoginController.cinetud,book.getId())){
            if(database.validetoreserve2(LoginController.cinetud,book.getId())){
                String qu="INSERT INTO RESERVATION (L_ID,CIN,EMAIL) VALUES ('"+book.getId()+"','"+LoginController.cinetud+"','"+LoginController.emailetud+"') ";
                    if(database.execAction(qu)){
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Réussie ", " Opération réussie ");
                    }else{
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Erreur !!", " Opération échoué ");
                    }
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Erreur !!", " Vous avez déja reserver cette livre ");
            }
        }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Erreur !!", " Vous avez déja emprunter cette livre ");
        }
    }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), "Aucune sélection n'a été effectuée.", "Vous n'avez pas sélectionné d'élément à reserver, Veuillez réessayer.");
    }
    }

    private void setCol() {
        titrecol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurcol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        editeurcol.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        nb_pagescol.setCellValueFactory(new PropertyValueFactory<>("nb_pages"));
        desccol.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }

    private void loadData() {
        String Query="SELECT * FROM LIVRE WHERE ISAVAIL='"+false+"'";
        ResultSet rs=database.execQuery(Query);
        try {
            while(rs.next()){
                String id =rs.getString("id");
                String titre =rs.getString("titre");
                String auteur =rs.getString("auteur");
                String editeur =rs.getString("editeur");
                String nb_pages =rs.getString("nb_pages");
                String desc =rs.getString("description");
                boolean disp =rs.getBoolean("isavail");
                String place=rs.getString("place");
                List.add(new Book(id,titre,auteur,editeur,nb_pages,desc,disp,place));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReserveController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableview.getItems().setAll(List);
    }

    private void ssearchOpertaion() {
         FilteredList<Book> filteredData = new FilteredList<>(List, p -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (book.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } else if (book.getAuteur().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }else if (book.getEditeur().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false; 
            });
        });
        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);
    }
   
}
