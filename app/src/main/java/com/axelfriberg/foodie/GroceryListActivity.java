package com.axelfriberg.foodie;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class GroceryListActivity extends ListActivity {
    private ArrayAdapter adapter;
    private ListView mListView;
    private final String GROCERY_LIST = "com.axelfriberg.foodie.GROCERY.LIST";
    private final String NO_ITEMS= "com.axelfriberg.foodie.NO.ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        setTitle("Grocery List");

        // initialize the items list

        // initialize and set the list adapter
        ArrayList<String> list = readFromGroceryList();
        if(list.contains(NO_ITEMS)){
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
            setListAdapter(adapter);
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readFromGroceryList());
            setListAdapter(adapter);
        }


        mListView = getListView();
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grocery_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_grocery_button){
                showAddDialog();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) l.getItemAtPosition(position);
        deleteItem(item);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    private void deleteItem(String s){
        adapter.remove(s);
        ArrayList<String> list = readFromGroceryList();
        list.remove(s);
        StringBuilder sb = new StringBuilder();
        for(String string : list){
            sb.append(string).append(" ");
        }
        writeToGroceryList(sb.toString());
        adapter.notifyDataSetChanged();
    }

    private void writeToGroceryList(String s) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GROCERY_LIST, s);
        editor.apply();
    }

    private ArrayList<String> readFromGroceryList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String items = sharedPref.getString(GROCERY_LIST, NO_ITEMS);
        String[] split = items.split("\\s+");
        return new ArrayList<>(Arrays.asList(split));
    }

    void showAddDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                R.string.alert_dialog_grocery_title);
        newFragment.show(getFragmentManager(), "dialog");

    }

    public void doPositiveClick(String newItem) {
        Log.i("FragmentAlertDialog", "Positive click!");
        newItem = newItem.trim();
        if(newItem.length() >= 1) {
            ArrayList<String> old = readFromGroceryList();
            old.add(newItem);
            adapter.add(newItem);
            StringBuilder sb = new StringBuilder();
            for (String s : old) {
                sb.append(s).append(" ");
            }
            writeToGroceryList(sb.toString());
            adapter.notifyDataSetChanged();
        } else {
            Log.i("FragmentAlertDialog", "Empty title");
        }
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");

    }
}
