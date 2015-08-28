package com.axelfriberg.foodie;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

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
                showSaveDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        showSaveDialog();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current photo
        savedInstanceState.putString(STATE_PATH, mCurrentPhotoPath);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore photo path
        mCurrentPhotoPath = savedInstanceState.getString(STATE_PATH);
        photoFile = new File(mCurrentPhotoPath);
    }



    //Creates a dialog to make sure if the user wants to save the recipe, if text has been entered
    void showSaveDialog() {
        recipe.setTitle(mTitleEditText.getText().toString());
        recipe.setInstructions(mInstructionsEditText.getText().toString());
        //Check if the user has entered any text, otherwise just finish
        if (recipe.getTitle().length() > 0 || recipe.getInstructions().length() > 0) {
            DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                    R.string.alert_dialog_save_title);
            newFragment.show(getFragmentManager(), "dialog");
        }else {
            finish();
        }
    }

    public void doPositiveClick() {
        Log.i("FragmentAlertDialog", "Positive click!");
        save();
    }

    public void doNegativeClick() {
        Log.i("FragmentAlertDialog", "Negative click!");
        finish();
    }
}
