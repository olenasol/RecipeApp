package com.example.olena.recipeapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeBundle {
    @SerializedName("recipes")
    private ArrayList<Recipe> listOfRecipes;

    public RecipeBundle(ArrayList<Recipe> listOfRecipes) {
        this.listOfRecipes = listOfRecipes;
    }

    public ArrayList<Recipe> getListOfRecipes() {
        return listOfRecipes;
    }

    public void setListOfRecipes(ArrayList<Recipe> listOfRecipes) {
        this.listOfRecipes = listOfRecipes;
    }
}
