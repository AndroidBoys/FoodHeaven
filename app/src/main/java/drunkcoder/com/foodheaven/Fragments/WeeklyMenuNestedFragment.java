package drunkcoder.com.foodheaven.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;

public class WeeklyMenuNestedFragment extends Fragment {

    private RecyclerView breakFastRecyclerView,
            lunchRecyclerView,
            dinnerRecyclerView;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> dinnerAdapter,
            lunchAdapter,
            breakFastAdapter;
    private String day,fullNameOfDay;
    private Activity activity;

    FirebaseDatabase todayMenuFirebaseDatabase;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weekly_menu_nested_fragment,container,false);
        breakFastRecyclerView=view.findViewById(R.id.breakFastRecyclerView);
        lunchRecyclerView=view.findViewById(R.id.lunchRecyclerView);
        dinnerRecyclerView=view.findViewById(R.id.dinnerRecyclerView);
        context=getContext();
        activity=getActivity();
        //PullRefreshLayout is used to refresh the page
        PullRefreshLayout weeklyRefreshLayout=view.findViewById(R.id.weeklyRefreshLayout);

        //it will get the day name passed when a perticular day is pressed from weeklyMenuFragment
        Bundle args=getArguments();
        day=args.getString("DAY",null);
        fullNameOfDay=getFullNameOfDay(day);

        todayMenuFirebaseDatabase=FirebaseDatabase.getInstance();
        // linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        setRecyclerView(breakFastRecyclerView);
        setRecyclerView(lunchRecyclerView);
        setRecyclerView(dinnerRecyclerView);
        showBreakFastImages(day);
        showlunchImages(day);
        showDinnerImages(day);
        weeklyRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showBreakFastImages(day);
                showlunchImages(day);
                showDinnerImages(day);
            }
        });
        weeklyRefreshLayout.setColor(R.color.colorPrimary);
        return view;
    }

    private String getFullNameOfDay(String day) {
        switch (day){
            case "Mon":
                return "Monday";
            case "Tues":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thrus":
                return "Thrusday";
            case "Fri":
                return "Friday";
            case "Sat":
                return "Saturday";
            case "Sun":
                return "Sunday";
        }
        return null;
    }

    private void setRecyclerView(RecyclerView recyclerView) {

        linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void showDinnerImages(String day) {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("WeeklyMenu").child(day).child("Dinner");
        dinnerAdapter=new FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder>(
                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
                setFoodDetails(foodMenuViewHolder, foodMenu);

            }
        };
        dinnerAdapter.notifyDataSetChanged();
        dinnerRecyclerView.setAdapter(dinnerAdapter);
        //Download the images from the firebase
    }

    private void setFoodDetails(final FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu) {
        foodMenuViewHolder.foodNameTextView.setText(foodMenu.getFoodName());
        foodMenuViewHolder.foodDescriptionTextView.setText(foodMenu.getFoodDescription());
        Picasso.with(context).load(foodMenu.getImageUrl()).into(foodMenuViewHolder.foodImageView, new Callback() {
            @Override
            public void onSuccess() {
                if(foodMenuViewHolder.imageProgressBar!=null) {
                    foodMenuViewHolder.imageProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    private void showlunchImages(String day) {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("WeeklyMenu").child(day).child("Lunch");
        lunchAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
                setFoodDetails(foodMenuViewHolder, foodMenu);

            }
        };
        lunchAdapter.notifyDataSetChanged();
        lunchRecyclerView.setAdapter(lunchAdapter);
        //Download the images from the firebase
    }

    private void showBreakFastImages(String day) {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("WeeklyMenu").child(day).child("BreakFast");
        breakFastAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
                setFoodDetails(foodMenuViewHolder, foodMenu);

            }
        };
        breakFastAdapter.notifyDataSetChanged();
        breakFastRecyclerView.setAdapter(breakFastAdapter);
        //Download the images from the firebase
    }

    public static WeeklyMenuNestedFragment newInstance(String day) {

        Bundle args = new Bundle();
        args.putString("DAY",day);

        WeeklyMenuNestedFragment fragment = new WeeklyMenuNestedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DescriptionActivity)activity).setActionBarTitle(fullNameOfDay+" Food Menu");
    }
}
