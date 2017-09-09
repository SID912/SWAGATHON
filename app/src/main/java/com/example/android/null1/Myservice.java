package com.example.android.null1;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Myservice extends Service{
    private Timer timer = new Timer();
    String id;
    GPSTracker gps;
    Handler handler;
    int delay = 1000;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        id = intent.getStringExtra("Id");
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
                Toast.makeText(Myservice.this,latitude+"  "+longitude,Toast.LENGTH_LONG).show();
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
       Toast.makeText(Myservice.this,latitude+longitude +" ",Toast.LENGTH_LONG).show();
        final String lat = Double.toString(latitude);
        final String lon = Double.toString(longitude);

        String cancel_req_tag = "location";
//        Toast.makeText(Myservice.this,"Your Location is - \nLat: " + latitude + "\nLong: "+ longitude+" "+driverid,Toast.LENGTH_SHORT).show();
        String URL = "http://165.227.97.128:8000/request/locationupdate";
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
                params.put("driverid",id);
                params.put("currentlat",lat);
                params.put("currentlong",lon);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strreq, cancel_req_tag);
    }

}
