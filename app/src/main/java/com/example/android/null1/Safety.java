package com.example.android.null1;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

public class Safety extends AppCompatActivity {
    EditText message;
    Button send;
    String id;
    GPSTracker gps;
    TextView SOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safetylayout);
        Bundle intent =getIntent().getExtras();
        id = intent.getString("id");
        message = (EditText)findViewById(R.id.message);
        SOS = (TextView)findViewById(R.id.SOS2);
        send=(Button)findViewById(R.id.Safety);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Safety.this,""+message.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null; // Usually back camera is at 0 position.
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, true);   //Turn ON
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                requesthelp();
            }
        });

    }


    public void safety(final String strmessage) {
        String cancel_req_tag = "SOS";
        String URL_FOR_LOGIN = "";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id",id);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(streq, cancel_req_tag);
    }
    public void requesthelp() {
        gps = new GPSTracker(Safety.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final String lat = Double.toString(latitude);
        final String lon = Double.toString(longitude);
        String cancel_req_tag = "SOS";
        String URL_FOR_LOGIN = "";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id",id);
                params.put("lat",lat);
                params.put("lon",lon);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(streq, cancel_req_tag);
    }
}
