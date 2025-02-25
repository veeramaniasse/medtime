package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class medicationAdapter extends BaseAdapter {

    private Context context;
    private List<Medication> patientList;

    public medicationAdapter(Context context, List<Medication> patients) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.medication, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.textView4311);
        TextView ageTextView = convertView.findViewById(R.id.textView4411);


        Medication patient = patientList.get(position);
        nameTextView.setText(patient.getName());
        if(patient.getbff().equals("1")){
            ageTextView.setText(patient.gettime()+"/Before Food");
        }
        else{
            ageTextView.setText(patient.gettime()+"/After Food");
        }




        // Set item click listener


        return convertView;
    }
}
