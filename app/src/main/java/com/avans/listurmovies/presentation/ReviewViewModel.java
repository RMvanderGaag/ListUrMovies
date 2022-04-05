package com.avans.listurmovies.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.dataacess.ReviewRepository;
import com.avans.listurmovies.domain.review.ReviewResults;

public class ReviewViewModel extends AndroidViewModel {
    private final ReviewRepository mRepository;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ReviewRepository(application);
    }

    public MutableLiveData<ReviewResults> getAllReviewsById(int movieId, int currentPage) {
        return mRepository.getAllReviewsById(movieId, currentPage);
    }
}
