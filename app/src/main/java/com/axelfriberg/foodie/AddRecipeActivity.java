package com.axelfriberg.foodie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class AddRecipeActivity extends AppCompatActivity {
    private Recipe recipe;
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipe = new Recipe();

        mTitleEditText = (EditText)findViewById(R.id.title_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_EditText);

        Intent intent = getIntent();
        boolean newNote = intent.getBooleanExtra(MainActivity.EXTRA_BOOLEAN, false);
        if(newNote){
            setTitle("New Note");
        } else {
            mTitleEditText.setText("Test");
        }
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
            recipe.setTitle(title);
            writeToFile(title,recipe);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeToFile(String fileName, Recipe r) {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(r);
            oos.close();
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
