package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.movie.Movie;
import com.bumptech.glide.Glide;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView title = findViewById(R.id.movie_detail_name);
        TextView description = findViewById(R.id.movie_detail_description);
        ImageView image = findViewById(R.id.movie_detail_image);
        TextView genre = findViewById(R.id.movie_detail_genre);
        TextView release = findViewById(R.id.movie_detail_release);

        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");
        title.setText(movie.getTitle());
        description.setText(movie.getOverview());

        if(movie.getBackdrop_path() == null){
            Glide.with(this).load(this.getString(R.string.movieURL) + movie.getPoster_path()).into(image);
        }else{
            Glide.with(this).load(this.getString(R.string.movieURL) + movie.getBackdrop_path()).into(image);
        }

        release.setText(movie.getRelease_date().toString());

    }
}