package com.example.olena.recipeapp.interfaces;

import com.example.olena.recipeapp.adapters.RecipeHolder;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;


public interface RecipeItemClickListener  {
    void onRecipeItemClick(RecipeHolder holder, int position, RecipeListAdapter adapter);
}
