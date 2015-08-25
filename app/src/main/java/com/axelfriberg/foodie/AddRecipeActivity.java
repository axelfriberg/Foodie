package com.axelfriberg.foodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class AddRecipeActivity extends Activity {
    private Recipe recipe;
    private EditText mTitleEditText;
    private EditText mInstructionsEditText;
    private FileUtilities fileUtilities;
    private File f;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipe = new Recipe();
        fileUtilities = new FileUtilities(this);
        f = new File(getFilesDir()+"/"+"mypic.jpg");

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
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                AddRecipeActivity.this.startActivityForResult(i, CAPTURE_IMAGE_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        backSaveDialog();
    }

    /* Se till så att vi visar bilden även efter texrotation. Behöver veta
	 * storleken på ImageViewn
	 * Därför gjort detta i denna metod istället för onCreate tex då
	 * detta värde inte finns tillgängligt då
	 */
    //
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(f.exists()&&hasFocus)
            updateImageViewFromFile();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            Log.d("AddRecipe", Integer.toString(resultCode));
            if (resultCode == RESULT_OK) {
                updateImageViewFromFile();
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

    private void updateImageViewFromFile() {
        ImageView imView=(ImageView) findViewById(R.id.imageView_add);

        //Läs in bilden som nu bör finnas där vi sa att den skulle placeras
        Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
        Log.d("AddRecipe", f.getAbsolutePath());

        //Skala om bilden så att den passar i imageviewn
        Bitmap bm2 = Bitmap.createScaledBitmap(bm,
                imView.getWidth(),
                imView.getHeight(),
                true);
        imView.setImageBitmap(bm2);
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
            fileUtilities.writeToFile(recipe);
            finish();
        } else {
            Toast toast = Toast.makeText(this, "That name already exists", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

    }


}
