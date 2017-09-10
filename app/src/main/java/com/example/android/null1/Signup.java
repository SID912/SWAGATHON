package com.example.android.null1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Signup extends AppCompatActivity{
    EditText name,password,userid,phone;
    Button Signup;
    private Spinner spinner;
    private static final String[]interest ={"Fashion","Tech","Science","Literature"};
    String strinterest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name = (EditText)findViewById(R.id.reg_name);
        password=(EditText)findViewById(R.id.reg_password);
        userid = (EditText)findViewById(R.id.userid);
        phone = (EditText)findViewById(R.id.phoneno);
        spinner = (Spinner)findViewById(R.id.interest);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup.this,android.R.layout.simple_spinner_item,interest);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strinterest = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Signup= (Button)findViewById(R.id.register);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(name.getText().toString(),password.getText().toString(),userid.getText().toString(),phone.getText().toString());
            }
        });
    }
    public void register(final String strname,final String strpassword,final String struserid,final String strphone)
    {
        String cancel_req_tag = "LOGIN";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/userregister";
        spinner.setVisibility(View.VISIBLE);
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject Json = new JSONObject(response);
                    String res = Json.getString("success");
                    Intent intent = new Intent(Signup.this,MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(Signup.this,"Username alredy exists",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signup.this,"Unsuccessful",Toast.LENGTH_LONG).show();
            }

        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String ,String>();
                params.put("name",strname);
                params.put("password",strpassword);
                params.put("userid",struserid);
                params.put("phone",strphone);
                params.put("interest",strinterest);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(streq, cancel_req_tag);
    }
}
