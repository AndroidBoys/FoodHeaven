package drunkcoder.com.foodheaven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import drunkcoder.com.foodheaven.Models.Assistance;
import drunkcoder.com.foodheaven.Models.Food;
import drunkcoder.com.foodheaven.R;

public class CurrentOrderArrayAdapter extends ArrayAdapter {
    private ArrayList<Food> foodArrayList=new ArrayList<>();
    private Context context;
    private TextView foodNameTextView,foodDescriptionTextView;
    private ImageView foodImage;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.currnt_order_row,parent,false);
        foodNameTextView=view.findViewById(R.id.foodNameTextView);
        foodImage=view.findViewById(R.id.foodImageView);
        foodDescriptionTextView=view.findViewById(R.id.foodDescriptionTextView);
        foodNameTextView.setText(foodArrayList.get(position).getFoodName());
        Picasso.with(context).load(foodArrayList.get(position).getImageUrl()).into(foodImage);
        foodDescriptionTextView.setText(foodArrayList.get(position).getFoodDescription());
        return view;
    }

    @Override
    public int getCount() {
        return foodArrayList.size();
    }

    public CurrentOrderArrayAdapter(Context context, ArrayList<Food> foodArrayList) {
        super(context,R.layout.current_order,foodArrayList);
        this.foodArrayList=foodArrayList;
        this.context=context;
    }
}
