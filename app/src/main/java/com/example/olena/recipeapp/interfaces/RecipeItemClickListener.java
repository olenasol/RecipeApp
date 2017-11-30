package com.example.olena.recipeapp.interfaces;

import android.widget.ImageView;

import com.example.olena.recipeapp.models.Recipe;

public interface RecipeItemClickListener  {
    void onRecipeItemClick(int position, Recipe recipeItem, ImageView imageView);
}
