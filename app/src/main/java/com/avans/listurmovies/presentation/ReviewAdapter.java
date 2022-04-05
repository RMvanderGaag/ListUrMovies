package com.avans.listurmovies.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.listurmovies.R;
import com.avans.listurmovies.domain.review.Review;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final LayoutInflater mInflater;
    private List<Review> mReviews;
    private Context mContext;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MMM yyyy");

    public ReviewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review current = mReviews.get(position);
        holder.bindTo(current);
    }

    void setReviews(List<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mReviews != null)
            return mReviews.size();
        else return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView authorImage;
        private TextView authorName;
        private TextView lastUpdatedReview;
        private TextView rating;
        private TextView content;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            authorImage = itemView.findViewById(R.id.author_image);
            authorName = itemView.findViewById(R.id.author_name);
            lastUpdatedReview = itemView.findViewById(R.id.last_updated_review);
            rating = itemView.findViewById(R.id.author_rating);
            content = itemView.findViewById(R.id.review_content);
        }

        public void bindTo(Review currentReview) {
            //Author image
            String author_image = currentReview.getAuthor().getAvatar_path();
            if(author_image != null) {
                Glide.with(mContext).load(author_image.substring(1))
                        .placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(authorImage);
                //Author name
                authorName.setText(currentReview.getAuthor().getUsername());
                //Last updated review
                lastUpdatedReview.setText(mDateFormat.format(currentReview.getUpdatedAt()));
                //Rating
                if(currentReview.getAuthor().getRating() != 0.00) {
                    rating.setText("\u2605 " + currentReview.getAuthor().getRating());
                }
                //Review contet;
                content.setText(currentReview.getContent());
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
