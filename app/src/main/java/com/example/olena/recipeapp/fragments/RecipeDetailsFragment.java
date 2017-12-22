package com.example.olena.recipeapp.fragments;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class RecipeDetailsFragment extends Fragment {

    public static final String ARGS_LIST = "args_list";

    private String transactionName;
    private TextView titleTxt;
    private TextView authorTxt;
    private ImageView imageView;
    private ImageButton goBackBtn;
    private RecyclerView recyclerViewIngr;
    private TwitterManager twitterManager;

    public static RecipeDetailsFragment newInstance(int position, RecipeListAdapter adapter) {
        RecipeDetailsFragment myFragment = new RecipeDetailsFragment();
        ArrayList<Recipe> list = new ArrayList<>();
        list.addAll(adapter.getListOfRecipes());
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelableArrayList(ARGS_LIST, list);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterManager = new TwitterManager(getContext());
        if ((getActivity()) != null) {
            ((MainNavActivity) getActivity()).setSearchGone();
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details, container, false);

        titleTxt = view.findViewById(R.id.titleTxt);
        authorTxt = view.findViewById(R.id.authorTxt);
        imageView = view.findViewById(R.id.imageView);
        goBackBtn = view.findViewById(R.id.goBackBtn);
        recyclerViewIngr = view.findViewById(R.id.rec_view_ingredients);
        Button shareFacebookBtn = view.findViewById(R.id.shareFacebookBtn);
        Button shareTwitterBtn = view.findViewById(R.id.shareTwitterBtn);
        final ArrayList<Recipe> listOfRecipes = getArguments().getParcelableArrayList("list");
        final int position = getArguments().getInt("position");
        shareFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookManager facebookManager = new FacebookManager(getActivity());
                facebookManager.postToFacebook(listOfRecipes.get(position).getImageUrl(),
                        listOfRecipes.get(position).getTitle(),
                        listOfRecipes.get(position).getListOfIngredientsString());

            }
        });
        shareTwitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterManager.postToTwitter(listOfRecipes.get(position).getImageUrl(),
                        listOfRecipes.get(position).getListOfIngredientsString());
            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        fillFields();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBackVisible();
            }
        });
        return view;
    }


    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    private void fillFields() {
        final ArrayList<Recipe> listOfRecipes = getArguments().getParcelableArrayList("list");
        final int position = getArguments().getInt("position");
        titleTxt.setText(listOfRecipes.get(position).getTitle());
        authorTxt.setText(listOfRecipes.get(position).getPublisher());
        imageView.setTransitionName(transactionName);
        Picasso.with(getContext())
                .load(listOfRecipes.get(position).getImageUrl())
                .into(imageView);
        recyclerViewIngr.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIngr.setAdapter(new IngredientListAdapter(listOfRecipes.get(position).getListOfIngredients()));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    private void getBackVisible() {
        if (goBackBtn.getVisibility() == View.GONE) {
            goBackBtn.setVisibility(View.VISIBLE);
        } else {
            goBackBtn.setVisibility(View.GONE);
        }
    }


}
