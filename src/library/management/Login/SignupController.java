
package library.management.Login;

import com.jfoenix.controls.JFXButton;
import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import library.management.AlertMaker.AlertMaker;
import library.management.database.DatabaseHandler;
import org.apache.commons.codec.digest.DigestUtils;

public class SignupController implements Initializable {
    @FXML
    private TextField fullname;
    @FXML
    private TextField cin;
    @FXML
    private TextField email;
    @FXML
    private TextField tele;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private StackPane rootStackPane;
    DatabaseHandler database = DatabaseHandler.getInstance();

    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void signup(ActionEvent event) {
        String fname = this.fullname.getText();
        String CIN = this.cin.getText();
        String emailadd = this.email.getText();
        String telep = this.tele.getText();
        String pass = DigestUtils.sha512Hex(this.password.getText());
        JFXButton button = new JFXButton("OK");
        if (!fname.isEmpty() && !CIN.isEmpty() && !emailadd.isEmpty() && !telep.isEmpty() && !pass.isEmpty()) {
            if (!validationemail(emailadd)) {
                AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(button), " Format de courrier électronique incorrect !", "S'il vous plaît, mettez une adresse email valide");
                this.email.getStyleClass().add("wrong-caredentails");
            } else {
                DatabaseHandler var10000 = this.database;
                if (DatabaseHandler.validemail(emailadd)) {
                    AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(button), " Erreur ", "Cet e-mail a déjà été enregistré !");
                } else {
                    var10000 = this.database;
                    if (DatabaseHandler.validcin(CIN)) {
                        AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(button), " Erreur ", "Cet CIN a déjà été enregistré !");
                    } else {
                        JFXButton accept = new JFXButton("J'accepte");
                        JFXButton decline = new JFXButton("Décliner");
                        AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(accept, decline), " Acceptez notre politique et confidentialité ", "empty empty empty");
                        accept.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
                            try {
                                PreparedStatement statement = null;
                                statement = DatabaseHandler.con.prepareStatement("INSERT INTO ETUDIANTS (CIN,FULLNAME,EMAIL,TELE,PASSWORD) VALUES (?,?,?,?,?)");
                                statement.setString(1, CIN);
                                statement.setString(2, fname);
                                statement.setString(3, emailadd);
                                statement.setString(4, telep);
                                statement.setString(5, pass);
                                statement.execute();
                            } catch (SQLException var9) {
                                JOptionPane.showMessageDialog((Component)null, "Error:" + var9.getMessage(), "Error Occured", 0);
                                System.out.println("Exception at execAction:dataHandler " + var9.getLocalizedMessage());
                            }

                            AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(button), " RÉUSSIE ", "Bienvenue à la bibliothèque du ESTG");
                        });
                        decline.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
                        });
                    }
                }
            }
        } else {
            AlertMaker.showMaterialDialog(this.rootStackPane, this.rootAnchorPane, Arrays.asList(button), " Erreur !", "Veuillez entrer tous les champs !!");
        }
    }

    @FXML
    private void login(MouseEvent event) {
        try {
            Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("Login.fxml"));
            Node node = (Node)event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException var5) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

    }

    public static boolean validationemail(String email) {
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(email);
        return m.find() && m.group().equals(email);
    }
}
