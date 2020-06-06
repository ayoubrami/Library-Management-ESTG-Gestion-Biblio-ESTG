package library.management.ListIssuedBooks;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.management.database.DatabaseHandler;

public class ListIssuedBooksController implements Initializable {
    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Livre> tableview;
    @FXML
    private TableColumn<Livre, String> idcol;
    @FXML
    private TableColumn<Livre, String> titrecol;
    @FXML
    private TableColumn<Livre, String> nomcol;
    @FXML
    private TableColumn<Livre, String> cincol;
    @FXML
    private TableColumn<Livre, String> datecol;
    ObservableList<Livre> List = FXCollections.observableArrayList();
    DatabaseHandler database = DatabaseHandler.getInstance();


    public void initialize(URL url, ResourceBundle rb) {
        this.setCol();
        this.loadData();
        this.searchOperation();
    }

    private void setCol() {
        this.idcol.setCellValueFactory(new PropertyValueFactory("id"));
        this.titrecol.setCellValueFactory(new PropertyValueFactory("titre"));
        this.nomcol.setCellValueFactory(new PropertyValueFactory("nom"));
        this.cincol.setCellValueFactory(new PropertyValueFactory("cin"));
        this.datecol.setCellValueFactory(new PropertyValueFactory("date"));
    }

    private void loadData() {
        String Query = "SELECT EMPRUNTER.L_ID,EMPRUNTER.CIN,EMPRUNTER.DATE_EM, \nLIVRE.TITRE,ETUDIANTS.FULLNAME \nFROM EMPRUNTER \nLEFT JOIN LIVRE \nON EMPRUNTER.L_ID=LIVRE.ID \nLEFT JOIN ETUDIANTS \nON EMPRUNTER.CIN=ETUDIANTS.CIN";
        ResultSet rs = this.database.execQuery(Query);

        try {
            while(rs.next()) {
                String id = rs.getString("L_ID");
                String titre = rs.getString("TITRE");
                String nom = rs.getString("FULLNAME");
                String cin = rs.getString("CIN");
                Timestamp issuetime = rs.getTimestamp("DATE_EM");
                Date date = new Date(issuetime.getTime());
                this.List.add(new library.management.ListIssuedBooks.ListIssuedBooksController.Livre(id, titre, nom, cin, date.toString()));
            }
        } catch (SQLException var9) {
            Logger.getLogger(ListIssuedBooksController.class.getName()).log(Level.SEVERE, (String)null, var9);
        }

        this.tableview.getItems().setAll(this.List);
    }

    private void searchOperation() {
        FilteredList<Livre> filteredData = new FilteredList(this.List, (p) -> {
            return true;
        });
        this.search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((livre) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (livre.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (livre.getNom().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (livre.getId().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        return livre.getCin().toLowerCase().contains(lowerCaseFilter);
                    }
                } else {
                    return true;
                }
            });
        });
        SortedList<Livre> sortedData = new SortedList(filteredData);
        sortedData.comparatorProperty().bind(this.tableview.comparatorProperty());
        this.tableview.setItems(sortedData);
    }
    public class Livre {
    private final SimpleStringProperty id;
    private final SimpleStringProperty titre;
    private final SimpleStringProperty nom;
    private final SimpleStringProperty cin;
    private final SimpleStringProperty date;

    Livre(String id, String titre, String nom, String cin, String date) {
        this.id = new SimpleStringProperty(id);
        this.titre = new SimpleStringProperty(titre);
        this.nom = new SimpleStringProperty(nom);
        this.cin = new SimpleStringProperty(cin);
        this.date = new SimpleStringProperty(date);
    }

    public String getId() {
        return this.id.get();
    }

    public String getTitre() {
        return this.titre.get();
    }

    public String getNom() {
        return this.nom.get();
    }

    public String getCin() {
        return this.cin.get();
    }

    public String getDate() {
        return this.date.get();
    }
}

}
