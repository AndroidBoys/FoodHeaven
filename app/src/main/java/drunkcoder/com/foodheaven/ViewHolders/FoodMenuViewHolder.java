package drunkcoder.com.foodheaven.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;

public class FoodMenuViewHolder extends RecyclerView.ViewHolder  {


    public ImageView foodImageView;
    public TextView foodNameTextView;
    public TextView foodDescriptionTextView;
   public FoodMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        foodImageView=itemView.findViewById(R.id.foodImageView);
        foodNameTextView=itemView.findViewById(R.id.foodNameTextView);
        foodDescriptionTextView=itemView.findViewById(R.id.foodDescriptionTextView);

    }


}
