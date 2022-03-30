package com.avans.listurmovies.presentation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final LayoutInflater mInflater;
    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
            Movie current = mMovies.get(position);
            holder.bindTo(current);
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

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMovieTitle;
        private ImageView mMovieImage;
        private TextView mMovieRating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieTitle = itemView.findViewById(R.id.movieTitle);
            mMovieImage = itemView.findViewById(R.id.movieImage);
            mMovieRating = itemView.findViewById(R.id.movieRating);

            itemView.setOnClickListener(this);
        }

        public void bindTo(Movie currentMovie) {
            mMovieTitle.setText(currentMovie.getTitle());
            Glide.with(mContext).load(mContext.getString(R.string.movieURL) + currentMovie.getPoster_path()).into(mMovieImage);
            mMovieRating.setText("Rating: " + currentMovie.getVote_average());
        }

        @Override
        public void onClick(View view){
            Movie currentMovie = mMovies.get(getAdapterPosition());
            Intent detailMovie = new Intent(mContext, MovieDetail.class);

            detailMovie.putExtra("Movie", currentMovie);
            mContext.startActivity(detailMovie);
        }
    }
}
