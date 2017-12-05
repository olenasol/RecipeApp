package com.example.olena.recipeapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;
import com.example.olena.recipeapp.models.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends Fragment {

    private static RecipeListAdapter adapter;
    private static  int position;
    private String transactionName;

    private TextView titleTxt;
    private TextView authorTxt;
    private ImageView imageView;

    public static RecipeDetailsFragment newInstance(int position,RecipeListAdapter adapter) {
        RecipeDetailsFragment.position = position;
        RecipeDetailsFragment.adapter = adapter;
        return new RecipeDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainNavActivity)getActivity()).setSearchGone();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details, container, false);
        titleTxt = view.findViewById(R.id.titleTxt);
        authorTxt =  view.findViewById(R.id.authorTxt);
        imageView = view.findViewById(R.id.imageView);
        fillFields();
        return view;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    private void fillFields(){
        titleTxt.setText(adapter.getListOfRecipes().get(position).getTitle());
        authorTxt.setText(adapter.getListOfRecipes().get(position).getPublisher());
        imageView.setTransitionName(transactionName);
        Picasso.with(getContext())
                .load(adapter.getListOfRecipes().get(position).getImageUrl())
                .into(imageView);
    }

}
