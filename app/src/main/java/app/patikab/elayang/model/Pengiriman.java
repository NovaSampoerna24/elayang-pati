package app.patikab.elayang.model;

/**
 * Created by paymin on 27/08/2018.
 */

public class Pengiriman {

    public String id;
    public String pengirim;
    public String id_surat_keluar;
    public String id_instansi;
    public String nama_instansi;
    public String tgl_baca;
    public String kurir;
    public String resi;
    public String id_surat_masuk;
    public String id_surat_tembusan;
    public String tgl_terima;
    public String penerima;

    public Pengiriman() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getId_surat_keluar() {
        return id_surat_keluar;
    }

    public void setId_surat_keluar(String id_surat_keluar) {
        this.id_surat_keluar = id_surat_keluar;
    }

    public String getId_instansi() {
        return id_instansi;
    }

    public void setId_instansi(String id_instansi) {
        this.id_instansi = id_instansi;
    }

    public String getNama_instansi() {
        return nama_instansi;
    }

    public void setNama_instansi(String nama_instansi) {
        this.nama_instansi = nama_instansi;
    }

    public String getTgl_baca() {
        return tgl_baca;
    }

    public void setTgl_baca(String tgl_baca) {
        this.tgl_baca = tgl_baca;
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

    public String getId_surat_masuk() {
        return id_surat_masuk;
    }

    public void setId_surat_masuk(String id_surat_masuk) {
        this.id_surat_masuk = id_surat_masuk;
    }

    public String getId_surat_tembusan() {
        return id_surat_tembusan;
    }

    public void setId_surat_tembusan(String id_surat_tembusan) {
        this.id_surat_tembusan = id_surat_tembusan;
    }

    public String getTgl_terima() {
        return tgl_terima;
    }

    public void setTgl_terima(String tgl_terima) {
        this.tgl_terima = tgl_terima;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }
}
