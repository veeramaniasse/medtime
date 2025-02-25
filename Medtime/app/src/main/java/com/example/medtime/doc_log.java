package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class doc_log extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_log);
        TextView t1= findViewById(R.id.textView4);
        t1.setOnClickListener(view -> {
            Intent it = new Intent(this, d_fgt_pswd.class);
            startActivity(it);
        });
        TextView t2= findViewById(R.id.textView6);
        t2.setOnClickListener(view -> {
            Intent it = new Intent(this, signup.class);
            startActivity(it);
        });
        Button bt= findViewById(R.id.button2);
        bt.setOnClickListener(view -> {
            Intent it= new Intent(this, MainActivity2.class);
            startActivity(it);
        });
    }
}