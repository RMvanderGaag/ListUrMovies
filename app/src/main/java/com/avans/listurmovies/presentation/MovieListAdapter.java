package com.avans.listurmovies.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.list.MovieList;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ListViewHolder> {
    private final LayoutInflater mInflater;
    private List<MovieList> mMovieList;
    private Context mContext;

    public MovieListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movielist_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        MovieList current = mMovieList.get(position);
        holder.bindTo(current);
    }

    void setLists(List<MovieList> words){
        mMovieList = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mMovieList != null)
            return mMovieList.size();
        else return 0;
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mListTitle;
        private TextView mListDescription;
        private ImageView mMovieImage;
        private TextView mMovieRating;

        public ListViewHolder(View itemView) {
            super(itemView);
            mListTitle = itemView.findViewById(R.id.movielistName);
            mListDescription = itemView.findViewById(R.id.movielistDescription);
           // mMovieImage = itemView.findViewById(R.id.movieImage);
           // mMovieRating = itemView.findViewById(R.id.movieRating);

            itemView.setOnClickListener(this);
        }

        public void bindTo(MovieList currentMovieList) {
            //Title
            mListTitle.setText(currentMovieList.getName());
            mListDescription.setText(currentMovieList.getDescription());

            //Image
            //Glide.with(mContext).load(mContext.getString(R.string.movieURL) + currentList.getPoster_path())
                    //.placeholder(R.drawable.img_placeholder).error(R.drawable.img_error).into(mMovieImage);
            //mMovieRating.setText("\u2605 " + currentList.getVote_average());
        }

        @Override
        public void onClick(View view){
            MovieList currentMovieList = mMovieList.get(getAdapterPosition());
            Intent detailList = new Intent(mContext, MovieListDetail.class);

            detailList.putExtra("List", currentMovieList);
            mContext.startActivity(detailList);
        }
    }
}

