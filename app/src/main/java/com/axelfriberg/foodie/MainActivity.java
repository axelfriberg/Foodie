package com.axelfriberg.foodie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ListActivity {
    private File[] files;
    private FileUtilities fu;
    private ArrayAdapter<String> adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the items list
        ArrayList<String> items = new ArrayList<>();
        files = this.getFilesDir().listFiles();
        fu = new FileUtilities(this);

        for(File file : files){
            items.add(file.getName());
        }

        // initialize and set the list adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items);
        setListAdapter(adapter);

        //Makes the list multi choice by use of Context Action Bar
        mListView = getListView();
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //What happens when one or more items are selected by long tap
                mode.setTitle(mListView.getCheckedItemCount() + " selected items");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.delete_button:
                        deleteSelectedItems();
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
                inflater.inflate(R.menu.menu_cab_main, menu);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.add_button:
                Intent addIntent = new Intent(this, AddRecipeActivity.class);
                startActivity(addIntent);
                return true;
            case R.id.grocery_button:
                Intent groceryIntent = new Intent(this, GroceryListActivity.class);
                startActivity(groceryIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Decides what happens when an item in the list is clicked
        //In this case starts an activity to view the clicked recipe
        String item = (String) l.getItemAtPosition(position);
        // retrieve theListView
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra(ViewRecipeActivity.EXTRA_TITLE, item);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //When returning to this activity check if the user has added any new recipes,
        // in that case add them to the list
        files = this.getFilesDir().listFiles();
        if(adapter.getCount() < files.length){
            adapter.add(files[files.length - 1].getName());
        }
        adapter.notifyDataSetChanged();
    }

    //Deletes the user selected items in the ListView
    private void deleteSelectedItems(){
        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) { //Add the selected items to a list
            if (checked.valueAt(i)){
                String selected = (String) mListView.getItemAtPosition(checked.keyAt(i));
                selectedItems.add(selected);
            }
        }
        for(String s : selectedItems){
            fu.delete(s); //Delete the recipe from the phones memory
            adapter.remove(s); //Remove it from the ListView
        }
        adapter.notifyDataSetChanged();
    }
}
