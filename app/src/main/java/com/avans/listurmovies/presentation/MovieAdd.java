package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.avans.listurmovies.R;

public class MovieAdd extends AppCompatActivity {

    private int mCurrentPage = 1;
    private int mLastPage = 1;
    private MovieAddAdapter mAdapter;
    private String mQuery = "";
    private int mFilter = R.id.popular_movies;
    private MovieViewModel mMovieViewModel;
    private String mListid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_to_movie_list_add);

        RecyclerView recyclerView = findViewById(R.id.movieAdd_recyclerview);
        mAdapter = new MovieAddAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        mListid = (String) getIntent().getSerializableExtra("ListId");

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
                mFilter = R.id.now_playing;
                loadMovies();
                break;
            case R.id.popular_movies:
                mFilter = R.id.popular_movies;
                loadMovies();
                break;
            case R.id.top_rated:
                mFilter = R.id.top_rated;
                loadMovies();
                break;
            case R.id.upcoming:
                mFilter = R.id.upcoming;
                loadMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(){
        mMovieViewModel.getMovies(mCurrentPage, mFilter).observe(this, movieResults -> {
            if(movieResults == null) return;
            mAdapter.setMovies(movieResults.getResult(), mListid);
        });
    }

    private void loadSearchMovies(){
        mMovieViewModel.searchMovies(mQuery, mCurrentPage).observe(MovieAdd.this, movieResults -> {
            if(movieResults == null) return;
            mAdapter.setMovies(movieResults.getResult(), mListid);
            mLastPage = movieResults.getTotal_pages();
        });
    }

}