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
import app.patikab.elayang.DetailDispositionActivity;
import app.patikab.elayang.R;
import app.patikab.elayang.adapter.ListOutDispositionAdapter;
import app.patikab.elayang.jsonReq.ListDispo;
import app.patikab.elayang.model.Disposition;
import app.patikab.elayang.model.Mail;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisOutFragment extends Fragment
        implements ListOutDispositionAdapter.IDispositionAdapter, RequestHandler.IParse {

    @BindView(R.id.recyclerViewDispo)
    RecyclerView rvDispo;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRef;

    ListOutDispositionAdapter dispositionAdapter;
    List<Disposition> dispositions = new ArrayList<>();

    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    int page = 1;

    String gKeyword = null;

    BottomSheetDialog dialogSearch;

    public DisOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dis_out, container, false);
        ButterKnife.bind(this, v);

        dispositionAdapter = new ListOutDispositionAdapter(getActivity(), this, dispositions);
        final LinearLayoutManager lnManager = new LinearLayoutManager(getActivity());
        rvDispo.setLayoutManager(lnManager);
        rvDispo.setAdapter(dispositionAdapter);

        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dispositions.clear();
                dispositionAdapter.notifyDataSetChanged();
                page = 1;
                reqDataDispo(gKeyword);
            }
        });

        rvDispo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = lnManager.getChildCount();
                    totalItemCount = lnManager.getItemCount();
                    pastVisiblesItems = lnManager.findFirstVisibleItemPosition();
                    if (!swipeRef.isRefreshing()) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            ++page;
                            reqDataDispo(gKeyword);
                        }
                    }
                }
            }
        });


        reqDataDispo(null);

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
                dispositions.clear();
                dispositionAdapter.notifyDataSetChanged();
                if (etKeyword.getText().toString().isEmpty()) {
                    reqDataDispo(null);
                } else {
                    reqDataDispo(etKeyword.getText().toString());
                }

                dialogSearch.dismiss();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispositions.clear();
                dispositionAdapter.notifyDataSetChanged();
                etKeyword.setText("");
                reqDataDispo(null);
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

    private void reqDataDispo(String keyword) {
        swipeRef.setRefreshing(true);
        ListDispo listDispo = new ListDispo();
        listDispo.setLimit("10");
        listDispo.setPage(String.valueOf(page));
        if (keyword != null) {
            listDispo.setKeyword(keyword);
        }
        gKeyword = keyword;

        String jsonReq = new Gson().toJson(listDispo);

        try {
            RequestHandler handler = new RequestHandler(getActivity(), this);
            handler.request(Request.Method.GET, Endpoint.DISPOSISI_LIST_OUT, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        swipeRef.setRefreshing(false);
        if (isSuccess) {
            if (method.equals(Endpoint.DISPOSISI_LIST_OUT)) {
                try {
                    JSONObject object = new JSONObject(data).getJSONObject("data");
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() >= 1) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject oD = array.getJSONObject(i);
                            Disposition disposition = new Disposition();
                            disposition.setId(oD.getString("id"));
                            disposition.setTgl_baca(oD.getString("tgl_baca"));
                            disposition.setStatus(oD.getString("status"));
                            disposition.setNama_penerima(oD.getString("nama_penerima"));
                            disposition.setJabatan_penerima(oD.getString("jabatan_penerima"));

                            if (!array.getJSONObject(i).isNull("surat_")) {
                                JSONObject o = array.getJSONObject(i).getJSONObject("surat_");
                                Mail mail = new Mail();
                                mail.setId(o.getString("id"));
                                mail.setNomor_surat(o.getString("nomor_surat"));
                                mail.setNomor_agenda(o.getString("nomor_agenda"));
                                mail.setTgl_terima(o.getString("tgl_terima"));
                                mail.setTgl_surat(o.getString("tgl_surat"));
                                mail.setPerihal(o.getString("perihal"));
                                mail.setNama_pengirim(o.getString("nama_pengirim"));
                                mail.setNama_instansi(o.getString("nama_instansi"));
                                mail.setJabatan_pengirim(o.getString("jabatan_pengirim"));
                                mail.setAlamat_pengirim(o.getString("alamat_pengirim"));

                                disposition.setMail(mail);
                                dispositions.add(disposition);
                            }
                        }

                        dispositionAdapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(String id, String data, View view) {
        startActivity(new Intent(getActivity(), DetailDispositionActivity.class)
                .putExtra(Config.CONTENT_TYPE, Config.DISOUT)
                .putExtra(Config.INTENT_ID_DISPO, id)
                .putExtra(Config.INTENT_DATA, data)
        );
    }
}
