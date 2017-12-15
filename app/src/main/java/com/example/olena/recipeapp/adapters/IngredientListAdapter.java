package com.example.olena.recipeapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.olena.recipeapp.R;

import java.util.ArrayList;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientHolder> {

    private ArrayList<String> listOfIngredients;

    public IngredientListAdapter(ArrayList<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder( LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        holder.getIngredientTxt().setText(listOfIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfIngredients.size();
    }

}
