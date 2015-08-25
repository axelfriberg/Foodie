package com.axelfriberg.foodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class AddRecipeActivity extends Activity {
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
            save();
            return true;
        }

        if(id == android.R.id.home){
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to save the current recipe?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            save();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backCheck();
    }

    private void save(){
        String title = mTitleEditText.getText().toString();
        File[] files = this.getFilesDir().listFiles();
        if(!fileExists(files,title)){
            String instructions = mInstructionsEditText.getText().toString();
            recipe.setTitle(title);
            recipe.setInstructions(instructions);
            fileUtilities.writeToFile(recipe);
            finish();
        } else {
            Toast toast = Toast.makeText(this, "That name already exists", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

    }

    private boolean fileExists(File[] files, String s){
        for(File f : files){
            if (f.getName().compareTo(s) == 0){
                return true;
            }
        }
        return false;
    }

    private void backCheck(){
        new AlertDialog.Builder(this)
                .setMessage("Do you want to save the current recipe?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }


}
