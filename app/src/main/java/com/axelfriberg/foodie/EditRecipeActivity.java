package com.axelfriberg.foodie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class EditRecipeActivity extends Activity {
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;
    private FileUtilities fileUtilities;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        mTitleEditText = (EditText) findViewById(R.id.title_edit_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_edit_EditText);

        Intent intent = getIntent();
        String title = intent.getStringExtra(ViewRecipeActivity.EXTRA_TITLE);
        String instructions = intent.getStringExtra(ViewRecipeActivity.EXTRA_INSTRUCTIONS);

        mTitleEditText.setText(title);
        mInstructionsEditText.setText(instructions);
        setTitle("Edit " + title);

        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_recipe, menu);
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

        if (id == R.id.done_edit_button){
            recipe.setTitle(mTitleEditText.getText().toString());
            recipe.setInstructions(mInstructionsEditText.getText().toString());
            fileUtilities.writeToFile(recipe);
            Intent intent = new Intent(this,ViewRecipeActivity.class);
            intent.putExtra(ViewRecipeActivity.EXTRA_TITLE,recipe.getTitle());
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
