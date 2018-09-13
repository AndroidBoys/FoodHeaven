package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import drunkcoder.com.foodheaven.R;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private boolean isFirstTime;
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            finish();
            if(isFirstTime) {
                startActivity(new Intent(SplashActivity.this, AppIntroActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        SharedPreferences sharedPreferences=getSharedPreferences("drunkcoder.com.foodheaven",MODE_PRIVATE);
        isFirstTime=sharedPreferences.getBoolean("isFirstTime",true);

        handler.postDelayed(runnable,4000);
    }
}
