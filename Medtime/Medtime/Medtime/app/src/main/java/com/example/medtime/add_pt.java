package com.simats.medtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class add_pt extends Fragment {



    public add_pt() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpt, container, false);
        Button bt = view.findViewById(R.id.button2);
        bt.setOnClickListener(view1 -> {
            Intent it =  new Intent(getActivity(),MainActivity2.class);
            startActivity(it);
        });
        return view;
    }
}