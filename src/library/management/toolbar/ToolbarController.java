
package library.management.toolbar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import library.management.utils.Utilities;

public class ToolbarController implements Initializable {
    @FXML
    private VBox root;

    public ToolbarController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void loadAjouterLivre(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/AddBook/FXMLDocument.fxml"), "Ajouter nouveaux Livre",null);
    }

    @FXML
    private void LoadDemandes(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/DemandeEMP/DemandeEMP.fxml"), "Les Demandes d'emprunt",null);
    }

    @FXML
    private void loadListBook(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/ListBook/List_Book.fxml"), "Les Livres",null);
    }

    @FXML
    private void Loadlistmembers(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/ListMember/ListMember.fxml"), "Les Utilisateurs",null);
    }

    @FXML
    private void LoadSettings(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/settings/Settings.fxml"), "Réglage",null);
    }

    @FXML
    private void loadIssuedook(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/ListIssuedBooks/ListIssuedBooks.fxml"), "Les livre emprunter",null);
    }

    @FXML
    private void LoadSignOut(ActionEvent event) throws IOException {
        Stage stage = (Stage)this.root.getScene().getWindow();
        stage.close();
        Utilities.loadwindow(this.getClass().getResource("/library/management/Login/Login.fxml"), "ESTG Biblio Login", null);
    }
}
