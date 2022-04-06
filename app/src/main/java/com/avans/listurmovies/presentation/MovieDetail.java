package com.avans.listurmovies.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ReviewViewModel mReviewViewModel;
    private MovieRepository mMovieRepository = new MovieRepository(this);
    Toast noMoreReviewsToast;
    private ReviewAdapter mAdapter;
    private int mCurrentPage = 1;
    private int mReviewsLastPage;
    private YouTubePlayerView mYouTubePlayerView;
    private NestedScrollView scrollView;
    private LinearLayout reviewRecyclerViewContainer;

    private int mMovieId = 0;
    private String mMovieTitle;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mReviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        scrollView = findViewById(R.id.nested_scroll_view);
        reviewRecyclerViewContainer = findViewById(R.id.review_recyclerview_container);

        TextView title = findViewById(R.id.movie_detail_name);
        TextView description = findViewById(R.id.movie_detail_description);
        ImageView image = findViewById(R.id.movie_detail_image);
        TextView genre = findViewById(R.id.movie_detail_genre);
        TextView release = findViewById(R.id.movie_detail_release);
        TextView voteAverage = findViewById(R.id.movie_detail_vote_average);
        TextView voteCount = findViewById(R.id.movie_detail_vote_count);

        //Get movie info
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        StringJoiner genreText = new StringJoiner(" | ");

        mMovieId = movie.getId();
        mMovieTitle = movie.getTitle();
        String mMovieDescription = movie.getOverview();
        Date mMovieReleaseDate = movie.getRelease_date();
        int[] mMovieGenreIds = movie.getGenres();
        String mMovieOriginalLanguage = movie.getOriginal_language();
        String mMoviePosterPath = movie.getPoster_path();
        String mMovieBackdropPath = movie.getBackdrop_path();

        //Stats
        double movieVoteAverage = movie.getVote_average();
        int movieVoteCount = movie.getVote_count();

        //Set movie image, if backdrop is not available use the original (poster) image
        if(movie.getBackdrop_path() == null) {
            Glide.with(this).load(this.getString(R.string.movieURL) + mMoviePosterPath).into(image);
        }else {
            Glide.with(this).load(this.getString(R.string.movieURL) + mMovieBackdropPath).into(image);
        }

        mYouTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(mYouTubePlayerView);

        //Set movie title
        title.setText(mMovieTitle);
        //Put language before the genres
        genreText.add(mMovieOriginalLanguage.toUpperCase());
        //Set genres
        for(int genreId: mMovieGenreIds) {
            genreText.add(String.valueOf(genreId));
        }

        genre.setText(genreText.toString());
        //Set movie description
        description.setText(mMovieDescription);
        //Set release date
        release.setText(getString(R.string.release_date) + " " + dateFormat.format(mMovieReleaseDate));

        voteAverage.setText(getString(R.string.rating) + " \u2605" + movieVoteAverage);
        voteCount.setText(getString(R.string.total_ratings) + " " + movieVoteCount);

        RecyclerView reviewRecyclerview = findViewById(R.id.review_recyclerview);
        mAdapter = new ReviewAdapter(this);
        reviewRecyclerview.setAdapter(mAdapter);
        reviewRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        noMoreReviewsToast = Toast.makeText(this, "No more reviews", Toast.LENGTH_SHORT);

        getMovieVideos();
        getAllReviewsById();
    }

    public void nextReviews(View view) {
        if(mReviewsLastPage > mCurrentPage) {
            mCurrentPage++;
            getAllReviewsById();
            scrollToTopOfReviews();
        } else {
            noMoreReviewsToast.show();
        }
    }

    public void previousReviews(View view) {
        if(mCurrentPage > 1) {
            mCurrentPage--;
            getAllReviewsById();
            scrollToTopOfReviews();
        } else {
            noMoreReviewsToast.show();
        }
    }

    public void scrollToTopOfReviews() {
        scrollView.scrollTo(0, reviewRecyclerViewContainer.getTop());
    }

    private void getMovieVideos() {
        mMovieRepository.getMovieVideos(mMovieId).observe(MovieDetail.this, trailerResult -> {
            if(trailerResult == null) return;
            List<Video> trailers = getMovieTrailer(trailerResult.getResults());

            //Get the trailer
            if(trailers.size() > 0) {
                String trailerURL = trailers.get(0).getVideoUrl();

                mYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(trailerURL, 0);
                        youTubePlayer.mute();
                    }
                });
            } else {
                //If no trailers are available, remove the player
                ((ViewGroup)mYouTubePlayerView.getParent()).removeView(mYouTubePlayerView);
            }
        });
    }

    private void getAllReviewsById() {
        mReviewViewModel.getAllReviewsById(mMovieId, mCurrentPage).observe(MovieDetail.this, reviewResults -> {
            if(reviewResults == null) return;

            mAdapter.setReviews(reviewResults.getReviews());
            mReviewsLastPage = reviewResults.getTotal_pages();
        });
    }

    public List<Video> getMovieTrailer(List<Video> allMovies) {
        List<Video> trailers = new ArrayList<>();

        //Filter trailers and trailers on youtube only
        for(Video video : allMovies) {
            if(video.getType().equals("Trailer") && video.getSite().equals("YouTube")) {
                trailers.add(video);
            }
        }

        return trailers;
    }

    public void shareMovie(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        mMovieRepository.getMovieVideos(mMovieId).observe(MovieDetail.this, trailerResult -> {
            if(trailerResult == null) return;
            List<Video> trailers = getMovieTrailer(trailerResult.getResults());
            String trailerURL = trailers.get(0).getVideoUrl();
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.check_this_movie) + "! \n" + mMovieTitle + "\n" + getString(R.string.youtube_link) + trailerURL);
        });

        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}