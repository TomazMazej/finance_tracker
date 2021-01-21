package com.mazej.financetracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.events.MessageToastEvent;
import com.mazej.financetracker.onFragmentBtnSelected;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class AddAccountFragment extends Fragment {

    private onFragmentBtnSelected listener;
    private DatabaseHelper myDB;
    private EditText accName;
    public static EditText amount;
    private Button add_acc_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setTitle("Add Accounts");

        accName = view.findViewById(R.id.accName);
        amount = view.findViewById(R.id.amount);
        add_acc_btn = view.findViewById(R.id.add_acc_btn);

        add_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accName.length() == 0 || amount.length() == 0){ //ce je okence prazno
                    Toast.makeText(getActivity().getBaseContext(), "Please enter a new account.",Toast.LENGTH_LONG).show();
                }
                else { //dodamo podatke v bazo
                    myDB.addAccount(accName.getText().toString(), amount.getText().toString());
                    listener.AddToAccount();
                    EventBus.getDefault().post(new MessageToastEvent("Account added"));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventToast(MessageToastEvent messageToastEvent){
        Activity activity = getActivity();
        Toast.makeText(activity, messageToastEvent.message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
