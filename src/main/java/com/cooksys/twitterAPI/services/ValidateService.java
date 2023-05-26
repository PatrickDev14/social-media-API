package com.cooksys.twitterAPI.services;

public interface ValidateService {

    //CHECK IF USERNAME EXISTS
    Boolean usernameExists(String username);

    //VALIDATE IF USERNAME IS AVAILABLE
    Boolean usernameAvailable(String username);
}
