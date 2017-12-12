package com.example.olena.recipeapp.activities;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.MediaCas;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.fragments.ProfileFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        if(isLoggedIn()){
            getProfile();
        }
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

               //     getProfile();

            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLoginActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FacebookLoginActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                if(currentProfile == null){
                    deleteProfile();
                }
                else {
                    getProfile();
                }
            }
        };

    }

    private void getProfile() {
         profileFragment = new ProfileFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_facebook_container, profileFragment)
                    .commitAllowingStateLoss();


    }
    private void deleteProfile(){
        getSupportFragmentManager()
                .beginTransaction()
                .remove(profileFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile()!=null;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
       // super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "myFragmentName", profileFragment);
        super.onSaveInstanceState(outState);
    }
}
