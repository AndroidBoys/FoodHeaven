package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Fragments.SigninFragment;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

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

        SigninFragment signinFragment = SigninFragment.newInstance();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            moveToHomeActivity();

        }
        else{

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

}
