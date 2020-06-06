
package library.management.userToolbar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import library.management.utils.Utilities;

public class UserToolbarController implements Initializable {
    @FXML
    private VBox root;

    public UserToolbarController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void LoadMesLivers(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/MainUser/MesLivres/MesLivres.fxml"), "Mes Livres",null);
    }

    @FXML
    private void loadReserver(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/MainUser/Reservation/Reserve.fxml"), "Réservation",null);
    }

    @FXML
    private void LoadReglages(ActionEvent event) {
        Utilities.loadwindow(this.getClass().getResource("/library/management/MainUser/Settings/Settings.fxml"), "Réglages",null);
    }

    @FXML
    private void signOut(ActionEvent event) throws IOException {
        Stage stage = (Stage)this.root.getScene().getWindow();
        stage.close();
        Utilities.loadwindow(this.getClass().getResource("/library/management/Login/Login.fxml"), "ESTG Biblio Login",null);
    }
}
