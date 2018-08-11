package drunkcoder.com.foodheaven.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;


public class SpecialFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView specialFoodImageView;
    public TextView specialFoodNameTextView;
    public TextView specialFoodDescriptionTextView;
    public ElegantNumberButton elegantNumberButton;
    public CheckBox specialFoodCheckBox;

    public SpecialFoodViewHolder(@NonNull View itemView) {
        super(itemView);
        specialFoodImageView=itemView.findViewById(R.id.specialFoodImageView);
        specialFoodNameTextView=itemView.findViewById(R.id.specialFoodNameTextView);
        specialFoodDescriptionTextView=itemView.findViewById(R.id.specialFoodDescriptionTextView);
        elegantNumberButton=itemView.findViewById(R.id.elegantNumberButton);
        specialFoodCheckBox=itemView.findViewById(R.id.specialFoodCheckBox);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
