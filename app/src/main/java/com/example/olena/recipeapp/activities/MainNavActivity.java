package com.example.olena.recipeapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.fragments.ProfileFragment;
import com.example.olena.recipeapp.fragments.RecipeDetailsFragment;
import com.example.olena.recipeapp.fragments.RecyclerViewFragment;
import com.example.olena.recipeapp.models.Recipe;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogManager;

public class MainNavActivity extends AppCompatActivity
       {
    private boolean isTypedIn = false;
    private boolean isSearched = false;
    private EditText editText;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerViewFragment recyclerViewFragment;
    private ImageButton deleteBtn;
    private Timer timer;
    public static ArrayList<Recipe> listOfNewRecipies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.searchEdit);
        deleteBtn = findViewById(R.id.deleteBtn);
        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            if (fragment instanceof RecyclerViewFragment){
               recyclerViewFragment = (RecyclerViewFragment) fragment;
            }
        }
        else {
            getNewRecipe();
            startMainFragment();
        }

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    startSearchFragment();
                    isSearched = true;
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchWhenIdle();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isTypedIn = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0&&isTypedIn&&isSearched){
                    startMainFragment();
                    isTypedIn = false;
                    isSearched = false;
                }
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                startMainFragment();
            }
        });

    }

    private void startMainFragment(){
        recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setSearch(false);
        recyclerViewFragment.setSearchString("");
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,recyclerViewFragment,"REC_FRAG");
        fragmentTransaction.commit();
    }
    private void startSearchFragment(){
        String str = editText.getText().toString();
        if(!str.equals("")){
            recyclerViewFragment = new RecyclerViewFragment();
            recyclerViewFragment.setSearch(true);
            recyclerViewFragment.setSearchString(str);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, recyclerViewFragment,"REC_FRAG")
                    .commit();
        }
    }

    private Recipe getNewRecipe(){
        Recipe recipe = getIntent().getParcelableExtra("new_recipe");
        if(recipe!=null) {
            listOfNewRecipies.add(0, recipe);
        }
        return recipe;
    }

    public void setSearchVisible(){
        if((editText!=null)&&(deleteBtn!=null))
        editText.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);
    }
    public void setSearchGone(){
        if((editText!=null)&&(deleteBtn!=null)) {
            editText.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "myFragmentName", recyclerViewFragment);
    }
    private void searchWhenIdle(){
        isTypedIn = true;

        if(timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isSearched = true;
                startSearchFragment();
            }
        }, 2000);

    }

}
