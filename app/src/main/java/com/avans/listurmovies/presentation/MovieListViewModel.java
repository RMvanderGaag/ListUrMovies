package com.avans.listurmovies.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.dataacess.ListRepository;
import com.avans.listurmovies.dataacess.UserRepository;
import com.avans.listurmovies.domain.list.MovieList;
import com.avans.listurmovies.domain.list.MovieListResults;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.avans.listurmovies.domain.user.User;

import java.util.HashMap;

public class MovieListViewModel extends AndroidViewModel {

    private final ListRepository mListRepository;
    private final UserRepository mUserRepository;


    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mListRepository = new ListRepository(application);
        mUserRepository = new UserRepository(application);

    }

    public void addList(String name, String description) {
        Log.d("name", name);
        Log.d("desc", description);
        mListRepository.addList(name, description);
    }

    public void deleteMovie(String list_id, int movieId) {
       // HashMap<String, Integer> movieData = new HashMap<>();
        //movieData.put("media_id", movieId);
       // Log.d("name", name);
       // Log.d("desc", description);
        mListRepository.deleteMovie(list_id, movieId);
    }

    public void deleteList(String list_id) {
        // HashMap<String, Integer> movieData = new HashMap<>();
        //movieData.put("media_id", movieId);
        // Log.d("name", name);
        // Log.d("desc", description);
        mListRepository.deleteList(list_id);
    }

    public MutableLiveData<MovieList> getListDetails(String list_id) {
        return mListRepository.getListDetails(list_id);
    }

    public MutableLiveData<MovieListResults> getLists(int page, User userinfo) {
        return mListRepository.getAllLists(page, userinfo);
        /*for(List list : mListRepository.getAllLists(page, userinfo).getValue().getResults()) {
            Log.d("List", list.getName());
        }
        if(mListRepository.getAllLists(page, userinfo) != null) {
            //Log.d("Lists", mRepository.getAllLists(page));
            for(List list : mListRepository.getAllLists(page, userinfo).getValue().getResults()) {
                Log.d("List", list.getName());
            }

            return mListRepository.getAllLists(page, userinfo);
        }
        else {
            return null;
        }*/
        /*if(filter == R.id.popular_movies){
            return mRepository.getPopularMovies(page);
        }else if(filter == R.id.now_playing){
            return mRepository.getNowPlayingMovies(page);
        }else if(filter == R.id.top_rated){
            return mRepository.getTopRatedMovies(page);
        }else if(filter == R.id.upcoming){
            return mRepository.getUpcomingMovies(page);
        }*/
        //return null;
    }
    /*public MutableLiveData<MovieResults> searchMovies(String query, int page) {
        return mRepository.searchMovies(query, page);
    }

    public void insert(Movie movie) { mRepository.insert(movie); }*/
}
