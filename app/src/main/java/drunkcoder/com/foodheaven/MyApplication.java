package drunkcoder.com.foodheaven;

import android.app.Application;

import drunkcoder.com.foodheaven.Models.User;

public class MyApplication extends Application {

    public static MyApplication thisApp;
    public static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        thisApp = this;
    }

    public static User getCurrentUser(){
        return thisApp.currentUser;
    }

}
