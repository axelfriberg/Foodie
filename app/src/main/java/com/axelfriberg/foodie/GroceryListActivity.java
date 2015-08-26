package com.axelfriberg.foodie;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class GroceryListActivity extends ListActivity {
    private ArrayAdapter adapter;
    private FileUtilities fu;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        setTitle("Grocery List");

        // initialize the items list
        fu = new FileUtilities(this);

        // initialize and set the list adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, fu.readFromGroceryList());
        setListAdapter(adapter);

        mListView = getListView();
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                mode.setTitle(mListView.getCheckedItemCount() + " selected items");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.done_button:
                        //deleteSelectedItems();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_cab_grocery, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_grocery_button){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a list item");
                builder.setMessage("What do you want to add?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputString = inputField.getText().toString();
                        ArrayList<String> old = fu.readFromGroceryList();
                        old.add(inputString);
                        adapter.add(inputString);
                        StringBuilder sb = new StringBuilder();
                        for(String s : old){
                            sb.append(s).append("\n");
                        }
                        fu.writeToGroceryList(sb.toString());
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel",null);
                builder.create().show();
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
        ArrayList<String> list = fu.readFromGroceryList();
        list.remove(s);
        StringBuilder sb = new StringBuilder();
        for(String string : list){
            sb.append(string).append("\n");
        }
        fu.writeToGroceryList(sb.toString());
        adapter.notifyDataSetChanged();
    }
}
