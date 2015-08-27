package com.axelfriberg.foodie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


public class ViewRecipeActivity extends ManageRecipeActivity {
    public final static String EXTRA_INSTRUCTIONS = "com.axelfriberg.foodie.INSTRUCTIONS";
    public final static String EXTRA_TITLE = "com.axelfriberg.foodie.TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileUtilities = new FileUtilities(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String instructions = fileUtilities.readFromFile(title);
        recipe = new Recipe(title,instructions);

        setTitle(title);
        mTitleEditText.setEnabled(false);
        mTitleEditText.setFocusable(false);
        mTitleEditText.setText(title);
        mInstructionsEditText.setEnabled(false);
        mInstructionsEditText.setFocusable(false);
        mInstructionsEditText.setText(instructions);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.image_path_default);
        mCurrentPhotoPath = prefs.getString(title, defaultValue);
        Log.d("Mange_log_view",mCurrentPhotoPath);

        photoFile = new File(mCurrentPhotoPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.edit_button:
                Intent intent = new Intent(this, EditRecipeActivity.class);
                intent.putExtra(EXTRA_TITLE, recipe.getTitle());
                intent.putExtra(EXTRA_INSTRUCTIONS, recipe.getInstructions());
                startActivityForResult(intent,EDIT_RECIPE_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
