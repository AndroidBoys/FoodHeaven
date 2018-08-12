package drunkcoder.com.foodheaven.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import drunkcoder.com.foodheaven.Adapters.ViewPagerAdapter;
import drunkcoder.com.foodheaven.R;

public class SubscribedUserFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.subscribed_user_fragment_layout,container,false);
        Log.i("Inside","Subscribed Fragment");
        tabLayout=view.findViewById(R.id.tabLayout);
        viewPager=view.findViewById(R.id.subscriptionViewPager);
        context=getContext();

        //First we will set the adapter to the viewPager
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        //user want to go to these menus from drawer
       if(getArguments()!=null){
           int position=getArguments().getInt("POSITION");
           if(position==1)
           viewPager.setCurrentItem(position);
           else if(position==2)
               viewPager.setCurrentItem(position);
       }


        //And then we set the viewPager on the tabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(getActivity().getColor(R.color.white));

        //This below one is used to set the custom textview on tabLayout items.
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv=(TextView)LayoutInflater.from(context).inflate(R.layout.custom_tab,null);
//            tv.setTypeface(Typeface);null
            tv.setTextColor(getActivity().getResources().getColor(R.color.white));
            tabLayout.getTabAt(i).setCustomView(tv);

        }

        return view;
    }


    public static SubscribedUserFragment newInstance() {

        Bundle args = new Bundle();

        SubscribedUserFragment fragment = new SubscribedUserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
