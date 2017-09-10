package com.example.android.null1;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

public class Safety extends AppCompatActivity {
    EditText message;
    Button send;
    String id,name,interest,date,time;
    String Otp;
    GPSTracker gps;
    ImageView SOS;
    TextView otpview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safetylayout);
        Bundle intent =getIntent().getExtras();
        id = intent.getString("id");
        name = intent.getString("name");
        interest = intent.getString("interest");
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);
        time = hour+":"+minute+":"+second;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dates = new Date();
        date = dateFormat.format(dates);
        otpview = (TextView)findViewById(R.id.otp);
        otpview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestotp();
            }
        });
        SOS = (ImageView) findViewById(R.id.SOS2);
        send=(Button)findViewById(R.id.Safety);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = (EditText)findViewById(R.id.message);
                safety(message.getText().toString());
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
        String cancel_req_tag = "Safety";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/message";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                    Toast.makeText(Safety.this,"message Sent",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(Safety.this,"Message Sending failed plz try again",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Safety.this,"Message Sending failed plz try again",Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id",id);
                params.put("message",strmessage);
                params.put("date",date);
                params.put("time",time);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(streq, cancel_req_tag);
    }
    public void requesthelp() {
        String cancel_req_tag = "SOS";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/sos";
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
    public void requestotp() {
        gps = new GPSTracker(Safety.this);
        String cancel_req_tag = "SOS";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/requestotp";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                    Otp = Json.getString("otp");
                    Intent totp = new Intent(Safety.this,Otp.class);
                    totp.putExtra("otp",Otp);
                    startActivity(totp);
                } catch (JSONException e) {
                    Toast.makeText(Safety.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Safety.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
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
}
