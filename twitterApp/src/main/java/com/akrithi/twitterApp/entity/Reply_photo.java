package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reply_photo
{
    private String reply_id;
    private byte[] r_photo;
    public Reply_photo(byte[] r_photo)
    {
        this.r_photo = r_photo;
    }
}
