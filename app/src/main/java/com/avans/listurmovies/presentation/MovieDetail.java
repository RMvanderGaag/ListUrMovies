package com.avans.listurmovies.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.MovieRepository;
import com.avans.listurmovies.domain.genre.Genre;
import com.avans.listurmovies.domain.genre.GenreResults;
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
import java.util.Locale;
import java.util.StringJoiner;

public class MovieDetail extends AppCompatActivity {
    private ReviewViewModel mReviewViewModel;
    private MovieViewModel mMovieViewModel;
    private MovieRepository mMovieRepository = new MovieRepository(this);
    private Toast noMoreReviewsToast;
    private ReviewAdapter mAdapter;
    private YouTubePlayerView mYouTubePlayerView;
    private NestedScrollView mScrollView;
    private LinearLayout mReviewRecyclerViewContainer;

    private int mCurrentPage = 1;
    private int mReviewsLastPage;

    private int mMovieId = 0;
    private String mMovieTitle;
    double mMovieVoteAverage;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mReviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        mScrollView = findViewById(R.id.nested_scroll_view);
        mReviewRecyclerViewContainer = findViewById(R.id.review_recyclerview_container);

        TextView title = findViewById(R.id.movie_detail_name);
        TextView description = findViewById(R.id.movie_detail_description);
        ImageView image = findViewById(R.id.movie_detail_image);
        TextView genre = findViewById(R.id.movie_detail_genre);
        TextView release = findViewById(R.id.movie_detail_release);
        TextView voteAverage = findViewById(R.id.movie_detail_vote_average);
        TextView voteCount = findViewById(R.id.movie_detail_vote_count);

        //Get movie info
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        StringJoiner genres = new StringJoiner(" | ");
        genres.add(movie.getOriginal_language().toUpperCase(Locale.ROOT));
        final Observer<GenreResults> nameObserver = new Observer<GenreResults>() {
            @Override
            public void onChanged(GenreResults genreResults) {
                for(int i = 0; i < movie.getGenres().length; i++){
                    for(Genre g : genreResults.genres){
                        if(g.getId() == movie.getGenres()[i]){
                            genres.add(g.getName());
                        }
                    }
                }
                genre.setText(genres.toString());
            }
        };
        mMovieViewModel.getGenres().observe(MovieDetail.this, nameObserver);

        StringJoiner genreText = new StringJoiner(" | ");

        mMovieId = movie.getId();
        mMovieTitle = movie.getTitle();
        String mMovieDescription = movie.getOverview();
        Date mMovieReleaseDate = movie.getRelease_date();
        String mMovieOriginalLanguage = movie.getOriginal_language();
        String mMoviePosterPath = movie.getPoster_path();
        String mMovieBackdropPath = movie.getBackdrop_path();

        //Stats
        mMovieVoteAverage = movie.getVote_average();
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
        //Set movie description
        description.setText(mMovieDescription);
        //Set release date
        release.setText(getString(R.string.release_date) + " " + dateFormat.format(mMovieReleaseDate));

        voteAverage.setText(getString(R.string.rating) + " \u2605" + mMovieVoteAverage);
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
        mScrollView.scrollTo(0, mReviewRecyclerViewContainer.getTop());
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

    public void rateMovie(View view) {
        final AlertDialog.Builder ratingPopUp = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker, null);
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);

        String[] nums = {"0.5","1","1.5","2","2.5","3","3.5","4","4.5","5","5.5","6","6.5","7","7.5","8","8.5","9","9.5","10"};
        numberPicker.setDisplayedValues(nums);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(nums.length - 1);

        //Set text in the popup
        ratingPopUp.setTitle("Rate");
        ratingPopUp.setMessage("Give the movie a rating");
        ratingPopUp.setView(dialogView);

        ratingPopUp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        ratingPopUp.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Submit the rating
                mMovieRepository.rateMovie(mMovieId, nums[numberPicker.getValue()]);
            }
        });

        //Show the popup
        ratingPopUp.show();
    }
}