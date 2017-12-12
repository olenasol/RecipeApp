package com.example.olena.recipeapp.models;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    @SerializedName("recipe_id")
    private String recipeId;

    private String title;
    private String publisher;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("source_url")
    private String sourceUrl;
    @SerializedName("social_rank")
    private double socialRank;

    private ArrayList<String> listOfIngredients;

    public Recipe(String recipeId, String title, String publisher, String imageUrl, String sourceUrl,double socialRank) {
        this.recipeId = recipeId;
        this.title = title;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.socialRank = socialRank;
    }

    public Recipe(Parcel in) {
        this.recipeId = in.readString();
        this.title = in.readString();
        this.publisher = in.readString();
        this.imageUrl = in.readString();
        this.sourceUrl = in.readString();
        this.socialRank = in.readDouble();
        this.listOfIngredients = in.createStringArrayList();

    }


    public ArrayList<String> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(ArrayList<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }
    public String getListOfIngredientsString(){
        StringBuilder s = new StringBuilder("");
        for(String ing:listOfIngredients){
            s.append(ing).append(", ");
        }
        s.replace(s.length()-2,s.length(),".");
        return s.toString();
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

    public double getSocialRank() {
        return socialRank;
    }

    public void setSocialRank(double socialRank) {
        this.socialRank = socialRank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipeId);
        dest.writeString(this.title);
        dest.writeString(this.publisher);
        dest.writeString(this.imageUrl);
        dest.writeString(this.sourceUrl);
        dest.writeDouble(this.socialRank);
        dest.writeStringList(this.listOfIngredients);
    }
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
