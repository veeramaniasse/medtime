package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pat_dtls extends AppCompatActivity {

    private String username;
    private List<Medic> patientList;
    private List<Tablet> patientList1;
    private ListView listView,l2;
    private medicAdapter adapter;
    private TabletAdapter adapter1;
    private ImageView im;

    private TextView docName, patAge, patGender, patPhone, treatment, bp, tsh, tft, fbs, ppbs, hba1c, urineRoutine, attName, attAge, attGender, attPhone, attRelPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_dtls);
        listView= findViewById(R.id.l1);
        l2= findViewById(R.id.l2);
        username = getIntent().getStringExtra("username");
        docName = findViewById(R.id.textView27);
        patAge = findViewById(R.id.textView31);
        patGender = findViewById(R.id.textView32);
        patPhone = findViewById(R.id.textView33);
        treatment = findViewById(R.id.textView34);
        bp = findViewById(R.id.textView26);
        tsh = findViewById(R.id.textView28);

        fbs = findViewById(R.id.textView272);
        ppbs = findViewById(R.id.textView311);
        hba1c = findViewById(R.id.textView321);
        urineRoutine = findViewById(R.id.textView331);
        attName = findViewById(R.id.textView2720);
        attAge = findViewById(R.id.textView3110);
        attGender = findViewById(R.id.textView3210);
        attPhone = findViewById(R.id.textView3310);
        attRelPhone = findViewById(R.id.textView39);
        ImageView im2=findViewById(R.id.imageView17);
        im2.setOnClickListener(view -> {
            Intent intent = new Intent(pat_dtls.this, edt_pat_dtls.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
        im = findViewById(R.id.contactssnap);
        patientList = new ArrayList<>();

        adapter = new medicAdapter(this, patientList);

        listView.setAdapter(adapter);
        fetchData(username);

        patientList1 = new ArrayList<>();

        adapter1 = new TabletAdapter(this, patientList1);

        l2.setAdapter(adapter1);
        String url1 = ip.ipn +"docnotify.php";
        makeRequest1(url1);

        String url = ip.ipn +"medication_details.php";
        makeRequest(url);
    }
    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"patientdetails.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                System.out.println(username);
                data.put("patient_id", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void handleResponse(String response) {
        try {
            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(response);

            // Check if the response indicates success
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                // If success, extract the data array
                JSONArray dataArray = jsonObject.getJSONArray("data");

                // Get the first object from the data array (assuming there's only one object)
                JSONObject dataObject = dataArray.getJSONObject(0);

                // Set patient details from the data object
                docName.setText(dataObject.getString("name"));
                patAge.setText(dataObject.getString("age"));
                patGender.setText(dataObject.getString("gender"));
                patPhone.setText(dataObject.getString("phone_number"));
                treatment.setText(dataObject.getString("Treatment"));
                bp.setText(dataObject.getString("BP"));
                tsh.setText(dataObject.getString("TSH"));
                fbs.setText(dataObject.getString("FBS"));
                ppbs.setText(dataObject.getString("ppbs"));
                hba1c.setText(dataObject.getString("HbA1c"));

                // Set attender details if available
                attName.setText(dataObject.optString("attender_name", ""));
                attAge.setText(dataObject.optString("relation_of_patient", ""));
                attGender.setText(dataObject.optString("att_gender", ""));
                attPhone.setText(dataObject.optString("att_phone_number", ""));

                // Check if there is an image URL
                if (dataObject.has("image")) {
                    String imageUrl = dataObject.getString("image");
                    String base64Image = imageUrl;
                    Static.glide(pat_dtls.this,ip.ipn+imageUrl,im);
//                    if (base64Image != null && !base64Image.isEmpty()) {
//                        byte[] decodedImageBytes = Base64.decode(base64Image, Base64.DEFAULT);
//                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
//                        Bitmap circularBitmap = Utils.getCircleBitmap(decodedBitmap);
//                        im.setImageBitmap(circularBitmap);
//                    }

                    // Load the image asynchronously using your preferred method (e.g., Picasso, Glide)
                    // Here, you can use any image loading library or method to load the image from the URL
                    // and set it to the ImageView
                    // For example, using Glide:

                }
            } else {
                // Handle failure case
                // You can show an error message or take appropriate action
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
    }



    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }

    private void makeRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                System.out.println(username);
                data.put("patient_id", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }



    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            patientList.clear();

            // Assuming you want to process each medication record
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String patientId = jsonObject.getString("patient_id");
                String medicationName = jsonObject.getString("medication_name");
                String morningDosage = jsonObject.getString("morning");
                String afternoonDosage = jsonObject.getString("afternoon");
                String nightDosage = jsonObject.getString("night");

                // Do whatever processing you need with this data
                // For example, you can create a Medication object and add it to a list
                Medic medication = new Medic( medicationName, morningDosage, afternoonDosage, nightDosage);
                // Assuming you have a list to store medications
                patientList.add(medication);

                // Here you can perform any additional logic needed based on the parsed data
            }
            adapter.notifyDataSetChanged();

            // Once you've processed all medication records, you can update your UI or perform any other tasks

            // Example:
            setListViewHeightBasedOnItems(listView);
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error

        }
    }


    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void makeRequest1(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse1(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                System.out.println(username);
                data.put("patient_id", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }



    private void parseResponse1(String response) {
        try {
            // Parse the response string into a JSONObject
            JSONObject jsonObject = new JSONObject(response);

            // Extract the 'data' array from the JSONObject
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            patientList1.clear();

            // Process each item in the 'data' array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String medicationName = item.getString("date");
                String morningDosage = item.getString("medicine_taken");

                // Create a Tablet object and add it to the patient list
                Tablet medication = new Tablet(medicationName, morningDosage);
                patientList1.add(medication);
            }

            // Notify the adapter that the data set has changed
            adapter1.notifyDataSetChanged();

            // Update the UI if necessary
            setListViewHeightBasedOnItems(l2);

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error

        }
    }






}