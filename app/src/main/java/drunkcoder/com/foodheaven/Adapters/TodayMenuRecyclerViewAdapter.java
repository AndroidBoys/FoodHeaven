package drunkcoder.com.foodheaven.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.Food;
import drunkcoder.com.foodheaven.R;

public class TodayMenuRecyclerViewAdapter extends RecyclerView.Adapter<TodayMenuRecyclerViewAdapter.FoodMenuViewHolder>{

    public ArrayList<Food> foodArrayList;
    public Context context;

    public TodayMenuRecyclerViewAdapter(ArrayList<Food> foodArrayList, Context context) {
        this.foodArrayList=foodArrayList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    @NonNull
    @Override
    public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_menu_row_layout,parent,false);
        return new FoodMenuViewHolder(v);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodMenuViewHolder holder, int position) {
        //for movement of description text
        holder.foodDescriptionTextView.setSelected(true);
        holder.foodDescriptionTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.foodDescriptionTextView.setText(foodArrayList.get(position).getFoodDescription());
        Picasso.with(context).load(foodArrayList.get(position).getImageUrl()).into(holder.foodImageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.imageProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        holder.foodNameTextView.setText(foodArrayList.get(position).getFoodName());
        Log.d("imageUrl",foodArrayList.get(position).getImageUrl());

    }


    public class FoodMenuViewHolder extends RecyclerView.ViewHolder {


        public ImageView foodImageView;
        public TextView foodNameTextView;
        public TextView foodDescriptionTextView;
        public ProgressBar imageProgressBar;

        public FoodMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodDescriptionTextView = itemView.findViewById(R.id.foodDescriptionTextView);
            imageProgressBar=itemView.findViewById(R.id.imageProgressBar);

        }


    }
}