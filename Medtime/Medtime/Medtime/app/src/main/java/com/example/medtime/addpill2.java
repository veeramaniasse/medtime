package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class addpill2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpill2);
        Button b1 = findViewById(R.id.button2);
        b1.setOnClickListener(view -> {
            Intent it= new Intent(this, MainActivity2.class);
            startActivity(it);
        });
    }
}