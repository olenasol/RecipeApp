package com.example.olena.recipeapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.models.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends Fragment {

    private Recipe recipe;
    private String transitionalName;

    private TextView titleTxt;
    private TextView authorTxt;
    private TextView link;
    private ImageView imageView;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        ((MainNavActivity)getActivity()).setSearchGone();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_item, container, false);
        titleTxt = view.findViewById(R.id.titleTxt);
        authorTxt =  view.findViewById(R.id.authorTxt);
        link = view.findViewById(R.id.sourceLink);
        imageView = view.findViewById(R.id.imageView);
        fillFields();
        return view;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getTransitionalName() {
        return transitionalName;
    }

    public void setTransitionalName(String transitionalName) {
        this.transitionalName = transitionalName;
    }
    private void fillFields(){
        titleTxt.setText(recipe.getTitle());
        authorTxt.setText(recipe.getPublisher());
        link.setText(recipe.getSourceUrl());
        imageView.setTransitionName(transitionalName);
        Picasso.with(getContext())
                .load(recipe.getImageUrl())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });
    }

}
