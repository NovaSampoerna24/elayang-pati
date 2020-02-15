package app.patikab.elayang.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import app.patikab.elayang.LoginActivity;


/**
 * Created by paymin on 22/08/2018.
 */

public class RequestHandler {

    Context context;
    IParse mIParse;

    public RequestHandler(Context mcontext, IParse iParse) {
        context = mcontext;
        mIParse = iParse;
    }

    public void request(final int method, final String endpoint, final JSONObject jsonRequest) {

        RequestQueue queue = Volley.newRequestQueue(context);

        if (jsonRequest != null)
            Log.i("request", jsonRequest.toString());

        String params = "?";
        if (method == Request.Method.GET) {
            if (jsonRequest != null) {
                Iterator<String> iter = jsonRequest.keys();
                while (iter.hasNext()) {
                    try {
                        String key = iter.next();
                        Object value = jsonRequest.get(key);
                        params += key + "=" + value + "&";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("request params", params);
            }
        }

        // Instantiate the RequestQueue.
        JsonObjectRequest request = new JsonObjectRequest(method, Config.BASE_URL + endpoint + params, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("url param",Config.BASE_URL+endpoint);
                            Log.i("response success", response.toString(2));
                            if (response.getBoolean("success")) {
                                mIParse.parseResponses(true, endpoint, response.toString());
                            } else {
                                if (response.isNull("message")) {
                                    mIParse.parseResponses(true, endpoint, response.toString());
                                } else {
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                                    mIParse.parseResponses(false, endpoint, response.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = null;
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Session expired... Please login again!";
//                            doLogout();
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        if (message != null) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Log.i("response error", message);
                        }

                        mIParse.parseResponses(false,  endpoint, Config.DEFAULT_STRING);

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " +
                        context.getSharedPreferences(Pref.DATA_USER, 0)
                                .getString(Pref.TOKEN, null));
                params.put("Accept", Config.ACCEPT);
                params.put("Content-Type", Config.ACCEPT);

                Log.i("request", params.toString());
                Log.i("request firebas", FirebaseInstanceId.getInstance().getToken());
                return params;
            }
        };
        request.setShouldCache(false)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void doLogout() {
        SharedPreferences preferencesSys = context.getSharedPreferences(Pref.DATA_SYS, 0);
        SharedPreferences.Editor editorSys = preferencesSys.edit();
        editorSys.putBoolean(Pref.IS_LOGIN, false);
        editorSys.apply();

        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private void errorAlert(String endpoint, String body) {

    }

    public interface IParse {
        void parseResponses(Boolean isSuccess, String method, String data);
    }

}
