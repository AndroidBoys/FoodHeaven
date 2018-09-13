package drunkcoder.com.foodheaven.Fragments.AppIntroFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.R;

public class AppIntroFragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.app_intro_fragment_layout_3,container,false);
        return view;
    }

    public static AppIntroFragment3 newInstance() {

        Bundle args = new Bundle();
        AppIntroFragment3 fragment = new AppIntroFragment3();
        fragment.setArguments(args);
        return fragment;
    }
}
