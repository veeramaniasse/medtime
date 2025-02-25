package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pat_edit extends AppCompatActivity {
    private String username;
    private EditText DoctorId,Gender,DoctorName,Speciality,c;
    private String name,spec,gend,cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_edit);
        username = getIntent().getStringExtra("username");
        DoctorId=findViewById(R.id.editTextText7);
        DoctorName=findViewById(R.id.editTextText8);
        Speciality=findViewById(R.id.editTextText9);
        Gender=findViewById(R.id.editTextText10);
        c=findViewById(R.id.editTextText11);
        Button bt = findViewById(R.id.button2);
        fetchData(username);
        bt.setOnClickListener(view -> {
            username=DoctorId.getText().toString().trim();
            name=DoctorName.getText().toString().trim();
            spec=Speciality.getText().toString().trim();
            gend=Gender.getText().toString().trim();
            cont=c.getText().toString().trim();
            sendLoginRequest(username);
        });

    }



    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"p_profile.php";

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
        Log.d("JSON Response", response);


        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                // Extracting data

                String doctorId = data.getString("patient_id");
                String doctorName = data.getString("name");
                String speciality = data.getString("age");
                String gender = data.getString("gender");
                String con = data.getString("phone_number");

                // Set text to EditText fields
                // Assuming editTextSNo, editTextDoctorId, editTextDoctorName, editTextSpeciality, editTextGender are your EditText fields

                DoctorId.setText(doctorId);
                DoctorName.setText(doctorName);
                Speciality.setText(speciality);
                Gender.setText(gender);
                c.setText(con);

            } else {
                String message = jsonResponse.getString("message");
                // Handle error message if needed
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }

    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }



    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"p_profileupdate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response,username);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username and password as POST parameters
                Map<String, String> data = new HashMap<>();
                System.out.println(username+" "+"");
                data.put("patient_id", username);
                data.put("name", name);
                data.put("age", spec);

                data.put("gender", gend);
                data.put("phone_number", cont);
                return data;
            }
        };

        // Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // Handle the JSON response
    private void handleResponse(String response,String username) {
        Log.d("JSON Response", response);

        // Handle your JSON response here without assuming a 'status' field
        // You can parse the response and handle success/failure accordingly
        try {
            // Example: Check if the response contains "success"
            if (response.toLowerCase().contains("true")) {
                Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(pat_edit.this, MainActivity3.class);
                intent.putExtra("username", username);
                startActivity(intent);
                System.out.println("aaa "+username);

                finish();
            } else {
                Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }
}