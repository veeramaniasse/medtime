package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class pat_log extends AppCompatActivity {
    private EditText eid, epassword;

    private String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_log);
        Button bt= findViewById(R.id.button2);
        eid = findViewById(R.id.editTextText1);
        epassword = findViewById(R.id.editTextText);
        bt.setOnClickListener(view -> {
            username = eid.getText().toString().trim();
            password = epassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                System.out.println(username);
                sendLoginRequest(username, password);

            } else {
                Toast.makeText(pat_log.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        TextView t1= findViewById(R.id.textView4);
        t1.setOnClickListener(view -> {
            Intent it = new Intent(this, p_fgt_pswd.class);
            startActivity(it);
        });
    }

    private void sendLoginRequest(final String username, final String password) {
        String URL = "http://14.139.187.229:8081/Medtime/user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
                Log.d("login","failed - ",error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username and password as POST parameters
                Map<String, String> data = new HashMap<>();
                data.put("patient_id", username);
                data.put("password", password);
                return data;
            }
        };

//         Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    // Handle the JSON response
    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        try {
            if (response.toLowerCase().contains("true")) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                long alarmTimeInMillis = System.currentTimeMillis() + 10000; // 10 seconds

                // Set the alarm
                AlarmHelper.setAlarm(this, alarmTimeInMillis);
                Intent intent = new Intent(pat_log.this, MainActivity3.class);
                intent.putExtra("username", username);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle network request errors
    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Log.d("loginerror","login time error: "+ error.getNetworkTimeMs());
            Log.d("loginerror","login time error: "+ error.getLocalizedMessage());
            Toast.makeText(this, "Request timed out. Check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(error.toString().trim());
            Log.d("loginerror","login error: "+ error.toString().trim());
            Toast.makeText(this, "v error "+ error.toString().trim(), Toast.LENGTH_SHORT).show();
        }
    }
}