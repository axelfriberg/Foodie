package com.axelfriberg.foodie;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


public class AddRecipeActivity extends ManageRecipeActivity {

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
                backSaveDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        backSaveDialog();
    }
}
