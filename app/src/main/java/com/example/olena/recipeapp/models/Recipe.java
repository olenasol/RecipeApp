package com.example.olena.recipeapp.models;

import com.google.gson.annotations.SerializedName;

public class Recipe  {
    @SerializedName("recipe_id")
    private String recipeId;

    private String title;
    private String publisher;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("source_url")
    private String sourceUrl;

    public Recipe(String recipeId,String title, String publisher, String imageUrl, String sourceUrl) {
        this.recipeId = recipeId;
        this.title = title;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
