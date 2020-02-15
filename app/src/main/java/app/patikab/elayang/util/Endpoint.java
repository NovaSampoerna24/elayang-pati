package app.patikab.elayang.util;

/**
 * Created by paymin on 22/08/2018.
 */

public class Endpoint {

    public static String GET_VERSION = "/getVersion";

    public static String GET_TOKEN = "/getToken";
    public static String SET_FIREBASE = "/setFirebase";
    public static String UNSET_FIREBASE = "/unsetFirebase";

    public static String LIST_NOTIF = "/notifikasi";
    public static String READ_NOTIF = "/notifikasi/";

    public static String MASTER_INSTRUKSI = "/master-instruksi";

    public static String LIST_USER_DISPOSITION = "/user/dispositionReciver/";

    public static String PROFILE_DETAIL = "/profile";
    public static String PROFILE_EDIT = "/profile/";
    public static String PROFILE_EDIT_PWD = "/profile/editPassword";

    public static String SURAT_MASUK_LIST = "/surat-masuk";
    public static String SURAT_MASUK_DETAIL = "/surat-masuk/";

    public static String SURAT_KELUAR_LIST = "/surat-keluar";
    public static String SURAT_KELUAR_DETAIL = "/surat-keluar/";

    public static String TTE_SURAT_KELUAR = "/tanda-tangan";
    public static String TTE_SURAT_DETAIL = "/tanda-tangan/";
    public static String TTE_SURAT_Tandatangani = "/tanda-tangan/";
   
    public static String KOMENTAR_LIST = "/komentar";
    public static String KOMENTAR_ADD = "/komentar/";

    public static String DISPOSISI_DETAIL = "/disposisi/";
    public static String DISPOSISI_SET_STATUS = "/disposisi/setStatus/";
    public static String DISPOSISI_ADD = "/disposisi";
    public static String DISPOSISI_LIST_IN = "/disposisi/masuk";
    public static String DISPOSISI_LIST_OUT = "/disposisi/keluar";
    public static String DISPOSISI_LIST_BYMAIL = "/disposisi/surat/";

    public static String KONSEP_LIST = "/konsep-surat/listChecked";
    public static String KONSEP_DETAIL = "/konsep-surat/";
    public static String KONSEP_CHECKING = "/konsep-surat/checkingConcept/";
    public static String KONSEP_CHECKER = "/konsep-surat/updateChecker/";
    public static String KONSEP_MAIL_CHECKER = "/user/all/";

    public static String STATISTIK_TOTAL = "/statistik/total";
    public static String STATISTIK_UNREAD = "/statistik/unread";

    public static String EKSPEDISI_LIST = "/ekspedisi";
    public static String UPDATE_RESI = "/surat-pengiriman/";
    public static String TRACK_RESI = "/surat-pengiriman/track/";

    public static String PLT_LIST = "/listplt";

}
