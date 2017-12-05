package com.example.olena.recipeapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;


public class RecipeHolder extends RecyclerView.ViewHolder {

    TextView titleTxt;
    TextView publisherTxt;
    ImageView imageView;
    TextView trandingTxt;
    CardView cardView;

    RecipeHolder(View itemView, final Context context) {
        super(itemView);
        titleTxt = itemView.findViewById(R.id.titleTxt);
        publisherTxt = itemView.findViewById(R.id.authorTxt);
        imageView = itemView.findViewById(R.id.imageView);
        cardView = itemView.findViewById(R.id.card_view);
        trandingTxt = itemView.findViewById(R.id.trendingTxt);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
