package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisplayFeed
{
    private String userId;
    private String username;
    private byte[] photo;
    private String hashtag;
    private String description;
    private int likes;
    private byte[] t_photo;
    private String tweet_time;
    private String reply;
    private String reply_time;
    @JsonIgnore
    private int reply_likes;

    private byte[] r_photo;


    public DisplayFeed(String userId, String username, String hashtag, String description, int likes) {
        this.userId = userId;
        this.username = username;
        this.hashtag = hashtag;
        this.description = description;
        this.likes = likes;

    }

    public DisplayFeed(byte[] photo, byte[] t_photo)
    {
        this.photo = photo;
        this.t_photo = t_photo;
    }

    public DisplayFeed(String userId, String username, byte[] photo) {
        this.userId = userId;
        this.username = username;
        this.photo = photo;
    }

    public DisplayFeed(String userId, String username, byte[] photo, String hashtag, String description, int likes, String tweet_time) {
        this.userId = userId;
        this.username = username;
        this.photo = photo;
        this.hashtag = hashtag;
        this.description = description;
        this.likes = likes;
        this.tweet_time = tweet_time;
    }

    public DisplayFeed(String userId, String username, byte[] photo, String reply, String  reply_time, int reply_likes) {
        this.userId = userId;
        this.username = username;
        this.photo = photo;
        this.reply = reply;
        this.reply_time = reply_time;
        this.reply_likes = reply_likes;
    }

    public DisplayFeed(String userId, String username, byte[] photo, String hashtag, String description, int likes, byte[] t_photo) {
        this.userId = userId;
        this.username = username;
        this.photo = photo;
        this.hashtag = hashtag;
        this.description = description;
        this.likes = likes;
        this.t_photo = t_photo;
    }
}
