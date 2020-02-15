package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 22/08/2018.
 */

public class SetStatusKonsep {

    public String id_surat_keluar;
    public String status;
    public String keterangan;

    public SetStatusKonsep() {
    }

    public String getId_surat_keluar() {
        return id_surat_keluar;
    }

    public void setId_surat_keluar(String id_surat_keluar) {
        this.id_surat_keluar = id_surat_keluar;
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
