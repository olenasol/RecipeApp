package com.example.olena.recipeapp.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.olena.recipeapp.activities.AddRecipeActivity;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.adapters.RecipeHolder;
import com.example.olena.recipeapp.classes.EndlessRecyclerOnScrollListener;
import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;
import com.example.olena.recipeapp.client.ApiClient;
import com.example.olena.recipeapp.client.RecipeClient;
import com.example.olena.recipeapp.interfaces.RecipeItemClickListener;
import com.example.olena.recipeapp.models.Recipe;
import com.example.olena.recipeapp.models.RecipeBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerViewFragment extends Fragment implements RecipeItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Retrofit retrofit;
    private RecipeClient client;
    private Handler handler;
    private RecipeListAdapter recipeListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button goToTopBtn;
    private static int yPosition;
    private boolean isSearch;
    private String searchString;
    private FloatingActionButton fabPlus;
    private  ArrayList<Recipe> listOfRecipes;

    public RecyclerViewFragment() {
        listOfRecipes = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        handler = new Handler();
        recyclerView = view.findViewById(R.id.recyclerView);
        fabPlus = view.findViewById(R.id.fab);
        goToTopBtn = view.findViewById(R.id.toTopBtn);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.canScrollVertically();
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddRecipeActivity.class);
                startActivity(intent);
            }
        });
        goToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager.scrollToPosition(0);
                yPosition = 0;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(layoutManager);
        retrofit = ApiClient.getClient(getContext());
        client = retrofit.create(RecipeClient.class);
        loadFirstSetOfElements();

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            int currentScrollPosition = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                yPosition = dy;
                currentScrollPosition += dy;

                hideTopButton(currentScrollPosition);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //if(recyclerView.SCROLL_STATE_IDLE==newState){
                    hideShowEditText();

                //}

            }


            @Override
            public void onLoadMore(final int currentPage) {
                loadRestElements(currentPage);
            }
        });

    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstSetOfElements();
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public void onRecipeItemClick(RecipeHolder holder, int position, RecipeListAdapter adapter) {

        RecipeDetailsFragment recipeDetails = RecipeDetailsFragment.newInstance(position,adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recipeDetails.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            recipeDetails.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        recipeDetails.setTransactionName(ViewCompat.getTransitionName(holder.getImageView()));

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.getImageView(), ViewCompat.getTransitionName(holder.getImageView()))
                .replace(R.id.fragment_container,recipeDetails)
                .addToBackStack(null)
                .commit();


    }
    private void hideTopButton(int x){
        if(yPosition<0){
            if(x>=10000) {
                goToTopBtn.setVisibility(View.VISIBLE);
            }
            else {
                goToTopBtn.setVisibility(View.GONE);
            }

        }
        else{
            goToTopBtn.setVisibility(View.GONE);
        }
    }
    private void hideShowEditText(){
        if(yPosition<=0){
            ((MainNavActivity)getActivity()).setSearchVisible();

        }
        else{
            ((MainNavActivity)getActivity()).setSearchGone();
        }
    }
    private void loadFirstSetOfElements(){
        if(listOfRecipes==null) {
            Call<RecipeBundle> ncall = null;
            if (RecyclerViewFragment.this.isSearch()) {
                ncall = client.getRecipe(RecipeClient.key, getSearchString(), new Integer(1).toString());
            } else {
                ncall = client.getRecipe(RecipeClient.key, new Integer(1).toString());

            }
            ncall.enqueue(new Callback<RecipeBundle>() {
                @Override
                public void onResponse(Call<RecipeBundle> call, Response<RecipeBundle> response) {
                    RecipeBundle recipeBundle = response.body();
                    if (recipeBundle.getListOfRecipes().size() == 0) {
                        getFragmentManager()
                                .beginTransaction()
                                .hide(RecyclerViewFragment.this)
                                .add(R.id.fragment_container, new NoResultsFragment())
                                .commit();
                    }
                    listOfRecipes = recipeBundle.getListOfRecipes();
                    addIngredientsToRecipes(listOfRecipes);

                    for(int i=0;i<MainNavActivity.listOfNewRecipies.size();i++){
                        listOfRecipes.add(i,MainNavActivity.listOfNewRecipies.get(i));
                    }

                    recipeListAdapter = new RecipeListAdapter(listOfRecipes, RecyclerViewFragment.this);
                    recyclerView.setAdapter(recipeListAdapter);

                }

                @Override
                public void onFailure(Call<RecipeBundle> call, Throwable t) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            recipeListAdapter = new RecipeListAdapter(listOfRecipes, RecyclerViewFragment.this);
            recyclerView.setAdapter(recipeListAdapter);
        }
    }
    private void loadRestElements(final int currentPage) {
        recipeListAdapter.listOfRecipes.add(null);
        recipeListAdapter.notifyItemInserted(recipeListAdapter.listOfRecipes.size());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeListAdapter.listOfRecipes.remove(recipeListAdapter.listOfRecipes.size() - 1);
                Call<RecipeBundle> ncall = null;
                if (RecyclerViewFragment.this.isSearch()) {
                    ncall = client.getRecipe(RecipeClient.key, getSearchString(), new Integer(currentPage).toString());
                } else {
                    ncall = client.getRecipe(RecipeClient.key, new Integer(currentPage).toString());
                }
                ncall.enqueue(new Callback<RecipeBundle>() {
                    @Override
                    public void onResponse(Call<RecipeBundle> call, Response<RecipeBundle> response) {
                        RecipeBundle recipeBundle = response.body();
                        for(Recipe recipe:recipeBundle.getListOfRecipes()){
                            listOfRecipes.add(recipe);
                        }
                        addIngredientsToRecipes(listOfRecipes);
                        recipeListAdapter.addDataToListOfRecipes(recipeBundle.getListOfRecipes());
                    }

                    @Override
                    public void onFailure(Call<RecipeBundle> call, Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, 2500);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ArrayList<Recipe> list = null;
        if (savedInstanceState != null) {
//            if(savedInstanceState.getParcelableArrayList("LIST_KEY")!=null) {
//                list = savedInstanceState.getParcelableArrayList("LIST_KEY");
//            }
//            layoutManager = new LinearLayoutManager(getContext());
//            recipeListAdapter = new RecipeListAdapter(list, RecyclerViewFragment.this);
//            recyclerView.setAdapter(recipeListAdapter);

            if(savedInstanceState.getParcelable("STATE")!=null){

                    recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("STATE"));

            }


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        ArrayList<Recipe> list = new ArrayList<>();
//            if(recipeListAdapter!=null) {
//                for (Recipe recipe : recipeListAdapter.listOfRecipes) {
//                    list.add(recipe);
//                }
//            }
//
//        outState.putParcelableArrayList("LIST_KEY", list);
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("State",listState);
    }

    private void addIngredientsToRecipes(ArrayList<Recipe> listOfRecipes){
        ArrayList<String> listOfIngredients = new ArrayList<>();
        listOfIngredients.add("1 bunch kale, large stems discarded, leaves finely chopped");
        listOfIngredients.add("1/3 cup feta cheese");
        listOfIngredients.add("1/2 teaspoon salt");
        listOfIngredients.add("1/4 cup currants");
        listOfIngredients.add("1 tablespoon apple cider vinegar");
        listOfIngredients.add("1/4 cup toasted pine nuts");
        listOfIngredients.add("1 apple, diced");
        for (Recipe recipe:listOfRecipes){
            recipe.setListOfIngredients(listOfIngredients);
        }
    }

//    public void addRecipe(Recipe recipe){
//        recipeListAdapter.listOfRecipes.add(0,recipe);
//        recipeListAdapter.notifyDataSetChanged();
//
//    }

}
