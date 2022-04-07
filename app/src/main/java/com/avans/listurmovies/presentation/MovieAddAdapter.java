package com.avans.listurmovies.presentation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.ListRepository;
import com.avans.listurmovies.domain.movie.Movie;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class MovieAddAdapter extends RecyclerView.Adapter<MovieAddAdapter.MovieAddViewHolder> {
    private final LayoutInflater mInflater;
    private List<Movie> mMovies;
    private Context mContext;
    private ListRepository mListRepository;
    private String list_id;

    public MovieAddAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mListRepository = new ListRepository(mContext);

    }

    @Override
    public MovieAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_item, parent, false);
        return new MovieAddViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieAddViewHolder holder, int position) {
        Movie current = mMovies.get(position);
        holder.bindTo(current);
    }

    void setMovies(List<Movie> words, String list_id){
        mMovies = words;
        this.list_id = list_id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else return 0;
    }

    class MovieAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMovieTitle;
        private ImageView mMovieImage;
        private TextView mMovieRating;


        public MovieAddViewHolder(View itemView) {
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
            mListRepository.addMovie(list_id, currentMovie.getId());
        }
    }
}
