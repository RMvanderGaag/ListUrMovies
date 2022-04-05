package com.avans.listurmovies.dataacess.retrofit;

import com.avans.listurmovies.domain.Genre;
import com.avans.listurmovies.domain.GenreResults;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.avans.listurmovies.domain.user.User;
import com.avans.listurmovies.domain.user.retrofit.PostUser;
import com.avans.listurmovies.domain.user.retrofit.UserRequestToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {
    // ### Login ###

    //Get request_token
    @GET("authentication/token/new")
    Call<UserRequestToken> createNewSession(
            @Query("api_key") String api_key
    );

    //Validate request_token with login
    @POST("authentication/token/validate_with_login")
    Call<PostUser> validateRequestToken(
            @Query("api_key") String api_key,
            @Body() PostUser loginData
    );

    //Create session id with request token
    @POST("authentication/session/new")
    Call<UserRequestToken> createSessionId(
            @Query("api_key") String api_key,
            @Body() UserRequestToken request_token
    );

    // ### User ###
    @GET("account")
    Call<User> getUserDetails(
            @Query("api_key") String api_key,
            @Query("session_id") String session_id
    );

    // ### Movies ###
    @GET("movie/popular")
    Call<MovieResults> getPopularMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
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
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreResults> getGenres(
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("discover/movie")
    Call<MovieResults> setFilter(
          @Query("api_key") String api_key,
          @Query("language") String language,
          @Query("page") int page,
          @Query("with_genres") String genres
    );
}
