package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class dprof extends AppCompatActivity {

    private String username;
    private TextView DoctorId,Gender,DoctorName,Speciality;
    private String name,spec,gend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dprof);
        username = getIntent().getStringExtra("username");
        DoctorId=findViewById(R.id.editTextText7);
        DoctorName=findViewById(R.id.editTextText8);
        Speciality=findViewById(R.id.editTextText9);
        Gender=findViewById(R.id.editTextText10);
        Button bt = findViewById(R.id.button2);
        fetchData(username);
        bt.setOnClickListener(view -> {
            Intent it = new Intent(this,doc_edit.class);
            it.putExtra("username",username);
            startActivity(it);
        });
        ImageView im1= findViewById(R.id.imageView9);
        im1.setOnClickListener(view -> {
            Dialog myDialog = new Dialog(this);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.setCancelable(false);
            myDialog.setContentView(R.layout.lgout);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            Button yes = myDialog.findViewById(R.id.button11);
            Button no = myDialog.findViewById(R.id.button9);
            yes.setOnClickListener(view1 -> {
                Intent intent = new Intent(this, page1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            });
            no.setOnClickListener(view1 -> {
                myDialog.dismiss();
            });
        });

    }



    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"d_profile.php";

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
                data.put("doctor_id", username);

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

                String doctorId = data.getString("doctor_id");
                String doctorName = data.getString("doctor_name");
                String speciality = data.getString("speciality");
                String gender = data.getString("gender");

                // Set text to EditText fields
                // Assuming editTextSNo, editTextDoctorId, editTextDoctorName, editTextSpeciality, editTextGender are your EditText fields

                DoctorId.setText(doctorId);
                DoctorName.setText(doctorName);
                Speciality.setText(speciality);
                Gender.setText(gender);

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




}