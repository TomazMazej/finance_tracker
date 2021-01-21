package com.mazej.financetracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.onFragmentBtnSelected;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.toolbar;

public class AddCategoryFragment extends Fragment {

    private onFragmentBtnSelected listener;
    private DatabaseHelper myDB;
    public static EditText newCategory;
    private Button add_category_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        toolbar.setTitle("Add Category");

        newCategory = view.findViewById(R.id.newCategory);
        add_category_btn = view.findViewById(R.id.add_category_btn);

        add_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newCategory.length()==0 ){ //ce je okence prazno
                    Toast.makeText(getActivity().getBaseContext(), "Please enter a new category.",Toast.LENGTH_LONG).show();
                }
                else { //dodamo podatke v bazo
                    myDB.addCategory(newCategory.getText().toString());
                    listener.AddToCategory();
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
