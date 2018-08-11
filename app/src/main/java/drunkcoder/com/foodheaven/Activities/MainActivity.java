package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        startActivity(new Intent(this,AuthenticationActivity.class));
    }
}
