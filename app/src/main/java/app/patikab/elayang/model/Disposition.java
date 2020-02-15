package app.patikab.elayang.model;

/**
 * Created by paymin on 22/08/2018.
 */

public class Disposition {

    public String id;

    public String id_surat_masuk;
    public String tgl_disposisi;

    public String nip_pengirim;
    public String nama_pengirim;
    public String jabatan_pengirim;

    public String nip_penerima;
    public String nama_penerima;
    public String jabatan_penerima;

    public String isi_disposisi;
    public String tgl_baca;
    public String tgl_selesai;

    public String status;

    public Mail mail;


    public Disposition() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_surat_masuk() {
        return id_surat_masuk;
    }

    public void setId_surat_masuk(String id_surat_masuk) {
        this.id_surat_masuk = id_surat_masuk;
    }

    public String getTgl_disposisi() {
        return tgl_disposisi;
    }

    public void setTgl_disposisi(String tgl_disposisi) {
        this.tgl_disposisi = tgl_disposisi;
    }

    public String getNip_pengirim() {
        return nip_pengirim;
    }

    public void setNip_pengirim(String nip_pengirim) {
        this.nip_pengirim = nip_pengirim;
    }

    public String getNama_pengirim() {
        return nama_pengirim;
    }

    public void setNama_pengirim(String nama_pengirim) {
        this.nama_pengirim = nama_pengirim;
    }

    public String getJabatan_pengirim() {
        return jabatan_pengirim;
    }

    public void setJabatan_pengirim(String jabatan_pengirim) {
        this.jabatan_pengirim = jabatan_pengirim;
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

    public String getIsi_disposisi() {
        return isi_disposisi;
    }

    public void setIsi_disposisi(String isi_disposisi) {
        this.isi_disposisi = isi_disposisi;
    }

    public String getTgl_baca() {
        return tgl_baca;
    }

    public void setTgl_baca(String tgl_baca) {
        this.tgl_baca = tgl_baca;
    }

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public void setTgl_selesai(String tgl_selesai) {
        this.tgl_selesai = tgl_selesai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }
}
