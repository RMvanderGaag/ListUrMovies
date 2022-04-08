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

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mListRepository = new ListRepository(application);
    }

    public void addList(String name, String description) {
        mListRepository.addList(name, description);
    }

    public void deleteMovie(String list_id, int movieId) {
        mListRepository.deleteMovie(list_id, movieId);
    }

    public void deleteList(String list_id) {
        mListRepository.deleteList(list_id);
    }

    public MutableLiveData<MovieList> getListDetails(String list_id) {
        return mListRepository.getListDetails(list_id);
    }

    public MutableLiveData<MovieListResults> getLists(int page, User userinfo) {
        return mListRepository.getAllLists(page, userinfo);
    }
}
