package drunkcoder.com.foodheaven.Notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.R;

public class NotificationService extends IntentService {


    private static final String TAG="NotificationService";

    // Set interval to 1 minute
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION";

    private String notificationId;


    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;

    // It must call the super constructor with the IntentService subclass name for proper implementation of the service
    public NotificationService()
    {
        super(TAG);

    }

    // All the commands(tasks) assigned to the service in form of intents are handled here
    // The intent service calls this method for each command on the background thread it has created
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // if network is not available for background service than return without trying to fetch
        if(!isNetworkAvailableorConnected())
        {
            return;
        }
        // if the network is available  for background task than proceed further to fetch
        else {
            Log.i(TAG, "Recieved Intent:" + intent);
            //    1. Pull out the current notification id from the default SharedPreferences .
           //    2. Fetch the latest notification from firebase  .
         //    4. Check to see whether it is different from the last notification ID.
        //    5. Store the first result back in SharedPreferences .

            fetchNotificationFromFirebase();

        }
    }

    // checks the availability of network for background tasks which  user might have turned off
    private boolean isNetworkAvailableorConnected() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // returns null if the user have turned off the background network permission even if there is network available on the
        // foreground
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        // cheacking the connection
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    // intent for starting this service
    public static Intent newIntent(Context context)
    {
        return new Intent(context,NotificationService.class);
    }

    // method is triggered from frontend like fragment to turn the search poll on or off in background
    public static void setServiceAlarm(Context context,boolean isOn)
    {
        // create a new intent which starts this service
        Intent i = NotificationService.newIntent(context);

        // prepare a pending intent with the intent created above , so that this service could be started
        // even when this app process dies
        PendingIntent pi = PendingIntent.getService(context,0,i,0);

        // get an AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // if the serviceAlarm is turned on set a alarm manager with configured pending intent
        if(isOn)
        {  // first parameter specify the type of time(time since the device booting in this case)
            // second parameter specify the starting time
            // Above two parameters can also be set for the real clock time
            // third parameter: time upto which the alarm should go off min. is 60s allowed in stock android
            // fourth: pending intent to fire when the alarm goes off
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),POLL_INTERVAL_MS,pi);
        }
        // else if the alarm is to be turned off cancel the alarm manager with the pending intent
        else
        {
            alarmManager.cancel(pi);
            pi.cancel();

        }
        // save the alarm state to preferences
        QueryPreferences.setAlarmOn(context, isOn);
    }

    // method to check the service alarm is on or not
    public static boolean isServiceAlarmOn(Context context) {
        // creating a pending intent with the same intent returns the old pending intent
        // for each pending intent there is one alarm manager instance so the alarm manager could be identified with the pending intent
        Intent i = NotificationService.newIntent(context);
        // the flag no create tells to return null if the pending intent do not exists already
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        // if pending intent is not null means it exsists already , than the alarm must be on so return true or false accordingly
        return pi != null;
    }

    public void fetchNotificationFromFirebase(){

        FirebaseDatabase.getInstance().getReference("notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drunkcoder.com.foodheaven.Models.Notification notification = dataSnapshot.getValue(drunkcoder.com.foodheaven.Models.Notification.class);
                notificationId = notification.getId();
                checkForNewNotification(notification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void checkForNewNotification(drunkcoder.com.foodheaven.Models.Notification notification1){

        String lastNotificationId = QueryPreferences.getLastNotificationId(this);


        // no updates in notifications
        if(lastNotificationId.equals(notificationId))
        {
            Log.i("old notification:",notificationId);
        }
        // new updates in search results show notification to the user
        else
        {
            Log.i("new notification:",notificationId);


            // prepare a pending intent for the situation when the user taps the notification
            Intent i= new Intent(this, HomeActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

            // build a notification
            Notification notification = new NotificationCompat.Builder(this,"download")
                    .setTicker("Notification")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("Hello from firebase")
                    .setContentText(notification1.getMessage())
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



            // Broadcast the information that a new result is found
            sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION));

        }

        // save the last notification id for next checking
        QueryPreferences.setLastNotificationId(this,notificationId);


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
