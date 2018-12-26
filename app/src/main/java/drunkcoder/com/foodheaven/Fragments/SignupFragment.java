package drunkcoder.com.foodheaven.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labo.kaji.fragmentanimations.PushPullAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.Models.Address;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.AuthUtil;

public class SignupFragment extends Fragment implements View.OnClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private AuthenticationActivity hostingActivity;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText,passwordVerificationEditText,
            nameEditText;
    private Button otpButton;
    private TextView loginTextview;

    private String mobileNumber;
    private String email;
    private boolean passwordVisible=false;
    private String password,name;
    private Address userAddress;

    private ImageView passwordVerificationImageView,passwordImageView;

    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private List<User> users;
    private  boolean passwordVerificationVisible=false;



    public static SignupFragment newInstance() {

        Bundle args = new Bundle();

        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        hostingActivity = (AuthenticationActivity) getActivity();


        fetchUsers();

        passwordVerificationEditText=view.findViewById(R.id.passwordVerificationEdittext);
        passwordVerificationImageView=view.findViewById(R.id.passwordVerificationImageView);
        passwordImageView=view.findViewById(R.id.passwordImageView);


        emailEditText = view.findViewById(R.id.emailEdittext);
        phoneEditText = view.findViewById(R.id.phonenumberEdittext);
        passwordEditText = view.findViewById(R.id.passwordEdittext);
        otpButton = view.findViewById(R.id.sendOtpButton);
        loginTextview = view.findViewById(R.id.loginTextview);
        nameEditText = view.findViewById(R.id.nameEdittext);

        placeAutocompleteFragment=(PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.addressAutoCompleteFragment);
        //hiding search button before fragment
        placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        //setting hint for ediittext
        EditText place;
        place= ((EditText)placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input));
        place.setHint("Enter Your Address");
        place.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);
        place.setTextColor(Color.WHITE);
        place.setTypeface(Typeface.DEFAULT);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
              userAddress= new Address((String) place.getAddress(),String.valueOf(place.getLatLng().longitude),String.valueOf(place.getLatLng().latitude));
//            removePlaceFragment();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getActivity(), ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        passwordVerificationImageView.setOnClickListener(this);
        passwordImageView.setOnClickListener(this);
        otpButton.setOnClickListener(this);
        loginTextview.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int start,end;
        switch (view.getId()) {

            case R.id.sendOtpButton:
//                removePlaceFragment();
                registerUser();
                break;
            case R.id.loginTextview:

                removePlaceFragment();
                hostingActivity.addDifferentFragment(SigninFragment.newInstance());
                break;

            case R.id.passwordImageView:

                //saving cursor's positions
                start =passwordEditText.getSelectionStart();
                end=passwordEditText.getSelectionEnd();

                if(passwordVisible){
                    passwordVisible=false;
                    passwordImageView.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
                else{
                    passwordVisible=true;
                    passwordEditText.setTransformationMethod(null);
                    passwordImageView.setImageResource(R.drawable.ic_visibility_black_24dp);
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                passwordEditText.setSelection(start, end);
                break;

            case R.id.passwordVerificationImageView:
                //saving cursor's positions
                start =passwordVerificationEditText.getSelectionStart();
                end=passwordVerificationEditText.getSelectionEnd();

                if(passwordVerificationVisible){
                    passwordVerificationVisible=false;
                    passwordVerificationImageView.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    passwordVerificationEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
                else{
                    passwordVerificationVisible=true;
                    passwordVerificationEditText.setTransformationMethod(null);
                    passwordVerificationImageView.setImageResource(R.drawable.ic_visibility_black_24dp);
                    passwordVerificationEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                passwordVerificationEditText.setSelection(start, end);
                break;


        }

    }

    private void removePlaceFragment() {

        getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.addressAutoCompleteFragment)).commit();

    }


    public void registerUser()
    {
        mobileNumber = phoneEditText.getText().toString().trim();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        name=nameEditText.getText().toString();

        if(userAddress==null){
            Toast.makeText(hostingActivity, "Please enter address first", Toast.LENGTH_SHORT).show();
            return;
        }
        if( !(AuthUtil.isValidEmail(email)))
        {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }
        if(!(AuthUtil.isVailidPhone(mobileNumber))){
            phoneEditText.setError("Enter a valid mobile number");
            phoneEditText.requestFocus();
            return;
        }

        if(passwordEditText.getText().toString().length()<6)
        {
            passwordEditText.setError("Password should have atleast 6 characters");
            passwordEditText.requestFocus();
            return;
        }
        if(!passwordVerificationEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            passwordVerificationEditText.setError("Password mismatched");
            passwordVerificationEditText.requestFocus();
            return;
        }

        removePlaceFragment();
        if(!checkAlreadyExists()) {
            verifyPhoneNumber();
        }else {
            Toast.makeText(hostingActivity, "You are already registered,Please Login", Toast.LENGTH_SHORT).show();
            hostingActivity.addDifferentFragment(SigninFragment.newInstance());

        }

    }

    public void verifyPhoneNumber()
    {

        hostingActivity.addDifferentFragment(VerificationFragment.newInstance(email,mobileNumber,password,userAddress,name));

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.LEFT,enter,1000);
    }

    public void fetchUsers()
    {
        users = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
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

    public boolean checkAlreadyExists()
    {
        for(int i=0;i<users.size();i++)
        {
            User user = users.get(i);
            if(user.getEmail().equals(email)||user.getPhoneNumber().equals(mobileNumber)){
                return true;
            }
        }

        return false;
    }

}
