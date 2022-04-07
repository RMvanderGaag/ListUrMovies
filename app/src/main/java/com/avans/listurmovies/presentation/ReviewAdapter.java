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

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAuthorImage;
        private TextView mAuthorName;
        private TextView mLastUpdatedReview;
        private TextView mRating;
        private TextView mContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorImage = itemView.findViewById(R.id.author_image);
            mAuthorName = itemView.findViewById(R.id.author_name);
            mLastUpdatedReview = itemView.findViewById(R.id.last_updated_review);
            mRating = itemView.findViewById(R.id.author_rating);
            mContent = itemView.findViewById(R.id.review_content);
        }

        public void bindTo(Review currentReview) {
            //Author image
            String author_image = currentReview.getAuthor().getAvatar_path();
            if(author_image != null) {
                //Author image
                Glide.with(mContext).load(author_image.substring(1))
                        .placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(mAuthorImage);
                //Author name
                mAuthorName.setText(currentReview.getAuthor().getUsername());
                //Last updated review
                mLastUpdatedReview.setText(mDateFormat.format(currentReview.getUpdatedAt()));
                //Rating
                if(currentReview.getAuthor().getRating() != 0.00) {
                    mRating.setText("\u2605 " + currentReview.getAuthor().getRating());
                }
                //Review contet;
                mContent.setText(currentReview.getContent());
            }
        }
    }
}
