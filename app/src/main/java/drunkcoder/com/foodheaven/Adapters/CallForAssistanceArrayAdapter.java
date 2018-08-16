package drunkcoder.com.foodheaven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import drunkcoder.com.foodheaven.Models.Assistance;
import drunkcoder.com.foodheaven.R;

public class CallForAssistanceArrayAdapter extends ArrayAdapter {
    private ArrayList<Assistance> assistancesArrayList=new ArrayList<>();
    private Context context;
    private TextView centerNameTextView,phoneNoTextView;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.call_for_assistance_fragment_list_adapter_row,parent,false);
        centerNameTextView=view.findViewById(R.id.centerName);
        phoneNoTextView=view.findViewById(R.id.phoneNo);
        centerNameTextView.setText(assistancesArrayList.get(position).getCenter());
        phoneNoTextView.setText(assistancesArrayList.get(position).getPhoneNo());

        return view;
    }

    @Override
    public int getCount() {
        return assistancesArrayList.size();
    }

    public CallForAssistanceArrayAdapter(Context context, ArrayList<Assistance> assistancesArrayList) {
        super(context,R.layout.our_plans_fragment_listview_row,assistancesArrayList);
        this.assistancesArrayList=assistancesArrayList;
        this.context=context;
    }
}
