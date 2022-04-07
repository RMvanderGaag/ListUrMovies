package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.businesslogic.validation.ValidationTools;

public class MovieListAdd extends AppCompatActivity {
    private MovieListViewModel mMovieListViewModel;
    private EditText mName;
    private EditText mDescription;
    private Button saveButton;
    private ValidationTools mValidationTools = new ValidationTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_add);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        mName = findViewById(R.id.new_listName);
        mDescription = findViewById(R.id.new_listDescription);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String description = mDescription.getText().toString();

                if (mValidationTools.isInputFieldEmpty(name)) {
                    mName.setError(getText(R.string.input_field_error));
                }

                if (mValidationTools.isInputFieldEmpty(description)) {
                    mDescription.setError(getText(R.string.input_field_error));
                }

                if (!mValidationTools.isInputFieldEmpty(name) && !mValidationTools.isInputFieldEmpty(description)) {
                    addList(mName.getText().toString(), mDescription.getText().toString());

                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    public void addList(String name, String description) {
        mMovieListViewModel.addList(name, description);
    }
}