package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.SpecialFood;
import drunkcoder.com.foodheaven.Models.SpecialFoodOrder;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.ProgressUtils;
import drunkcoder.com.foodheaven.ViewHolders.SpecialFoodViewHolder;
import info.hoang8f.widget.FButton;

public class SpecialOrders extends Fragment {

    private RecyclerView specialOrderRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private DatabaseReference specialFoodDatabaseReference;
    private FirebaseRecyclerAdapter<SpecialFood, SpecialFoodViewHolder> specialFoodAdapter;
    private ArrayList<SpecialFood> specialFoodArrayList;
    private FButton orderNowButton;
    private SpecialFoodOrder specialFoodOrder;
//    private ElegantNumberButton elegantNumberButton;
    private Spinner specialOrderChooseMealTimeSpinner;
    private FButton specialOrderPaymentButton;
    private int selectedMeal=0;
    private String mealTimeArray[];
    private AlertDialog.Builder alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.special_order_fragment,container,false);
        context=getContext();
        specialFoodArrayList=new ArrayList<>();
        mealTimeArray=getResources().getStringArray(R.array.mealTime);
        specialOrderRecyclerView=view.findViewById(R.id.specialFoodRecyclerView);
        orderNowButton=view.findViewById(R.id.orderNowButton);
        layoutManager=new LinearLayoutManager(context);
//        elegantNumberButton=view.findViewById(R.id.elegantNumberButton);
        PullRefreshLayout pullRefreshLayout=view.findViewById(R.id.pullRefreshLayout);
        specialOrderRecyclerView.setHasFixedSize(true);
        specialOrderRecyclerView.setLayoutManager(layoutManager);

        orderNowButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        showSpecialFoodList();
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(specialFoodArrayList.size()!=0){
                    //Place the order in the firebase
                    Toast.makeText(context,specialFoodArrayList.size()+" item selected",Toast.LENGTH_SHORT).show();
                    showPlaceOrderAlertDialog();
                }else{
                    Toast.makeText(context,"Please first select the item",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //This below method is used to refresh the page on swiping down the recyclerview
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSpecialFoodList();
            }
        });
        pullRefreshLayout.setColor(R.color.colorPrimary);
        return view;
    }

    private void showPlaceOrderAlertDialog(){
        alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Order the food");
        alertDialog.setIcon(R.drawable.thali_graphic);
        LayoutInflater layoutInflater=getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.special_order_payment_alert_dialog,null,false);
        specialOrderChooseMealTimeSpinner=view.findViewById(R.id.specialOrderChooseMealTimeSpinner);
        specialOrderPaymentButton=view.findViewById(R.id.specialOrderPaymentButton);


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,mealTimeArray);
        specialOrderChooseMealTimeSpinner.setAdapter(arrayAdapter);

        specialOrderChooseMealTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeal=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        specialOrderPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMeal!=0){
                    saveDataIntoFirebase();
                    ProgressUtils.showLoadingDialog(context);
                    //go for payment
                }else{
                    Toast.makeText(context,"Please first select the meal time",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void saveDataIntoFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SpecialOrder").child("NewOrders");
        for (int i = 0; i < specialFoodArrayList.size(); i++) {
            Log.i("foodname", "------------------" + specialFoodArrayList.get(i).getFoodName() + "  " + specialFoodArrayList.get(i).getFoodQuantity());
            databaseReference.child(specialFoodArrayList.get(i).getFoodName()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(specialFoodArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(context, "Save Data into fb", Toast.LENGTH_SHORT).show();
                }
            });
        }

        specialFoodOrder = new SpecialFoodOrder();
        specialFoodOrder.setSpecialFoodsArrayList(specialFoodArrayList);
        specialFoodOrder.setMealTime(mealTimeArray[selectedMeal]);
        //Saving special order in another node also
        FirebaseDatabase.getInstance().getReference("Orders").child("NewSpecialOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(specialFoodOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Order Successful", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
                alertDialog.setView(null);

            }
        });
    }

    private void showSpecialFoodList() {
        specialFoodDatabaseReference= FirebaseDatabase.getInstance().getReference("SpecialOrder").child("FoodImages");
        specialFoodAdapter=new FirebaseRecyclerAdapter<SpecialFood, SpecialFoodViewHolder>(SpecialFood.class,
                R.layout.special_food_raw_layout,SpecialFoodViewHolder.class,specialFoodDatabaseReference) {
            @Override
            protected void populateViewHolder(final SpecialFoodViewHolder specialFoodViewHolder, final SpecialFood specialFood, int i) {
                specialFoodViewHolder.specialFoodDescriptionTextView.setText(specialFood.getFoodDescription());
                specialFoodViewHolder.specialFoodNameTextView.setText(specialFood.getFoodName());
//                specialFoodViewHolder.specialFoodQuantityTextView.setText(specialFood.getFoodQuantity());

                //CallBack method is used to remove progress bar from the frame when image fetched successfully
                Picasso.with(context).load(specialFood.getImageUrl()).into(specialFoodViewHolder.specialFoodImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if(specialFoodViewHolder.imageProgressBar!=null){
                            specialFoodViewHolder.imageProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

                //if user first tick the checkBox and then he is incrementing the elegant number then also we have
                //to save that incremented value there.
                specialFoodViewHolder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        if(specialFoodViewHolder.specialFoodCheckBox.isChecked()){
                            //first i will remove that object from arraylist then i will add it in arraylist
                            deleteFoodFromArrayList(specialFood);
                            specialFood.setFoodQuantity(specialFoodViewHolder.elegantNumberButton.getNumber());
                            Log.i("ElegantNumber",specialFoodViewHolder.elegantNumberButton.getNumber());
                            Log.i("special food quantity",specialFood.getFoodQuantity());
                            specialFoodArrayList.add(specialFood);
                        }
                    }
                });

                specialFoodViewHolder.specialFoodCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox=(CheckBox)view;
                        if(checkBox.isChecked()){
                            Log.i("food","-------"+specialFood.getFoodName());
                            specialFood.setFoodQuantity(specialFoodViewHolder.elegantNumberButton.getNumber());
                            specialFoodArrayList.add(specialFood);
                        }else{
                            Log.i("removed","------"+specialFood.getFoodName());
                            deleteFoodFromArrayList(specialFood);
                        }
                    }
                });
            }
        };
        specialOrderRecyclerView.setAdapter(specialFoodAdapter);
    }

    private void deleteFoodFromArrayList(SpecialFood specialFood){
        for(int j=0;j<specialFoodArrayList.size();j++){
            if(specialFoodArrayList.get(j).getFoodName().equals(specialFood.getFoodName())){
                Log.i("inside if",specialFoodArrayList.get(j).getFoodQuantity());
//                                    specialFoodArrayList.remove(specialFoodArrayList.get(j));
                specialFoodArrayList.remove(j);
                return;
            }
        }
    }


    public static SpecialOrders newInstance() {

        Bundle args = new Bundle();

        SpecialOrders fragment = new SpecialOrders();
        fragment.setArguments(args);
        return fragment;
    }
}
