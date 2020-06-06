
package library.management.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import library.management.callback.callback;
import library.management.mail.MailServerInfo.MailServerInfo;

/**
 *
 * @author AYOUB
 */
public class MailService {
    public static void sendTestMail(MailServerInfo mailServerInfo, String recepient,callback call) {

        Runnable emailSendTask = () -> {
            Properties props = new Properties();
            try {
                
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", mailServerInfo.getStmpservername());
                props.put("mail.smtp.port", mailServerInfo.getPort());

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailServerInfo.getEmail(), mailServerInfo.getPass());
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailServerInfo.getEmail()));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recepient));
                message.setSubject("Test mail from Library Assistant");
                message.setText("Hi,"
                        + "\n\n This is a test mail from Library Management!");

                Transport.send(message);
                call.taskcomplete(Boolean.TRUE);
            } catch (Throwable exp) {
               Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, exp);
                call.taskcomplete(Boolean.FALSE);
                
            }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }
    public static void sendemail(MailServerInfo mailServerInfo, String recepient, String content, String title){
        Runnable emailSendTask = () -> {
            try{
        Properties prop= new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", mailServerInfo.getStmpservername());
        prop.put("mail.smtp.port", mailServerInfo.getPort());
        
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailServerInfo.getEmail(), mailServerInfo.getPass());
            }
        });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailServerInfo.getEmail()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(title);
            message.setContent(content, "text/html");

      
            Transport.send(message);
        
        System.out.println("Message sent successfully");  
                
          } catch(Exception e){
                  Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
                  }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }
    
}
