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
import com.mazej.financetracker.events.MessageToastEvent;
import com.mazej.financetracker.onFragmentBtnSelected;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class EditAccountFragment extends Fragment {

    private onFragmentBtnSelected listener;
    private DatabaseHelper myDB;
    private Cursor data;
    public static ArrayList<String> theList;
    private Spinner select_acc;
    public static EditText amount;
    private Button add_acc_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setTitle("Edit Accounts");

        select_acc = view.findViewById(R.id.select_acc);
        amount = view.findViewById(R.id.amount);
        add_acc_btn = view.findViewById(R.id.add_acc_btn);

        data = myDB.getAccounts();
        theList = new ArrayList<>();

        while(data.moveToNext()) {
            theList.add(data.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, theList);
        select_acc.setAdapter(adapter);

        add_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.length() == 0){ //if we didnt input all the data
                    Toast.makeText(getActivity().getBaseContext(), "Please enter a new account.",Toast.LENGTH_LONG).show();
                }
                else { //add data to database
                    myDB.editAccount(select_acc.getSelectedItem().toString(), amount.getText().toString());
                    listener.EditToAccount();
                    EventBus.getDefault().post(new MessageToastEvent("Account edited"));
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
