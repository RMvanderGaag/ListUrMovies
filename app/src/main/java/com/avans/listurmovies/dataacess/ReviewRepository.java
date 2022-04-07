package com.avans.listurmovies.dataacess;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.retrofit.MovieAPI;
import com.avans.listurmovies.dataacess.retrofit.RetrofitClient;
import com.avans.listurmovies.domain.review.ReviewResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {
    private final MovieAPI mService;
    private Context mContext;
    private final MutableLiveData<ReviewResults> mReviews = new MutableLiveData<>();

    public ReviewRepository(Context mContext) {
        this.mService = RetrofitClient.getInstance().getmRepository();
        this.mContext = mContext;
    }

    public MutableLiveData<ReviewResults> getAllReviewsById(int movieId, int currentPage) {
        Call<ReviewResults> call = mService.getAllReviewsByPage(movieId, mContext.getResources().getString(R.string.api_key), currentPage);
        Log.d(ReviewRepository.class.getSimpleName(), "MovieId: " + movieId + ", language: " + MovieRepository.LANGUAGE + ", currentpage: " + currentPage);

        call.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                if(response.code() == 200) {
                    mReviews.setValue(response.body());
                    Log.d("Response: ", response.body().getTotal_results() + "");
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the reviews: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve the reviews");
            }
        });

        return mReviews;
    }
}
