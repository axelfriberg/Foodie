package com.axelfriberg.foodie;

import android.graphics.drawable.Drawable;

public class RecipeListItem {
    public final String title;        // the text for the ListView item title


    public RecipeListItem(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return title;
    }
}
