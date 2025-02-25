package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class medicAdapter extends BaseAdapter {

    private Context context;
    private List<Medic> patientList;

    public medicAdapter(Context context, List<Medic> patients) {
        this.context = context;
        this.patientList = patients;
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.med, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.textView4311);
        TextView ageTextView = convertView.findViewById(R.id.textView4411);


        Medic patient = patientList.get(position);
        nameTextView.setText(patient.getName());

        ageTextView.setText(patient.getmrng()+"-"+patient.geteve()+"-"+patient.getnght());




        // Set item click listener


        return convertView;
    }
}
