package app.patikab.elayang.model;

/**
 * Created by paymin on 27/08/2018.
 */

public class User {
    public String nip;
    public String nama_lengkap;
    public String nohp;
    public String email;
    public String foto;
    public String nama_jabatan;
    public String kode_eselon;
    public String keyword;

    public User() {
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama_jabatan() {
        return nama_jabatan;
    }

    public void setNama_jabatan(String nama_jabatan) {
        this.nama_jabatan = nama_jabatan;
    }

    public String getKode_eselon() {
        return kode_eselon;
    }

    public void setKode_eselon(String kode_eselon) {
        this.kode_eselon = kode_eselon;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return nama_lengkap;
    }

}
