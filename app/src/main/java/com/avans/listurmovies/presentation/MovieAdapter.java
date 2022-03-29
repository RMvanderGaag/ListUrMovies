package com.avans.listurmovies.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final LayoutInflater mInflater;
    private List<Movie> mMovies; // Cached copy of words

    public MovieAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mMovies != null) {
            Movie current = mMovies.get(position);
            holder.movieItemView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.movieItemView.setText("No Movie");
        }
    }

    void setMovies(List<Movie> words){
        mMovies = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieItemView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieItemView = itemView.findViewById(R.id.textView);
        }
    }
}
