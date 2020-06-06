
package library.management.TestMail;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import library.management.AlertMaker.AlertMaker;
import library.management.Login.SignupController;
import library.management.callback.callback;
import library.management.mail.MailServerInfo.MailServerInfo;
import library.management.mail.MailService;

public class TestMailController implements Initializable,callback {
    @FXML
    private JFXTextField recemail;
    private MailServerInfo mailserverinfo;
    @FXML
    private JFXProgressBar progressbar;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
             progressbar.setVisible(false);
        
    }    
     public void setMailServerInfo(MailServerInfo mailserverinfo) {
        this.mailserverinfo = mailserverinfo;
    }
    @FXML
    private void SendTest(ActionEvent event) {
        String address=recemail.getText();
        if(SignupController.validationemail(address)){
             MailService.sendTestMail(mailserverinfo, address, this);
             progressbar.setVisible(true);
        }else{
            AlertMaker.showErrorMessage("Failed", "Invalid email address!");
        }
        
    }

    @FXML
    private void cancel(ActionEvent event) {
    }

    @Override
    public Object taskcomplete(Object var) {
        boolean res =(boolean) var;
        Platform.runLater(() -> {
            if (res) {
                AlertMaker.showSimpleAlert("Success", "Email successfully sent!");
            } else {
                AlertMaker.showErrorMessage("Failed", "Something went wrong!");
            }
            progressbar.setVisible(false);
        });
        return true;
    }
    
}
