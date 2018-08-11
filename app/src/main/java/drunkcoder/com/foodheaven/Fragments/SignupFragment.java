package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.labo.kaji.fragmentanimations.PushPullAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.R;

public class SignupFragment extends Fragment implements View.OnClickListener {

    private AuthenticationActivity hostingActivity;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private TextView loginTextview;

    private String mobileNumber;
    private String email;
    private String password;



    public static SignupFragment newInstance() {

        Bundle args = new Bundle();

        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        hostingActivity=(AuthenticationActivity)getActivity();

        emailEditText = view.findViewById(R.id.emailEdittext);
        phoneEditText = view.findViewById(R.id.phonenumberEdittext);
        passwordEditText = view.findViewById(R.id.passwordEdittext);
        signupButton = view.findViewById(R.id.signupButton);
        loginTextview = view.findViewById(R.id.loginTextview);

        signupButton.setOnClickListener(this);
        loginTextview.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signupButton:
                 registerUser();
                 break;
            case R.id.loginTextview:
                 hostingActivity.addDifferentFragment(SigninFragment.newInstance());
                 break;
        }

    }

    public void registerUser()
    {
        mobileNumber = phoneEditText.getText().toString().trim();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if( !isValidEmail(email))
        {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }
        if(mobileNumber.isEmpty() || mobileNumber.length() < 10 ){
            phoneEditText.setError("Enter a valid mobile number");
            phoneEditText.requestFocus();
            return;
        }

        if(passwordEditText.getText().toString().trim().length()<6)
        {
            passwordEditText.setError("Password should have atleast 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        verifyPhoneNumber();

    }

    public void verifyPhoneNumber()
    {

        hostingActivity.addDifferentFragment(VerificationFragment.newInstance(email,mobileNumber,password));

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.LEFT,enter,1000);
    }

}
