package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MovieListDetail extends AppCompatActivity {

    private MovieListViewModel mMovieListViewModel;
    private MovieAdapter adapter;
    private MovieList listWithMovies;
    private int mCurrentPage = 1;
    private int mLastPage = 1;
    private int filter = R.id.popular_movies;
    private String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_detail);

        RecyclerView recyclerView = findViewById(R.id.listDetail_recyclerview);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);




        FloatingActionButton fab = findViewById(R.id.fab);

        MovieList movieList = (MovieList) getIntent().getSerializableExtra("List");


        Log.d("count", String.valueOf(movieList.getItem_count()));
        Log.d("movielist", movieList.getId());




        /*for(Movie movie : movieList.getItems()) {
            Log.d("movie", movie.getTitle());
        }*/

        //Log.d("")
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


}