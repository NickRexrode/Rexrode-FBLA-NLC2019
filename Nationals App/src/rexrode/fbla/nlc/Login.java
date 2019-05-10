package rexrode.fbla.nlc;
 
import java.util.ArrayList;
import java.util.List;
 
public class Login {
    private String user;
    private String pass;
   
    public Login(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
       
        public static String[] getLoginsAsText(Login[] logins) {
            String[] userpass = new String[logins.length];
           
            for (int i = 0; i < userpass.length; i++) {
                userpass[i] = "" +logins[i].getUser();
            }
            return userpass;
        }
        public static Login[] asArray(ArrayList<Login> a) {
            Login[] logins = new Login[a.size()];
            for (int i = 0; i < a.size(); i++) {
                logins[i] = a.get(i);
            }
            return logins;
           
        }
   
 
   
 
   
    public boolean equals(Object other) {
            boolean equals = false;
            if (other instanceof Login) {
                Login othera = (Login)other;
                String user1 = othera.getUser();
                String pass1 = othera.getPass();
                if (this.getUser().equals(user1) && this.getPass().equals(pass1)) {
                    equals = true;
                }
            }
        return equals;
    }
        public String getUser() {
            return this.user;
        }
        public String getPass() {
            return this.pass;
        }
        public String toString() {
            return ""+this.user + " "+ this.pass;
        }
 
}