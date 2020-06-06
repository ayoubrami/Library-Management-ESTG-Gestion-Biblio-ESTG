//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package library.management.splash;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.management.utils.Utilities;

public class Loader extends Application {
   

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("Splash.fxml"));
        Scene scene = new Scene(root);
         stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
       
        Utilities.setIcon(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
