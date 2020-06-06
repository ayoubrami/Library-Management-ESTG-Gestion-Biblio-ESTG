
package library.management.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.management.AlertMaker.AlertMaker;
import library.management.database.DatabaseHandler;
import library.management.utils.Utilities;

public class MainController implements Initializable {
    @FXML
    private JFXTextField EtudCIN;
    @FXML
    private HBox Livre_info;
    @FXML
    private HBox Etudiant_info;
    @FXML
    private JFXTextField livreinfo;
    @FXML
    private Text livrename;
    @FXML
    private Text disp;
    @FXML
    private JFXTextField etudinfo;
    @FXML
    private Text etudname;
    @FXML
    private Text etudtele;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Text nometud;
    @FXML
    private Text emailetud;
    @FXML
    private Text teleetud;
    @FXML
    private Text nomlivre;
    @FXML
    private Text nomauteur;
    @FXML
    private Text editeur;
    @FXML
    private Text dateem;
    @FXML
    private Text nb_jours;
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private JFXButton renouveler;
    @FXML
    private JFXButton retourner;
    @FXML
    private HBox displayer;
    @FXML
    private StackPane livrecontainer;
    @FXML
    private StackPane etudcontainer;
    @FXML
    private Tab accueil;
    @FXML
    private Text renouvlercount;
    
    DatabaseHandler database;
    Boolean retournflag=false;
    PieChart livrechart;
    PieChart etudchart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       JFXDepthManager.setDepth(Livre_info, 1);
       JFXDepthManager.setDepth(Etudiant_info, 1);
       database=DatabaseHandler.getInstance();
       initDrawer();
       initGraph();
       refreshGraph();
    }    

    
    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearscene();
            DatabaseHandler database=DatabaseHandler.getInstance();
            retournflag=false;
            String CIN =EtudCIN.getText();
            try{
            String Query="SELECT EMPRUNTER.L_ID,EMPRUNTER.CIN,EMPRUNTER.DATE_EM,EMPRUNTER.RENOUVELER,\n" +
                          "ETUDIANTS.FULLNAME,ETUDIANTS.EMAIL,ETUDIANTS.TELE,\n" +
                          "LIVRE.TITRE,LIVRE.AUTEUR,LIVRE.EDITEUR\n" +
                          "FROM EMPRUNTER \n" +
                          "LEFT JOIN ETUDIANTS\n" +
                          "ON EMPRUNTER.CIN=ETUDIANTS.CIN\n" +
                          "LEFT JOIN LIVRE\n" +
                          "ON EMPRUNTER.L_ID =LIVRE.ID\n" +
                          "WHERE EMPRUNTER.CIN='"+CIN+"'";
            ResultSet rs=database.execQuery(Query);
            if(rs.next()){
                nometud.setText(rs.getString("FULLNAME"));
                emailetud.setText(rs.getString("EMAIL"));
                teleetud.setText(rs.getString("TELE"));
                
                nomlivre.setText(rs.getString("TITRE"));
                nomauteur.setText(rs.getString("AUTEUR"));
                editeur.setText(rs.getString("EDITEUR"));
                
                Timestamp issuetime=rs.getTimestamp("DATE_EM");
                Date date=new Date(issuetime.getTime());
                dateem.setText(date.toString());
                Long time =System.currentTimeMillis()-issuetime.getTime();
                Long jours=TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
                nb_jours.setText(" N° Des Jours : "+jours.toString());
                if(rs.getInt("RENOUVELER")<1){
                    renouvlercount.setText("Non renouvelé");   
                }else{
                    renouvlercount.setText("Déjà renouvelé");
                }
                retournflag=true;
                buttons(true);
                displayer.setOpacity(1);
            }
            else
            {
                JFXButton okbutton=new JFXButton("OK");
                AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Erreur !! ", "Il n'y a aucun élève dans les dossiers d'emprunt. ");
            
            }
            }catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void LoadLivreInfo(ActionEvent event) {
        clearlivre();
        affichageGraph(false);
        String id=livreinfo.getText();
        String Query="SELECT * FROM LIVRE WHERE ID='"+id+"'";
        ResultSet rs=database.execQuery(Query);
        Boolean flag=false;
        try {
            while(rs.next()){
                String lname=rs.getString("titre");
                Boolean lstatus=rs.getBoolean("isavail");
                flag=true;
                String status=(lstatus)? "Disponible":"Indisponible";
                livrename.setText(lname);
                disp.setText(status);
            }
            if(!flag){
                livrename.setText("Aucun livre de ce type n'est disponible");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void clearlivre(){
        livrename.setText("");
        disp.setText("");
    }
    void clearetud(){
        etudname.setText("");
        etudtele.setText("");
    }

    @FXML
    private void LoadEtudInfo(ActionEvent event) {
        clearetud();
        affichageGraph(false);
        String cin=etudinfo.getText();
        String Query="SELECT * FROM ETUDIANTS WHERE CIN ='"+cin+"'";
        ResultSet rs=database.execQuery(Query);
        Boolean flag=false;
        try {
            while(rs.next()){
                String ename=rs.getString("FULLNAME");
                String etele=rs.getString("TELE");
                etudname.setText(ename);
                etudtele.setText(etele);
                flag=true;
            }
            if(!flag){
                etudname.setText("Aucun Etudiant de ce type n'est disponible");     
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void EmprunterOperation(ActionEvent event) {
        String etudid= etudinfo.getText();
        String livreid=livreinfo.getText();
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton= new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        if(!(etudid.isEmpty() || livreid.isEmpty())){
        if(database.validtoissue(etudid)){
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(yesbutton, nobutton), "Confirmer l'emprunt",
                "êtes-vous sûr de vouloir emprunter ce livre "+livrename.getText()+"\n à cet étudiant "+etudname.getText()+" ?" );
            yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
           String query="INSERT INTO EMPRUNTER(L_ID,CIN)VALUES("
                  +"'"+livreid+"',"
                   +"'"+etudid+"')";
           String Query="UPDATE LIVRE SET NB_EXMP=NB_EXMP -1 WHERE ID ='"+livreid+"'";     
           if(database.execAction(query)&&database.execAction(Query)){
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Réussie", "Opération réussie ");
            database.Availiablity();
            refreshGraph();
           } else{
               AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Erreur !!", "Opération échoué  ");
           }   
            });
            nobutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Annulé", "Opération annulé ");
            });
        }else{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Opération échoué ", "Cet étudiant a déjà un livre !!");
        }   
        clearinfo();
        }else{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Erreur !! ", "Veuillez entrer tous les champs");
        }
    }

    @FXML
    private void RetournerOperation(ActionEvent event) {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton= new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(yesbutton, nobutton), "Confirmer","êtes-vous sûr de vouloir retourner ce livre?");
        yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            try {
                String cin=EtudCIN.getText();
                ResultSet rs=database.execQuery("SELECT L_ID FROM EMPRUNTER WHERE CIN ='"+cin+"'");
                while(rs.next()){
                    String livreid=rs.getString("L_ID");
                    String Query="DELETE FROM EMPRUNTER WHERE CIN = '"+cin+"'";
                    String Query1="UPDATE LIVRE SET NB_EXMP=NB_EXMP +1 WHERE ID= '"+livreid+"'";
                    if(database.execAction(Query) && database.execAction(Query1)){
                        AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Réussie", "Opération réussie ");
                        database.Availiablity();
                    }else{
                        AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Opération échoué ", "Cet étudiant a déjà un livre !!");
                    }
                }   } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        nobutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Annulé", "Opération annulé ");
        });
    }

    @FXML
    private void RenouvelerOperation(ActionEvent event) throws SQLException {
        JFXButton yesbutton=new JFXButton("Oui");
        JFXButton nobutton= new JFXButton("Non");
        JFXButton okbutton=new JFXButton("OK");
        if(database.validetorenew(EtudCIN.getText())){
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(yesbutton, nobutton), "Confirmer","êtes-vous sûr de vouloir renouveler ce livre?");
         yesbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            String cin=EtudCIN.getText();
            String Query ="UPDATE EMPRUNTER SET DATE_EM= CURRENT_TIMESTAMP, RENOUVELER=RENOUVELER +1 WHERE CIN= '"+cin+"'";
            if(database.execAction(Query)){
               AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Réussie", "Opération réussie ");
            }else{
               AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Opération échoué ", "Cet étudiant a déjà un livre !!");
            } 
         });
         nobutton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Annulé", "Opération annulé ");
        });
        }
        else{
            AlertMaker.showMaterialDialog(rootPane, rootBorderPane,Arrays.asList(okbutton),"Opération échoué ", "Vous avez déjà renouvelé ce livre !!");
         }
    }

    @FXML
    private void Closemenubar(ActionEvent event) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void listlivre(ActionEvent event) {
    Utilities.loadwindow(getClass().getResource("/library/management/ListBook/List_Book.fxml"), "Les Livres", null);
    }

    @FXML
    private void listmemebre(ActionEvent event) {
    Utilities.loadwindow(getClass().getResource("/library/management/ListMember/ListMember.fxml"), "Les Utilisateurs", null);
    }

    @FXML
    private void fullscreen(ActionEvent event) {
        Stage stage=((Stage)rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void initDrawer() {
        try {
            VBox toolbar=FXMLLoader.load(getClass().getResource("/library/management/toolbar/toolbar.fxml"));
            drawer.setSidePane(toolbar);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task= new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) ->{

          
                task.setRate(task.getRate() * -1);
                task.play();
                if(drawer.isClosed()){
                    drawer.open();
                }
                else{
                    drawer.close();
                }
            
            
            }
        );
    }

    private void clearscene() {
        nometud.setText("");
        emailetud.setText("");
        teleetud.setText("");
        
        nomlivre.setText("");
        nomauteur.setText("");
        editeur.setText("");
        
        dateem.setText("");
        nb_jours.setText("");
        buttons(false);
        displayer.setOpacity(0);
    }
    private void buttons(Boolean flag){
       if(flag){
           renouveler.setDisable(false);
           retourner.setDisable(false);
       }else{
           renouveler.setDisable(true);
           retourner.setDisable(true);
       }
    }

    private void clearinfo() {
        livreinfo.clear();
        etudinfo.clear();
        livrename.setText("");
        disp.setText("");
        etudname.setText("");
        etudtele.setText("");
        affichageGraph(true);
        refreshGraph();
    }

    private void initGraph() {
    livrechart = new PieChart(database.getLivreGraphStatistics());
    livrecontainer.getChildren().add(livrechart);
    etudchart = new PieChart(database.getEtudGraphStatistics());
    etudcontainer.getChildren().add(etudchart);
    accueil.setOnSelectionChanged((Event event) -> {
        clearinfo();
        if(accueil.isSelected()){
            refreshGraph();
        }
    });}
    private void affichageGraph(Boolean status){
        if(status){
            livrechart.setOpacity(1);
            etudchart.setOpacity(1);
        }else{
            livrechart.setOpacity(0);
            etudchart.setOpacity(0);
        }
    }
    private void refreshGraph(){
        livrechart.setData(database.getLivreGraphStatistics());
        etudchart.setData(database.getEtudGraphStatistics());
        
    }
}
