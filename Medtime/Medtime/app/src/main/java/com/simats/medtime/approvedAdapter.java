package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class approvedAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> patientList;
    private approvedAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public approvedAdapter(Context context, List<Appointment> patients) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_approved, parent, false);
        }

        TextView user = convertView.findViewById(R.id.textView4311);
        TextView name = convertView.findViewById(R.id.textView4411);
        TextView issue = convertView.findViewById(R.id.textView45);
        TextView date = convertView.findViewById(R.id.textView47);


        Appointment patient = patientList.get(position);
        name.setText(patient.getName());
        user.setText(patient.getuser());
        issue.setText(patient.getIssue());
        date.setText(patient.getDate());



        // Set item click listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        return convertView;
    }

    public void setOnItemClickListener(approvedAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

}
