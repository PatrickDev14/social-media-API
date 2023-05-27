package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.HashtagDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {
    List<TweetResponseDto> getAllTweetsByHashtag(String label);
    
    List<HashtagDto> getAllHashtags();
}
