package com.simats.medtime;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class appt extends Fragment {
    private String username;
    public appt() {
        // Required empty public constructor
    }
    public appt(String user) {
        username=user;
    }

    private List<Appointment> patientList;
    private ListView listView;
    private pendingAdapter adapter1;
    private approvedAdapter adapter2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.appt, container, false);
        if (username == null || username.isEmpty()) {
            // If username is null or empty, retrieve it from arguments
            Bundle args = getArguments();
            if (args != null) {
                username = args.getString("username");
            }
        }



        listView = view.findViewById(R.id.Listview);
        patientList = new ArrayList<>(); // Assuming Patient is a class representing patient details

        // Create a custom adapter for your patients
        adapter1 = new pendingAdapter(getActivity(), patientList);

        // Set item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });
        adapter1.setOnAcceptButtonClickListener(new pendingAdapter.OnAcceptButtonClickListener() {
            @Override
            public void onAcceptButtonClick(int position) {
                // Perform the action when the "Accept" button is clicked
                Appointment selectedPatient = patientList.get(position);
                String username = selectedPatient.getuser();
                Dialog myDialog = new Dialog(requireActivity());
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setCancelable(false);
                myDialog.setContentView(R.layout.app_cnf);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                Button no = myDialog.findViewById(R.id.button7);
                no.setOnClickListener(view1 -> {
                    myDialog.dismiss();
                    sendLoginRequest(username);
                });

            }
        });

        listView.setAdapter(adapter1);



        Button bt1= view.findViewById(R.id.button5);
        Button bt2= view.findViewById(R.id.button3);
        bt1.setOnClickListener(view1 -> {
            listView = view.findViewById(R.id.Listview);
            patientList = new ArrayList<>(); // Assuming Patient is a class representing patient details

            // Create a custom adapter for your patients
            adapter1 = new pendingAdapter(getActivity(), patientList);
            adapter1.setOnItemClickListener(new pendingAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Appointment selectedPatient = patientList.get(position);
//                Intent intent = new Intent(doct_pat.this, pat_view.class);
//                intent.putExtra("username", selectedPatient.getuser());
//
//                startActivity(intent);
                }
            });

            listView.setAdapter(adapter1);

            // Set item click listener for ListView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Handle item click if needed
                }
            });


            String url = ip.ipn +"appprintbooked.php";
            makeRequest(url);


            bt1.setBackground(this.getResources().getDrawable(R.drawable.txtfl));
            bt2.setBackground(new ColorDrawable(Color.TRANSPARENT));

        });
        bt2.setOnClickListener(view1 -> {

            listView = view.findViewById(R.id.Listview);
            patientList = new ArrayList<>(); // Assuming Patient is a class representing patient details

            // Create a custom adapter for your patients
            adapter2 = new approvedAdapter(getActivity(), patientList);


            listView.setAdapter(adapter2);

            // Set item click listener for ListView

            String url = ip.ipn +"appprintbooked1.php";
            makeRequest1(url);

            bt2.setBackground(this.getResources().getDrawable(R.drawable.txtfl));
            bt1.setBackground(new ColorDrawable(Color.TRANSPARENT));

        });
        String url = ip.ipn +"appprintbooked.php";
        makeRequest(url);
        return view;
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
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                // Clear existing data
                patientList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject patientObject = jsonArray.getJSONObject(i);
                    String user= patientObject.getString("patient_id");
                    String name = patientObject.getString("name");
                    String issue = patientObject.getString("issue");
                    String date = patientObject.getString("date");


                    // Add this patient to your patientList
                    patientList.add(new Appointment(name, user,issue,date));
                }

                // Notify your adapter that the data set has changed
                adapter1.notifyDataSetChanged();
            } else {
                // Handle empty response
                Toast.makeText(getActivity(), "Empty response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeRequest1(String url) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.d("Volley Response", response);
                        parseResponse1(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void parseResponse1(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() > 0) {
                // Clear existing data
                patientList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject patientObject = jsonArray.getJSONObject(i);
                    String user= patientObject.getString("patient_id");
                    String name = patientObject.getString("name");
                    String issue = patientObject.getString("issue");
                    String date = patientObject.getString("date");


                    // Add this patient to your patientList
                    patientList.add(new Appointment(name, user,issue,date));
                }

                // Notify your adapter that the data set has changed
                adapter2.notifyDataSetChanged();
            } else {
                // Handle empty response
                Toast.makeText(getActivity(), "Empty response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"appacceptance.php";
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
                Toast.makeText(getActivity(), "Apoointment added successful", Toast.LENGTH_SHORT).show();




            } else {
                Toast.makeText(getActivity(), "Apoointment addition failed", Toast.LENGTH_SHORT).show();
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