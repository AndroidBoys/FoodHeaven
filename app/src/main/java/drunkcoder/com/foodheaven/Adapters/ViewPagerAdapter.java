package drunkcoder.com.foodheaven.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import drunkcoder.com.foodheaven.Fragments.SpecialOrders;
import drunkcoder.com.foodheaven.Fragments.SubscribedUserTodaysMenu;
import drunkcoder.com.foodheaven.Fragments.Wallet;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return SubscribedUserTodaysMenu.getInstance();

            case 1:
                return SpecialOrders.getInstance();

            case 2:
                return Wallet.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Menu";

            case 1:
                return "Special Order";

            case 2:
                return "Wallet";

        }
        return null;
    }


}
