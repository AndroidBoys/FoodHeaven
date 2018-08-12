package drunkcoder.com.foodheaven.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;

public class WantsToEatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView wantsFoodImageView;
    public TextView wantsFoodNameTextView;
    public TextView wantsFoodDescriptionTextView;
    public CheckBox wantsFoodCheckBox;

    public WantsToEatViewHolder(@NonNull View itemView) {
        super(itemView);
        wantsFoodImageView=itemView.findViewById(R.id.specialFoodImageView);
        wantsFoodNameTextView=itemView.findViewById(R.id.specialFoodNameTextView);
        wantsFoodDescriptionTextView=itemView.findViewById(R.id.specialFoodDescriptionTextView);
        wantsFoodCheckBox=itemView.findViewById(R.id.specialFoodCheckBox);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
