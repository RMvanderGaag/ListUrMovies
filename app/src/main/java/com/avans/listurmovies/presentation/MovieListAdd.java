package com.avans.listurmovies.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.listurmovies.R;

public class MovieListAdd extends AppCompatActivity {

    private MovieListViewModel mMovieListViewModel;
    private TextView name;
    private TextView description;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_add);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        name = findViewById(R.id.new_listName);
        description = findViewById(R.id.new_listDescription);
        saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addList(name.getText().toString(), description.getText().toString());

                setResult(Activity.RESULT_OK);
                finish();

            }
        });

    }

    public void addList(String name, String description) {
        mMovieListViewModel.addList(name, description);
    }
}