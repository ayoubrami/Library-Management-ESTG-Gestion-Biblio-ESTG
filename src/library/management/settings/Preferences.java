
package library.management.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;


public class Preferences {
    public static final String CONFIG_FILE="Conf.txt";
    String username;
    String password;
    public Preferences(){
        username="admin";
        setPassword("admin");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length()<16){
            this.password = DigestUtils.sha512Hex(password);
        }else{
            this.password=password;
        }
        
    }
    
    public static void initconfig(){
        Writer writer=null;
        try {
            Preferences pref=new Preferences();
            Gson gson=new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(pref,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static Preferences getPreferences(){
        Gson gson=new Gson();
        Preferences pref=new Preferences();
        try {
            pref=gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initconfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pref;
    }
    public static void writepreftofile(Preferences pref){
         Writer writer=null;
        try {
            Gson gson=new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(pref,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
