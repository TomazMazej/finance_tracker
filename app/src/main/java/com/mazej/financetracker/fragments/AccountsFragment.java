package com.mazej.financetracker.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazej.financetracker.ApplicationMy;
import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.R;
import com.mazej.financetracker.adapters.AccountsListAdapter;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.objects.Account;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class AccountsFragment extends Fragment {

    private DatabaseHelper myDB;
    private Cursor data;
    private ListView accountsList;
    private TextView totalBalance;
    public static ArrayList<Account> theList;
    public static AccountsListAdapter arrayAdapter;
    private double balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        data = myDB.getAccounts();
        toolbar.setBackgroundColor(Color.parseColor("#455a64"));
        toolbar.setTitle("Accounts");
        accountsList = (ListView) view.findViewById(R.id.accountsList);
        totalBalance = (TextView) view.findViewById(R.id.totalBalance);
        balance = 0.0;

        theList = new ArrayList<>();
        arrayAdapter = new AccountsListAdapter(getActivity().getBaseContext(), R.layout.adapter_accounts_layout, theList);
        accountsList.setAdapter(arrayAdapter);

        if(data.getCount() == 0) {
            Toast.makeText(getActivity().getBaseContext(), "You have no accounts!", Toast.LENGTH_LONG).show();
        }
        else {
            Account acc;
            while (data.moveToNext()) {
                acc = new Account(data.getString(0), data.getString(1), data.getString(2));
                theList.add(0, acc);
                balance += Integer.parseInt(data.getString(2));
            }
        }
        totalBalance.setText("Total: " + balance + " " + ApplicationMy.currency);
        return view;
    }
}
