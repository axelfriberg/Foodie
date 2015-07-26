package com.axelfriberg.foodie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;


public class AddRecipeActivity extends AppCompatActivity {
    private Recipe recipe;
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;
    private FileUtilities fileUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);

        mTitleEditText = (EditText)findViewById(R.id.title_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_EditText);

        setTitle("New Recipe");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_recipe, menu);
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

        if (id == R.id.done_button){
            String title = mTitleEditText.getText().toString();
            String instructions = mInstructionsEditText.getText().toString();
            recipe.setTitle(title);
            recipe.setInstructions(instructions);
            fileUtilities.writeToFile(recipe);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
