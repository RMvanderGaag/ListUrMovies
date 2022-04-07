package com.avans.listurmovies.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.dataacess.UserRepository;
import com.avans.listurmovies.domain.user.User;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository mRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
    }

    public MutableLiveData<User> getUser() {
        return mRepository.getUserInfo();
    }
}
