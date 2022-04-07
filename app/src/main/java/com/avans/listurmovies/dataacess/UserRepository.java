package com.avans.listurmovies.dataacess;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.retrofit.MovieAPI;
import com.avans.listurmovies.dataacess.retrofit.RetrofitClient;
import com.avans.listurmovies.domain.user.User;
import com.avans.listurmovies.domain.user.PostUser;
import com.avans.listurmovies.domain.user.UserRequestToken;
import com.avans.listurmovies.presentation.LoginPage;
import com.avans.listurmovies.presentation.MovieOverview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final MovieAPI mService;
    private Context mContext;
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private static UserRepository instance = null;

    public static final String SHARED_PREFS = "user";
    public static final String SESSION_ID = "session_id";

    public UserRepository(Context mContext) {
        this.mService = RetrofitClient.getInstance().getmRepository();
        this.mContext = mContext;
    }

    public UserRepository() {
        this.mService = RetrofitClient.getInstance().getmRepository();

    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }


    //Step 1: This request a request_token from the api by sending the api key with the request
    public void loginGetToken(String username, String password) {
        //Get a new session (request_token)
        Call<UserRequestToken> call = mService.createNewSession(mContext.getResources().getString(R.string.api_key));

        call.enqueue(new Callback<UserRequestToken>() {
            @Override
            public void onResponse(Call<UserRequestToken> call, Response<UserRequestToken> response) {
                if(response.code() == 200) {
                    UserRequestToken request_token = response.body();
                    loginValidateToken(username, password, request_token.getRequest_token());
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong with retrieving the new request_token: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UserRequestToken> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request_token request");
            }
        });
    }

    //Step 2: If a key is available, validate the token with the user info to get a session_id
    public void loginValidateToken(String username, String password, String request_token) {
        PostUser body = new PostUser(username, password, request_token);
        Call<PostUser> call = mService.validateRequestToken(mContext.getResources().getString(R.string.api_key), body);

        call.enqueue(new Callback<PostUser>() {
            @Override
            public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                if(response.code() == 200) {
                    PostUser result = response.body();
                    loginGetSessionId(result.getRequest_token());
                    Log.d(UserRepository.class.getSimpleName(), "Validated user: " + body.getUsername() + "\n"
                            + "with request token: " + result.getRequest_token() + "\n");
                } else {
                    Log.d(UserRepository.class.getSimpleName(), "username or password is invalid");
                    Toast.makeText(mContext, R.string.login_toast, Toast.LENGTH_LONG).show();
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong with validating the request_token: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<PostUser> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the validate request token request");
            }
        });
    }

    //Step 3: If the request_key is validated by the user, request a session_id from the server
    public void loginGetSessionId(String request_token) {
        UserRequestToken body = new UserRequestToken(request_token);
        Call<UserRequestToken> call = mService.createSessionId(mContext.getResources().getString(R.string.api_key), body);

        call.enqueue(new Callback<UserRequestToken>() {
            @Override
            public void onResponse(Call<UserRequestToken> call, Response<UserRequestToken> response) {
                if(response.code() == 200) {
                    UserRequestToken result = response.body();

                    //Save the session_id in the sharedpreference
                    SharedPreferences sharedPref = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SESSION_ID, result.getSession_id());
                    editor.apply();

                    goToMovieOverview();
                    Log.d(UserRepository.class.getSimpleName(), "Session id created: " + result.getSession_id());
                } else {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving the session id: \n"
                            + "Response code: " + response.code() + "\n"
                            + "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UserRequestToken> call, Throwable t) {
                Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the request to retrieve session id");
            }
        });
    }

    public MutableLiveData<User> getUserInfo() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String session_id = sharedPreferences.getString(SESSION_ID, null);
//        sharedPreferences.edit().remove(SESSION_ID).commit();
        String api_key = mContext.getResources().getString(R.string.api_key);

        if(session_id != null) {
            //Logged in
            Call<User> call = mService.getUserDetails(api_key, session_id);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200) {
                        User user = response.body();
                        mUser.setValue(user);
                    } else {
                        Log.e(UserRepository.class.getSimpleName(), "Something went wrong when retrieving account details: \n"
                                + "Response code: " + response.code() + "\n"
                                + "Response body: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(UserRepository.class.getSimpleName(), "Something went wrong when starting the account details retreive");
                }
            });
        } else {
            User guest = new User(0, "Guest", null, false);
            mUser.setValue(guest);
        }

        return mUser;
    }

    public void logOut() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(SESSION_ID).commit();
        goToLoginPage();
    }

    public void goToLoginPage() {
        Intent intent = new Intent(mContext, LoginPage.class);
        mContext.startActivity(intent);
    }

    public void goToMovieOverview() {
        Intent intent = new Intent(mContext, MovieOverview.class);
        mContext.startActivity(intent);
    }
}
