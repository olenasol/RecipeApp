package com.example.olena.recipeapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.olena.recipeapp.R;

class ProgressViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }


    ProgressViewHolder(View v) {
        super(v);
        progressBar = v.findViewById(R.id.progressBar);
    }
}