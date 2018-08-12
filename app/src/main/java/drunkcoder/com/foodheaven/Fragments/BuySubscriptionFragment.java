package drunkcoder.com.foodheaven.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Models.OurPlans;
import drunkcoder.com.foodheaven.R;

public class BuySubscriptionFragment extends Fragment {

    private OurPlans plan;
    private int totalPrice=0;
    private ImageView planImageView;
    private TextView planNameTextView;
    private TextView priceTextView;
    private TextView planDescriptionTextView;
    private ElegantNumberButton timeCounterButton;
    private Button subscribeButton;



    public static BuySubscriptionFragment newInstance(OurPlans plan) {

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
        plan = (OurPlans) getArguments().getSerializable("plan");

        planImageView = view.findViewById(R.id.planImageview);
        priceTextView = view.findViewById(R.id.plan_price);
        planNameTextView = view.findViewById(R.id.plan_name);
        planDescriptionTextView = view.findViewById(R.id.plan_description);
        timeCounterButton=view.findViewById(R.id.elegantNumberButton);
        subscribeButton=view.findViewById(R.id.subscribe);

        totalPrice=Integer.valueOf(priceTextView.getText().toString());
        Picasso.with(getActivity()).load(plan.getPackImageUrl()).into(planImageView);

        planNameTextView.setText(plan.getPackName());
        planDescriptionTextView.setText(plan.getDescription());

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SUBSCRIBE THIS PLAN
            }
        });

        timeCounterButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton elegantNumberButton, int oldValue, int newValue) {
                priceTextView.setText(""+totalPrice*newValue);

            }
        });
        return  view;
    }
}
