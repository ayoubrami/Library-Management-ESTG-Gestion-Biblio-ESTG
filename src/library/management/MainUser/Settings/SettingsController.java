
package library.management.MainUser.Settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.management.AlertMaker.AlertMaker;
import library.management.Login.LoginController;
import library.management.Login.SignupController;
import library.management.database.DatabaseHandler;
import library.management.utils.Utilities;
import org.apache.commons.codec.digest.DigestUtils;

public class SettingsController implements Initializable {
    @FXML
    private JFXPasswordField oldpass;
    @FXML
    private JFXPasswordField newpass;
    @FXML
    private JFXPasswordField connewpass;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField cin;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;
    
    DatabaseHandler database=DatabaseHandler.getInstance();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void handleSavePass(ActionEvent event) {
        String oldpassword=oldpass.getText();
        String newpassword=newpass.getText();
        String conpassword=connewpass.getText();
        String hashedpass=DigestUtils.sha512Hex(oldpassword);
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton donebutton=new JFXButton("Terminé");
        JFXButton okbutton=new JFXButton("OK");
        if(!(oldpassword.isEmpty()||newpassword.isEmpty()||conpassword.isEmpty())){
        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton),"Confirmer ", "Voulez-vous vraiment changer votre mot de passe?");
        yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)  ->{
            String Query="SELECT PASSWORD FROM ETUDIANTS WHERE CIN='"+LoginController.cinetud+"'";
            ResultSet rs=database.execQuery(Query);
            String OldPass=null;
            try {
                while(rs.next()){
                     OldPass=rs.getString("PASSWORD");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(OldPass.equals(hashedpass) && newpassword.equals(conpassword)){
            String Query2="UPDATE ETUDIANTS SET PASSWORD='"+DigestUtils.sha512Hex(newpassword)+"' WHERE CIN='"+LoginController.cinetud+"'";
                if(database.execAction(Query2)){
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opertaion réssuie", "Votre mot de passe a été changé avec succès ");
                }else{
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Opertaion échoué ", "votre mot de passe n'a pas été changé ! "); 
                }
            }else{
                oldpass.getStyleClass().add("wrong-caredentails");
                newpass.getStyleClass().add("wrong-caredentails");
                connewpass.getStyleClass().add("wrong-caredentails");
            }
        });
        }else{
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Erreur !! ", "Veuillez entrer tous les champs ");    
        }   
    }

    @FXML
    private void handleSaveInfo(ActionEvent event) {
        String Email=email.getText();
        String CIN=cin.getText();
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton donebutton=new JFXButton("Terminé");
        JFXButton okbutton=new JFXButton("OK");
        if(!(Email.isEmpty()) && CIN.isEmpty()){
                 AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton),"Confirmer ", "Voulez-vous vraiment changer votre email ?");
                yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)  ->{
            if(SignupController.validationemail(Email)){
               String Query="UPDATE ETUDIANTS SET EMAIL='"+Email+"' WHERE CIN='"+LoginController.cinetud+"'";
               if(database.execAction(Query)){
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opertaion réssuie", "Votre email a été changé avec succès ");
                }else{
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Opertaion échoué ", "votre email n'a pas été changé ! "); 
                }
            }else{
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), " Format de courrier électronique incorrect !", "S'il vous plaît, mettez une adresse email valide");
                email.getStyleClass().add("wrong-caredentails");
            }
        });
        }else if(!(CIN.isEmpty()) && Email.isEmpty()){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton),"Confirmer ", "Voulez-vous vraiment changer votre CIN ?");
                yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)  ->{
               String Query="UPDATE ETUDIANTS SET CIN='"+CIN+"' WHERE CIN='"+LoginController.cinetud+"'";
               if(database.execAction(Query)){
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opertaion réssuie", "Votre CIN a été changé avec succès ");
                }else{
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Opertaion échoué ", "votre CIN n'a pas été changé ! "); 
                }
        });
        }else if (!(Email.isEmpty()||CIN.isEmpty())){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton),"Confirmer ", "Voulez-vous vraiment changer vos informations ?");
                yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)  ->{
            if(SignupController.validationemail(Email)){
               String Query="UPDATE ETUDIANTS SET EMAIL='"+Email+"' WHERE CIN='"+LoginController.cinetud+"'";
               String Query2="UPDATE ETUDIANTS SET CIN='"+CIN+"' WHERE CIN='"+LoginController.cinetud+"'";
               if(database.execAction(Query) && database.execAction(Query2)){
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opertaion réussie", "Vos informations ont été modifiées avec succès ");
                }else{
                    AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Opertaion échoué ", "Vos informations ont été pas changé ! "); 
                }
            }else{
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), " Format de courrier électronique incorrect !", "S'il vous plaît, mettez une adresse email valide");
                email.getStyleClass().add("wrong-caredentails");
            }
        });
        }else{
            email.getStyleClass().add("wrong-caredentails");
            cin.getStyleClass().add("wrong-caredentails");
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        JFXButton donebutton=new JFXButton("Bye Bye :( ");
        if(database.userdeletion(LoginController.cinetud)){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton),"Confirmer ", "Voulez-vous vraiment supprimer votre compte?\n" +
                                                                                                                  "Cette action entraînera la perte de toutes vos données !!");
                yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)  ->{
                    String Query=" DELETE FROM ETUDIANTS WHERE CIN='"+LoginController.cinetud+"'";
                    database.execAction(Query);
                     AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opertaion réussie", "Nous espérons vous revoir bientôt ");
                     donebutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventtt)  ->{
                         Stage stage=(Stage) rootAnchorPane.getScene().getWindow();
                         stage.close();
                         Utilities.loadwindow(getClass().getResource("/library/management/login/login.fxml"), "ESTG", null);
                     });
                });
        }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(okbutton), "Erreur !! ", "Nous pensons que vous n'avez pas encore retourné de livre!\n" +
                                                                                                    "Vous devez d'abord retourner le livre, puis vous pouvez supprimer votre compte. ");
        }
    }
    
}
