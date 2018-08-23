package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;

public class UserProfileFragment extends Fragment implements View.OnClickListener{
    private ImageView userImage,editProfileImageView,doneEditingImageView;
    private TextView userNameTextViewHeader,
            userEmailTextViewHeader,
    planName,lunch,dinner,breakfast,days,price;
    private EditText name,email,phone,address;
    private Button walletButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_profile,container,false);

        walletButton=view.findViewById(R.id.walletButton);
       userImage=view.findViewById(R.id.profile_image);
        userNameTextViewHeader=view.findViewById(R.id.userNameTextView);
        userEmailTextViewHeader=view.findViewById(R.id.userEmailTextView);
        planName=view.findViewById(R.id.planNameTextView);
        lunch=view.findViewById(R.id.lunchTextView);
        dinner =view.findViewById(R.id.dinnerTextView);
        breakfast=view.findViewById(R.id.breakfastTextView);
        days=view.findViewById(R.id.daysTextView);
        price=view.findViewById(R.id.priceTextView);
        name=view.findViewById(R.id.userNameEditText);
        email=view.findViewById(R.id.emailEditText);
        phone=view.findViewById(R.id.phoneEditText);
        address=view.findViewById(R.id.addressEditText);
        editProfileImageView=view.findViewById(R.id.editProfileImageView);
        doneEditingImageView=view.findViewById(R.id.doneImageView);
        
        editProfileImageView.setOnClickListener(this);
        doneEditingImageView.setOnClickListener(this);
        walletButton.setOnClickListener(this);
        setUsersProfile();


        return view;
    }


    private String checkBool(boolean bool) {
        if(bool==true)
            return "Included";
        else
            return "Not Included";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editProfileImageView:
                setNeddedContentsVisibility(1);
                break;
            case R.id.doneImageView:
                doneEditingImageView.setVisibility(View.GONE);
                setNeddedContentsVisibility(2);
                //UPDATE DATA INTO FIREBASE
                break;
            case R.id.walletButton:
                goTOWallet();
                break;

        }
    }

    private void goTOWallet() {


        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.descriptionFrameLayout,WalletFragment.newInstance(),null).commit();

    }

    private void setNeddedContentsVisibility(int pressed) {
        switch (pressed) {
            case 1:

                name.setFocusable(true);
                phone.setFocusable(true);
                email.setFocusable(true);
                address.setFocusable(true);

                name.setFocusableInTouchMode(true);
                phone.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);
                address.setFocusableInTouchMode(true);
                doneEditingImageView.setVisibility(View.VISIBLE);
                break;

            case 2:
                name.setFocusableInTouchMode(false);
                phone.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                address.setFocusableInTouchMode(false);
                doneEditingImageView.setVisibility(View.GONE);

                name.setFocusable(false);
                phone.setFocusable(false);
                email.setFocusable(false);
                address.setFocusable(false);

                break;
        }

    }

    private void setUsersProfile() {
        User user =MyApplication.getCurrentUser();
        Plan plan=user.getSubscribedPlan();

        userEmailTextViewHeader.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
        email.setText(user.getEmail());
        userNameTextViewHeader.setText(user.getName());
        name.setText(user.getName());
        address.setText(user.getUserAddress().address);

        ColorGenerator generator=ColorGenerator.MATERIAL;

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(""+"" + user.getName().charAt(0), generator.getRandomColor());//setting first letter of the user name
        userImage.setImageDrawable(textDrawable);


        if(plan!=null) {
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
}
