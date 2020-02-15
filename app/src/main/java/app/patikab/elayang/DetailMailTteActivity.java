package app.patikab.elayang;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Base64;
import java.util.List;

import app.patikab.elayang.adapter.ListManifestAdapter;
import app.patikab.elayang.adapter.ListPemeriksaAdapter;
import app.patikab.elayang.adapter.ListPengirimanAdapter;
import app.patikab.elayang.jsonReq.Tte;
import app.patikab.elayang.jsonReq.UpdateResi;
import app.patikab.elayang.model.Konsep;
import app.patikab.elayang.model.Kurir;
import app.patikab.elayang.model.Manifest;
import app.patikab.elayang.model.Pengiriman;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler2;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMailTteActivity extends AppCompatActivity
        implements RequestHandler2.IParse, ListPemeriksaAdapter.IPemeriksaAdapter, ListPengirimanAdapter.IPengirimanAdapter {

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
    @BindView(R.id.recyclerViewPengiriman)
    RecyclerView rvPengiriman;
    @BindView(R.id.fabTte)
    FloatingActionButton fabtte;

    String idMail;
    String url;
    String url_lampiran;
    @BindView(R.id.fabExpand)
    FloatingActionsMenu fabExpand;

    List<Konsep> pemeriksas = new ArrayList<>();
    ListPemeriksaAdapter pemeriksaAdapter;

    ListPengirimanAdapter pengirimanAdapter;
    List<Pengiriman> pengirimans = new ArrayList<>();
    List<Kurir> kurirs = new ArrayList<>();

    ListManifestAdapter manifestAdapter;
    List<Manifest> manifests = new ArrayList<>();

    AlertDialog.Builder dialog, dialog2;
    LayoutInflater inflater;
    View dialogView;
    EditText editTextkodette;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mail_tte);
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

        idMail = getIntent().getStringExtra(Config.INTENT_ID_MAIL);
        pemeriksaAdapter = new ListPemeriksaAdapter(this, this, pemeriksas);
        rvPemeriksa.setLayoutManager(new LinearLayoutManager(this));
        rvPemeriksa.setAdapter(pemeriksaAdapter);

        pengirimanAdapter = new ListPengirimanAdapter(this, this, pengirimans);
        rvPengiriman.setLayoutManager(new LinearLayoutManager(this));
        rvPengiriman.setAdapter(pengirimanAdapter);


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
                openFileLampiran();
            }
        });
        findViewById(R.id.fabTte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabExpand.collapse();
                openDialog();
            }
        });

        ivPindaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        reqDetailMail();
        reqListKurir();
    }


    private void openDialog() {
        dialog = new AlertDialog.Builder(DetailMailTteActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);

        editTextkodette = (EditText) dialogView.findViewById(R.id.txt_kode_tte);


        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kode = editTextkodette.getText().toString();
//                Toast.makeText(DetailMailTteActivity.this, kode, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                postTte(kode, idMail);
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openFile() {
        if (url.isEmpty()) {
            Toast.makeText(this, "Lampiran Tidak Tersedia", Toast.LENGTH_SHORT).show();
        } else {
            if (!url.equals("") && !url.equals(null) && !url.equals("undefined")) {
                if (!url.contains(Config.BASE_URL_STORAGE) && !url.contains("http")) {
                    url = Config.BASE_URL_STORAGE + url;
                }
                Log.i("url", url);


//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(url), "application/pdf");
//
//            startActivity(intent);
                Intent i = new Intent(this, OpenPdf.class);
                i.putExtra("urlpdf", url);
                startActivity(i);
            } else {
                Toast.makeText(this, "Lampiran tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileLampiran() {
        if (!url_lampiran.equals("") && !url.equals(null) && !url.equals("undefined")) {
            if (!url_lampiran.contains(Config.BASE_URL_STORAGE) && !url_lampiran.contains("http")) {
                url_lampiran = Config.BASE_URL_STORAGE + url_lampiran;
            }
            Log.i("url_lampiran", url_lampiran);


//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_lampiran));
//            startActivity(intent);
            Intent i = new Intent(this, OpenPdf.class);
            i.putExtra("urlpdf", url_lampiran);
            startActivity(i);
        } else {
            Toast.makeText(this, "Lampiran tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void reqDetailMail() {
        RequestHandler2 handler = new RequestHandler2(this, this);
        handler.request(Request.Method.GET, Endpoint.TTE_SURAT_DETAIL + idMail, null);
    }

    private void reqListKurir() {
        RequestHandler2 handler = new RequestHandler2(this, this);
        handler.request(Request.Method.GET, Endpoint.EKSPEDISI_LIST, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String test(String s) {
        String encoded = new String(Base64.getEncoder().encode(s.getBytes()));
        String decoded = new String(Base64.getEncoder().encode(encoded.getBytes()));
//        Log.d("btoa", s + " -> " + encoded + " -> " + decoded);
        return decoded;
    }

    void postTte(String Passphrase, String id) {
        String kodepass = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            kodepass = test(Passphrase);
//            Log.d("kodepass",kodepass);
        }
        Tte jsonTte = new Tte();
        jsonTte.setId(id);
        jsonTte.setPassphrase(kodepass);
        String request = new Gson().toJson(jsonTte);

        try {
            RequestHandler2 handler = new RequestHandler2(this, this);
            handler.request(Request.Method.POST, Endpoint.TTE_SURAT_Tandatangani + id, new JSONObject(request));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void parseResponses(Boolean isSuccess, String method, String data, String meto) {

        if (isSuccess) {
            if (meto.equals("post")) {
                if (method.equals(Endpoint.TTE_SURAT_Tandatangani + idMail)) {
                    reqDetailMail();

                    openresponse(data);
                    Log.d("respometod", "post sukses");
                }

            } else {
                Log.d("respometod", "get sukses");
                if (method.equals(Endpoint.TTE_SURAT_DETAIL + idMail)) {
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONObject o = object.getJSONObject("data");
                        idMail = o.getString("id");
                        tvNoSurat.setText(o.getString("nomor_surat"));
                        tvStatus.setText(Config.defStatusKonsep(o.getString("status_surat")));
                        if (o.getString("status_surat").equals("1")) {
                            tvStatus.setBackgroundResource(R.drawable.shape_chip_yellow);
                        } else if (o.getString("status_surat").equals("2")) {
                            tvStatus.setBackgroundResource(R.drawable.shape_chip_yellow);
                        } else {
                            Log.d("statussurat", o.getString("status_surat"));
                            if (o.getString("status_surat").equals("4")) {
                                fabtte.setVisibility(View.GONE);
                            } else {
                                fabtte.setVisibility(View.VISIBLE);
                            }
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

                        url = o.getString("lampiran");
                        url_lampiran = o.getString("lampiran");

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

                    /*JSONArray arrayTembusan = new JSONArray(o.getString("arr_tembusan"));
                    if (arrayTembusan.length() > 0) {
                        for (int i = 0; i < arrayTembusan.length(); i++) {
                            tvTembusan.setText(arrayTembusan.getString(i) + "\n");
                        }
                    } else {
                        tvTembusan.setText("-");
                    }*/

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

                        // NEW CODE
                        if (o.getString("dari_surat_masuk").equals("") || o.getString("dari_surat_masuk").equals("null")) {
                            tvKaitanSurat.setText("-");
                        } else {
                            tvKaitanSurat.setText(o.getString("dari_surat_masuk"));
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
                        tvKecepatan.setText(Config.defKecepatan(o.getString("kecepatan")));

                        JSONArray arrayPengiriman = o.getJSONArray("pengiriman_");
                        pengirimans.clear();
                        for (int i = 0; i < arrayPengiriman.length(); i++) {
                            Pengiriman pengiriman = new Pengiriman();
                            pengiriman.setId(arrayPengiriman.getJSONObject(i).getString("id"));
                            pengiriman.setNama_instansi(arrayPengiriman.getJSONObject(i).getString("nama_instansi"));
                            pengiriman.setTgl_baca(arrayPengiriman.getJSONObject(i).getString("tgl_baca"));
                            pengiriman.setKurir(arrayPengiriman.getJSONObject(i).getString("kurir"));
                            pengiriman.setResi(arrayPengiriman.getJSONObject(i).getString("resi"));
                            pengiriman.setTgl_terima(arrayPengiriman.getJSONObject(i).getString("tgl_terima"));
                            pengirimans.add(pengiriman);
                        }
                        pengirimanAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.d("error", e.toString());
                        e.printStackTrace();
                    }
                } else if (method.equals(Endpoint.EKSPEDISI_LIST)) {
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        kurirs.clear();
                        for (int i = 0; i < array.length(); i++) {
                            Kurir kurir = new Kurir();
                            kurir.setNama(array.getJSONObject(i).getString("nama"));
                            kurir.setKode(array.getJSONObject(i).getString("kode"));
                            kurirs.add(kurir);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (method.contains(Endpoint.TRACK_RESI)) {
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        manifests.clear();
                        for (int i = 0; i < array.length(); i++) {
                            Manifest manifest = new Manifest();
                            manifest.setManifest_description(array.getJSONObject(i).getString("manifest_description"));
                            manifest.setManifest_time(array.getJSONObject(i).getString("manifest_time"));
                            manifest.setManifest_date(array.getJSONObject(i).getString("manifest_date"));
                            manifests.add(manifest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final View view = getLayoutInflater().inflate(R.layout.content_manifest, null);
                    final AlertDialog.Builder builder;

                    final RecyclerView rvManifest = view.findViewById(R.id.recyclerViewManifest);
                    manifestAdapter = new ListManifestAdapter(this, manifests);
                    rvManifest.setLayoutManager(new LinearLayoutManager(this));
                    rvManifest.setAdapter(manifestAdapter);

                    builder = new AlertDialog.Builder(this);
                    builder.setView(view);
                    builder.setTitle("Lacak Pengiriman")
                            .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else if (method.contains(Endpoint.UPDATE_RESI)) {
                    Toast.makeText(this, "Berhasil mengisi resi", Toast.LENGTH_SHORT).show();
                    reqDetailMail();
                }
            }

        }

    }

    private void openresponse(String message) {
        dialog2 = new AlertDialog.Builder(DetailMailTteActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_message, null);
        dialog2.setView(dialogView);
        dialog2.setCancelable(true);
        dialog2.setIcon(R.mipmap.ic_launcher);

        TextView txtmsg = (TextView) findViewById(R.id.txt_message);
//        txtmsg.setText(message);

        dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    String kode = editTextkodette.getText().toString();
//                Toast.makeText(DetailMailTteActivity.this, kode, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
//                    postTte(kode, idMail);
            }
        });

//        dialog2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
        dialog2.show();

    }

    @Override
    public void onClick(String id, String data, View view) {

    }

    @Override
    public void onLacak(String id) {
        reqListManifest(id);
    }

    private void reqListManifest(String id) {
        RequestHandler2 handler = new RequestHandler2(this, this);
        handler.request(Request.Method.GET, Endpoint.TRACK_RESI + id, null);
    }

    @Override
    public void onIsiResi(final String id, final String kurir, final String resi) {
        final View view = getLayoutInflater().inflate(R.layout.content_isi_resi, null);
        final AlertDialog.Builder builder;
        final EditText editText = view.findViewById(R.id.editTextNoResi);
        final Spinner spinner = view.findViewById(R.id.spinnerKurir);
        ArrayAdapter<Kurir> adapter =
                new ArrayAdapter<Kurir>(this,
                        android.R.layout.simple_spinner_dropdown_item, kurirs);
        spinner.setAdapter(adapter);

        for (int i = 0; i < kurirs.size(); i++) {
            if (kurirs.get(i).getKode().equals(kurir)) {
                spinner.setSelection(i);
                break;
            }
        }
        editText.setText(resi);

        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Pengisian Resi")
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String no_resi = editText.getText().toString();
                        String kurir = kurirs.get(spinner.getSelectedItemPosition()).kode;

//                        Toast.makeText(DetailMailOutActivity.this, no_resi+kurir, Toast.LENGTH_SHORT).show();
                        reqUpdateResi(id, no_resi, kurir);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    private void reqUpdateResi(String id, String no_resi, String kurir) {
        UpdateResi updateResi = new UpdateResi();
        updateResi.setId(id);
        updateResi.setKurir(kurir);
        updateResi.setResi(no_resi);
        String jsonReq = new Gson().toJson(updateResi);

        try {
            RequestHandler2 handler = new RequestHandler2(this, this);
            handler.request(Request.Method.PUT, Endpoint.UPDATE_RESI + id, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
