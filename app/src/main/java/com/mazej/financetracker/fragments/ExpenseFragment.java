package com.mazej.financetracker.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mazej.financetracker.R;
import com.mazej.financetracker.adapters.ExpenseListAdapter;
import com.mazej.financetracker.adapters.IncomeListAdapter;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.objects.Expense;
import com.mazej.financetracker.objects.Income;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class ExpenseFragment extends Fragment {
    private DatabaseHelper myDB;
    private Cursor data;
    public static ListView expensesList;
    public static ArrayList<Expense> theList;
    public static ExpenseListAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        data = myDB.getMonthExpenses();
        theList = new ArrayList<>();
        toolbar.setBackgroundColor(Color.parseColor("#C22121"));
        toolbar.setTitle("Expenses");

        expensesList = view.findViewById(R.id.expensesList);

        arrayAdapter = new ExpenseListAdapter(getActivity().getBaseContext(), R.layout.adapter_expenses_layout, theList);
        expensesList.setAdapter(arrayAdapter);

        if(data.getCount() == 0) {
            Toast.makeText(getActivity().getBaseContext(), "You have no expenses this month!", Toast.LENGTH_LONG).show();
        }
        else{
            Expense expense;
            while(data.moveToNext()) {
                expense = new Expense(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                theList.add(0, expense);
            }
        }
        return view;
    }
}