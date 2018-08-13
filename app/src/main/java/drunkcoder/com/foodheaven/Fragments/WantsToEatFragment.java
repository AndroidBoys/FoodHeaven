package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.SpecialFood;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;
import drunkcoder.com.foodheaven.ViewHolders.SpecialFoodViewHolder;
import drunkcoder.com.foodheaven.ViewHolders.WantsToEatViewHolder;
import info.hoang8f.widget.FButton;


public class WantsToEatFragment extends Fragment {

    private RecyclerView wantsToEatRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private FirebaseRecyclerAdapter<FoodMenu, WantsToEatViewHolder> wantsToEatFoodAdapter;
    private DatabaseReference wantsToEatDatabaseReference;
    private FButton wantsSubmitButton;
    private int maxLimit;
    private ArrayList<String> foodChooseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wants_to_eat_layout,container,false);
        context=getContext();
        wantsToEatRecyclerView=view.findViewById(R.id.wantsToEatRecyclerView);
        wantsToEatDatabaseReference=FirebaseDatabase.getInstance().getReference("FavouriteFood");
        foodChooseList=new ArrayList<>();
        wantsSubmitButton=view.findViewById(R.id.wantsSubmitButton);
        layoutManager=new LinearLayoutManager(context);
        wantsToEatRecyclerView.setHasFixedSize(true);
        wantsToEatRecyclerView.setLayoutManager(layoutManager);
        loadWantToEatImages();
        wantsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(foodChooseList.size()!=0){
                    Toast.makeText(context,"Submitted Succesfully",Toast.LENGTH_SHORT).show();
                    //make the entry in firebase

                    for(int i=0;i<foodChooseList.size();i++){

                        //wantsToEatDatabaseReference.child("ChoosedBy").child(foodChooseList.get(i)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue()
                    }
                }

            }
        });

        PullRefreshLayout wantsRefreshLayout=view.findViewById(R.id.wantsRefreshLayout);
        wantsRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWantToEatImages();
            }
        });
        wantsRefreshLayout.setColor(R.color.colorPrimary);//set the color of refresh circle.

        wantsSubmitButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));

        return view;
    }

    private void loadWantToEatImages() {

        DatabaseReference databaseReference=wantsToEatDatabaseReference.child("FoodImages");
        wantsToEatDatabaseReference.child("maxLimit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    maxLimit=dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        wantsToEatFoodAdapter=new FirebaseRecyclerAdapter<FoodMenu, WantsToEatViewHolder>(FoodMenu.class,
                R.layout.wants_to_eat_raw_layout,WantsToEatViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(WantsToEatViewHolder wantsToEatViewHolder, final FoodMenu foodMenu, int i) {
                wantsToEatViewHolder.wantsFoodNameTextView.setText(foodMenu.getFoodName());
                wantsToEatViewHolder.wantsFoodDescriptionTextView.setText(foodMenu.getFoodDescription());
                Picasso.with(context).load(foodMenu.getImageUrl()).into(wantsToEatViewHolder.wantsFoodImageView);
                
                wantsToEatViewHolder.wantsFoodCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox=(CheckBox)view;
                        if(checkBox.isChecked()){
                            if(foodChooseList.size()<maxLimit){
                                foodChooseList.add(foodMenu.getFoodName());
                            }else{
                                checkBox.setChecked(false);
                                Toast.makeText(context,"You can only select "+maxLimit+" items",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Log.i("removed","------"+foodMenu.getFoodName());
                            for(int j=0;j<foodChooseList.size();j++){
                                if(foodChooseList.get(j).equals(foodMenu.getFoodName())){
                                    Log.i("inside if",foodChooseList.get(j));
                                    foodChooseList.remove(j);
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        };
        wantsToEatRecyclerView.setAdapter(wantsToEatFoodAdapter);
        //
    }

    public static WantsToEatFragment newInstance() {
        
        Bundle args = new Bundle();

        WantsToEatFragment fragment = new WantsToEatFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
