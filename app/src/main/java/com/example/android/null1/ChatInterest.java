package com.example.android.null1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Siddharth  Singh on 09/09/2017.
 */

public class ChatInterest extends AppCompatActivity {
    Button Safety;
    String id;
    int clickCount = 0;
    long startTime;
    long duration;
    static final int MAX_DURATION = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        Bundle intent =getIntent().getExtras();
        id = intent.getString("id");

        Safety = (Button)findViewById(R.id.Safety);
        TextView test = (TextView)findViewById(R.id.test);
        test.setText(id);
        Safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent safety = new Intent(ChatInterest.this,Safety.class);
                safety.putExtra("id",id);
                startActivity(safety);
            }
        });

    }
}
