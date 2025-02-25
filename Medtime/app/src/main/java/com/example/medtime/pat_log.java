package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class pat_log extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_log);
        TextView t1= findViewById(R.id.textView4);
        t1.setOnClickListener(view -> {
            Intent it = new Intent(this, d_fgt_pswd.class);
            startActivity(it);
        });
    }
}