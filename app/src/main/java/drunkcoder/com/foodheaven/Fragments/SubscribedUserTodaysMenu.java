package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;

public class SubscribedUserTodaysMenu extends Fragment {

    private RecyclerView breakFastRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView dinnerRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> dinnerAdapter;
    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> breakFastAdapter;
    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> lunchAdapter;
    private FirebaseDatabase todayMenuFirebaseDatabase;
    private TextView markAbsenceTextView;
    private TextView wantToEatTextView;
    private Button startDateButton;
    private Button endDateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.subscribed_user_todays_menu_layout,container,false);
        breakFastRecyclerView=view.findViewById(R.id.breakFastRecyclerView);
        lunchRecyclerView=view.findViewById(R.id.lunchRecyclerView);
        dinnerRecyclerView=view.findViewById(R.id.dinnerRecyclerView);
        markAbsenceTextView=view.findViewById(R.id.markAbsenceTextView);
        wantToEatTextView=view.findViewById(R.id.wantToEatTextView);
        context=getContext();

        todayMenuFirebaseDatabase=FirebaseDatabase.getInstance();
       // linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        setRecyclerView(breakFastRecyclerView);
        setRecyclerView(lunchRecyclerView);
        setRecyclerView(dinnerRecyclerView);
        showBreakFastImages();
        showlunchImages();
        showDinnerImages();

        markAbsenceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAbsenceDialog();
            }
        });

        wantToEatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void showAbsenceDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Mark Your Absence");
        alertDialog.setIcon(R.drawable.thali_graphic);
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.mark_absence_layout,null,false);
        startDateButton=view.findViewById(R.id.startDateButton);
        endDateButton=view.findViewById(R.id.endDateButton);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Save the absence data into firebase
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showDatePicker() {

        final Calendar calendar=Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                startDateButton.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(context,dateSetListener,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
    }


    private void setRecyclerView(RecyclerView recyclerView) {

        linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void showDinnerImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("TodayMenu").child("dinner");
        dinnerAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
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

    private void setFoodDetails(FoodMenuViewHolder foodMenuViewHolder,FoodMenu foodMenu) {
        foodMenuViewHolder.foodNameTextView.setText(foodMenu.getFoodName());
        foodMenuViewHolder.foodDescriptionTextView.setText(foodMenu.getFoodDescription());
        Picasso.with(context).load(foodMenu.getImageUrl()).into(foodMenuViewHolder.foodImageView);

    }

    private void showlunchImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("TodayMenu").child("lunch");
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

    private void showBreakFastImages() {
        DatabaseReference databaseReference= todayMenuFirebaseDatabase.getReference("TodayMenu").child("breakFast");
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


    public static SubscribedUserTodaysMenu newInstance() {

        Bundle args = new Bundle();

        SubscribedUserTodaysMenu fragment = new SubscribedUserTodaysMenu();
        fragment.setArguments(args);
        return fragment;
    }

}
