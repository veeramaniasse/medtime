package com.simats.medtime;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.simats.medtime.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacefragment(new dhome());
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            int it=item.getItemId();
            if(it == R.id.homeid1)
                replacefragment(new dhome());
            else if (it == R.id.addpllid1) {
                replacefragment( new addpill());
            }
            else if (it == R.id.apptid1) {
                replacefragment(new appt());
            }
            else if (it == R.id.adpid1) {
                replacefragment(new add_pt());
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