package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
    TweetResponseDto replyToTweet(TweetRequestDto tweetRequestDto, Long id);

    List<TweetResponseDto> getReposts(Long id);

    List<UserResponseDto> getMentions(Long id);
}
