package drunkcoder.com.foodheaven.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Payments.PaymentsActivity;
import drunkcoder.com.foodheaven.R;
import info.hoang8f.widget.FButton;

public class BuySubscriptionFragment extends Fragment {

    private Plan plan;
    private int totalPrice=0;
    private ImageView planImageView;
    private TextView planNameTextView;
    private TextView priceTextView;
    private TextView planDescriptionTextView;
    private FButton subscribeButton;
    private CheckBox dinnerCheckBox;
    private CheckBox lunchCheckBox;
    private CheckBox breakfastCheckBox;
    private boolean dinnerChecked=true;
    private boolean lunchChecked=true;
    private boolean breakfastChecked=true;
    private int noOfChecks=3;



    public static BuySubscriptionFragment newInstance(Plan plan) {

        Bundle args = new Bundle();
        args.putSerializable("plan",plan);
        BuySubscriptionFragment fragment = new BuySubscriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buy_subscription,container,false);
        plan = (Plan) getArguments().getSerializable("plan");

        planImageView = view.findViewById(R.id.planImageview);
        priceTextView = view.findViewById(R.id.plan_price);
        planNameTextView = view.findViewById(R.id.plan_name);
        planDescriptionTextView = view.findViewById(R.id.plan_description);
        breakfastCheckBox = view.findViewById(R.id.BreakFastCheckbox);
        lunchCheckBox = view.findViewById(R.id.lunchChekbox);
        dinnerCheckBox = view.findViewById(R.id.dinnerCheckbox);


        subscribeButton=view.findViewById(R.id.subscribe);


        priceTextView.setText(String.valueOf(calculatePrice()));

        totalPrice=Integer.valueOf(priceTextView.getText().toString());
        Picasso.with(getActivity()).load(plan.getPlanImageUrl()).into(planImageView);

        planNameTextView.setText(plan.getPlanName());
        planDescriptionTextView.setText(plan.getDescription());


        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SUBSCRIBE THIS PLAN
                if(noOfChecks!=0) {
                    moveToPaymentsActivity();
                }else{
                    Toast.makeText(getContext(), "Please choose the one time plan atleast", Toast.LENGTH_SHORT).show();
                }

            }
        });
        subscribeButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));

        breakfastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(breakfastChecked){
                    breakfastChecked=false;
                    noOfChecks--;
                }
                else {
                    breakfastChecked=true;
                    noOfChecks++;
                }

                priceTextView.setText(""+calculatePrice());

            }
        });
        lunchCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(lunchChecked){
                    lunchChecked = false;
                    noOfChecks--;
                }
                else {
                    lunchChecked = true;
                    noOfChecks++;
                }

                priceTextView.setText(""+calculatePrice());
            }
        });
        dinnerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(dinnerChecked){
                    dinnerChecked=false;
                    noOfChecks--;
                }
                else {
                    dinnerChecked=true;
                    noOfChecks++;
                }

                priceTextView.setText(""+calculatePrice());
            }
        });

        return  view;
    }

    public void moveToPaymentsActivity(){
        Intent intent = new Intent(getActivity(), PaymentsActivity.class);
        if(breakfastChecked){
            plan.includesBreakFast=true;
        }
        if(lunchChecked){
            plan.includesLunch=true;
        }
        if(dinnerChecked){
            plan.includesDinner=true;
        }
        intent.putExtra("choosenPlan",plan);
        startActivity(intent);
    }

    private int calculatePrice(){


        if(noOfChecks==1){
            return getInt(plan.oneTimePrice);
        }else if(noOfChecks==2){
            return getInt(plan.twoTimePrice);
        }else if(noOfChecks==3) {
            return getInt(plan.threeTimePrice);
        }
        return 0;
    }

    private int getInt(String s){
        return  Integer.parseInt(s);
    }




}
