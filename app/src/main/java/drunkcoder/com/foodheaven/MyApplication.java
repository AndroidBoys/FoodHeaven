package drunkcoder.com.foodheaven;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import drunkcoder.com.foodheaven.Models.User;

public class MyApplication extends Application {

    public static MyApplication thisApp;
    public static User currentUser;
    public static boolean notificationStatus;
    public static boolean dinnerStatus;
    public static boolean lunchStatus;
    public static boolean breakFastStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        thisApp = this;

        checkNotificationStatus();
        checkDinnerStatus();
        checkLunchStatus();
        checkBreakFastStatus();

    }

    private void checkBreakFastStatus() {
        FirebaseDatabase.getInstance().getReference("TodayMenu").child("BreakFast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                    breakFastStatus=true;
                else
                    breakFastStatus=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkLunchStatus() {
        FirebaseDatabase.getInstance().getReference("TodayMenu").child("Lunch").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue()!=null)
                lunchStatus=true;
            else
                lunchStatus=false;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
    }

    private void checkDinnerStatus() {
        FirebaseDatabase.getInstance().getReference("TodayMenu").child("Dinner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                    dinnerStatus=true;
                else
                    dinnerStatus=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkNotificationStatus() {
        FirebaseDatabase.getInstance().getReference("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    notificationStatus = true;
                else
                    notificationStatus = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static User getCurrentUser(){
        return thisApp.currentUser;
    }

}
