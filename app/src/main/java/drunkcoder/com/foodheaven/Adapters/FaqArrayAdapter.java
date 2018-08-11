package drunkcoder.com.foodheaven.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import drunkcoder.com.foodheaven.Models.Assistance;
import drunkcoder.com.foodheaven.Models.Faq;
import drunkcoder.com.foodheaven.R;

public class FaqArrayAdapter extends ArrayAdapter {
    private ArrayList<Faq> faqArrayList=new ArrayList<>();
    private Context context;
    private TextView questionTextView,ansTextView;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.f_a_q_fragment_list_adapter_row,parent,false);
        questionTextView=view.findViewById(R.id.questionTextView);
        ansTextView=view.findViewById(R.id.ansTextView);
        questionTextView.setText(faqArrayList.get(position).getQuestion());
        ansTextView.setText(faqArrayList.get(position).getAnswer());

        return view;
    }

    @Override
    public int getCount() {
        return faqArrayList.size();
    }

    public FaqArrayAdapter(Context context, ArrayList<Faq> faqArrayList) {
        super(context,R.layout.our_plans_fragment_listview_row,faqArrayList);
        this.faqArrayList=faqArrayList;
        this.context=context;
    }
}
