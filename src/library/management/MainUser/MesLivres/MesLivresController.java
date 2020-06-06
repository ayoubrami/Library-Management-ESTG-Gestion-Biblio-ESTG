
package library.management.MainUser.MesLivres;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import library.management.AlertMaker.AlertMaker;
import library.management.Login.LoginController;
import library.management.database.DatabaseHandler;

public class MesLivresController implements Initializable {
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private Text demtitre;
    @FXML
    private Text demauteur;
    @FXML
    private Text demediteur;
    @FXML
    private Text restitre;
    @FXML
    private Text resauteur;
    @FXML
    private Text resediteur;
    @FXML
    private Text emptitre;
    @FXML
    private Text empauteur;
    @FXML
    private Text empediteur;
    @FXML
    private Text empdate;

    DatabaseHandler database=DatabaseHandler.getInstance();
    boolean demflag=false;
    boolean resflag=false;
    boolean empflag=false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    loadData();
    }    

    private void loadData() {
        clearentries();
        try {
            String Query1="SELECT LIVRE.TITRE,\n"
                    + "LIVRE.AUTEUR,LIVRE.EDITEUR \n"
                    + "FROM DEMANDE \n"
                    + "LEFT JOIN LIVRE \n"
                    + "ON DEMANDE.L_ID=LIVRE.ID \n"
                    + "WHERE CIN ='"+LoginController.cinetud+"'";
            ResultSet rs1=database.execQuery(Query1);
            if(rs1.next()){
                demtitre.setText("Titre : "+rs1.getString("TITRE"));
                demauteur.setText("Auteur : "+rs1.getString("AUTEUR"));
                demediteur.setText("Editeur : "+rs1.getString("EDITEUR"));
                demflag=true;
            }
            String Query2="SELECT LIVRE.TITRE,\n"
                    + "LIVRE.AUTEUR,LIVRE.EDITEUR \n"
                    + "FROM RESERVATION \n"
                    + "LEFT JOIN LIVRE \n"
                    + "ON RESERVATION.L_ID=LIVRE.ID \n"
                    + "WHERE CIN ='"+LoginController.cinetud+"'";
            ResultSet rs2=database.execQuery(Query2);
            if(rs2.next()){
                restitre.setText("Titre : "+rs2.getString("TITRE"));
                resauteur.setText("Auteur : "+rs2.getString("AUTEUR"));
                resediteur.setText("Editeur : "+rs2.getString("EDITEUR"));
                resflag=true;
            }
            String Query3="SELECT EMPRUNTER.DATE_EM,LIVRE.TITRE,\n"
                    + "LIVRE.AUTEUR,LIVRE.EDITEUR \n"
                    + "FROM EMPRUNTER \n"
                    + "LEFT JOIN LIVRE \n"
                    + "ON EMPRUNTER.L_ID=LIVRE.ID \n"
                    + "WHERE CIN ='"+LoginController.cinetud+"'";
            ResultSet rs3=database.execQuery(Query3);
            if(rs3.next()){
                emptitre.setText("Titre : "+rs3.getString("TITRE"));
                empauteur.setText("Auteur : "+rs3.getString("AUTEUR"));
                empediteur.setText("Editeur : "+rs3.getString("EDITEUR"));
                Timestamp issuetime=rs3.getTimestamp("DATE_EM");
                Date date=new Date(issuetime.getTime());
                empdate.setText("Date d'Emission : \n"+date.toString());
                empflag=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MesLivresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleCancelDem(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton donebutton=new JFXButton("Términé");
            if(demflag){
        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton), "Confirmer ", "Êtes-vous sûr de vouloir annuler cette demande ?");
        yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)->{
            String Query="DELETE FROM DEMANDE WHERE CIN='"+LoginController.cinetud+"'";
            if(database.execAction(Query)){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération réussie", "Vous avez annulé la demande avec succès");
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération échoué", "La démade n'a pas été annulée !");
            }   
            });
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Erreur ", "Il n'y a aucune demande !");
            }
    }

    @FXML
    private void handleCancelRes(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton donebutton=new JFXButton("Términé");
            if(resflag){
        AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton), "Confirmer ", "Êtes-vous sûr de vouloir annuler cette reservation ?");
        yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)->{
            String Query="DELETE FROM RESERVATION WHERE CIN='"+LoginController.cinetud+"'";
            if(database.execAction(Query)){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération réussie", "Vous avez annulé la reservation avec succès");
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération échoué", "La reservation n'a pas été annulée !");
            }   
            });
            }else{
                AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Erreur ", "Il n'y a aucune reservation !");
            }
    }

    @FXML
    private void handleRenouvler(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton=new JFXButton("Non");
        JFXButton donebutton=new JFXButton("Términé");
        if(empflag){
           if(database.validetorenew(LoginController.cinetud)){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(yesbutton,nobutton), "Confirmer ", "Êtes-vous sûr de vouloir annuler cette reservation ?");
            yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventt)->{
            String Query="UPDATE EMPRUNTER SET DATE_EM= CURRENT_TIMESTAMP, RENOUVELER=RENOUVELER +1 WHERE CIN= '"+LoginController.cinetud+"'";
            if(database.execAction(Query)){
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération réussie", "Vous avez renouveler votre livre avec succès");
            }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération échoué", "Quelque chose s'est mal passé!");
            }   
            });
          }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Opération échoué", "Vous avez déjà renouvelé ce livre !");
           }
        }else{
            AlertMaker.showMaterialDialog(rootStackPane, rootAnchorPane, Arrays.asList(donebutton), "Erreur ", "Il n'y a aucune livre !");
        }
        
    }
    private void clearentries(){
        demtitre.setText("");
        demauteur.setText("");
        demediteur.setText("");
        
        restitre.setText("");
        resauteur.setText("");
        resediteur.setText("");
        
        emptitre.setText("");
        empauteur.setText("");
        empediteur.setText("");
        empdate.setText("");
    }
    
}
