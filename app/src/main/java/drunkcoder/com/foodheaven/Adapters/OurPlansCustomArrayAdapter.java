package drunkcoder.com.foodheaven.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Fragments.BuySubscriptionFragment;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.R;
import info.hoang8f.widget.FButton;

public class OurPlansCustomArrayAdapter extends ArrayAdapter {
    private ArrayList<Plan> ourPlans=new ArrayList<>();
    private DescriptionActivity hostingActivity;
    private ImageView planImageView;
    private TextView planNameTextView;
    private TextView showDetailButton;
    private ProgressBar imageProgressBar;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) hostingActivity.getSystemService(hostingActivity.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.our_plans_fragment_listview_row,parent,false);
        planImageView=view.findViewById(R.id.packsImageView);
        planNameTextView=view.findViewById(R.id.packName);
        imageProgressBar=view.findViewById(R.id.imageProgressBar);
        showDetailButton = (FButton)view.findViewById(R.id.showDetailsButton);

        ((FButton) showDetailButton).setButtonColor(hostingActivity.getResources().getColor(R.color.colorPrimary));
        showDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hostingActivity.addDifferentFragment(BuySubscriptionFragment.newInstance(ourPlans.get(position)),"ourPlanButton");
//                Log.d("position","hahahahh*******"+position);
            }
        });

        Picasso.with(hostingActivity).load(ourPlans.get(position).getPlanImageUrl()).into(planImageView, new Callback() {
            @Override
            public void onSuccess() {
                if(imageProgressBar!=null){
                    imageProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });
        planNameTextView.setText(ourPlans.get(position).getPlanName());

        return view;
    }

    @Override
    public int getCount()
    {
        return ourPlans.size();
    }

    public OurPlansCustomArrayAdapter(DescriptionActivity hostingActivity, ArrayList<Plan> ourPlans) {
        super(hostingActivity,R.layout.our_plans_fragment_listview_row,ourPlans);
        this.ourPlans=ourPlans;
        this.hostingActivity=hostingActivity;
    }
}