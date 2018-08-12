package drunkcoder.com.foodheaven.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.R;

public class UnsubscribedUser extends Fragment implements BaseSliderView.OnSliderClickListener,View.OnClickListener{
    private SliderLayout bannerSlider;
    private ImageView packsImageView;
    private Button ourPlansButton,
                    weeklyMenuButton;
    private TextView whyHeavenFoodsTextView,
            faqTextView,
            callForAssistanceTextView,
            attractUserTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.unsubscribed_user,container,false);

        packsImageView=view.findViewById(R.id.packsImageView);
        bannerSlider= view.findViewById(R.id.bannerSlider);
        ourPlansButton=view.findViewById(R.id.ourPlansButton);
        weeklyMenuButton=view.findViewById(R.id.weeklyMenuButton);
        whyHeavenFoodsTextView =view.findViewById(R.id.whyHeavenFoodsTextView);
        callForAssistanceTextView=view.findViewById(R.id.callForAssistenceTextView);
        faqTextView=view.findViewById(R.id.faqTextView);
        attractUserTextView=view.findViewById(R.id.attractUser);

        //setting clicklistener
        ourPlansButton.setOnClickListener(this);
        callForAssistanceTextView.setOnClickListener(this);
        weeklyMenuButton.setOnClickListener(this);
        whyHeavenFoodsTextView.setOnClickListener(this);
        faqTextView.setOnClickListener(this);

        getBannerFromFirebase();
        getpackImageFromFirebase();
        getAttractiveSloganFromFirebase();



        return view;
    }

    private void getAttractiveSloganFromFirebase() {
    FirebaseDatabase.getInstance().getReference("AttractiveQuotes").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            attractUserTextView.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void setBanners(String imageUrl) {

            TextSliderView textSliderView=new TextSliderView(getContext());
            textSliderView.image(imageUrl)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            bannerSlider.addSlider(textSliderView);
    }

    private void getBannerFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Banners").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("urls",dataSnapshot.getValue().toString());
                setBanners(dataSnapshot.getValue().toString());

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
    private void getpackImageFromFirebase(){
        FirebaseDatabase.getInstance().getReference("Pack").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.getValue().toString()).into(packsImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static UnsubscribedUser newInstance(){
        return new UnsubscribedUser();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        onClick(ourPlansButton);
    }

    @Override
    public void onClick(View view) {

        Intent intent=new Intent(getContext(), DescriptionActivity.class);
        intent.putExtra("ID",view.getId());//PASSING TAG AS VIEW ID
        startActivity(intent);

    }
    @Override
    public void onStop() {
        bannerSlider.stopAutoCycle();
        super.onStop();
    }
}
