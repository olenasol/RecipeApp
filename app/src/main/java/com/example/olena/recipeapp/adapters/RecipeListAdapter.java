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



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType==VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false);
            context = parent.getContext();
           vh = new RecipeHolder(view, parent.getContext());
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
            ((RecipeHolder) holder).sourceLinkTxt.setText(listOfRecipes.get(position).getSourceUrl());
            Picasso.with(context).load(listOfRecipes.get(position).getImageUrl()).into(((RecipeHolder) holder).imageView);
            ViewCompat.setTransitionName(((RecipeHolder) holder).imageView, "image"+position);
            ((RecipeHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeItemClickListener.onRecipeItemClick(position,listOfRecipes.get(position),((RecipeHolder)holder).imageView);
                }
            });
        }
        else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

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

    class RecipeHolder extends RecyclerView.ViewHolder {

        TextView titleTxt;
        TextView publisherTxt;
        TextView sourceLinkTxt;
        ImageView imageView;
        CardView cardView;

        RecipeHolder(View itemView, final Context context) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            publisherTxt = itemView.findViewById(R.id.authorTxt);
            sourceLinkTxt = itemView.findViewById(R.id.sourceLink);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

}
