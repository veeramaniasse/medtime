package com.simats.medtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;


public class dhome extends Fragment {

    private TextView t1,t2,t3,at1,at2,at3,at4;

    private String username;
    public dhome(String user) {
        username=user;
    }
    public dhome() {
        // Required empty public constructor
    }
    private ImageView im1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.dhome, container, false);
        t1= view.findViewById(R.id.textView451);
        t2 = view.findViewById(R.id.textView4511);
        t3 = view.findViewById(R.id.textView46);
        at2= view.findViewById(R.id.textView4311);
        at1 = view.findViewById(R.id.textView43);
        at3 = view.findViewById(R.id.textView4411);
        at4 = view.findViewById(R.id.textView44);
        t3.setOnClickListener(view1 -> {
            Intent it = new Intent(getActivity(),pat_list.class);
            startActivity(it);
        });
        im1 = view.findViewById(R.id.imageView8);
        Button b1= view.findViewById(R.id.button6);
        b1.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("username", username);

// Instantiate the fragment
            appt fragment = new appt();
            fragment.setArguments(bundle);

// Begin a transaction to replace the current fragment with the appt fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null) // Optional: Add fragment to back stack
                    .commit();
        });
        System.out.println(username);
        im1.setOnClickListener(view1 -> {
            Intent it = new Intent(getActivity(), dprof.class);
            it.putExtra("username", username);
            startActivity(it);
        });
        fetchData(username);
        fetchData1(username);
        return view;
    }

    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"patienthomelist.php";

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


                return data;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");

            // Ensure there are at least two patients in the response
            if (data.length() >= 2) {
                JSONObject patient1 = data.getJSONObject(0);
                JSONObject patient2 = data.getJSONObject(1);

                // Extract patient IDs
                String patientId1 = patient1.getString("patient_id");
                String patientId2 = patient2.getString("patient_id");

                // Set patient IDs to text fields
                t1.setText(patientId1);
                t2.setText(patientId2);
            } else {
                // Handle case where there are fewer than 2 patients
                Log.e("Handle Response", "Less than 2 patients in the response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Log.e("Handle Response", "Error parsing JSON response: " + e.getMessage());
        }




    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }

    private void fetchData1(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"recent_appointment.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse1(response);
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


                return data;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


    private void handleResponse1(String response) {
        Log.d("JSON Response", response);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");

            // Ensure there is at least one appointment in the response
            if (data.length() >= 1) {
                // Get the first appointment
                JSONObject appointment = data.getJSONObject(0);

                // Extract appointment details
                String patientId = appointment.getString("patient_id");
                String name = appointment.getString("name");
                String issue = appointment.getString("issue");
                String date = appointment.getString("date");

                // Set appointment details to TextViews
                at1.setText(patientId);
                at2.setText(name);
                at3.setText(issue);
                at4.setText(date);
            } else {
                // Handle case where there are no appointments
                Log.e("Handle Response", "No appointments in the response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Log.e("Handle Response", "Error parsing JSON response: " + e.getMessage());
        }
    }



}