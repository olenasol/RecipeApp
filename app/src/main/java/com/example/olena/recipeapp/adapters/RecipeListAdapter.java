package com.example.olena.recipeapp.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.interfaces.RecipeItemClickListener;
import com.example.olena.recipeapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public List<Recipe> listOfRecipes;
    private final RecipeItemClickListener recipeItemClickListener;
    private Context context;

    public RecipeListAdapter(List<Recipe> listOfRecipes,RecipeItemClickListener recipeItemClickListener) {
        this.listOfRecipes = listOfRecipes;
        this.recipeItemClickListener = recipeItemClickListener;

    }

    public List<Recipe> getListOfRecipes() {
        return listOfRecipes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType==VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false);
            context = parent.getContext();
           vh = new RecipeHolder(view);
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof RecipeHolder) {
            ((RecipeHolder) holder).titleTxt.setText(listOfRecipes.get(position).getTitle());
            ((RecipeHolder) holder).publisherTxt.setText(listOfRecipes.get(position).getPublisher());
            Picasso.with(context).load(listOfRecipes.get(position).getImageUrl()).into(((RecipeHolder) holder).imageView);
            if(listOfRecipes.get(position).getSocialRank() == 100){
                ((RecipeHolder)holder).trandingTxt.setText("  Popular");
                ((RecipeHolder)holder).trandingTxt.setVisibility(View.VISIBLE);

            }
            ViewCompat.setTransitionName(((RecipeHolder) holder).getImageView(), String.valueOf(position) + "_image");
            ((RecipeHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeItemClickListener.onRecipeItemClick(((RecipeHolder)holder),position,RecipeListAdapter.this);
                }
            });
        }
        else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

//    public void addFirstDataToListOfRecipes(List<Recipe> additionalList){
//        for(int i=0;i<additionalList.size();i++){
//            listOfRecipes.add(i,additionalList.get(i));
//        }
//        notifyDataSetChanged();
//    }
    public void addDataToListOfRecipes(List<Recipe> additionalList){
        for(Recipe recipe:additionalList){
            listOfRecipes.add(recipe);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfRecipes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listOfRecipes.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

}
