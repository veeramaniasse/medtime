package com.simats.medtime;

import android.content.Intent;
import android.os.Bundle;

import com.simats.medtime.databinding.ActivityMain2Binding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.simats.medtime.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    private ActivityMain3Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = getIntent().getStringExtra("username");
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);

//        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacefragment(new pat_home(username));
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            int it=item.getItemId();
            if(it == R.id.homeid1)
                replacefragment(new pat_home(username));

            else if (it == R.id.apptid1) {
                replacefragment(new pat_appt(username));
            }
            else if (it == R.id.prf) {
                replacefragment(new PATI_prof(username));
            }

            return true;
        });
    }

    private void replacefragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}