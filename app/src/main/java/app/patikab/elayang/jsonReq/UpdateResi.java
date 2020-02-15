package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 24/08/2018.
 */

public class UpdateResi {

    public String id;
    public String kurir;
    public String resi;

    public UpdateResi() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKurir() {
        return kurir;
    }

    public void setKurir(String kurir) {
        this.kurir = kurir;
    }

    public String getResi() {
        return resi;
    }

    public void setResi(String resi) {
        this.resi = resi;
    }
}
