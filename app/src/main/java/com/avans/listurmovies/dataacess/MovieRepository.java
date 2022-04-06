package com.avans.listurmovies.dataacess;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.retrofit.MovieAPI;
import com.avans.listurmovies.dataacess.retrofit.RetrofitClient;
import com.avans.listurmovies.dataacess.room.MovieDAO;
import com.avans.listurmovies.dataacess.room.MovieRoomDatabase;
import com.avans.listurmovies.domain.movie.Movie;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.avans.listurmovies.domain.movie.VideoResult;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final MovieAPI mService;
    private Context mContext;
    private MovieDAO mMovieDAO;
    private MutableLiveData<VideoResult> mVideo = new MutableLiveData<>();

    private final MutableLiveData<MovieResults> listOfMovies = new MutableLiveData<>();

    public static final String LANGUAGE = Locale.getDefault().toLanguageTag();

    public MovieRepository(Context context) {
        this.mService = RetrofitClient.getInstance().getmRepository();
        this.mContext = context;

        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(context);
        mMovieDAO = db.movieDAO();;
    }


    public void insert (Movie movie) {
        new insertAsyncTask(mMovieDAO).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO mAsyncTaskDao;

        insertAsyncTask(MovieDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public MutableLiveData<MovieResults> getPopularMovies(int page) {
        Call<MovieResults> call = mService.getPopularMovies(mContext.getResources().getString(R.string.api_key), LANGUAGE, page);
        apiCall(call);
        return listOfMovies;
    }

    public MutableLiveData<MovieResults> getNowPlayingMovies(int page) {
        Call<MovieResults> call = mService.getNowPlayingMovies(mContext.getResources().getString(R.string.api_key), LANGUAGE, page);
        apiCall(call);
        return listOfMovies;
    }

    public MutableLiveData<MovieResults> getTopRatedMovies(int page) {
        Call<MovieResults> call = mService.getTopRatedMovies(mContext.getResources().getString(R.string.api_key), LANGUAGE, page);
        apiCall(call);
        return listOfMovies;
    }

    public MutableLiveData<MovieResults> getUpcomingMovies(int page) {
        Call<MovieResults> call = mService.getUpcomingMovies(mContext.getResources().getString(R.string.api_key), LANGUAGE, page);
        apiCall(call);
        return listOfMovies;
    }

    public MutableLiveData<MovieResults> searchMovies(String query, int page) {
        Log.d("query: ", query + " " + LANGUAGE);
        Call<MovieResults> call = mService.searchMovie(mContext.getResources().getString(R.string.api_key), LANGUAGE, query, page);
        Log.d(MovieRepository.class.getSimpleName(), "https://api.themoviedb.org/3/search/movie?api_key=" + mContext.getResources().getString(R.string.api_key) + "&language=" + LANGUAGE + "&query=" + query + "&page=" + page);
        apiCall(call);
        return listOfMovies;
    }

    private void apiCall(Call<MovieResults> call){
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                listOfMovies.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                listOfMovies.postValue(null);
            }
        });
    }

    public MutableLiveData<VideoResult> getMovieVideos(int movieId) {
        Call<VideoResult> call = mService.getTrailer(movieId, mContext.getResources().getString(R.string.api_key), LANGUAGE);

        call.enqueue(new Callback<VideoResult>() {
            @Override
            public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                if(response.code() == 200) {
                    mVideo.setValue(response.body());
                } else {
                    Log.e(MovieRepository.class.getSimpleName(), "Something went wrong when retrieving the trailer: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<VideoResult> call, Throwable t) {
                Log.e(MovieRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the trailer");
                mVideo.setValue(null);
            }
        });

        return mVideo;
    }
}
