package com.avans.listurmovies.dataacess;

import static com.avans.listurmovies.dataacess.UserRepository.SESSION_ID;
import static com.avans.listurmovies.dataacess.UserRepository.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.retrofit.MovieAPI;
import com.avans.listurmovies.dataacess.retrofit.RetrofitClient;
import com.avans.listurmovies.domain.list.MovieData;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.list.MovieListData;
import com.avans.listurmovies.domain.list.MovieListResults;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.avans.listurmovies.domain.user.User;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepository {

    private final MovieAPI mService;
    private Context mContext;
    private UserRepository mUserRepository = UserRepository.getInstance();

    private final MutableLiveData<MovieListResults> mListOfLists = new MutableLiveData<MovieListResults>();
    private final MutableLiveData<MovieList> mListOfListDetails = new MutableLiveData<MovieList>();
    private final MutableLiveData<MovieResults> mListOfMovies = new MutableLiveData<MovieResults>();

    public static final String LANGUAGE = Locale.getDefault().toLanguageTag();

    public ListRepository(Context context) {
        this.mService = RetrofitClient.getInstance().getmRepository();
        this.mContext = context;
    }

    public MutableLiveData<MovieListResults> getAllLists(int page, User userinfo) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);

        Call<MovieListResults> call = mService.getAllLists(userinfo.getId(), mContext.getResources().getString(R.string.api_key), LANGUAGE, session_id, page);
        apiCall(call);

        return mListOfLists;
    }

    public MutableLiveData<MovieList> getListDetails(String list_id) {
        Call<MovieList> call = mService.getListDetails(list_id, mContext.getResources().getString(R.string.api_key), LANGUAGE);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if(response.code() == 200) {
                    mListOfListDetails.setValue(response.body());
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the reviews: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the reviews");
            }
        });


        return mListOfListDetails;
    }

    public void addList(String name, String description) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);

        MovieListData body = new MovieListData(name, description);

        Call<MovieListResults> call = mService.createList(mContext.getResources().getString(R.string.api_key), session_id, LANGUAGE, body);
        apiCall(call);
    }

    public void addMovie(String list_id, int movieId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);
        MovieData body = new MovieData(movieId);

        Call<MovieResults> call = mService.addMovie(list_id, mContext.getResources().getString(R.string.api_key), session_id, body);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if(response.code() == 201) {
                    mListOfMovies.setValue(response.body());
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the reviews: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the reviews");
            }
        });
    }

    public void deleteMovie(String list_id, int movieId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);
        Toast deletedMovieFromListToast = Toast.makeText(mContext, R.string.deleted_movie_from_list, Toast.LENGTH_LONG);
        MovieData body = new MovieData(movieId);

        Call<MovieResults> call = mService.deleteMovie(list_id, mContext.getResources().getString(R.string.api_key), session_id, body);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if(response.code() == 200) {
                    mListOfMovies.setValue(response.body());
                    deletedMovieFromListToast.show();
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the reviews: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the reviews");
            }
        });
    }

    public void deleteList(String list_id) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);
        Toast deletedListToast = Toast.makeText(mContext, R.string.deleted_list, Toast.LENGTH_LONG);

        Call<MovieListResults> call = mService.deleteList(list_id, mContext.getResources().getString(R.string.api_key), session_id);

        call.enqueue(new Callback<MovieListResults>() {
            @Override
            public void onResponse(Call<MovieListResults> call, Response<MovieListResults> response) {
                if(response.code() == 200) {
                    deletedListToast.show();
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the reviews: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieListResults> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the reviews");
            }
        });
    }

    private void apiCall(Call<MovieListResults> call){
        call.enqueue(new Callback<MovieListResults>() {
            @Override
            public void onResponse(Call<MovieListResults> call, Response<MovieListResults> response) {
                mListOfLists.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieListResults> call, Throwable t) {
                mListOfLists.postValue(null);
            }
        });
    }
}
