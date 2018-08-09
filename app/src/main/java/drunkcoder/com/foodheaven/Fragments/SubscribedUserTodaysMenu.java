package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.R;

public class SubscribedUserTodaysMenu extends Fragment {

    private RecyclerView breakFastRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView dinnerRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.subscribed_user_todays_menu_layout,container,false);
        breakFastRecyclerView=view.findViewById(R.id.breakFastRecyclerView);
        context=getContext();

        linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        breakFastRecyclerView.setHasFixedSize(true);
        breakFastRecyclerView.setLayoutManager(linearLayoutManager);
        showBreakFastImages();
        showlunchImages();
        showDinnerImages();

        return view;
    }

    private void showDinnerImages() {
        String[] dinnerArray={"Chapati","Dal","Vegetable","Egg","Meat","Something"};

        //Download the images from the firebase
    }

    private void showlunchImages() {
        //Download the images from the firebase
    }

    private void showBreakFastImages() {
        //Download the images from the firebase
    }

    public static Fragment getInstance(){
        return new SubscribedUserTodaysMenu();
    }
}
