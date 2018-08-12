package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.Activities.Description;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;

public class SubscribedUserTodaysMenu extends Fragment{

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
    private boolean isStartDate;
    private Button submitButton;
    private String startDate;
    private String endDate;

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

                //Moving into description activity and passed text id.
                Intent intent=new Intent(context, Description.class);
                intent.putExtra("ID",wantToEatTextView.getId());
                startActivity(intent);

            }
        });

        return view;
    }

    private void showAbsenceDialog() {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        //alertDialog.setTitle("Mark Your Absence");
        //alertDialog.setIcon(R.drawable.thali_graphic);
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.mark_absence_layout,null,false);
        startDateButton=view.findViewById(R.id.startDateButton);
        endDateButton=view.findViewById(R.id.endDateButton);
        submitButton=view.findViewById(R.id.submitButton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartDate=true;
                showDatePicker();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartDate=false;
                showDatePicker();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map=new HashMap<>();
                map.put("startDate",startDate);
                map.put("endDate",endDate);
                FirebaseDatabase.getInstance().getReference("Absence").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        setValue(map);
                Toast.makeText(context,"Submitted Succesfully", Toast.LENGTH_SHORT).show();
                alertDialog.setView(null);//To dimiss the alert dialog set its view to null
            }
        });
        alertDialog.setView(view);
        alertDialog.show();

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        if(isStartDate){
                            startDateButton.setText(date);
                            startDate=date;

                        }else{
                            endDateButton.setText(date);
                            endDate=date;
                        }
                    }
                },
                calendar.get(Calendar.YEAR), // Initial year selection
                calendar.get(Calendar.MONTH), // Initial month selection
                calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//
//        String date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//        if(isStartDate){
//            startDateButton.setText(date);
//        }else{
//            endDateButton.setText(date);
//        }
//
//
//    }

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
