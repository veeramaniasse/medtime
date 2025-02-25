package com.simats.medtime;



import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class pat_home extends Fragment {

    GraphView graphView;

    private String username;
    private List<Medication> patientList1,patientList2,patientList3;
    private ListView listView1,listView2,listView3;
    private medicationAdapter adapter1,adapter2,adapter3;
    private int jam=14, menit=26;


    public pat_home(String user) {
        username=user;
    }
    public pat_home() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.pat_home, container, false);
        listView1 = view.findViewById(R.id.lst1);
        listView2 = view.findViewById(R.id.lst2);
        listView3 = view.findViewById(R.id.lst3);
        ImageView im1 = view.findViewById(R.id.imageView8);
        im1.setOnClickListener(view1 -> {
            Dialog myDialog = new Dialog(getActivity());
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.setContentView(R.layout.noti);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            Button yes = myDialog.findViewById(R.id.button8);
            Button no = myDialog.findViewById(R.id.button10);
            yes.setOnClickListener(vie -> {
                sendLoginRequest(username,"taken");
                myDialog.dismiss();
            });
            no.setOnClickListener(vie -> {
                sendLoginRequest(username,"not taken");
                myDialog.dismiss();
            });
        });
        ImageView im2 = view.findViewById(R.id.imageView10);
        im2.setOnClickListener(view1 -> {
            Intent it = new Intent(getActivity(), appt_status.class);
            it.putExtra("username", username);
            startActivity(it);
        });

        patientList1 = new ArrayList<>();
        patientList2 = new ArrayList<>();
        patientList3 = new ArrayList<>();

        adapter1 = new medicationAdapter(getActivity(), patientList1);
        adapter2 = new medicationAdapter(getActivity(), patientList2);
        adapter3 = new medicationAdapter(getActivity(), patientList3);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        Calendar calendar = Calendar.getInstance();

        // Add 10 seconds to the current time
        calendar.add(Calendar.SECOND, 3);
        long alarmTimeInMillis = calendar.getTimeInMillis();

        // Call the setAlarm() method from AlarmHelper class to set the alarm
        AlarmHelper.setAlarm(getContext(), alarmTimeInMillis);
//        Toast.makeText(getActivity(), "Set Alarm " + jam + " : " + menit, Toast.LENGTH_SHORT).show();
//        setTimer();
//        notification();



//        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
//        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
//
//        if (isFirstTime) {
//            // Set the alarm to trigger after 10 seconds
//            long delayInMillis = 10000; // 10 seconds
//            AlarmHelper.setAlarm(getActivity(), delayInMillis);
//
//            // Mark that the app has been opened for the first time
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isFirstTime", false);
//            editor.apply();
//        }
//        String timeString = "10:19";
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//
//        try {
//            Date targetTime = sdf.parse(timeString);
//            long targetTimeMillis = targetTime.getTime();
//
//            // Get the current time
//            Calendar calendar = Calendar.getInstance();
//            long currentTimeMillis = calendar.getTimeInMillis();
//
//            // Calculate the difference between the target time and current time
//            long delayInMillis = targetTimeMillis - currentTimeMillis;
//
//            // If the target time is in the past, add 1 day to the delay
//            if (delayInMillis < 0) {
//                calendar.add(Calendar.DAY_OF_MONTH, 1);
//                delayInMillis = targetTimeMillis - calendar.getTimeInMillis();
//            }
//
//            // Set the alarm with the calculated delay using AlarmHelper
//            AlarmHelper.setAlarm(getContext(), delayInMillis);
//
//            // Inform the user
//            // You can add a toast message or any other notification here
//        } catch (ParseException e) {
//            e.printStackTrace();
//            // Handle invalid input
//        }



        graphView=(GraphView) view.findViewById(R.id.graphView);

        Intent serviceIntent = new Intent(getActivity(), MyBackgroundService.class);
        getActivity().startService(serviceIntent);



        String url = ip.ipn +"medicationhomepage.php";
        fetchData(username);
        makeRequest(url);
        return view;
    }

    private void makeRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
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

        Volley.newRequestQueue(getContext()).add(stringRequest);

    }



    private void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject medicationDetails = jsonObject.getJSONObject("medication_details");

            List<String> allMedicationTimes = new ArrayList<>();

            // Parse and add first_intake to allMedicationTimes
            JSONArray firstIntake = medicationDetails.getJSONArray("first_intake");
            addMedicationTimesToList(firstIntake, allMedicationTimes);

            // Parse and add second_intake to allMedicationTimes
            JSONArray secondIntake = medicationDetails.getJSONArray("second_intake");
            addMedicationTimesToList(secondIntake, allMedicationTimes);

            // Parse and add third_intake to allMedicationTimes
            JSONArray thirdIntake = medicationDetails.getJSONArray("third_intake");
            addMedicationTimesToList(thirdIntake, allMedicationTimes);

            // Remove duplicates
            Set<String> uniqueTimesSet = new HashSet<>(allMedicationTimes);
            List<String> uniqueTimesList = new ArrayList<>(uniqueTimesSet);

            // Round times to nearest half-hour
            List<String> roundedTimes = new ArrayList<>();
            for (String time : uniqueTimesList) {
                roundedTimes.add(roundTimeToNearestHalfHour(time));
            }

            System.out.println("Rounded Medication Times:");




            // Clear existing data
            patientList1.clear();
            patientList2.clear();
            patientList3.clear();

            // Parse and add first_intake to patientList1
            //JSONArray firstIntake = medicationDetails.getJSONArray("first_intake");
            for (int i = 0; i < firstIntake.length(); i++) {
                System.out.println(i);
                JSONObject medication = firstIntake.getJSONObject(i);
                String medicationName = medication.getString("medication_name");
                String beforeFood = medication.getString("before_food");
                String time = medication.getString("Timing");
                Medication medicationDetail = new Medication(medicationName, beforeFood,time);
                patientList1.add(medicationDetail);
            }
            adapter1.notifyDataSetChanged();

            // Parse and add second_intake to patientList2
            //JSONArray secondIntake = medicationDetails.getJSONArray("second_intake");
            for (int i = 0; i < secondIntake.length(); i++) {
                System.out.println(i);
                JSONObject medication = secondIntake.getJSONObject(i);
                String medicationName = medication.getString("medication_name");
                String beforeFood = medication.getString("before_food");
                String time = medication.getString("Timing");
//                Medication medicationDetail =
                patientList2.add(new Medication(medicationName, beforeFood,time));
            }
            adapter2.notifyDataSetChanged();

            // Parse and add third_intake to patientList3
            //JSONArray thirdIntake = medicationDetails.getJSONArray("third_intake");
            for (int i = 0; i < thirdIntake.length(); i++) {
                System.out.println(i);
                JSONObject medication = thirdIntake.getJSONObject(i);
                String medicationName = medication.getString("medication_name");
                String beforeFood = medication.getString("before_food");
                String time = medication.getString("Timing");
                Medication medicationDetail = new Medication(medicationName, beforeFood,time);
                patientList3.add(medicationDetail);
            }
            adapter3.notifyDataSetChanged();
            setListViewHeightBasedOnItems(listView1);
            setListViewHeightBasedOnItems(listView2);
            setListViewHeightBasedOnItems(listView3);

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }




    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"graph.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse22(response);
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
                data.put("patient_id", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequest);

    }


    private void handleResponse22(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString("status");
            if (status.equals("success")) {
                JSONArray hba1cDates = jsonResponse.getJSONArray("HbA1c_dates");
                int length = hba1cDates.length();
                DataPoint[] dataPoints = new DataPoint[length];
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());

                for (int i = 0; i < length; i++) {
                    JSONObject entry = hba1cDates.getJSONObject(i);
                    String hba1c = entry.getString("HbA1c").replace("%", "");
                    String dateStr = entry.getString("date");

                    // Parse date string to Date object
                    Date date = dateFormat.parse(dateStr);

                    // Create a DataPoint with x as Date and y as HbA1c value
                    dataPoints[i] = new DataPoint(date.getTime(), Double.parseDouble(hba1c));
                }

                // Sort the DataPoint array by x-values in ascending order
                Arrays.sort(dataPoints, Comparator.comparingDouble(DataPoint::getX));

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                series.setColor(Color.parseColor("#2D2F6A"));
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(5);

                // Add series to the graph
                graphView.addSeries(series);

                // Customize the viewport if needed
                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(dataPoints[0].getX()); // Set your min X value
                graphView.getViewport().setMaxX(dataPoints[length - 1].getX() + TimeUnit.DAYS.toMillis(1)); // Set your max X value
                graphView.getViewport().setYAxisBoundsManual(true);
                graphView.getViewport().setMinY(0); // Set your min Y value

                // Customize x-axis label
                GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
                gridLabelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), dateFormat));
                gridLabelRenderer.setHorizontalAxisTitle("Date");
                gridLabelRenderer.setHorizontalAxisTitleColor(Color.BLACK);
                gridLabelRenderer.setHorizontalAxisTitleTextSize(24);

                // Set the label for the y axis
                gridLabelRenderer.setVerticalAxisTitle("HbA1c (%)");
                // Customize y-axis label
                gridLabelRenderer.setVerticalAxisTitleColor(Color.BLACK);
                gridLabelRenderer.setVerticalAxisTitleTextSize(24);

                // Customize grid lines
                gridLabelRenderer.setGridColor(Color.LTGRAY);
                gridLabelRenderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
                graphView.getGridLabelRenderer().setPadding(10);
            } else {
                // Handle error case if needed
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            // Handle JSON parsing error or date parsing error
        }
    }




    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }


    private void addMedicationTimesToList(JSONArray intake, List<String> allMedicationTimes) throws JSONException {
        for (int i = 0; i < intake.length(); i++) {
            JSONObject medication = intake.getJSONObject(i);
            String time = medication.getString("Timing");
            allMedicationTimes.add(time);
        }
    }

    private String roundTimeToNearestHalfHour(String time) {
        // Split time into hours and minutes
        String[] parts = time.split(":");
        if (parts.length != 2) {
            // Handle unexpected format
            return ""; // or throw an exception
        }

        int hours;
        int minutes;

        // Check if the minutes part contains additional information like AM/PM
        String[] minutesParts = parts[1].split("\\s+"); // Split on whitespace
        if (minutesParts.length == 2) {
            // Extract hours and minutes
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(minutesParts[0]);
            String amPm = minutesParts[1];

            // Convert 12-hour format to 24-hour format
            if (amPm.equalsIgnoreCase("PM")) {
                hours += 12;
            }
        } else {
            // Extract hours and minutes directly
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
        }

        // Round minutes to the nearest half-hour
        if (minutes < 15) {
            minutes = 0;
        } else if (minutes < 45) {
            minutes = 30;
        } else {
            minutes = 0;
            hours++; // Move to the next hour
        }

        // Format the rounded time
        return String.format("%02d:%02d", hours, minutes);
    }



//    private void notification() {
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "Alarm Reminders";
//            String description = "Hey, Wake Up!!";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel  = new NotificationChannel("Notify", name,importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void setTimer() {
//        AlarmManager alarmManager  = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//
//        Date date = new Date();
//
//        Calendar cal_alarm = Calendar.getInstance();
//        Calendar cal_now = Calendar.getInstance();
//
//        cal_now.setTime(date);
//        cal_alarm.setTime(date);
//
//        cal_alarm.set(Calendar.HOUR_OF_DAY, jam);
//        cal_alarm.set(Calendar.MINUTE, menit);
//        cal_alarm.set(Calendar.SECOND, 0);
//
//        if(cal_alarm.before(cal_now)){
//            cal_alarm.add(Calendar.DATE, 1);
//        }
//
//        Intent i = new Intent(getActivity(), MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, i, PendingIntent.FLAG_IMMUTABLE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(),pendingIntent);
//
//
//        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    private void createNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "akchannel";
//            String desc = "Channel for Alarm Manager";
//            int imp = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
//            channel.setDescription(desc);
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    private void sendLoginRequest(final String username, final String password) {
        String URL = ip.ipn+"Patnotify.php";
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username and password as POST parameters
                Map<String, String> data = new HashMap<>();
                data.put("patient_id", username);
                data.put("medicine_taken", password);
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
    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        // Handle your JSON response here without assuming a 'status' field
        // You can parse the response and handle success/failure accordingly
        try {
            // Example: Check if the response contains "success"
            if (response.toLowerCase().contains("Succes")) {
                Toast.makeText(getActivity(), "Updated successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                intent.putExtra("username", username);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Log.d("JSON Response", response);
                Toast.makeText(getActivity(), "updation failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle network request errors


}