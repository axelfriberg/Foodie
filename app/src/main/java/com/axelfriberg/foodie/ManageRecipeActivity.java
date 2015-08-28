package com.axelfriberg.foodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class ManageRecipeActivity extends Activity {
    protected Recipe recipe;
    protected EditText mTitleEditText;
    protected EditText mInstructionsEditText;
    protected ImageView mImageView;
    protected FileUtilities fileUtilities;
    protected File photoFile;
    protected String mCurrentPhotoPath;
    protected boolean edit;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
    protected static final int EDIT_RECIPE_REQUEST_CODE = 2;
    protected static final String NO_IMAGE = "NoImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_recipe);
        mTitleEditText = (EditText) findViewById(R.id.title_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_EditText);
        mImageView = (ImageView) findViewById(R.id.imageView_add);
        mImageView.setVisibility(View.INVISIBLE);
        photoFile = createImageFile(); //Create a file for potentially saving an image
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.done_button:
                save();
                return true;
            case R.id.camera_button:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ManageActivitiy_result", Integer.toString(requestCode));
        Log.d("ManageActivitiy_result", Integer.toString(resultCode));
        //If the user took a picture using the intent, set it in the ImageView.
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            setPic();
            mCurrentPhotoPath = photoFile.getAbsolutePath();
        } else if(requestCode == EDIT_RECIPE_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EditRecipeActivity.EXTRA_TITLE);
            String instructions = fileUtilities.readFromFile(title);
            String oldTitle = recipe.getTitle();
            if (title.equals(oldTitle)) {
                Log.d("ViewActivity_old", oldTitle);
                Log.d("ViewActivity_new", title);
                Log.d("ViewActivity", "Equals");
                recipe.setInstructions(instructions);
                mInstructionsEditText.setText(instructions);
            } else {
                Log.d("ViewActivity", "Not Equals");
                Log.d("ViewActivity_old", oldTitle);
                Log.d("ViewActivity_new", title);
                File file = new File(getFilesDir(), oldTitle);
                file.delete();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(title, sharedPref.getString(oldTitle, "NoImage"));
                editor.remove(oldTitle);
                editor.apply();
                recipe.setTitle(title);
                recipe.setInstructions(instructions);
                mTitleEditText.setText(title);
                mInstructionsEditText.setText(instructions);
                setTitle(title);
                fileUtilities.writeToFile(recipe);
            }
        }
    }

    //Writes the current recipe to the phones storage
    protected void save() {
        String title = mTitleEditText.getText().toString();
        title = title.trim(); //Check that the title is filled in, and not only consists of space
        if (title.length() > 0) {
            Log.d("Test_test",Boolean.toString(edit));
            if(fileExists(getFilesDir().listFiles(),title) && !edit){
                Toast toast = Toast.makeText(this, "That recipe name already exists", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                String instructions = mInstructionsEditText.getText().toString();
                recipe.setTitle(title);
                recipe.setInstructions(instructions);
                fileUtilities.writeToFile(recipe);
                //Save the file name of the picture in Shared Preferences so the file can be loaded later
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(title, mCurrentPhotoPath);
                editor.apply();
                finish();
            }
        } else {
            Toast toast = Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    //Check if the file name that the user wants to save as already exists
    private boolean fileExists(File[] files, String s) {
        for (File f : files) {
            if (f.getName().compareTo(s) == 0) {
                return true;
            }
        }
        return false;
    }

    //Used to launch the intent to take a picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,
                        CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }

    protected File createImageFile() {
        // Create a unique image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir + "/" + imageFileName);
    }


    //Scales the image and sets it in the ImageView
    protected void setPic() {
        mImageView.setVisibility(View.VISIBLE);

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        //Set the image
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    //Used in case of rotation or something similar to set the picture again
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("Mange_log_file", Boolean.toString(photoFile.exists()));
        Log.d("Mange_log_focus", Boolean.toString(hasFocus));
        Log.d("Mange_log_focus", photoFile.getName());
        if (photoFile.exists() && hasFocus) {
            setPic();
        } else {
            mImageView.setVisibility(View.INVISIBLE);
        }
    }

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

    void showAddDialog() {
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
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        save();
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
        finish();
    }
}
