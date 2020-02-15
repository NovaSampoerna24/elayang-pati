package app.patikab.elayang;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.getbase.floatingactionbutton.FloatingActionButton;
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
import app.patikab.elayang.jsonReq.SetStatusDispo;
import app.patikab.elayang.model.Disposition;
import app.patikab.elayang.model.Mail;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.Pref;
import app.patikab.elayang.util.RequestHandler;

public class DetailDispositionActivity extends AppCompatActivity
        implements RequestHandler.IParse, ListDispositionAdapter.IDispositionAdapter {

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

    @BindView(R.id.cardStatus)
    CardView cardStatus;
    @BindView(R.id.textStatus)
    TextView textStatus;
    @BindView(R.id.textViewKetDis)
    TextView tvKetDis;
    @BindView(R.id.textViewIsiDis)
    TextView tvIsiDis;
    @BindView(R.id.textViewInstruksi)
    TextView tvInstruksiDis;
    @BindView(R.id.textViewNamaDis)
    TextView tvNamaDis;
    @BindView(R.id.textViewJabatanDis)
    TextView tvJabatanDis;

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

    @BindView(R.id.fabAccept)
    FloatingActionButton fabAccept;
    @BindView(R.id.fabReject)
    FloatingActionButton fabReject;


    String idDiposisi;
    String idSurat;
    String typeDispo;
    String url;

    Mail mail = new Mail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_disposition);
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

        setTitle("Detail " + getIntent().getStringExtra(Config.CONTENT_TYPE));

        idDiposisi = getIntent().getStringExtra(Config.INTENT_ID_DISPO);
        typeDispo = getIntent().getStringExtra(Config.CONTENT_TYPE);

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
                startActivity(new Intent(DetailDispositionActivity.this, DispositionActivity.class)
                        .putExtra(Config.CONTENT_TYPE, Config.INBOX)
                        .putExtra(Config.INTENT_DATA, new Gson().toJson(mail))
                        .putExtra(Config.INTENT_ID_DISPO, idDiposisi)
                        .putExtra(Config.INTENT_ID_MAIL, idSurat)
                );
            }
        });

        findViewById(R.id.fabComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                startActivity(new Intent(DetailDispositionActivity.this, CommentActivity.class)
                        .putExtra(Config.CONTENT_TYPE, Config.INBOX)
                        .putExtra(Config.INTENT_ID_DISPO, idDiposisi)
                );
            }
        });

        findViewById(R.id.fabReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogRej();
            }
        });

        findViewById(R.id.fabAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAcc();
            }
        });

        findViewById(R.id.fabFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabExpand.collapse();
                openFile();
            }
        });

        ivPindaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        if (getIntent().getStringExtra(Config.CONTENT_TYPE).equals(Config.DISOUT)) {
            fabAccept.setVisibility(View.GONE);
            fabReject.setVisibility(View.GONE);
        }

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
        handler.request(Request.Method.GET, Endpoint.DISPOSISI_DETAIL + idDiposisi, null);
    }

    private void reqDispoHistory() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.DISPOSISI_LIST_BYMAIL + idDiposisi, null);
    }

    private void openDialogAcc() {
        final View view = getLayoutInflater().inflate(R.layout.content_accept_dispo, null);
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Selesaikan Disposisi")
                .setPositiveButton("Selesaikan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.editTextIsi);
                        reqStatusDispo("1", editText.getText().toString());
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    private void openDialogRej() {
        final View view = getLayoutInflater().inflate(R.layout.content_accept_dispo, null);
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Tolak Disposisi")
                .setPositiveButton("Tolak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.editTextIsi);
                        reqStatusDispo("2", editText.getText().toString());
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    private void reqStatusDispo(String status, String keterangan) {
        SetStatusDispo statusDispo = new SetStatusDispo();
        statusDispo.setId(idDiposisi);
        statusDispo.setStatus(status);
        statusDispo.setKeterangan(keterangan);

        String jsonReq = new Gson().toJson(statusDispo);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.DISPOSISI_SET_STATUS + idDiposisi, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openFile() {
        if (!url.equals("") && !url.equals(null) && !url.equals("undefined")) {
            if (!url.contains(Config.BASE_URL_STORAGE) && !url.contains("http")) {
                url = Config.BASE_URL_STORAGE + url;
            }
            Log.i("url", url);

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            Intent i = new Intent(this,OpenPdf.class);
            i.putExtra("urlpdf",url);
            startActivity(i);
        } else {
            Toast.makeText(this, "Lampiran tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (isSuccess) {
            if (method.equals(Endpoint.DISPOSISI_DETAIL + idDiposisi)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject oD = object.getJSONObject("data");

                    if (!typeDispo.equals(Config.DISIN) || !oD.getString("status").equals("0")) {
                        fabAccept.setVisibility(View.GONE);
                        fabReject.setVisibility(View.GONE);
                    }
                    if (oD.getString("status").equals("1")) {
                        cardStatus.setCardBackgroundColor(Color.parseColor("#03A9F4"));
                    } else if (oD.getString("status").equals("2")) {
                        cardStatus.setCardBackgroundColor(Color.parseColor("#d32f2f"));
                    }
                    textStatus.setText(("Status : " + Config.defStatus(oD.getString("status"))));

                    if (typeDispo.equals(Config.DISIN)) {
                        tvNamaDis.setText(oD.getString("nama_pengirim"));
                        tvJabatanDis.setText(oD.getString("jabatan_pengirim"));
                        tvInstruksiDis.setText(oD.getJSONObject("instruksi_").getString("nama"));
                        tvIsiDis.setText(oD.getString("isi_disposisi"));
                        tvKetDis.setText("Didisposisikan Oleh");
                    } else if (typeDispo.equals(Config.DISOUT)) {
                        tvNamaDis.setText(oD.getString("nama_penerima"));
                        tvJabatanDis.setText(oD.getString("jabatan_penerima"));
                        tvInstruksiDis.setText(oD.getJSONObject("instruksi_").getString("nama"));
                        tvIsiDis.setText(oD.getString("isi_disposisi"));
                        tvKetDis.setText("Didispoisikan Kepada");
                    }


                    JSONObject o = object.getJSONObject("data").getJSONObject("surat_");

                    idSurat = o.getString("id");
                    tvNoSurat.setText(o.getString("nomor_surat"));
                    tvNoAgenda.setText(o.getString("nomor_agenda"));
                    tvTglTerima.setText(Config.defTanggal(o.getString("tgl_terima")));
                    tvTglSurat.setText(Config.defTanggal(o.getString("tgl_surat")));
                    tvPerihal.setText(o.getString("perihal"));
                    tvRingkasan.setText(o.getString("ringkasan"));

                    if (o.getString("lampiran").contains(".jpg") || o.getString("lampiran").contains(".png")) {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_collections_blue_24dp));
                    } else {
                        ivPindaian.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_in_page_blue_24dp));
                    }

                    url = o.getString("lampiran");
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
                    if (o.getString("nama_pimpinan").isEmpty()) {
                        tvPenerima.setText("-");
                    }
                    tvInstansi.setText(o.getString("nama_instansi"));
                    tvPengirim.setText(o.getString("nama_pengirim"));
                    tvJabPengirim.setText(o.getString("jabatan_pengirim"));
                    tvAlPengirim.setText(o.getString("alamat_pengirim"));

                    tvKlasifikasi.setText("-");
                    tvKeamanan.setText(Config.defKeamanan(o.getString("keamanan")));
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
            } else if (method.contains(Endpoint.DISPOSISI_SET_STATUS)) {
                Toast.makeText(this, "Sukses", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClickInstruksi(String id, String data, View view) {

    }

}
