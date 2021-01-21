package com.mazej.financetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.R;
import com.mazej.financetracker.objects.Account;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class AccountsListAdapter extends ArrayAdapter<Account> {

    private Context mContext;
    private int mResource;

    private String id;
    private String accountName;
    private String amount;

    private LayoutInflater inflater;

    private TextView tvName;
    private TextView tvAmount;
    private CheckBox simpleCheckBox;

    public AccountsListAdapter(Context context, int resource, ArrayList<Account> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        id = getItem(position).getId();
        accountName = getItem(position).getName();
        amount = getItem(position).getAmount();

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        tvName = (TextView) convertView.findViewById(R.id.accName);
        tvAmount = (TextView) convertView.findViewById(R.id.amount);
        simpleCheckBox = (CheckBox) convertView.findViewById(R.id.simpleCheckBox);

        tvName.setText(accountName);
        tvAmount.setText(amount);

        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    MainActivity.deleteList.add(position);
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
}
