package app.patikab.elayang;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;

import com.android.volley.Request;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.adapter.ListPemeriksaAdapter;
import app.patikab.elayang.jsonReq.SetListPemeriksa;
import app.patikab.elayang.jsonReq.SetStatusKonsep;
import app.patikab.elayang.model.Konsep;
import app.patikab.elayang.model.User;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

public class DetailKonsepActivity extends AppCompatActivity
        implements RequestHandler.IParse, ListPemeriksaAdapter.IPemeriksaAdapter {

    private WebView webView;

    @BindView(R.id.linear1)
    LinearLayout line1;
    @BindView(R.id.linear2)
    LinearLayout line2;
    @BindView(R.id.showMore1)
    TextView showMore1;
    @BindView(R.id.showMore2)
    TextView showMore2;

    @BindView(R.id.linearPembuat)
    LinearLayout lnPembuat;
    @BindView(R.id.linearTujuan)
    LinearLayout lnTujuan;

    @BindView(R.id.textViewNoSurat)
    TextView tvNoSurat;
    @BindView(R.id.textViewNoAgenda)
    TextView tvNoAgenda;
    @BindView(R.id.textViewTglSurat)
    TextView tvTglSurat;
    @BindView(R.id.textViewStatus)
    TextView tvStatus;
    @BindView(R.id.textViewPerihal)
    TextView tvPerihal;
    @BindView(R.id.textViewPembuat)
    TextView tvNamaPembuat;
    @BindView(R.id.textViewJabatanPembuat)
    TextView tvJabatanPembuat;
    @BindView(R.id.textViewTujuan)
    TextView tvTujuan;

    @BindView(R.id.textViewTembusan)
    TextView tvTembusan;
    @BindView(R.id.textViewPemeriksa)
    TextView tvPemeriksa;
    @BindView(R.id.textViewKaitanSurat)
    TextView tvKaitanSurat;
    @BindView(R.id.textViewRingakas)
    TextView tvRingkasan;
    @BindView(R.id.textViewIsi)
    TextView tvIsi;

    @BindView(R.id.textViewKlasifikasi)
    TextView tvKlasifikasi;
    @BindView(R.id.textViewKecepatan)
    TextView tvKecepatan;
    @BindView(R.id.textViewKeamanan)
    TextView tvKeamanan;
    @BindView(R.id.textViewPindaian)
    ImageView ivPindaian;

    @BindView(R.id.recyclerViewPemeriksa)
    RecyclerView rvPemeriksa;

    String idKonsep;
    String url;
    @BindView(R.id.fabExpand)
    FloatingActionsMenu fabExpand;

    List<User> userPemeriksas = new ArrayList<>();
    List<Konsep> pemeriksas = new ArrayList<>();
    ListPemeriksaAdapter pemeriksaAdapter;
    List<String> arrPemeriksa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konsep);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idKonsep = getIntent().getStringExtra(Config.INTENT_ID_KONSEP);
        pemeriksaAdapter = new ListPemeriksaAdapter(this, this, pemeriksas);
        rvPemeriksa.setLayoutManager(new LinearLayoutManager(this));
        rvPemeriksa.setAdapter(pemeriksaAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            line1.getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
            line2.getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        showMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lnPembuat.getVisibility() == View.GONE) {
                    showMore1.setText("Show Less");
                    lnPembuat.setVisibility(View.VISIBLE);
                    lnTujuan.setVisibility(View.VISIBLE);
                } else {
                    showMore1.setText("Show More");
                    lnPembuat.setVisibility(View.GONE);
                    lnTujuan.setVisibility(View.GONE);
                }
            }
        });

        showMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvIsi.getLineCount() > 3) {
                    showMore2.setText("Show More");
                    tvIsi.setMaxLines(3);
                } else {
                    showMore2.setText("Show Less");
                    tvIsi.setMaxLines(Integer.MAX_VALUE);
                }
            }
        });

        findViewById(R.id.fabFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                openFile();
            }
        });

        findViewById(R.id.fabStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                openDialogStatus();
            }
        });

        findViewById(R.id.fabPemeriksa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                openDialogPemriksa();
            }
        });

        ivPindaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        reqDetailKonsep();
        reqListPemeriksa();
    }

    private void openDialogStatus() {
        final View view = getLayoutInflater().inflate(R.layout.content_status_konsep, null);
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Ubah Status")
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.editTextIsi);
                        Spinner spinner = view.findViewById(R.id.spinnerStatus);
                        String status = String.valueOf(spinner.getSelectedItemPosition() + 2);

                        reqUpdateStatus(editText.getText().toString(), status);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    private void openDialogPemriksa() {
        final View view = getLayoutInflater().inflate(R.layout.content_pemeriksa_konsep, null);
        final ListView lView = view.findViewById(R.id.listView);
        lView.setAdapter(new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_multiple_choice,
                userPemeriksas));
        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Tambah Pemeriksa")
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reqAddPemeriksa(lView);

                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    private void reqUpdateStatus(String keterangan, String status) {
        SetStatusKonsep statusKonsep = new SetStatusKonsep();
        statusKonsep.setId_surat_keluar(idKonsep);
        statusKonsep.setKeterangan(keterangan);
        statusKonsep.setStatus(status);

        String jsonReq = new Gson().toJson(statusKonsep);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.KONSEP_CHECKING + idKonsep, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reqAddPemeriksa(ListView lView) {
        List<Konsep> checkedUser = new ArrayList<>();
        SparseBooleanArray checked = lView.getCheckedItemPositions();
        for (int i = 0; i < lView.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                arrPemeriksa.add(userPemeriksas.get(i).nip);
                Konsep konsep = new Konsep();
                konsep.setNama_pemeriksa(userPemeriksas.get(i).getNama_lengkap());
                konsep.setNip_pemeriksa(userPemeriksas.get(i).getNip());
                konsep.setJabatan_pemeriksa(userPemeriksas.get(i).getNama_jabatan());
                checkedUser.add(konsep);
            }
        }

        SetListPemeriksa listPemeriksa = new SetListPemeriksa();
        listPemeriksa.setId_surat_keluar(idKonsep);
        listPemeriksa.setArr_pemeriksa(new Gson().toJson(checkedUser));

        String jsonReq = new Gson().toJson(listPemeriksa);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.KONSEP_CHECKER + idKonsep, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private WebView view; //membuat variabel view agar bisa akses method

    private void openFile() {
        if (!url.equals("") && !url.equals(null) && !url.equals("undefined") ) {

            //setContentView(R.layout.activity_webview);
            //view = (WebView) this.findViewById(R.id.webviewid);
            //view.getSettings().setJavaScriptEnabled(true);
            //view.getSettings().setSupportZoom(true);
            //view.setWebViewClient(new DetailKonsepActivity.MyBrowser());
            //view.loadData("<iframe src='"+url+"' style='border: 0; width: 100%; height: 100%'></iframe>", "text/html; charset=utf-8", "UTF-8");
            //view.loadUrl("https://docs.google.com/viewerng?embedded=true&url="+url);
            //view.loadData(("http://103.110.43.48:8080/generatepdf/tte/getprevpdf?id=153"), "text/javascript", "utf-8");

            /*if (!url.contains(Config.BASE_URL_STORAGE) && !url.contains("http")) {
                //url = Config.BASE_URL_STORAGE + url;
                url = url;
            }
            Log.i("url", url);
            //String url = "http://103.110.43.48:8080/generatepdf/tte/getprevpdf?id=153.pdf"; */

            //https://docs.google.com/viewerng/viewer?url=http://103.110.43.48:8080/generatepdf/tte/getprevpdf?id%3D172
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setDataAndType(Uri.parse(url), "text/html");
            //intent.setDataAndType(Uri.parse("https://docs.google.com/viewerng/viewer?url=" +url), "text/html");
            //startActivity(intent);

            if (!url.contains(Config.BASE_URL_STORAGE) && !url.contains("http")) {
                url = Config.BASE_URL_STORAGE + url;
            }
            Log.i("url", url);

//            url = "https://docs.google.com/viewerng/viewer?url=" + url;

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(url), "application/pdf");
//
//            startActivity(intent);
            Intent i = new Intent(this,OpenPdf.class);
            i.putExtra("urlpdf",url);
            startActivity(i);

        } else {
            Toast.makeText(this, "Lampiran tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }

    private void reqDetailKonsep() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.KONSEP_DETAIL + idKonsep, null);
    }

    private void reqListPemeriksa() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.KONSEP_MAIL_CHECKER, null);
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (isSuccess) {
            if (method.equals(Endpoint.KONSEP_DETAIL + idKonsep)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject o = object.getJSONObject("data");

                    idKonsep = o.getString("id");
                    tvNoSurat.setText(o.getString("nomor_surat"));
                    if (o.isNull("nomor_surat")) {
                        tvNoSurat.setText("-");
                    }
                    tvStatus.setText(Config.defStatusKonsep(o.getString("status_surat")));
                    if (o.getString("status_surat").equals("1")) {
                        tvStatus.setBackgroundResource(R.drawable.shape_chip_yellow);
                    } else if (o.getString("status_surat").equals("2")) {
                        tvStatus.setBackgroundResource(R.drawable.shape_chip_yellow);
                    } else {
                        tvStatus.setBackgroundResource(R.drawable.shape_chip_green);
                    }

                    tvNoAgenda.setText(o.getString("nomor_agenda"));
                    if (o.isNull("nomor_agenda") || o.getString("nomor_agenda").equals("")) {
                        tvNoAgenda.setText("-");
                    }
                    tvTglSurat.setText(Config.defTanggal(o.getString("tgl_surat")));
                    tvPerihal.setText(o.getString("perihal"));
                    if (o.isNull("perihal") || o.getString("perihal").equals("")) {
                        tvPerihal.setText("-");
                    }
                    tvRingkasan.setText(o.getString("ringkasan"));
                    tvNamaPembuat.setText(o.getString("nama_pembuat"));
                    tvJabatanPembuat.setText(o.getString("jabatan_pembuat"));

                    //url = o.getString("lampiran");
                    url = o.getString("url_pdf");
                    tvKecepatan.setText(Config.defKecepatan(o.getString("kecepatan")));

                    JSONArray arrayTujuan = new JSONArray(o.getString("arr_penerima"));
                    String tujuan = "";
                    if (arrayTujuan.length() > 0) {
                        for (int i = 0; i < arrayTujuan.length(); i++) {
                            String index = i + 1 + ". ";
                            tujuan += index + arrayTujuan.getJSONObject(i).getString("nama_instansi") + "\n";
                        }
                        tvTujuan.setText(tujuan);
                    } else {
                        tvTujuan.setText("-");
                    }

                    JSONArray arrayTembusan = new JSONArray(o.getString("arr_tembusan"));
                    String tembusan = "";
                    if (arrayTembusan.length() > 0) {
                        for (int i = 0; i < arrayTembusan.length(); i++) {
                            String index = i + 1 + ". ";
                            tembusan += index + arrayTembusan.getJSONObject(i).getString("nama_instansi") + "\n";
                        }
                        tvTembusan.setText(tembusan);
                    } else {
                        tvTembusan.setText("-");
                    }

                    JSONArray arrayPemeriksa = o.getJSONArray("pemeriksa_");
                    String pemeriksa = "";
                    pemeriksas.clear();
                    for (int i = 0; i < arrayPemeriksa.length(); i++) {
                        String index = i + 1 + ". ";
                        pemeriksa += index + arrayPemeriksa.getJSONObject(i).getString("nama_pemeriksa") + "\n";
                        Konsep konsep = new Konsep();
                        konsep.setNama_pemeriksa(arrayPemeriksa.getJSONObject(i).getString("nama_pemeriksa"));
                        konsep.setStatus_surat(arrayPemeriksa.getJSONObject(i).getString("status"));
                        pemeriksas.add(konsep);
                    }
                    pemeriksaAdapter.notifyDataSetChanged();
                    tvPemeriksa.setText(pemeriksa);

                    // NEW CODE - NEW CODE

                    tvKaitanSurat.setText(o.getString("dari_surat_masuk"));
                    if (/*o.getInt("dari_surat_masuk") == 0 || */!o.getString("dari_surat_masuk").equals("undefined") || o.getString("dari_surat_masuk").equals(null) ) {
                        tvKaitanSurat.setText("-");
                    } else {
                        String surat = "";
                        surat += "<b>" + o.getJSONObject("surat_masuk_").getString("nomor_surat") + "</b>" + "<br>";
                        surat += "Pengirim : " + o.getJSONObject("surat_masuk_").getString("nama_pengirim") + "<br>";
                        surat += o.getJSONObject("surat_masuk_").getString("jabatan_pengirim") + "<br>";
                        surat += Config.defTanggal(o.getJSONObject("surat_masuk_").getString("tgl_surat"));
                        tvKaitanSurat.setText(Html.fromHtml(surat));
                    }

                    tvIsi.setText(Config.formatHtml(o.getString("isi_surat")));
                    tvIsi.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tvIsi.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            if (tvIsi.getLineCount() <= 2) {
                                showMore2.setVisibility(View.GONE);
                            }
                        }
                    });

                    if (o.getString("lampiran").contains(".jpg") || o.getString("lampiran").contains(".png")) {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_collections_blue_24dp));
                    } else {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_in_page_blue_24dp));
                    }

                    tvKlasifikasi.setText(o.getJSONObject("klasifikasi_").getString("nama"));
                    tvKeamanan.setText(Config.defKeamanan(o.getString("keamanan")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.KONSEP_MAIL_CHECKER)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("data");
                    userPemeriksas.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        User user = new User();
                        user.setNip(o.getString("nip"));
                        user.setNama_lengkap(o.getString("nama_lengkap"));
                        userPemeriksas.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.contains(Endpoint.KONSEP_CHECKER)) {
                Toast.makeText(this, "Berhasil menambah pemeriksa", Toast.LENGTH_LONG).show();
                reqDetailKonsep();
            } else if (method.contains(Endpoint.KONSEP_CHECKING)) {
                try {
                    JSONObject object = new JSONObject(data);
                    Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();
                    reqDetailKonsep();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onClick(String id, String data, View view) {

    }
}
