package app.patikab.elayang.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.DetailMailActivity;
import app.patikab.elayang.R;
import app.patikab.elayang.adapter.ListMailAdapter;
import app.patikab.elayang.jsonReq.ListInbox;
import app.patikab.elayang.model.Mail;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment implements ListMailAdapter.IMailAdapter, RequestHandler.IParse {

    @BindView(R.id.recyclerViewMail)
    RecyclerView rvMail;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRef;

    ListMailAdapter mailAdapter;
    List<Mail> mails = new ArrayList<>();

    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    int page = 1;

    String gKeyword = null;

    BottomSheetDialog dialogSearch;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, v);

        mailAdapter = new ListMailAdapter(getActivity(), this, mails);
        final LinearLayoutManager lnManager = new LinearLayoutManager(getActivity());
        rvMail.setLayoutManager(lnManager);
        rvMail.setAdapter(mailAdapter);

        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mails.clear();
                page = 1;
                reqDataMail(gKeyword);
            }
        });

        rvMail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = lnManager.getChildCount();
                    totalItemCount = lnManager.getItemCount();
                    pastVisiblesItems = lnManager.findFirstVisibleItemPosition();
                    if (!swipeRef.isRefreshing()) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            ++page;
                            reqDataMail(gKeyword);
                        }
                    }
                }
            }
        });

        reqDataMail(null);

        setHasOptionsMenu(true);

        initSearch();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                dialogSearch.show();
                break;
        }
        return true;
    }

    private void initSearch() {
        dialogSearch = new BottomSheetDialog(getActivity());
        View sheetView = getLayoutInflater().inflate(R.layout.content_search, null);
        final EditText etKeyword = sheetView.findViewById(R.id.editTextIsi);
        Button btnSimpan = sheetView.findViewById(R.id.buttonSearch);
        Button btnReset = sheetView.findViewById(R.id.buttonReset);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mails.clear();
                mailAdapter.notifyDataSetChanged();
                if (etKeyword.getText().toString().isEmpty()) {
                    reqDataMail(null);
                } else {
                    reqDataMail(etKeyword.getText().toString());
                }

                dialogSearch.dismiss();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mails.clear();
                mailAdapter.notifyDataSetChanged();
                etKeyword.setText("");
                reqDataMail(null);
                dialogSearch.dismiss();
            }
        });
        sheetView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSearch.dismiss();
            }
        });
        dialogSearch.setContentView(sheetView);
    }


    private void reqDataMail(String keyword) {
        swipeRef.setRefreshing(true);
        ListInbox listInbox = new ListInbox();
        listInbox.setLimit("10");
        listInbox.setPage(String.valueOf(page));
        if (keyword != null) {
            listInbox.setKeyword(keyword);
        }
        gKeyword = keyword;
        String jsonReq = new Gson().toJson(listInbox);

        try {
            RequestHandler handler = new RequestHandler(getActivity(), this);
            handler.request(Request.Method.GET, Endpoint.SURAT_MASUK_LIST, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(String id, String data, View view) {
        startActivity(new Intent(getActivity(), DetailMailActivity.class)
                .putExtra(Config.CONTENT_TYPE, Config.INBOX)
                .putExtra(Config.INTENT_ID_MAIL, id)
                .putExtra(Config.INTENT_DATA, data)
        );
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        swipeRef.setRefreshing(false);
        if (isSuccess) {
            if (method.equals(Endpoint.SURAT_MASUK_LIST)) {
                try {
                    JSONObject object = new JSONObject(data).getJSONObject("data");
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() >= 1) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            Mail mail = new Mail();
                            mail.setId(o.getString("id"));
                            mail.setNomor_surat(o.getString("nomor_surat"));
                            mail.setNomor_agenda(o.getString("nomor_agenda"));

                            mail.setTgl_terima(o.getString("tgl_terima"));
                            mail.setTgl_surat(o.getString("tgl_surat"));

                            mail.setPerihal(o.getString("perihal"));
                            mail.setRingkasan(o.getString("ringkasan"));
                            mail.setIsi_surat(o.getString("isi_surat"));
                            mail.setLampiran(o.getString("lampiran"));

                            mail.setNip_tata_usaha(o.getString("nip_tata_usaha"));
                            mail.setNama_tata_usaha(o.getString("nama_tata_usaha"));
                            mail.setJabatan_tata_usaha(o.getString("jabatan_tata_usaha"));

                            mail.setNip_plt(o.getString("nip_plt"));
                            mail.setNama_plt(o.getString("nama_plt"));
                            mail.setJabatan_plt(o.getString("jabatan_plt"));

                            mail.setNip_pimpinan(o.getString("nip_pimpinan"));
                            mail.setNama_pimpinan(o.getString("nama_pimpinan"));
                            mail.setJabatan_pimpinan(o.getString("jabatan_pimpinan"));

                            mail.setNama_pengirim(o.getString("nama_pengirim"));
                            mail.setNama_instansi(o.getString("nama_instansi"));
                            mail.setJabatan_pengirim(o.getString("jabatan_pengirim"));
                            mail.setAlamat_pengirim(o.getString("alamat_pengirim"));

                            mail.setKeamanan(o.getString("keamanan"));
                            mail.setKecepatan(o.getString("kecepatan"));

                            mail.setTgl_baca_pimpinan(o.getString("tgl_baca_pimpinan"));
                            mail.setTgl_baca_tata_usaha(o.getString("tgl_baca_tata_usaha"));
                            mail.setTgl_baca_plt(o.getString("tgl_baca_plt"));
                            mails.add(mail);
                        }
                    } else {
                        if (page == 1) {
                            Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    mailAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
