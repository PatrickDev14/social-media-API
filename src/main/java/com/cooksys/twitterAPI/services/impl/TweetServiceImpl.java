package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl {

    private TweetRepository tweetRepository;
    private TweetMapper tweetMapper;

}
