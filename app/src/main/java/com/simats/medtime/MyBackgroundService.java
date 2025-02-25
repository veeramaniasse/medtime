package com.simats.medtime;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyBackgroundService extends Service {

    private ScheduledExecutorService scheduler;

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = createNotification();
            startForeground(1, notification);
        }
        // Create and show a notification


        startScheduledTask();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startScheduledTask() {
        scheduler = Executors.newScheduledThreadPool(1);

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19); // Set the hour
        calendar.set(Calendar.MINUTE, 13); // Set the minute
        calendar.set(Calendar.SECOND, 0); // Set the second

        // Schedule the task to run at the specified time
        Date scheduledTime = calendar.getTime();
        long initialDelay = scheduledTime.getTime() - System.currentTimeMillis();

        // If the time has already passed today, schedule for tomorrow
        if (initialDelay < 0) {
            initialDelay += 24 * 60 * 60 * 1000; // 24 hours
        }

        scheduler.scheduleAtFixedRate(() -> {
            // Your code to be executed at the scheduled time
            System.out.println("Executing the task at: " + new Date());
        }, initialDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS); // Repeat every 24 hours
    }

    private Notification createNotification() {
        // Create a notification channel (required for Android Oreo and above)
        String channelId = "my_channel";
        String channelName = "My Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("My Background Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_launcher_foreground); // Set your notification icon here

        return builder.build();
    }
}
