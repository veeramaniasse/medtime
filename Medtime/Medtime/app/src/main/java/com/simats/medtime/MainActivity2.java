package com.simats.medtime;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.simats.medtime.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = getIntent().getStringExtra("username");
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacefragment(new dhome(username));
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            int it=item.getItemId();
            if(it == R.id.homeid1)
                replacefragment(new dhome(username));
            else if (it == R.id.addpllid1) {
                replacefragment( new addpill(username));
            }
            else if (it == R.id.apptid1) {
                replacefragment(new appt(username));
            }
            else if (it == R.id.adpid1) {
                replacefragment(new add_pt(username));
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