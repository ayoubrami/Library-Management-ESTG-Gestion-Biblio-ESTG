
package library.management.database;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;
import javax.swing.JOptionPane;
import library.management.ListBook.List_BookController.Book;
import library.management.mail.MailServerInfo.MailServerInfo;
import library.management.mail.MailService;
import library.management.main.MainController;

public final class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static final String DB_URL = "jdbc:derby:database;create=true";
    public static Connection con = null;
    public static Statement stm = null;
    public static PreparedStatement pst = null;
    private MailServerInfo mailserverinfo;

    private DatabaseHandler() {
        createConnection();
        this.setupEtudiantsTable();
        this.setupLivreTable();
        this.setupEmprunterTable();
        this.setupDemandeTable();
        this.setupReservationTable();
        this.setupMailServiceTable();
        this.Availiablity();
        this.SendReservationMail();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }

        return handler;
    }

    private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            con = DriverManager.getConnection("jdbc:derby:database;create=true");
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException var1) {
            JOptionPane.showMessageDialog((Component)null, "Cant load database", "Database Error", 0);
            System.exit(0);
        }

    }

    private void setupLivreTable() {
        String Table_name = "Livre";
       
            try {
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table_name.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table_name + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table_name + 
                            "(id varchar(100) primary key,\n"
                            + "titre varchar(200),auteur varchar(100),\n"
                            + "editeur varchar(100),nb_pages int,\n"
                            + "description clob,nb_exmp int,\n"
                            + "place varchar(200),isavail boolean)");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }

    }

    private void setupEtudiantsTable() {
        String Table = "ETUDIANTS";

            try {   
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table + 
                            "(CIN varchar(30) primary key,\n"
                            + "FULLNAME varchar(50),\n"
                            + "EMAIL varchar(100),\n"
                            + "TELE varchar(100),\n"
                            + "PASSWORD varchar(150))");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }
        
    }

    private void setupEmprunterTable() {
        String Table_name = "EMPRUNTER";

            try {
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table_name.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table_name + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table_name + 
                            "(L_ID varchar(100) primary key,\n"
                            + "CIN varchar(30),\n"
                            + "DATE_EM timestamp default CURRENT_TIMESTAMP,\n"
                            + "RENOUVELER integer default 0,\n"
                            + "FOREIGN KEY (L_ID) REFERENCES LIVRE(id),\n"
                            + "FOREIGN KEY (CIN) REFERENCES ETUDIANTS(CIN))");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }

    }

    private void setupDemandeTable() {
        String Table_name = "DEMANDE";

            try {
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table_name.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table_name + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table_name + 
                            "(L_ID varchar(100),\n"
                            + "CIN varchar(30) primary key,\n"
                            + "FOREIGN KEY (L_ID) REFERENCES LIVRE(id),\n"
                            + "FOREIGN KEY (CIN) REFERENCES ETUDIANTS(CIN))");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }
    }

    private void setupReservationTable() {
        String Table_name = "RESERVATION";

        
            try {
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table_name.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table_name + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table_name + 
                            "(L_ID varchar(100),\n"
                            + "CIN varchar(30) primary key,\n"
                            + "EMAIL varchar(100),\n"
                            + "FOREIGN KEY (L_ID) REFERENCES LIVRE(id),\n"
                            + "FOREIGN KEY (CIN) REFERENCES ETUDIANTS(CIN))");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }
    }

    private void setupMailServiceTable() {
        String Table_name = "MAILSERVICE";
            try {
                stm = con.createStatement();
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet tables = dbm.getTables((String)null, (String)null, Table_name.toUpperCase(), (String[])null);
                if (tables.next()) {
                    System.out.println("Table " + Table_name + " already exists ready to go ");
                } else {
                    stm.execute("CREATE TABLE " + Table_name + 
                            "(STMPSERVERNAME varchar(255),\n"
                            + "STMPPORT INTEGER,\n"
                            + "EMAIL VARCHAR(100) ,\n"
                            + "PASSWORD VARCHAR(150))");
                }
            } catch (SQLException var7) {
                System.err.println(var7.getMessage() + ".....setupDatabase");
            }

       
    }

    public ResultSet execQuery(String query) {
        try {
            stm = con.createStatement();
            ResultSet result = stm.executeQuery(query);
            return result;
        } catch (SQLException var8) {
            System.out.println("Exception at execQuery:dataHandler" + var8.getLocalizedMessage());
            Object var4 = null;
            return (ResultSet)var4;
        } 
    }

    public boolean execAction(String qu) {
        try {
            stm = con.createStatement();
            stm.execute(qu);
            boolean var2 = true;
            return var2;
        } catch (SQLException var7) {
            JOptionPane.showMessageDialog((Component)null, "Error:" + var7.getMessage(), "Error Occured", 0);
            System.out.println("Exception at execAction:dataHandler" + var7.getLocalizedMessage());
            boolean var3 = false;
            return var3;
        } 
    }

    public void setMailServerInfo(MailServerInfo mailserverinfo) {
        this.mailserverinfo = mailserverinfo;
    }

    public void Availiablity() {
        try {
            String qu = "SELECT NB_EXMP FROM LIVRE";
            ResultSet rs = this.execQuery(qu);
            while(rs.next()) {
                String Query;
                if (rs.getInt("NB_EXMP") == 0) {
                    Query = "UPDATE LIVRE SET isavail=false WHERE NB_EXMP=0";
                    pst = con.prepareStatement(Query);
                    pst.executeUpdate();
                } else {
                    Query = "UPDATE LIVRE SET isavail=true WHERE NB_EXMP>0";
                    pst = con.prepareStatement(Query);
                    pst.executeUpdate();
                }
            }
        } catch (SQLException var4) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var4);
        }

    }

    public ObservableList<Data> getLivreGraphStatistics() {
        ObservableList data = FXCollections.observableArrayList();

        try {
            String Query1 = "SELECT COUNT (*) FROM LIVRE";
            String Query2 = "SELECT COUNT (*) FROM EMPRUNTER";
            ResultSet rs = this.execQuery(Query1);
            int count;
            if (rs.next()) {
                count = rs.getInt(1);
                data.add(new Data("Total de livres (" + count + ")", (double)count));
            }

            rs = this.execQuery(Query2);
            if (rs.next()) {
                count = rs.getInt(1);
                data.add(new Data("Livres empruntés (" + count + ")", (double)count));
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return data;
    }

    public ObservableList<Data> getEtudGraphStatistics() {
        ObservableList data = FXCollections.observableArrayList();

        try {
            String Query1 = "SELECT COUNT (*) FROM ETUDIANTS";
            String Query2 = "SELECT COUNT (DISTINCT CIN) FROM EMPRUNTER";
            ResultSet rs = this.execQuery(Query1);
            int count;
            if (rs.next()) {
                count = rs.getInt(1);
                data.add(new Data("Nombre total d'étudiants (" + count + ")", (double)count));
            }

            rs = this.execQuery(Query2);
            if (rs.next()) {
                count = rs.getInt(1);
                data.add(new Data("Étudiants avec des livres (" + count + ")", (double)count));
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return data;
    }

    public boolean validtodemande1(Book book) {
        return book.getDisp().equalsIgnoreCase("Disponible");
    }

    public boolean validtodemande2(String cin) {
        int count = 0;
        String Query = "SELECT COUNT (*) FROM DEMANDE WHERE CIN ='" + cin + "'";
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return count < 1;
    }

    public boolean validtodemande3(Book book) {
        String Query = "SELECT * FROM EMPRUNTER WHERE L_ID='" + book.getId() + "'";
        ResultSet rs = this.execQuery(Query);

        try {
            if (rs.next()) {
                return false;
            }
        } catch (SQLException var5) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return true;
    }

    public boolean validtoissue(String cin) {
        int count = 0;
        String Query = "SELECT COUNT(*) FROM EMPRUNTER WHERE CIN='" + cin + "'";
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return count < 1;
    }

    public boolean validetorenew(String cin) {
        int renew = 0;
        String Query = "SELECT RENOUVELER FROM EMPRUNTER WHERE CIN='" + cin + "'";
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                renew = rs.getInt("RENOUVELER");
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return renew < 1;
    }

    public boolean validetoreserve(String cin, String id) {
        String Query = "SELECT COUNT(*) FROM EMPRUNTER WHERE CIN ='" + cin + "' AND L_ID='" + id + "'";
        int count = 0;
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException var7) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return count < 1;
    }

    public boolean validetoreserve2(String cin, String id) {
        String Query = "SELECT COUNT(*) FROM RESERVATION WHERE CIN ='" + cin + "' AND L_ID='" + id + "'";
        int count = 0;
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException var7) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var7);
        }

        return count < 1;
    }

    public boolean deletefromlivre(Book book) {
        try {
            String Query = "DELETE FROM LIVRE WHERE ID = ?";
            PreparedStatement stmt = con.prepareStatement(Query);
            stmt.setString(1, book.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException var5) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return false;
    }

    public boolean userdeletion(String cin) {
        int count = 0;
        String Query = "SELECT COUNT(*) FROM EMPRUNTER WHERE CIN='" + cin + "'";
        ResultSet rs = this.execQuery(Query);

        try {
            while(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException var6) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return count < 1;
    }

    public void SendReservationMail() {
        String Query = "SELECT LIVRE.ISAVAIL,RESERVATION.EMAIL FROM LIVRE RIGHT JOIN RESERVATION ON LIVRE.ID=RESERVATION.L_ID WHERE ISAVAIL='true'";
        ResultSet rs = this.execQuery(Query);

        try {
            if (rs.next()) {
                String email = rs.getString("EMAIL");
                if (!email.isEmpty()) {
                    MailServerInfo mailserverinfoo = loadMailServerInfo();
                    MailService.sendemail(mailserverinfoo, email, "this is content", "this is title");
                    String Query2 = "DELETE FROM RESERVATION WHERE EMAIL ='" + email + "'";
                    this.execAction(Query2);
                }
            }
        } catch (SQLException var6) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

    }

    private void SendOverdueMail() {
    }

    public static void wipeTable(String tableName) {
        try {
            Statement statement = con.createStatement();
            statement.execute("DELETE FROM " + tableName + " WHERE TRUE");
        } catch (SQLException var2) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, (String)null, var2);
        }

    }

    public static boolean updateMailServerInfo(MailServerInfo mailServerInfo) {
        try {
            wipeTable("MAILSERVICE");
            PreparedStatement statement = con.prepareStatement("INSERT INTO MAILSERVICE(STMPSERVERNAME,STMPPORT,EMAIL,PASSWORD) VALUES(?,?,?,?)");
            statement.setString(1, mailServerInfo.getStmpservername());
            statement.setInt(2, mailServerInfo.getPort());
            statement.setString(3, mailServerInfo.getEmail());
            statement.setString(4, mailServerInfo.getPass());
            return statement.executeUpdate() > 0;
        } catch (SQLException var2) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, (String)null, var2);
            return false;
        }
    }

    public static MailServerInfo loadMailServerInfo() {
        try {
            String Query = "SELECT * FROM MAILSERVICE";
            ResultSet rs = stm.executeQuery(Query);
            if (rs.next()) {
                String mailServer = rs.getString("STMPSERVERNAME");
                Integer port = rs.getInt("STMPPORT");
                String emailID = rs.getString("EMAIL");
                String userPassword = rs.getString("PASSWORD");
                return new MailServerInfo(mailServer, port, emailID, userPassword);
            }
        } catch (SQLException var6) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, (String)null, var6);
        }

        return null;
    }

    public static boolean validemail(String email) {
        String checkemail = null;

        try {
            String Query = "SELECT * FROM ETUDIANTS";
            ResultSet rs = stm.executeQuery(Query);
            if (rs.next()) {
                checkemail = rs.getString("EMAIL");
                return checkemail.equals(email);
            }
        } catch (SQLException var4) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var4);
        }

        return false;
    }

    public static boolean validcin(String cin) {
        String checkcin = null;

        try {
            String Query = "SELECT * FROM ETUDIANTS";
            ResultSet rs = stm.executeQuery(Query);
            if (rs.next()) {
                checkcin = rs.getString("CIN");
                return checkcin.equals(cin);
            }
        } catch (SQLException var4) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, (String)null, var4);
        }

        return false;
    }
}
