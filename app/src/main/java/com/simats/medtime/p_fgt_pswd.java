package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class p_fgt_pswd extends AppCompatActivity {

    private EditText eid, epassword,  ecp;
    private String username, password, cpass;
    private String URL = ip.ipn +"forgot_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pfgt_pswd);

        eid = findViewById(R.id.editTextTextEmailAddress3);

        epassword = findViewById(R.id.editTextTextEmailAddress4);
        ecp = findViewById(R.id.editTextTextEmailAddress5);
        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = eid.getText().toString().trim();
                password = epassword.getText().toString().trim();
                cpass = ecp.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || cpass.isEmpty() ) {
                    Toast.makeText(p_fgt_pswd.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();


                } else if (!password.equals(cpass)) {
                    Toast.makeText(p_fgt_pswd.this, "Password and Confirm password should be same", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(username);
                    sendLoginRequest(username, password);
                }
            }
        });

    }

    private void sendLoginRequest(final String username, final String password) {
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
                data.put("patient_id", username);
                data.put("new_password", password);

                data.put("confirm_password", cpass);

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
                Toast.makeText(this, "password successfully changed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(p_fgt_pswd.this, pat_log.class);
                startActivity(intent);
                finish();
            }else if (response.toLowerCase().contains("not found")) {
                Toast.makeText(this, "Incorrect Username or Contact Number", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle network request errors
    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(this, "Request timed out. Check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show();
        }
    }
}