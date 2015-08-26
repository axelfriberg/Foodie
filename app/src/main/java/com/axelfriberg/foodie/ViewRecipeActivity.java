package com.axelfriberg.foodie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class ViewRecipeActivity extends Activity {
    private Recipe recipe;
    private TextView mViewRecipeTextView;
    private ImageView mImageView;
    private FileUtilities fileUtilities;
    private String filePath;
    private File photoFile;

    public final static String EXTRA_INSTRUCTIONS = "com.axelfriberg.foodie.INSTRUCTIONS";
    public final static String EXTRA_TITLE = "com.axelfriberg.foodie.TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        mViewRecipeTextView = (TextView) findViewById(R.id.view_recipe_TextView);
        mImageView = (ImageView) findViewById(R.id.image_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);

        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);
        String instructions = fileUtilities.readFromFile(title);
        recipe.setTitle(title);
        recipe.setInstructions(instructions);

        setTitle(recipe.getTitle());
        mViewRecipeTextView.setText(recipe.getInstructions());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getResources().getString(R.string.image_path_default);
        filePath = prefs.getString(title, defaultValue);
        photoFile = new File(filePath);
        Log.d("Pic_view", "hello"+filePath);
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

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(photoFile.exists()&&hasFocus) {
            setPic();
        } else {
            mImageView.setVisibility(View.GONE);
        }
    }
}
