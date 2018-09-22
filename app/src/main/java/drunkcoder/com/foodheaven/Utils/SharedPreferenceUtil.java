package drunkcoder.com.foodheaven.Utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;

public class SharedPreferenceUtil {

    private static String NOTIFICATION_TOKEN="notification token";

    public static void saveNotificationToken(String token, Context context){

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(NOTIFICATION_TOKEN,token)
                .apply();
    }

    public static String getSavedNotificationToken(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NOTIFICATION_TOKEN, FirebaseInstanceId.getInstance().getToken());
    }




}
