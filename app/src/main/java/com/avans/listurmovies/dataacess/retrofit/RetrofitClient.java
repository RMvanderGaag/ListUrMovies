package com.avans.listurmovies.dataacess.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance = null;
    private MovieAPI mRepository;

    public RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRepository = retrofit.create(MovieAPI.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if(mInstance == null) {
            mInstance = new RetrofitClient();
        }

        return mInstance;
    }

    public MovieAPI getmRepository() {
        return mRepository;
    }
}
