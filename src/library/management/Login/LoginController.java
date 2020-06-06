
package library.management.Login;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.management.database.DatabaseHandler;
import library.management.main.MainController;
import library.management.settings.Preferences;
import library.management.utils.Utilities;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label label;
    Preferences pref = Preferences.getPreferences();
    public static String labelnom;
    public static String cinetud;
    public static String emailetud;

    public LoginController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
        this.label.setVisible(false);
    }

    @FXML
    private void login(ActionEvent event) {
        String emailadd = this.email.getText();
        String pass = DigestUtils.sha512Hex(this.password.getText());
        DatabaseHandler database = DatabaseHandler.getInstance();
        String Query = "SELECT * FROM ETUDIANTS WHERE EMAIL='" + emailadd + "'AND PASSWORD='" + pass + "'";
        String Query1 = "SELECT * FROM ETUDIANTS WHERE EMAIL='" + emailadd + "'";
        ResultSet rs = database.execQuery(Query);
        ResultSet rl = database.execQuery(Query1);

        try {
            while(rl.next()) {
                labelnom = rl.getString("FULLNAME");
                cinetud = rl.getString("CIN");
                emailetud = rl.getString("EMAIL");
            }

            if (rs.next()) {
                this.closeStage();
                this.loadmainuser();
            } else if (emailadd.equals(this.pref.getUsername()) && pass.equals(this.pref.getPassword())) {
                this.closeStage();
                this.loadmainadmin();
            } else {
                this.email.getStyleClass().add("wrong-caredentails");
                this.password.getStyleClass().add("wrong-caredentails");
                this.label.setVisible(true);
                this.label.setText("EMAIL OU MOT DE PASSE INVALIDE !! ");
            }
        } catch (SQLException var10) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, (String)null, var10);
        }

    }

    @FXML
    private void signup(MouseEvent event) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("Signup.fxml"));
            Node node = (Node)event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException var5) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

    }

    private void closeStage() {
        ((Stage)this.email.getScene().getWindow()).close();
    }

    public void loadmainadmin() {
        try {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("/library/management/main/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Gestion de Biblio ESTG");
            stage.setScene(new Scene(parent));
            stage.show();
            Utilities.setIcon(stage);
        } catch (IOException var3) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, (String)null, var3);
        }

    }

    public void loadmainuser() {
        try {
            Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("/library/management/MainUser/MainUser.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Gestion de Biblio ESTG");
            stage.setScene(new Scene(parent));
            stage.show();
            Utilities.setIcon(stage);
        } catch (IOException var3) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, (String)null, var3);
        }

    }
}
