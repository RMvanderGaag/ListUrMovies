package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.businesslogic.validation.ValidationTools;
import com.avans.listurmovies.dataacess.UserRepository;

public class LoginPage extends AppCompatActivity {
    private UserRepository mUserRepository = new UserRepository(this);
    private ValidationTools mValidationTools = new ValidationTools();

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.loginButton);
        mForgotPassword = findViewById(R.id.forgot_password);

        //Remove previous logins
        SharedPreferences sharedPreferences = getSharedPreferences(mUserRepository.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(mUserRepository.SESSION_ID).commit();

        mForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (mValidationTools.isInputFieldEmpty(username)) {
                    mUsername.setError(getText(R.string.username_error));
                }

                if (mValidationTools.isInputFieldEmpty(password)) {
                    mPassword.setError(getText(R.string.password_error));
                }

                if (!mValidationTools.isInputFieldEmpty(password) && !mValidationTools.isInputFieldEmpty(password)) {
                    mUserRepository.loginGetToken(username, password);
                }
            }
        });
    }

    public void continueAsGuest(View view) {
        mUserRepository.goToMovieOverview();
    }

    public void noAccount(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/signup"));
        startActivity(browserIntent);
    }

    public void forgotPassword(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/reset-password"));
        startActivity(browserIntent);
    }
}