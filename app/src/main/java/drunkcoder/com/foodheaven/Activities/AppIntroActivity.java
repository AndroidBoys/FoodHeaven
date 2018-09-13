package drunkcoder.com.foodheaven.Activities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Fragments.AppIntroFragments.AppIntroFragment1;
import drunkcoder.com.foodheaven.Fragments.AppIntroFragments.AppIntroFragment2;
import drunkcoder.com.foodheaven.Fragments.AppIntroFragments.AppIntroFragment3;
import drunkcoder.com.foodheaven.Fragments.AppIntroFragments.AppIntroFragment4;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_app_intro);

        getSupportActionBar().hide();

        addSlide(AppIntroFragment1.newInstance());
        addSlide(AppIntroFragment2.newInstance());
        addSlide(AppIntroFragment3.newInstance());
        addSlide(AppIntroFragment4.newInstance());

        showSkipButton(false);
        showDoneButton(true);

        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar or actionBar
        showStatusBar(false);


        // Edit the color of the nav bar on Lollipop+ devices
        setNavBarColor(R.color.purpleColor);


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences sharedPreferences=getSharedPreferences("drunkcoder.com.foodheaven",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isFirstTime",false).apply();
        startActivity(new Intent(this, AuthenticationActivity.class));

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

}
