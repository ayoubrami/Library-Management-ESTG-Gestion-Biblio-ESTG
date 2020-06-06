
package library.management.AlertMaker;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.management.utils.Utilities;

public class AlertMaker {
    public AlertMaker() {
    }

    public static void showSimpleAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showErrorMessage(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String header, String body) {
        BoxBlur blur = new BoxBlur(4.0D, 4.0D, 4);
        if (controls.isEmpty()) {
            controls.add(new JFXButton("Okay"));
        }

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, DialogTransition.BOTTOM);
        controls.forEach((controlButton) -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                dialog.close();
            });
        });
        dialogLayout.setHeading(new Node[]{new Label(header)});
        dialogLayout.setBody(new Node[]{new Label(body)});
        dialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed((event1) -> {
            nodeToBeBlurred.setEffect((Effect)null);
        });
        nodeToBeBlurred.setEffect(blur);
    }

    private static void styleAlert(Alert alert) {
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        Utilities.setIcon(stage);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertMaker.class.getResource("/library/management/resources/them.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }
}
