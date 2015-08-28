package com.axelfriberg.foodie;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AddRecipeActivity extends ManageRecipeActivity {
    static final String STATE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Recipe");

        mImageView.setVisibility(View.INVISIBLE);
        mCurrentPhotoPath = NO_IMAGE;
        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);
        edit = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_PATH, mCurrentPhotoPath);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        mCurrentPhotoPath = savedInstanceState.getString(STATE_PATH);
    }
}
