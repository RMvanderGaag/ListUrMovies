package com.avans.listurmovies.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.MovieRepository;
import com.avans.listurmovies.domain.genre.Genre;
import com.avans.listurmovies.domain.movie.Movie;
import com.avans.listurmovies.domain.movie.MovieResults;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.StringJoiner;

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

    void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

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
            //Title
            mMovieTitle.setText(currentMovie.getTitle());

            //Image
            Glide.with(mContext).load(mContext.getString(R.string.movieURL) + currentMovie.getPoster_path())
                    .placeholder(R.drawable.img_placeholder).error(R.drawable.img_error).into(mMovieImage);
            mMovieRating.setText("\u2605 " + currentMovie.getVote_average());
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
