package app.patikab.elayang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.adapter.ListCommentAdapter;
import app.patikab.elayang.jsonReq.AddKomentar;
import app.patikab.elayang.jsonReq.ListKomentar;
import app.patikab.elayang.model.Comment;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

public class CommentActivity extends AppCompatActivity implements ListCommentAdapter.ICommentAdapter, RequestHandler.IParse {

    @BindView(R.id.editTextKomentar)
    EditText etKomentar;
    @BindView(R.id.buttonSend)
    ImageView btnSend;
    @BindView(R.id.recyclerViewComment)
    RecyclerView rvComment;

    ListCommentAdapter commentAdapter;
    List<Comment> comments = new ArrayList<>();

    String idSurat = null;
    String idDispo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
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

        commentAdapter = new ListCommentAdapter(this, this, comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        rvComment.setLayoutManager(layoutManager);
        rvComment.setAdapter(commentAdapter);

        idSurat = getIntent().getStringExtra(Config.INTENT_ID_MAIL);
        idDispo = getIntent().getStringExtra(Config.INTENT_ID_DISPO);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etKomentar.getText().toString().isEmpty()) {
                    reqAddComment();
                }
            }
        });

        reqListComment();

    }

    private void reqListComment() {

        ListKomentar listKomentar = new ListKomentar();
        listKomentar.setId_surat_masuk(idSurat);
        listKomentar.setId_disposisi(idDispo);
        listKomentar.setPage("1");
        listKomentar.setLimit("100");

        String jsonReq = new Gson().toJson(listKomentar);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.GET, Endpoint.KOMENTAR_LIST, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reqAddComment() {

        AddKomentar addKomentar = new AddKomentar();
        addKomentar.setId_surat_masuk(idSurat);
        addKomentar.setId_disposisi(idDispo);
        addKomentar.setIsi_komentar(etKomentar.getText().toString());

        String jsonReq = new Gson().toJson(addKomentar);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.POST, Endpoint.KOMENTAR_ADD, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(String id, String data, View view) {

    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (isSuccess) {
            if (method.equals(Endpoint.KOMENTAR_LIST)) {
                try {
                    JSONObject object = new JSONObject(data);

                    JSONArray array = object.getJSONObject("data").getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        JSONObject oProfile = o.getJSONObject("pengirim_");
                        Comment comment = new Comment();
                        comment.setId(o.getString("id"));
                        comment.setIsi_komentar(o.getString("isi_komentar"));
                        comment.setTgl(o.getString("tgl"));
                        comment.setNama_lengkap(oProfile.getString("nama_lengkap"));
                        comment.setNip(oProfile.getString("nip"));
                        comments.add(comment);
                    }

                    commentAdapter.notifyDataSetChanged();
                    rvComment.scrollToPosition(comments.size() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.KOMENTAR_ADD)) {
                etKomentar.setText("");
                comments.clear();
                reqListComment();
            }
        }
    }
}
