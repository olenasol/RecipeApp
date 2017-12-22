package com.example.olena.recipeapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.example.olena.recipeapp.socialsitesmanagers.FacebookManager;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private TextView userNameTxt;
    private TextView userSurnameTxT;
    private ImageView profilePic;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userSurnameTxT = view.findViewById(R.id.userSurnameTxt);
        profilePic = view.findViewById(R.id.profilePic);
        fillProfile();

        Button goToMainBtn = view.findViewById(R.id.goToMainBtn);
        goToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        return view;
    }

    private void fillProfile() {
        FacebookManager facebookManager = new FacebookManager(getContext());
        String name = "Name: " + facebookManager.getProfileFirstName();
        String surname = "Surname: " + facebookManager.getProfileLastName();
        userNameTxt.setText(name);
        userSurnameTxT.setText(surname);
        Picasso.with(getContext()).load(facebookManager.getProfilePictureUri()).into(profilePic);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getContext(), MainNavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
