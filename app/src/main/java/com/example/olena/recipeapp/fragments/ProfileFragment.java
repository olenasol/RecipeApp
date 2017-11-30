package com.example.olena.recipeapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.activities.MainNavActivity;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private  TextView userNameTxt;
    private  TextView userSurnameTxT;
    private ImageView profilePic;

    public ProfileFragment() {
        // Required empty public constructor
//        ((MainNavActivity)getActivity()).setSearchGone();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainNavActivity)getActivity()).setSearchGone();
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userSurnameTxT = view.findViewById(R.id.userSurnameTxt);
        profilePic = view.findViewById(R.id.profilePic);
        fillProfile();
        return view;
    }
    private void fillProfile(){
        Profile profile = Profile.getCurrentProfile();
        userNameTxt.setText("Name: " + profile.getFirstName());
        userSurnameTxT.setText("Surname: " + profile.getLastName());
        Picasso.with(getContext()).load(profile.getProfilePictureUri(200,200)).into(profilePic);
    }

}
