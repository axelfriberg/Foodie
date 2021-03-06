package com.axelfriberg.foodie;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Axel on 2015-07-26.
 *
 * Used to handle files.
 */
public class FileUtilities {
    private Context context;


    public FileUtilities(Context context){
        this.context = context;
    }

    /**
     * Writes a recipe object to the phones memory
     * @param r The recipe that should be saved
     */
    public void writeToFile(Recipe r) {
        String title = r.getTitle();
        String instructions = r.getInstructions();
        try {
            FileOutputStream fos = context.openFileOutput(title, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(instructions);
            osw.close();
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Reads a specified file from the memory
     * @param fileName The name of the file that should be read
     * @return The instructions from the file
     */

    public String readFromFile(String fileName) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                StringBuilder sb = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    sb.append(receiveString).append("\n");
                }
                if(sb.length() >= 1){
                    sb.setLength(sb.length() - 1); //Remove the last added linebreak
                }

                inputStream.close();
                ret = sb.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    /**
     * Deletes a specified file from the memory
     * @param fileName
     * @return If the deletion was successful or not
     */
    public boolean delete(String fileName){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        String imagePath = sharedPref.getString(fileName,AddRecipeActivity.NO_IMAGE);
        if(!imagePath.equals(AddRecipeActivity.NO_IMAGE)){
            File imageFile = new File(imagePath);
            if(imageFile.delete()){
                Log.d("FileUtilities", "Image delete successful");
            } else {
                Log.d("FileUtilities", "Image delete unsuccessful");
            }
        }
        File file = new File(context.getFilesDir(), fileName);
        editor.remove(fileName);
        editor.apply();
        return file.delete();
    }
}
