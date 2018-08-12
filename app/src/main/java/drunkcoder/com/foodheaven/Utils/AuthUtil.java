package drunkcoder.com.foodheaven.Utils;

import android.text.TextUtils;
import android.util.Patterns;

public class AuthUtil {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target.toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isVailidPhone(CharSequence target){
        return (!TextUtils.isEmpty(target.toString().trim()) && Patterns.PHONE.matcher(target).matches()&&target.length()==10);

    }
}
