package com.example.olena.recipeapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.olena.recipeapp.R;

class IngredientHolder extends RecyclerView.ViewHolder {

    private TextView ingredientTxt;

    TextView getIngredientTxt() {
        return ingredientTxt;
    }


    IngredientHolder(View itemView) {
        super(itemView);
        ingredientTxt = itemView.findViewById(R.id.ingredientTxt);

    }
}