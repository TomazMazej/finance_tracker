package com.mazej.financetracker.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mazej.financetracker.ApplicationMy;
import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.OnFragmentInteractionListener;
import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mazej.financetracker.MainActivity.toolbar;

public class MainFragment extends Fragment {

    private DatabaseHelper myDB;
    private Cursor data;
    private TextView currentDate;
    private TextView incomeText;
    private TextView expenseText;
    private TextView balanceText;
    private PieChart piechart;
    private ImageButton addIncome;
    private ImageButton addExpense;
    private double income;
    private double expenses;
    public static double balance;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setBackgroundColor(Color.parseColor("#455a64"));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Home");

        currentDate = (TextView) view.findViewById(R.id.dateText);
        incomeText = (TextView) view.findViewById(R.id.incomeText);
        expenseText = (TextView) view.findViewById(R.id.expenseText);
        balanceText = (TextView) view.findViewById(R.id.balanceText);
        piechart = view.findViewById(R.id.piechart);
        addExpense = view.findViewById(R.id.add_expense);
        addIncome = view.findViewById(R.id.add_income);
        income = expenses = 0.0;

        Calendar c = Calendar.getInstance();
        String[]monthName = {"January","February","March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = monthName[c.get(Calendar.MONTH)];
        int year = c.get(Calendar.YEAR);

        data = myDB.getMonthIncome();
        while(data.moveToNext()) {
            income += Double.parseDouble(data.getString(3));
        }

        data = myDB.getMonthExpenses();
        while(data.moveToNext()) {
            expenses += Double.parseDouble(data.getString(3));
        }
        balance = income - expenses;

        piechart.setUsePercentValues(true);
        List<PieEntry> values = new ArrayList<>();

        //Pie Chart
        PieDataSet pieDataSet = new PieDataSet(values, "");
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);

        final int[] MY_COLORS = {Color.rgb(0, 105, 0), Color.rgb(194, 33, 33)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if(income > 0.0 || expenses > 0.0){
            values.add(new PieEntry((int)income, "Income"));
            values.add(new PieEntry((int)expenses, "Expenses"));
            for(int i: MY_COLORS) colors.add(i);
        }
        else{
            values.add(new PieEntry(1, "No data"));
            colors.add(Color.rgb(128,128,128));
        }

        pieDataSet.setColors(colors);

        Description desc = new Description();
        desc.setText("");
        piechart.setDescription(desc);

        piechart.animateXY(1400, 1400);
        //piechart.setHoleRadius(25f);

        currentDate.setText(month + " " + year);
        incomeText.setText("Income: " + income);
        expenseText.setText("Expenses: " + expenses);
        balanceText.setText("Balance: " + balance);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragment(1);
            }
        });

        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragment(2);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }
}
