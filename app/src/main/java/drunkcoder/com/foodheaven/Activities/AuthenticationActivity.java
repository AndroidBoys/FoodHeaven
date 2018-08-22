package drunkcoder.com.foodheaven.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Fragments.SigninFragment;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportActionBar().hide();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
           initCurrentUser();
        }
        else{
            SigninFragment signinFragment = SigninFragment.newInstance();
            addDifferentFragment(signinFragment);
        }
    }

    public void moveToHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }



    public void addDifferentFragment(Fragment replacableFragment){
        Log.i("Inside","Different fragment function");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment,null).commit();
    }

    public void initCurrentUser(){

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MyApplication.currentUser = dataSnapshot.getValue(User.class);
                        moveToHomeActivity();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

}
