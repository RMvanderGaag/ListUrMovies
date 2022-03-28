package com.avans.listurmovies.dataacess;

import com.avans.listurmovies.domain.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieResults> getPopularMeals(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );
}
