package com.codepath.apps.KimJeeTwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.KimJeeTwitter.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity
{
    public static  final String  TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    Button btnTweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        // Add a listener to the button
            // (1) Set a click Listener
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Error checking for 0 string, or 140+ string
                final String tweetContent = etCompose.getText().toString();
                Boolean valid_tweet = validateTweetContent(ComposeActivity.this, tweetContent);

                // (2) Make an API call to Twitter to publish the tweet
                if (valid_tweet == true)
                {
                    client.publishTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e(TAG, "onSuccess: JSONException has occurred", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                        Log.e(TAG, response, throwable);
                    }
                }, tweetContent);
                };
            }
        });


    }

    /*
    Validates the user's input for length of the tweet
    @params
        tweetContent -- string to be tweeted
    @ returns
        True -- if tweet is valid
        False -- if tweet is 0 or strictly greater than 140 characters.
    */
    private boolean validateTweetContent(ComposeActivity _this, String tweetContent)
    {
        if (tweetContent.length() == 0)
        {
            Toast.makeText(_this, "Tweet is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tweetContent.length() > MAX_TWEET_LENGTH)
        {
            Toast.makeText(_this, "Tweet is above 140 characters.", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(_this, "Tweet Sent.", Toast.LENGTH_LONG).show();
        return true;
    }




}

