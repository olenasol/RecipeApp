package com.example.olena.recipeapp.client;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    public static Retrofit retrofit = null;

    public  static Retrofit getClient(Context context){
        if (retrofit == null){
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://food2fork.com/api/")
                    .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
        }
        return retrofit;
    }

}
