package com.simats.medtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class patientAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Patient> patientList;
    private List<Patient> filteredList;

    public patientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patientList = patients;
        this.filteredList = new ArrayList<>(patients);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pat_item, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.textView48);
            holder.ageTextView = convertView.findViewById(R.id.textView49);
            holder.im = convertView.findViewById(R.id.imageView16);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Patient patient = patientList.get(position);
        holder.nameTextView.setText(patient.getName());
        holder.ageTextView.setText(patient.getuser());
        if(patient.getimg().equals("")){}
        else{
            String base64Image = ip.ipn+patient.getimg();
            Static.glide(context, base64Image, holder.im);
//            if (base64Image != null && !base64Image.isEmpty()) {
//                byte[] decodedImageBytes = Base64.decode(base64Image, Base64.DEFAULT);
//                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
//                Bitmap circularBitmap = Utils.getCircleBitmap(decodedBitmap);
//                holder.im.setImageBitmap(circularBitmap);
//            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView ageTextView;
        ImageView im;
    }
    public void setFilteredList(List<Patient> filteredList) {
        patientList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Patient> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    // If constraint is null or empty, return the original list
                    filtered.addAll(patientList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Patient patient : patientList) {
                        // Filter the data based on the name containing the filter pattern
                        if (patient.getName().toLowerCase().contains(filterPattern)) {
                            filtered.add(patient);
                        }
                    }
                }
                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Patient>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
