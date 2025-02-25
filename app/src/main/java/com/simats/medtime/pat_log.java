package com.simats.medtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class pat_log extends AppCompatActivity {
    private EditText eid, epassword;

    private String username, password;
    ImageView imageView;

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            } else {
//                showNotification();
            }
        } else {
//            showNotification(); // No permission needed for older versions
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_log);
        Button bt= findViewById(R.id.button2);
        eid = findViewById(R.id.editTextText1);
        imageView = findViewById(R.id.imageView3);
        epassword = findViewById(R.id.editTextText);
        bt.setOnClickListener(view -> {
            username = eid.getText().toString().trim();
            password = epassword.getText().toString().trim();
            checkNotificationPermission();
            if (!username.isEmpty() && !password.isEmpty()) {
                System.out.println(username);
                sendLoginRequest(username, password);
            } else {
                Toast.makeText(pat_log.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
//        cancelAllAlarms(recon)
        TextView t1= findViewById(R.id.textView4);
        t1.setOnClickListener(view -> {
            Intent it = new Intent(this, p_fgt_pswd.class);
            startActivity(it);
        });

        imageView.setOnClickListener(v -> {
//            setAlarm(this, 9, 43);
        });

    }

    public void setAlarm(Context context, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set time for the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Set exact alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void sendLoginRequest(final String username, final String password) {
        String URL = ip.ipn+"user.php";
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
//                AlarmHelper.setAlarm(this, alarmTimeInMillis);
                Intent intent = new Intent(pat_log.this, MainActivity3.class);
                intent.putExtra("username", username);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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