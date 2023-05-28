package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
    TweetResponseDto replyToTweet(TweetRequestDto tweetRequestDto, Long id);

    List<TweetResponseDto> getReposts(Long id);

    List<UserResponseDto> getMentions(Long id);

    List<TweetResponseDto> getAllSavedTweets();

    TweetResponseDto getTweet(Long id);

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDtoDto);


    void createATweetLike(Long id, CredentialsDto credentialsDto);

    TweetResponseDto softDeleteTweet(Long id, CredentialsDto credentialsDto);
}
