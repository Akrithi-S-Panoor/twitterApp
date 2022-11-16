package com.akrithi.twitterApp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Login
{
    private int login_id;
    private String userId;

}
