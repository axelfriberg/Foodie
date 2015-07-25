package com.axelfriberg.foodie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class ViewRecipeActivity extends AppCompatActivity {
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        recipe = readFromFile("test");
        setTitle(recipe.getTitle());
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

        return super.onOptionsItemSelected(item);
    }

    private Recipe readFromFile(String fileName) {
        Recipe r = new Recipe();
        try {
            FileInputStream fis = openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            r = (Recipe) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return r;
    }
}
