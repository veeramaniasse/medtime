package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TabletAdapter extends BaseAdapter {

    private Context context;
    private List<Tablet> patientList;

    public TabletAdapter(Context context, List<Tablet> patients) {
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


        Tablet patient = patientList.get(position);
        nameTextView.setText(patient.getName());

        ageTextView.setText(patient.getmrng());




        // Set item click listener


        return convertView;
    }
}
