package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class pendingAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> patientList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public pendingAdapter(Context context, List<Appointment> patients) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_pending, parent, false);
        }

        TextView user = convertView.findViewById(R.id.textView4311);
        TextView name = convertView.findViewById(R.id.textView4411);
        TextView issue = convertView.findViewById(R.id.textView45);
        TextView date = convertView.findViewById(R.id.textView47);
        Button acceptButton = convertView.findViewById(R.id.button6);


        Appointment patient = patientList.get(position);
        name.setText(patient.getName());
        user.setText(String.valueOf(patient.getuser()));
        issue.setText(patient.getIssue());
        date.setText(String.valueOf(patient.getDate()));



        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acceptButtonClickListener != null) {
                    acceptButtonClickListener.onAcceptButtonClick(position);
                }
            }
        });



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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnAcceptButtonClickListener {
        void onAcceptButtonClick(int position);
    }

    private OnAcceptButtonClickListener acceptButtonClickListener;

    public void setOnAcceptButtonClickListener(OnAcceptButtonClickListener listener) {
        this.acceptButtonClickListener = listener;
    }



}
