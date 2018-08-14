package drunkcoder.com.foodheaven.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import drunkcoder.com.foodheaven.Payments.PaymentsActivity;
import drunkcoder.com.foodheaven.R;
import info.hoang8f.widget.FButton;

public class WalletFragment extends Fragment {

    FButton addMoneyButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet,container,false);
        addMoneyButton = view.findViewById(R.id.addToWalletButton);
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PaymentsActivity.class));
            }
        });
        addMoneyButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        return view;
    }

    public static WalletFragment newInstance() {

        Bundle args = new Bundle();

        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
