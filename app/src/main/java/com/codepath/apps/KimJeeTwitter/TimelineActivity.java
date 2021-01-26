package com.codepath.apps.KimJeeTwitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.KimJeeTwitter.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweetList;
    TweetsAdapter tweetsAdapter;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i(TAG, "fetching new data.");
                populateHomeTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // (1) Find recycler
        rvTweets = findViewById(R.id.rvTweets);

        // Init list of tweets & adapter
        tweetList = new ArrayList<Tweet>();

        tweetsAdapter = new TweetsAdapter(this, tweetList);

        Log.i(TAG, "rvTweets");
        // Recycler view setup:  layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);
        populateHomeTimeline();




    }


    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: Timeline was recieved successfully." + json.toString());

                JSONArray jsonArray =  json.jsonArray;
                try {
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(Tweet.fromJsonArray(jsonArray));
                    swipeContainer.setRefreshing(false);
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "JSON Exception", e);
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: Failed!" + response);
            }
        });
    }
}