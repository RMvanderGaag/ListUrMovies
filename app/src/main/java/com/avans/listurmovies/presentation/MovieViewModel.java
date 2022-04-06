package com.avans.listurmovies.dataacess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.genre.GenreResults;
import com.avans.listurmovies.domain.movie.Movie;
import com.avans.listurmovies.domain.movie.MovieResults;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository mRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    public MutableLiveData<MovieResults> getMovies(int page, int filter) {
        if(filter == R.id.popular_movies){
            return mRepository.getPopularMovies(page);
        }else if(filter == R.id.now_playing){
            return mRepository.getNowPlayingMovies(page);
        }else if(filter == R.id.top_rated){
            return mRepository.getTopRatedMovies(page);
        }else if(filter == R.id.upcoming){
            return mRepository.getUpcomingMovies(page);
        }
        return null;
    }
    public MutableLiveData<MovieResults> searchMovies(String query, int page) {
        return mRepository.searchMovies(query, page);
    }

    public MutableLiveData<MovieResults> setGenreFilter(String filter, int page) {
        return mRepository.setGenreFilter(filter, page);
    }

    public MutableLiveData<MovieResults> setRatingFilter(int min, int max, int page) {
        return mRepository.setRatingFilter(min, max, page);
    }

    public MutableLiveData<GenreResults> getGenres() {
        return mRepository.getGenres();
    }


    public void insert(Movie movie) { mRepository.insert(movie); }

}
