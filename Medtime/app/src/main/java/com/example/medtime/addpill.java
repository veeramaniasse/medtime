package com.simats.medtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class addpill extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpill, container, false);
        Button bt = view.findViewById(R.id.button2);
        bt.setOnClickListener(view1 -> {
            Intent it =  new Intent(getActivity(),addpill2.class);
            startActivity(it);
        });
        return view;

    }
}