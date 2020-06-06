
package library.management.ListBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.management.AlertMaker.AlertMaker;
import library.management.database.DatabaseHandler;





public class List_BookController implements Initializable {
    ObservableList<Book> List=FXCollections.observableArrayList();
    DatabaseHandler database=DatabaseHandler.getInstance();
    @FXML
    private  TableView<Book> tableview;
    @FXML
    private TableColumn<Book, String> idcol;
    @FXML
    private TableColumn<Book, String> titrecol;
    @FXML
    private TableColumn<Book, String> auteurcol;
    @FXML
    private TableColumn<Book, String> editeurcol;
    @FXML
    private TableColumn<Book, String> nb_pagescol;
    @FXML
    private TableColumn<Book, String> descol;
    @FXML
    private TableColumn<Book, Boolean> discol;
    @FXML
    private TableColumn<Book, String> emplacementcol;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private JFXTextField search;
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCol();
        loadData();
        searchOperation();
    } 
    private void setCol() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titrecol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurcol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        editeurcol.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        nb_pagescol.setCellValueFactory(new PropertyValueFactory<>("nb_pages"));
        descol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        discol.setCellValueFactory(new PropertyValueFactory<>("disp"));
        emplacementcol.setCellValueFactory(new PropertyValueFactory<>("place"));
    }
    private void loadData() {
        DatabaseHandler database=DatabaseHandler.getInstance();
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
                String emplacement=rs.getString("place");
              List.add(new Book(id,titre,auteur,editeur,nb_pages,desc,disp,emplacement));
              
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } 
     tableview.getItems().setAll(List);
    }

    @FXML
    private void modifierlivre(ActionEvent event) throws IOException {
        int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifierLivre.fxml"));
            Parent tableviewparent =loader.load();
            Scene tableviewscene=new Scene(tableviewparent);
            ModifierLivreController controller=loader.getController();
            controller.setData(tableview.getSelectionModel().getSelectedItem());
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(tableviewscene);
            window.show();
        }else{
            JFXButton button=new JFXButton("OK");
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), " Erreur !! ", "Vous n'avez pas sélectionné d'élément à modifier, veuillez réessayer. ");
        }
            
    }

    @FXML
    private void Supprimer(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton= new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton, nobutton), "Confirmer",
                "êtes-vous sûr de vouloir supprimer cette livre ");
            yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
                 Book book=(Book)tableview.getSelectionModel().getSelectedItem();
                 String Query="DELETE FROM LIVRE WHERE ID = '"+book.getId()+"'";
                 if(database.execAction(Query)){
                 //tableview.getItems().remove(selectedIndex);
                 List.remove(selectedIndex);
                 AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane,Arrays.asList(yesbutton),"Opération réussie", "Le Livre à été bien supprimée ");
                 }else{
                      AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Echec de l'opération", null);
                 }
        });
        }else{
        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), " Erreur !! ", "Vous n'avez pas sélectionné d'élément à supprimer, veuillez réessayer. ");
        }
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
                }else if (book.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getEditeur().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }else if (book.getPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false; 
            });
        });
        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);
    }
    public static class Book{
        private final SimpleStringProperty id;
        private final SimpleStringProperty titre;
        private final SimpleStringProperty auteur;
        private final SimpleStringProperty editeur;
        private final SimpleStringProperty nb_pages;
        private final SimpleStringProperty desc;
        private final SimpleStringProperty disp;
        private final SimpleStringProperty place;
        
        public Book(String id,String titre,String auteur,String editeur,String nb_pages,String desc,boolean disp,String place){
            this.id=new SimpleStringProperty(id);
            this.titre=new SimpleStringProperty(titre);
            this.auteur=new SimpleStringProperty(auteur);
            this.editeur=new SimpleStringProperty(editeur);
            this.nb_pages=new SimpleStringProperty(nb_pages);
            this.desc=new SimpleStringProperty(desc);
            if (disp) {
                this.disp = new SimpleStringProperty("Disponible");
            } else {
                this.disp = new SimpleStringProperty("Indisponible");
            }
            this.place=new SimpleStringProperty(place);
        }

        public String getId() {
            return id.get();
        }

        public String getTitre() {
            return titre.get();
        }

        public String getAuteur() {
            return auteur.get();
        }

        public String getEditeur() {
            return editeur.get();
        }

        public String getNb_pages() {
            return nb_pages.get();
        }

        public String getDesc() {
            return desc.get();
        }

        public String getDisp() {
            return disp.get();
        }

        public String getPlace() {
            return place.get();
        }
        
    }
    
    
}
