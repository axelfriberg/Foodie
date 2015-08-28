package com.axelfriberg.foodie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import java.io.File;

public class EditRecipeActivity extends ManageRecipeActivity {
    public final static String EXTRA_TITLE = "com.axelfriberg.foodie.EDIT.TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra(ViewRecipeActivity.EXTRA_TITLE);
        String instructions = intent.getStringExtra(ViewRecipeActivity.EXTRA_INSTRUCTIONS);

        mTitleEditText.setText(title);
        mInstructionsEditText.setText(instructions);
        setTitle("Edit " + title);

        recipe = new Recipe(title, instructions);
        fileUtilities = new FileUtilities(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.image_path_default);
        mCurrentPhotoPath = prefs.getString(title, defaultValue);

        photoFile = new File(mCurrentPhotoPath);
        edit = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TITLE, mTitleEditText.getText().toString());
                setResult(RESULT_OK, intent);
                save();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, mTitleEditText.getText().toString());
        setResult(RESULT_OK, intent);
        showDialog();
    }
}
