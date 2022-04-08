package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.avans.listurmovies.domain.genre.GenreResults;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.movie.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MovieListDetail extends AppCompatActivity {

    private MovieListViewModel mMovieListViewModel;
    private MovieViewModel mMovieViewModel;
    private MovieAdapter adapter;
    private MovieList listWithMovies;
    private int mCurrentPage = 1;
    private int mLastPage = 1;
    private int filter = R.id.popular_movies;
    private String mQuery = "";
    private int minRating;
    private int maxRating;
    private int sort = R.id.popular_movies;

    private Boolean mLoadMovies = true;
    private Boolean mGenreFilter = false;
    private Boolean mRatingFilter = false;
    private Boolean mSearchMovies = false;

    private List<Genre> genres = new ArrayList<>();
    private List<Integer> filteredGenres = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_detail);

        recyclerView = findViewById(R.id.listDetail_recyclerview);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);



        FloatingActionButton fab = findViewById(R.id.fab);

        MovieList movieList = (MovieList) getIntent().getSerializableExtra("List");


        Log.d("count", String.valueOf(movieList.getItem_count()));
        Log.d("movielist", movieList.getId());




        /*for(Movie movie : movieList.getItems()) {
            Log.d("movie", movie.getTitle());
        }*/

        //Log.d("")
        final Observer<GenreResults> genreObserver = new Observer<GenreResults>() {
            @Override
            public void onChanged(GenreResults genreResults) {
                genres.addAll(genreResults.getResult());
            }
        };
        mMovieViewModel.getGenres().observe(MovieListDetail.this, genreObserver);

        mMovieListViewModel.getListDetails(movieList.getId()).observe(MovieListDetail.this, listWithMovies -> {
            if(listWithMovies == null) return;

            //loadLists(mCurrentPage, user);
            this.listWithMovies = listWithMovies;
            adapter.setMovies(listWithMovies.getItems());
            //mLastPage = listWithMovies.getItems();

            for(Movie movie : listWithMovies.getItems()) {
                Log.d("Movie", movie.getTitle());
            }

        });

        ItemTouchHelper helper = new ItemTouchHelper(new
                                                             ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT
                                                                     , ItemTouchHelper.LEFT) {
                                                                 @Override
                                                                 public boolean onMove(RecyclerView recyclerView,
                                                                                       RecyclerView.ViewHolder viewHolder,
                                                                                       RecyclerView.ViewHolder target) {

                                                                     return false;

                                                                 }

                                                                 @Override
                                                                 public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                                                                      int direction) {
                                                                     Movie currentMovie = listWithMovies.getItems().get(viewHolder.getAdapterPosition());

                                                                     mMovieListViewModel.deleteMovie(movieList.getId(), currentMovie.getId());
                                                                     listWithMovies.getItems().remove(viewHolder.getAdapterPosition());
                                                                     adapter.notifyItemRemoved(viewHolder.getAdapterPosition());


                                                                 }
                                                             });
        helper.attachToRecyclerView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detailList = new Intent(getApplicationContext(), MovieAdd.class);
                detailList.putExtra("ListId", movieList.getId());
                Log.d("listid", movieList.getId());
                startActivity(detailList);
            }
        });
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
                filteredGenres.clear();
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
        if (id == R.id.filter_genre || id == R.id.filter_rating) {
            showAlertBox(id);
        }

        switch (id) {
            case R.id.now_playing:
                sort = R.id.now_playing;
                loadMovies();
                break;
            case R.id.popular_movies:
                sort = R.id.popular_movies;
                loadMovies();
                break;
            case R.id.top_rated:
                sort = R.id.top_rated;
                loadMovies();
                break;
            case R.id.upcoming:
                sort = R.id.upcoming;
                loadMovies();
                break;
            case R.id.movie_lists:
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
                                        filteredGenres.add(g.getId());
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

            if(this.listWithMovies == null) return;
            adapter.setMovies(this.listWithMovies.getItems());
            mLastPage = this.listWithMovies.getItem_count() / 20;

    }

    private void loadSearchMovies(){
        setInActive();
        mSearchMovies = true;

            if(this.listWithMovies == null) return;
            List<Movie> searchedMovies = new ArrayList<>();
            for(Movie movie : this.listWithMovies.getItems()) {
                if(movie.getTitle().toLowerCase(Locale.ROOT).contains(mQuery.toLowerCase(Locale.ROOT)) == true) {
                    searchedMovies.add(movie);
                }
            }
            adapter.setMovies(searchedMovies);
            mLastPage = this.listWithMovies.getItem_count() / 20;

    }

    private void setGenreFilter(){
        setInActive();
        mGenreFilter = true;
        ArrayList<Movie> genreMovies = new ArrayList<>();

            if(this.listWithMovies == null) return;
            for(Movie movie : this.listWithMovies.getItems()) {
                for(int i = 0; i < movie.getGenres().length; i++){
                    for(int id : filteredGenres){
                        if(id == movie.getGenres()[i]){
                            genreMovies.add(movie);
                        }
                    }
                }

            }
            adapter.setMovies(genreMovies);
            mLastPage = this.listWithMovies.getItem_count() / 20;

    }

    private void setRatingFilter(){
        setInActive();
        mRatingFilter = true;

        ArrayList<Movie> ratingMovies = new ArrayList<>();
            if(this.listWithMovies == null) return;
            for(Movie movie : this.listWithMovies.getItems()) {
                if(movie.getVote_average() >= minRating && movie.getVote_average() <= maxRating) {
                    ratingMovies.add(movie);
                }
            }
            adapter.setMovies(ratingMovies);
            mLastPage = this.listWithMovies.getItem_count() / 20;

    }




}