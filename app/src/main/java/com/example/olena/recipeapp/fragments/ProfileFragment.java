package com.example.olena.recipeapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private  TextView userNameTxt;
    private  TextView userSurnameTxT;
    private ImageView profilePic;
    private Button goToMainBtn;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userSurnameTxT = view.findViewById(R.id.userSurnameTxt);
        goToMainBtn = view.findViewById(R.id.goToMainBtn);
        profilePic = view.findViewById(R.id.profilePic);
        fillProfile();
        goToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        return view;
    }
    private void fillProfile(){
        Profile profile = Profile.getCurrentProfile();
        userNameTxt.setText("Name: " + profile.getFirstName());
        userSurnameTxT.setText("Surname: " + profile.getLastName());
        Picasso.with(getContext()).load(profile.getProfilePictureUri(200,200)).into(profilePic);
    }
    private  void goToMainActivity(){
        Intent intent = new Intent(getContext(),MainNavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
