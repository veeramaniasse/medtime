package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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

public class appt_status extends AppCompatActivity {

    private String username;

    private List<Appointment> patientList;
    private ListView listView;
    private appointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appt_status);
        username = getIntent().getStringExtra("username");
        listView = findViewById(R.id.Listview);
        patientList = new ArrayList<>(); // Assuming Patient is a class representing patient details

        // Create a custom adapter for your patients
        adapter = new appointmentAdapter(this, patientList);
        listView.setAdapter(adapter);
        String url = ip.ipn +"appstatus.php";
        makeRequest(url);

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
        System.out.println(response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            if (jsonArray.length() > 0) {
                // Clear existing data
                patientList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject patientObject = jsonArray.getJSONObject(i);
                    String user= patientObject.getString("patient_id");
                    String name = patientObject.getString("name");
                    String issue = patientObject.getString("issue");
                    String date = patientObject.getString("date");
                    String status = patientObject.getString("status");

                    // Add this patient to your patientList
                    patientList.add(new Appointment(name, user, issue, date, status));
                }

                // Notify your adapter that the data set has changed
                adapter.notifyDataSetChanged();
            } else {
                // Handle empty response
                Toast.makeText(this, "Empty response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }


}