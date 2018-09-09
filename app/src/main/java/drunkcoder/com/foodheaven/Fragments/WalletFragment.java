package drunkcoder.com.foodheaven.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Models.Wallet;
import drunkcoder.com.foodheaven.Payments.PaymentsActivity;
import drunkcoder.com.foodheaven.R;
import info.hoang8f.widget.FButton;

public class WalletFragment extends Fragment {

    private FButton addMoneyButton;
    private TextView creditedAmountTextView;
    private TextView remainingDaysTextView;
    private TextView choosenPlanTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet,container,false);
        addMoneyButton = view.findViewById(R.id.addToWalletButton);
//        addMoneyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), PaymentsActivity.class));
//            }
//        });
        //addMoneyButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        creditedAmountTextView=view.findViewById(R.id.creditedAmountTextView);
        remainingDaysTextView=view.findViewById(R.id.remainingDaysTextView);
        choosenPlanTextView=view.findViewById(R.id.choosenPlanTextView);
        fetchUserWalletDetails();

        return view;
    }

    private void fetchUserWalletDetails(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.child("wallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                remainingDaysTextView.setText("Remaining Days : "+wallet.getRemainingDays());
                creditedAmountTextView.setText("Debited Amount : "+wallet.getCreditedAmount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("subscribedPlan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plan plan=dataSnapshot.getValue(Plan.class);
                if(plan!=null) {
                    choosenPlanTextView.setText("Choosen Plan : "+plan.getPlanName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public static WalletFragment newInstance() {

        Bundle args = new Bundle();

        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
