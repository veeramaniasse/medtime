package com.simats.medtime;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskExample {

    private Context mContext; // Reference to the context

    public ScheduledTaskExample(Context context) {
        mContext = context; // Assign the context received from the caller
    }

    public void startScheduledTask(int[] alarmTimes) {

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        // Iterate over each alarm time
        for (int time : alarmTimes) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, time / 100); // Extract the hour part
            calendar.set(Calendar.MINUTE, time % 100); // Extract the minute part
            calendar.set(Calendar.SECOND, 0); // Set the second

            // Check if the alarm time is in the past
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                // If so, set it for the next day
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Create an intent to trigger the AlarmReceiver
            // Create an intent to trigger the AlarmReceiver
            Intent intent = new Intent(mContext, AlarmHelper.AlarmReceiver.class);
// Use FLAG_IMMUTABLE to specify that the PendingIntent is immutable
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, time, intent, PendingIntent.FLAG_IMMUTABLE);


            // Set the alarm using AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(alarmTimes.length);

        // Get the current time
        Calendar calendar1 = Calendar.getInstance();

        // Schedule tasks for each alarm time
        for (int time1 : alarmTimes) {
            // Set the alarm time
            calendar1.set(Calendar.HOUR_OF_DAY, time1 / 100); // Extract the hour part
            calendar1.set(Calendar.MINUTE, time1 % 100); // Extract the minute part
            calendar1.set(Calendar.SECOND, 0); // Set the second

            // Schedule the task to run at the specified time
            Date scheduledTime = calendar1.getTime();
            long initialDelay = scheduledTime.getTime() - System.currentTimeMillis();

            // If the time has already passed today, schedule for tomorrow
            if (initialDelay < 0) {
                initialDelay += 24 * 60 * 60 * 1000; // 24 hours
            }

            scheduler.scheduleAtFixedRate(() -> {
                // Your code to be executed at the scheduled time
                System.out.println("Executing the task at: " + new Date());
                // Add your code to set the alarm here
            }, initialDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS); // Repeat every 24 hours
        }

        // Shut down the scheduler gracefully when done
        // scheduler.shutdown();
    }
}
