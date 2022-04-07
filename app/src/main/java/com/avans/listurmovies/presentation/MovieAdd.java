package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.movie.Movie;

import java.util.List;

public class MovieAdd extends AppCompatActivity {

    private int mCurrentPage = 1;
    private int mLastPage = 1;
    private MovieAddAdapter adapter;
    private String mQuery = "";
    private int filter = R.id.popular_movies;
    private MovieViewModel mMovieViewModel;
    private String listid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_to_movie_list_add);

        RecyclerView recyclerView = findViewById(R.id.movieAdd_recyclerview);
        adapter = new MovieAddAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        listid = (String) getIntent().getSerializableExtra("ListId");

        loadMovies();
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
        // automatically handle clicks on the Home/Up button

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
            adapter.setMovies(movieResults.getResult(), listid);
        });
    }

    private void loadSearchMovies(){
        mMovieViewModel.searchMovies(mQuery, mCurrentPage).observe(MovieAdd.this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult(), listid);
            mLastPage = movieResults.getTotal_pages();
        });
    }

}