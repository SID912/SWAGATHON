package com.example.android.null1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    EditText userid,password;
    String name,interest;
    GPSTracker gpss;
    Button Login;
    TextView register;
    String id,struserid,strpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Toast.makeText(MainActivity.this,""+dateFormat.format(date),Toast.LENGTH_LONG).show();
        PackageManager pm = this.getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this).setTitle("Location Permission").setMessage("This app need location Permission to work")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                    , MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).create().show();
            }
        register = (TextView)findViewById(R.id.Signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Reg = new Intent(MainActivity.this,Signup.class);
                startActivity(Reg);
            }
        });
        Login = (Button)findViewById(R.id.Login_Button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = (EditText)findViewById(R.id.Username);
                password = (EditText)findViewById(R.id.Password);
                login();
            }
        });
    }
    public void login() {
        struserid = userid.getText().toString();
        strpassword = password.getText().toString();
        String cancel_req_tag = "LOGIN";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/userlogin";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                    id = Json.getString("id");
                    interest = Json.getString("interest");
                    name = Json.getString("name");
                    Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    Intent service = new Intent(MainActivity.this,Myservice.class);
                    service.putExtra("id",id);
                    startService(service);
                    Intent chat=new Intent(MainActivity.this,ChatInterest.class);
                    chat.putExtra("name",name);
                    chat.putExtra("id",id);
                    chat.putExtra("interest",interest);
                    startActivity(chat);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "failed to Sign in Try again or Signup", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Failed to Sign in Try again or Signup",Toast.LENGTH_LONG).show();
                Log.e("MainActivity","ERROR"+ error.getMessage());
            }

        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id",struserid);
                params.put("password",strpassword);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(streq, cancel_req_tag);
    }

}
