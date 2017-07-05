package com.vikram.locateme.firbase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vikram.locateme.R;
import com.vikram.locateme.ui.location.MapsActivity;
import com.vikram.locateme.ui.main.MainActivity;
import com.vikram.locateme.utils.Constants;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MessagingService", "From: " + remoteMessage.getFrom());
        Log.d("MessagingService", "Message Body: " + remoteMessage.getData());

        String isShare = remoteMessage.getData().get("share");
        if (isShare != null) {
            shareLocationNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("text"));
        } else {
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("text"));

            if (remoteMessage.getData().get("title") != null && remoteMessage.getData().get("title").contains("Approve")) {
                Intent intent = new Intent();
                intent.setAction(Constants.APPROVE_INTENT);
                sendBroadcast(intent);
            } else if (remoteMessage.getData().get("title") != null && remoteMessage.getData().get("title").contains("Connected")) {
                Intent intent = new Intent();
                intent.setAction(Constants.CONNECTED_INTENT);
                sendBroadcast(intent);

                Intent intent2 = new Intent();
                intent2.setAction(Constants.PENDING_INTENT);
                sendBroadcast(intent2);
            }
        }
    }

    private void sendNotification(String title, String detail) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.drawable.ic_stat_location_on)
                        .setContentTitle(title)
                        .setContentText(detail)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m, builder.build());
    }

    private void shareLocationNotification(String title, String latLon) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("latLon", latLon);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.drawable.ic_stat_location_on)
                        .setContentTitle(title)
                        .setContentText(latLon)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m, builder.build());
    }
}
