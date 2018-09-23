package drunkcoder.com.foodheaven.ViewHolders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;


public class WhyHeavensFoodViewHolder extends RecyclerView.ViewHolder {


    public ImageView aboutImageView;
    public TextView aboutTextView;

    public WhyHeavensFoodViewHolder(@NonNull View itemView) {
        super(itemView);
        aboutImageView=itemView.findViewById(R.id.aboutImage1);
        aboutTextView=itemView.findViewById(R.id.aboutTextView1);
//        itemView.setOnCreateContextMenuListener(this);

    }

}
