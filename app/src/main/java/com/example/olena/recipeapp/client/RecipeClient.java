package com.example.olena.recipeapp.client;


import com.example.olena.recipeapp.models.RecipeBundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeClient {

    @GET("/api/search")
    Call<RecipeBundle> getRecipe(@Query("key") String key, @Query("page") String pageNumber);

    @GET("/api/search")
    Call<RecipeBundle> getRecipe(@Query("key") String key, @Query("q") String query, @Query("page") String pageNumber);
}
