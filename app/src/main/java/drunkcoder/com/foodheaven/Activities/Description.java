package drunkcoder.com.foodheaven.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Fragments.CallForAssistanceFragment;
import drunkcoder.com.foodheaven.Fragments.OurPlansFragment;
import drunkcoder.com.foodheaven.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

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
                addDifferentFragment(OurPlansFragment.newInstance());
                break;
            case R.id.callForAssistenceTextView:
                addDifferentFragment(CallForAssistanceFragment.newInstance());
                break;
            case R.id.faqTextView:
                addDifferentFragment(OurPlansFragment.newInstance());
                break;
            case R.id.whyHeavenFoodsTextView:
                addDifferentFragment(OurPlansFragment.newInstance());
                break;
        }

    }


    private void addDifferentFragment(Fragment replacableFragment){
        Log.i("Inside","Different fragment function");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.descriptionFrameLayout,replacableFragment,null).commit();
    }

}
