package app.patikab.elayang.jsonReq;

/**
 * Created by paymin on 22/08/2018.
 */

public class ListKomentar {

    public String id_surat_masuk;
    public String id_disposisi;
    public String page;
    public String limit;

    public ListKomentar() {
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
