
package library.management.splash;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SplashController implements Initializable {
    @FXML
    private StackPane rootStackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();
    }
    class SplashScreen extends Thread {
        @Override
        public void run(){
            try {
                Thread.sleep(5000);
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        Parent root=null;
                        try {
                            root=FXMLLoader.load(getClass().getResource("/library/management/Login/Login.fxml"));
                        } catch (IOException ex) {
                            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Scene scene=new Scene(root);
                        Stage stage=new Stage();
                        stage.setScene(scene);
                        stage.setTitle("ESTG Biblio Login");
                        stage.show();
                        stage.getIcons().add(new Image("/library/management/resources/icon.png"));
                        rootStackPane.getScene().getWindow().hide();
                    }
                    
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
