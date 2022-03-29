package com.avans.listurmovies.dataacess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.domain.Movie;
import com.avans.listurmovies.domain.MovieResults;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository mRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    public MutableLiveData<MovieResults> getMovies(int page) {
        return mRepository.getMovies(page);
    }

    public void insert(Movie movie) { mRepository.insert(movie); }
}
