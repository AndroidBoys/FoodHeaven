package drunkcoder.com.foodheaven.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import drunkcoder.com.foodheaven.Fragments.SpecialOrders;
import drunkcoder.com.foodheaven.Fragments.SubscribedUserTodaysMenu;
import drunkcoder.com.foodheaven.Fragments.WalletFragment;

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return SubscribedUserTodaysMenu.newInstance();

            case 1:
                return SpecialOrders.newInstance();

            case 2:
                return WalletFragment.newInstance();
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
                return "Today's Menu";

            case 1:
                return "Special Order";

            case 2:
                return "Wallet";

        }
        return null;
    }


}
