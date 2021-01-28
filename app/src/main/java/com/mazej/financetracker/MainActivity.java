package com.mazej.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;

import com.google.android.material.navigation.NavigationView;
import com.mazej.financetracker.fragments.AccountsFragment;
import com.mazej.financetracker.fragments.AddAccountFragment;
import com.mazej.financetracker.fragments.AddCategoryFragment;
import com.mazej.financetracker.fragments.AddExpenseFragment;
import com.mazej.financetracker.fragments.AddIncomeFragment;
import com.mazej.financetracker.fragments.CategoryFragment;
import com.mazej.financetracker.fragments.ExpenseFragment;
import com.mazej.financetracker.fragments.IncomeFragment;
import com.mazej.financetracker.fragments.MainFragment;
import com.mazej.financetracker.db.DatabaseHelper;
import com.mazej.financetracker.objects.Income;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.mazej.financetracker.fragments.MainFragment.balance;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, onFragmentBtnSelected, OnFragmentInteractionListener{

    public static DatabaseHelper myDB;
    private Cursor data;
    public static Menu myMenu;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static int myID=100;

    public static ArrayList<Integer> deleteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);
        data = myDB.getCategories();
        myID = (int)(System.currentTimeMillis()%1000000);
        deleteList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //load default fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();

        //default categories
        String[] categories = {"Entertainment", "Food & Drinks", "Housing", "Transportation", "Vehicle", "Financial expenses", "Investments", "Salary", "Gifts"};

        //if its first time, we add categories in the database
        if(data.getCount() == 0){
            for(int i = 0; i < categories.length; i++){
                myDB.addCategory(categories[i]);
            }
        }

        //add cash and savings account if its first time
        //add functionality to edit account balance

        //createNotification(myID++, R.drawable.ic_account_balance_black_24dp, "Finance Tracker", "Merry Xmas and Happy new Year!", ApplicationMy.CHANNEL_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        myMenu = menu;
        hideButtons();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //handles toolbar buttons
        Cursor data;
        Income income;
        switch(item.getItemId()) {
            case R.id.delete_category_btn:
                SparseBooleanArray positionChecker = CategoryFragment.categoryList.getCheckedItemPositions();
                for (int i = CategoryFragment.categoryList.getCount() - 1; i >= 0; i--) {
                    if (positionChecker.get(i)) {
                        myDB.deleteCategory(CategoryFragment.theList.get(i));
                        CategoryFragment.theList.remove(i);
                    }
                }
                positionChecker.clear();
                CategoryFragment.arrayAdapter2.notifyDataSetChanged();
                CategoryFragment.categoryList.setAdapter(CategoryFragment.arrayAdapter);
                myMenu.findItem(R.id.delete_category_btn).setVisible(false);
                break;
            case R.id.delete_income_btn:
                Collections.sort(deleteList);
                for (int i = deleteList.size(); i > 0; i--) {
                    myDB.deleteIncome(IncomeFragment.theList.get(deleteList.get(i-1)).getId());
                    IncomeFragment.theList.remove(IncomeFragment.theList.get(deleteList.get(i-1)));
                }
                deleteList.clear();
                IncomeFragment.arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_expense_btn:
                Collections.sort(deleteList);
                for (int i = deleteList.size(); i > 0; i--) {
                    myDB.deleteExpense(ExpenseFragment.theList.get(deleteList.get(i-1)).getId());
                    ExpenseFragment.theList.remove(ExpenseFragment.theList.get(deleteList.get(i-1)));
                }
                deleteList.clear();
                ExpenseFragment.arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_account_btn:
                Collections.sort(deleteList);
                for (int i = deleteList.size(); i > 0; i--) {
                    myDB.deleteAccount(AccountsFragment.theList.get(deleteList.get(i-1)).getId());
                    AccountsFragment.theList.remove(AccountsFragment.theList.get(deleteList.get(i-1)));
                }
                deleteList.clear();
                AccountsFragment.arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.add_income_btn:
                IncomeToAdd();
                break;
            case R.id.add_account_btn:
                AccountToAdd();
                break;
            case R.id.add_expense_btn:
                ExpenseToAdd();
                break;
            case R.id.add_category_btn:
                CategoryToAdd();
                break;
            case R.id.today:
                data = myDB.getTodayIncome();
                IncomeFragment.theList.clear();
                while(data.moveToNext()) {
                    income = new Income(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                    IncomeFragment.theList.add(0, income);
                }
                IncomeFragment.arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.month:
                data = myDB.getMonthIncome();
                IncomeFragment.theList.clear();
                while(data.moveToNext()) {
                    income = new Income(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                    IncomeFragment.theList.add(0, income);
                }
                IncomeFragment.arrayAdapter.notifyDataSetChanged();
                break;
            case R.id.all:
                data = myDB.getIncome();
                IncomeFragment.theList.clear();
                while(data.moveToNext()) {
                    income = new Income(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4));
                    IncomeFragment.theList.add(0, income);
                }
                IncomeFragment.arrayAdapter.notifyDataSetChanged();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) { //handles side nav buttons
        hideButtons();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if(menuItem.getItemId() == R.id.home){
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
        }

        if(menuItem.getItemId() == R.id.income){
            myMenu.findItem(R.id.delete_income_btn).setVisible(true);
            myMenu.findItem(R.id.add_income_btn).setVisible(true);
            myMenu.findItem(R.id.date_range).setVisible(true);
            fragmentTransaction.replace(R.id.container_fragment, new IncomeFragment());
        }

        if(menuItem.getItemId() == R.id.expenses){
            myMenu.findItem(R.id.delete_expense_btn).setVisible(true);
            myMenu.findItem(R.id.add_expense_btn).setVisible(true);
            myMenu.findItem(R.id.date_range).setVisible(true);
            fragmentTransaction.replace(R.id.container_fragment, new ExpenseFragment());
        }

        if(menuItem.getItemId() == R.id.accounts){
            myMenu.findItem(R.id.delete_account_btn).setVisible(true);
            myMenu.findItem(R.id.add_account_btn).setVisible(true);
            fragmentTransaction.replace(R.id.container_fragment, new AccountsFragment());
        }

        if(menuItem.getItemId() == R.id.categories){
            myMenu.findItem(R.id.add_category_btn).setVisible(true);
            fragmentTransaction.replace(R.id.container_fragment, new CategoryFragment());
        }

        if(menuItem.getItemId() == R.id.settings){

        }
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void CategoryToAdd() {
        hideButtons();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new AddCategoryFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void AddToCategory() {
        hideButtons();
        AddCategoryFragment.newCategory.onEditorAction(EditorInfo.IME_ACTION_DONE);
        myMenu.findItem(R.id.add_category_btn).setVisible(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new CategoryFragment());
        fragmentTransaction.commit();
    }

    public void IncomeToAdd(){
        hideButtons();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new AddIncomeFragment());
        fragmentTransaction.commit();
    }

    public void AddToIncome(){
        hideButtons();
        AddIncomeFragment.amount.onEditorAction(EditorInfo.IME_ACTION_DONE);
        myMenu.findItem(R.id.add_income_btn).setVisible(true);
        myMenu.findItem(R.id.date_range).setVisible(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new IncomeFragment());
        fragmentTransaction.commit();
    }

    public void ExpenseToAdd(){
        hideButtons();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new AddExpenseFragment());
        fragmentTransaction.commit();
    }

    public void AddToExpense(){
        hideButtons();
        AddExpenseFragment.amount.onEditorAction(EditorInfo.IME_ACTION_DONE);
        myMenu.findItem(R.id.add_expense_btn).setVisible(true);
        myMenu.findItem(R.id.date_range).setVisible(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new ExpenseFragment());
        fragmentTransaction.commit();
    }

    public void AccountToAdd(){
        hideButtons();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new AddAccountFragment());
        fragmentTransaction.commit();
    }

    public void AddToAccount(){
        hideButtons();
        AddAccountFragment.amount.onEditorAction(EditorInfo.IME_ACTION_DONE);
        myMenu.findItem(R.id.add_account_btn).setVisible(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new AccountsFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void changeFragment(int id){ //when we want to get from main fragment to add expense/income
        if (id == 1) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new AddExpenseFragment());
            fragmentTransaction.commit();
        }
        else if (id == 2) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new AddIncomeFragment());
            fragmentTransaction.commit();
        }
    }

    public static void hideButtons(){ //hides all the toolbar buttons
        myMenu.findItem(R.id.general).setVisible(false);
        myMenu.findItem(R.id.other).setVisible(false);
        myMenu.findItem(R.id.delete_account_btn).setVisible(false);
        myMenu.findItem(R.id.delete_income_btn).setVisible(false);
        myMenu.findItem(R.id.delete_expense_btn).setVisible(false);
        myMenu.findItem(R.id.delete_category_btn).setVisible(false);
        myMenu.findItem(R.id.add_income_btn).setVisible(false);
        myMenu.findItem(R.id.add_expense_btn).setVisible(false);
        myMenu.findItem(R.id.add_category_btn).setVisible(false);
        myMenu.findItem(R.id.add_account_btn).setVisible(false);
        myMenu.findItem(R.id.date_range).setVisible(false);
    }

    private void createNotification(int nId, int iconRes, String title, String body, String channelId) { //creates a notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, channelId).setSmallIcon(iconRes)
                .setContentTitle(title).setContentText(body);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(nId, mBuilder.build());
    }
}
