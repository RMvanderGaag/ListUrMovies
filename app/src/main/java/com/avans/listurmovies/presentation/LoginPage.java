package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.UserRepository;

public class LoginPage extends AppCompatActivity {
    private UserRepository mUserRepository = new UserRepository(this);

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.loginButton);

        //Remove previous logins
        SharedPreferences sharedPreferences = getSharedPreferences(mUserRepository.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(mUserRepository.SESSION_ID).commit();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                mUserRepository.loginGetToken(username, password);
            }
        });
    }

    public void continueAsGuest(View view) {
        mUserRepository.goToMovieOverview();
    }
}