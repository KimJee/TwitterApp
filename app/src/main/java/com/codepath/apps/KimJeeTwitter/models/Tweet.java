package com.codepath.apps.KimJeeTwitter.models;

import com.codepath.apps.KimJeeTwitter.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String body;
    public String createdAt;
    public String timeDifference;

    public User user;
    public static TimeFormatter timeFormatter = new TimeFormatter();

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.timeDifference = timeFormatter.getTimeDifference(tweet.createdAt);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        return tweet;
    }

    public  static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweetList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            tweetList.add((fromJson(jsonArray.getJSONObject(i))));
        }
        return tweetList;
    }


}
