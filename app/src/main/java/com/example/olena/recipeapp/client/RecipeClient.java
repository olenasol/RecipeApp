package com.example.olena.recipeapp.client;


import com.example.olena.recipeapp.models.RecipeBundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeClient {
    String key = "e1a1ac1024adab2c6d3fccb6d125c289";

    @GET("/api/search")
    Call<RecipeBundle> getRecipe(@Query("key") String key, @Query("page") String pageNumber);

    @GET("/api/search")
    Call<RecipeBundle> getRecipe(@Query("key") String key, @Query("q") String query, @Query("page") String pageNumber);
}
