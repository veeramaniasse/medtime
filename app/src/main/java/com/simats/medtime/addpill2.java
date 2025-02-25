package com.simats.medtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class addpill2 extends AppCompatActivity {
    private RadioButton c1,c2;
    private int bf,af;
    private String username,mrngt,evngt,nghtt,name,id,medname,form,mrng,evng,nght;
    private EditText emrngt,eevngt,enghtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpill2);
        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        medname = getIntent().getStringExtra("medname");
        form = getIntent().getStringExtra("form");
        mrng = getIntent().getStringExtra("mrng");
        evng = getIntent().getStringExtra("evng");
        nght = getIntent().getStringExtra("nght");
        c1= (RadioButton) findViewById(R.id.radioButton);
        c2= (RadioButton) findViewById(R.id.radioButton2);
        emrngt=findViewById(R.id.editTextText6);
        eevngt=findViewById(R.id.editTextText62);
        enghtt=findViewById(R.id.editTextText63);

        emrngt.setOnClickListener(view -> {
                TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    // Format hours and minutes to ensure leading zeros if necessary
                    String formattedTime = String.format("%02d:%02d", hours, minutes);
                    emrngt.setText(formattedTime);
                }
            }, 15, 0, true);
            dialog.show();

        });
        eevngt.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    // Format hours and minutes to ensure leading zeros if necessary
                    String formattedTime = String.format("%02d:%02d", hours, minutes);
                    eevngt.setText(formattedTime);
                }
            }, 15, 0, true);
            dialog.show();

        });

        enghtt.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    // Format hours and minutes to ensure leading zeros if necessary
                    String formattedTime = String.format("%02d:%02d", hours, minutes);
                    enghtt.setText(formattedTime);
                }
            }, 15, 0, true);
            dialog.show();

        });

        Button b1 = findViewById(R.id.button2);
        b1.setOnClickListener(view -> {
            int t=0;
            if (c1 != null && c1.isChecked()) {
                bf=1;
                t += 1;
            } else if (c2 != null && c2.isChecked()) {
                af=1;
                t += 1;
            }
            int d=0;
            mrngt=emrngt.getText().toString().trim();
            evngt=eevngt.getText().toString().trim();
            nghtt=enghtt.getText().toString().trim();

            if(mrngt.isEmpty()){
                mrngt="0";
                d++;
            }
            if(evngt.isEmpty()){
                evngt="0";
                d++;
            }
            if(nghtt.isEmpty()){
                nghtt="0";
                d++;
            }
            if (d!=3 && !nghtt.isEmpty()&& t!=0) {
                sendLoginRequest(username);
            }else {
                Toast.makeText(addpill2.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"medication_reminder.php";
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

                data.put("patient_id", id);
                data.put("name", name);
                data.put("medication_name", medname);
                data.put("medication_form", form);
                data.put("morning", mrng.equalsIgnoreCase("0")?"no":"yes");
                data.put("afternoon", evng.equalsIgnoreCase("0")?"no":"yes");
                data.put("night", nght.equalsIgnoreCase("0")?"no":"yes");
                data.put("before_food", bf==1?"yes":"no");
                data.put("after_food", af==1?"yes":"no");
                data.put("first_intake", mrngt);
                data.put("second_intake", evngt);
                data.put("third_intake", nghtt);

                return data;
            }
        };

        // Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Handle the JSON response
    private void handleResponse(String response,String username) {
        Log.d("JSON Response", response);

        // Handle your JSON response here without assuming a 'status' field
        // You can parse the response and handle success/failure accordingly
        try {
            // Example: Check if the response contains "success"
            if (response.toLowerCase().contains("success")) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity2.class);
                intent.putExtra("username", username);
                startActivity(intent);
                System.out.println("aaa "+username);

            } else {
                Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(@NonNull VolleyError error) {
        System.out.println("boooooo -> "+ error.getMessage());
    }
}