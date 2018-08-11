package drunkcoder.com.foodheaven.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.R;

public class WeeklyMenuFragment extends Fragment {
    ImageView sunImageView,monImageView,tuesImageView,thrusImageView,wedImageView,friImageView,satImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_menu_fragment, container, false);
        sunImageView=view.findViewWithTag("SUN");
        monImageView=view.findViewWithTag("MON");
        tuesImageView=view.findViewWithTag("TUES");
        wedImageView=view.findViewWithTag("WED");
        thrusImageView=view.findViewWithTag("THRUS");
        friImageView=view.findViewWithTag("FRI");
        satImageView=view.findViewWithTag("SAT");

        setImageTextDrawable(sunImageView);
        setImageTextDrawable(monImageView);
        setImageTextDrawable(tuesImageView);
        setImageTextDrawable(wedImageView);
        setImageTextDrawable(thrusImageView);
        setImageTextDrawable(friImageView);
        setImageTextDrawable(satImageView);
        return view;
    }

    private void setImageTextDrawable(ImageView imageView) {
        Typeface custom_font=null;
            custom_font=ResourcesCompat.getFont(getContext(),R.font.josefin_bold);
        TextDrawable textDrawable=TextDrawable.builder()
                .beginConfig().textColor(Color.BLACK)
                .useFont(custom_font)
                .fontSize(60).endConfig()
                .buildRoundRect(imageView.getTag().toString(), Color.CYAN,10);

        imageView.setImageDrawable(textDrawable);

    }

    void showTodaysMenu(View view){
        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

    }

    public static WeeklyMenuFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WeeklyMenuFragment fragment = new WeeklyMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
