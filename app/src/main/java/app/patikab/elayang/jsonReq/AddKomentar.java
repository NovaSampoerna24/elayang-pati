package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 22/08/2018.
 */

public class AddKomentar {

    public String id_surat_masuk;
    public String id_disposisi;
    public String isi_komentar;

    public AddKomentar() {
    }

    public String getId_surat_masuk() {
        return id_surat_masuk;
    }

    public void setId_surat_masuk(String id_surat_masuk) {
        this.id_surat_masuk = id_surat_masuk;
    }

    public String getId_disposisi() {
        return id_disposisi;
    }

    public void setId_disposisi(String id_disposisi) {
        this.id_disposisi = id_disposisi;
    }

    public String getIsi_komentar() {
        return isi_komentar;
    }

    public void setIsi_komentar(String isi_komentar) {
        this.isi_komentar = isi_komentar;
    }
}
