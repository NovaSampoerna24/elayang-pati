package app.patikab.elayang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.adapter.ListNotifAdapter;
import app.patikab.elayang.jsonReq.ListNotif;
import app.patikab.elayang.model.Notification;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

public class NotificationActivity extends AppCompatActivity
        implements RequestHandler.IParse, ListNotifAdapter.INotificationAdapter {

    @BindView(R.id.recyclerViewNotif)
    RecyclerView rvNotif;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRef;

    ListNotifAdapter notifAdapter;
    List<Notification> notifications = new ArrayList<>();

    String idClicked;

    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        notifAdapter = new ListNotifAdapter(this, this, notifications);
        final LinearLayoutManager lnManager = new LinearLayoutManager(this);
        rvNotif.setLayoutManager(lnManager);
        rvNotif.setAdapter(notifAdapter);

        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifications.clear();
                notifAdapter.notifyDataSetChanged();
                page = 1;
                reqNotif();
            }
        });

        rvNotif.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = lnManager.getChildCount();
                    totalItemCount = lnManager.getItemCount();
                    pastVisiblesItems = lnManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        ++page;
                        reqNotif();
                    }
                }
            }
        });


        reqNotif();
    }

    private void reqNotif() {
        swipeRef.setRefreshing(true);
        ListNotif listNotif = new ListNotif();
        listNotif.setLimit("10");
        listNotif.setPage(String.valueOf(page));

        String jsonReq = new Gson().toJson(listNotif);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.GET, Endpoint.LIST_NOTIF, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reqReadNotif(String id) {
        idClicked = id;
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.PUT, Endpoint.READ_NOTIF + id, null);
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (swipeRef.isRefreshing()) {
            swipeRef.setRefreshing(false);
        }
        if (isSuccess) {
            if (method.equals(Endpoint.LIST_NOTIF)) {
                try {
                    JSONObject object = new JSONObject(data);

                    JSONArray array = object.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Notification notif = new Notification();
                        notif.setId(o.getString("id"));
                        notif.setIsi(o.getString("isi"));
                        notif.setStatus(o.getString("status"));
                        notif.setTanggal(o.getString("tgl"));

                        notifications.add(notif);
                    }
                    notifAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.READ_NOTIF + idClicked)) {
                notifications.clear();
                reqNotif();
                try {
                    JSONObject object = new JSONObject(data);
//                        Log.d("testtype",object.toString());
                    if (object.getString("message").contains("surat-masuk")) {
                        if (object.getString("data").toLowerCase().contains("mengomentari")) {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, CommentActivity.class)
                                    .putExtra(Config.INTENT_ID_MAIL, split[2]));
                        } else {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, DetailMailActivity.class)
                                    .putExtra(Config.INTENT_ID_MAIL, split[2]));
                        }
                    }
                    else if (object.getString("message").contains("surat-keluar")) {
                        if (object.getString("data").toLowerCase().contains("mengomentari")) {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, CommentActivity.class)
                                    .putExtra(Config.INTENT_ID_MAIL, split[2]));
                        } else {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, DetailMailActivity.class)
                                    .putExtra(Config.INTENT_ID_MAIL, split[2]));
                        }
                    } else if (object.getString("message").contains("konsep-surat")) {
                        String[] split = object.getString("message").split("/");
                        reqDetailKonsep(split[3]);
                    } else if (object.getString("message").contains("disposisi-keluar")) {
                        if (object.getString("data").toLowerCase().contains("mengomentari")) {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, CommentActivity.class)
                                    .putExtra(Config.INTENT_ID_DISPO, split[2]));
                        } else {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, DetailDispositionActivity.class)
                                    .putExtra(Config.INTENT_ID_DISPO, split[2])
                                    .putExtra(Config.CONTENT_TYPE, Config.DISOUT));
                        }
                    } else if (object.getString("message").contains("disposisi-masuk")) {
                        if (object.getString("data").toLowerCase().contains("mengomentari")) {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, CommentActivity.class)
                                    .putExtra(Config.INTENT_ID_DISPO, split[2]));
                        } else {
                            String[] split = object.getString("message").split("/");
                            startActivity(new Intent(this, DetailDispositionActivity.class)
                                    .putExtra(Config.INTENT_ID_DISPO, split[2])
                                    .putExtra(Config.CONTENT_TYPE, Config.DISIN));
                        }
                    } else if (object.getString("message").contains("disposisi-masuk")) {
                        String[] split = object.getString("message").split("/");
                        startActivity(new Intent(this, DetailDispositionActivity.class)
                                .putExtra(Config.INTENT_ID_DISPO, split[2])
                                .putExtra(Config.CONTENT_TYPE, Config.DISIN));
                    }else if(object.getString("message").contains("null")){
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.contains(Endpoint.KONSEP_DETAIL)) {

                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject o = object.getJSONObject("data");
                    startActivity(new Intent(this, DetailKonsepActivity.class)
                            .putExtra(Config.INTENT_ID_KONSEP, o.getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reqDetailKonsep(String idKonsep) {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.KONSEP_DETAIL + idKonsep, null);
    }

    @Override
    public void onClick(String id, String data, View view) {
        reqReadNotif(id);
    }
}