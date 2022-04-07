package com.avans.listurmovies.presentation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.dataacess.ListRepository;
import com.avans.listurmovies.domain.movie.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieListDetailAdapter extends RecyclerView.Adapter<MovieListDetailAdapter.MovieListDetailViewHolder> {
    private final LayoutInflater mInflater;
    private List<Movie> mMovies;
    private Context mContext;
    private MovieListViewModel mMovieListViewModel;
    private ListRepository mListRepository;

    public MovieListDetailAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        //mMovieListViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mListRepository = new ListRepository(mContext);

    }

    @Override
    public MovieListDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_item, parent, false);
        return new MovieListDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieListDetailViewHolder holder, int position) {
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

    class MovieListDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMovieTitle;
        private ImageView mMovieImage;
        private TextView mMovieRating;


        public MovieListDetailViewHolder(View itemView) {
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
            //HashMap<String, Integer> movieData;
            //movieData.put("media_id", currentMovie.getId());

           /* Log.d("listid", list_id);
            Log.d("movieid", String.valueOf(currentMovie.getId()));
            mListRepository.addMovie(list_id, currentMovie.getId());*/

            /*Intent detailMovie = new Intent(mContext, MovieDetail.class);


            detailMovie.putExtra("Movie", currentMovie);
            mContext.startActivity(detailMovie);*/
        }
    }
}
