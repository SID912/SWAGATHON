package com.example.android.null1;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Myservice extends Service{
    private Timer timer = new Timer();
    String id,name,interest;
    GPSTracker gps;
    Handler handler;
    int delay = 5*1000;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        id = intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        interest = intent.getStringExtra("interest");
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        handler = new Handler();
        //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                gps = new GPSTracker(Myservice.this);
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                sendRequest();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onDestroy()
    {

    }
    public void sendRequest()
    {
        gps = new GPSTracker(Myservice.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        final String lat = Double.toString(latitude);
        final String lon = Double.toString(longitude);
        String cancel_req_tag = "location";
        String URL = "http://13.126.238.174:8000/home/locationupdate";
        StringRequest strreq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
            }
        }

        ){
            @Override
            protected Map<String,String > getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("id",id);
                params.put("currentlat",lat);
                params.put("currentlong",lon);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strreq, cancel_req_tag);
    }

}
