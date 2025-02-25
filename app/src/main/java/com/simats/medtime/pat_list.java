package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pat_list extends AppCompatActivity {

    private List<Patient> patientList;
    private GridView gridView;
    private SearchView searchView;
    private patientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_list);
        searchView = findViewById(R.id.searchView);
        gridView = findViewById(R.id.gridView);
        patientList = new ArrayList<>();

        adapter = new patientAdapter(this, patientList);
        gridView.setAdapter(adapter);

        // Set item click listener to open pat_view activity with selected patient details
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient selectedPatient = patientList.get(position);
                Intent intent = new Intent(pat_list.this, pat_dtls.class);
                intent.putExtra("username", selectedPatient.getuser());
                startActivity(intent);
            }
        });

        String url = ip.ipn +"patientlist.php";
        makeRequest(url);
        setupSearchView();
    }

    private void makeRequest(String url) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.d("Volley Response", response);
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(pat_list.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseResponse(String response) {
        Log.d("JSON Response", response);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");

            // Ensure there is data in the response
            if (data.length() > 0) {
                // Clear existing data
                patientList.clear();

                // Iterate through the data array and add patients to the list
                for (int i = 0; i < data.length(); i++) {
                    JSONObject patientObject = data.getJSONObject(i);
                    String patientId = patientObject.getString("patient_id");
                    String name = patientObject.getString("name");
                    String img = patientObject.getString("image");

                    // Add the patient to the list
                    patientList.add(new Patient(name, patientId,img));
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            } else {
                // Handle case where there is no data in the response
                Log.e("Handle Response", "No data in the response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Log.e("Handle Response", "Error parsing JSON response: " + e.getMessage());
        }
    }

    private void filterParentList(String searchText) {
        Log.d("SearchText", searchText);

        List<Patient> filteredList = new ArrayList<>();

        // Trim leading and trailing whitespaces
        searchText = searchText.trim().toLowerCase();

        for (Patient patient : patientList) {
            Log.d("PatientName", patient.getName());
            if (patient.getName().toLowerCase().contains(searchText)) {
                filteredList.add(patient);
            }
        }

        // Update the adapter with the filtered list
        adapter.setFilteredList(filteredList);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the parent list based on the entered text
                filterParentList(newText);
                return true;
            }
        });
    }


}