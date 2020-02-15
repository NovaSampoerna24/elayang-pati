package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 22/08/2018.
 */

public class AddDisposisi {

    public String id_surat_masuk;
    public String nip_penerima;
    public String nama_penerima;
    public String jabatan_penerima;
    public String instruksi;
    public String isi_disposisi;

    public AddDisposisi() {
    }

    public String getId_surat_masuk() {
        return id_surat_masuk;
    }

    public void setId_surat_masuk(String id_surat_masuk) {
        this.id_surat_masuk = id_surat_masuk;
    }

    public String getNip_penerima() {
        return nip_penerima;
    }

    public void setNip_penerima(String nip_penerima) {
        this.nip_penerima = nip_penerima;
    }

    public String getNama_penerima() {
        return nama_penerima;
    }

    public void setNama_penerima(String nama_penerima) {
        this.nama_penerima = nama_penerima;
    }

    public String getJabatan_penerima() {
        return jabatan_penerima;
    }

    public void setJabatan_penerima(String jabatan_penerima) {
        this.jabatan_penerima = jabatan_penerima;
    }

    public String getInstruksi() {
        return instruksi;
    }

    public void setInstruksi(String instruksi) {
        this.instruksi = instruksi;
    }

    public String getIsi_disposisi() {
        return isi_disposisi;
    }

    public void setIsi_disposisi(String isi_disposisi) {
        this.isi_disposisi = isi_disposisi;
    }
}
