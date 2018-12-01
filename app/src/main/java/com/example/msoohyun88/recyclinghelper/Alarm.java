package com.example.msoohyun88.recyclinghelper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

    public static final String CHANNEL_1_ID = "channel1";
    private final int MY_NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            notificationManager.createNotificationChannel(channel1);
        }

        Intent restartMainActivityIntent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, restartMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle("Recycling Reminder Title")
                    .setContentText("Recycling Reminder Text")
                    .setAutoCancel(true);

        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());

        Toast.makeText(context, "Alarm fired.", Toast.LENGTH_SHORT).show();

    }

}
