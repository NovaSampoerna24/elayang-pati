package app.patikab.elayang.util;

import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by paymin on 22/08/2018.
 */

public class Config {

//  public static String BASE_URL = "https://latihaneoffice.patikab.go.id/api";
    public static String BASE_URL = "https://e-layang.patikab.go.id/api";
//  public static String BASE_URL = "http://10.10.8.7:8080/elayang_api/api";
//  public static String BASE_URL = "http://bimtek.e-layang.patikab.go.id/api";
//  public static String BASE_URL = "https://bimtek-elayang.patikab.go.id/api";


  public static String BASE_URL_STORAGE = "https://e-layang.patikab.go.id";
//  public static String BASE_URL_STORAGE = "http://bimtek-elayang.patikab.go.id";
//    public static String BASE_URL_STORAGE = "10.10.8.7";
//  public static String BASE_URL_STORAGE = "http://10.10.8.7:8080/elayang_api";

//    public static String BASE_URL_PICTURES = "http://bimtek-elayang.patikab.go.id";
   public static String BASE_URL_PICTURES = "https://e-layang.patikab.go.id";
//    public static String BASE_URL_PICTURES = "http://10.10.8.7:8080/elayang_api";

//    public static String BASE_URL_STORAGE = "http://bimtek.e-layang.patikab.go.id";

    public static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static String ACCEPT = "application/json";

    public static String DEFAULT_STRING = "";

    public static String INTENT_TYPE = "INTENTTYPE";
    public static String INTENT_DATA = "INTENTDATA";
    public static String INTENT_ID_MAIL = "INTENTID";
    public static String INTENT_ID_KONSEP = "INTENTKONSEP";
    public static String INTENT_ID_DISPO = "INTENTIDDISPO";

    public static String INBOX = "Surat Masuk";
    public static String OUTBOX = "Surat Keluar";
    public static String DISIN = "Disposisi Masuk";
    public static String DISOUT = "Diposisi Keluar";
    public static String DISPO = "Disposisi";
    public static String KONSEP = "Konsep Surat";
    public static String TTE = "Tanda Tangan Elektronik";

    public static void setErrorEmpty(EditText editText) {
        editText.setError(((TextInputLayout) editText.getParent().getParent())
                .getHint() + " Belum Diisi ");
    }

    public static String convertMonth(String month) {
        String bulan = "";
        if (month.equals("1") || month.equals("01")) {
            bulan = "Januari";
        } else if (month.equals("2") || month.equals("02")) {
            bulan = "Februari";
        } else if (month.equals("3") || month.equals("03")) {
            bulan = "Maret";
        } else if (month.equals("4") || month.equals("04")) {
            bulan = "April";
        } else if (month.equals("5") || month.equals("05")) {
            bulan = "Mei";
        } else if (month.equals("6") || month.equals("06")) {
            bulan = "Juni";
        } else if (month.equals("7") || month.equals("07")) {
            bulan = "Juli";
        } else if (month.equals("8") || month.equals("08")) {
            bulan = "Agustus";
        } else if (month.equals("9") || month.equals("09")) {
            bulan = "September";
        } else if (month.equals("10") || month.equals("10")) {
            bulan = "Oktober";
        } else if (month.equals("11") || month.equals("11")) {
            bulan = "November";
        } else if (month.equals("12") || month.equals("12")) {
            bulan = "Desember";
        }

        return bulan;
    }

    public static Spanned formatHtml(String sorc) {
        sorc = sorc.replace("\\n", " ");
        sorc = sorc.replace("\\\"", "\"");
        sorc = sorc.replace("<br></p>", "</p>");
        sorc = sorc.replace("<p>", "");
        sorc = sorc.replace("</p>", "");
        return Html.fromHtml(sorc);
    }

    public static String defKecepatan(String kecepatan) {
        String def = "Biasa";

        if (kecepatan.equals("1"))
            def = "Biasa";
        else if (kecepatan.equals("2"))
            def = "Segera";
        else if (kecepatan.equals("3"))
            def = "Amat Segera";
        else if (kecepatan.equals("4"))
            def = "Rahasia";
        else if (kecepatan.equals("5"))
            def = "Penting";

        return def;
    }

    public static String defStatus(String status) {
        String def = "Belum Ditanggapi";

        if (status.equals("0"))
            def = "Belum Ditanggapi";
        else if (status.equals("1"))
            def = "Diselesaikan";
        else if (status.equals("2"))
            def = "Ditolak";

        return def;
    }

    public static String defStatusKonsep(String status) {
        String def = "Proses Pemeriksaan";

        if (status.equals("0"))
            def = "Belum Dibaca";
        else if (status.equals("1"))
            def = "Proses Pemeriksaan";
        else if (status.equals("2"))
            def = "Revisi";
        else if (status.equals("3"))
            def = "Disetujui";
        else if (status.equals("4"))
            def = "Terkirim";


        return def;
    }

    public static String defKeamanan(String keamanan) {
        String def = "Biasa";

        if (keamanan.equals("1"))
            def = "Biasa";
        else if (keamanan.equals("2"))
            def = "Rahasia Terbatas";
        else if (keamanan.equals("3"))
            def = "Rahasia";
        else if (keamanan.equals("4"))
            def = "Sangat Rahasia";

        return def;
    }


    public static String defTanggalWaktu(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        Date dates;
        String dateTime = "not defined";
        try {
            dates = format.parse(date);
            dateTime = dateFormat.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }


    public static String defTanggal(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date dates;
        String dateTime = "not defined";
        try {
            dates = format.parse(date);
            dateTime = dateFormat.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }


}
