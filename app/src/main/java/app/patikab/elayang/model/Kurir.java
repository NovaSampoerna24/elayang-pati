package app.patikab.elayang.model;

public class Kurir {

    public String kode;
    public String nama;

    public Kurir() {
    }

    @Override
    public String toString() {
        return nama;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
