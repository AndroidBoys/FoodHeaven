package drunkcoder.com.foodheaven.Fragments;

// <<<<<<< arvind100
import android.app.Activity;
// ===
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
// >>>>>>> master
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Activities.AuthenticationActivity;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Models.Address;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.AuthUtil;

public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView userImage, editProfileImageView, doneEditingImageView;
    private TextView userNameTextViewHeader,
            userEmailTextViewHeader,
            planName, lunch, dinner, breakfast, days, price;
    private EditText name, email, phone, address;
    private Button walletButton;
    private Activity activity;
    private Context context;
    private String oldPhoneNo;


    //for phone no. verification

    AlertDialog builder;
    EditText otpEditText;
    TextView verificationTextView;
    Button submitButton;

    //verification id that will be sent to the user
    String mVerificationId;
    FirebaseAuth mAuth;
    PhoneAuthCredential credential;

    ProgressBar progressBar;
    KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        activity=getActivity();
        context = getContext();
        walletButton = view.findViewById(R.id.walletButton);
        userImage = view.findViewById(R.id.profile_image);
        userNameTextViewHeader = view.findViewById(R.id.userNameTextView);
        userEmailTextViewHeader = view.findViewById(R.id.userEmailTextView);
        planName = view.findViewById(R.id.planNameTextView);
        lunch = view.findViewById(R.id.lunchTextView);
        dinner = view.findViewById(R.id.dinnerTextView);
        breakfast = view.findViewById(R.id.breakfastTextView);
        days = view.findViewById(R.id.daysTextView);
        price = view.findViewById(R.id.priceTextView);
        name = view.findViewById(R.id.userNameEditText);
        email = view.findViewById(R.id.emailEditText);
        phone = view.findViewById(R.id.phoneEditText);
        address = view.findViewById(R.id.addressEditText);
        editProfileImageView = view.findViewById(R.id.editProfileImageView);
        doneEditingImageView = view.findViewById(R.id.doneImageView);

        if(MyApplication.getCurrentUser().getSubscribedPlan()==null){
            walletButton.setVisibility(View.GONE);
        }

// <<<<<<< 24-sept

        mAuth = FirebaseAuth.getInstance();
// =======
// // >>>>>>> master
// >>>>>>> master
        editProfileImageView.setOnClickListener(this);
        doneEditingImageView.setOnClickListener(this);
        walletButton.setOnClickListener(this);
        setUsersProfile();


        return view;
    }


    private String checkBool(boolean bool) {
        if (bool == true)
            return "Included";
        else
            return "Not Included";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editProfileImageView:
                setNeddedContentsVisibility(1);
                break;
            case R.id.doneImageView:
                doneEditingImageView.setVisibility(View.GONE);
                setNeddedContentsVisibility(2);
                updateProfile();
                //UPDATE DATA INTO FIREBASE
                break;
            case R.id.walletButton:
                goTOWallet();
                break;

        }
    }

    private void setNeddedContentsVisibility(int pressed) {
        switch (pressed) {
            case 1:

                name.setFocusable(true);
                phone.setFocusable(true);
                email.setFocusable(true);
//                address.setFocusable(true);

                name.setFocusableInTouchMode(true);
                phone.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
//                address.setFocusableInTouchMode(true);
                doneEditingImageView.setVisibility(View.VISIBLE);
                break;

            case 2:
                name.setFocusableInTouchMode(false);
                phone.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
//                address.setFocusableInTouchMode(false);
                doneEditingImageView.setVisibility(View.GONE);

                name.setFocusable(false);
                phone.setFocusable(false);
                email.setFocusable(false);
//                address.setFocusable(false);

                break;
        }

    }

    private void setUsersProfile() {
        User user = MyApplication.getCurrentUser();
        Plan plan = user.getSubscribedPlan();

        userEmailTextViewHeader.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
        oldPhoneNo=user.getPhoneNumber();
        email.setText(user.getEmail());
        userNameTextViewHeader.setText(user.getName());
        name.setText(user.getName());
        address.setText(user.getUserAddress().address);

        ColorGenerator generator = ColorGenerator.MATERIAL;

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound("" + "" + user.getName().charAt(0), generator.getRandomColor());//setting first letter of the user name
        userImage.setImageDrawable(textDrawable);


        if (plan != null) {
            planName.setText(plan.getPlanName());
            lunch.setText(checkBool(plan.isIncludesLunch()));
            dinner.setText(checkBool(plan.includesDinner));
            breakfast.setText(checkBool(plan.includesBreakFast));
            days.setText(plan.noOfDays);
            int totalPrice = Integer.parseInt(plan.getFrequencyPerDay()) * Integer.parseInt(plan.getSingleTimePrice());
            price.setText("" + totalPrice);
        }

    }

    public static UserProfileFragment newInstance() {

        Bundle args = new Bundle();

        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

// <<<<<<< arvind100
    @Override
    public void onResume() {
        super.onResume();
        ((DescriptionActivity)activity).setActionBarTitle("My Profile");
// =======
    }
    private void updateProfile() {


        if (!(AuthUtil.isValidEmail(email.getText()))) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        if (!(AuthUtil.isVailidPhone(phone.getText()))) {
            phone.setError("Enter a valid mobile number");
            phone.requestFocus();
            return;
        }

        if(!oldPhoneNo.equals(phone.getText().toString()))
            verifyPhoneNumber();
        else
            addUsertoDB();

    }


    private void goTOWallet() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.descriptionFrameLayout, WalletFragment.newInstance(), null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void verifyPhoneNumber() {


        sendVerificationCode();

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_phone_no_update_verification, null);

        otpEditText = view.findViewById(R.id.otpEdittext);
        submitButton = view.findViewById(R.id.signupButton);
        progressBar = view.findViewById(R.id.progressbar);
        verificationTextView = view.findViewById(R.id.textView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitOtp();
                builder.dismiss();

            }
        });



        builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
                .setView(view)
                .setIcon(R.drawable.thali_graphic)
                .show();

    }

    private void submitOtp() {

        if (credential != null) {
            kProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Getting you in")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            registerUser();
        }
    }


    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone.getText(),
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

                //verifying the code
                verifyVerificationCode(code);
                progressBar.setVisibility(View.GONE);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            // mResendToken = forceResendingToken;
        }
    };


    private void verifyVerificationCode(String otp) {

        //creating the credential
        credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        verificationTextView.setText("Phone number verified!");
        Toast.makeText(context, "Phone number verified!", Toast.LENGTH_SHORT).show();
    }


    private void registerUser() {

        mAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // add phone number to the email/ password login
                        Log.d("tag","phone no. linked");
                        addUsertoDB();
                    }
                    else{

                        Toast.makeText(context, "Try again and please enter a valid code", Toast.LENGTH_SHORT).show();
                        Log.d("tag","phone no. doesn't linked");
                        kProgressHUD.dismiss();
                        Log.d("tagexp",task.getException().toString());

                }
            }
        });
    }

    private void addUsertoDB() {
        User user = new User();
        user.setEmail(email.getText().toString());
        user.setName(name.getText().toString());
        user.setPhoneNumber(phone.getText().toString());
        user.setPassword(MyApplication.getCurrentUser().getPassword());
        user.setUserAddress(MyApplication.getCurrentUser().getUserAddress());
        user.setSubscribedPlan(MyApplication.getCurrentUser().getSubscribedPlan());
        user.setWallet(MyApplication.getCurrentUser().getWallet());

        // Entry into Users table
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);
       if(kProgressHUD!=null)
        kProgressHUD.dismiss();

//                Toast.makeText(hostingActivity, "Signed up successfully:Please log in", Toast.LENGTH_SHORT).show();

// >>>>>>> master
    }
}

