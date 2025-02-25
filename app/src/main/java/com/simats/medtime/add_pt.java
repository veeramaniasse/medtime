package com.simats.medtime;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class add_pt extends Fragment {

    private static final int CAMERA_GALLERY_REQUEST_CODE = 1001;
    private Intent lastActivityResultData;
    private Uri selectedImageUri;
    private ImageView im;
    private String username;

    public add_pt(String user) {
        username=user;
    }
    public add_pt() {
        // Required empty public constructor
    }


    private EditText epatid,egen,ename,eage,econt,etret,ebp,etsh,etft,efbs,eppbs,ehba,eur_rn,eat_name,erlt,eat_age,eat_gen,eat_cont;
    private String patid,gen,name,age,cont,tret,bp,tsh,tft,fbs,ppbs,hba,ur_rn,at_name,rlt,at_age,at_gen,at_cont;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpt, container, false);

        im = view.findViewById(R.id.imageView6);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraOrGallery();
            }
        });

        epatid=view.findViewById(R.id.editTextTextEmailAddress3);
        ename=view.findViewById(R.id.editTextTextEmailAddress);
        eage=view.findViewById(R.id.editTextTextEmailAddress2);
        egen=view.findViewById(R.id.editTextTextEmailAddress6);
        econt=view.findViewById(R.id.editTextTextEmailAddress4);
        etret=view.findViewById(R.id.editTextTextEmailAddress5);
        ebp=view.findViewById(R.id.editTextTextEmailAddress41);
        etsh=view.findViewById(R.id.editTextTextEmailAddress51);

        efbs=view.findViewById(R.id.editTextTextEmailAddress8);
        eppbs=view.findViewById(R.id.editTextTextEmailAddress9);
        ehba=view.findViewById(R.id.editTextTextEmailAddress10);
        eur_rn=view.findViewById(R.id.editTextTextEmailAddress11);
        eat_name=view.findViewById(R.id.editTextTextEmailAddress12);
        erlt=view.findViewById(R.id.editTextTextEmailAddress13);
        eat_age=view.findViewById(R.id.editTextTextEmailAddress14);
        eat_gen=view.findViewById(R.id.editTextTextEmailAddress15);
        eat_cont=view.findViewById(R.id.editTextTextEmailAddress16);


        Button bt = view.findViewById(R.id.button2);
        bt.setOnClickListener(view1 -> {
            patid=epatid.getText().toString().trim();
            name=ename.getText().toString().trim();
            age=eage.getText().toString().trim();
            gen=egen.getText().toString().trim();
            cont=econt.getText().toString().trim();
            tret=etret.getText().toString().trim();
            bp=ebp.getText().toString().trim();
            tsh=etsh.getText().toString().trim();

            fbs=efbs.getText().toString().trim();
            ppbs=eppbs.getText().toString().trim();
            hba=ehba.getText().toString().trim();
            ur_rn=eur_rn.getText().toString().trim();
            at_name=eat_name.getText().toString().trim();
            rlt=erlt.getText().toString().trim();
            at_age=eat_age.getText().toString().trim();
            at_gen=eat_gen.getText().toString().trim();
            at_cont=eat_cont.getText().toString().trim();
            sendLoginRequest(username);
            if (selectedImageUri != null || lastActivityResultData != null) {
                try {
                    Bitmap bitmap;
                    if (selectedImageUri != null && selectedImageUri.toString().startsWith("content://")) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), selectedImageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        // Camera image captured
                        Bitmap photo = (Bitmap) lastActivityResultData.getExtras().get("data");
                        bitmap = photo;
                    }

                    String base64Image = convertBitmapToBase64(bitmap);
                    // Execute AsyncTask to send data to the server
                    new SendDataToServer().execute(patid, selectedImageUri != null ? selectedImageUri.toString() : "", base64Image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"addpatient.php";
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
                data.put("age", age);
                data.put("gender", gen);
                data.put("phone_number", cont);
                data.put("Treatment", tret);
                data.put("BP", bp);
                data.put("TSH", tsh);
                data.put("RBS", ur_rn);
                data.put("FBS", fbs);
                data.put("ppbs", ppbs);
                data.put("HbA1c", hba);
//                data.put("urine_routine", ur_rn);
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
            if (!response.toLowerCase().contains("error")) {
                Toast.makeText(getActivity(), "Sign Up successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity2.class);
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

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void openCameraOrGallery() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Create a chooser intent to allow the user to select between camera and gallery
        Intent chooser = Intent.createChooser(galleryIntent, "Select Image Source");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });

        startActivityForResult(chooser, CAMERA_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Gallery image selected
                selectedImageUri = data.getData();

                im.setImageURI(selectedImageUri);
            } else if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                // Camera image captured
                lastActivityResultData = data; // Store the data
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                im.setImageBitmap(photo);
            }
        }
    }


    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String username = params[0];
                String imageUriString = params[1];
                String base64Image = params[2];
                System.out.println(base64Image);
                // Create a JSON object
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("username", username);
                jsonParams.put("base64image", base64Image);

                // Convert JSON object to string
                String jsonData = jsonParams.toString();

//                URL url = new URL(ip.ipn+"img_upld.php");
                URL url = new URL(ip.ipn+"image.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setDoOutput(true);

                // Write JSON data to the server
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonData.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response from the server
                InputStream inputStream = urlConnection.getInputStream();
                int responseCode = urlConnection.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Data inserted successfully";
                } else {
                    return "Failed to insert data";
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity() != null && result != null && result.equals("Data inserted successfully")) {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
            }
        }

    }
}