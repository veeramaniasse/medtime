package com.simats.medtime;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class pat_appt extends Fragment {


    private String username,patid,name,issue,datee;
    private EditText epatid,ename,eissue,edate;

    private DatePickerDialog.OnDateSetListener setListener;

    public pat_appt(String user) {
        username=user;
    }

    public pat_appt() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pat_appt, container, false);
        epatid=view.findViewById(R.id.editTextText7);
        ename=view.findViewById(R.id.editTextText8);
        eissue=view.findViewById(R.id.editTextText9);
        edate=view.findViewById(R.id.editTextText10);

        Calendar calendar= Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edate.setOnClickListener(view1 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month+1;
                    String date = year+"-"+month+"-"+day;
                    edate.setText(date);

                }
            },year,month,day);
            datePickerDialog.show();

        });

        Button bt = view.findViewById(R.id.button4);
        bt.setOnClickListener( view1 -> {
            patid=epatid.getText().toString().trim();
            name=ename.getText().toString().trim();
            issue=eissue.getText().toString().trim();
            datee=edate.getText().toString().trim();

            if (!patid.isEmpty() && !name.isEmpty()&& !issue.isEmpty()&& !datee.isEmpty()) {
                System.out.println(username);
                sendLoginRequest(username);

            } else {
                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"appbooking.php";
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

                data.put("patient_id", patid);
                data.put("name", name);
                data.put("issue", issue);
                data.put("date", datee);

                return data;
            }
        };

        // Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                Toast.makeText(getActivity(), "Sign Up successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity3.class);
                intent.putExtra("username", username);
                startActivity(intent);
                System.out.println("aaa "+username);


            } else {
                Toast.makeText(getActivity(), "Sign Up failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }
}