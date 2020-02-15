package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 24/08/2018.
 */

public class SetStatusDispo {

    public String id;
    public String status;
    public String keterangan;

    public SetStatusDispo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
