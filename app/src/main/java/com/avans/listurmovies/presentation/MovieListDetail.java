package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.movie.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieListDetail extends AppCompatActivity {

    private MovieListViewModel mMovieListViewModel;
    private MovieAdapter mAdapter;
    private MovieList mListWithMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_detail);

        RecyclerView recyclerView = findViewById(R.id.listDetail_recyclerview);
        mAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        MovieList movieList = (MovieList) getIntent().getSerializableExtra("List");

        mMovieListViewModel.getListDetails(movieList.getId()).observe(MovieListDetail.this, listWithMovies -> {
            if(listWithMovies == null) return;
            this.mListWithMovies = listWithMovies;
            mAdapter.setMovies(listWithMovies.getItems());

            for(Movie movie : listWithMovies.getItems()) {
                Log.d("Movie", movie.getTitle());
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
             @Override
             public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                 Movie currentMovie = mListWithMovies.getItems().get(viewHolder.getAdapterPosition());

                 mMovieListViewModel.deleteMovie(movieList.getId(), currentMovie.getId());
                 mListWithMovies.getItems().remove(viewHolder.getAdapterPosition());
                 mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());


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