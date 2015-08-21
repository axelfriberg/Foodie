package com.axelfriberg.foodie;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class RecipeListFragment extends ListFragment {
    private ArrayList<RecipeListItem> mItems;        // ListView items list
    private File[] files;
    private FileUtilities fu;
    private ArrayAdapter<RecipeListItem>  adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
        Resources resources = getResources();
        files = getActivity().getFilesDir().listFiles();
        fu = new FileUtilities(getActivity());

        for (int i = 0; i < files.length; i++) {
            mItems.add(new RecipeListItem(files[i].getName()));
        }

        // initialize and set the list adapter
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mItems);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        RecipeListItem item = mItems.get(position);

        Intent intent = new Intent(getActivity(), ViewRecipeActivity.class);
        intent.putExtra(ViewRecipeActivity.EXTRA_TITLE, item.title);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        File[] tempFiles = getActivity().getFilesDir().listFiles();
        if(tempFiles.length > files.length){ // Recipe has been added
            adapter.add(new RecipeListItem(tempFiles[tempFiles.length-1].getName()));
            files = tempFiles;
            adapter.notifyDataSetChanged();
        }
    }
}


