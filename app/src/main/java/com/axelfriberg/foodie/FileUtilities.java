package com.axelfriberg.foodie;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

    public String readFromFile(String fileName) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }

        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    public boolean delete(String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return file.delete();
    }
}
