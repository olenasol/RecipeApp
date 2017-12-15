package com.example.olena.recipeapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;


public class RecipeHolder extends RecyclerView.ViewHolder {

    private TextView titleTxt;
    private TextView publisherTxt;
    private ImageView imageView;
    private TextView trandingTxt;
    private CardView cardView;

    RecipeHolder(View itemView) {
        super(itemView);
        titleTxt = itemView.findViewById(R.id.titleTxt);
        publisherTxt = itemView.findViewById(R.id.authorTxt);
        imageView = itemView.findViewById(R.id.imageView);
        cardView = itemView.findViewById(R.id.card_view);
        trandingTxt = itemView.findViewById(R.id.trendingTxt);
    }

    public TextView getTitleTxt() {
        return titleTxt;
    }

    public TextView getPublisherTxt() {
        return publisherTxt;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTrandingTxt() {
        return trandingTxt;
    }

    public CardView getCardView() {
        return cardView;
    }
}
