package com.avans.listurmovies.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.MovieRepository;
import com.avans.listurmovies.domain.movie.Movie;
import com.avans.listurmovies.domain.movie.Video;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class MovieDetail extends AppCompatActivity {
    private MovieRepository mMovieRepository = new MovieRepository(this);
    private ReviewViewModel mReviewViewModel;
    private ReviewAdapter mAdapter;
    private int mCurrentPage = 1;
    private int mMovieId = 0;
    private String mTrailerUrl;
    private YouTubePlayerView youTubePlayerView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mReviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        TextView title = findViewById(R.id.movie_detail_name);
        TextView description = findViewById(R.id.movie_detail_description);
        ImageView image = findViewById(R.id.movie_detail_image);
        TextView genre = findViewById(R.id.movie_detail_genre);
        TextView release = findViewById(R.id.movie_detail_release);
        TextView voteAverage = findViewById(R.id.movie_detail_vote_average);
        TextView voteCount = findViewById(R.id.movie_detail_vote_count);

        //Get movie info
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        StringJoiner genreText = new StringJoiner(" | ");

        mMovieId = movie.getId();
        String movieTitle = movie.getTitle();
        String movieDescription = movie.getOverview();
        Date movieReleaseDate = movie.getRelease_date();
        int[] movieGenreIds = movie.getGenres();
        String movieOriginalLanguage = movie.getOriginal_language();
        String moviePosterPath = movie.getPoster_path();
        String movieBackdropPath = movie.getBackdrop_path();

        //Stats
        double movieVoteAverage = movie.getVote_average();
        int movieVoteCount = movie.getVote_count();

        //Set movie image, if backdrop is not available use the original (poster) image
        if(movie.getBackdrop_path() == null) {
            Glide.with(this).load(this.getString(R.string.movieURL) + moviePosterPath).into(image);
        }else {
            Glide.with(this).load(this.getString(R.string.movieURL) + movieBackdropPath).into(image);
        }

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        //Set movie title
        title.setText(movieTitle);
        //Put language before the genres
        genreText.add(movieOriginalLanguage.toUpperCase());
        //Set genres
        for(int genreId: movieGenreIds) {
            genreText.add(String.valueOf(genreId));
        }

        genre.setText(genreText.toString());
        //Set movie description
        description.setText(movieDescription);
        //Set release date
        release.setText(getString(R.string.release_date) + " " + dateFormat.format(movieReleaseDate));

        voteAverage.setText(getString(R.string.rating) + " \u2605" + movieVoteAverage);
        voteCount.setText(getString(R.string.total_ratings) + " " + movieVoteCount);

        RecyclerView reviewRecyclerview = findViewById(R.id.review_recyclerview);
        mAdapter = new ReviewAdapter(this);
        reviewRecyclerview.setAdapter(mAdapter);
        reviewRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        getMovieVideos();
        getAllReviewsById();
    }

//    public void nextReviews(View view) {
//        mCurrentPage++;
//        getAllReviewsById();
//    }

    private void getMovieVideos() {
        mMovieRepository.getMovieVideos(mMovieId).observe(MovieDetail.this, trailerResult -> {
            if(trailerResult == null) return;
            List<Video> allMovies = trailerResult.getResults();
            List<Video> trailers = new ArrayList<>();

            //Filter trailers and trailers on youtube only
            for(Video video : allMovies) {
                if(video.getType().equals("Trailer") && video.getSite().equals("YouTube")) {
                    trailers.add(video);
                }
            }

            //Get the trailer
            if(trailers.size() > 0) {
                mTrailerUrl = trailers.get(0).getVideoUrl();
            }

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(mTrailerUrl, 0);
                    youTubePlayer.mute();
                }
            });
        });
    }

    private void getAllReviewsById() {
        mReviewViewModel.getAllReviewsById(mMovieId, mCurrentPage).observe(MovieDetail.this, reviewResults -> {
            if(reviewResults == null) return;
            mAdapter.setReviews(reviewResults.getReviews());
            // mLastPage = reviewResults.getTotal_pages();
        });
    }
}