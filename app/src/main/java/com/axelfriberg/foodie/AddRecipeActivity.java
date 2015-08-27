package com.axelfriberg.foodie;

import android.app.Activity;
import android.app.AlertDialog;
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


public class AddRecipeActivity extends Activity {
    private Recipe recipe;
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;
    private FileUtilities fileUtilities;
    private ImageView mImageView;
    private File photoFile;
    private String mCurrentPhotoPath;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
    private static final String NO_IMAGE = "NoImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        setTitle("New Recipe");

        mTitleEditText = (EditText)findViewById(R.id.title_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_EditText);
        mImageView = (ImageView) findViewById(R.id.imageView_add);
        mCurrentPhotoPath = NO_IMAGE;

        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);

        photoFile = createImageFile(); //Create a file for potentially saving an image
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

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.done_button:
                save();
                return true;
            case android.R.id.home:
                backSaveDialog();
                return true;
            case R.id.camera_button:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        backSaveDialog();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the user took a picture using the intent, set it in the ImageView.
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
                setPic();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
        }
    }

    //Check if the user wants to save the current recipe that has been added on back click
    private void backSaveDialog(){
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

    //Writes the current recipe to the phones storage
    private void save(){
        String title = mTitleEditText.getText().toString();
        File[] files = this.getFilesDir().listFiles();
        if(!fileExists(files,title)){ //Check if the file name is already in use
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
        } else {
            Toast toast = Toast.makeText(this, "That name already exists", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Check if the file name that the user wants to save as already exists
    private boolean fileExists(File[] files, String s){
        for(File f : files){
            if (f.getName().compareTo(s) == 0){
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

    private File createImageFile() {
        // Create a unique image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir+"/"+imageFileName);
    }

    /*
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoFile.getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    } */


    //Scales the image and sets it in the ImageView
    private void setPic() {
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
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        //Set the image
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    //Used in case of rotation or something similar to set the picture again
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(photoFile.exists()&&hasFocus)
            setPic();
    }


}
