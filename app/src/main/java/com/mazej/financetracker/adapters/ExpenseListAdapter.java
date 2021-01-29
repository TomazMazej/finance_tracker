package com.mazej.financetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mazej.financetracker.ApplicationMy;
import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.R;
import com.mazej.financetracker.objects.Expense;
import com.mazej.financetracker.objects.Income;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ExpenseListAdapter  extends ArrayAdapter<Expense> {

    private Context mContext;
    private int mResource;

    private String id;
    private String category;
    private String description;
    private String amount;
    private String date;

    private LayoutInflater inflater;

    private TextView tvDate;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvAmount;
    public static CheckBox simpleCheckBox;

    public ExpenseListAdapter(Context context, int resource, ArrayList<Expense> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        id = getItem(position).getId();
        category = getItem(position).getCategory();
        description = getItem(position).getDescription();
        amount = getItem(position).getAmount();
        date = getItem(position).getDate();

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvDate = (TextView) convertView.findViewById(R.id.textView);
        tvCategory = (TextView) convertView.findViewById(R.id.textView2);
        tvDescription = (TextView) convertView.findViewById(R.id.textView3);
        tvAmount = (TextView) convertView.findViewById(R.id.textView4);
        simpleCheckBox = (CheckBox) convertView.findViewById(R.id.simpleCheckBox);

        tvDate.setText(date);
        tvCategory.setText(category);
        tvDescription.setText(description);
        tvAmount.setText(amount + " " + ApplicationMy.currency);

        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    MainActivity.deleteList.add(position);
                    System.out.println(position);
                }
                else{
                    for(int i = 0; i < MainActivity.deleteList.size(); i++){
                        if(MainActivity.deleteList.get(i) == position){
                            MainActivity.deleteList.remove(i);
                        }
                    }
                }
            }
        });
        return convertView;
    }

    public static CheckBox getSimpleCheckBox() {
        return simpleCheckBox;
    }

    public static void setSimpleCheckBox(CheckBox simpleCheckBox) {
        IncomeListAdapter.simpleCheckBox = simpleCheckBox;
    }
}
