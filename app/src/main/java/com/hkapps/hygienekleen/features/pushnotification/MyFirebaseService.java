package com.hkapps.hygienekleen.features.pushnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.hkapps.hygienekleen.R;
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.DailyClosingManagementActivity;
import com.hkapps.hygienekleen.features.splash.ui.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();
        if (!data.isEmpty()) {
            String title = data.get("title");
            String body = data.get("body");
            String notifType = data.get("type");
            String target = data.get("extra");

            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
//        if (title.equals("New CTalk") || title == "New CTalk") {
//            Intent intent = new Intent(this, NotifVendorMidLevelActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(
//                    Context.NOTIFICATION_SERVICE);
//
//            PendingIntent pendingIntent = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//                pendingIntent = PendingIntent.getActivity
//                        (this, 0, intent, PendingIntent.FLAG_MUTABLE);
//            } else {
//                pendingIntent = PendingIntent.getActivity
//                        (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                createNotificationChannel(notificationManager);
//            }
//
////        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        Uri soundUri = Uri.parse("android.resource://"+getPackageName()+"/raw/notif.mp3");
//            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notif);
//            Notification notification = new NotificationCompat.Builder(this, "696969")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setAutoCancel(true)
//                    .setSound(soundUri)
//                    .setContentIntent(pendingIntent)
//                    .build();
//
//            notificationManager.notify(new Random().nextInt(), notification);
//        } else {
            Intent intent;
            Log.d("TESTED",title);
            if (title.toLowerCase().contains("closing".toLowerCase())) {
                intent = new Intent(this, DailyClosingManagementActivity.class);
            } else {
                intent = new Intent(this, SplashActivity.class);
            }
//            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity
                        (this, 0, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity
                        (this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager);
            }

//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri soundUri = Uri.parse("android.resource://"+getPackageName()+"/raw/notif.mp3");
            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notiff);
            Notification notification = new NotificationCompat.Builder(this, "696969")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(body)))
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(new Random().nextInt(), notification);
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "carefast-channel";
        NotificationChannel notificationChannel = new NotificationChannel("696969",
                channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(
                new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.CYAN);
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notiff);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        notificationChannel.setSound(soundUri, audioAttributes);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}

