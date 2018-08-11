package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Models.WhyHeavenFood;
import drunkcoder.com.foodheaven.R;

public class WhyHeavensFoodFragment extends Fragment {
    private TextView about1TextView,about2TextView;
    private ImageView about1ImageView,about2ImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.why_heaven_foods_fragment,container,false);
        about1ImageView=view.findViewById(R.id.aboutImage1);
        about2ImageView=view.findViewById(R.id.aboutImage2);
        about1TextView=view.findViewById(R.id.aboutTextView1);
        about2TextView=view.findViewById(R.id.aboutTextView2);

        fetchAboutDataFromFirebase();

        return view;
    }

    private void fetchAboutDataFromFirebase() {
        FirebaseDatabase.getInstance().getReference("WhyHeavenFood").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WhyHeavenFood whyHeavenFood=dataSnapshot.getValue(WhyHeavenFood.class);
                setData(whyHeavenFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setData(WhyHeavenFood whyHeavenFood) {
        Picasso.with(getContext()).load(whyHeavenFood.getImage1()).into(about1ImageView);
        Picasso.with(getContext()).load(whyHeavenFood.getImage2()).into(about2ImageView);
        about1TextView.setText(whyHeavenFood.getAbout1());
        about2TextView.setText(whyHeavenFood.getAbout2());
    }

    public WhyHeavensFoodFragment() {
    }

      public static WhyHeavensFoodFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WhyHeavensFoodFragment fragment = new WhyHeavensFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
