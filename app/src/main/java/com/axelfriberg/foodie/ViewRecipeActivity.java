package com.axelfriberg.foodie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;


public class ViewRecipeActivity extends Activity {
    private Recipe recipe;
    private TextView mViewRecipeTextView;
    private FileUtilities fileUtilities;
    public final static String EXTRA_INSTRUCTIONS = "com.axelfriberg.foodie.INSTRUCTIONS";
    public final static String EXTRA_TITLE = "com.axelfriberg.foodie.TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        mViewRecipeTextView = (TextView) findViewById(R.id.view_recipe_TextView);
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);

        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);
        String instructions = fileUtilities.readFromFile(title);
        recipe.setTitle(title);
        recipe.setInstructions(instructions);

        setTitle(recipe.getTitle());
        mViewRecipeTextView.setText(recipe.getInstructions());
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
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.edit_button){
            Intent intent = new Intent(this, EditRecipeActivity.class);
            intent.putExtra(EXTRA_TITLE,recipe.getTitle());
            intent.putExtra(EXTRA_INSTRUCTIONS,recipe.getInstructions());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
