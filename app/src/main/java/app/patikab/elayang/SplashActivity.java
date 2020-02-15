package app.patikab.elayang;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import app.patikab.elayang.jsonReq.Version;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.Pref;
import app.patikab.elayang.util.RequestHandler;
import butterknife.BindView;

public class SplashActivity extends AppCompatActivity implements RequestHandler.IParse {

    TextView txtbaseurl,txtversi;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    int versi;
    int update = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        txtbaseurl = (TextView)findViewById(R.id.baseurl);
        txtversi = (TextView)findViewById(R.id.txtversion);

//        txtbaseurl.setText(Config.BASE_URL);



        cekversi();

    }

    void cekversi(){
        Version jsonLogin = new Version();
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versi = info.versionCode;
            txtversi.setText("Versi "+info.versionName);
//            Toast.makeText(this,
//                    "PackageName = " + info.packageName + "\nVersionCode = "
//                            + info.versionCode + "\nVersionName = "
//                            + info.versionName + "\nPermissions = " + info.permissions, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        jsonLogin.setVersion(versi);

        String request = new Gson().toJson(jsonLogin);
        try {
            RequestHandler handler = new RequestHandler(this, this);
            handler.request(Request.Method.POST, Endpoint.GET_VERSION, new JSONObject(request));
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    private void start(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferencesSys = getSharedPreferences(Pref.DATA_SYS, 0);

                    if (preferencesSys.getBoolean(Pref.IS_LOGIN, false)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }

                // close this activity
                finish();
            }
        }, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cekversi();
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        if(isSuccess){
            if(method.equals(Endpoint.GET_VERSION)){
                try {
                    JSONObject object = new JSONObject(data);
                    String o = object.getString("message");
                    if(o.equals("null")){
                        update = 0;
                        start();
                    }else{
                        update = 1;
                        opendialogcekversi();
                    }

                    Log.d("response",String.valueOf(update));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            start();
        }
    }

    private void opendialogcekversi() {

        dialog = new AlertDialog.Builder(this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_update, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle("Pembaruan Aplikasi");
        dialog.setIcon(R.mipmap.ic_launcher);

//        editTextkodette = (EditText) dialogView.findViewById(R.id.txt_kode_tte);


        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String kode = editTextkodette.getText().toString();
//                Toast.makeText(DetailMailTteActivity.this, kode, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
//                postTte(kode, idMail);
//                start();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        dialog.setNegativeButton("Skip", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                start();

            }
        });
        dialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

