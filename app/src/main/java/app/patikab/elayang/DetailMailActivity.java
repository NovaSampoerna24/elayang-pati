package app.patikab.elayang;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import app.patikab.elayang.adapter.ListDispositionAdapter;
import app.patikab.elayang.model.Disposition;
import app.patikab.elayang.model.Mail;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.Pref;
import app.patikab.elayang.util.RequestHandler;

import android.webkit.WebView; // memasukan class WebView
import android.webkit.WebViewClient; //memasukan class WebViewClient

public class DetailMailActivity extends AppCompatActivity
        implements RequestHandler.IParse, ListDispositionAdapter.IDispositionAdapter {

    private WebView webView;

    @BindView(R.id.linear1)
    LinearLayout line1;
    @BindView(R.id.linear2)
    LinearLayout line2;
    @BindView(R.id.showMore1)
    TextView showMore1;
    @BindView(R.id.showMore2)
    TextView showMore2;

    @BindView(R.id.linearPengirim)
    LinearLayout lnPengirim;
    @BindView(R.id.linearPenerima)
    LinearLayout lnPenerima;

    @BindView(R.id.textViewNoSurat)
    TextView tvNoSurat;
    @BindView(R.id.textViewNoAgenda)
    TextView tvNoAgenda;
    @BindView(R.id.textViewTglSurat)
    TextView tvTglSurat;
    @BindView(R.id.textViewTglTerima)
    TextView tvTglTerima;
    @BindView(R.id.textViewKlasifikasi)
    TextView tvKlasifikasi;
    @BindView(R.id.textViewPerihal)
    TextView tvPerihal;

    @BindView(R.id.textViewPengirim)
    TextView tvPengirim;
    @BindView(R.id.textViewJabatanPengirim)
    TextView tvJabPengirim;
    @BindView(R.id.textViewAlamatPengirim)
    TextView tvAlPengirim;

    @BindView(R.id.textViewPenerima)
    TextView tvPenerima;
    @BindView(R.id.textViewInstansi)
    TextView tvInstansi;

    @BindView(R.id.textViewRingakas)
    TextView tvRingkasan;
    @BindView(R.id.textViewIsi)
    TextView tvIsi;

    @BindView(R.id.textViewPelaksana)
    TextView tvPelaksana;
    @BindView(R.id.textViewJabatanPelaksana)
    TextView tvJabatanPelaksana;
    @BindView(R.id.textViewKecepatan)
    TextView tvKecepatan;
    @BindView(R.id.textViewKeamanan)
    TextView tvKeamanan;
    @BindView(R.id.textViewPindaian)
    ImageView ivPindaian;

    @BindView(R.id.recyclerViewDispo)
    RecyclerView rvDispo;

    ListDispositionAdapter dispositionAdapter;
    List<Disposition> dispositions = new ArrayList<>();


    @BindView(R.id.vCover)
    View vCover;
    @BindView(R.id.fabExpand)
    FloatingActionsMenu fabExpand;
    @BindView(R.id.frameContainer)
    FrameLayout container;

    String idSurat;
    String url;
    String url_lampiran;

    Mail mail = new Mail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mail);
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

        idSurat = getIntent().getStringExtra(Config.INTENT_ID_MAIL);

        dispositionAdapter = new ListDispositionAdapter(this, this, dispositions);
        rvDispo.setLayoutManager(new LinearLayoutManager(this));
        rvDispo.setAdapter(dispositionAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            line1.getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
            line2.getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        showMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lnPengirim.getVisibility() == View.GONE) {
                    showMore1.setText("Show Less");
                    lnPengirim.setVisibility(View.VISIBLE);
                    lnPenerima.setVisibility(View.VISIBLE);
                } else {
                    showMore1.setText("Show More");
                    lnPengirim.setVisibility(View.GONE);
                    lnPenerima.setVisibility(View.GONE);
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

        fabExpand.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                vCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                vCover.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.fabDisposition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                startActivity(new Intent(DetailMailActivity.this, DispositionActivity.class)
                        .putExtra(Config.CONTENT_TYPE, Config.INBOX)
                        .putExtra(Config.INTENT_DATA, new Gson().toJson(mail))
                        .putExtra(Config.INTENT_ID_MAIL, idSurat)
                );
            }
        });

        findViewById(R.id.fabComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                startActivity(new Intent(DetailMailActivity.this, CommentActivity.class)
                        .putExtra(Config.CONTENT_TYPE, Config.INBOX)
                        .putExtra(Config.INTENT_ID_MAIL, idSurat)
                );
            }
        });

        findViewById(R.id.fabFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                openFileLampiran();
            }
        });

        ivPindaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openFile(); }
        });

        SharedPreferences preferences = getSharedPreferences(Pref.DATA_USER, 0);
        if (preferences.getString(Pref.LEVEL, "").equals("3")) {
            findViewById(R.id.fabDisposition).setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        reqDetailMail();
        reqDispoHistory();
    }

    private void reqDetailMail() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.SURAT_MASUK_DETAIL + idSurat, null);
    }

    private void reqDispoHistory() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.DISPOSISI_LIST_BYMAIL + idSurat, null);
    }

    private WebView view; //membuat variabel view agar bisa akses method

    private void openFile() {
        if (!url.equals("") && !url.equals(null) && !url.equalsIgnoreCase("undefined")) {

            /*setContentView(R.layout.activity_webview);
            view = (WebView) this.findViewById(R.id.webviewid);
            view.getSettings().setJavaScriptEnabled(true);
            view.setWebViewClient(new MyBrowser());
            //view.loadUrl("http://103.110.43.48:8080/generatepdf/tte/preview?id=142");
            //view.loadUrl("<iframe style='width:100%; height:100%; overflow:none;' src=;http://103.110.43.48:8080/generatepdf/tte/preview?id=142'></iframe>");*/

            if (!url.contains(Config.BASE_URL_STORAGE) && !url.contains("http")) {
                url = Config.BASE_URL_STORAGE + url;
            }
            Log.i("url", url);

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

    private void openFileLampiran() {
        if (!url_lampiran.equals("") && !url.equals(null) && !url.equals("undefined")) {
            if (!url_lampiran.contains(Config.BASE_URL_STORAGE) && !url_lampiran.contains("http")) {
                url_lampiran = Config.BASE_URL_STORAGE + url_lampiran;
            }
            Log.i("url_lampiran", url_lampiran);

            Intent i = new Intent(this,OpenPdf.class);
            i.putExtra("urlpdf",url_lampiran);
            startActivity(i);

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_lampiran));
//            startActivity(intent);
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
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (isSuccess) {
            if (method.contains(Endpoint.SURAT_MASUK_DETAIL)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject o = object.getJSONObject("data");

                    tvNoSurat.setText(o.getString("nomor_surat"));
                    tvNoAgenda.setText(o.getString("nomor_agenda"));

                    if  (o.getString("tgl_terima").equals("") || o.getString("tgl_terima").equals("null")) {
                        tvTglTerima.setText("-");
                    } else {
                        tvTglTerima.setText(Config.defTanggal(o.getString("tgl_terima")));
                    }

                    tvTglSurat.setText(Config.defTanggal(o.getString("tgl_surat")));

                    tvPerihal.setText(o.getString("perihal"));
                    tvRingkasan.setText(o.getString("ringkasan"));

                    if (o.getString("lampiran").contains(".jpg") || o.getString("lampiran").contains(".png")) {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_collections_blue_24dp));
                    } else {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_in_page_blue_24dp));
                    }

                    url = o.getString("url_pdf");
                    url_lampiran = o.getString("lampiran");


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

                    tvPelaksana.setText(o.getString("nama_plt"));
                    tvJabatanPelaksana.setText(o.getString("jabatan_plt"));
                    if (o.getString("nama_plt").equals("") || o.getString("nama_plt").equals("null")) {
                        tvPelaksana.setText("-");
                        tvJabatanPelaksana.setVisibility(View.GONE);
                    }


                    tvPenerima.setText(o.getString("nama_pimpinan"));
                    if (o.getString("nama_pimpinan").isEmpty() || o.getString("nama_pimpinan").equals("null")) {
                        tvPenerima.setText("-");
                    }
                    tvInstansi.setText(o.getString("nama_instansi"));

                    tvPengirim.setText(o.getString("nama_pengirim"));
                    if  (o.getString("nama_pengirim").equals("") || o.getString("nama_pengirim").equals("null")) {
                        tvPengirim.setText("-");
                    }

                    tvJabPengirim.setText(o.getString("jabatan_pengirim"));

                    tvAlPengirim.setText(o.getString("alamat_pengirim"));
                    if  (o.getString("alamat_pengirim").equals("") || o.getString("alamat_pengirim").equals("null")) {
                        tvAlPengirim.setText("-");
                    }

                    tvKlasifikasi.setText(o.getJSONObject("klasifikasi_").getString("nama"));

                    if  (o.getString("tgl_terima").equals("") || o.getString("tgl_terima").equals("null")) {
                        tvKeamanan.setText("-");
                    } else {
                        tvKeamanan.setText(Config.defKeamanan(o.getString("keamanan")));
                    }

                    tvKecepatan.setText(Config.defKecepatan(o.getString("kecepatan")));

                    mail.setTgl_terima(o.getString("tgl_terima"));
                    mail.setPerihal(o.getString("perihal"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.contains(Endpoint.DISPOSISI_LIST_BYMAIL)) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray array = jsonObject.getJSONArray("data");

                    dispositions.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Disposition disposition = new Disposition();
                        disposition.setId(o.getString("id"));
                        disposition.setNama_penerima(o.getString("nama_penerima"));
                        disposition.setNama_pengirim(o.getString("nama_pengirim"));
                        disposition.setTgl_disposisi(o.getString("tgl_disposisi"));
                        disposition.setTgl_baca(o.getString("tgl_baca"));
                        disposition.setTgl_selesai(o.getString("tgl_selesai"));
                        disposition.setStatus(o.getString("status"));
                        dispositions.add(disposition);
                    }

                    dispositionAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClickInstruksi(String id, String data, View view) {

    }
}
