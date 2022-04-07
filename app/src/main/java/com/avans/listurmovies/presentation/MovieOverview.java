package com.avans.listurmovies.presentation;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.avans.listurmovies.R;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MovieOverview extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    private MovieViewModel mMovieViewModel;
    private int mCurrentPage = 1;
    private int mLastPage = 1;
    private MovieAdapter adapter;
    private int filter = R.id.popular_movies;
    private DrawerLayout drawer;
    private String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView recyclerView = findViewById(R.id.movie_recyclerview);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView menu_username = header.findViewById(R.id.menu_username);
        ImageView menu_user_image = header.findViewById(R.id.menu_user_image);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.logout) {

                }

                if (id == R.id.movie_lists) {
                    //Log.d("user", mUserViewModel.getUser().getValue().getId());
                    if(mUserViewModel.getUser().getValue().getId() != 0) {
                        Intent intent = new Intent(getApplicationContext(), MovieListOverview.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MovieOverview.this, "Please login first", Toast.LENGTH_LONG).show();
                    }
                }
                if (id == R.id.add_movie_list) {
                    if(mUserViewModel.getUser().getValue().getId() != 0) {
                        Intent intent = new Intent(getApplicationContext(), MovieListAdd.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MovieOverview.this, "Please login first", Toast.LENGTH_LONG).show();
                    }
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //Load user information into the menu
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(MovieOverview.this, user -> {
            if(user == null) return;
            //Set the username in the menu bar to the current logged in user
            menu_username.setText(user.getUsername());
            //Set the user image in the menu bar to the current logged in user
            Glide.with(this).load(this.getString(R.string.userImageURL) + user.getAvatarHash()).into(menu_user_image);
        });

        //Load the default movies page
        loadMovies();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCurrentPage = 1;
                Log.d("submit", "onQueryTextSubmit: " + query);
                mQuery = query;
                loadSearchMovies();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("change", "onQueryTextChange: " + query);
                return false;
            }


        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mQuery = "";
                mCurrentPage = 1;
                loadMovies();
                return false;
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Set page to 1
        mCurrentPage = 1;

        int id = item.getItemId();


        switch (id) {
            case R.id.now_playing:
                filter = R.id.now_playing;
                loadMovies();
                break;
            case R.id.popular_movies:
                filter = R.id.popular_movies;
                loadMovies();
                break;
            case R.id.top_rated:
                filter = R.id.top_rated;
                loadMovies();
                break;
            case R.id.upcoming:
                filter = R.id.upcoming;
                loadMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(){
        mMovieViewModel.getMovies(mCurrentPage, filter).observe(this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult());
            mLastPage = movieResults.getTotal_pages();
        });
    }

    private void loadSearchMovies(){
        mMovieViewModel.searchMovies(mQuery, mCurrentPage).observe(MovieOverview.this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult());
            mLastPage = movieResults.getTotal_pages();
        });
    }

    public void nextMovies(View view) {
        if(mCurrentPage < mLastPage) {
            mCurrentPage++;
            if(mQuery.isEmpty()){
                loadMovies();
            }else{
                loadSearchMovies();
            }
            Toast.makeText(this, "Current page: " + mCurrentPage, Toast.LENGTH_SHORT).show();
        }
    }

    public void previousMovies(View view) {
        if(mCurrentPage == 1) return;
        mCurrentPage--;
        if(mQuery.isEmpty()){
            loadMovies();
        }else{
            loadSearchMovies();
        }
        Toast.makeText(this, "Current page: " + mCurrentPage, Toast.LENGTH_SHORT).show();
    }
}