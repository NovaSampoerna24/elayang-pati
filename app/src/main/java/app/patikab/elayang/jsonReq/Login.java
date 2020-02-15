package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 22/08/2018.
 */

public class Login {

    public String nip;
    public String password;

    public Login() {
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
