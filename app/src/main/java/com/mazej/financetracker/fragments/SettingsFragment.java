package com.mazej.financetracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mazej.financetracker.ApplicationMy;
import com.mazej.financetracker.R;
import com.mazej.financetracker.onFragmentBtnSelected;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class SettingsFragment extends Fragment {

    private onFragmentBtnSelected listener;
    private EditText fullName;
    private EditText email;
    public static EditText budged;
    private Button save_btn;
    private Spinner currencies_dropdown;
    public static ArrayList<String> theList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar.setTitle("Settings");
        toolbar.setBackgroundColor(Color.parseColor("#455a64"));

        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        budged = view.findViewById(R.id.budged);
        currencies_dropdown = view.findViewById(R.id.currencies);
        save_btn = view.findViewById(R.id.save_btn);
        theList = new ArrayList<>();

        //currencies
        String[] currencies = {"EUR", "USD", "GBP", "YEN"};
        theList.addAll(Arrays.asList(currencies));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, theList);
        currencies_dropdown.setAdapter(adapter);

        fullName.setText(ApplicationMy.sp.getString("fullName",""));
        email.setText(ApplicationMy.sp.getString("email",""));
        budged.setText(ApplicationMy.sp.getString("budged",""));

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ApplicationMy.sp.edit();
                editor.putString("fullName", fullName.getText().toString());
                editor.putString("email", email.getText().toString());
                if(budged.getText().toString().equals("")){
                    editor.putString("budged", "1000000000");
                }
                else{
                    editor.putString("budged", budged.getText().toString());
                }
                editor.putString("currency", currencies_dropdown.getSelectedItem().toString());
                editor.apply();
                ApplicationMy.currency = currencies_dropdown.getSelectedItem().toString();
                listener.SettingsToMain();
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
