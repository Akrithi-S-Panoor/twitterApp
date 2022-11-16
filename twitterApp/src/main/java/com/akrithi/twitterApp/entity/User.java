package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User
{
    public User(String userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    private String userId;
    private String username;
    private String email;
    private String password;
    private byte[] photo;

    public User(String userId, String username, byte[] photo)
    {
        this.userId = userId;
        this.username = username;
        this.photo = photo;
    }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public User(byte[] photo) {
        this.photo = photo;
    }
}
