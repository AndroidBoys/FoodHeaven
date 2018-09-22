package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Fragments.CallForAssistanceFragment;
import drunkcoder.com.foodheaven.Fragments.CurrentOrder;
import drunkcoder.com.foodheaven.Fragments.FaqFragment;
import drunkcoder.com.foodheaven.Fragments.OurPlansFragment;
import drunkcoder.com.foodheaven.Fragments.UserProfileFragment;
import drunkcoder.com.foodheaven.Fragments.WantsToEatFragment;
import drunkcoder.com.foodheaven.Fragments.WeeklyMenuFragment;
import drunkcoder.com.foodheaven.Fragments.WeeklyMenuNestedFragment;
import drunkcoder.com.foodheaven.Fragments.WhyHeavensFoodFragment;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class DescriptionActivity extends AppCompatActivity {

    private static final int R_id_profileId=1000;
    private FrameLayout frameLayout;
    private Fragment fragmentInForeground;
    private static final int R_id_currentOrder=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        frameLayout=findViewById(R.id.descriptionFrameLayout);

        Intent intent=getIntent();
        int viewId=intent.getIntExtra("ID",0);
        selectFragmentByViewId(viewId);

    }

    private void selectFragmentByViewId(int id) {
        switch(id){
            case R.id.ourPlansButton:
                OurPlansFragment fragment =OurPlansFragment.newInstance();
                fragmentInForeground=fragment;
                addDifferentFragment(fragment);
                break;
            case R.id.weeklyMenuButton:
                WeeklyMenuFragment fragment1=WeeklyMenuFragment.newInstance();
                fragmentInForeground=fragment1;
                addDifferentFragment(fragment1);
                break;
            case R.id.callForAssistenceTextView:
                CallForAssistanceFragment fragment2 = CallForAssistanceFragment.newInstance();
                fragmentInForeground = fragment2;
                addDifferentFragment(fragment2);
                break;
            case R.id.faqTextView:
                FaqFragment fragment3 = FaqFragment.newInstance();
                fragmentInForeground = fragment3;
                addDifferentFragment(fragment3);
                break;
            case R.id.whyHeavenFoodsTextView:
                WhyHeavensFoodFragment fragment4 = WhyHeavensFoodFragment.newInstance();
                fragmentInForeground = fragment4;
                addDifferentFragment(fragment4);
                break;
            case R.id.wantToEatTextView:
                WantsToEatFragment fragment5 = WantsToEatFragment.newInstance();
                fragmentInForeground = fragment5;
                addDifferentFragment(fragment5);
                break;
            case R_id_profileId:
                UserProfileFragment fragment6 = UserProfileFragment.newInstance();
                fragmentInForeground = fragment6;
                addDifferentFragment(fragment6);
                break;
            case R_id_currentOrder:
                CurrentOrder fragment7=CurrentOrder.newInstance();
                fragmentInForeground=fragment7;
                addDifferentFragment(fragment7);
                break;
        }

    }


    public void addDifferentFragment(Fragment replacableFragment){
        Log.i("Inside","Different fragment function");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.descriptionFrameLayout,replacableFragment,null).commit();
    }

    //this method will call when user select a week day from the weeklyMenuFragment

    public void showTodaysMenu(View view){
        Toast.makeText(this, view.getTag().toString()+" selected", Toast.LENGTH_SHORT).show();
        addDifferentFragment(WeeklyMenuNestedFragment.newInstance(view.getTag().toString()));


    }

}
