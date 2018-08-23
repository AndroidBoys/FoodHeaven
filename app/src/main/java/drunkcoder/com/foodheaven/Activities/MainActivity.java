package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import drunkcoder.com.foodheaven.R;


import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        startActivity(new Intent(this, AuthenticationActivity.class));

//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("todayMenu");
//        FoodMenu todayMenu=new FoodMenu();
//        todayMenu.setImageUrl("http://medifoods.my/wp-content/uploads/2015/03/cover-menu-fingerfoods2.jpg");
//        todayMenu.setFoodName("Apna Food");
//        todayMenu.setFoodDescription("looking good");
//        todayMenu.setFoodQuantity("4");
//        databaseReference.child("lunch").child("04").setValue(todayMenu).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                startActivity(new Intent(MainActivity.this, HomeActivity.class));
//            }
//        });}
    }
}
