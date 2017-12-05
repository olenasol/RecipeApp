package com.example.olena.recipeapp.fragments;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.adapters.RecipeHolder;
import com.example.olena.recipeapp.interfaces.EndlessRecyclerOnScrollListener;
import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.adapters.RecipeListAdapter;
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
    private Retrofit retrofit;
    private RecipeClient client;
    private Handler handler;
    private RecipeListAdapter recipeListAdapter;
    private static int yPosition;
    private boolean isSearch;
    private String searchString;

    public RecyclerViewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        handler = new Handler();
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.canScrollVertically();
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
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                yPosition = dy;
               // hideShowEditText();
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
            //recipeDetails.setEnterTransition(new Fade());
            //setExitTransition(new Fade());
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
    private void hideShowEditText(){
        if(yPosition<=0){
            ((MainNavActivity)getActivity()).setSearchVisible();
        }
        else{
            //yPosition=0;
            ((MainNavActivity)getActivity()).setSearchGone();
        }
    }
    private void loadFirstSetOfElements(){
        Call<RecipeBundle> ncall = null;
        if(RecyclerViewFragment.this.isSearch()){
            ncall = client.getRecipe(RecipeClient.key,getSearchString(), new Integer(1).toString());
        }
        else {
            ncall = client.getRecipe(RecipeClient.key, new Integer(1).toString());
        }
        ncall.enqueue(new Callback<RecipeBundle>() {
            @Override
            public void onResponse(Call<RecipeBundle> call, Response<RecipeBundle> response) {
                RecipeBundle recipeBundle = response.body();
                if(recipeBundle.getListOfRecipes().size()==0){
                    getFragmentManager()
                            .beginTransaction()
                            .hide(RecyclerViewFragment.this)
                            .add(R.id.fragment_container, new NoResultsFragment())
                            .commit();
                }
                recipeListAdapter = new RecipeListAdapter(recipeBundle.getListOfRecipes(), RecyclerViewFragment.this);
                recyclerView.setAdapter(recipeListAdapter);
            }

            @Override
            public void onFailure(Call<RecipeBundle> call, Throwable t) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Recipe> list = savedInstanceState.getParcelableArrayList("LIST_KEY");
            layoutManager = new LinearLayoutManager(getContext());
            recipeListAdapter = new RecipeListAdapter(list, RecyclerViewFragment.this);
            recyclerView.setAdapter(recipeListAdapter);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Recipe> list = new ArrayList<>();
        for(Recipe recipe:recipeListAdapter.listOfRecipes){
            list.add(recipe);
        }
        outState.putParcelableArrayList("LIST_KEY", list);
    }

}
