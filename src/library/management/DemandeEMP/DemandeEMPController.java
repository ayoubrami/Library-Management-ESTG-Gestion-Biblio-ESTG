
package library.management.DemandeEMP;

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
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import library.management.AlertMaker.AlertMaker;
import library.management.database.DatabaseHandler;
import static library.management.database.DatabaseHandler.con;


public class DemandeEMPController implements Initializable {
    ObservableList<Demande> List=FXCollections.observableArrayList();
    DatabaseHandler database =DatabaseHandler.getInstance();
    @FXML
    private TableView<Demande> tableview;
    @FXML
    private TableColumn<Demande,String> livreid;
    @FXML
    private TableColumn<Demande,String> titre;
    @FXML
    private TableColumn<Demande,String> cin;
    @FXML
    private TableColumn<Demande, String> nom;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private JFXTextField search;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setCol();
       loaddata();
       searchOperation();
    }    
     public void setCol(){
        livreid.setCellValueFactory(new PropertyValueFactory<>("id"));
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
     } 
     
     private void loaddata() {
          DatabaseHandler database=DatabaseHandler.getInstance();
        String query="SELECT DEMANDE.L_ID,DEMANDE.CIN,\n" +
                     "LIVRE.TITRE,ETUDIANTS.FULLNAME\n" +
                     "FROM DEMANDE \n" +
                     "LEFT JOIN LIVRE\n" +
                     "ON DEMANDE.L_ID=LIVRE.ID\n" +
                     "LEFT JOIN ETUDIANTS\n" +
                     "ON DEMANDE.CIN=ETUDIANTS.CIN";
        ResultSet rs=database.execQuery(query);
        try{
            while(rs.next()){
                String titre =rs.getString("TITRE");
                String id =rs.getString("L_ID");
                String cin =rs.getString("CIN");
                String nom =rs.getString("FULLNAME");
                
                
              List.add(new Demande(id,cin,titre,nom));
              
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        } 
     tableview.getItems().setAll(List);
    }
    @FXML
    private void emprunter(ActionEvent event) {
    Demande dem=(Demande)tableview.getSelectionModel().getSelectedItem(); 
    JFXButton yesbutton=new JFXButton("Oui");
    JFXButton nobutton=new JFXButton("Non");
    JFXButton okbutton=new JFXButton("OK");
    JFXButton button = new JFXButton("Terminé");
    JFXButton button1 = new JFXButton("D'accord, je vais vérifier");
     int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            if(database.validtoissue(dem.getCin())){
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton, nobutton), "Confirmer l'emprunt",
                "êtes-vous sûr de vouloir emprunter ce livre \n"+dem.getTitre()+" à cet étudiant "+dem.getNom());
                yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                    String Query ="INSERT INTO EMPRUNTER (L_ID,CIN) VALUES ('"+dem.getId()+"','"+dem.getCin()+"')";
                    String Query2="UPDATE LIVRE SET NB_EXMP=NB_EXMP -1 WHERE ID ='"+dem.getId()+"'";
                    String Query3="DELETE FROM DEMANDE WHERE L_ID='"+dem.getId()+"'";
                    if(database.execAction(Query)&&database.execAction(Query2)&&database.execAction(Query3)){
                        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button), "Opération réussie", null);
                        database.Availiablity();
                        List.remove(selectedIndex);
                        database.SendReservationMail();
                    }else{
                        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(button1), "Echec de l'opération", null);
                    }
                });
                nobutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event2) ->{
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(nobutton), "annulé", null);
                });
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Erreur !", "Cet étudiant a déjà emprunté un livre.");
            }
        }else{ 
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Erreur !", "Vous n'avez pas sélectionné d'élément à emprunter, Veuillez réessayer.");
        }
    }

    @FXML
    private void annulerdemande(ActionEvent event) {
        Demande dem=(Demande)tableview.getSelectionModel().getSelectedItem(); 
        int selectedIndex=tableview.getSelectionModel().getSelectedIndex();
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton= new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        if(selectedIndex >= 0){ 
           AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton, nobutton), "Confirmer",
                "êtes-vous sûr de vouloir annuler cette demande ");
            yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
        try {
            String Query ="DELETE FROM DEMANDE WHERE L_ID='"+dem.getId()+"'";
            PreparedStatement pst=con.prepareStatement(Query);
            pst.executeUpdate();
            List.remove(selectedIndex);
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane,Arrays.asList(okbutton),"Réussie", " Opération réussie ");
        } catch (SQLException ex) {
            Logger.getLogger(DemandeEMPController.class.getName()).log(Level.SEVERE, null, ex);
        }     
          });
            nobutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(nobutton), "annulé", "La demande a été annulée");            });
        }else{
        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane,Arrays.asList(okbutton),"Erreur !", "Vous n'avez pas sélectionné d'élément, Veuillez réessayer. ");
        }
    }

   private void searchOperation() {
        FilteredList<Demande> filteredData = new FilteredList<>(List, p -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(demande -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (demande.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } else if (demande.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }else if (demande.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false; 
            });
        });
        SortedList<Demande> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);
    }
    
    public static class Demande{
   private final SimpleStringProperty id;
   private final SimpleStringProperty cin;
   private final SimpleStringProperty titre;
   private final SimpleStringProperty nom;
    public Demande(String id,String cin,String titre,String nom){
        this.id=new SimpleStringProperty(id);
        this.cin=new SimpleStringProperty(cin);
        this.nom=new SimpleStringProperty(nom);
        this.titre=new SimpleStringProperty(titre);
    }

        public String getId() {
            return id.get();
        }

        public String getCin() {
            return cin.get();
        }

        public String getTitre() {
            return titre.get();
        }

        public String getNom() {
            return nom.get();
        }
    
}
    
}
