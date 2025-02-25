package com.simats.medtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class addpill extends Fragment {

    private String username,name,id,medname,form;
    private EditText ename,eid,emedname,eform;
    private int mrng=0,evng=0,nght=0;
    private CheckBox c1,c2,c3;

    public addpill(String user) {
        username=user;
    }
    public addpill() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpill, container, false);
        eid=view.findViewById(R.id.editTextText3);
        ename=view.findViewById(R.id.editTextText2);
        emedname=view.findViewById(R.id.editTextText4);
        eform=view.findViewById(R.id.editTextText5);

        c1=view.findViewById(R.id.checkBox);
        c2=view.findViewById(R.id.checkBox2);
        c3=view.findViewById(R.id.checkBox3);

        Button bt = view.findViewById(R.id.button2);
        bt.setOnClickListener(view1 -> {

            id=eid.getText().toString().trim();
            name=ename.getText().toString().trim();
            medname=emedname.getText().toString().trim();
            form=eform.getText().toString().trim();
            int t=0;

            if (c1 != null && c1.isChecked()) {
                mrng=1;
                t += 1;
            }
            if (c2 != null && c2.isChecked()) {
                evng=1;
                t += 1;
            }
            if (c3 != null && c3.isChecked()) {
                nght=1;
                t += 1;
            }

            if (!id.isEmpty() && !name.isEmpty() && !medname.isEmpty() && !form.isEmpty() && t!=0) {
                Intent it =  new Intent(getActivity(),addpill2.class);
                it.putExtra("username", username);
                it.putExtra("name", name);
                it.putExtra("id", id);
                it.putExtra("medname", medname);
                it.putExtra("form", form);
                it.putExtra("mrng", String.valueOf(mrng));
                it.putExtra("evng", String.valueOf(evng));
                it.putExtra("nght", String.valueOf(nght));
                startActivity(it);
            }else {
                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }

        });
        return view;

    }
}