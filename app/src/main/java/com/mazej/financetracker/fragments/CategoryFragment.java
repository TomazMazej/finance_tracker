package com.mazej.financetracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mazej.financetracker.MainActivity;
import com.mazej.financetracker.R;
import com.mazej.financetracker.db.DatabaseHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.mazej.financetracker.MainActivity.myMenu;
import static com.mazej.financetracker.MainActivity.toolbar;

public class CategoryFragment extends Fragment {

    private DatabaseHelper myDB;
    private Cursor data;
    public static ListView categoryList;
    public static ArrayList<String> theList;
    public static ArrayAdapter<String> arrayAdapter;
    public static ArrayAdapter<String> arrayAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        myDB = new DatabaseHelper(getActivity().getBaseContext());
        data = myDB.getCategories();
        toolbar.setBackgroundColor(Color.parseColor("#455a64"));
        toolbar.setTitle("Categories");

        categoryList = (ListView) view.findViewById(R.id.categoryList);

        theList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,theList);
        arrayAdapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_multiple_choice,theList){ //checkbox adapter
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = 300;
                view.setLayoutParams(params);
                return view;
            }
        };
        categoryList.setAdapter(arrayAdapter);

        while(data.moveToNext()) {
            theList.add(data.getString(1));
        }

        categoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //če drizmo na kategoriji jo lahko izbrisemo
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                categoryList.setAdapter(arrayAdapter2);
                myMenu.findItem(R.id.delete_category_btn).setVisible(true);
                return false;
            }
        });
        return view;
    }
}
