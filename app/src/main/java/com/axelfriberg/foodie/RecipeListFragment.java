package com.axelfriberg.foodie;

import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class RecipeListFragment extends ListFragment {
    private ArrayList<RecipeListItem> mItems;        // ListView items list

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
        Resources resources = getResources();

        mItems.add(new RecipeListItem(getString(R.string.test_title)));

        // initialize and set the list adapter
        ArrayAdapter<RecipeListItem> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mItems);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        RecipeListItem item = mItems.get(position);

        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }
}


