package com.example.olena.recipeapp.fragments;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
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
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RecipeDetailsFragment extends Fragment {

    private static RecipeListAdapter adapter;
    private static  int position;
    private String transactionName;
    private Button shareFacebookBtn;
    private Button shareTwitterBtn;
    private TextView titleTxt;
    private TextView authorTxt;
    private ImageView imageView;
    private RecyclerView recyclerViewIngr;

    public static RecipeDetailsFragment newInstance(int position,RecipeListAdapter adapter) {

        RecipeDetailsFragment.position = position;
        RecipeDetailsFragment.adapter = adapter;
        return new RecipeDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainNavActivity)getActivity()).setSearchGone();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initTwitter();
        View view = inflater.inflate(R.layout.recipe_details, container, false);
        titleTxt = view.findViewById(R.id.titleTxt);
        authorTxt =  view.findViewById(R.id.authorTxt);
        imageView = view.findViewById(R.id.imageView);
        recyclerViewIngr = view.findViewById(R.id.rec_view_ingredients);
        shareFacebookBtn = view.findViewById(R.id.shareFacebookBtn);
        shareTwitterBtn = view.findViewById(R.id.shareTwitterBtn);
        shareFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postToFacebook();

            }
        });
        shareTwitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postToTwitter();
            }
        });
        fillFields();
        return view;
    }

    private void makeShare(Uri imagePath){
        if (imagePath != null){
            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                    .text(adapter.getListOfRecipes().get(position).getListOfIngredientsString())
                    .image(imagePath);
            builder.show();
        }else {
            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                    .text(adapter.getListOfRecipes().get(position).getListOfIngredientsString());
            builder.show();
        }
    }



    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    private void fillFields(){
        titleTxt.setText(adapter.getListOfRecipes().get(position).getTitle());
        authorTxt.setText(adapter.getListOfRecipes().get(position).getPublisher());
        imageView.setTransitionName(transactionName);
        Picasso.with(getContext())
                .load(adapter.getListOfRecipes().get(position).getImageUrl())
                .into(imageView);
        recyclerViewIngr.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIngr.setAdapter(new IngredientListAdapter(adapter.getListOfRecipes().get(position).getListOfIngredients()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else{
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }
    private void postToFacebook(){

            GetImageAsync task = new GetImageAsync();
            task.execute(adapter.getListOfRecipes().get(position).getImageUrl());
            Bitmap image = null;
            try {
                image = task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .setUserGenerated(true)
                        .build();
                ShareOpenGraphObject obj = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "object")
                        .putString("og:title", adapter.getListOfRecipes().get(position).getTitle())
                        .putString("og:description", adapter.getListOfRecipes().get(position).getListOfIngredientsString())
                        .putPhoto("og:image", photo)
                        .build();

                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("og.likes")
                        .putObject("object", obj)
                        .build();
                ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("object")
                        .setAction(action)
                        .build();
                ShareDialog.show(RecipeDetailsFragment.this, content);
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
    private void postToTwitter(){
        TweetComposer.getInstance();
        Picasso.with(getContext())
                .load(adapter.getListOfRecipes().get(position).getImageUrl())
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                              File file = getImageFile(bitmap);

                              if (file != null)
                                  makeShare(FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", file));
                              else
                                  makeShare(null);
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

    private void initTwitter(){
        Twitter.initialize(getContext());
        TwitterConfig config = new TwitterConfig.Builder(getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY), getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
    class GetImageAsync extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String...str) {

            return getBitmapFromURL(str[0]);
        }
        public Bitmap getBitmapFromURL(String src) {

                URL url = null;
                try {
                    url = new URL(src);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    return image;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
        }

    }
    private File getImageFile(Bitmap bitmap){
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "JPEGImage";
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        }catch (IOException e){

        }
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {

            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){

        }
        return file;
    }
}
