package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Adapters.CurrentOrderArrayAdapter;
import drunkcoder.com.foodheaven.Models.Food;
import drunkcoder.com.foodheaven.Models.Order;
import drunkcoder.com.foodheaven.Models.SpecialFood;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.SpecialFoodViewHolder;
import info.hoang8f.widget.FButton;

public class CurrentOrder extends Fragment {

    private CurrentOrderArrayAdapter currentOrderArrayAdapter;
    private ListView currentOrderListView;
    private Context context;
    private DatabaseReference specialFoodDatabaseReference;
    private ArrayList<Food> foodArrayList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.current_order,container,false);
        context=getContext();

        fetchCurrentOrderList();
        currentOrderListView=view.findViewById(R.id.currentOrdersListView);
        currentOrderArrayAdapter=new CurrentOrderArrayAdapter(context,foodArrayList);
        currentOrderListView.setAdapter(currentOrderArrayAdapter);
        currentOrderListView.setDivider(null);

    return view;
    }

    private void fetchCurrentOrderList() {
    FirebaseDatabase.getInstance().getReference("Orders").child("NewFoodOrders").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        Order order=dataSnapshot.getValue(Order.class);
                        for(int i=0;i<order.getFoodArrayList().size();i++){
                            foodArrayList.add(order.getFoodArrayList().get(i));
                            if(currentOrderArrayAdapter!=null)
                                currentOrderArrayAdapter.notifyDataSetChanged();
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
    public static CurrentOrder newInstance() {

        Bundle args = new Bundle();

        CurrentOrder fragment = new CurrentOrder();
        fragment.setArguments(args);
        return fragment;
    }
}
