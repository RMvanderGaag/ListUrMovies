package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Toast;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.genre.Genre;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdd extends AppCompatActivity {

    private int mCurrentPage = 1;
    private int mLastPage = 10;
    private MovieAddAdapter adapter;
    private String mQuery = "";
    private int filter = R.id.now_playing;
    private MovieViewModel mMovieViewModel;
    private String listid;
    private int minRating;
    private int maxRating;

    private Boolean mLoadMovies = true;
    private Boolean mGenreFilter = false;
    private Boolean mRatingFilter = false;
    private Boolean mSearchMovies = false;

    private List<Genre> genres = new ArrayList<>();
    private List<String> filteredGenres = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_to_movie_list_add);

        recyclerView = findViewById(R.id.movieAdd_recyclerview);
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Set page to 1
        mCurrentPage = 1;

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


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

    private void showAlertBox(int id){
        if(id == R.id.filter_genre){

            List<String> genreNames = new ArrayList<>();
            for(Genre g : genres){
                genreNames.add(g.getName());
            }
            String[] genreArray = genreNames.toArray(new String[genreNames.size()]);
            filteredGenres.clear();


            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Filter")
                    .setMultiChoiceItems(genreArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean checked) {
                            if(checked){
                                for(Genre g : genres){
                                    if(g.getName().equals(genreArray[position])){
                                        filteredGenres.add(g.getId() + "");
                                    }
                                }
                            }
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setGenreFilter();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }else if(id == R.id.filter_rating){
            final NumberPicker maxVal = new NumberPicker(this);
            maxVal.setMaxValue(10);
            maxVal.setMinValue(0);

            final NumberPicker minVal = new NumberPicker(this);
            minVal.setMaxValue(10);
            minVal.setMinValue(0);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(minVal);
            linearLayout.addView(maxVal);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Filter")
                    .setMessage("Enter a minimum and maximum rating value")
                    .setView(linearLayout)
                    .setPositiveButton(android.R.string.yes, null)
                    .setNegativeButton(android.R.string.no, null)
                    .show();

            Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(minVal.getValue() > maxVal.getValue()) return;
                    dialog.dismiss();
                    minRating = minVal.getValue();
                    maxRating = maxVal.getValue();
                    setRatingFilter();
                }
            });
        }
    }

    private void setInActive(){
        mLoadMovies = false;
        mGenreFilter = false;
        mRatingFilter = false;
        mSearchMovies = false;
    }


    private void loadMovies(){
        setInActive();
        mLoadMovies = true;
        mMovieViewModel.getMovies(mCurrentPage, filter).observe(this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult(), listid);
            mLastPage = movieResults.getTotal_pages();
        });
    }

    private void loadSearchMovies(){
        setInActive();
        mSearchMovies = true;
        mMovieViewModel.searchMovies(mQuery, mCurrentPage).observe(MovieAdd.this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult(), listid);
            mLastPage = movieResults.getTotal_pages();
        });
    }

    private void setGenreFilter(){
        setInActive();
        mGenreFilter = true;
        String filters = String.join(",", filteredGenres);
        mMovieViewModel.setGenreFilter(filters, mCurrentPage).observe(MovieAdd.this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult(), listid);
            mLastPage = movieResults.getTotal_pages();
        });
    }

    private void setRatingFilter(){
        setInActive();
        mRatingFilter = true;
        mMovieViewModel.setRatingFilter(minRating, maxRating, mCurrentPage).observe(MovieAdd.this, movieResults -> {
            if(movieResults == null) return;
            adapter.setMovies(movieResults.getResult(), listid);
            mLastPage = movieResults.getTotal_pages();
        });
    }




    public void nextMoviesAdd(View view) {
        if(mCurrentPage < mLastPage) {
            mCurrentPage++;
            if(mGenreFilter) {
                setGenreFilter();
            }else if(mLoadMovies){
                loadMovies();
            }else if(mSearchMovies){
                loadSearchMovies();
            }else if(mRatingFilter){
                setRatingFilter();
            }

            recyclerView.scrollTo(0, recyclerView.getTop());
            Toast.makeText(this, "Current page: " + mCurrentPage, Toast.LENGTH_SHORT).show();
        }
    }

    public void previousMoviesAdd(View view) {
        if (mCurrentPage == 1) return;
        mCurrentPage--;
        if (mGenreFilter) {
            setGenreFilter();
        } else if (mLoadMovies) {
            loadMovies();
        } else if (mSearchMovies) {
            loadSearchMovies();
        } else if (mRatingFilter) {
            setRatingFilter();
        }
    }

}