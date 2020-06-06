
package library.management.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.management.AlertMaker.AlertMaker;
import library.management.Login.SignupController;
import library.management.TestMail.TestMailController;
import library.management.database.DatabaseHandler;
import library.management.mail.MailServerInfo.MailServerInfo;
import library.management.utils.Utilities;

public class SettingsController implements Initializable {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField servername;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField pass;
    
    DatabaseHandler database =DatabaseHandler.getInstance();
    @FXML
    private StackPane rootStackPane;
    @FXML
    private BorderPane rootBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
   setDefaultValues();
    }    

    @FXML
    private void save(ActionEvent event) {
        Preferences pref=Preferences.getPreferences();
        pref.setUsername(username.getText());
        pref.setPassword(password.getText());
        Preferences.writepreftofile(pref);
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage=(Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void setDefaultValues() {
        Preferences pref=Preferences.getPreferences();
        username.setText(String.valueOf(pref.getUsername()));
        password.setText(String.valueOf(pref.getPassword()));
        loadserverinfo();
    }
    private MailServerInfo mailserver(){
        try{
        MailServerInfo mailserverinfo=new MailServerInfo(servername.getText(),Integer.parseInt(port.getText()),email.getText(),pass.getText());    
        if(!mailserverinfo.validate() || !SignupController.validationemail(email.getText())){
            throw new InvalidParameterException();
        }
        return mailserverinfo;
        }catch(Exception e){
            AlertMaker.showErrorMessage("Invalid Entries Found", "Correct input and try again");
        }
        return null;
    }

    @FXML
    private void sendTest(ActionEvent event) {
        MailServerInfo mailserverinfo=mailserver();
        if(mailserverinfo!=null){
            TestMailController controller = (TestMailController) Utilities.loadwindow(getClass().getResource("/library/management/TestMail/TestMail.fxml"), "Test Email", null);
            controller.setMailServerInfo(mailserverinfo);
        }
    }

    @FXML
    private void savemailconfig(ActionEvent event) {
        MailServerInfo mailserverinfo=mailserver(); 
        if(mailserverinfo !=null ){
            if (database.updateMailServerInfo(mailserverinfo)) {
                AlertMaker.showSimpleAlert("Success", "Saved successfully!");
            } else {
                AlertMaker.showErrorMessage("Failed", "Something went wrong!");
            }
        }
    }
    private void loadserverinfo(){
        MailServerInfo mailServerInfo=database.loadMailServerInfo();
        if(mailServerInfo !=null){
            servername.setText(mailServerInfo.getStmpservername());
            port.setText(String.valueOf(mailServerInfo.getPort()));
            email.setText(mailServerInfo.getEmail());
            pass.setText(mailServerInfo.getPass());
        }
    }
}
