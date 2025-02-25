package com.simats.medtime;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmHelper {
    private static final String CHANNEL_ID = "alarm_channel";

    public static void setAlarm(Context context, long alarmTimeInMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent);
    }


    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            playAlarm(context);
            createNotification(context);
        }

        private void playAlarm(Context context) {
            Uri alarmSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/audio");
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmSoundUri);
            if (ringtone != null) {
                ringtone.play();
            }
        }

        private void createNotification(Context context) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Alarm Channel";
                String description = "Channel for Alarm";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
            long[] vibratePattern = {0, 1000};
            Uri alarmSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/audio");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.smclogo)
                    .setContentTitle("Medication Reminder")
                    .setContentText("Kindly Take your medicine!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(alarmSoundUri)
                    .setVibrate(vibratePattern)
                    .setAutoCancel(true);

            notificationManager.notify(0, builder.build());
        }
    }
}
