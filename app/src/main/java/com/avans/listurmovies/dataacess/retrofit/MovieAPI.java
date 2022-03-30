package com.avans.listurmovies.dataacess.retrofit;

import com.avans.listurmovies.domain.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieResults> getPopularMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/latest")
    Call<MovieResults> getLatestMovies(
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("movie/now_playing")
    Call<MovieResults> getNowPlayingMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MovieResults> getUpcomingMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResults> searchMovie(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query
    );
}
