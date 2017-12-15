package com.example.olena.recipeapp.socialsitesmanagers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.dataproviders.ImageProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

public class TwitterManager {

    private Context context;

    public TwitterManager(Context context) {
        this.context = context;
        initTwitter(context);
    }

    private void initTwitter(Context context){
        Twitter.initialize(context);
        TwitterConfig config = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(context.getString(R.string.CONSUMER_KEY), context.getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
    private void makeShare(Uri imagePath,String text){
        if(context !=null)
            if (imagePath != null){
                TweetComposer.Builder builder = new TweetComposer.Builder(context)
                        //.text(adapter.getListOfRecipes().get(position).getListOfIngredientsString())
                        .text(text)
                        .image(imagePath);
                builder.show();
            }else {
                TweetComposer.Builder builder = new TweetComposer.Builder(context)
                        .text(text);
                builder.show();
            }
    }

    public void postToTwitter(String imageUrl, final String text){
        TweetComposer.getInstance();
        Picasso.with(context)
               // .load(adapter.getListOfRecipes().get(position).getImageUrl())
                .load(imageUrl)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                              File file = ImageProvider.getImageFile(bitmap,context);

                              if (file != null)
                                  if(context!=null)
                                      makeShare(FileProvider.getUriForFile(context, "com.example.android.fileprovider", file),text);
                                  else
                                      makeShare(null,"");
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
    }

}



