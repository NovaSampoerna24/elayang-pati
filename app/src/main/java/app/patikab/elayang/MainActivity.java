package app.patikab.elayang;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import app.patikab.elayang.adapter.ListContactAdapter;
import app.patikab.elayang.adapter.ListContactPenerimaAdapter;
import app.patikab.elayang.adapter.ListInstructionAdapter;
import app.patikab.elayang.adapter.ListPltAdapter;
import app.patikab.elayang.jsonReq.AddDisposisi;
import app.patikab.elayang.model.Plt;
import app.patikab.elayang.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import app.patikab.elayang.jsonReq.DeviceInfo;
import app.patikab.elayang.jsonReq.EditProfile;
import app.patikab.elayang.jsonReq.ListNotif;
import app.patikab.elayang.jsonReq.SetFirebase;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.Pref;
import app.patikab.elayang.util.RequestHandler;

public class MainActivity extends AppCompatActivity implements RequestHandler.IParse, ListPltAdapter.IpltAdapter {

    private static final int RESULT_LOAD_IMG = 22;
    RequestHandler handler;

    @BindView(R.id.imageViewProfile)
    ImageView ivProfile;
    @BindView(R.id.textViewName)
    TextView tvName;
    @BindView(R.id.textViewNip)
    TextView tvNip;
    @BindView(R.id.textViewJabatan)
    TextView tvJabatan;
    @BindView(R.id.textTotalArchive)
    TextView tvArchive;
    @BindView(R.id.textViewTotalInbox)
    TextView tvInbox;
    @BindView(R.id.textViewTotalOutbox)
    TextView tvOutbox;
    @BindView(R.id.textViewTotalDisIn)
    TextView tvDisIn;
    @BindView(R.id.textViewTotalDisOut)
    TextView tvDisOut;

    @BindView(R.id.textViewUnreadInbox)
    TextView tvUnInbox;
    @BindView(R.id.textViewUnreadDisIn)
    TextView tvUnDisIn;
    @BindView(R.id.textViewUnreadDiOut)
    TextView tvUnDisOut;
    @BindView(R.id.textViewUnreadKonsep)
    TextView tvUnKonsep;
    @BindView(R.id.textViewUnreadMailOut)
    TextView tvUnOutbox;
    @BindView(R.id.tvUnreadTte)
    TextView tvTte;
    @BindView(R.id.layoutTte)
    LinearLayout layoutte;

    @BindView(R.id.txtViewPLT)
    TextView txtPLT;

    RecyclerView rvplt;
    EditText etIsi;

    ListPltAdapter listPltAdapter;

    BottomSheetDialog dialogPlt;
    List<Plt> pltList = new ArrayList<>();

    @BindView(R.id.imageNotification)
    ImageView ivNotif;

    TextView tvNotif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getSupportActionBar().setElevation(0);
//        }

        handler = new RequestHandler(this, this);


        findViewById(R.id.layoutInbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.INBOX));
            }
        });

        findViewById(R.id.layoutDisIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.DISIN));
            }
        });

        findViewById(R.id.layoutDisOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.DISOUT));
            }
        });

        findViewById(R.id.linearInbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.INBOX));
            }
        });

        findViewById(R.id.linearOutbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.OUTBOX));
            }
        });

        findViewById(R.id.linearDisIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.DISIN));
            }
        });

        findViewById(R.id.linearDisOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.DISOUT));
            }
        });

        findViewById(R.id.layoutKonsep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.KONSEP));
            }
        });

        findViewById(R.id.layoutOubox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.OUTBOX));
            }
        });
        findViewById(R.id.layoutTte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class)
                        .putExtra(Config.INTENT_TYPE, Config.TTE));
            }
        });

        ivNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        findViewById(R.id.imageViewCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(MainActivity.this);
            }
        });
        findViewById(R.id.txtViewPLT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlt.show();
            }
        });

//        BadgeFactory.createDot(this)
//                .setTextColor(Color.WHITE)
//                .setWidthAndHeight(7,7)
//                .setBadgeBackground(Color.parseColor("#f44336"))
//                .setBadgeGravity(Gravity.END | Gravity.TOP)
//                .setShape(BadgeView.SHAPE_CIRCLE)
//                .bind(ivNotif);
        dialogPlt = new BottomSheetDialog(this);
        listPltAdapter = new ListPltAdapter(this,this,pltList);
        listPlt();
        initBottomPLTDialog();
        reqProfile();
        reqStatistic();
        reqSetFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reqUnread();
        reqNotif();
        fillData();
    }

    private void fillData() {
        SharedPreferences preferences = getSharedPreferences(Pref.DATA_USER, 0);
        try {
            JSONObject object = new JSONObject(preferences.getString(Pref.JSON_PROFILE, "{}"));

            tvName.setText(object.getString("nama_lengkap"));
            tvNip.setText(object.getString("nip"));
            tvJabatan.setText(object.getString("nama_jabatan"));

            Glide.with(this)
                    .load(Config.BASE_URL_PICTURES + object.getString("foto"))
                    .apply(RequestOptions.circleCropTransform().error(R.drawable.lg_pati_round))
                    .into(ivProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reqProfile() {
        handler.request(Request.Method.GET, Endpoint.PROFILE_DETAIL, null);
    }

    private void reqStatistic() {
        handler.request(Request.Method.GET, Endpoint.STATISTIK_TOTAL, null);
    }

    private void reqUnread() {
        handler.request(Request.Method.GET, Endpoint.STATISTIK_UNREAD, null);
    }

    private void reqNotif() {
        ListNotif listNotif = new ListNotif();
        listNotif.setLimit("1");
        listNotif.setPage("1");
        handler.request(Request.Method.GET, Endpoint.LIST_NOTIF, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notif);

        View actionView = MenuItemCompat.getActionView(menuItem);
        tvNotif = actionView.findViewById(R.id.cart_badge);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            reqProfile();
            reqStatistic();
            reqUnread();
        } else if (id == R.id.action_logout) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle("Konfirmasi");
            dialog.setMessage("Anda yakin ingin logout?");
            dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
//                    unset notifikasi
                    doLogout();
                }
            }).setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();
        } else if (id == R.id.action_notif) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    private void listPlt(){
            RequestHandler handler = new RequestHandler(this,this);
            handler.request(Request.Method.POST,Endpoint.PLT_LIST,null);
    }

    private void doLogout() {
        SetFirebase setFirebase = new SetFirebase();
        setFirebase.setFirebase_device("App");

        String request = new Gson().toJson(setFirebase);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.UNSET_FIREBASE, new JSONObject(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reqSetFirebase() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDevice_id(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        deviceInfo.setVersion(String.valueOf(Build.VERSION.SDK_INT));
        deviceInfo.setDevice(Build.BRAND);
        deviceInfo.setModel(Build.MODEL);
        deviceInfo.setManufacturer(Build.MANUFACTURER);

        Log.i("device info", new Gson().toJson(deviceInfo));

        SetFirebase setFirebase = new SetFirebase();
        setFirebase.setFirebase_device("App");
        setFirebase.setFirebase_token(FirebaseInstanceId.getInstance().getToken());
        setFirebase.setFirebase_info(new Gson().toJson(deviceInfo));

        String request = new Gson().toJson(setFirebase);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.SET_FIREBASE, new JSONObject(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if (isSuccess) {
            if (method.equals(Endpoint.PROFILE_DETAIL)) {
                try {
                    JSONObject object = new JSONObject(data);

                    JSONObject dataJson = object.getJSONObject("data");

                    tvName.setText(dataJson.getString("nama_lengkap"));
                    tvNip.setText(dataJson.getString("nip"));
                    tvJabatan.setText(dataJson.getString("nama_jabatan"));

                    Glide.with(this)
                            .load(Config.BASE_URL_STORAGE + dataJson.getString("foto"))
                            .apply(RequestOptions.circleCropTransform().error(R.drawable.lg_pati_round))
                            .into(ivProfile);


                    SharedPreferences preferences = getSharedPreferences(Pref.DATA_USER, 0);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(Pref.JSON_PROFILE, dataJson.toString());
                    editor.putString(Pref.NIP, dataJson.getString("nip"));
                    editor.putString(Pref.LEVEL, dataJson.getString("level"));
                    editor.apply();
                    String level = dataJson.getString("level");
                    if(level.equals("2")){
                        layoutte.setVisibility(View.VISIBLE);
                    }else{
                        layoutte.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.STATISTIK_TOTAL)) {
                try {
                    JSONObject object = new JSONObject(data);

                    JSONArray dataJson = object.getJSONArray("data");

                    int totalSurat = 0;
                    for (int i = 0; i < dataJson.length(); i++) {
                        JSONObject o = dataJson.getJSONObject(i);

                        if (o.getString("nama").equals("Surat Masuk")) {
                            totalSurat += o.getInt("total");
                            tvInbox.setText(o.getString("total"));
                        } else if (o.getString("nama").equals("Disposisi Masuk")) {
                            totalSurat += o.getInt("total");
                            tvDisIn.setText(o.getString("total"));
                        } else if (o.getString("nama").equals("Disposisi Keluar")) {
                            tvDisOut.setText(o.getString("total"));
                        } else if (o.getString("nama").equals("Surat Keluar")) {
                            tvOutbox.setText(o.getString("total"));
                            totalSurat += o.getInt("total");
                        } else if (o.getString("nama").equals("Konsep Surat")) {
                            tvUnKonsep.setText((o.getString("total") + " New"));
                        }else if(o.getString("nama").equals("Tanda Tangan")){
                            tvTte.setText((o.getString("total")+" New"));
                        }
                    }

                    tvArchive.setText(String.valueOf(totalSurat));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.STATISTIK_UNREAD)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray dataJson = object.getJSONArray("data");

                    for (int i = 0; i < dataJson.length(); i++) {
                        JSONObject o = dataJson.getJSONObject(i);
                        if (o.getString("nama").equals(Config.INBOX)) {
                            tvUnInbox.setText((o.getString("total") + " New"));
                        } else if (o.getString("nama").equals(Config.DISPO)) {
                            tvUnDisIn.setText((o.getString("total") + " New"));
                            tvUnDisOut.setText(("0 New"));
                        } else if (o.getString("nama").equals(Config.OUTBOX)) {
                            tvUnOutbox.setText((o.getString("total") + " New"));
                        }
//                        else if (o.getString("nama").equals(Config.KONSEP)) {
//                            tvUnKonsep.setText((o.getString("total") + " New"));
//                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.UNSET_FIREBASE)) {
                SharedPreferences preferencesSys = getSharedPreferences(Pref.DATA_SYS, 0);
                SharedPreferences.Editor editorSys = preferencesSys.edit();
                editorSys.putBoolean(Pref.IS_LOGIN, false);
                editorSys.apply();

                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else if (method.equals(Endpoint.LIST_NOTIF)) {
                try {
                    JSONObject object = new JSONObject(data);
                    tvNotif.setText(object.getString("message"));
                    if (object.getString("message").equals("") || object.getString("message").equals("0")
                            || object.getString("message").equals("null")) {
                        tvNotif.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.PROFILE_EDIT)) {
                reqProfile();
            }else if(method.equals(Endpoint.PLT_LIST)){
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Plt pltne = new Plt();
                        pltne.setTblpltjabatan_nip(o.getString("tblpltjabatan_nip"));
                        pltne.setTblpltjabatan_instansiplt(o.getString("tblpltjabatan_kdlokasiplt"));
                        pltne.setNmjabatan(o.getString("nmjabatan"));
                        pltne.setNmlokasi(o.getString("nmlokasi"));
                        pltList.add(pltne);
                    }
                    listPltAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void uploadImage(File file) {

        RequestParams params = new RequestParams();
        try {
            params.put("file", file, "image/jpeg");
            params.setForceMultipartEntityContentType(true);
            params.setUseJsonStreamer(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " +
                getSharedPreferences(Pref.DATA_USER, 0)
                        .getString(Pref.TOKEN, null));
        client.post(Config.BASE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Berhasil Upload Foto", Toast.LENGTH_SHORT).show();
                try {
                    String str = new String(responseBody, "UTF-8");
                    reqEditProfile(str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void reqEditProfile(String str) {
        EditProfile editProfile = new EditProfile();
        editProfile.setFoto(str);

        String jsonReq = new Gson().toJson(editProfile);

        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.PUT, Endpoint.PROFILE_EDIT, new JSONObject(jsonReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult resultImg = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = resultImg.getUri();
                uploadImage(new File(resultUri.getPath()));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultImg.getError();
                Log.i("cropError", error.getMessage());
            }
        }
    }
    private void initBottomPLTDialog() {
        View sheetView = null;

//        if (i == 1)
        sheetView = getLayoutInflater().inflate(R.layout.content_plt, null);
        rvplt = sheetView.findViewById(R.id.recyclerViewPlt);
        rvplt.setLayoutManager(new LinearLayoutManager(this));
        rvplt.setAdapter(listPltAdapter);



        final EditText editText = sheetView.findViewById(R.id.editTextQuery);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sheetView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlt.dismiss();
            }
        });
        dialogPlt.setContentView(sheetView);



    }

    @Override
    public void onClickInstruksi(String id, String data, View view) {
        String nip = id;
        String kode_jabatan_plt = "";

        try {
            JSONObject object = new JSONObject(data);
            kode_jabatan_plt = object.getString("tblpltjabatan_kdlokasiplt");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("send-plt","nip:"+nip+", kode_plt:"+kode_jabatan_plt);
    }
}
