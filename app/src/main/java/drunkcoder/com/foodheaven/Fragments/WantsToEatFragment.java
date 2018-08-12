package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.SpecialFood;
import drunkcoder.com.foodheaven.Models.TodayMenu;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;
import drunkcoder.com.foodheaven.ViewHolders.SpecialFoodViewHolder;
import drunkcoder.com.foodheaven.ViewHolders.WantsToEatViewHolder;

public class WantsToEatFragment extends Fragment {

    private RecyclerView wantsToEatRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private FirebaseRecyclerAdapter<TodayMenu, WantsToEatViewHolder> wantsToEatFoodAdapter;
    private DatabaseReference wantsToEatDatabaseReference;
    private Button wantsSubmitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wants_to_eat_layout,container,false);
        context=getContext();
        wantsToEatRecyclerView=view.findViewById(R.id.wantsToEatRecyclerView);
        wantsSubmitButton=view.findViewById(R.id.wantsSubmitButton);
        layoutManager=new LinearLayoutManager(context);
        wantsToEatRecyclerView.setHasFixedSize(true);
        wantsToEatRecyclerView.setLayoutManager(layoutManager);
        loadWantToEatImages();
        wantsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        return view;
    }

    private void loadWantToEatImages() {
        wantsToEatDatabaseReference= FirebaseDatabase.getInstance().getReference("FavouriteFood").child("FoodImages");
        
        wantsToEatFoodAdapter=new FirebaseRecyclerAdapter<TodayMenu, WantsToEatViewHolder>(TodayMenu.class,
                R.layout.wants_to_eat_raw_layout,WantsToEatViewHolder.class,wantsToEatDatabaseReference) {
            @Override
            protected void populateViewHolder(WantsToEatViewHolder wantsToEatViewHolder, TodayMenu todayMenu, int i) {
                wantsToEatViewHolder.wantsFoodNameTextView.setText(todayMenu.getFoodName());
                wantsToEatViewHolder.wantsFoodDescriptionTextView.setText(todayMenu.getFoodDescription());
                Picasso.with(context).load(todayMenu.getImageUrl()).into(wantsToEatViewHolder.wantsFoodImageView);
                
                wantsToEatViewHolder.wantsFoodCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox=(CheckBox)view;
                        if(checkBox.isChecked()){
                            
                        }else{
                            
                        }
                    }
                });
            }
        };
    }

    public static WantsToEatFragment newInstance() {
        
        Bundle args = new Bundle();

        WantsToEatFragment fragment = new WantsToEatFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
