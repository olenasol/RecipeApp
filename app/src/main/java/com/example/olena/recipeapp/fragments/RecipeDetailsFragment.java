package com.example.olena.recipeapp.fragments;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.adapters.IngredientListAdapter;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;

import com.example.olena.recipeapp.models.Recipe;
import com.example.olena.recipeapp.socialsitesmanagers.FacebookManager;
import com.example.olena.recipeapp.socialsitesmanagers.TwitterManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecipeDetailsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static RecipeListAdapter adapter;
    private static  int position;
    private String transactionName;
    private TextView titleTxt;
    private TextView authorTxt;
    private ImageView imageView;
    private ImageButton goBackBtn;
    private RecyclerView recyclerViewIngr;
    private TwitterManager twitterManager;

    public static RecipeDetailsFragment newInstance(int position,RecipeListAdapter adapter) {

        RecipeDetailsFragment.position = position;
        RecipeDetailsFragment.adapter = adapter;
        return new RecipeDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterManager = new TwitterManager(getContext());
        if ((getActivity()) != null) {
            ((MainNavActivity)getActivity()).setSearchGone();
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details, container, false);
        titleTxt = view.findViewById(R.id.titleTxt);
        authorTxt =  view.findViewById(R.id.authorTxt);
        imageView = view.findViewById(R.id.imageView);
        goBackBtn = view.findViewById(R.id.goBackBtn);
        recyclerViewIngr = view.findViewById(R.id.rec_view_ingredients);
        Button shareFacebookBtn = view.findViewById(R.id.shareFacebookBtn);
        Button shareTwitterBtn = view.findViewById(R.id.shareTwitterBtn);
        shareFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookManager facebookManager = new FacebookManager(getActivity());
                facebookManager.postToFacebook(adapter.getListOfRecipes().get(position).getImageUrl(),
                        adapter.getListOfRecipes().get(position).getTitle(),
                        adapter.getListOfRecipes().get(position).getListOfIngredientsString());

            }
        });
        shareTwitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterManager.postToTwitter(adapter.getListOfRecipes().get(position).getImageUrl(),
                        adapter.getListOfRecipes().get(position).getListOfIngredientsString());
            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
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
        recyclerViewIngr.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIngr.setAdapter(new IngredientListAdapter(adapter.getListOfRecipes().get(position).getListOfIngredients()));

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else{
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }



}
