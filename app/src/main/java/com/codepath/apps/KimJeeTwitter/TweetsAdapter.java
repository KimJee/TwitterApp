package com.codepath.apps.KimJeeTwitter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.KimJeeTwitter.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";
    Context context;
    List<Tweet> tweetList;

    public TweetsAdapter(Context context, List<Tweet> tweetList) {
        Log.i(TAG, "TweetsAdapter: constructor()");
        this.context = context;
        this.tweetList = tweetList;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Then bind that layout
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("TweetsAdapter", "onBindViewHolder");
        // Grabs data
        Tweet tweet = tweetList.get(position);
        // Binds tweet with view holder
        holder.bind(tweet);
    }

    // Then bind that layout
    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweetList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "ViewHolder";

        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvTweet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // Representation of one "row" in the recycler view.

            Log.i(TAG, "Viewholder's constructor");
            // Reference to the actual view
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTweet = itemView.findViewById(R.id.tvTweet);
        }

        public void bind(Tweet tweet)
        {
            Log.i("ViewHolder", "bind: Binding to the image");
            tvTweet.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileimageUrl).into(ivProfileImage);
        }
    }
}
