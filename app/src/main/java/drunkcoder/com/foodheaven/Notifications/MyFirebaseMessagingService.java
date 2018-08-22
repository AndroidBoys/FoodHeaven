package drunkcoder.com.foodheaven.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.Activities.MainActivity;
import drunkcoder.com.foodheaven.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // prepare a pending intent for the situation when the user taps the notification
        Intent i= new Intent(this, HomeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

        // build a notification
        Notification notification = new NotificationCompat.Builder(this,"download")
                .setTicker("Notification")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Hello from firebase")
                .setContentText(remoteMessage.getData().get("body"))
                .setContentIntent(pi) // pending intent to fire when notification clicked
                .setAutoCancel(true)
                .build();


        // post this notification using notification manager
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        // id here should be  unique for each notification since a new notification with an exisiting id will replace the
        // previous notification
        // This could be used to implement dynamic visiuls such as download progress etc.
        notificationManager.notify(0, notification);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

}
