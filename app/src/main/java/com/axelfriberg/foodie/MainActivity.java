package com.axelfriberg.foodie;

import android.app.ListActivity;
import android.content.Intent;
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
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ListActivity {
    private File[] files;
    private FileUtilities fu;
    private ArrayAdapter<RecipeListItem> adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the items list
        ArrayList<RecipeListItem> items = new ArrayList<>();
        files = this.getFilesDir().listFiles();
        fu = new FileUtilities(this);

        for(File file : files){
            items.add(new RecipeListItem(file.getName()));
        }

        // initialize and set the list adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_button){
            Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        RecipeListItem item = (RecipeListItem) l.getItemAtPosition(position);
        // retrieve theListView
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        intent.putExtra(ViewRecipeActivity.EXTRA_TITLE, item.title);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //Load the list of items and add, in case the user has added a new one
        files = this.getFilesDir().listFiles();
        if(adapter.getCount() < files.length){
            adapter.add(new RecipeListItem(files[files.length - 1].getName()));
        }
        adapter.notifyDataSetChanged();

    }

    //Deletes the user selected items in the ListView in this fragment
    private void deleteSelectedItems(){
        adapter.notifyDataSetChanged();
        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        Log.d("Checked", Integer.toString(checked.size()));
        Log.d("Adapter count", Integer.toString(adapter.getCount()));
        ArrayList<RecipeListItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)){
                Log.d("Key at", Integer.toString(checked.keyAt(i)));
                RecipeListItem selected = (RecipeListItem) mListView.getItemAtPosition(checked.keyAt(i));
                selectedItems.add(selected);
            }
        }
        for(RecipeListItem rli : selectedItems){
            fu.delete(rli.toString());
            adapter.remove(rli);
        }
        adapter.notifyDataSetChanged();
    }
}
