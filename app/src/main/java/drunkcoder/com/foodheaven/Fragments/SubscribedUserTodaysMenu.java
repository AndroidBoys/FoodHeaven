package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.TodayMenu;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;

public class SubscribedUserTodaysMenu extends Fragment {

    private RecyclerView breakFastRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView dinnerRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private FirebaseRecyclerAdapter<TodayMenu,FoodMenuViewHolder> dinnerAdapter;
    private FirebaseRecyclerAdapter<TodayMenu,FoodMenuViewHolder> breakFastAdapter;
    private FirebaseRecyclerAdapter<TodayMenu,FoodMenuViewHolder> lunchAdapter;
    private FirebaseDatabase todayMenuFirebaseDatabase;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.subscribed_user_todays_menu_layout,container,false);
        breakFastRecyclerView=view.findViewById(R.id.breakFastRecyclerView);
        lunchRecyclerView=view.findViewById(R.id.lunchRecyclerView);
        dinnerRecyclerView=view.findViewById(R.id.dinnerRecyclerView);
        context=getContext();

        todayMenuFirebaseDatabase=FirebaseDatabase.getInstance();
       // linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        setRecyclerView(breakFastRecyclerView);
        setRecyclerView(lunchRecyclerView);
        setRecyclerView(dinnerRecyclerView);
        showBreakFastImages();
        showlunchImages();
        showDinnerImages();

        return view;
    }

    private void setRecyclerView(RecyclerView recyclerView) {

        linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void showDinnerImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("todayMenu").child("dinner");
        dinnerAdapter=new FirebaseRecyclerAdapter<TodayMenu, FoodMenuViewHolder>(
                TodayMenu.class,R.layout.food_menu_raw_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, TodayMenu todayMenu, int i) {
                setFoodDetails(foodMenuViewHolder,todayMenu);

            }
        };
        dinnerAdapter.notifyDataSetChanged();
        dinnerRecyclerView.setAdapter(dinnerAdapter);
        //Download the images from the firebase
    }

    private void setFoodDetails(FoodMenuViewHolder foodMenuViewHolder,TodayMenu todayMenu) {
        foodMenuViewHolder.foodNameTextView.setText(todayMenu.getFoodName());
        foodMenuViewHolder.foodDescriptionTextView.setText(todayMenu.getFoodDescription());
        foodMenuViewHolder.foodQuantityTextView.setText("Quantity: "+todayMenu.getFoodQuantity());
        Picasso.get().load(todayMenu.getImageUrl()).into(foodMenuViewHolder.foodImageView);

    }

    private void showlunchImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("todayMenu").child("lunch");
        lunchAdapter=new FirebaseRecyclerAdapter<TodayMenu, FoodMenuViewHolder>(
                TodayMenu.class,R.layout.food_menu_raw_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, TodayMenu todayMenu, int i) {
                setFoodDetails(foodMenuViewHolder,todayMenu);

            }
        };
        lunchAdapter.notifyDataSetChanged();
        lunchRecyclerView.setAdapter(lunchAdapter);
        //Download the images from the firebase
    }

    private void showBreakFastImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("todayMenu").child("breakFast");
        breakFastAdapter=new FirebaseRecyclerAdapter<TodayMenu, FoodMenuViewHolder>(
                TodayMenu.class,R.layout.food_menu_raw_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, TodayMenu todayMenu, int i) {
                setFoodDetails(foodMenuViewHolder,todayMenu);

            }
        };
        breakFastAdapter.notifyDataSetChanged();
        breakFastRecyclerView.setAdapter(breakFastAdapter);
        //Download the images from the firebase
    }


    public static SubscribedUserTodaysMenu newInstance() {

        Bundle args = new Bundle();

        SubscribedUserTodaysMenu fragment = new SubscribedUserTodaysMenu();
        fragment.setArguments(args);
        return fragment;
    }

}
