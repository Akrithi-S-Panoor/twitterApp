package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tweet_photo
{
    private int tweet_id;
    private byte[] t_photo;
    public Tweet_photo(byte[] t_photo) {
        this.t_photo = t_photo;
    }
}
