    package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Activities.DescriptionActivity;
import drunkcoder.com.foodheaven.Adapters.TodayMenuRecyclerViewAdapter;
import drunkcoder.com.foodheaven.Models.Absence;
import drunkcoder.com.foodheaven.Models.Category;
import drunkcoder.com.foodheaven.Models.Food;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.Models.FoodMenu;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Models.Wallet;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;

public class SubscribedUserTodaysMenu extends Fragment{

    private RecyclerView breakFastRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView dinnerRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
//
//    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> dinnerAdapter;
//    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> breakFastAdapter;
//    private FirebaseRecyclerAdapter<FoodMenu,FoodMenuViewHolder> lunchAdapter;
    private TodayMenuRecyclerViewAdapter dinnerAdapter,breakFastAdapter,lunchAdpater;
    private FirebaseDatabase FoodMenuFirebaseDatabase;
    private TextView markAbsenceTextView;
    private TextView wantToEatTextView;
    private Button startDateButton;
    private Button endDateButton;
    private boolean isStartDate;
    private Button submitButton;
    private String startDate;
    private String endDate;
    private long noOfDaysExtended;
    private ArrayList<Food> dinnerArrayList=new ArrayList<>();
    private ArrayList<Food> lunchArrayList=new ArrayList<>();
    private ArrayList<Food> breakFastArrayList=new ArrayList<>();

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

        FoodMenuFirebaseDatabase=FirebaseDatabase.getInstance();
       // linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        setRecyclerView(breakFastRecyclerView);
        setRecyclerView(lunchRecyclerView);
        setRecyclerView(dinnerRecyclerView);


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
                Intent intent=new Intent(context, DescriptionActivity.class);
                intent.putExtra("ID",wantToEatTextView.getId());
                startActivity(intent);

            }
        });

        loadTodaysMenu();

        PullRefreshLayout todayMenuRefreshLayout=view.findViewById(R.id.todayMenuRefreshLayout);
        todayMenuRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadTodaysMenu();
            }
        });
        todayMenuRefreshLayout.setColor(R.color.colorPrimary);
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean b) {
                //takeToPublicTour();
            }
        });
        return view;
    }

    private void showAbsenceDialog() {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        //alertDialog.setTitle("Mark Your Absence");
        //alertDialog.setIcon(R.drawabl.thali_graphic);
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

                if(MyApplication.getCurrentUser().getAbsence()==null) {
                    if (compareDate()) {
                        Absence absence = new Absence();
                        absence.setStartDate(startDate);
                        absence.setEndDate(endDate);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                child("absence").setValue(absence).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Submitted Succesfully", Toast.LENGTH_SHORT).show();
                                extendDueDate();
                            }
                        });
                        alertDialog.setView(null);//To dimiss the alert dialog set its view to null
                    } else {
                        Toast.makeText(context, "please select correct starting and ending date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "You already have marked absence..So wait until last absence date do not come :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();

    }

    private void extendDueDate() {
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        final DatabaseReference databaseReference=FoodMenuFirebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wallet");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                String previousDueDate=wallet.getDueDate();

                Calendar calendar = Calendar.getInstance();
                try {
                     calendar.setTime(simpleDateFormat.parse(previousDueDate));
                        // So that we can add them later one
                    } catch (ParseException e) {
                    e.printStackTrace();

                }
                calendar.add(Calendar.DATE, (int)noOfDaysExtended);
                Date extendedDate = new Date(calendar.getTimeInMillis()); //we are getting the timeInMillis after adding dates
                String resultLastDate=simpleDateFormat.format(extendedDate); //used to convert date object into String
                wallet.setDueDate(resultLastDate);

                //setting new value
                databaseReference.setValue(wallet).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Your dueDate is extended by "+noOfDaysExtended+" Days",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean compareDate() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date startingDate = simpleDateFormat.parse(startDate); //it parses starDate string into Date object
            Date endingDate = simpleDateFormat.parse(endDate);
            long difference=endingDate.getTime()-startingDate.getTime();
            noOfDaysExtended=difference/(24*60*60*1000);
            return difference > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date;
                        if(monthOfYear+1<10 && dayOfMonth<10){
                            date="0"+dayOfMonth+"/"+"0"+(monthOfYear+1)+"/"+year;
                        } else if(dayOfMonth<10){
                            date="0"+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        }else{
                            date=dayOfMonth+"/"+"0"+(monthOfYear+1)+"/"+year;
                        }

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
        fetchFoods("Dinner");
//        DatabaseReference databaseReference= FoodMenuFirebaseDatabase.getReference("TodayMenu").child("Dinner");
//        dinnerAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
//                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
//            @Override
//            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
//                setFoodDetails(foodMenuViewHolder, foodMenu);
//            }
//        };

        dinnerAdapter=new TodayMenuRecyclerViewAdapter(dinnerArrayList,context);
        dinnerRecyclerView.setAdapter(dinnerAdapter);
        //Download the images from the firebase

    }

    private void fetchFoods(final String mealTime) {
       FoodMenuFirebaseDatabase.getReference("TodayMenu")
                .child(mealTime).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Category category = dataSnapshot.getValue(Category.class);

                if(mealTime.equals("Dinner")) {
                    ArrayList<Food> list = category.getFoodArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        dinnerArrayList.add(list.get(i));
                        dinnerAdapter.notifyDataSetChanged();
                    }
                }
                else if(mealTime.equals("Lunch")){
                    ArrayList<Food> list = category.getFoodArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        lunchArrayList.add(list.get(i));
                        lunchAdpater.notifyDataSetChanged();
                    }
                }
                else{
                    ArrayList<Food> list = category.getFoodArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        breakFastArrayList.add(list.get(i));
                        breakFastAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setFoodDetails(FoodMenuViewHolder foodMenuViewHolder,FoodMenu foodMenu) {
        foodMenuViewHolder.foodNameTextView.setText(foodMenu.getFoodName());
        foodMenuViewHolder.foodDescriptionTextView.setText(foodMenu.getFoodDescription());
        Picasso.with(context).load(foodMenu.getImageUrl()).placeholder(R.drawable.progress_animation).into(foodMenuViewHolder.foodImageView);

    }

    private void showlunchImages() {
//        DatabaseReference databaseReference= FoodMenuFirebaseDatabase.getReference("TodayMenu").child("Lunch");
//        lunchAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
//                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
//            @Override
//            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
//                setFoodDetails(foodMenuViewHolder, foodMenu);
//
//            }
//        };
        fetchFoods("Lunch");
        lunchAdpater=new TodayMenuRecyclerViewAdapter(lunchArrayList,context);
        lunchRecyclerView.setAdapter(lunchAdpater);
        lunchAdpater.notifyDataSetChanged();
        //Download the images from the firebase
    }

    private void showBreakFastImages() {
//        DatabaseReference databaseReference= FoodMenuFirebaseDatabase.getReference("TodayMenu").child("BreakFast");
//        breakFastAdapter=new FirebaseRecyclerAdapter<FoodMenu, FoodMenuViewHolder>(
//                FoodMenu.class,R.layout.food_menu_row_layout,FoodMenuViewHolder.class,databaseReference) {
//            @Override
//            protected void populateViewHolder(FoodMenuViewHolder foodMenuViewHolder, FoodMenu foodMenu, int i) {
//                setFoodDetails(foodMenuViewHolder, foodMenu);
//
//            }
//        };

        fetchFoods("BreakFast");
        breakFastAdapter=new TodayMenuRecyclerViewAdapter(breakFastArrayList,context);
        breakFastAdapter.notifyDataSetChanged();
        breakFastRecyclerView.setAdapter(breakFastAdapter);
        breakFastAdapter.notifyDataSetChanged();
        //Download the images from the firebase
    }


    public static SubscribedUserTodaysMenu newInstance() {

        Bundle args = new Bundle();
        SubscribedUserTodaysMenu fragment = new SubscribedUserTodaysMenu();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadTodaysMenu(){

        Plan currentUserPlan= MyApplication.getCurrentUser().subscribedPlan;

        showBreakFastImages();
        showlunchImages();
        showDinnerImages();

        if(!currentUserPlan.includesBreakFast) {

              breakFastRecyclerView.setAlpha(0.3f);
        }

        if(!currentUserPlan.includesLunch) {

             lunchRecyclerView.setAlpha(0.3f);
        }

        if(!currentUserPlan.includesDinner){

            dinnerRecyclerView.setAlpha(0.3f);
        }
    }

    public void takeToPublicTour(){

        View one = wantToEatTextView;
        int[] oneLocation = new int[2];
        one.getLocationInWindow(oneLocation);
        float oneX = oneLocation[0] + one.getWidth() / 2f;
        float oneY = oneLocation[1] + one.getHeight() / 2f;
        Log.i("mesure", "takeToPublicTour: "+oneX+" "+oneY);
        // make an target
        SimpleTarget firstTarget = new SimpleTarget.Builder(getActivity()).setPoint(oneX, oneY)
                .setShape(new Circle(400f))
                .setTitle("Your Choice")
                .setDescription("Click here to choose your meal from variety of options")
                .build();

        Spotlight.with(getActivity())
                .setOverlayColor(R.color.background)
                .setDuration(400L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(firstTarget)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                    @Override
                    public void onStarted() {
                        Toast.makeText(getContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEnded() {
                        Toast.makeText(getActivity(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                    }
                }).start();



    }
}
