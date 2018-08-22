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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.SpecialFood;
import drunkcoder.com.foodheaven.R;
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
//    private ElegantNumberButton elegantNumberButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.special_order_fragment,container,false);
        context=getContext();
        specialFoodArrayList=new ArrayList<>();
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
                    saveDataIntoFirebase();
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

    private void saveDataIntoFirebase() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("SpecialOrder").child("NewOrders");
        for(int i=0;i<specialFoodArrayList.size();i++) {
            Log.i("foodname","------------------"+specialFoodArrayList.get(i).getFoodName()+"  "+specialFoodArrayList.get(i).getFoodQuantity());
            databaseReference.child(specialFoodArrayList.get(i).getFoodName()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(specialFoodArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Save Data into fb", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(context, "Save Data into fb", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
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
                Picasso.with(context).load(specialFood.getImageUrl()).into(specialFoodViewHolder.specialFoodImageView);
                String quantity=specialFoodViewHolder.elegantNumberButton.getNumber();

                //if user first tick the checkBox and then he is incrementing the elegant number then also we have
                //to save that incremented value there.

                if(specialFoodViewHolder.specialFoodCheckBox.isChecked()){
                    //first i will remove that object from arraylist then i will add it in arraylist
                    deleteFoodFromArrayList(specialFood);
                    specialFood.setFoodQuantity(specialFoodViewHolder.elegantNumberButton.getNumber());
                    specialFoodArrayList.add(specialFood);
                }

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
