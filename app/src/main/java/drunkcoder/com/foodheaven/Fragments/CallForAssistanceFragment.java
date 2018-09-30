package drunkcoder.com.foodheaven.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Adapters.CallForAssistanceArrayAdapter;
import drunkcoder.com.foodheaven.Models.Assistance;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.ProgressUtils;

public class CallForAssistanceFragment extends Fragment {

    private ListView listView;
    private ArrayList<Assistance> assistanceList=new ArrayList<>();
    private CallForAssistanceArrayAdapter callForAssistanceArrayAdapter;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.call_for_assistance_fragment,container,false);
        activity=getActivity();
        listView=view.findViewById(R.id.callForAssistenceListView);
        callForAssistanceArrayAdapter=new CallForAssistanceArrayAdapter(getContext(),assistanceList);
        listView.setAdapter(callForAssistanceArrayAdapter);
        ProgressUtils.showLoadingDialog(activity);
        fetchAssistanceFromFirebase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v=view.findViewById(R.id.phoneNo);

                dialNo(v.getText().toString());
            }

        });
        listView.setDivider(null);
        return view;
    }

    private void fetchAssistanceFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Assistances").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                assistanceList.add(dataSnapshot.getValue(Assistance.class));
                if(callForAssistanceArrayAdapter!=null)
                callForAssistanceArrayAdapter.notifyDataSetChanged();
                ProgressUtils.cancelLoading();
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

    public CallForAssistanceFragment() {
    }

    public static CallForAssistanceFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CallForAssistanceFragment fragment = new CallForAssistanceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void dialNo(String s) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + s));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DescriptionActivity)activity).setActionBarTitle("Call For Assistance");
    }
}
