package drunkcoder.com.foodheaven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import drunkcoder.com.foodheaven.Models.OurPlans;
import drunkcoder.com.foodheaven.R;

public class OurPlansCustomArrayAdapter extends ArrayAdapter {
    private ArrayList<OurPlans> ourPlans=new ArrayList<>();
    private Context context;
    private ImageView planImageView;
    private TextView planNameTextView;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.our_plans_fragment_listview_row,parent,false);
        planImageView=view.findViewById(R.id.packsImageView);
        planNameTextView=view.findViewById(R.id.packName);

        Picasso.with(context).load(ourPlans.get(position).getPackImageUrl()).into(planImageView);
        planNameTextView.setText(ourPlans.get(position).getPackName());

        return view;
    }

    @Override
    public int getCount() {
        return ourPlans.size();
    }

    public OurPlansCustomArrayAdapter(Context context, ArrayList<OurPlans> ourPlans) {
        super(context,R.layout.our_plans_fragment_listview_row,ourPlans);
        this.ourPlans=ourPlans;
        this.context=context;
    }
}
