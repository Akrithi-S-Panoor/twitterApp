package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tweet
{
    private int tweet_id;
    private String userId;
    private String hashtag;
    private String description;
    private String tweet_time;
    int likes;

    public Tweet(String hashtag, String description, String tweet_time, int likes)
    {
        this.hashtag = hashtag;
        this.description = description;
        this.tweet_time = tweet_time;
        this.likes = likes;
    }

    public Tweet(String userId, String hashtag, String description, String tweet_time, int likes) {
        this.userId = userId;
        this.hashtag = hashtag;
        this.description = description;
        this.tweet_time = tweet_time;
        this.likes = likes;
    }
}
