package drunkcoder.com.foodheaven.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Adapters.OurPlansCustomArrayAdapter;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.R;

public class OurPlansFragment extends Fragment {
    private ListView ourPlanslistView;
    private Activity activity;
    private ArrayList<Plan> planList;
    private OurPlansCustomArrayAdapter ourPlansCustomArrayAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        planList=new ArrayList<>();
        View view=inflater.inflate(R.layout.our_plans_fragment,container,false);
        ourPlanslistView=view.findViewById(R.id.ourPlansListview);

        fetchOurPlansListFromFirebase();

        activity = getActivity();
//        hostingActivity.getSupportActionBar().hide();

        ourPlansCustomArrayAdapter=new OurPlansCustomArrayAdapter((DescriptionActivity)activity, planList);
        ourPlanslistView.setAdapter(ourPlansCustomArrayAdapter);

        ourPlanslistView.setDivider(null);
        return view;
    }

    private void fetchOurPlansListFromFirebase() {
        FirebaseDatabase.getInstance().getReference("OurPlans").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Plan plan =dataSnapshot.getValue(Plan.class);
                planList.add(plan);
                if(ourPlansCustomArrayAdapter!=null)
                ourPlansCustomArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static OurPlansFragment newInstance() {

        Bundle args = new Bundle();
        OurPlansFragment fragment = new OurPlansFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DescriptionActivity)activity).setActionBarTitle("Our Plans");
    }
}
