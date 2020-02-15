package app.patikab.elayang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.jsonReq.DeviceInfo;
import app.patikab.elayang.jsonReq.Login;
import app.patikab.elayang.jsonReq.SetFirebase;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.Pref;
import app.patikab.elayang.util.RequestHandler;

public class LoginActivity extends AppCompatActivity implements RequestHandler.IParse {

    @BindView(R.id.editTextNip)
    EditText etNip;

    @BindView(R.id.editTextPassword)
    EditText etPassword;

    @BindView(R.id.buttonSignIn)
    Button btnSignIn;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textViewVersion)
    TextView tvVersion;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqLogin();
            }
        });

        findViewById(R.id.textViewForgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPw();
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText((getString(R.string.app_name) + " v" + version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        requestPermission();
    }

    private void forgotPw() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Info")
                .setMessage("Silakan hubungi Admin OPD untuk melakukan reset password. Terima Kasih")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(R.drawable.lg_app)
                .show();
    }

    private void reqLogin() {
        if (valid()) {
            btnSignIn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Login jsonLogin = new Login();
            jsonLogin.setNip(etNip.getText().toString());
            jsonLogin.setPassword(etPassword.getText().toString());

            String request = new Gson().toJson(jsonLogin);

            try {
                RequestHandler handler = new RequestHandler(this, this);
                handler.request(Request.Method.POST, Endpoint.GET_TOKEN, new JSONObject(request));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean valid() {
        Boolean isValid = true;
        if (etNip.getText().toString().isEmpty()) {
            Config.setErrorEmpty(etNip);
            isValid = false;
            etNip.requestFocus();
        } else if (etPassword.getText().toString().isEmpty()) {
            Config.setErrorEmpty(etPassword);
            isValid = false;
            etPassword.requestFocus();
        }
        return isValid;

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
        btnSignIn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (method.equals(Endpoint.GET_TOKEN)) {
                try {
                    JSONObject object = new JSONObject(data);

                    SharedPreferences preferences = getSharedPreferences(Pref.DATA_USER, 0);
                    SharedPreferences.Editor editor = preferences.edit();

                    JSONObject dataJson = object.getJSONObject("data");
                    editor.putString(Pref.TOKEN, dataJson.getString("token"));
                    editor.apply();

                    reqSetFirebase();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.SET_FIREBASE)) {
                SharedPreferences preferencesSys = getSharedPreferences(Pref.DATA_SYS, 0);
                SharedPreferences.Editor editorSys = preferencesSys.edit();
                editorSys.putBoolean(Pref.IS_LOGIN, true);
                editorSys.apply();
                startActivity(new Intent(this, MainActivity.class));

                finish();
            }
        }
    }

    private void requestPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


}
