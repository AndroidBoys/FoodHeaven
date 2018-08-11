package drunkcoder.com.foodheaven.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;

public class FoodMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView foodImageView;
    public TextView foodNameTextView;
    public TextView foodDescriptionTextView;
    public TextView foodQuantityTextView;

    public FoodMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        foodImageView=itemView.findViewById(R.id.foodImageView);
        foodNameTextView=itemView.findViewById(R.id.foodNameTextView);
        foodDescriptionTextView=itemView.findViewById(R.id.foodDescriptionTextView);
        foodQuantityTextView=itemView.findViewById(R.id.foodQuantityTextView);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
