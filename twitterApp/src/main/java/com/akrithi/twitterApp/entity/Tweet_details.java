package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tweet_details
{
    private  String reply_id;
    private int tweet_id;
    private String userId;
    private String reply;
    private String reply_time;
    private int reply_likes;

}
