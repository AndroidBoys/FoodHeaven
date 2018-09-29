package drunkcoder.com.foodheaven.Fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.Models.Address;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.R;

public class VerificationFragment extends Fragment implements View.OnClickListener {

    private EditText otpEditText;
    private TextView verificationTextView;
    private Button signupButton;

    //verification id that will be sent to the user
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthCredential credential;

    private AuthenticationActivity hostingActivity;
    private ProgressBar progressBar;
    private KProgressHUD kProgressHUD;

    private String mobile,name;
    private String email;
    private String password;
    private Address address;

    public static VerificationFragment newInstance(String email,String mobile,String password,Address userAddress,String name) {

        Bundle args = new Bundle();
        args.putString("mobile",mobile);
        args.putString("email",email);
        args.putString("password",password);
        args.putSerializable("userAddress", userAddress);
        args.putString("name",name);
        VerificationFragment fragment = new VerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_verification,container,false);
        hostingActivity = (AuthenticationActivity)getActivity();

        otpEditText = view.findViewById(R.id.otpEdittext);
        signupButton = view.findViewById(R.id.signupButton);
        progressBar = view.findViewById(R.id.progressbar);
        verificationTextView = view.findViewById(R.id.textView);

        signupButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


        mobile= getArguments().getString("mobile");
        email = getArguments().getString("email");
        password = getArguments().getString("password");
        address= (Address) getArguments().getSerializable("userAddress");
        name=getArguments().getString("name");


        sendVerificationCode();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signupButton:
                signUp();
                break;
        }
    }

    private void signUp(){
        kProgressHUD=KProgressHUD.create(hostingActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting you in")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        if(credential!=null)
        {
               registerUser();
        }
        else{
            verifyVerificationCode(otpEditText.getText().toString());
            registerUser();
        }
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpEditText.setText(code);
                otpEditText.setFocusable(false);
                otpEditText.setFocusableInTouchMode(false);

                //generating credential

                //verifying the code
                verifyVerificationCode(code);
                progressBar.setVisibility(View.GONE);

            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(hostingActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
           // mResendToken = forceResendingToken;
            //after sending the code progress bar will show till 5 sec
            new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    progressBar.setVisibility(View.GONE);
                }
            }.start();
        }
    };


    private void verifyVerificationCode(String otp) {

        //creating the credential
        credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        verificationTextView.setText("Phone number verified!");
    }


    private void registerUser()
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(hostingActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // add phone number to the email/ password login
                    mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("tag", "phone no. linked");
                                addUsertoDB();
                                hostingActivity.addDifferentFragment(SigninFragment.newInstance());
                            } else {
                                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(getContext(), "Please enter a valid otp", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                kProgressHUD.dismiss();
                                Toast.makeText(hostingActivity, "Sign up failed:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    kProgressHUD.dismiss();
                    Toast.makeText(hostingActivity, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

            private void addUsertoDB() {
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPhoneNumber(mobile);
                user.setPassword(password);
                user.setUserAddress(address);
                user.setSubscribedPlan(null);
                user.setWallet(null);

                // Entry into Users table
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(mAuth.getCurrentUser().getUid())
                        .setValue(user);
                kProgressHUD.dismiss();

                Toast.makeText(hostingActivity, "Signed up successfully:Please log in", Toast.LENGTH_SHORT).show();

            }

        }
