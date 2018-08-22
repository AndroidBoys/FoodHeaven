package drunkcoder.com.foodheaven.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartUpReciever extends BroadcastReceiver {

    // The method is executed when an appropriate broadcast(As per registered properties) is received
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Received Broadcast:",intent.getAction());

        // check the alarm state and fire the PollService to work in background on boot
        boolean isOn = QueryPreferences.isAlarmOn(context);
        NotificationService.setServiceAlarm(context, isOn);

    }



}
