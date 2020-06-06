
package library.management.mail.MailServerInfo;

public class MailServerInfo {
    private String stmpservername;
    private Integer port;
    private String email;
    private String pass;

    public MailServerInfo(String stmpservername, Integer port, String email, String pass) {
        this.stmpservername = stmpservername;
        this.port = port;
        this.email = email;
        this.pass = pass;
    }

    public String getStmpservername() {
        return this.stmpservername;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPass() {
        return this.pass;
    }

    public boolean validate() {
        return !this.stmpservername.isEmpty() && this.port != null && !this.email.isEmpty() && !this.pass.isEmpty();
    }
}
