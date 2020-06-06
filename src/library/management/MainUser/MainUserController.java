
package library.management.MainUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import library.management.AlertMaker.AlertMaker;
import library.management.ListBook.List_BookController.Book;
import library.management.Login.LoginController;
import library.management.database.DatabaseHandler;
import library.management.main.MainController;
import library.management.utils.Utilities;

public class MainUserController implements Initializable {
    @FXML
    private Label nometud;
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
    private TableColumn<Book,String> descol;
    @FXML
    private TableColumn<Book,String> dispcol;
    @FXML
    private JFXDrawer userdrawer;
    @FXML
    private JFXHamburger userhamburger;
     
    DatabaseHandler database=DatabaseHandler.getInstance();
    ObservableList<Book> List=FXCollections.observableArrayList();
    String cin=LoginController.cinetud;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private BorderPane rootBorderPane;
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      setCol();
      loadData();
      searchOperation();
      initDrawer();
      nometud.setText(LoginController.labelnom);
    }    

    @FXML
    private void DemandeOperation(ActionEvent event) {
        Book book=(Book)tableview.getSelectionModel().getSelectedItem();
        int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
         JFXButton button=new JFXButton("OK");
        if(selectedIndex >= 0){
        if(database.validtodemande1(book)){
        if(database.validtodemande2(cin)){
            if(database.validtodemande3(book)){
            String qu="INSERT INTO DEMANDE (L_ID,CIN) VALUES ('"+book.getId()+"','"+cin+"') ";
            database.execAction(qu);
            AlertMaker.showMaterialDialog(rootStackPane, rootBorderPane, Arrays.asList(button), " Réussie ", " Opération réussie ");
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootBorderPane, Arrays.asList(button), " Erreur ", "Vous avez déjà emprunter cette livre !! ");
            }
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootBorderPane, Arrays.asList(button), " Vous avez atteint le maximum de demandes ", "Vous avez déjà demandé un livre !! ");
            }
          }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootBorderPane, Arrays.asList(button), " Livre est Indisponible ", "Vous pouvez le réserver dans la section de réservation.");
            }
        
          }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootBorderPane, Arrays.asList(button), "Aucune sélection n'a été effectuée.", "Vous n'avez pas sélectionné d'élément à demander, Veuillez réessayer.");
            }
    }
    private void setCol() {
        titrecol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurcol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        editeurcol.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        nb_pagescol.setCellValueFactory(new PropertyValueFactory<>("nb_pages"));
        descol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        dispcol.setCellValueFactory(new PropertyValueFactory<>("disp"));
    }

    private void loadData() {
        String query="SELECT * FROM LIVRE";
        ResultSet rs=database.execQuery(query);
        try{
            while(rs.next()){
                String titre =rs.getString("titre");
                String id =rs.getString("id");
                String auteur =rs.getString("auteur");
                String editeur =rs.getString("editeur");
                String nb_pages =rs.getString("nb_pages");
                String desc =rs.getString("description");
                boolean disp =rs.getBoolean("isavail");
                String place=rs.getString("place");
                
              List.add(new Book(id,titre,auteur,editeur,nb_pages,desc,disp,place));
              
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } 
     tableview.getItems().setAll(List);
    } 
    private void searchOperation() {
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
     @FXML
    private void meslivres(ActionEvent event) {
         Utilities.loadwindow(getClass().getResource("/library/management/MainUser/MesLivres/MesLivres.fxml"), "Mes Livres", null);
    }

    @FXML
    private void indisplivres(ActionEvent event) {
         Utilities.loadwindow(getClass().getResource("/library/management/MainUser/Reservation/Reserve.fxml"), "Reservation", null);
    }

    @FXML
    private void reglages(ActionEvent event) {
         Utilities.loadwindow(getClass().getResource("/library/management/MainUser/Settings/Settings.fxml"), "Réglages", null);
    }

    @FXML
    private void fullscreen(ActionEvent event) {
        Stage stage=((Stage)rootStackPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }
    @FXML
    private void fermer(ActionEvent event) {
        ((Stage)rootStackPane.getScene().getWindow()).close();
    }
    private void initDrawer() {
        try {
            VBox toolbar=FXMLLoader.load(getClass().getResource("/library/management/userToolbar/userToolbar.fxml"));
            userdrawer.setSidePane(toolbar);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task= new HamburgerSlideCloseTransition(userhamburger);
        task.setRate(-1);
        userhamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) ->{

          
                task.setRate(task.getRate() * -1);
                task.play();
                if(userdrawer.isClosed()){
                    userdrawer.open();
                }
                else{
                    userdrawer.close();
                }
            
            
            }
        );
    }


}
