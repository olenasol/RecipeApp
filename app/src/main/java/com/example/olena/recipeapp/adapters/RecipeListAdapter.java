package com.example.olena.recipeapp.adapters;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.interfaces.RecipeItemClickListener;
import com.example.olena.recipeapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private List<Recipe> listOfRecipes;
    private final RecipeItemClickListener recipeItemClickListener;
    private Context context;

    public RecipeListAdapter(List<Recipe> listOfRecipes, RecipeItemClickListener recipeItemClickListener) {
        this.listOfRecipes = listOfRecipes;
        this.recipeItemClickListener = recipeItemClickListener;

    }

    public List<Recipe> getListOfRecipes() {
        return listOfRecipes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false);
            context = parent.getContext();
            vh = new RecipeHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeHolder) {
            ((RecipeHolder) holder).getTitleTxt().setText(listOfRecipes.get(position).getTitle());
            ((RecipeHolder) holder).getPublisherTxt().setText(listOfRecipes.get(position).getPublisher());
            Picasso.with(context).load(listOfRecipes.get(position).getImageUrl()).into(((RecipeHolder) holder).getImageView());
            if (listOfRecipes.get(position).getSocialRank() == 100) {
                String str = " Popular";
                ((RecipeHolder) holder).getTrandingTxt().setText(str);
                ((RecipeHolder) holder).getTrandingTxt().setVisibility(View.VISIBLE);

            }
            ViewCompat.setTransitionName(((RecipeHolder) holder).getImageView(), String.valueOf(position) + "_image");
            ((RecipeHolder) holder).getCardView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeItemClickListener.onRecipeItemClick(((RecipeHolder) holder), (holder).getAdapterPosition(), RecipeListAdapter.this);
                }
            });
        } else {
            ((ProgressViewHolder) holder).getProgressBar().setIndeterminate(true);
        }
    }

    public void addDataToListOfRecipes(List<Recipe> additionalList) {
        listOfRecipes.addAll(additionalList);
        notifyDataSetChanged();
    }

    public void addElementToListOfRecipes(Recipe recipe) {
        listOfRecipes.add(recipe);
    }

    public void removeLastFromListOfRecipes() {
        listOfRecipes.remove(listOfRecipes.size() - 1);
    }

    @Override
    public int getItemCount() {
        return listOfRecipes.size();
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return listOfRecipes.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


}
