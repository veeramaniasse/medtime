package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class edt_pat_dtls extends AppCompatActivity {
    private ImageView im;
    private String username;
    private EditText docName, patAge, patGender, patPhone, treatment, tbp, ttsh, ttft, tfbs, tppbs, hba1c, urineRoutine, attName, attAge, attGender, attPhone, attRelPhone;
    private EditText epatid,egen,ename,eage,econt,etret,ebp,etsh,etft,efbs,eppbs,ehba,eur_rn,eat_name,erlt,eat_age,eat_gen,eat_cont;
    private String patid,gen,name,age,cont,tret,bp,tsh,tft,fbs,ppbs,hba,ur_rn,at_name,rlt,at_age,at_gen,at_cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edt_pat_dtls);
        username = getIntent().getStringExtra("username");
        docName = findViewById(R.id.textView27);
        patAge = findViewById(R.id.textView31);
        patGender = findViewById(R.id.textView32);
        patPhone = findViewById(R.id.textView33);
        treatment = findViewById(R.id.textView34);
        tbp = findViewById(R.id.textView26);
        ttsh = findViewById(R.id.textView28);

        tfbs = findViewById(R.id.textView272);
        tppbs = findViewById(R.id.textView311);
        hba1c = findViewById(R.id.textView321);
        urineRoutine = findViewById(R.id.textView331);
        attName = findViewById(R.id.textView2720);
        attAge = findViewById(R.id.textView3110);
        attGender = findViewById(R.id.textView3210);
        attPhone = findViewById(R.id.textView3310);
        attRelPhone = findViewById(R.id.textView39);
        im = findViewById(R.id.contactssnap);
        fetchData(username);

        ename=findViewById(R.id.textView27);
        eage=findViewById(R.id.textView31);
        egen=findViewById(R.id.textView32);
        econt=findViewById(R.id.textView33);
        etret=findViewById(R.id.textView34);
        ebp=findViewById(R.id.textView26);
        etsh=findViewById(R.id.textView28);

        efbs=findViewById(R.id.textView272);
        eppbs=findViewById(R.id.textView311);
        ehba=findViewById(R.id.textView321);
        eur_rn=findViewById(R.id.textView331);
        eat_name=findViewById(R.id.textView2720);
        erlt=findViewById(R.id.textView3310);
        eat_age=findViewById(R.id.textView3110);
        eat_gen=findViewById(R.id.textView3210);
        eat_cont=findViewById(R.id.textView39);

        Button bt = findViewById(R.id.button8);
        bt.setOnClickListener(view1 -> {
            name = ename.getText().toString().trim();
            age = eage.getText().toString().trim();
            gen = egen.getText().toString().trim();
            cont = econt.getText().toString().trim();
            tret = etret.getText().toString().trim();
            bp = ebp.getText().toString().trim();
            tsh = etsh.getText().toString().trim();

            fbs = efbs.getText().toString().trim();
            ppbs = eppbs.getText().toString().trim();
            hba = ehba.getText().toString().trim();
            ur_rn = eur_rn.getText().toString().trim();
            at_name = eat_name.getText().toString().trim();
            rlt = erlt.getText().toString().trim();
            at_age = eat_age.getText().toString().trim();
            at_gen = eat_gen.getText().toString().trim();
            at_cont = eat_cont.getText().toString().trim();
            sendLoginRequest(username);
        });
    }
    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"patientdetails.php";

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
        try {
            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(response);

            // Check if the response indicates success
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                // If success, extract the data array
                JSONArray dataArray = jsonObject.getJSONArray("data");

                // Get the first object from the data array (assuming there's only one object)
                JSONObject dataObject = dataArray.getJSONObject(0);

                // Set patient details from the data object
                docName.setText(dataObject.getString("name"));
                patAge.setText(dataObject.getString("age"));
                patGender.setText(dataObject.getString("gender"));
                patPhone.setText(dataObject.getString("phone_number"));
                treatment.setText(dataObject.getString("Treatment"));
                tbp.setText(dataObject.getString("BP"));
                ttsh.setText(dataObject.getString("TSH"));
                tfbs.setText(dataObject.getString("FBS"));
                tppbs.setText(dataObject.getString("ppbs"));
                hba1c.setText(dataObject.getString("HbA1c"));

                // Set attender details if available
                attName.setText(dataObject.optString("attender_name", ""));
                attAge.setText(dataObject.optString("relation_of_patient", ""));
                attGender.setText(dataObject.optString("att_gender", ""));
                attPhone.setText(dataObject.optString("att_phone_number", ""));


                // Check if there is an image URL
                if (dataObject.has("image")) {
                    String imageUrl = dataObject.getString("image");
                    String base64Image = imageUrl;
                    if (base64Image != null && !base64Image.isEmpty()) {
                        byte[] decodedImageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
                        Bitmap circularBitmap = Utils.getCircleBitmap(decodedBitmap);
                        im.setImageBitmap(circularBitmap);
                    }

                    // Load the image asynchronously using your preferred method (e.g., Picasso, Glide)
                    // Here, you can use any image loading library or method to load the image from the URL
                    // and set it to the ImageView
                    // For example, using Glide:

                }
            } else {
                // Handle failure case
                // You can show an error message or take appropriate action
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
        String URL = ip.ipn+"editpatientdetails.php";
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
                data.put("name", name);
                data.put("age", age);
                data.put("gender", gen);
                data.put("phone_number", cont);
                data.put("Treatment", tret);
                data.put("BP", bp);
                data.put("TSH", tsh);

                data.put("FBS", fbs);
                data.put("ppbs", ppbs);
                data.put("HbA1c", hba);
                data.put("urine_routine", ur_rn);
                data.put("attender_name", at_name);
                data.put("relation_of_patient", rlt);
                data.put("att_age", at_age);
                data.put("att_gender", at_gen);
                data.put("att_phone_number", at_cont);

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
            if (response.toLowerCase().contains("true")) {
                Toast.makeText(this, "Updated successful", Toast.LENGTH_SHORT).show();




            } else {
                Toast.makeText(this, "Sign Up failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }


}