
package library.management.utils;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.management.main.MainController;

public class Utilities {
    private static final String IMAGE_LOCATION="/library/management/resources/icon.png";
    public static void setIcon(Stage stage){
        stage.getIcons().add(new Image(IMAGE_LOCATION));
    }
    public static Object loadwindow(URL loc,String titre,Stage parentstage){
        Object controller = null;
        try{
             FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            Stage stage = null;
            if (parentstage != null) {
                stage = parentstage;
            } else {
                stage = new Stage(StageStyle.DECORATED);
            }
            stage.setTitle(titre);
            stage.setScene(new Scene(parent));
            stage.show();
            setIcon(stage);
        }catch(IOException e){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
        }
        return controller;
    }
    
}
