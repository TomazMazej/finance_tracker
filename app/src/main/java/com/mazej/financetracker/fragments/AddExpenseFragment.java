package com.mazej.financetracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.onFragmentBtnSelected;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class AddExpenseFragment extends Fragment {

    private onFragmentBtnSelected listener;
    private DatabaseHelper myDB;
    private Cursor data;
    private EditText description;
    private Button add_expense_btn;
    private Spinner category_dropdown;
    private Spinner account_dropdown;
    public static EditText amount;
    public static ArrayList<String> theList;
    public static ArrayList<String> theList2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setTitle("Add Expense");

        data = myDB.getCategories();
        theList = new ArrayList<>();
        theList2 = new ArrayList<>();

        description = view.findViewById(R.id.description);
        amount = view.findViewById(R.id.amount);
        add_expense_btn = view.findViewById(R.id.add_category_btn);
        category_dropdown = view.findViewById(R.id.spinner1);
        account_dropdown = view.findViewById(R.id.spinner2);

        while(data.moveToNext()) {
            theList.add(data.getString(1));
        }

        data = myDB.getAccounts();
        while(data.moveToNext()) {
            theList2.add(data.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, theList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, theList2);

        category_dropdown.setAdapter(adapter);
        account_dropdown.setAdapter(adapter2);

        add_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(description.length()==0 || amount.length()==0 ){ //ce je okence prazno
                    Toast.makeText(getActivity().getBaseContext(), "Please enter all the data!.",Toast.LENGTH_LONG).show();
                }
                else { //dodamo podatke v bazo
                    myDB.addExpenses(category_dropdown.getSelectedItem().toString(), description.getText().toString(), amount.getText().toString());
                    myDB.addToAccount(account_dropdown.getSelectedItem().toString(), amount.getText().toString());
                    listener.AddToExpense();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        if(context instanceof onFragmentBtnSelected){
            listener = (onFragmentBtnSelected)context;
        }
        else{
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }
}
