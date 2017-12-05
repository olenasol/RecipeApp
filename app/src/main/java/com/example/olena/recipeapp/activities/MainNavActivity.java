package com.example.olena.recipeapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
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

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.fragments.ProfileFragment;
import com.example.olena.recipeapp.fragments.RecipeDetailsFragment;
import com.example.olena.recipeapp.fragments.RecyclerViewFragment;
import com.facebook.login.LoginManager;

import java.util.logging.LogManager;

public class MainNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText editText;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerViewFragment recyclerViewFragment;
    private ImageButton deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editText = findViewById(R.id.searchEdit);
        deleteBtn = findViewById(R.id.deleteBtn);
        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
            if (fragment instanceof RecyclerViewFragment){
                recyclerViewFragment = (RecyclerViewFragment) fragment;
            }
            else if(fragment instanceof RecipeDetailsFragment){


            }
        }
        else {
            startMainFragment();
        }
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    startSearchFragment();
                    return true;
                }

                return false;
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
        fragmentTransaction.add(R.id.fragment_container,recyclerViewFragment);
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
                    .replace(R.id.fragment_container, recyclerViewFragment)
                    .commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            ProfileFragment profileFragment = new ProfileFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, profileFragment)
                    .commit();
        } else if (id == R.id.likes) {

        } else if (id == R.id.logout) {
            LoginManager.getInstance().logOut();
            goToLoginPage();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToLoginPage() {
        Intent intent = new Intent(this,FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void setSearchVisible(){
        if(editText!=null)
        editText.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);
    }
    public void setSearchGone(){
        if(editText!=null)
        editText.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "myFragmentName", recyclerViewFragment);
    }
    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStack();
        } else {

            super.onBackPressed();
        }
    }
}
