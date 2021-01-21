package com.mazej.financetracker.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mazej.financetracker.ApplicationMy;
import com.mazej.financetracker.MainActivity;
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
    private double income;
    private double expenses;
    public static double balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setBackgroundColor(Color.parseColor("#455a64"));
        toolbar.setTitle("Home");

        currentDate = (TextView) view.findViewById(R.id.dateText);
        incomeText = (TextView) view.findViewById(R.id.incomeText);
        expenseText = (TextView) view.findViewById(R.id.expenseText);
        balanceText = (TextView) view.findViewById(R.id.balanceText);
        piechart = view.findViewById(R.id.piechart);
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
        values.add(new PieEntry((int)income, "Income"));
        values.add(new PieEntry(100, "Expenses"));

        //Pie Chart
        PieDataSet pieDataSet = new PieDataSet(values, "");
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);

        final int[] MY_COLORS = {Color.rgb(0, 105, 0), Color.rgb(194, 33, 33)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if(income != 0.0 || expenses != 0.0){
            for(int i: MY_COLORS) colors.add(i);
        }
        else{
            colors.add(Color.rgb(0, 0, 105));
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

        return view;
    }
}
