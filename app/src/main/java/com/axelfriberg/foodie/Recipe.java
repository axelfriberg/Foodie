package com.axelfriberg.foodie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Axel on 2015-07-21.
 * A class for representing a recipe.
 */
public class Recipe implements Parcelable {
    private String title;
    private String instructions;

    public Recipe(){

    }

    public Recipe(String title){
        this.title = title;
    }

    public Recipe(String title, String instructions){
        this.title = title;
        this.instructions = instructions;
    }

    public void setTitle(String t){
        title = t;
    }

    public void setInstructions(String i){
        instructions = i;
    }

    public String getTitle(){
        return title;
    }

    public String getInstructions(){
        return instructions;
    }

    @Override
    public String toString(){
        return title;
    }

    //Parcel code
    protected Recipe(Parcel in){
        title = in.readString();
        instructions = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(instructions);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
