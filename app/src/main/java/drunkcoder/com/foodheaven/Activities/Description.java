package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Fragments.CallForAssistanceFragment;
import drunkcoder.com.foodheaven.Fragments.FaqFragment;
import drunkcoder.com.foodheaven.Fragments.OurPlansFragment;
import drunkcoder.com.foodheaven.Fragments.WantsToEatFragment;
import drunkcoder.com.foodheaven.Fragments.WeeklyMenuFragment;
import drunkcoder.com.foodheaven.Fragments.WeeklyMenuNestedFragment;
import drunkcoder.com.foodheaven.Fragments.WhyHeavensFoodFragment;
import drunkcoder.com.foodheaven.Models.WhyHeavenFood;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Description extends AppCompatActivity {

    private FrameLayout frameLayout;
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
                addDifferentFragment(OurPlansFragment.newInstance());
                break;
            case R.id.weeklyMenuButton:
                addDifferentFragment(WeeklyMenuFragment.newInstance());
                break;
            case R.id.callForAssistenceTextView:
                addDifferentFragment(CallForAssistanceFragment.newInstance());
                break;
            case R.id.faqTextView:
                addDifferentFragment(FaqFragment.newInstance());
                break;
            case R.id.whyHeavenFoodsTextView:
                addDifferentFragment(WhyHeavensFoodFragment.newInstance());
                break;
            case R.id.wantToEatTextView:
                addDifferentFragment(WantsToEatFragment.newInstance());
                break;

        }

    }


    private void addDifferentFragment(Fragment replacableFragment){
        Log.i("Inside","Different fragment function");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.descriptionFrameLayout,replacableFragment,null).commit();
    }

    //this method will call when user select a week day from the weeklyMenuFragment

  public void showTodaysMenu(View view){
        Toast.makeText(this, view.getTag().toString()+"selected", Toast.LENGTH_SHORT).show();
    addDifferentFragment(WeeklyMenuNestedFragment.newInstance(view.getTag().toString()));

    }

}
