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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddRecipeActivity extends Activity {
    private Recipe recipe;
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;
    private FileUtilities fileUtilities;
    private ImageView mImageView;
    private File photoFile;
    private String fileName;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);

        try {
            photoFile = createImageFile();
            fileName = photoFile.getName();
        } catch (IOException ex) {
            Log.e("Create", ex.getMessage());
        }

        mTitleEditText = (EditText)findViewById(R.id.title_EditText);
        mInstructionsEditText = (EditText) findViewById(R.id.instructions_EditText);
        mImageView = (ImageView) findViewById(R.id.imageView_add);

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
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            Log.d("AddRecipe", Integer.toString(resultCode));
            if (resultCode == RESULT_OK) {
                setPic();
                Log.d("AddRecipe", "ok");
            } else if (resultCode == RESULT_CANCELED) {
                // User did not want to take a picture
                Log.d("AddRecipe", "canceled");
            } else {
                // Something went wrong
                Log.d("AddRecipe", "error");
            }
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

    private void save(){
        String title = mTitleEditText.getText().toString();
        File[] files = this.getFilesDir().listFiles();
        if(!fileExists(files,title)){
            String instructions = mInstructionsEditText.getText().toString();
            recipe.setTitle(title);
            recipe.setInstructions(instructions);
            recipe.setPhotoFilePath(photoFile.getAbsolutePath());
            fileUtilities.writeToFile(recipe);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(title, photoFile.getAbsolutePath());
            Log.d("Add_camera_path", photoFile.getAbsolutePath());
            editor.commit();
            finish();
        } else {
            Toast toast = Toast.makeText(this, "That name already exists", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = AddRecipeActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

     /* Se till så att vi visar bilden även efter texrotation. Behöver veta
	 * storleken på ImageViewn
	 * Därför gjort detta i denna metod istället för onCreate tex då
	 * detta värde inte finns tillgängligt då
     */

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(photoFile.exists()&&hasFocus)
            setPic();
    }


}
