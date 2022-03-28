package com.avans.listurmovies.dataacess;

import android.content.Context;
import android.util.Log;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.Movie;
import com.avans.listurmovies.domain.MovieResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final MovieAPI mService;
    private Context mContext;

    private final static String LANGUAGE = Locale.getDefault().toLanguageTag();

    public MovieRepository(Context context) {
        this.mService = RetrofitClient.getInstance().getmRepository();
        this.mContext = context;
    }

    public void getPopularMovies() {
        Call<MovieResults> call = mService.getPopularMeals(mContext.getResources().getString(R.string.api_key), LANGUAGE, 1);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<Movie> movies = results.getResult();

                String result;
                for(Movie movie : movies) {
                    result = "Movie{" +
                            "id=" + movie.getId() +
                            ", title='" + movie.getTitle() + '\'' +
                            ", overview='" + movie.getOverview() + '\'' +
                            ", release_date=" + movie.getRelease_date() +
                            ", genres=" + Arrays.toString(movie.getGenres()) +
                            ", original_language='" + movie.getOriginal_language() + '\'' +
                            ", original_title='" + movie.getOriginal_title() + '\'' +
                            ", poster_path='" + movie.getPoster_path() + '\'' +
                            ", backdrop_path='" + movie.getBackdrop_path() + '\'' +
                            ", popularity=" + movie.getPopularity() +
                            ", vote_avarage=" + movie.getVote_avarage() +
                            ", vote_count=" + movie.getVote_avarage() +
                            ", adult=" + movie.isAdult() +
                            '}';

                    Log.d(MovieRepository.class.getSimpleName(), result);
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

}
