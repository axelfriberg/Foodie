package com.axelfriberg.foodie;

import java.util.ArrayList;

/**
 * Created by Axel on 2015-07-21.
 * A class for representing a recipe.
 */
public class Recipe {
    private String name;
    private String instruction;
    private ArrayList<String>  ingredients;


    public Recipe(String name){
        this.name = name;
    }

    public void addIngredient(String ing){
        ingredients.add(ing);
    }
}
