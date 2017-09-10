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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

public class ChatInterest extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public String interest,id;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageView mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    Button safety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        // Initialize references to views
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Bundle intent =getIntent().getExtras();
        id = intent.getString("id");
        mUsername = intent.getString("name");
        interest = intent.getString("interest");
        safety = (Button)findViewById(R.id.Safety);
        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent safe = new Intent(ChatInterest.this,Safety.class);
                safe.putExtra("id",id);
                safe.putExtra("name",mUsername);
                safe.putExtra("interest",interest);
                startActivity(safe);
            }
        });

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("message");
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageView)findViewById(R.id.SOS);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        Global.b =interest;
        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
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

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername,null,interest);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);
                mMessageEditText.setText("");
            }
        });
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                mMessageAdapter.add(friendlyMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
    }
    public void requesthelp() {
        String cancel_req_tag = "SOS";
        String URL_FOR_LOGIN = "http://13.126.238.174:8000/home/sos";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject Json = new JSONObject(response);
                    Toast.makeText(ChatInterest.this,"Sent",Toast.LENGTH_LONG).show();
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

    }
