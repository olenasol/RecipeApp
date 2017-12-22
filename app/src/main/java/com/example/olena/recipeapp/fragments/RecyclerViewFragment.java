package com.example.olena.recipeapp.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.AddRecipeActivity;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.adapters.RecipeHolder;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;
import com.example.olena.recipeapp.behaviorclasses.EndlessRecyclerOnScrollListener;
import com.example.olena.recipeapp.client.ApiClient;
import com.example.olena.recipeapp.client.RecipeClient;
import com.example.olena.recipeapp.interfaces.RecipeItemClickListener;
import com.example.olena.recipeapp.models.Recipe;
import com.example.olena.recipeapp.models.RecipeBundle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerViewFragment extends Fragment implements RecipeItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecipeClient client;

    private RecipeListAdapter recipeListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button goToTopBtn;
    private static int yPosition;
    private boolean isSearch;
    private String searchString;
    private static ArrayList<Recipe> listOfRecipes;


    public static void setListOfRecipes(ArrayList<Recipe> listOfRecipes) {
        RecyclerViewFragment.listOfRecipes = listOfRecipes;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton fabPlus = view.findViewById(R.id.fab);
        goToTopBtn = view.findViewById(R.id.toTopBtn);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.canScrollVertically();
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });
        goToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = ApiClient.getClient();
        client = retrofit.create(RecipeClient.class);
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList("LIST_KEY") != null) {

                ArrayList<Recipe> list = savedInstanceState.getParcelableArrayList("LIST_KEY");
                recipeListAdapter = new RecipeListAdapter(list, RecyclerViewFragment.this);
                recyclerView.setAdapter(recipeListAdapter);
            }
            recyclerView.getLayoutManager().scrollToPosition(savedInstanceState.getInt("STATE"));

        } else {
            loadFirstSetOfElements();

        }

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            int currentScrollPosition = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                yPosition = dy;
                currentScrollPosition += dy;

                hideTopButton();

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                hideShowEditText();

            }


            @Override
            public void onLoadMore(final int currentPage) {
                if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() >= 3)
                    loadRestElements(currentPage);
            }
        });

    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listOfRecipes = null;
                loadFirstSetOfElements();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
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
    public void onRecipeItemClick(final RecipeHolder holder, int position, RecipeListAdapter adapter) {

        final RecipeDetailsFragment recipeDetails = RecipeDetailsFragment.newInstance(position, adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recipeDetails.setTransactionName(ViewCompat.getTransitionName(holder.getImageView()));
            recipeDetails.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            recipeDetails.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));


        }

        startRecipeFragment(holder, recipeDetails);


    }

    private void startRecipeFragment(RecipeHolder holder, RecipeDetailsFragment recipeDetails) {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(holder.getImageView(), ViewCompat.getTransitionName(holder.getImageView()))
                    .replace(R.id.fragment_container, recipeDetails)
                    .addToBackStack(null)
                    .commit();

    }

    private void startNoResultFragment() {
        if (getFragmentManager() != null)
            getFragmentManager()
                    .beginTransaction()
                    .hide(RecyclerViewFragment.this)
                    .add(R.id.fragment_container, new NoResultsFragment())
                    .commit();
    }

    private void hideTopButton() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (yPosition <= 0) {
            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 10) {
                goToTopBtn.setVisibility(View.VISIBLE);
            } else {
                goToTopBtn.setVisibility(View.GONE);
            }
        } else {
            goToTopBtn.setVisibility(View.GONE);
        }

    }

    private void hideShowEditText() {
        if (yPosition <= 0) {
            if (getActivity() != null)
                ((MainNavActivity) getActivity()).setSearchVisible();

        } else {
            if (getActivity() != null)
                ((MainNavActivity) getActivity()).setSearchGone();
        }
    }

    private void loadFirstSetOfElements() {
        if (listOfRecipes == null) {
            Call<RecipeBundle> ncall = getCall(1);
            ncall.enqueue(new Callback<RecipeBundle>() {
                @Override
                public void onResponse(@NonNull Call<RecipeBundle> call, @NonNull Response<RecipeBundle> response) {
                    RecipeBundle recipeBundle = response.body();
                    assert recipeBundle != null;

                    if (recipeBundle.getListOfRecipes().size() == 0) {
                        startNoResultFragment();
                    }
                    listOfRecipes = recipeBundle.getListOfRecipes();
                    addIngredientsToRecipes(listOfRecipes);

                    for (int i = 0; i < MainNavActivity.listOfNewRecipies.size(); i++) {
                        listOfRecipes.add(i, MainNavActivity.listOfNewRecipies.get(i));
                    }

                    recipeListAdapter = new RecipeListAdapter(listOfRecipes, RecyclerViewFragment.this);
                    recyclerView.setAdapter(recipeListAdapter);

                }

                @Override
                public void onFailure(@NonNull Call<RecipeBundle> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            recipeListAdapter = new RecipeListAdapter(listOfRecipes, RecyclerViewFragment.this);
            recyclerView.setAdapter(recipeListAdapter);
        }
    }

    private void loadRestElements(final int currentPage) {
        recipeListAdapter.addElementToListOfRecipes(null);
        recipeListAdapter.notifyItemInserted(recipeListAdapter.getItemCount());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeListAdapter.removeLastFromListOfRecipes();
                Call<RecipeBundle> ncall = getCall(currentPage);
                ncall.enqueue(new Callback<RecipeBundle>() {
                    @Override
                    public void onResponse(@NonNull Call<RecipeBundle> call, @NonNull Response<RecipeBundle> response) {

                        RecipeBundle recipeBundle = response.body();
                        assert recipeBundle != null;
                        listOfRecipes.addAll(recipeBundle.getListOfRecipes());

                        addIngredientsToRecipes(listOfRecipes);
                        recipeListAdapter.addDataToListOfRecipes(recipeBundle.getListOfRecipes());
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecipeBundle> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, 2500);
    }

    private Call<RecipeBundle> getCall(int currentPage) {
        Call<RecipeBundle> ncall;
        if (RecyclerViewFragment.this.isSearch()) {
            ncall = client.getRecipe(RecipeClient.key, getSearchString(), Integer.valueOf(currentPage).toString());
        } else {
            ncall = client.getRecipe(RecipeClient.key, Integer.valueOf(currentPage).toString());

        }
        return ncall;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Recipe> list = new ArrayList<>();

        list.addAll(listOfRecipes);
        outState.putParcelableArrayList("LIST_KEY", list);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        outState.putInt("STATE", linearLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    private void addIngredientsToRecipes(ArrayList<Recipe> listOfRecipes) {
        ArrayList<String> listOfIngredients = new ArrayList<>();
        listOfIngredients.add("1 bunch kale, large stems discarded, leaves finely chopped");
        listOfIngredients.add("1/3 cup feta cheese");
        listOfIngredients.add("1/2 teaspoon salt");
        listOfIngredients.add("1/4 cup currants");
        listOfIngredients.add("1 tablespoon apple cider vinegar");
        listOfIngredients.add("1/4 cup toasted pine nuts");
        listOfIngredients.add("1 apple, diced");
        for (Recipe recipe : listOfRecipes) {
            recipe.setListOfIngredients(listOfIngredients);
        }
    }


}
