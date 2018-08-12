package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.labo.kaji.fragmentanimations.PushPullAnimation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.AuthUtil;

public class SigninFragment extends Fragment implements View.OnClickListener {

    private AuthenticationActivity hostingActivity;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signinButton;
    private TextView signupTextview;
    private TextView forgotTextview;
    private FirebaseAuth mAuth;
    private KProgressHUD kProgressHUD;

    private List<User> users;
    private boolean isPhoneNumber;
    private boolean isEmail;
    private String username="Sample@test.com";

    public static SigninFragment newInstance() {

        Bundle args = new Bundle();

        SigninFragment fragment = new SigninFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signin,container,false);
        hostingActivity = (AuthenticationActivity) getActivity();

        usernameEditText = view.findViewById(R.id.usernameEdittext);
        passwordEditText = view.findViewById(R.id.passwordEdittext);
        signinButton = view.findViewById(R.id.signinButton);
        signupTextview = view.findViewById(R.id.signupTextview);
        forgotTextview = view.findViewById(R.id.forgotTextview);

        signupTextview.setOnClickListener(this);
        signinButton.setOnClickListener(this);
        forgotTextview.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();



        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.signupTextview:
                Log.i("click","Sign up clicked");
                hostingActivity.addDifferentFragment(SignupFragment.newInstance());
                break;
            case R.id.signinButton:
                vailidateDetails();
                break;
            case R.id.forgotTextview:
                //recoverPassword();
                break;

        }
    }

    private void recoverPassword()
    {
        Toast.makeText(hostingActivity, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(hostingActivity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(hostingActivity, "A Password reset email has been sent to your email "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(hostingActivity, "Something went wrong.."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void signIn()
    {
        if(isPhoneNumber){
            username = getEmailForPhoneNumber();
        }
        mAuth.signInWithEmailAndPassword(username,passwordEditText.getText().toString().trim())
                .addOnCompleteListener(hostingActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(hostingActivity, "Signed in sucessfully!", Toast.LENGTH_SHORT).show();
                            hostingActivity.moveToHomeActivity();
                            kProgressHUD.dismiss();
                        }
                        else
                        {
                            Toast.makeText(hostingActivity, "Sign in failed!:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            kProgressHUD.dismiss();
                        }
                    }
                });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return PushPullAnimation.create(PushPullAnimation.DOWN,enter,1000);
    }

    public void fetchUsers()
    {
        users = new ArrayList<>();
        kProgressHUD= KProgressHUD.create(hostingActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


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

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All child have been added
                signIn();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String getEmailForPhoneNumber()
    {
        for(int i=0;i<users.size();i++)
        {
            User user = users.get(i);
            if(user.getPhoneNumber().equals(usernameEditText.getText().toString())){
                return user.getEmail();
            }
        }

        return "Sample@test.com";
    }

    public void vailidateDetails(){

        isPhoneNumber= AuthUtil.isVailidPhone(usernameEditText.getText());
        isEmail=AuthUtil.isValidEmail(usernameEditText.getText());

      if(isEmail||isPhoneNumber){
            username = usernameEditText.getText().toString();
        }else{
            usernameEditText.setError("Enter a valid Email/Password");
            usernameEditText.requestFocus();
            return;
        }

        if(passwordEditText.getText().toString().trim().length()<6)
        {
            passwordEditText.setError("Password should have atleast 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        fetchUsers();

    }
}
