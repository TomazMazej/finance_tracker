package com.mazej.financetracker.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.objects.Income;
import com.mazej.financetracker.adapters.IncomeListAdapter;
import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class IncomeFragment extends Fragment {
    private DatabaseHelper myDB;
    private Cursor data;
    public static ListView incomeList;
    public static ArrayList<Income> theList;
    public static IncomeListAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        data = myDB.getMonthIncome();
        theList = new ArrayList<>();
        toolbar.setBackgroundColor(Color.parseColor("#006900"));
        toolbar.setTitle("Income");

        incomeList = view.findViewById(R.id.incomeList);

        arrayAdapter = new IncomeListAdapter(getActivity().getBaseContext(), R.layout.adapter_income_layout, theList);
        incomeList.setAdapter(arrayAdapter);

        if(data.getCount() == 0) {
            Toast.makeText(getActivity().getBaseContext(), "You have no incomes this month!", Toast.LENGTH_LONG).show();
        }
        else{
            Income income;
            while(data.moveToNext()) {
                income = new Income(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                theList.add(0, income);
                //theList.add(data.getString(1) + " " + data.getString(2) + " " + data.getString(3));
            }
        }
        return view;
    }
}
