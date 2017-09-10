package com.example.android.null1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Siddharth  Singh on 09/10/2017.
 */

public class Otp extends AppCompatActivity {
    String otp;
    TextView OtpView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otplayout);
        Bundle intent =getIntent().getExtras();
        otp = intent.getString("otp");
        OtpView = (TextView)findViewById(R.id.Otp);
        OtpView.setText(otp);

    }
}
