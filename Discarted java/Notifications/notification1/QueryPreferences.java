package drunkcoder.com.foodheaven.Notifications;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {

    private static final String PREF_LAST_NOTIFICATION_ID ="lastresult" ;
    private static final String PREF_IS_ALARM_ON ="isOn" ;


    // get the saved last result id
    public static String getLastNotificationId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_NOTIFICATION_ID, "");
    }
    // save the last result id
    public static void setLastNotificationId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_NOTIFICATION_ID, lastResultId)
                .apply();
    }

    // saving and retrieving the state of alarm manager set for NotificationService in background
    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
    
    
    
}
