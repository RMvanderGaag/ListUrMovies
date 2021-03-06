package com.avans.listurmovies.dataacess.retrofit;

import com.avans.listurmovies.domain.genre.GenreResults;
import com.avans.listurmovies.domain.list.MovieData;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.list.MovieListData;
import com.avans.listurmovies.domain.list.MovieListResults;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.avans.listurmovies.domain.movie.Rating;
import com.avans.listurmovies.domain.movie.RatingResponse;
import com.avans.listurmovies.domain.movie.VideoResult;
import com.avans.listurmovies.domain.review.ReviewResults;
import com.avans.listurmovies.domain.user.User;
import com.avans.listurmovies.domain.user.PostUser;
import com.avans.listurmovies.domain.user.UserRequestToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<MovieResults> setGenreFilter(
          @Query("api_key") String api_key,
          @Query("language") String language,
          @Query("page") int page,
          @Query("with_genres") String genres
    );

    @GET("discover/movie")
    Call<MovieResults> setRatingFilter(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page,
            @Query("vote_average.gte") Number min,
            @Query("vote_average.lte") Number max
    );

    @GET("movie/{movie_id}/videos")
    Call<VideoResult> getTrailer(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

    //Rate movie
    @POST("movie/{movie_id}/rating")
    Call<RatingResponse> postRating(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id,
            @Body() Rating rating
    );

    // ### Reviews ###
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResults> getAllReviewsByPage(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    // ### Lists ###
    @GET("list/{list_id}")
    Call<MovieList> getListDetails(
            @Path("list_id") String id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );
    @GET("account/{account_id}/lists")
    Call<MovieListResults> getAllLists(
            @Path("account_id") int account_id,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("session_id") String session_id,
            @Query("page") int page
    );
    @POST("list")
    Call<MovieListResults> createList(
            @Query("api_key") String api_key,
            @Query("session_id") String session_id,
            @Query("language") String language,
            @Body() MovieListData movieListData
    );

    @POST("list/{list_id}/add_item")
    Call<MovieResults> addMovie(
            @Path("list_id") String list_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id,
            @Body() MovieData movieData
            );

    @POST("list/{list_id}/remove_item")
    Call<MovieResults> deleteMovie(
            @Path("list_id") String list_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id,
            @Body() MovieData movieData
    );

    @DELETE("list/{list_id}")
    Call<MovieListResults> deleteList(
            @Path("list_id") String list_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id
    );
}
