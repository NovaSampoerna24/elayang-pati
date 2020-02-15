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
import app.patikab.elayang.DetailKonsepActivity;
import app.patikab.elayang.R;
import app.patikab.elayang.adapter.ListKonsepAdapter;
import app.patikab.elayang.jsonReq.ListKonsep;
import app.patikab.elayang.model.Konsep;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class KonsepFragment extends Fragment
        implements RequestHandler.IParse, ListKonsepAdapter.IKonsepAdapter {

    @BindView(R.id.recyclerViewKonsep)
    RecyclerView rvKonsep;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRef;

    ListKonsepAdapter konsepAdapter;
    List<Konsep> konseps = new ArrayList<>();

    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    int page = 1;

    String gKeyword = null;

    BottomSheetDialog dialogSearch;

    public KonsepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_konsep, container, false);
        ButterKnife.bind(this, v);

        konsepAdapter = new ListKonsepAdapter(getActivity(), this, konseps);
        final LinearLayoutManager lnManager = new LinearLayoutManager(getActivity());
        rvKonsep.setLayoutManager(lnManager);
        rvKonsep.setAdapter(konsepAdapter);

        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                konseps.clear();
                konsepAdapter.notifyDataSetChanged();
                page = 1;
                reqDataKonsep(gKeyword);
            }
        });

        rvKonsep.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = lnManager.getChildCount();
                    totalItemCount = lnManager.getItemCount();
                    pastVisiblesItems = lnManager.findFirstVisibleItemPosition();
                    if (!swipeRef.isRefreshing()) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            ++page;
                            reqDataKonsep(gKeyword);
                        }
                    }
                }
            }
        });

        reqDataKonsep(null);

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
                konseps.clear();
                konsepAdapter.notifyDataSetChanged();
                if (etKeyword.getText().toString().isEmpty()) {
                    reqDataKonsep(null);
                } else {
                    reqDataKonsep(etKeyword.getText().toString());
                }

                dialogSearch.dismiss();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konseps.clear();
                konsepAdapter.notifyDataSetChanged();
                etKeyword.setText("");
                reqDataKonsep(null);
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

    private void reqDataKonsep(String keyword) {
        swipeRef.setRefreshing(true);
        ListKonsep listKonsep = new ListKonsep();
        listKonsep.setLimit("100");
        listKonsep.setPage("1");
        if (keyword != null) {
            listKonsep.setKeyword(keyword);
        }
        gKeyword = keyword;

        String jsonReq = new Gson().toJson(listKonsep);

        try {
            RequestHandler handler = new RequestHandler(getActivity(), this);
            handler.request(Request.Method.GET, Endpoint.KONSEP_LIST, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        swipeRef.setRefreshing(false);
        if (isSuccess) {
            if (method.equals(Endpoint.KONSEP_LIST)) {
                try {
                    JSONObject object = new JSONObject(data).getJSONObject("data");
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() >= 1) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            Konsep konsep = new Konsep();
                            konsep.setId(o.getString("id"));
                            konsep.setTgl(o.getString("tgl"));

                            konsep.setId(o.getString("id"));
                            konsep.setNomor_surat(o.getString("nomor_surat"));
                            konsep.setNomor_agenda(o.getString("nomor_agenda"));
                                konsep.setTgl_surat(o.getString("tgl_surat"));
                            konsep.setPerihal(o.getString("perihal"));
                            konsep.setNama_pembuat(o.getString("nama_pembuat"));
                            konsep.setJabatan_pembuat(o.getString("jabatan_pembuat"));
                            konsep.setNip_pembuat(o.getString("nip_pembuat"));
                            konsep.setStatus_surat(o.getString("status_surat"));

                            konseps.add(konsep);
                        }

                        konsepAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(String id, String data, View view) {
        startActivity(new Intent(getActivity(), DetailKonsepActivity.class)
                .putExtra(Config.CONTENT_TYPE, Config.KONSEP)
                .putExtra(Config.INTENT_ID_KONSEP, id)
                .putExtra(Config.INTENT_DATA, data)
        );
    }
}
