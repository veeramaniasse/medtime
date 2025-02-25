package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class page1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);
        LinearLayout l1 = findViewById(R.id.l1);
        LinearLayout l2 = findViewById(R.id.l2);
        l1.setOnClickListener(view ->{
            Intent it= new Intent(this, doc_log.class);
            startActivity(it);
        });
        l2.setOnClickListener(view ->{
            Intent it= new Intent(this, pat_log.class);
            startActivity(it);
        });

    }
}