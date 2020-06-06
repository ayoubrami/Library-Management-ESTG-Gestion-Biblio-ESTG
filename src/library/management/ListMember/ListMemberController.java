
package library.management.ListMember;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import library.management.database.DatabaseHandler;

public class ListMemberController implements Initializable {
    ObservableList<Etudiants> List = FXCollections.observableArrayList();
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Etudiants> tableview;
    @FXML
    private TableColumn<Etudiants, String> cincol;
    @FXML
    private TableColumn<Etudiants, String> fnamecol;
    @FXML
    private TableColumn<Etudiants, String> emailcol;
    @FXML
    private TableColumn<Etudiants, String> telecol;
    @FXML
    private JFXTextField search;

    public ListMemberController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
        this.setCol();
        this.loadData();
        this.searchOperation();
    }

    private void setCol() {
        this.cincol.setCellValueFactory(new PropertyValueFactory("cin"));
        this.fnamecol.setCellValueFactory(new PropertyValueFactory("fname"));
        this.emailcol.setCellValueFactory(new PropertyValueFactory("email"));
        this.telecol.setCellValueFactory(new PropertyValueFactory("tele"));
    }

    private void loadData() {
        DatabaseHandler database = DatabaseHandler.getInstance();
        String query = "SELECT CIN,FULLNAME,EMAIL,TELE FROM ETUDIANTS";
        ResultSet rs = database.execQuery(query);

        try {
            while(rs.next()) {
                String cin = rs.getString("cin");
                String fname = rs.getString("fullname");
                String email = rs.getString("email");
                String tele = rs.getString("tele");
                this.List.add(new Etudiants(cin, fname, email, tele));
            }
        } catch (SQLException var8) {
            System.err.println(var8.getMessage());
        }

        this.tableview.getItems().setAll(this.List);
    }

    private void searchOperation() {
        FilteredList<Etudiants> filteredData = new FilteredList(this.List, (p) -> {
            return true;
        });
        this.search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((etudiant) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (etudiant.getFname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (etudiant.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (etudiant.getCin().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else {
                        return etudiant.getTele().toLowerCase().contains(lowerCaseFilter);
                    }
                } else {
                    return true;
                }
            });
        });
        SortedList<Etudiants> sortedData = new SortedList(filteredData);
        sortedData.comparatorProperty().bind(this.tableview.comparatorProperty());
        this.tableview.setItems(sortedData);
    }
    public class Etudiants {
    private final SimpleStringProperty cin;
    private final SimpleStringProperty fname;
    private final SimpleStringProperty email;
    private final SimpleStringProperty tele;

    Etudiants(String cin, String fname, String email, String tele) {
        this.cin = new SimpleStringProperty(cin);
        this.fname = new SimpleStringProperty(fname);
        this.email = new SimpleStringProperty(email);
        this.tele = new SimpleStringProperty(tele);
    }

    public String getCin() {
        return this.cin.get();
    }

    public String getFname() {
        return this.fname.get();
    }

    public String getEmail() {
        return this.email.get();
    }

    public String getTele() {
        return this.tele.get();
    }
}

}
